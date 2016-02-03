/*******************************************************
 * Source File: SimpleNodeComparisonCost.java
 *******************************************************/
package net.ruready.parser.atpm.manager;

import java.util.HashMap;
import java.util.Map;

import net.ruready.parser.atpm.entity.CostType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple cost computation for the edit distance algorithm. Relies on the
 * natural comparison method of the two label objects.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public class SimpleNodeComparisonCost<E> implements NodeComparisonCost<E>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ShashaEditDistanceComputer.class);

	// Default costs of different tree node comparison outcomes
	// TODO: make those parameters of the cost computer
	private static final Map<CostType, Double> defaultCostMap = new HashMap<CostType, Double>();

	static {
		// Include only the supported cost types -- need not be all
		defaultCostMap.put(CostType.INSERT_DELETE, 1.0);
		// defaultCostMap.put(CostType.DELETE, 1.0);
		// Cost of finding two labels to be equal. Is normally 0.
		defaultCostMap.put(CostType.EQUAL, 0.0);
		defaultCostMap.put(CostType.UNEQUAL_SAME_TYPE, 1.0);
	}

	// ========================= FIELDS ====================================

	// ===============================
	// Input fields
	// ===============================

	// Holds custom costs of different outcomes of token comparisons
	private final Map<CostType, Double> costMap;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Prepares a simple edit distance cost function computer based on label
	 * object equality.
	 */
	public SimpleNodeComparisonCost()
	{
		// TODO: optionally pass this as a parameter to this constructor
		// Initialize cost map using defaults
		this.costMap = SimpleNodeComparisonCost.defaultCostMap;
	}

	/**
	 * Prepares a simple edit distance cost function computer with custom costs.
	 * 
	 * @param costMap
	 *            custom cost map (cost type -> cost)
	 */
	public SimpleNodeComparisonCost(final Map<CostType, Double> costMap)
	{
		super();
		this.costMap = costMap;
	}

	// ========================= IMPLEMENTATION: NodeComparisonCost ============

	/**
	 * Returns the cost type corresponding to the outcome of comparing two tree
	 * node labels (tokens).
	 * <p> - If at least one of the tokens is null, returns <code>RELABEL</code>.
	 * <p> - If the labels are equal strings, <code>EQUAL</code>.
	 * <p> - Otherwise, returns <code>RELABEL</code>.
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost type of the outcome of comparing two tree node labels.
	 */
	public CostType getComparisonCostType(E tokenLeft, E tokenRight)
	{
		// =========================================================
		// At least one node is null, they are not equal.
		// If both are null, they can't be equal either.
		// =========================================================
		if ((tokenLeft == null) || (tokenRight == null)) {
			return CostType.UNEQUAL_SAME_TYPE;
		}

		// =========================================================
		// Compare stings
		// =========================================================
		return tokenLeft.equals(tokenRight) ? CostType.EQUAL : CostType.UNEQUAL_SAME_TYPE;
	}

	/**
	 * Returns the cost corresponding to the outcome of comparing two tree node
	 * labels (tokens). Uses the {@link #getComparisonCost(E, E)} method to get
	 * the cost type, and the <code>costMap</code> field to return the
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost of the outcome of comparing two tree node labels.
	 */
	public double getComparisonCost(E tokenLeft, E tokenRight)
	{
		return this.costMap.get(this.getComparisonCostType(tokenLeft, tokenRight));
	}

	/**
	 * Returns the cost of inserting a new node into a tree or deleting a node
	 * from a tree.
	 * 
	 * @return the cost of inserting a new node into a tree or deleting a node
	 *         from a tree.
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
	 * @return the costMap
	 */
	public Map<CostType, Double> getCostMap()
	{
		return costMap;
	}
}
