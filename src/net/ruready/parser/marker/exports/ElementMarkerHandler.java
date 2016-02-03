/*******************************************************
 * Source File: ElementMarkerHandler.java
 *******************************************************/
package net.ruready.parser.marker.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.analysis.entity.AnalysisID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler runs the element marker on a refernce + response pair.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
@Deprecated
public class ElementMarkerHandler extends MarkerHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ElementMarkerHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * @param attributeNameResult
	 */
	public ElementMarkerHandler(String attributeNameResult)
	{
		super(AnalysisID.ELEMENT, attributeNameResult);
	}

	/**
	 * @param attributeNameResult
	 * @param nextNode
	 */
	public ElementMarkerHandler(String attributeNameResult, RequestHandler nextNode)
	{
		super(AnalysisID.ELEMENT, attributeNameResult, nextNode);
	}

	// ========================= IMPLEMENTATION: ATPMEditDistanceHandler
	// ===========

	// /**
	// * @see
	// net.ruready.parser.analysis.exports.MarkerHandler#processTargets(net.ruready.parser.arithmetic.entity.ArithmeticTarget,
	// * net.ruready.parser.arithmetic.entity.ArithmeticTarget)
	// */
	// @Override
	// protected void processTargets(ParametricEvaluationTarget referenceTarget,
	// ParametricEvaluationTarget responseTarget)
	// {
	// // Set and run the marker
	// marker = new MarkerFactory().createType(NodeMatchType.ELEMENT,
	// referenceTarget,
	// responseTarget);
	// marker.compare();
	// }

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return "Element marker";
	}
}
