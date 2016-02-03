/*****************************************************************************************
 * Source File: ParserRawOutputPort.java
 ****************************************************************************************/
package net.ruready.parser.port.output.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler for an output port for the math parser. This is a &quot;raw&quot;
 * port in the sense that it converts the parser's input string into some output, without
 * depending on running the parser on the input string.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 26, 2007
 */
public abstract class ParserRawOutputPort extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParserRawOutputPort.class);

	// ========================= FIELDS ====================================

	// Attribute name of the output object constructed by the handler
	private String attributeNameOutput;

	// Parser control options
	private ParserOptions options;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a parser input string output port handler.
	 * 
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 */
	public ParserRawOutputPort(String attributeNameOutput)
	{
		super();
		this.attributeNameOutput = attributeNameOutput;
	}

	/**
	 * Initialize a parser input string output port handler.
	 * 
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public ParserRawOutputPort(String attributeNameOutput, RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameOutput = attributeNameOutput;
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Construct the output object to be attached to the request. This is an abstract
	 * hook.
	 */
	protected abstract Object getOutput();

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
		String inputString = parserRequest.getInputString();

		// ====================================================
		// Business logic
		// ====================================================
		// Process in the input string
		this.processInputString(inputString);

		// logger.debug("After " + name + ": tree " + tree);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		// Save the output object
		request.setAttribute(attributeNameOutput, this.getOutput());

		return false;
	}

	// ========================= METHODS ===================================

	/**
	 * Process the inputString. This is a hook. By default, this does nothing.
	 * 
	 * @param inputString
	 *            parser input string to process
	 */
	protected void processInputString(String inputString)
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
