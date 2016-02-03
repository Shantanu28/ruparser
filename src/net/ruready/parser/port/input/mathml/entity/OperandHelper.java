/*****************************************************************************************
 * Source File: OperandHelper.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import net.ruready.common.parser.xml.helper.GriddyHelper;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A helper that processes an operand in a mathematical expression.
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
 * @version Sep 8, 2007
 */
class OperandHelper extends GriddyHelper
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(OperandHelper.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a helper that processes an operand in a mathematical expression.
	 * 
	 * @param tagName
	 *            final String tagName
	 */
	public OperandHelper(final String tagName)
	{
		super(tagName);
	}

	// ========================= IMPLEMENTATION: Helper ====================

	/**
	 * Create a math token
	 * 
	 * @see net.ruready.parser.core.xml.Helper#characters(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void processCharacters(String s, Object target)
	{
		// Cast to a friendlier version
		ConverterTarget myTarget = (ConverterTarget) target;
		ArithmeticValue value = null;

		if (tagName.equals("cn"))
		{
			// This is a number. Preserve its string representation (so that
			// we don't get "1.0" for s = "1" but also keep a numerical value
			// that's in line with the options passed into the target

			// Make a math token representing this number
			// Convert all numbers encountered to the field of values specified
			// by
			// the arithmetic mode
			try
			{
				value = myTarget.createValue(Double.parseDouble(s));
			}
			catch (NumberFormatException e)
			{
				// May be caught when the arithmetic mode does not support this
				// number
				throw new MathParserException(null, "The number " + s
						+ " was found in the expression but"
						+ "not supported by the parser options arithmetic mode: ");
			}

		}
		else
		{
			// This is a letter/word/variable
			value = new Variable(tagName);
		}

		// Create a new element for this tag's body
		MathElement element = new MathElement(s, value);

		// Push the new element and onto the stack
		myTarget.push(element);
	}

}
