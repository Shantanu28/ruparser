/*****************************************************************************************
 * Source File: UnaryOpAssembler.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import net.ruready.common.exception.SystemException;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.port.output.mathml.entity.XmlStringTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop a number from the stack and push the result of its unary operation (e.g., "sin" or
 * "cos").
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
class UnaryOperationAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(UnaryOperationAssembler.class);

	// Should belong to all Value fields
	private static final double SOME_NUMBER = 0.0;

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

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

		// Pop the expression and operation from stack
		StringBuffer e = (StringBuffer) a.pop();
		MathToken t = (MathToken) a.pop();

		// Check if operation is supported by the current arithmetic mode
		XmlStringTarget target = (XmlStringTarget) a.getTarget();
		try
		{
			UnaryOperation op = (UnaryOperation) t.getValue();
			// logger.debug("t.value " + t.getValue() + " value " +
			// target.createValue(SOME_NUMBER));
			op.eval(target.createValue(SOME_NUMBER));
		}
		catch (SystemException ex)
		{
			target.addSyntaxError(new InternationalizableErrorMessage(ex.getMessage(),
					ParserNames.KEY.MATH_EXCEPTION.UNARY_OP, t.getValue().toString(),
					target.getArithmeticMode().toString()));
		}

		// Push the result on the stack
		e = ConversionUtil.applyOperationElement(t, e);
		a.push(e);

		// logger.debug("end, a="+a);
	}

}
