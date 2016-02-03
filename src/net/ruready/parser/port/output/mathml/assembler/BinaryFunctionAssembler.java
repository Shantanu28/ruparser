/*****************************************************************************************
 * Source File: BinaryFuncAssembler.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import net.ruready.common.exception.SystemException;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.port.output.mathml.entity.XmlStringTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop two numbers from the stack and push the result of their binary operation (one of
 * +,-,*,/,^).
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
 * @version Aug 4, 2007
 */
class BinaryFunctionAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(BinaryFunctionAssembler.class);

	// Should belong to all Value fields
	private static final double SOME_NUMBER = 0.0;

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Pop the top two expressions and a binary operation from the target's stack and push
	 * the result of the binary function. If the desired operation is "log(5,x)", we pop
	 * "5", then "x", then "log" (actually the corresponding <code>MathToken</code> is
	 * read, from which we extract the value of this operation, "log"), compute the result
	 * "log(5,x)" and push a new <code>Expression</code> on the assembly's stack.
	 * 
	 * @param a
	 *            the assembly whose stack is used
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		StringBuffer e1 = (StringBuffer) a.pop();
		StringBuffer e2 = (StringBuffer) a.pop();
		MathToken t = (MathToken) a.pop();

		// Check if operation is supported by the current arithmetic mode
		XmlStringTarget target = (XmlStringTarget) a.getTarget();
		try
		{
			BinaryFunction op = (BinaryFunction) t.getValue();
			NumericalValue value = target.createValue(SOME_NUMBER);
			// logger.debug("t.value " + t.getValue() + " value " + value);
			op.eval(value, value);
		}
		catch (SystemException ex)
		{
			target.addSyntaxError(new InternationalizableErrorMessage(ex.getMessage(),
					ParserNames.KEY.MATH_EXCEPTION.BINARY_FUNC, t.getValue().toString(),
					target.getArithmeticMode().toString()));
		}

		StringBuffer e3 = ConversionUtil.applyOperationElement(t, e2, e1);
		a.push(e3);
		// logger.debug("end, a=" + a);
	}

}
