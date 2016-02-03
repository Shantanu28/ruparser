/*****************************************************************************************
 * Source File: AssociationSwitch.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.parser.absolute.entity.OperationPrecedenceUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryValue;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.manager.SyntaxTreeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Switch the association of equal-precedence operations in the following case:
 * <p>
 * a o [b x c] -> [a o b] x c where o = + or * and x = inv(o)
 * <p>
 * In this case, (implicit) parentheses around [b x c] are redundant and we can replace
 * them with implicit parentheses around [a o b].
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
 * @version Jul 19, 2007
 */
public class AssociationSwitch extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AssociationSwitch.class);

	// ========================= FIELDS ====================================

	// arithmetic target object where redundant tokens are stored
	// outside the syntax tree
	private final MathTarget target;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize redundancy remover.
	 * 
	 * @param target
	 *            arithmetic target where redundant tokens are stored
	 */
	public AssociationSwitch(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Pre-traversal ordering implied. Using negative checks to decide whether to process
	 * this node.
	 * 
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPre(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPre(SyntaxTreeNode thisNode)
	{
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken data = thisNode.getData();

		// Token is not a binary operation
		// ==> don't process
		if (data.getValueID() != MathValueID.ARITHMETIC_BINARY_OPERATION)
		{
			// logger.debug("process=false, not a binary op");
			return false;
		}

		// Binary operation but the right operand is not a binary operation of the same
		// precedence as the parent
		// ==> don't process
		MathToken rightChildData = thisNode.getChildData(BinaryValue.RIGHT);
		if (!OperationPrecedenceUtil.isBothBinaryAndEqualPrecedence(data, rightChildData))
		{
			// logger
			// .debug("process=false, Binary op but the right operand is not a
			// binary "
			// + "operation of the same precedence as the parent or op
			// precedence");
			return false;
		}

		// Parentheses around child not redundant, cannot switch association
		// ==> don't process
		if (!OperationPrecedenceUtil.isParenAroundChildRedundant(data.getValue(),
				rightChildData.getValue(), BinaryValue.RIGHT))
		{
			// logger
			// .debug("process=false, non-redundant parentheses does not allow
			// association switching");
			return false;
		}

		// All other cases ==> process
		return true;
	}

	/**
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPre(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPre(SyntaxTreeNode thisNode)
	{
		logger.debug("processPre(thisNode = " + thisNode + ")");

		// Remove thisNode.rightChild's children, but save them
		SyntaxTreeNode rightChild = thisNode.getChild(BinaryValue.RIGHT);
		SyntaxTreeNode rightLeftGrandChild = rightChild.getChild(BinaryValue.LEFT);
		SyntaxTreeNode rightRightGrandChild = rightChild.getChild(BinaryValue.RIGHT);
		rightChild.removeAllChilds();

		// Replace thisNode.parent with thisNode.rightChild
		SyntaxTreeUtil.replaceParentNode(target, thisNode, rightChild);

		// Replace thisNode.rightChild by rightLeftGrandChild
		thisNode.setChildAt(BinaryValue.RIGHT, rightLeftGrandChild);

		// thisNode is currently rightChild's only child. Add the original
		// right-right grand-child at the end of rightChild's children list,
		// in other words, make it the right child.
		rightChild.addChild(rightRightGrandChild);

		// Admittedly, the above tree edit operations are kind of perverse...
		logger.debug("Updated tree " + target.getSyntax());

		// Recursively run association switch on the newly formed
		// rightChild.leftChild (which is conveniently stored in thisNode)
		// because new associations might now be revealed and switched
		logger.debug("Re-running on new left child " + thisNode);
		this.executeOnTree(thisNode);
	}
}
