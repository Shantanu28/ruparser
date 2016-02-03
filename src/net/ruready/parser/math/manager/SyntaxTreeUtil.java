/*******************************************************
 * Source File: SyntaxTreeUtil.java
 *******************************************************/
package net.ruready.parser.math.manager;

import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

/**
 * Utilities related to syntax trees, nodes and math tokens.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 6, 2007
 */
public class SyntaxTreeUtil
{
	// ========================= CONSTANTS =================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private SyntaxTreeUtil()
	{

	}

	// ========================= METHODS ===================================

	/**
	 * Replace the parent node of <code>thisNode</code> by a new parent
	 * 
	 * @param target
	 *            target whose syntax tree contains <code>thisNode</code>
	 * @param thisNode
	 *            this node
	 * @param newParent
	 *            new parent
	 */
	public static void replaceParentNode(MathTarget target,
			SyntaxTreeNode thisNode, SyntaxTreeNode newParent)
	{
		// Given the new branch newParent, don't yet add thisNode as a
		// child quite yet, because then asking for its parent will return...
		// newParent! instead of the original parent which we're seeking.

		// Add newParent in place of thisNode under parent
		SyntaxTreeNode parent = thisNode.getParent();
		if (parent == null) {
			newParent.removeParentReference();
			target.setSyntax(newParent);
		}
		else {
			parent.replaceChild(thisNode, newParent);
		}

		// OK, add newParent -> thisNode
		newParent.addChild(thisNode);
	}

	/**
	 * Is a math token data a multinary commutative operation.
	 * 
	 * @param thisData
	 *            a node's data
	 * @return is a math token data a multinary commutative operation
	 */
	public static boolean isOpBinaryAndCommutative(MathToken thisData)
	{
		if (thisData.getValueID() != MathValueID.ARITHMETIC_MULTINARY_OPERATION) {
			return false;
		}
		MultinaryOperation thisValue = (MultinaryOperation) thisData.getValue();
		return thisValue.isCommutative();
	}
}
