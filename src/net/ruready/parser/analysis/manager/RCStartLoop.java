/*******************************************************
 * Source File: RCStartLoop.java
 *******************************************************/
package net.ruready.parser.analysis.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.HandlerMessage;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.misc.Auxiliary;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.relative.manager.TreeCommutativeDepth;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler is called at the beginning of the RC loop and initializes the
 * loop counter to 0.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 12, 2007
 */
public class RCStartLoop extends ParserHandler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RCStartLoop.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a canonicalization step handler.
	 */
	public RCStartLoop()
	{
		super();
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param name
	 *            Name of this canonicalization step
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public RCStartLoop(RequestHandler nextNode)
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
		ParserRequest parserRequest = (ParserRequest) request;
		// Assumes that all variables in the options object are
		// numerical and none are symbolic (null-valued)
		ParserOptions options = parserRequest.getOptions();

		// Get the two syntax trees
		SyntaxTreeNode referenceTree = ((MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE))
				.getSyntax();
		MathTarget responseTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);
		SyntaxTreeNode responseTree = responseTarget.getSyntax();

		// ====================================================
		// Business logic
		// ====================================================

		// Initialize loop counter
		int counter = 0;

		// Determine max number of iterations =
		// max(max(refTreeCommutativeDepth,responseTreeCommutativeDepth)+1,options.maxAnalysisIterations)
		// The +1 is because we should always do at least one iteration to
		// determine the nodal mapping, before we can run any RC steps.
		int referenceCommDepth = TreeCommutativeDepth.compute(referenceTree);
		int responseCommDepth = TreeCommutativeDepth.compute(responseTree);
		int maxCommDepth = Math.max(referenceCommDepth, responseCommDepth);
		int minIterations = options.getMinAnalysisIterations();
		int maxIterations = Math
				.max(maxCommDepth + 1, options.getMaxAnalysisIterations());
		logger.debug("#iterations: min " + minIterations + " max " + maxIterations
				+ " tree comm.depths " + referenceCommDepth + "," + responseCommDepth);
		request.addMessage(new HandlerMessage(this.getName(), "#iterations: min "
				+ minIterations + " max " + maxIterations + " tree comm.depths "
				+ referenceCommDepth + "," + responseCommDepth));

		// ====================================================
		// Attach outputs to the request
		// ====================================================

		// Save loop controls
		request.setAttribute(
				ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.COUNTER,
				counter);
		request
				.setAttribute(
						ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.MIN_ITERATIONS,
						minIterations);
		request
				.setAttribute(
						ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.MAX_ITERATIONS,
						maxIterations);

		// Save best configuration to date. At this point, there's no edit
		// distance yet, so set it to a large number.
		request
				.setAttribute(
						ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_ITERATION,
						counter);
		request
				.setAttribute(
						ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_EDIT_DISTANCE,
						Double.MAX_VALUE);
		request
				.setAttribute(
						ParserNames.REQUEST.ATTRIBUTE.RELATIVE_CANONICALIZATION.RC_LOOP.BEST_RESPONSE_TARGET,
						responseTarget);

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
				+ "RC Start Loop";
	}
}
