/*******************************************************
 * Source File: TreeImagePrinterOutputPort.java
 *******************************************************/
package net.ruready.parser.port.output.image.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.math.basic.IntegerPair;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.port.output.image.manager.TreeImagePrinter;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A parser output port that prints the syntax tree as an ASCII string.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class TreeImagePrinterOutputPort extends ImageOutputPort
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TreeImagePrinterOutputPort.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an ASCII printer handler.
	 * 
	 * @param attributeNameTarget
	 *            attribute name of the arithmetic target to be printed
	 * @param attributeNameOutput
	 *            attribute name of the output stream constructed by the printer
	 * @param attributeNameSize
	 *            attribute name of the image size object constructed by the
	 *            printer
	 */
	public TreeImagePrinterOutputPort(String attributeNameTarget,
			String attributeNameOutput, String attributeNameSize)
	{
		super(attributeNameTarget, attributeNameOutput, attributeNameSize);
	}

	/**
	 * Initialize an ASCII printer handler.
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
	public TreeImagePrinterOutputPort(String attributeNameTarget,
			String attributeNameOutput, String attributeNameSize, RequestHandler nextNode)
	{
		super(attributeNameTarget, attributeNameOutput, attributeNameSize, nextNode);
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
		return ParserNames.HANDLER.PORT.OUTPUT + CommonNames.MISC.TAB_CHAR
				+ "Tree Image Printer";
	}

	// ========================= IMPLEMENTATION: ImageOutputPort =========

	/**
	 * Process the target -- construct an ASCII printer for it. This is a hook.
	 * By default, this does nothing. This should also set the printer field.
	 * 
	 * @param target
	 *            arithmetic target to process
	 */
	@Override
	protected void processTarget(MathTarget target)
	{
		printer = new TreeImagePrinter(target, this.getOptions());
	}

	/**
	 * @see net.ruready.parser.port.output.image.exports.ImageOutputPort#getSize()
	 */
	@Override
	protected IntegerPair getSize()
	{
		return printer.getSize();
	}
}
