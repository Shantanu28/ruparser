/*******************************************************
 * Source File: ParserInputPort.java
 *******************************************************/
package net.ruready.parser.port.input.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler for an input port for the math parser.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public abstract class ParserInputPort extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParserInputPort.class);

	// ========================= FIELDS ====================================

	// Parser control options
	private ParserOptions options;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an input port handler.
	 */
	public ParserInputPort()
	{
		super();
	}

	/**
	 * Initialize a parser input port handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ParserInputPort(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Construct the input string for the math parser. This is an abstract hook.
	 */
	protected abstract String getInput();

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * A template method for printer ports.
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

		// Get the input string
		String inputString = this.getInput();

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		parserRequest.setInputString(inputString);

		return false;
	}

	// ========================= METHODS ===================================

	/**
	 * Process the target. This is a hook. By default, this does nothing.
	 * 
	 * @param target
	 *            arithmetic target to process
	 */
	protected void processTarget(MathTarget target)
	{

	}

	/**
	 * @return the options
	 */
	protected ParserOptions getOptions()
	{
		return options;
	}

}
