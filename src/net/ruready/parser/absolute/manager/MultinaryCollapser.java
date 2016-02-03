/*****************************************************************************************
 * Source File: MultinaryCollapser.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import java.util.ArrayList;
import java.util.List;

import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Collapses multi-nary operation children under a parent multi-nary operation. These
 * children are removed their their [grand-]children are added directly under the parent.
 * Children branches are then sorted by their natural data ordering.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version May 1, 2007
 */
public class MultinaryCollapser extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MultinaryCollapser.class);

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
	public MultinaryCollapser(final MathTarget target)
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
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken thisData = thisNode.getData();

		// Process multi-nary operation nodes only
		return (thisData.getValueID() == MathValueID.ARITHMETIC_MULTINARY_OPERATION);
	}

	/**
	 * Collapse multi-nary operation children under a parent multi-nary operation. These
	 * children are removed their their [grand-]children are added directly under the
	 * parent. Children are then sorted by natural node data ordering.
	 * 
	 * @param thisNode
	 *            processed node/syntax tree; it is the parent multi-nary operation in
	 *            this case
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");
		MathToken thisData = thisNode.getData();
		MultinaryOperation thisValue = (MultinaryOperation) thisData.getValue();

		// Find all multinary children whose value equals thisValue
		logger.debug("children " + thisNode.getChildren());
		List<SyntaxTreeNode> multinaryChildren = new ArrayList<SyntaxTreeNode>();
		for (SyntaxTreeNode child : thisNode.getChildren())
		{
			if ((child.getValueID() == MathValueID.ARITHMETIC_MULTINARY_OPERATION)
					&& (child.getValue().equals(thisValue)))
			{
				multinaryChildren.add(child);
			}
		}
		logger.debug("Multinary children: " + multinaryChildren);

		// For each such multinary child, remove the child from the tree and add
		// its children under thisNode at the same index
		for (SyntaxTreeNode child : multinaryChildren)
		{
			thisNode.removeChildTree(child);
		}

		// ???????????????? COMMENT ?? OR UNCOMMENT ???????????
		// Sort children by the "natural" MathToken ordering (by type, then by
		// value, etc.)
		// thisNode.setComparator(new SyntaxTreeNodeBranchComparator(new
		// MathTokenComparatorByValue()));
		// Collections.sort(thisNode.getChildren(), new
		// SyntaxTreeNodeBranchComparator(
		// new MathTokenComparatorByValue()));
		logger.debug("Updated tree " + target.getSyntax());
	}

	// ========================= PRIVATE METHODS ===========================
}
