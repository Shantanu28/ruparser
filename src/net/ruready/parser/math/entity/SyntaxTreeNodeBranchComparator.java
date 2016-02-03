/*****************************************************************************************
 * Source File: SyntaxTreeNodeBranchComparator.java
 ****************************************************************************************/
package net.ruready.parser.math.entity;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of syntax tree nodes that recurses through all branches of the nodes and
 * compares them as in {@link SyntaxTreeNode#compareTo}, but using a general comparator
 * of math tokens to compare node data.
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
 * @version Aug 1, 2007
 */
public class SyntaxTreeNodeBranchComparator implements Comparator<SyntaxTreeNode>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(SyntaxTreeNodeBranchComparator.class);

	// ========================= FIELDS ====================================

	/**
	 * Math Token comparator; if null, we'll use {@link MathToken}'s natural ordering.
	 */
	private final Comparator<MathToken> mathTokenComparator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a syntax tree node comparator.
	 * 
	 * @param mathTokenComparator
	 *            comparator of node data
	 */
	public SyntaxTreeNodeBranchComparator(final Comparator<MathToken> mathTokenComparator)
	{
		super();
		this.mathTokenComparator = mathTokenComparator;
	}

	// ========================= IMPLEMENTATION: Comparator ================

	/**
	 * Compare two syntax trees. This is a (recursive) lexicographic ordering (first
	 * compare root data nodes, then compare children branches (recursively), and finally
	 * compare the number of children.
	 * 
	 * @param element1
	 *            left operand to be compared
	 * @param element2
	 *            right operand to be compared
	 * @return result of comparison
	 */
	public int compare(SyntaxTreeNode element1, SyntaxTreeNode element2)
	{
		SyntaxTreeNode tree1 = element1;
		SyntaxTreeNode tree2 = element2;

		// Compare root data using comparator, if it is available, otherwise using
		// data natural ordering
		MathToken data1 = tree1.getData();
		MathToken data2 = tree2.getData();

		// Compare root data; must be a symmetric relation; either data may be null,
		// that has nothing to do with the general contract of Comparable in regard to
		// compareTo(null), because we're comparing tree nodes here, not node data.
		int compareData = (mathTokenComparator == null) ? ((data1 == null) ? ((data2 == null) ? 0
				: -1)
				: ((data2 == null) ? 1 : data1.compareTo(data2)))
				: mathTokenComparator.compare(data1, data2);
		// logger.debug("compareTo(): data " + getData() + " tree2.data "
		// + tree2.getData() + " compareData " + compareData);

		if (compareData != 0)
		{
			return compareData;
		}

		// Compare children
		int thisSize = tree1.getNumChildren();
		int tree2Size = tree2.getNumChildren();
		for (int i = 0; i < Math.min(thisSize, tree2Size); i++)
		{
			int compareChild = (tree1.getChild(i)).compareTo(tree2.getChild(i));
			// logger.debug("compareTo(): i " + i + " child " + getChild(i)
			// + " tree2Child " + tree2.getChild(i) + " compareChild "
			// + compareChild);
			if (compareChild != 0)
			{
				return compareChild;
			}

		}

		// Compare #children
		// logger.debug("compareTo(): comparing #children "
		// + new Integer(thisSize).compareTo(tree2Size));
		return new Integer(thisSize).compareTo(tree2Size);
	}

	// ================================================================
	// Alternative Non-recursive branch comparison - deprecated
	// ================================================================
	// /**
	// * A comparator of tree nodes based on comparing their math token data by
	// * {@link MathTokenComparatorFull}.
	// *
	// * @param element1
	// * left operand to be compared
	// * @param element2
	// * right operand to be compared
	// * @return result of comparison
	// */
	// public int compare(LinkedListTreeNode element1, LinkedListTreeNode
	// element2)
	// {
	// SyntaxTreeNode tree1 = element1;
	// SyntaxTreeNode tree2 = element2;
	// return mathTokenComparator.compare(tree1.getData(), tree2.getData());
	// }

	// ========================= PRIVATE METHODS ===========================
}
