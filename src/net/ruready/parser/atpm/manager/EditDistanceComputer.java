/*******************************************************************************
 * Source File: EditDistanceComputer.java
 ******************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;

import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.atpm.entity.NodeMapping;

/**
 * An object that an edit distance and a nodal mapping between a pair of trees.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 15, 2007
 */
public interface EditDistanceComputer<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Returns the cost type corresponding to the outcome of comparing two tree
	 * node labels (tokens) using the cost computer of this object.
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost type of the outcome of comparing two tree node labels.
	 */
	CostType getComparisonCostType(D tokenLeft, D tokenRight);

	/**
	 * Returns the edit distance between the two trees.
	 * 
	 * @return the editDistance between the two trees
	 */
	double getEditDistance();

	/**
	 * Return the edit distance between subtrees <code>i</code> and
	 * <code>j</code> of the two original trees.
	 * 
	 * @param i
	 *            post-traversal node <i>1-based</i> index of a node in the
	 *            reference tree
	 * @param j
	 *            post-traversal node <i>1-based</i> index of a node in the
	 *            response tree
	 * @return the editDistance between the reference sub-tree rooted at
	 *         <code>i</code> and the response sub-tree rooted at
	 *         <code>j</code>
	 */
	double getSubTreeEditDistance(final int i, final int j);

	/**
	 * Returns the edit distance between a tree and an empty tree.
	 * 
	 * @return the editDistance between a tree and an empty tree
	 */
	double getEditDistanceTreeEmpty(T tree);

	/**
	 * Returns the reference tree size (#nodes).
	 * 
	 * @return reference tree size (#nodes)
	 * @see net.ruready.parser.atpm.manager.TreeInfo#getTreeSize()
	 */
	int getReferenceTreeSize();

	/**
	 * Returns the response tree size (#nodes).
	 * 
	 * @return response tree size (#nodes)
	 * @see net.ruready.parser.atpm.manager.TreeInfo#getTreeSize()
	 */
	int getResponseTreeSize();

	/**
	 * Returns the tree-to-tree nodal mapping.
	 * 
	 * @return Returns the mapping.
	 */
	NodeMapping<D, T> getMapping();
}
