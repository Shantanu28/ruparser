/*******************************************************
 * Source File: PrinterOutputPort.java
 *******************************************************/
package net.ruready.parser.port.output.text.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.port.output.exports.ParserOutputPort;
import net.ruready.parser.port.output.text.manager.TextPrinter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler of a printer output port.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public abstract class PrinterOutputPort extends ParserOutputPort
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(PrinterOutputPort.class);

	// ========================= FIELDS ====================================

	// Printer object
	protected TextPrinter printer;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a parser printer output port handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 */
	public PrinterOutputPort(String attributeNameTarget, String attributeNameOutput)
	{
		super(attributeNameTarget, attributeNameOutput);
	}

	/**
	 * Initialize a parser printer output port handler.
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
	public PrinterOutputPort(String attributeNameTarget, String attributeNameOutput,
			RequestHandler nextNode)
	{
		super(attributeNameTarget, attributeNameOutput, nextNode);
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Process the target. This is an abstract hook.
	 * 
	 * @param target
	 *            arithmetic target to process
	 */
	@Override
	abstract protected void processTarget(MathTarget target);

	// ========================= IMPLEMENTATION: ParserOutputPort ==========

	/**
	 * @see net.ruready.parser.port.output.exports.ParserOutputPort#getOutput()
	 */
	@Override
	protected final Object getOutput()
	{
		return printer.getOutput();
	}
}
