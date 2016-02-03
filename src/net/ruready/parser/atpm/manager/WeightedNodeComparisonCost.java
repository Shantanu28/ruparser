/*****************************************************************************************
 * Source File: WeightedNodeComparisonCost.java
 ****************************************************************************************/
package net.ruready.parser.atpm.manager;

import java.util.HashMap;
import java.util.Map;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default cost computation for the edit distance algorithm.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 16, 2007
 */
public class WeightedNodeComparisonCost implements NodeComparisonCost<MathToken>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(WeightedNodeComparisonCost.class);

	// Default costs of different tree node comparison outcomes
	private static final Map<CostType, Double> defaultCostMap = new HashMap<CostType, Double>();

	static
	{
		// Include only the supported cost types -- need not be all
		defaultCostMap.put(CostType.INSERT_DELETE,
				ParserNames.OPTIONS.EDIT_DISTANCE.COST.INSERT_DELETE);
		// defaultCostMap
		// .put(CostType.DELETE, ParserNames.OPTIONS.EDIT_DISTANCE.COST.DELETE);
		// Cost of finding two labels to be equal. Is normally 0.
		defaultCostMap.put(CostType.EQUAL, ParserNames.OPTIONS.EDIT_DISTANCE.COST.EQUAL);
		defaultCostMap.put(CostType.UNEQUAL_SAME_TYPE,
				ParserNames.OPTIONS.EDIT_DISTANCE.COST.UNEQUAL_SAME_TYPE);
		defaultCostMap.put(CostType.UNEQUAL_SAME_TYPE_OPERATION,
				ParserNames.OPTIONS.EDIT_DISTANCE.COST.UNEQUAL_SAME_TYPE_OPERATION);
		defaultCostMap.put(CostType.UNEQUAL_DIFFERENT_TYPE,
				ParserNames.OPTIONS.EDIT_DISTANCE.COST.UNEQUAL_DIFFERENT_TYPE);
		defaultCostMap.put(CostType.UNEQUAL_DIFFERENT_TYPE_OPERATION,
				ParserNames.OPTIONS.EDIT_DISTANCE.COST.UNEQUAL_DIFFERENT_TYPE_OPERATION);
		defaultCostMap.put(CostType.UNEQUAL_FICTITIOUS,
				ParserNames.OPTIONS.EDIT_DISTANCE.COST.UNEQUAL_FICTITIOUS);
	}

	// ========================= FIELDS ====================================

	// ===============================
	// Input fields
	// ===============================

	// Numerical token comparison relative tolerance
	private final double tol;

	// Holds custom costs of different outcomes of token comparisons
	private final Map<CostType, Double> costMap;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Prepares a weighted edit distance cost function computer.
	 * 
	 * @param tol
	 *            numerical token relative comparison tolerance
	 */
	public WeightedNodeComparisonCost(final double tol)
	{
		this.tol = tol;

		// Initialize cost map using defaults
		this.costMap = WeightedNodeComparisonCost.defaultCostMap;
		logger.debug("Initialized with default cost map " + costMap);
	}

	/**
	 * Prepares a weighted edit distance cost function computer with custom costs.
	 * 
	 * @param tol
	 *            numerical token relative comparison tolerance
	 * @param costMap
	 *            custom cost map (cost type -> cost)
	 */
	public WeightedNodeComparisonCost(final double tol,
			final Map<CostType, Double> costMap)
	{
		super();
		this.tol = tol;
		this.costMap = costMap;
		logger.debug("Initialized with custom cost map " + costMap);
	}

	// ========================= IMPLEMENTATION: NodeComparisonCost ============

	/**
	 * Returns the cost type corresponding to the outcome of comparing two tree node
	 * labels (tokens), according to the following rules (applied in their order of
	 * apperance below):
	 * <p> - If at least one of the tokens is null, returns
	 * <code>UNEQUAL_DIFFERENT_TYPE</code>.
	 * <p> - If the labels have the same type (ID):
	 * <ul>
	 * <li>If the labels are tolerantly equal (either numerically, if they are numerical,
	 * otherwise according to their natural equality, which is usually their string
	 * representation equality), return <code>EQUAL</code>.</li>
	 * <li>Otherwise, return <code>UNEQUAL_SAME_TYPE_OPERATION</code> if the tokens are
	 * operations, otherwise return <code>UNEQUAL_SAME_TYPE</code>.</li>
	 * </ul>
	 * <p> - If exactly one of the tokens is fictitious, returns
	 * <code>UNEQUAL_FICTITIOUS</code>.
	 * <p> - If the labels don't have the same type, return
	 * <code>UNEQUAL_DIFFERENT_TYPE_OPERATION</code> if one of them is an operation,
	 * otherwise return <code>UNEQUAL_DIFFERENT_TYPE</code>.
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost type of the outcome of comparing two tree node labels.
	 */
	public CostType getComparisonCostType(MathToken tokenLeft, MathToken tokenRight)
	{
		// logger.debug("Comparing tokenLeft " + tokenLeft + " tokenRight " +
		// tokenRight);

		// =========================================================
		// At least one node is null, they are not equal.
		// If both are null, they can't be equal either.
		// =========================================================
		if ((tokenLeft == null) || (tokenRight == null))
		{
			// logger.debug("Result: " + CostType.UNEQUAL_DIFFERENT_TYPE);
			return CostType.UNEQUAL_DIFFERENT_TYPE;
		}

		// =========================================================
		// If tokens have equal IDs:
		// - If they are equal (if numerical, tolerantely compare the
		// numerical values; otherwise, compare values), return EQUAL.
		// - Otherwise, return UNEQUAL_OPERATION (if operations),
		// or UNEQUAL_SAME_TYPE (if they are not operations).
		// =========================================================

		if (tokenLeft.getValueID() == tokenRight.getValueID())
		{
			boolean tokensEqual;
			if (tokenLeft.isNumerical())
			{
				// Compare numerical values
				NumericalValue valueLeft = (NumericalValue) tokenLeft.getValue();
				NumericalValue valueRight = (NumericalValue) tokenRight.getValue();
				tokensEqual = (valueLeft.tolerantlyEquals(valueRight, tol) == TolerantlyComparable.EQUAL);
				// logger.debug("Numerical value comparison; equals? " +
				// tokensEqual);
			}
			else
			{
				// Compare values
				tokensEqual = tokenLeft.getValue().equals(tokenRight.getValue());
				// logger.debug("Non-numerical value comparison; equals? " +
				// tokensEqual);
			}

			// Decide what to return based on tokensEqual and whether the tokens
			// are operations or not.
			// logger
			// .debug("Result: "
			// + ((tokensEqual) ? CostType.EQUAL
			// : (tokenLeft.isArithmeticOperation() ?
			// CostType.UNEQUAL_SAME_TYPE_OPERATION
			// : CostType.UNEQUAL_SAME_TYPE)));
			return (tokensEqual) ? CostType.EQUAL
					: (tokenLeft.isArithmeticOperation() ? CostType.UNEQUAL_SAME_TYPE_OPERATION
							: CostType.UNEQUAL_SAME_TYPE);
		}

		// =========================================================
		// Tokens are of different types;
		// One node is fictitious and the other is not
		// =========================================================
		if ((tokenLeft.getStatus() == MathTokenStatus.FICTITIOUS_CORRECT)
				^ (tokenRight.getStatus() == MathTokenStatus.FICTITIOUS_CORRECT))
		{
			// logger.debug("Result: " + CostType.UNEQUAL_FICTITIOUS);
			return CostType.UNEQUAL_FICTITIOUS;
		}

		// =========================================================
		// Tokens are of different types + same fictitiousness
		// =========================================================
		// Decide what to return based on whether the tokens are operations or
		// not.
		// logger.debug("arithmetic ops? " + tokenLeft.isArithmeticOperation() +
		// " "
		// + tokenRight.isArithmeticOperation());
		boolean isOperation = (tokenLeft.isArithmeticOperation() || tokenRight
				.isArithmeticOperation());
		// logger.debug("Result: "
		// + (isOperation ? CostType.UNEQUAL_DIFFERENT_TYPE_OPERATION
		// : CostType.UNEQUAL_DIFFERENT_TYPE));
		return (isOperation ? CostType.UNEQUAL_DIFFERENT_TYPE_OPERATION
				: CostType.UNEQUAL_DIFFERENT_TYPE);
	}

	/**
	 * Returns the cost corresponding to the outcome of comparing two tree node labels
	 * (tokens). Uses the {@link #getComparisonCost(MathToken, MathToken)} method to get
	 * the cost type, and the <code>costMap</code> field to return the
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost of the outcome of comparing two tree node labels.
	 */
	public double getComparisonCost(MathToken tokenLeft, MathToken tokenRight)
	{
		return this.costMap.get(this.getComparisonCostType(tokenLeft, tokenRight));
	}

	/**
	 * Returns the cost of inserting a new node into a tree or deleting a node from a
	 * tree.
	 * 
	 * @return the cost of inserting a new node into a tree or deleting a node from a tree
	 */
	public double getInsertDeleteCost()
	{
		return this.costMap.get(CostType.INSERT_DELETE);
	}

	// /**
	// * Returns the cost of deleting a node from a tree.
	// *
	// * @return the cost of deleting a node from a tree.
	// */
	// public double getDeleteCost()
	// {
	// return this.costMap.get(CostType.DELETE);
	// }

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return Returns the tol.
	 */
	public double getTol()
	{
		return tol;
	}

	/**
	 * @return the costMap
	 */
	public Map<CostType, Double> getCostMap()
	{
		return costMap;
	}

	/**
	 * @return the defaultCostMap
	 */
	public static Map<CostType, Double> getDefaultCostMap()
	{
		return defaultCostMap;
	}

}
