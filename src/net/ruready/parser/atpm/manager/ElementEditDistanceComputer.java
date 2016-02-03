/*******************************************************************************
 * Source File: ElementEditDistanceComputer.java
 ******************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;

import net.ruready.common.exception.SystemException;
import net.ruready.common.text.TextUtil;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.atpm.entity.NodeMapping;
import net.ruready.parser.marker.manager.Marker;
import net.ruready.parser.math.entity.MathTokenStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Element marker's edit distance computer. Uses the number of computed elements
 * of each type to compute an edit distance.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 11, 2007
 */
public class ElementEditDistanceComputer<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
		implements EditDistanceComputer<D, T>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(ElementEditDistanceComputer.class);

	// ========================= FIELDS ====================================

	// ===============================
	// Input fields
	// ===============================

	// Stores the number of elements of each type
	private final Marker marker;

	// Computes the cost function for edit distance minimization
	private final NodeComparisonCost<D> costComputer;

	// ===============================
	// Convenient local variables and
	// internal data structures
	// ===============================

	// ===============================
	// Output fields
	// ===============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Run the edit distance computation on a pair of trees. After construction
	 * is complete, use the getter methods on this object to retrieve the
	 * results.
	 * 
	 * @param marker
	 *            Stores the number of elements of each type
	 * @param costComputer
	 *            Computes the cost function for edit distance minimization
	 */
	public ElementEditDistanceComputer(final Marker marker,
			final NodeComparisonCost<D> costComputer)
	{
		this.marker = marker;
		this.costComputer = costComputer;
	}

	// ========================= IMPLEMENTATION: Object =============

	/**
	 * Print the contents the tree-to-tree mapping.
	 * 
	 * @return A string describing the tree-to-tree mapping.
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append("Edit distance: ");
		s.append(getEditDistance());
		return s.toString();
	}

	// ========================= IMPLEMENTATION: EditDistanceComputer ======

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
	public CostType getComparisonCostType(D tokenLeft, D tokenRight)
	{
		return costComputer.getComparisonCostType(tokenLeft, tokenRight);
	}

	/**
	 * Returns the edit distance as (no. missing + no. unrecognized) * (cost of
	 * inserting/deleting a node).
	 * 
	 * @return the edit distance
	 */
	public double getEditDistance()
	{
		return (marker.getNumElements(MathTokenStatus.MISSING) + marker
				.getNumElements(MathTokenStatus.UNRECOGNIZED))
				* costComputer.getInsertDeleteCost();
	}

	/**
	 * An exception will be thrown if this method is invoked -- don't use this
	 * object to calculate a mapping.
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
	public double getSubTreeEditDistance(final int i, final int j)
	{
		throw new SystemException(
				"Sub-tree edit distance computation not supported");
	}

	/**
	 * Returns the edit distance between a tree and an empty tree. In this case,
	 * it is the size of the tree times the cost of inserting/deleting a node.
	 * 
	 * @return the editDistance between a tree and an empty tree
	 */
	public double getEditDistanceTreeEmpty(T tree)
	{
		return tree.getSize() * costComputer.getInsertDeleteCost();
	}

	/**
	 * Returns the reference tree size (#nodes). An exception will be thrown if
	 * this method is invoked.
	 * 
	 * @return reference tree size (#nodes)
	 * @see net.ruready.parser.atpm.manager.TreeInfo#getTreeSize()
	 */
	public int getReferenceTreeSize()
	{
		throw new SystemException(
				"Reference tree size computation not supported");
	}

	/**
	 * Returns the response tree size (#nodes). An exception will be thrown if
	 * this method is invoked.
	 * 
	 * @return response tree size (#nodes)
	 * @see net.ruready.parser.atpm.manager.TreeInfo#getTreeSize()
	 */
	public int getResponseTreeSize()
	{
		throw new SystemException(
				"Response tree size computation not supported");
	}

	/**
	 * An exception will be thrown if this method is invoked -- don't use this
	 * object to calculate a mapping.
	 * 
	 * @return Returns the mapping.
	 * @see EditDistanceComputer#getMapping()
	 */
	public NodeMapping<D, T> getMapping()
	{
		throw new SystemException(
				"Mapping computation not supported");
	}

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

	// ========================= GETTERS & SETTERS =========================
}
