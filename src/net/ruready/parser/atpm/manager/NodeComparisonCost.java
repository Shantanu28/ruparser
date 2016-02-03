/*******************************************************
 * Source File: NodeComparisonCost.java
 *******************************************************/
package net.ruready.parser.atpm.manager;

import net.ruready.parser.atpm.entity.CostType;

/**
 * An object that computes the cost of inequality beteen two tree node labels
 * for tree edit distance minimization purposes. Equality normally means string
 * equality, except numbers, which are compared up to the prescribed relative
 * tolerance.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 15, 2007
 */
public interface NodeComparisonCost<E>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================
	
	/**
	 * Returns the cost type corresponding to the outcome of comparing two tree
	 * node labels (tokens).
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost type of the outcome of comparing two tree node labels.
	 */
	CostType getComparisonCostType(E tokenLeft, E tokenRight);

	/**
	 * Returns the cost of the outcome of comparing two tree node labels.
	 * Normally, if the labels are equal, this method returns <code>0.0</code>.
	 * 
	 * @param tokenLeft
	 *            left node corresponding math token
	 * @param tokenRight
	 *            right node corresponding math
	 * @return the cost of the outcome of comparing two tree node labels.
	 */
	double getComparisonCost(E tokenLeft, E tokenRight);

	/**
	 * Returns the cost of inserting a new node into a tree or deleting a node from a tree.
	 * 
	 * @return the cost of inserting a new node into a tree or deleting a node from a tree
	 */
	double getInsertDeleteCost();

	// /**
	// * Returns the cost of deleting a node from a tree.
	// *
	// * @return the cost of deleting a node from a tree.
	// */
	// double getDeleteCost();
}
