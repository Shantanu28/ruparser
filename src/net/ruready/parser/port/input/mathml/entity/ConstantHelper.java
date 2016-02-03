/*****************************************************************************************
 * Source File: FunctionHelper.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import net.ruready.common.parser.xml.helper.GriddyHelper;
import net.ruready.common.util.EnumUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A helper that processes a mathematical constant in a mathematical expression.
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
 * @version Oct 11, 2007
 */
class ConstantHelper extends GriddyHelper
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ConstantHelper.class);

	// ========================= FIELDS ====================================

	// Corresponding name of mathematical constant in parser syntax
	private final String parserConstantName;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a helper that processes a function/operation in a mathematical
	 * expression.
	 * 
	 * @param tagName
	 *            final String tagName
	 * @param parserConstantNameCorresponding
	 *            name of mathematical constant in parser syntax
	 */
	public ConstantHelper(final String tagName, final String parserConstantName)
	{
		super(tagName);
		this.parserConstantName = parserConstantName;
	}

	// ========================= IMPLEMENTATION: Helper ====================

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

		// Create a new element for this tag.
		// Decide which type of function or operation to construct based
		// on the tag's name. The element's string representation is dummy.
		ArithmeticValue value = ConstantHelper.generateValue(parserConstantName);
		MathElement element = new MathElement(value.toString(), value);

		// Push the new element and onto the stack
		myTarget.push(element);
	}

	/**
	 * Decide which type of constant to construct based on a tag's name.
	 * 
	 * @param name
	 *            tag's name
	 * @return corresponding math value: a binary function, binary operation or unary
	 *         operation
	 */
	private static ArithmeticValue generateValue(String name)
	{

		// Try to match a complex-valued constant
		ComplexConstant complexConstant = EnumUtil.createFromString(
				ComplexConstant.class, name);
		if (complexConstant != null)
		{
			return new ArithmeticLiteralValue(complexConstant.toString());

		}

		// Try to match a real-valued constant
		RealConstant realConstant = EnumUtil.createFromString(RealConstant.class, name);
		if (realConstant != null)
		{
			return new ArithmeticLiteralValue(realConstant.toString());

		}

		logger.warn("No match found for " + name);
		return null;
	}
}
