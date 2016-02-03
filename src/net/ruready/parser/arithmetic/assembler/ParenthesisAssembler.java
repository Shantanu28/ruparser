/*****************************************************************************************
 * Source File: ParenthesisAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Append a parenthesis math token at the top of the syntax tree. Create references from
 * this math token to both parenthesis tokens in the original assembly.
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
 * @version Aug 15, 2007
 */
class ParenthesisAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParenthesisAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Append a parenthesis math token at the top of the syntax tree. Create references
	 * from this math token to both parenthesis tokens in the original assembly.
	 * 
	 * @param a
	 *            the assembly to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a="+a);
		// Pop the the parenthesis symbols and the operand
		MathToken tokenClose = (MathToken) a.pop();
		SyntaxTreeNode e = (SyntaxTreeNode) a.pop();
		MathToken tokenOpen = (MathToken) a.pop();

		// Create a parenthesis math token that has references to both
		// tokens in the assembly
		MathToken t = new MathToken(tokenOpen.get(0), new ParenthesisValue());
		t.add(tokenClose.get(0));

		// Carry out the parenthesis "operation"
		e = SyntaxTreeNode.op(t, e);

		// Return the result on the assembly stack
		a.push(e);
		// logger.debug("end, a="+a);
	}

}
