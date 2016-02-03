/*****************************************************************************************
 * Source File: RCEndLoop.java
 ****************************************************************************************/
package net.ruready.parser.analysis.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.HandlerMessage;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.misc.Auxiliary;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler is called at the end of the RC loop. It checks whether the stopping
 * criterion has been met; if yes, the loop is terminated.
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
public class RCEndLoop extends ParserHandler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RCEndLoop.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a canonicalization step handler.
	 * 
	 * @param name
	 *            Name of this canonicalization step
	 */
	public RCEndLoop()
	{
		super();
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param name
	 *            Name of this canonicalization step
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public RCEndLoop(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= ABSTRACT METHODS ==========================

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
		// ParserRequest parserRequest = (ParserRequest) request;
		// Assumes that all variables in the options object are
		// numerical and none are symbolic (null-valued)
		// ParserOptions options = parserRequest.getOptions();
		int counter = (Integer) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.COUNTER);

		int minIterations = (Integer) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.MIN_ITERATIONS);
		int maxIterations = (Integer) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.MAX_ITERATIONS);
		logger.debug("---- Finished iteration # " + counter + " ----");

		// ====================================================
		// Business logic
		// ====================================================

		// Get latest and best results
		double bestEditDistance = (Double) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_EDIT_DISTANCE);
		Analysis result = (Analysis) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.LATEST_RESULT);
		double latestEditDistance = result.getEditDistance();

		// This is the stopping criterion.
		// Note: counter is zero-based.
		if ((counter == maxIterations - 1)
				|| ((counter >= minIterations - 1) && (latestEditDistance >= ParserNames.OPTIONS.EDIT_DISTANCE_MIN_REDUCTION_FACTOR
						* bestEditDistance)))
		{
			logger.debug("Terminating loop");
			request.addMessage(new HandlerMessage(this.getName(), "Terminating loop"));

			// Roll back to best tree and analysis result found during iteration
			int bestIteration = (Integer) request
					.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_ITERATION);
			if (bestIteration != counter)
			{
				Analysis bestResult = (Analysis) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_ANALYSIS_RESULT);
				request.setAttribute(
						ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.LATEST_RESULT, bestResult);

				MathTarget bestResponseTarget = (MathTarget) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_RESPONSE_TARGET);
				request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
						bestResponseTarget);
				logger.debug("Rolling back to best iteration # " + bestIteration
						+ ": result " + bestResult);
				request.addMessage(new HandlerMessage(this.getName(),
						"Rolling back to best iteration # " + bestIteration + ": result "
								+ bestResult));
			}

			// Break the loop
			this.setNextNode(null);
			return false;
		}
		else
		{

			// ====================================================
			// Attach outputs to the request
			// ====================================================

			// Save best configuration to date. At this point, there's no edit
			// distance yet, so set it to a large number.

			if (latestEditDistance < bestEditDistance)
			{
				// Save a copy of the current response tree and analysis result
				// object

				// Note: get the response target BEFORE the RC steps (right
				// after the marker is run)
				MathTarget responseTarget = (MathTarget) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.RESPONSE_TARGET);
				logger.debug("best response target so far: "
						+ responseTarget.toStringDetailed());
				request
						.setAttribute(
								ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_ITERATION,
								counter);

				// TODO: bestEditDistance is somewhat redundant, we could
				// instead
				// read it from the best result object. Remove bestEditDistance?
				request
						.setAttribute(
								ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_EDIT_DISTANCE,
								latestEditDistance);

				request
						.setAttribute(
								ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_RESPONSE_TARGET,
								responseTarget.clone());

				request
						.setAttribute(
								ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_ANALYSIS_RESULT,
								result.clone());
			}

			// Update loop counters
			counter++;
			request
					.setAttribute(
							ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.COUNTER,
							counter);
		}
		return false;
	}

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return ParserNames.HANDLER.ANALYSIS.RELATIVE_CANONICALIZATION.NAME + " "
				+ "RC End Loop";
	}
}
