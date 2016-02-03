/*****************************************************************************************
 * Source File: RelationAssembler.java
 ****************************************************************************************/
package net.ruready.parser.logical.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop a single expression from the stack and prepare a relation for it. If the expression
 * is <code>x</code>, the corresponding relation is <code>x := EMPTY</code>.
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
class SingleExpressionAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SingleExpressionAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Pop a single expression from the stack and prepare a relation for it. If the
	 * expression is <code>x</code>, the corresponding relation is
	 * <code>x := EMPTY</code>.
	 * 
	 * @param a
	 *            the assembly whose stack is used
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		// Pop the single expression from the stack
		// MathTarget target = (MathTarget) a.getTarget();
		// target.setSyntax(null);

		SyntaxTreeNode e1 = (SyntaxTreeNode) a.pop();
//		logger.debug("e1 = " + e1);
//		e1.removeFromParent();
//		logger.debug("e1 = " + e1);

		// Prepare the fictitious right-branch of the relation
		MathToken e2Data = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				new EmptyValue(), MathTokenStatus.FICTITIOUS_CORRECT);
		SyntaxTreeNode e2 = new SyntaxTreeNode(e2Data);

		// Prepare a relation and push it onto the stack
		final MathToken t = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				RelationOperation.SINGLE_EXPRESSION_OP,
				MathTokenStatus.FICTITIOUS_CORRECT);

		SyntaxTreeNode e3 = SyntaxTreeNode.op(t, e1, e2);

		a.push(e3);
		// logger.debug("end, a=" + a);
	}
}
