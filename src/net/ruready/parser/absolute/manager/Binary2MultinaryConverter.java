/*****************************************************************************************
 * Source File: Binary2MultinaryConverter.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convert binary operations to multi-nary form.
 * <p>
 * Examples:<br>
 * 1. x+y -> (+x)+(+y)<br>
 * 2. x-y -> (+x)+(-y)<br>
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
public class Binary2MultinaryConverter extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Binary2MultinaryConverter.class);

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
	public Binary2MultinaryConverter(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Post-traversal ordering implied. Using positive checks to decide whether to process
	 * this node.
	 * 
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		logger.debug("Considering thisNode " + thisNode + "...");
		MathToken thisData = thisNode.getData();
		logger.debug("ID " + thisData.getValueID() + " value " + thisData.getValue());
		if (thisData.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION)
		{
			logger.debug("BOP inverse "
					+ ((BinaryOperation) thisData.getValue()).inverse());
		}
		return ((thisData.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION) && (((BinaryOperation) thisData
				.getValue()).inverse() != null));
	}

	/**
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");
		MathToken thisData = thisNode.getData();

		// Binary operation that has a multi-nary form (e.g. binary +)
		if (Binary2MultinaryConverter.isBinaryOpMultinarySubCase(thisData))
		{
			this.processPostBinaryOpMultinarySubCase(thisNode, true);
		}
		else
		{

			// Binary operation whose inverse has a multi-nary form (e.g.
			// binary -)
			this.processPostBinaryOpMultinarySubCase(thisNode, false);
		}

		logger.debug("Updated tree " + target.getSyntax());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Assuming that {@link #isBinaryOpCase(MathToken)} is true, is the value of
	 * <code>isBinaryOpCase</code> the multi-nary operation (if it's its inverse, this
	 * returns <code>false</code>).
	 * 
	 * @param thisData
	 *            a node's data
	 * @return assuming that {@link #isBinaryOpCase(MathToken)} is true, is the value of
	 *         <code>isBinaryOpCase</code> the multi-nary operation (if it's its
	 *         inverse, this returns <code>false</code>).
	 */
	private static boolean isBinaryOpMultinarySubCase(MathToken thisData)
	{
		BinaryOperation thisValue = (BinaryOperation) thisData.getValue();
		return (thisValue.multinaryOpForm() != null);
	}

	/**
	 * Process a unary operation that satisfies {@link #isBinaryOpCase(MathToken)}
	 * <code> && </code> {@link #isBinaryOpMultinarySubCase(MathToken)}.
	 * 
	 * @param thisNode
	 *            processed node (syntax tree)
	 * @param isMultinary
	 *            if true, processes a binary operation that is also multinary (like +);
	 *            otherwise, assumes that the inverse operation is multinary and the
	 *            current operation is e.g. -
	 */
	private void processPostBinaryOpMultinarySubCase(SyntaxTreeNode thisNode,
			boolean isMultinary)
	{
		MathToken thisData = thisNode.getData();
		BinaryOperation thisValue = (BinaryOperation) thisData.getValue();

		// ---------------------------------------------------------------
		// Replace thisValue { childA childB} by
		// thisValue.multinaryForm { thisValue.unaryForm { childA }
		// A { childB } }
		// Here A = inverse(thisValue).unaryForm thisValue is e.g. +;
		// otherwise A = thisValue.unaryForm (if thisValue is e.g. -)
		// ---------------------------------------------------------------

		// Prepare the new multinary node replacing thisNode. Points to the
		// binary operation element in the original assembly
		MultinaryOperation multinaryThisValue = thisValue.multinaryOpForm();
		MultinaryOperation multinaryOpInverse = thisValue.inverse().multinaryOpForm();
		MultinaryOperation newParentOp = isMultinary ? multinaryThisValue
				: multinaryOpInverse;
		MathToken newThisData = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				newParentOp, MathTokenStatus.FICTITIOUS_CORRECT);
		SyntaxTreeNode newThisNode = new SyntaxTreeNode(newThisData);

		// Add children branches with added fictitious parents into a list
		UnaryOperation unaryThisValue = thisValue.unaryOpForm();
		UnaryOperation unaryInverseValue = thisValue.inverse().unaryOpForm();
		List<SyntaxTreeNode> newChildren = new ArrayList<SyntaxTreeNode>();
		boolean firstChild = true;
		for (SyntaxTreeNode oldChild : thisNode.getChildren())
		{
			if ((oldChild.getValueID() == MathValueID.ARITHMETIC_MULTINARY_OPERATION)
					&& ((MultinaryOperation) oldChild.getValue() == newParentOp))
			{
				// ========================================================
				// Don't add a fictitious unary operation in front of the
				// original child
				// ========================================================
				// If this child is a multi-nary operation with the same value
				// as the parent, skip it. We don't want an extra unary op
				// in front of this child.

				// Notwithstanding, if this is not the first child, copy over
				// the parent's operation value to the first grandchild
				// as well as the original assembly reference.
				if (!firstChild)
				{
					SyntaxTreeNode grandChild = oldChild.getChild(0);
					MathToken newGrandChildData = new MathToken(thisData.get(0),
							grandChild.getValue(), grandChild.getStatus());
					grandChild.setData(newGrandChildData);
				}

				newChildren.add(oldChild);
			}
			else
			{
				// ========================================================
				// Add a fictitious unary operation in front of the original
				// child
				// ========================================================
				// If this is the first child (argument), add a fictitious unary
				// operation in front of it. Otherwise (that is, this is the
				// second child), create a unary operation but link it to the
				// original assembly element.
				MathToken newChildData = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
						(isMultinary || !firstChild) ? unaryThisValue : unaryInverseValue);
				if (firstChild)
				{
					newChildData.setStatus(MathTokenStatus.FICTITIOUS_CORRECT);
				}
				else
				{
					// This unary operation represents the original binary
					// operation. Don't set the status but set the assembly
					// element reference.
					newChildData.setPrimaryElement(thisData.get(0));
				}
				SyntaxTreeNode newChild = new SyntaxTreeNode(newChildData);
				newChild.addChild(oldChild);
				newChildren.add(newChild);
			}

			if (firstChild)
			{
				firstChild = false;
			}
		}

		// Replace thisNode's children list with the prepared list of new
		// children
		newThisNode.removeAllChilds();
		for (SyntaxTreeNode newChild : newChildren)
		{
			newThisNode.addChild(newChild);
		}

		// Replace thisNode by newThisNode under parent
		SyntaxTreeNode parent = thisNode.getParent();
		if (parent == null)
		{
			newThisNode.removeParentReference();
			target.setSyntax(newThisNode);
		}
		else
		{
			parent.replaceChild(thisNode, newThisNode);
		}

	}
}
