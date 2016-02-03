/*****************************************************************************************
 * Source File: NodeMatch.java
 ****************************************************************************************/
package net.ruready.parser.atpm.entity;

import java.io.Serializable;

import net.ruready.common.misc.Immutable;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.common.tree.AbstractListTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tree-to-tree mapping element consisting of two (left and right) pairs of post-traversal
 * index and node data (one from each tree). Both the left or the right may be empty.
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
 * ------------------------------------------------------------------------- *
 * 
 * @immutable
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 25, 2007
 */
public class NodeMatch<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
		implements Comparable<NodeMatch<D, T>>, Immutable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NodeMatch.class);

	// ========================= FIELDS ====================================

	// ======================
	// Left node data
	// ======================

	// Node # in post-traversal ordering of nodes in the left tree
	private final int leftIndex;

	// Left node's data
	private final D leftData;

	// Sub-tree rooted at left node of this match
	private final T leftNode;

	// ======================
	// Right node data
	// ======================

	// Node # in post-traversal ordering of nodes in the right tree
	private final int rightIndex;

	// Right node's data
	private final D rightData;

	// Sub-tree rooted at right node of this match
	private final T rightNode;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a mapping element (representing the mapping Token in tree 1 -> Token in tree
	 * 2).
	 * 
	 * @param leftIndex
	 * @param leftData
	 * @param leftNode
	 * @param rightIndex
	 * @param rightData
	 * @param rightNode
	 */
	public NodeMatch(final int leftIndex, final D leftData, final T leftNode,
			final int rightIndex, final D rightData, final T rightNode)
	{
		this.leftIndex = leftIndex;
		this.leftData = leftData;
		this.leftNode = leftNode;

		this.rightIndex = rightIndex;
		this.rightData = rightData;
		this.rightNode = rightNode;
	}

	// ========================= IMPLEMENTATION: Object =============

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((leftData == null) ? 0 : leftData.hashCode());
		result = PRIME * result + leftIndex;
		result = PRIME * result + ((rightData == null) ? 0 : rightData.hashCode());
		result = PRIME * result + rightIndex;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NodeMatch<?, ?> other = (NodeMatch<?, ?>) obj;
		if (leftData == null)
		{
			if (other.leftData != null)
				return false;
		}
		else if (!leftData.equals(other.leftData))
			return false;
		if (leftIndex != other.leftIndex)
			return false;
		if (rightData == null)
		{
			if (other.rightData != null)
				return false;
		}
		else if (!rightData.equals(other.rightData))
			return false;
		if (rightIndex != other.rightIndex)
			return false;
		return true;
	}

	/**
	 * Print a mapping element.
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(CommonNames.TREE.PARENTHESIS_OPEN);
		s.append(leftIndex);
		s.append(CommonNames.TREE.SEPARATOR);
		s.append(leftData);
		s.append(CommonNames.TREE.STATEMENT_SEPARATOR);
		s.append(rightIndex);
		s.append(CommonNames.TREE.SEPARATOR);
		s.append(rightData);
		s.append(CommonNames.TREE.PARENTHESIS_CLOSE);
		return s.toString();
	}

	// ========================= IMPLEMENTATION: Comparable<NodeMatch<D>> ==

	/**
	 * Compare two pairs by lexicographic order (first by left indices, then by left data,
	 * then by right indices, and finally by right data).
	 * 
	 * @param other
	 *            the the <code>NodeMatch</code> to be compared with this one.
	 * @return the result of comparison
	 */
	public int compareTo(NodeMatch<D, T> other)
	{
		// Compare left indices
		int compareLeftIndex = new Integer(leftIndex).compareTo(other.leftIndex);
		if (compareLeftIndex != 0)
		{
			return compareLeftIndex;
		}

		// Compare left data
		int compareLeftData = (leftData == null) ? ((other.leftData == null) ? 0 : -1)
				: leftData.compareTo(other.leftData);
		if (compareLeftData != 0)
		{
			return compareLeftData;
		}

		// Compare right indices
		int compareRightIndex = new Integer(rightIndex).compareTo(other.rightIndex);
		if (compareRightIndex != 0)
		{
			return compareRightIndex;
		}

		// Compare right data
		int compareRightData = (rightData == null) ? ((other.rightData == null) ? 0 : -1)
				: rightData.compareTo(other.rightData);
		if (compareRightData != 0)
		{
			return compareRightData;
		}

		// Objects are equal
		return 0;
	}

	// ========================= PUBLIC METHODS ============================

	/**
	 * Print a mapping element in a nice format.
	 */
	public String toStringDetailed()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		Object[] temp = new Object[4];
		temp[0] = (leftData == null) ? CommonNames.MISC.NONE
				: (CommonNames.MISC.EMPTY_STRING + leftIndex);
		temp[1] = (leftData == null) ? CommonNames.MISC.NONE : leftData;
		temp[2] = (rightData == null) ? CommonNames.MISC.NONE
				: (CommonNames.MISC.EMPTY_STRING + rightIndex);
		temp[3] = (rightData == null) ? CommonNames.MISC.NONE : rightData.toString();
		String format = "( %-3s %-10s   %-3s %-10s )\n";
		s = s.append(String.format(format, temp));
		return s.toString();
	}

	/**
	 * Returns <code>true</code> if and only if both the left and right data are
	 * non-null.
	 * 
	 * @return <code>true</code> if and only if both the left and right data are
	 *         non-null
	 */
	public boolean isFull()
	{
		return (leftData != null) && (rightData != null);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the leftData
	 */
	public D getLeftData()
	{
		return leftData;
	}

	/**
	 * @return the leftIndex
	 */
	public int getLeftIndex()
	{
		return leftIndex;
	}

	/**
	 * @return the leftNode
	 */
	public T getLeftNode()
	{
		return leftNode;
	}

	/**
	 * @return the rightData
	 */
	public D getRightData()
	{
		return rightData;
	}

	/**
	 * @return the rightIndex
	 */
	public int getRightIndex()
	{
		return rightIndex;
	}

	/**
	 * @return the rightNode
	 */
	public T getRightNode()
	{
		return rightNode;
	}
}
