/*****************************************************************************************
 * Source File: ComplexConstantAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ConstantValue;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.tokenizer.entity.MathAssembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pops a token from the stack and push the corresponding mathematical complex constant
 * onto the arithmetic stack (e.g., "i" or "j").
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
class ComplexConstantAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ComplexConstantAssembler.class);

	// ========================= FIELDS ====================================

	// Complex-valued mathematical constant
	private ComplexConstant complexConstant;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an OpAssember with a specific arithmetic unary operation.
	 * 
	 * @param complexConstant
	 *            complex-valued mathematical constant that this assembler will use.
	 */
	public ComplexConstantAssembler(final ComplexConstant complexConstant)
	{
		super();
		this.complexConstant = complexConstant;
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
		// logger.debug("start, a="+a);

		// Pop a token from the assembly with the
		// constant's value and create an analysis object
		// Update part lists. A constant is like a number.
		/* Token t = (Token) */a.pop();

		// Make up a null value in case there's a syntax error so as
		// not to throw an exception during target.createValue()
		ConstantValue value = null;

		MathTarget target = (MathTarget) a.getTarget();
		if (target.getArithmeticMode().compareTo(ArithmeticMode.COMPLEX) < 0)
		{
			// Complex arithmetic mode was turned off, so this expression is
			// illegal.
			target.addSyntaxError(new InternationalizableErrorMessage(
					"Complex arithmetic mode is turned off",
					ParserNames.KEY.MATH_EXCEPTION.COMPLEX_CONSTANT, complexConstant
							.toString(), target.getArithmeticMode().toString()));
		}
		else
		{
			// mode ok, create value
			value = target.createConstantValue(complexConstant);
		}

		// Push a constant-filled expression on the stack
		// Convert token to lower case.
		MathToken mt = new MathToken(((MathAssembly) a).elementsConsumedAll() - 1, value);
		a.push(new SyntaxTreeNode(mt));
		// logger.debug("end, a="+a);
	}
}
