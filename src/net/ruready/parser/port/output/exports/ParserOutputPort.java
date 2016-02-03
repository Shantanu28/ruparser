/*******************************************************
 * Source File: ParserOutputPort.java
 *******************************************************/
package net.ruready.parser.port.output.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler for an output port for the math parser.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public abstract class ParserOutputPort extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParserOutputPort.class);

	// ========================= FIELDS ====================================

	// Attribute name of the arithmetic target to be printed
	private String attributeNameTarget;

	// Attribute name of the output object constructed by the handler
	private String attributeNameOutput;

	// Parser control options
	private ParserOptions options;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an output port handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 */
	public ParserOutputPort(String attributeNameTarget, String attributeNameOutput)
	{
		super();
		this.attributeNameTarget = attributeNameTarget;
		this.attributeNameOutput = attributeNameOutput;
	}

	/**
	 * Initialize a parser output port handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ParserOutputPort(String attributeNameTarget, String attributeNameOutput,
			RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameTarget = attributeNameTarget;
		this.attributeNameOutput = attributeNameOutput;
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Construct the output object to be attached to the request. This is an
	 * abstract hook.
	 */
	protected abstract Object getOutput();

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * A template method for printer ports.
	 * 
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@Override
	protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		ParserRequest parserRequest = (ParserRequest) request;
		options = parserRequest.getOptions();

		// Get the target
		MathTarget target = (MathTarget) request.getAttribute(attributeNameTarget);

		// ====================================================
		// Business logic
		// ====================================================
		// Process the target
		// TODO: make an abstraction for printers (Printer) that outputs
		// an output stream and add an ASCII printer and a tree image printer.
		this.processTarget(target);

		// logger.debug("After " + name + ": tree " + tree);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		// Save the output object
		logger.debug("Saving output in attribute '" + attributeNameOutput + "'");
		request.setAttribute(attributeNameOutput, this.getOutput());

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
