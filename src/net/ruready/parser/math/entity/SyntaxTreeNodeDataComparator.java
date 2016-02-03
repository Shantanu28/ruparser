/*******************************************************************************
 * Source File: SyntaxTreeNodeDataComparator.java
 ******************************************************************************/
package net.ruready.parser.math.entity;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of two syntax tree nodes based on comparing their data (math
 * tokens).
 * 
 * @todo add multi-nary operations
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jan 26, 2007
 */
public class SyntaxTreeNodeDataComparator implements Comparator<SyntaxTreeNode>
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
			.getLog(SyntaxTreeNodeDataComparator.class);

	// ========================= FIELDS ====================================

	// Math Token comparator
	private final Comparator<MathToken> mathTokenComparator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a syntax tree node comparator.
	 * 
	 * @param mathTokenComparator
	 *            comparator of node data
	 */
	public SyntaxTreeNodeDataComparator(
			final Comparator<MathToken> mathTokenComparator)
	{
		super();
		this.mathTokenComparator = mathTokenComparator;
	}

	// ========================= IMPLEMENTATION: Comparator ================

	/**
	 * Compare two syntax trees. This is a lexicographic ordering (first compare
	 * root data nodes, then compare children branches (recursively), and
	 * finally compare the number of children.
	 * 
	 * @param tree1
	 *            left operand to be compared
	 * @param tree2
	 *            right operand to be compared
	 * @return result of comparison
	 */
	public int compare(SyntaxTreeNode tree1, SyntaxTreeNode tree2)
	{
		// Compare root data
		int compareData = mathTokenComparator.compare(tree1.getData(), tree2
				.getData());
		// logger.debug("compareTo(): data " + getData() + " other.data "
		// + other.getData() + " compareData " + compareData);

		if (compareData != 0) {
			return compareData;
		}
		// Compare children
		int thisSize = tree1.getNumChildren();
		int tree2Size = tree2.getNumChildren();
		for (int i = 0; i < Math.min(thisSize, tree2Size); i++) {
			int compareChild = (tree1.getChild(i)).compareTo(tree2.getChild(i));
			// logger.debug("compareTo(): i " + i + " child " + getChild(i)
			// + " tree2Child " + tree2.getChild(i) + " compareChild "
			// + compareChild);
			if (compareChild != 0) {
				return compareChild;
			}

		}

		// Compare #children
		// logger.debug("compareTo(): comparing #children "
		// + new Integer(thisSize).compareTo(tree2Size));
		return new Integer(thisSize).compareTo(tree2Size);
	}

	// ========================= PRIVATE METHODS ===========================
}
