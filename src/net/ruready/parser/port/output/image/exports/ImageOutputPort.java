/*******************************************************
 * Source File: ImageOutputPort.java
 *******************************************************/
package net.ruready.parser.port.output.image.exports;

import java.awt.image.BufferedImage;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.math.basic.IntegerPair;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.port.output.exports.ParserOutputPort;
import net.ruready.parser.port.output.image.manager.ImagePrinter;

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
public abstract class ImageOutputPort extends ParserOutputPort
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ImageOutputPort.class);

	// ========================= FIELDS ====================================

	// Printer object
	protected ImagePrinter printer;

	// attribute name of the image size object constructed by the printer
	private String attributeNameSize;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a parser printer output port handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 * @param attributeNameSize
	 *            attribute name of the image size object constructed by the
	 *            printer
	 */
	public ImageOutputPort(String attributeNameTarget, String attributeNameOutput,
			String attributeNameSize)
	{
		super(attributeNameTarget, attributeNameOutput);
		this.attributeNameSize = attributeNameSize;
	}

	/**
	 * Initialize a parser printer output port handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 * @param attributeNameSize
	 *            attribute name of the image size object constructed by the
	 *            printer
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ImageOutputPort(String attributeNameTarget, String attributeNameOutput,
			String attributeNameSize, RequestHandler nextNode)
	{
		super(attributeNameTarget, attributeNameOutput, nextNode);
		this.attributeNameSize = attributeNameSize;
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

	/**
	 * Return the output image size (height, width).
	 * 
	 * @return the output image size (height, width)
	 */
	protected abstract IntegerPair getSize();

	// ========================= IMPLEMENTATION: ParserOutputPort ==========

	/**
	 * A template method for printer ports.
	 * 
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@Override
	protected boolean handle(ChainRequest request)
	{
		super.handle(request);

		// ====================================================
		// Attach more outputs to the request
		// ====================================================

		// Save the image size object
		request.setAttribute(attributeNameSize, this.getSize());

		return false;
	}

	/**
	 * @see net.ruready.parser.port.output.exports.ParserOutputPort#getOutput()
	 */
	@Override
	protected final BufferedImage getOutput()
	{
		return printer.getOutput();
	}
}
