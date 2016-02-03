/*****************************************************************************************
 * Source File: TreeReferenceInspector.java
 ****************************************************************************************/
package net.ruready.parser.tree.exports;

import net.ruready.common.tree.SimpleTreeVisitor;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Inspects the references between parent and children nodes in the tree and checks their
 * integrity (parent points to child iff child points to parent).
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
public class TreeReferenceInspector extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
{
	// ========================= CONSTANTS ==================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TreeReferenceInspector.class);

	// ========================= FIELDS =====================================

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Create a canonicalization operation processor.
	 */
	public TreeReferenceInspector()
	{
		super();
	}

	// ========================= ABSTRACT METHODS / HOOKS ====================

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executeOnTree(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	public void executeOnTree(SyntaxTreeNode rootNode)
	{
		logger.debug("Inspecting syntax tree references..." + rootNode);
		super.executeOnTree(rootNode);
	}

	/**
	 * @param thisNode
	 * @return
	 */
	protected boolean isProcessedPre(SyntaxTreeNode thisNode)
	{
		return true;
	}

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePre(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePre(SyntaxTreeNode thisNode)
	{
		thisNode.checkReferences();
		return null;
	}

	/**
	 * @param thisNode
	 * @return
	 */
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		return false;
	}

	/**
	 * @param thisNode
	 * @return
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePost(net.ruready.common.tree.MutableTreeNode)
	 */
	@Override
	protected Object executePost(SyntaxTreeNode thisNode)
	{
		return null;
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
