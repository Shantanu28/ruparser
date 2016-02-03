/*******************************************************
 * Source File: ATPMMarkerHandler.java
 *******************************************************/
package net.ruready.parser.marker.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.marker.manager.MarkerFactory;
import net.ruready.parser.math.entity.MathTarget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler runs the ATPM marker on a refernce + response pair.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class ATPMMarkerHandler extends MarkerHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ATPMMarkerHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * @param attributeNameResult
	 */
	public ATPMMarkerHandler(String attributeNameResult)
	{
		super(AnalysisID.ATPM, attributeNameResult);
	}

	/**
	 * @param attributeNameResult
	 * @param nextNode
	 */
	public ATPMMarkerHandler(String attributeNameResult, RequestHandler nextNode)
	{
		super(AnalysisID.ATPM, attributeNameResult, nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return "ATPM Marker";
	}

	// ========================= IMPLEMENTATION: ATPMEditDistanceHandler
	// ===========

	/**
	 * @see net.ruready.parser.marker.exports.MarkerHandler#processTargets(net.ruready.parser.math.entity.MathTarget,
	 *      net.ruready.parser.math.entity.MathTarget)
	 */
	@Override
	protected void processTargets(MathTarget referenceTarget,
			MathTarget responseTarget)
	{
		// Set and run the marker using the parser options found in the
		// request
		marker = new MarkerFactory().createType(this.getAnalysisID(), this.options,
				referenceTarget, responseTarget);
		marker.compare();
	}
}
