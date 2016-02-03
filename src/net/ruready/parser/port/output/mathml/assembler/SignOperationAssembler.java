/*******************************************************
 * Source File: SignOpAssembler.java
 *******************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import java.util.List;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.math.entity.MathToken;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 21/12/2005 Pop all the math tokens corresponding to signs, above the
 *          signOp fence. Must be called only after the fence has been set by a
 *          SignOpSymbolAssembler(true) instance.
 */
class SignOperationAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SignOperationAssembler.class);

	// ========================= FIELDS ====================================

	// Fence token
	private Token fence;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a multi-nary operation assembler.
	 * 
	 * @param fence
	 *            fence token
	 */
	public SignOperationAssembler(Token fence)
	{
		super();
		this.fence = fence;
	}

	// ========================= IMPLEMENTATION: Assembler =================

	/**
	 * Pop the top two Configurations from the target's strack and push the
	 * result of the operation. If the assignment is "x=5", the first token
	 * popped is "5" and second is "x". Make a new variable (x,5) and add it to
	 * the target's stack. Pop two numbers from the stack and push the result of
	 * the operation.
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a="+a);

		// Pop the expression from stack
		StringBuffer e = (StringBuffer) a.pop();

		// Pop all the signs above the fence
		List<?> allSigns = Assembler.elementsAbove(a, fence);

		for (Object t : allSigns) {
			// Process each sign as a unary operation
			MathToken mathToken = (MathToken) t;
			e = ConversionUtil.applyOperationElement(mathToken, e);
		}

		// Push the resulting expression on the stack
		a.push(e);

		// logger.debug("end, a="+a);
	}

	// ========================= METHODS ===================================

}
