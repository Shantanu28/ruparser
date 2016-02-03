/*****************************************************************************************
 * Source File: RealConstantAssembler.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop a token from the stack and push the corresponding mathematical constant onto the
 * arithmetic stack (e.g., "e" or "pi").
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
class RealConstantAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RealConstantAssembler.class);

	// ========================= FIELDS ====================================

	// Real-valued mathematical constant
	protected RealConstant realConstant;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an OpAssember with a specific arithmetic unary operation.
	 * 
	 * @param constant
	 *            real-valued mathematical constant that this assembler will use.
	 */
	public RealConstantAssembler(RealConstant realConstant)
	{
		super();
		this.realConstant = realConstant;
	}

	// ========================= METHODS ===================================

	/**
	 * Pop the top two Configurations from the target's strack and push the result of the
	 * operation. If the assignment is "x=5", the first token popped is "5" and second is
	 * "x". Make a new variable (x,5) and add it to the target's stack. Pop two numbers
	 * from the stack and push the result of the operation.
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);

		// Pop a token from the assembly with the
		// constant's value and create an analysis object
		// Update part lists. A constant is like a number.
		/* Token t = (Token) */a.pop();

		// Make up a null value in case there's a syntax error so as
		// not to throw an exception during target.createValue()
		// NumericalValue value = null;
		//
		// XmlStringTarget target = (XmlStringTarget) a.getTarget();
		// if (target.getArithmeticMode().compareTo(ArithmeticMode.COMPLEX) < 0)
		// {
		// // Complex arithmetic mode was turned off, so this expression is
		// // illegal.
		// target.addSyntaxError(new InternationalizableErrorMessage(
		// ParserNames.KEY.MATH_EXCEPTION.REAL_CONSTANT, true,
		// realConstant.toString(), target.getArithmeticMode().toString()));
		// }
		// else {
		// // mode ok, create value
		// value = target.createConstantValue(realConstant);
		// }

		// Push a constant-filled expression on the stack
		// Convert token to lower case.
		// MathToken mt = new MathToken(((MathAssembly) a).elementsConsumedAll()
		// - 1, value);
		// StringBuffer e = ConversionUtil.generateNumericalElement(mt);
		StringBuffer e = ConversionUtil.generateRealConstantElement(realConstant);

		a.push(e);
		// logger.debug("end, a=" + a);
	}

}
