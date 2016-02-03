/*****************************************************************************************
 * Source File: AbsoluteCanonicalizer.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.common.tree.SimpleTreeVisitor;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A unifying abstraction / base class for absolute canonicalization operations. By
 * default, all hooks do nothing. Sub-classes can override some of them to add their
 * functionality.
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
public class AbsoluteCanonicalizer extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
{
	// ========================= CONSTANTS ==================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AbsoluteCanonicalizer.class);

	// ========================= FIELDS =====================================

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Create a canonicalization operation processor.
	 */
	public AbsoluteCanonicalizer()
	{
		super();
	}

	// ========================= ABSTRACT METHODS / HOOKS ====================

	/**
	 * The result of this method will determine whether the node will be processed before
	 * its children. If the return type is false, this node is not processed. By default,
	 * this method returns <code>false</code>.
	 * 
	 * @param thisNode
	 *            current root node
	 * @return should this canonicalization operation be performed on the node before its
	 *         children
	 */
	protected boolean isProcessedPre(SyntaxTreeNode thisNode)
	{
		return false;
	}

	/**
	 * The result of this method will determine whether the node will be processed after
	 * its children. If the return type is false, this node is not processed. By default,
	 * this method returns <code>false</code>.
	 * 
	 * @param thisNode
	 *            current root node
	 * @return should this canonicalization operation be performed on the node after its
	 *         children
	 */
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		return false;
	}

	/**
	 * Process the node (in pre traversal ordering) - before its children are processed.
	 * 
	 * @param thisNode
	 *            current root node
	 */
	protected void processPre(SyntaxTreeNode thisNode)
	{

	}

	/**
	 * Process the node (in post traversal ordering) - after its children are processed.
	 * 
	 * @param thisNode
	 *            current root node
	 */
	protected void processPost(SyntaxTreeNode thisNode)
	{

	}

	// ========================= IMPLEMENTATION: TreeCommutativeDepth ======

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePre(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	final protected Object executePre(SyntaxTreeNode thisNode)
	{
		// Process a node using the visit() functions (see also
		// this.executePost())
		if (isProcessedPre(thisNode))
		{
			processPre(thisNode);
		}
		return null;
	}

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePost(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	final protected Object executePost(SyntaxTreeNode thisNode)
	{
		// Process a node using the same visit() functions (see also
		// this.executePre())
		if (isProcessedPost(thisNode))
		{
			processPost(thisNode);
		}
		return null;
	}

	/**
	 * Evaluate the tree. This method is public here.
	 * 
	 * @param rootNode
	 *            the root node of the tree.
	 */
	@Override
	final public void executeOnTree(SyntaxTreeNode rootNode)
	{
		// Run evaluation
		super.executeOnTree(rootNode);
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
