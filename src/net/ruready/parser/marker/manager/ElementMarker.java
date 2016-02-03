/*****************************************************************************************
 * Source File: ElementMarker.java
 ****************************************************************************************/
package net.ruready.parser.marker.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.atpm.manager.ElementEditDistanceComputer;
import net.ruready.parser.atpm.manager.NodeComparisonCost;
import net.ruready.parser.atpm.manager.WeightedNodeComparisonCost;
import net.ruready.parser.marker.entity.MarkerUtil;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenComparatorByValue;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Mark a response math expression target using the elements method. It simply compares
 * the list of tokens with the reference tree token list of the same type, marks them
 * accordingly, and counts how many tokens of each type exist.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 25, 2007
 */
class ElementMarker implements Marker
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ElementMarker.class);

	// ========================= FIELDS ====================================

	// Reference target
	private MathTarget referenceTarget;

	// Response target
	private MathTarget responseTarget;

	// Convenient local variables
	private List<MathToken> referenceTokens;

	private List<MathToken> responseTokens;

	private List<List<MathToken>> referenceBinnedTokens;

	private List<List<MathToken>> responseBinnedTokens;

	private final EditDistanceComputer<MathToken, SyntaxTreeNode> editDistanceComputer;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an element marker.
	 * 
	 * @param options
	 *            control options object
	 * @param reference
	 *            reference target
	 * @param response
	 *            the response target to be analyzed
	 */
	public ElementMarker(final ParserOptions options, MathTarget referenceTarget,
			MathTarget responseTarget)
	{
		this.referenceTarget = referenceTarget;
		this.responseTarget = responseTarget;

		// Initialize
		referenceTokens = referenceTarget.toMathTokenArray();
		responseTokens = responseTarget.toMathTokenArray();
		referenceBinnedTokens = ElementMarker.binTokens(referenceTokens);
		responseBinnedTokens = ElementMarker.binTokens(responseTokens);

		logger.debug("referenceBinnedTokens = " + referenceBinnedTokens);
		logger.debug("responseBinnedTokens  = " + responseBinnedTokens);

		// Cache edit distance computation objects
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

		this.editDistanceComputer = new ElementEditDistanceComputer<MathToken, SyntaxTreeNode>(
				this, costComputer);
	}

	/**
	 * Construct an element marker without options.
	 * 
	 * @param reference
	 *            reference target
	 * @param response
	 *            the response target to be analyzed
	 */
	public ElementMarker(final MathTarget referenceTarget, final MathTarget responseTarget)
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
		return AnalysisID.ELEMENT;
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
	 * Compare this result object to a reference object using elements.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#compare()
	 */
	public void compare()
	{
		logger.debug("compare()");

		// Loop over math token types and compare the corresponding reference
		// and response lists separately for each type.
		// Note: discarded tokens are not part of the MathTokens list of an
		// expression, so these reference and response lists are empty and dummy
		// here.
		for (MathValueID id : MathValueID.values())
		{
			logger.debug("<<<<< Comparing tokens of type " + id + " >>>>>");
			ElementMarker.compareTokens(referenceBinnedTokens.get(id.getValue()),
					responseBinnedTokens.get(id.getValue()));
		}
	}

	/**
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
	 * The distance between the trees is defined as e, where e = # errors = #missing + #
	 * unrecognized + #wrong.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#getDistance()
	 */
	public double getDistance()
	{
		// return 1.0 * MarkerUtil.getNumErrors(this);
		return editDistanceComputer.getEditDistance();
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
					"No elements found for correct element fraction computation");
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
		// The element marker does not use an ED computer
		return null;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Bin a <code>MathToken</code> list into specific lists for each token type. All
	 * math tokens whose status is negative but not discarded (e.g. fictitious, redundant)
	 * are ignored and not added to the lists.
	 * 
	 * @param tokens
	 *            list of tokens
	 */
	@SuppressWarnings("unused")
	private static List<List<MathToken>> binTokens(List<MathToken> tokens)
	{
		logger.debug("binTokens(tokens = " + tokens + ")");

		// Initialize lists of all token types to be empty yet non-null.
		// The index of a MathValueID in the super-list is its value because
		// MathValueID values are running and 0-based.
		List<List<MathToken>> parts = new ArrayList<List<MathToken>>();
		for (MathValueID t : MathValueID.values())
		{
			parts.add(new ArrayList<MathToken>());
		}

		// Bin tokens into lists. Note that MathValueID.value is a 0-based
		// running index, so we use it to find the appropriate list for each
		// token.
		for (MathToken mt : tokens)
		{
			if (mt.getStatus().getValue() >= MathTokenStatus.DISCARDED.getValue())
			{
				parts.get(mt.getValueID().getValue()).add(mt);
			}
		}

		return parts;
	}

	/**
	 * Compare a reference and response <code>MathToken</code> lists of the same token
	 * type and mark their status accordingly.
	 * 
	 * @param tokens
	 *            list of tokens
	 */
	private static void compareTokens(List<MathToken> referenceTokens,
			List<MathToken> responseTokens)
	{
		// logger.debug("compareTokens()");
		// Optimization: if both lists are empty, skip
		if (referenceTokens.isEmpty() && responseTokens.isEmpty())
		{
			return;
		}
		// First, mark all ref tokens as missing and all response tokens
		// as unrecognized
		MarkerUtil.resetTokens(referenceTokens, responseTokens);

		// A pointer to an element in the response token list
		int responseIndex = 0;
		MathTokenComparatorByValue comparator = new MathTokenComparatorByValue();

		// Loop over reference tokens and attempt to find counterparts
		// in the response token list. If found, update the status of both
		// tokens. Because the lists are assumed to be sorted, we can iterate
		// sequentially over the response list (using responseIndex). This is an
		// O(n+m) algorithm where m = # reference elements and n = # response
		// elements, not O(n*m).
		for (MathToken referenceToken : referenceTokens)
		{
			logger.debug("Considering ref token " + referenceToken + " responseIndex "
					+ responseIndex);
			while (responseIndex < responseTokens.size())
			{
				MathToken responseToken = responseTokens.get(responseIndex);
				// Note: The following MathToken equality ignores the tokens'
				// element indices and status. It compares based on values only.
				// This is a major drawback of the element marker.
				int comparisonResult = comparator.compare(referenceToken, responseToken);
				logger.debug("Comparing ref token " + referenceToken
						+ " with response token " + responseToken + " result "
						+ comparisonResult);
				if (comparisonResult == 0)
				{
					// Do not mark both as correct, because this will double
					// count correct elements. Simply discard the reference
					// token.
					logger.debug("Found a match:");
					logger.debug("Marking reference token " + referenceToken
							+ " as discarded");
					logger.debug("Marking response  token " + responseToken
							+ " as correct");
					referenceToken.setStatus(MathTokenStatus.DISCARDED);
					responseToken.setStatus(MathTokenStatus.CORRECT);
					break;
				}
				else if (comparisonResult < 0)
				{
					// Passed the point in the response array where potential
					// matches
					// for this reference token exist. Stop searching.
					break;
				}
				responseIndex++;
			}
		}
	}

	// ========================= GETTERS & SETTERS =========================

}
