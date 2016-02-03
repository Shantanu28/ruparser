/*******************************************************************************
 * Source File: SetTraversalIndex.java
 ******************************************************************************/
package net.ruready.parser.relative.manager;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.tree.SimpleTreeVisitor;
import net.ruready.common.tree.TraversalOrder;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sets pre- or post- traversal indices in node data in a math expression syntax
 * tree.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, USyntaxTreeNode 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City,
 *         USyntaxTreeNode 84112 Protected by U.S. Provisional Patent U-4003,
 *         February 2006
 * @version Jun 12, 2007
 */
class SetTraversalIndex extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
		implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(SetTraversalIndex.class);

	// ========================= FIELDS =====================================

	// ====================
	// Inputs
	// ====================

	// Node traversal order identifier
	private final TraversalOrder traversalOrder;

	// ====================
	// Convenient local variables
	// ====================
	private int counter;

	// ====================
	// Outputs
	// ====================

	// None; fields of the tree's math tokens are updated

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a syntax tree node traverser.
	 * 
	 * @param traversalOrder
	 *            node traversal order (e.g. pre/post)
	 */
	public SetTraversalIndex(final TraversalOrder traversalOrder)
	{
		this.traversalOrder = traversalOrder;
	}

	/**
	 * A facade to be called instead of constructing this object. Sets the
	 * traversal indices of the tree's node data.
	 * 
	 * @param syntax
	 *            syntax tree [root node]
	 * @param baseIndex
	 *            base index for traversal indices (usually 0-based)
	 */
	public void run(final SyntaxTreeNode syntax, final int baseIndex)
	{
		counter = baseIndex;
		// Traverse the tree into a node list
		this.executeOnTree(syntax);
	}

	// ========================= IMPLEMENTATION: TreeCommutativeDepth
	// ============

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePre(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePre(SyntaxTreeNode thisNode)
	{
		if (traversalOrder == TraversalOrder.PRE) {
			process(thisNode.getData(), counter);
			counter++;
		}
		return null;
	}

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePost(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePost(SyntaxTreeNode thisNode)
	{
		if (traversalOrder == TraversalOrder.POST) {
			process(thisNode.getData(), counter);
			counter++;
		}
		return null;
	}

	// ========================= METHODS ======================================

	/**
	 * Process a math token (set its traversal index).
	 * 
	 * @param visitable
	 * @param traversalIndex
	 */
	private void process(MathToken mt, int traversalIndex)
	{
		new SetTraversalVisitor(mt, traversalIndex);
	}

	// ========================= IMPLEMENTATION: MathTokenTraversalVisitor ====

	/**
	 * Return the traversal index to be set in the next processed math token
	 * 
	 * @return the traversal index to be set in the next processed math token
	 */
	public int getTraversalIndex()
	{
		return counter;
	}

	// ========================= GETTERS & SETTERS =========================

}
