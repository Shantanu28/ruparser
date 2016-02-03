/*****************************************************************************************
 * Source File: RelationAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop two expression from the stack and push back the result of their binary operation.
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
class BinaryOperationAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(BinaryOperationAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Pop the top two expressions and a binary operation from the target's stack and push
	 * the result of the binary operation. If the desired relational operation is "x=5",
	 * we pop "5", then "=", (actually the corresponding <code>MathToken</code> is read,
	 * from which we extract the value of this operation, "+") then "x", compute the
	 * resulting relation "x=5" and push a new <code>SyntaxTreeNode</code> on the
	 * assembly's stack.
	 * 
	 * @param a
	 *            the assembly whose stack is used
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		SyntaxTreeNode e1 = (SyntaxTreeNode) a.pop();
		MathToken t = (MathToken) a.pop();
		SyntaxTreeNode e2 = (SyntaxTreeNode) a.pop();

		// logger.debug("Inspecting e1");
		// new TreeReferenceInspector().executeOnTree(e1);
		// logger.debug("Inspecting e2");
		// new TreeReferenceInspector().executeOnTree(e2);

		SyntaxTreeNode e3 = SyntaxTreeNode.op(t, e2, e1);
		a.push(e3);
		// logger.debug("end, a=" + a);
	}

}
