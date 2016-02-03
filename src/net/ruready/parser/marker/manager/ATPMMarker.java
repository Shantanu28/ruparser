/*****************************************************************************************
 * Source File: ATPMMarker.java
 ****************************************************************************************/
package net.ruready.parser.marker.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.atpm.entity.NodeMapping;
import net.ruready.parser.atpm.entity.NodeMatch;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.atpm.manager.NodeComparisonCost;
import net.ruready.parser.atpm.manager.ShashaEditDistanceComputer;
import net.ruready.parser.atpm.manager.WeightedNodeComparisonCost;
import net.ruready.parser.marker.entity.MarkerUtil;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Mark a response math expression target using the Approximate Tree Pattern Matching
 * (ATPM) algorithm. Currently implemented for arithmetic expressions only, it computes
 * the edit distance between the two syntax trees, and uses the assoicated nodal mapping
 * to mark {@link MathToken}s of both trees (set their status).
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
class ATPMMarker implements Marker
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ATPMMarker.class);

	// ========================= FIELDS ====================================

	// ===============================
	// Input fields
	// ===============================

	// Parser control options, containing a custom cost map
	private final ParserOptions options;

	// Reference target
	private final MathTarget referenceTarget;

	// Response target
	private final MathTarget responseTarget;

	// ===============================
	// Convenient local variables
	// ===============================

	// List of math tokens of both targets
	private List<MathToken> referenceTokens;

	private List<MathToken> responseTokens;

	// ===============================
	// Output fields
	// ===============================

	// ATPM edit distance
	private double editDistance;

	// ED computer
	private EditDistanceComputer<MathToken, SyntaxTreeNode> editDistanceComputer;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an ATPM marker.
	 * 
	 * @param options
	 *            control options object
	 * @param reference
	 *            reference target
	 * @param response
	 *            the response target to be analyzed
	 */
	public ATPMMarker(final ParserOptions options, final MathTarget referenceTarget,
			final MathTarget responseTarget)
	{
		this.options = options;
		this.referenceTarget = referenceTarget;
		this.responseTarget = responseTarget;

		// Initialize
		referenceTokens = referenceTarget.toMathTokenArray();
		responseTokens = responseTarget.toMathTokenArray();

		logger.debug("referenceTokens = " + referenceTokens);
		logger.debug("responseTokens  = " + responseTokens);
	}

	/**
	 * Construct an ATPM marker without options.
	 * 
	 * @param reference
	 *            reference target
	 * @param response
	 *            the response target to be analyzed
	 */
	public ATPMMarker(final MathTarget referenceTarget, final MathTarget responseTarget)
	{
		this(null, referenceTarget, responseTarget);
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public AnalysisID getIdentifier()
	{
		return AnalysisID.ATPM;
	}

	// ========================= IMPLEMENTATION: Marker ===========

	/**
	 * @see net.ruready.parser.marker.manager.Marker#getReferenceTarget()
	 */
	public MathTarget getReferenceTarget()
	{
		return referenceTarget;
	}

	/**
	 * @see net.ruready.parser.marker.manager.Marker#getResponseTarget()
	 */
	public MathTarget getResponseTarget()
	{
		return responseTarget;
	}

	/**
	 * Compare this result object to a reference object using the ATPM algorithm.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#compare()
	 */
	public void compare()
	{
		logger.debug("compare()");

		// =============================================
		// Compute edit distance and tree->tree mapping
		// =============================================
		SyntaxTreeNode referenceTree = referenceTarget.getSyntax();
		SyntaxTreeNode responseTree = responseTarget.getSyntax();

		NodeComparisonCost<MathToken> costComputer = null;
		if (options == null)
		{
			// Use the comparison tolerance of the response target object
			costComputer = new WeightedNodeComparisonCost(responseTarget
					.getPrecisionTol());
		}
		else
		{
			// Use the comparison tolerance & cost map of the options object
			costComputer = new WeightedNodeComparisonCost(options.getPrecisionTol(),
					options.getCostMap());
		}

		editDistanceComputer = new ShashaEditDistanceComputer<MathToken, SyntaxTreeNode>(
				referenceTree, responseTree, costComputer);
		logger.debug(editDistanceComputer);

		// Save results in fields
		editDistance = editDistanceComputer.getEditDistance();

		// =============================================
		// Set nodes' stati using nodal mapping
		// =============================================
		this.setTokenStati(costComputer, editDistanceComputer.getMapping());

		logger.debug("After updating token stati:");
		logger.debug(editDistanceComputer);
	}

	/**
	 * Return the number of elements with a specified status. Counts both in the
	 * <code>refTokens</code> and <code>responseTokens</code> arrays as well as in the
	 * response target's extraneous token list.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#getNumElements(net.ruready.parser.math.entity.MathTokenStatus)
	 */
	public int getNumElements(MathTokenStatus status)
	{
		// Compute number of elements on the fly from the marked token lists.
		int count = 0;
		// See the general contract of MathTokenStatus values
		if (status.getValue() >= 0)
		{
			// The token belongs to the main syntax tree.
			// Note that we search for tokens that match this status in BOTH
			// lists.
			for (MathToken mt : referenceTokens)
			{
				if (mt.getStatus() == status)
				{
					count++;
				}
			}
			for (MathToken mt : responseTokens)
			{
				if (mt.getStatus() == status)
				{
					count++;
				}
			}
		}
		else
		{
			// This is an extraneous token; search only in the response's
			// extraneous list because there may be redundant elements in the
			// reference that should not be counted. Same for discarded
			// elements.
			for (MathToken mt : responseTarget.getExtraneous())
			{
				if (mt.getStatus() == status)
				{
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * @see net.ruready.parser.marker.manager.Marker#getNumElementMap()
	 */
	public Map<MathTokenStatus, Integer> getNumElementMap()
	{
		Map<MathTokenStatus, Integer> elementCountMap = new HashMap<MathTokenStatus, Integer>();
		for (MathTokenStatus status : MathTokenStatus.values())
		{
			elementCountMap.put(status, this.getNumElements(status));
		}
		return elementCountMap;
	}

	/**
	 * Returns the ATPM edit distance between the syntax trees.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#getDistance()
	 */
	public double getDistance()
	{
		return editDistance;
	}

	/**
	 * The correct element fractions is defined as c/(c+e) where c = # correct, e = #
	 * errors = #missing + # unrecognized + #wrong.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#getDistance()
	 */
	public double getCorrectElementFraction()
	{
		int c = this.getNumElements(MathTokenStatus.CORRECT);
		int e = MarkerUtil.getNumErrors(this);

		if (c + e == 0)
		{
			// This shouldn't happen because there are always marked elements
			// for all expressions in the parser's language
			throw new IllegalStateException(
					"No elements found for edit distance computation");
		}

		// logger.debug("c " + c + " e " + e);
		return (1.0 * c) / (c + e);
	}

	/**
	 * @see net.ruready.parser.marker.manager.Marker#getScore()
	 */
	public double getScore()
	{
		// The element marker's [sub-total] score for the response is simply
		// taken to be the fraction of correct elements, although this can in
		// principle be replaced with something else.
		// Note: the score scale is [0,100].
		return 100.0 * this.getCorrectElementFraction();
	}

	/**
	 * @see net.ruready.parser.marker.manager.Marker#getEditDistanceComputer()
	 */
	public EditDistanceComputer<MathToken, SyntaxTreeNode> getEditDistanceComputer()
	{
		return editDistanceComputer;
	}

	// ========================= PRIVATE METHODS ===========================
	/**
	 * Uses the nodal mapping to set the status of reference and response tokens.
	 * 
	 * @param costComputer
	 *            a cost computer used to [tolerantely] compare nodes to decide on their
	 *            stati
	 * @param mapping
	 *            tree-to-tree nodal mapping whose elements reference the reference and
	 *            response tokens
	 */
	private void setTokenStati(NodeComparisonCost<MathToken> costComputer,
			NodeMapping<MathToken, SyntaxTreeNode> mapping)
	{
		// logger.debug("setTokenStati()");
		// First, mark all ref tokens as missing and all response tokens
		// as unrecognized
		MarkerUtil.resetTokens(referenceTokens, responseTokens);

		// Loop over mapping full elements (for which both left and right are
		// non-null) and set the corresponding tokens' stati

		for (NodeMatch<MathToken, SyntaxTreeNode> nodeMatch : mapping)
		{
			if (nodeMatch.isFull())
			{
				MathToken referenceToken = nodeMatch.getLeftData();
				MathToken responseToken = nodeMatch.getRightData();

				// Decide what to do based on cost computer's node comparison
				CostType costType = costComputer.getComparisonCostType(referenceToken,
						responseToken);
				// logger
				// .debug("Updating status: reference token " + referenceToken
				// + " response token " + responseToken + " comparison: "
				// + costType);

				switch (costType)
				{
					case EQUAL:
					{
						// =============================================
						// Nodes are equal
						// =============================================
						// Do not mark both as correct, because this will double
						// count correct elements. Simply discard the reference
						// token.
						referenceToken.setStatus(MathTokenStatus.DISCARDED);
						responseToken.setStatus(MathTokenStatus.CORRECT);
						// logger.debug("Marking reference token " + referenceToken
						// + " as discarded");
						// logger.debug("Marking response token " + responseToken
						// + " as correct");
						break;
					}

					case UNEQUAL_FICTITIOUS:
					{
						// =============================================
						// One node is fictitious and the other is not
						// =============================================
						// Do nothing, nodes already marked by resetTokens()
						break;
					}

					case UNEQUAL_SAME_TYPE:
					case UNEQUAL_SAME_TYPE_OPERATION:
					{
						// =============================================
						// Nodes are unequal but of the same type
						// =============================================
						referenceToken.setStatus(MathTokenStatus.DISCARDED);
						responseToken.setStatus(MathTokenStatus.WRONG);
						// logger.debug("Marking reference token " + referenceToken
						// + " as discarded");
						// logger.debug("Marking response token " + responseToken
						// + " as wrong");
						break;
					}

					case UNEQUAL_DIFFERENT_TYPE:
					case UNEQUAL_DIFFERENT_TYPE_OPERATION:
					{
						// =============================================
						// Nodes are unequal and have different types
						// =============================================
						// Do nothing, nodes already marked by resetTokens()
						break;
					}

					default:
					{
						throw new IllegalStateException(
								"We shouldn't be here with costType = " + costType);
					}
				} // switch (costType)
			} // if (nodeMatch.isFull())
		} // for (NodeMatch<MathToken> nodeMatch : mapping)
	} // setTokenStati()

	// ========================= GETTERS & SETTERS =========================

}
