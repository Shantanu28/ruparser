/*******************************************************
 * Source File: AutoAnalysisHandler.java
 *******************************************************/
package net.ruready.parser.analysis.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.marker.exports.MarkerHandler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A prepared parser processing chain that decides which analysis phase to run
 * based on the parser options at run time, including a marker and possibly
 * relative canonicalization steps on a pair of reference + response targets.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 17, 2007
 */
public class AutoAnalysisHandler extends RequestHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AutoAnalysisHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an arithmetic grammar matching processor.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name of the arithmetic target to process
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this processor
	 *            is completed. If <code>null</code>, the request processing
	 *            chain will terminate after this handler handles the request.
	 */
	public AutoAnalysisHandler(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@Override
	final protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		ParserRequest parserRequest = (ParserRequest) request;
		ParserOptions options = parserRequest.getOptions();

		// ====================================================
		// Business logic
		// ====================================================

		RequestHandler dynamicNextNode = null;
		switch (options.getAnalysisID())
		{
			case ELEMENT:
			{
				dynamicNextNode = new MarkerHandler(AnalysisID.ELEMENT,
						ParserNames.REQUEST.ATTRIBUTE.MARKER.ELEMENTS_RESULT, this
								.getNextNode());
				break;
			}

			case ATPM:
			{
				// Decide where to forward the request next based on the options
				dynamicNextNode = new ATPMAnalysisLoop(this.getNextNode());
				break;
			}
		}

		this.setNextNode(dynamicNextNode);

		// ====================================================
		// Attach outputs to the request
		// ====================================================

		return false;
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
		return ParserNames.HANDLER.PROCESSOR.NAME + " "
				+ "Automatically Detected Marker Type";
	}
}
