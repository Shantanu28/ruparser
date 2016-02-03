/*****************************************************************************************
 * Source File: SignOpAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import java.util.List;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop all the math tokens corresponding to signs, above the signOp fence. Must be called
 * only after the fence has been set by a SignOpSymbolAssembler(true) instance.
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
class SignOperationAssembler extends Assembler implements Auxiliary
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

		// Pop the expression from stack
		SyntaxTreeNode e = (SyntaxTreeNode) a.pop();

		// Pop all the signs above the fence
		List<?> allSigns = Assembler.elementsAbove(a, fence);

		for (Object t : allSigns)
		{
			// Process each sign as a unary operation
			MathToken mathToken = (MathToken) t;
			e = SyntaxTreeNode.op(mathToken, e);
		}

		// Push the resulting expression on the stack
		a.push(e);

		// logger.debug("end, a="+a);
	}

	// ========================= METHODS ===================================

}
