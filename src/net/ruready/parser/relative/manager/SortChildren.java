/*****************************************************************************************
 * Source File: SortChildren.java
 ****************************************************************************************/
package net.ruready.parser.relative.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.ruready.common.math.munkres.AssignmentType;
import net.ruready.common.math.munkres.Munkres;
import net.ruready.common.tree.TraversalOrder;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.atpm.entity.NodeMatch;
import net.ruready.parser.atpm.entity.NodeMatchRightIndexComparator;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmatrices.dbl.Matrix;
import org.jmatrices.dbl.MatrixFactory;

/**
 * Sort children of commutative operations; used during the Relative Canonicalization (RC)
 * phase. This may permute children in both trees to minimize the total cost (edit
 * distance) using the Munkres assignment algorithm.
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
public class SortChildren extends RelativeCanonicalizer
{
	// ========================= CONSTANTS ==================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SortChildren.class);

	// ========================= FIELDS =====================================

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Create a children sorting RC operation processor.
	 * 
	 * @param reference
	 *            Reference tree
	 * @param response
	 *            Response tree
	 * @param editDistanceComputer
	 *            ED computer prepared by a marker
	 */
	public SortChildren(final SyntaxTreeNode reference, final SyntaxTreeNode response,
			final EditDistanceComputer<MathToken, SyntaxTreeNode> editDistanceComputer)
	{
		super(reference, response, editDistanceComputer);
	}

	// ========================= IMPLEMENTATION: RelativeCanonicalizer =======

	/**
	 * The result of this method will determine whether the node match will be processed.
	 * In this case, we process all response tokens (NodeMatch.leftData) that are marked
	 * as correct and are multinary operations.
	 * 
	 * @param match
	 *            node match to be considered
	 * @return is this node match to be processed
	 */
	@Override
	protected boolean isProcessed(NodeMatch<MathToken, SyntaxTreeNode> match)
	{
		MathToken referenceToken = match.getLeftData();
		MathToken responseToken = match.getRightData();
		CostType costType = editDistanceComputer.getComparisonCostType(referenceToken,
				responseToken);
		// Note 1: the root nodes data (referenceToken, responseToken) are both
		// fictitious multi-nary operation math tokens. Hence, they won't be
		// marked as correct. To ascertain whether this is an exact node match,
		// use costType == CostType.EQUALS.
		// Note 2: response root nodes (containing a list of relations under them)
		// are also considered multi-nary. In the future, if statements allow multiple
		// relations, they should also be processed here.

		if (responseToken == null)
		{
			// Don't process inexact node matches
			return false;
		}

		boolean isMultinaryNode = ((responseToken.getValueID() == MathValueID.ARITHMETIC_MULTINARY_OPERATION) || (responseToken
				.getValueID() == MathValueID.LOGICAL_RESPONSE));
		return (isMultinaryNode && (costType == CostType.EQUAL));
	}

	/**
	 * Process the node match. Applies the Munkres assignment to the children of the
	 * corresponding nodes, and sorts the response's children according to this
	 * assignment. `
	 * 
	 * @param match
	 *            node match to be processed
	 */
	@Override
	protected void processMatch(NodeMatch<MathToken, SyntaxTreeNode> match)
	{
		logger.debug("Processing match " + match);
		// Retrieve children lists
		SyntaxTreeNode referenceNode = match.getLeftNode();
		SyntaxTreeNode responseNode = match.getRightNode();
		List<SyntaxTreeNode> referenceChildren = referenceNode.getChildren();
		List<SyntaxTreeNode> responseChildren = responseNode.getChildren();

		// Construct the Munkres cost matrix
		final Matrix costMatrix = computeCostMatrix(referenceChildren, responseChildren);
		logger.debug("Munkres cost matrix:\n" + costMatrix);
		// Compute the Munkres child-to-child assignment
		Munkres munkres = new Munkres(costMatrix);
		munkres.solve(AssignmentType.MIN);
		Map<Integer, Integer> assignment = munkres.getAssignment();
		logger.debug("Munkres best cost  " + munkres.getBestCost());
		logger.debug("Munkres assignment " + munkres.getAssignment());

		// Sort children by mapping. Because the cost matrix is augmented by
		// a -contiguous- set of dummy rows/columns, these extra/missing children
		// will constitute a a -contiguous- set (at the end of the children
		// list).
		Collections.sort(responseChildren,
				new SyntaxTreeNodeChildMappingComparator<MathToken, SyntaxTreeNode>(
						assignment, Munkres.INDEX_BASE));
		logger.debug("Updated response tree :\n" + response);
	}

	/**
	 * Loop over the ED computer's nodal mapping elements in post-traversal ordering of
	 * response nodes, and process those that qualify under the <code>isProcessed()</code>
	 * method contract.
	 */
	@Override
	public void execute()
	{
		// Set post-traversal ordering indices in the math tokens of both trees
		new SetTraversalIndex(TraversalOrder.POST).run(reference, Munkres.INDEX_BASE);
		new SetTraversalIndex(TraversalOrder.POST).run(response, Munkres.INDEX_BASE);

		// Ensure post-traversal ordering here by making a copy of the list
		// and sorting it by post-traversal index (NodeMatch.rightIndex)
		List<NodeMatch<MathToken, SyntaxTreeNode>> mapping = editDistanceComputer
				.getMapping();
		List<NodeMatch<MathToken, SyntaxTreeNode>> sortedMapping = new ArrayList<NodeMatch<MathToken, SyntaxTreeNode>>(
				mapping);
		Collections.sort(sortedMapping,
				new NodeMatchRightIndexComparator<MathToken, SyntaxTreeNode>());

		// Loop over matches and process those that apply
		for (NodeMatch<MathToken, SyntaxTreeNode> match : sortedMapping)
		{
			if (isProcessed(match))
			{
				processMatch(match);
			}
		}
	}

	// ========================= PUBLIC METHODS ==============================

	// ========================= PRIVATE METHODS =============================

	/**
	 * Compute the Munkres assignment cost matrix.
	 * 
	 * @param referenceChildren
	 *            list of reference children
	 * @param responseChildren
	 *            list of response children
	 * @return cost matrix
	 */
	private Matrix computeCostMatrix(List<SyntaxTreeNode> referenceChildren,
			List<SyntaxTreeNode> responseChildren)
	{
		// Convenient variables
		final int numReferenceChildren = referenceChildren.size();
		final int numResponseChildren = responseChildren.size();

		// Allocate the matrix
		final int numMunkres = Math.max(numReferenceChildren, numResponseChildren);
		final Matrix costMatrix = MatrixFactory.getMatrix(numMunkres, numMunkres, null);

		// Step 1) Internal matrix
		// costMatrix(i,j) := editDistance(responseChild(i), referenceChild(j)),
		// 1 <= i <= numResponseChildren, 1 <= j <= numReferenceChildren.
		for (int i = 1; i <= numResponseChildren; i++)
		{
			int responseIndex = SortChildren.getTraversalIndex(responseChildren, i);
			for (int j = 1; j <= numReferenceChildren; j++)
			{
				int referenceIndex = SortChildren.getTraversalIndex(referenceChildren, j);
				// logger.debug("i "
				// + i
				// + " index "
				// + responseIndex
				// + " resp "
				// + getChildBranch(responseChildren, i)
				// + " j "
				// + j
				// + " index "
				// + referenceIndex
				// + " ref "
				// + getChildBranch(referenceChildren, j)
				// + " distance "
				// + editDistanceComputer.getSubTreeEditDistance(responseIndex,
				// referenceIndex));
				costMatrix.set(i, j, editDistanceComputer.getSubTreeEditDistance(
						responseIndex, referenceIndex));
			}
		}

		// Step 2) If (numResponseChildren < numReferenceChildren), augment
		// rows:
		// costMatrix(i,j) := |referenceChild(j)|,
		// numResponseChildren+1 <= i <= numReferenceChildren, 1 <= j <=
		// numReferenceChildren.
		if (numResponseChildren < numReferenceChildren)
		{
			for (int j = 1; j <= numReferenceChildren; j++)
			{
				final double distance = editDistanceComputer
						.getEditDistanceTreeEmpty(getChildBranch(referenceChildren, j));
				for (int i = numResponseChildren + 1; i <= numReferenceChildren; i++)
				{
					costMatrix.set(i, j, distance);
				}
			}
		}

		// Step 3) If (numResponseChildren > numReferenceChildren), augment
		// columns:
		// costMatrix(i,j) := |responseChild(i)|,
		// 1 <= i <= numResponseChildren, numReferenceChildren+1 <= j <=
		// numResponseChildren.
		if (numResponseChildren > numReferenceChildren)
		{
			for (int i = 1; i <= numResponseChildren; i++)
			{
				final double distance = editDistanceComputer
						.getEditDistanceTreeEmpty(getChildBranch(responseChildren, i));
				for (int j = numReferenceChildren + 1; j <= numResponseChildren; j++)
				{
					costMatrix.set(i, j, distance);
				}
			}
		}

		return costMatrix;
	}

	/**
	 * Return the a child a children node list.
	 * 
	 * @param children
	 *            children list
	 * @param munkresIndex
	 *            Munkres index-based index
	 * @return child
	 */
	private static SyntaxTreeNode getChildBranch(List<SyntaxTreeNode> children,
			int munkresIndex)
	{
		return children.get(munkresIndex - Munkres.INDEX_BASE);
	}

	/**
	 * Return the traversal index of a node in a children node list.
	 * 
	 * @param children
	 *            children list
	 * @param munkresIndex
	 *            Munkres index-based index
	 * @return traversal index
	 */
	private static int getTraversalIndex(List<SyntaxTreeNode> children, int munkresIndex)
	{
		return getChildBranch(children, munkresIndex).getTraversalIndex();
	}

	// ========================= GETTERS & SETTERS =========================

}
