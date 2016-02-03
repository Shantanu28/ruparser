/*****************************************************************************************
 * Source File: RedundancyRemover.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.parser.absolute.entity.OperationPrecedenceUtil;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Removes redundant parentheses and unary plus operations from a syntax tree.
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
public class RedundancyRemover extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RedundancyRemover.class);

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
	public RedundancyRemover(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Post-traversal ordering implied. Using negative checks to decide whether to process
	 * this node.
	 * 
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPost(final SyntaxTreeNode thisNode)
	{
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken data = thisNode.getData();

		// Parentheses already marked redundant
		if (data.getStatus() == MathTokenStatus.REDUNDANT)
		{
			// logger.debug("process=false, already redundant");
			return false;
		}

		// Unary + ==> always process
		if (AbsoluteCanonicalizationUtil.isUnaryPlus(data))
		{
			// logger.debug("process=true, unary +");
			return true;
		}

		// Not a parenthesis, don't process
		if (data.getValueID() != MathValueID.ARITHMETIC_PARENTHESIS)
		{
			// logger.debug("process=false, not a parenthesis/Unary +");
			return false;
		}

		// If this is the root node, always process
		SyntaxTreeNode parent = thisNode.getParent();
		if (thisNode.getParent() == null)
		{
			// logger.debug("process=true, parent is null");
			return true;
		}

		// Process iff parentheses around child are redundant
		MathToken parentData = parent.getData();
		SyntaxTreeNode child = thisNode.getChild(0);
		MathToken childData = child.getData();
		int childIndex = parent.indexOf(thisNode);
		boolean redundant = OperationPrecedenceUtil.isParenAroundChildRedundant(
				parentData.getValue(), childData.getValue(), childIndex);
		// logger.debug("process = " + redundant);
		return redundant;
	}

	/**
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");

		// Set data status to redundant and move it
		// to list of extraneous tokens in the arithmetic target
		MathToken data = thisNode.getData();
		data.setStatus(MathTokenStatus.REDUNDANT);
		target.addExtraneous(data);

		// Remove node from the tree and move its (only) child under its
		// parent. If the parent is null, set the child to be the new syntax
		// tree in the target.
		SyntaxTreeNode parent = thisNode.getParent();
		SyntaxTreeNode child = thisNode.getChild(0);

		// Detach children from parenthesis node
		thisNode.removeAllChilds();

		if (parent == null)
		{
			child.removeParentReference();
			target.setSyntax(child);
		}
		else
		{
			parent.replaceChild(thisNode, child);
		}

		logger.debug("Updated tree " + target.getSyntax());
	}
}
