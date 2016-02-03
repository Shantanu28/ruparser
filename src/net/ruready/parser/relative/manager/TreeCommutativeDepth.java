/*******************************************************************************
 * Source File: TreeCommutativeDepth.java
 ******************************************************************************/
package net.ruready.parser.relative.manager;

import java.util.List;

import net.ruready.common.tree.SimpleTreeVisitor;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A syntax tree visitor that computes the (multinary) commutative operation
 * depth.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 14/01/2006
 */
public class TreeCommutativeDepth extends
		SimpleTreeVisitor<MathToken, SyntaxTreeNode>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TreeCommutativeDepth.class);

	// ========================= FIELDS ====================================

	// Convenient local variables
	private int commDepth;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a tree simple visitor.
	 * 
	 * @param tree
	 *            the tree to be printed
	 */
	private TreeCommutativeDepth()
	{

	}

	/**
	 * A facade to be called instead of constructing this object. Computes the
	 * commutative depth of a tree.
	 * 
	 * @param syntax
	 *            syntax tree [root node]
	 * @return syntax tree's commutative depth
	 */
	public static int compute(SyntaxTreeNode syntax)
	{
		TreeCommutativeDepth c = new TreeCommutativeDepth();
		c.executeOnTree(syntax);
		return c.getCommDepth();
	}

	// ========================= IMPLEMENTATION: SimpleTreeVisitor =========

	/**
	 * Do this operation at a node before operating on its children. This is a
	 * hook.
	 * 
	 * @param thisNode
	 *            the root node of the tree.
	 * @return an object containing intermediate processing results
	 */
	@Override
	protected Object executePre(SyntaxTreeNode thisNode)
	{
		return null;
	}

	/**
	 * Do this operation at a node after operating on its children. This is a
	 * hook.
	 * 
	 * @param thisNode
	 *            the root node of the tree.
	 * @return an object containing intermediate processing results
	 */
	@Override
	protected Object executePost(SyntaxTreeNode thisNode)
	{
		// logger.debug("thisNode " + thisNode);
		if ((thisNode.getData() != null)
				&& (thisNode.getValue() != null)
				&& (thisNode.getValueID() == MathValueID.ARITHMETIC_MULTINARY_OPERATION)
				&& ((MultinaryOperation) thisNode.getValue()).isCommutative()) {
			logger.debug("thisNode " + thisNode.getData()
					+ " MOP & commutative - ADD 1");
			return new Integer(1);
		}
		return new Integer(0);
	}

	// ========================= METHODS ===================================

	/**
	 * Apply the operation <code>execute()</code> at every node of a tree.
	 * 
	 * @param rootNode
	 *            the root node of the tree.
	 */
	@Override
	protected void executeOnTree(SyntaxTreeNode rootNode)
	{
		// Tracks absolute depth in the visited tree
		commDepth = (Integer) visitTo(rootNode);
	}

	// ========================= IMPLEMENTATION: TreeVisitor ===============

	/**
	 * Process tree and execute a function at every node. Do not call this
	 * function directly; used <code>executeOnTree</code> instead.
	 * 
	 * @param thisNode
	 *            the root node of the tree to be printed.
	 * @return null, this should be called as a void method.
	 */
	@Override
	public Object visitTo(SyntaxTreeNode thisNode)
	{
		// -----------------------------------
		// Pre-traversal node processing
		// -----------------------------------
		executePre(thisNode);

		// -----------------------------------
		// Process child nodes and find the
		// maximum comm depth among them
		// -----------------------------------
		List<SyntaxTreeNode> children = thisNode.getChildren();
		int maxChildCommDepth = 0;
		for (SyntaxTreeNode child : children) {
			int childCommDepth = (Integer) visitTo(child);
			maxChildCommDepth = Math.max(maxChildCommDepth, childCommDepth);
		}

		// -----------------------------------
		// Post-traversal node processing
		// -----------------------------------
		int thisNodeContribution = (Integer) executePost(thisNode);

		return thisNodeContribution + maxChildCommDepth;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the commDepth
	 */
	public int getCommDepth()
	{
		return commDepth;
	}
}
