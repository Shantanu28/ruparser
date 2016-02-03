/*******************************************************
 * Source File: ScoreComputerHandler.java
 *******************************************************/
package net.ruready.parser.score.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.score.manager.ScoreComputer;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler for the score computation phase.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public abstract class ScoreComputerHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ScoreComputerHandler.class);

	// ========================= FIELDS ====================================

	// Parser control options
	protected ParserOptions options;

	// Printer object
	protected ScoreComputer scoreComputer;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a parser scoreComputer handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 */
	public ScoreComputerHandler()
	{
		super();
	}

	/**
	 * Initialize a parser scoreComputer handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ScoreComputerHandler(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Process the target. This is an abstract hook.This should also set the
	 * score computer field.
	 * 
	 * @param request
	 *            request to read results from
	 */
	abstract protected void processRequest(ParserRequest request);

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * A template method for scoreComputer ports.
	 * 
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@Override
	final protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		ParserRequest parserRequest = (ParserRequest) request;
		options = parserRequest.getOptions();

		// ====================================================
		// Business logic
		// ====================================================
		// Process the target
		this.processRequest(parserRequest);

		// logger.debug("After " + name + ": tree " + tree);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		// Save the score
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.SCORE.SCORE, scoreComputer.getScore());

		return false;
	}
}
