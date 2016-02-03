/*****************************************************************************************
 * Source File: ApplyFuncHelper.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import java.util.List;

import net.ruready.common.parser.xml.helper.GriddyHelper;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/**
 * A helper that assembles a list of arguments pushed into the target's stack into a
 * vector. This is used to make a math-parser-recognizable-response from a list of
 * relations.
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
 * @version Jul 19, 2007
 */
class VectorHelper extends GriddyHelper
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(VectorHelper.class);

	// Fence for this function application block
	public static final MathElement FENCE = new MathElement("VECTOR_FENCE",
			new ArithmeticLiteralValue("VECTOR_FENCE"));

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a helper that applies a function or operation to its operands.
	 * 
	 * @param tagName
	 *            final String tagName
	 */
	public VectorHelper(final String tagName)
	{
		super(tagName);
	}

	// ========================= IMPLEMENTATION: Helper ====================

	/**
	 * Stack a fence signifying that a function application block is starting. This fence
	 * is retrieved by {@link #endElement(Object)}.
	 * 
	 * @param atts
	 *            the attributes attached to the element
	 * @param target
	 * @see net.ruready.parser.core.xml.Helper#startElement(java.lang.Object)
	 */
	@Override
	public void processStartElement(final Attributes atts, final Object target)
	{
		// Cast to a friendlier version
		ConverterTarget myTarget = (ConverterTarget) target;

		// Stack the fence
		myTarget.push(VectorHelper.FENCE);
	}

	/**
	 * Create a math element to wrap the most recently pushed element on the target's
	 * stack.
	 * 
	 * @see net.ruready.parser.core.xml.Helper#endElement(java.lang.Object)
	 */
	@Override
	public void processEndElement(Object target)
	{
		// Cast to a friendlier version
		ConverterTarget myTarget = (ConverterTarget) target;

		// Pop all terms above the fence
		List<MathElement> rawElements = myTarget.elementsAbove(VectorHelper.FENCE);

		int numArgs = rawElements.size();

		// logger.debug("<<<<<< Assembling vector >>>>>>");
		// logger.debug("rawElements " + rawElements);
		// logger.debug("numArgs " + numArgs);

		// =================================================================
		// Concatenate argument strings into the result string in reverse
		// order
		// =================================================================
		StringBuffer resultBuffer = TextUtil.emptyStringBuffer();

		if (numArgs == 0)
		{
			// No arguments
			throw new MathParserException(null,
					"Cannot assembler a vector with no arguments");
		} // numArgs == 0
		else
		{
			// Multiple arguments
			for (int i = numArgs - 1; i >= 0; i--)
			{
				resultBuffer.append(rawElements.get(i).getString());
				if (i > 0)
				{
					resultBuffer.append(ParserNames.LOGICAL_COMPILER.STATEMENT_SEPARATOR);
				}
			}

		} // else (i.e. numArgs > 0)

		// Note: no value is currently associated with a vector. This
		// may change in the future.
		MathElement result = new MathElement(resultBuffer.toString(), null);
		// logger.debug("result " + result);

		// Push the new element in place of the old latest element.
		myTarget.push(result);
	}
}
