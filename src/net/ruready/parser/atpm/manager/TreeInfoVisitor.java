/*******************************************************************************
 * Source File: TreeInfoVisitor.java
 ******************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.ruready.common.rl.CommonNames;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.common.tree.SimpleTreeVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A tree information structure for the edit distance algorithm, using a tree
 * visitor pattern.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public final class TreeInfoVisitor<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, T>>
		extends SimpleTreeVisitor<D, T> implements TreeInfo<D, T>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TreeInfoVisitor.class);

	// ========================= FIELDS =====================================

	final List<Integer> label = new ArrayList<Integer>();

	final List<Integer> leftMost = new ArrayList<Integer>();

	final List<Integer> compSet = new ArrayList<Integer>();

	final List<D> dataToken = new ArrayList<D>();

	final List<T> dataNode = new ArrayList<T>();

	final int treeSize;

	final int compSetSize;

	// ==============================
	// Convenient local variables
	// ==============================

	private Stack<Integer> stack = new Stack<Integer>();

	private int n, labelIndex, labelCounter;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a tree info bean.
	 * 
	 * @param str
	 *            tree's string representation
	 * @param tokens
	 *            optional token list from a prepared syntax tree. This array
	 *            corresponds to the order of elements in the
	 *            <code>dataLabel</code> stack.
	 */
	public TreeInfoVisitor(T tree)
	{
		n = 1;
		labelIndex = 1;
		labelCounter = 0;

		label.add(0);
		leftMost.add(0);
		compSet.add(0);
		// pre.add(0);
		stack.push(0);

		this.executeOnTree(tree);

		// Tree ost processing
		compSet.add(n - 1);
		treeSize = n - 1;
		compSetSize = compSet.size() - 1;
	}

	// ========================= IMPLEMENTATION: TreeCommutativeDepth
	// ============

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePre(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePre(T thisNode)
	{
		// E
		stack.push(labelIndex++);
		stack.push(n);

		// F
		// Nothing to do

		if (thisNode.hasChildren()) {
			// A
			dataToken.add(thisNode.getData());
			dataNode.add(thisNode);
			labelCounter++;
		}
		return null;
	}

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePost(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePost(T thisNode)
	{
		if (!thisNode.hasChildren()) {
			// This is a leaf node
			// B
			dataToken.add(thisNode.getData());
			dataNode.add(thisNode);
			labelCounter++;
		}

		// C
		leftMost.add(stack.pop());
		label.add(stack.pop());
		n++;

		// if (stack.peek() != leftMost.get(leftMost.size() - 1)) {
		if ((thisNode.getParent() != null)
				&& (thisNode.getParent().indexOf(thisNode) > 0)) {
			// This is not the first child of its parent
			// D
			compSet.add(n - 1);
		}

		return null;
	}

	// ========================= IMPLEMENTATION: Object =============

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		// TODO: convert to StringBuffer implementation
		StringBuffer s = new StringBuffer("========= Tree Info =========\n");
		s.append("treeSize    = " + treeSize + CommonNames.MISC.NEW_LINE_CHAR);
		s.append("compSetSize = " + compSetSize + CommonNames.MISC.NEW_LINE_CHAR);
		s.append(TreeInfoR1.printIntegerList("label    ", label));
		s.append(TreeInfoR1.printIntegerList("leftMost ", leftMost));
		s.append(TreeInfoR1.printIntegerList("compSet  ", compSet));
		s.append(TreeInfoR1.printIntegerList("dataToken", dataToken));

		return s.toString();
	}

	// ========================= IMPLEMENTATION: TreeInfo ===========

	/**
	 * @return Returns the setSize.
	 */
	public int getCompSetSize()
	{
		return compSetSize;
	}

	/**
	 * @return Returns the treeSize.
	 */
	public int getTreeSize()
	{
		return treeSize;
	}

	/**
	 * @return Returns the dataToken.
	 */
	public List<D> getDataToken()
	{
		return dataToken;
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Integer getCompSet(int index)
	{
		return compSet.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public D getDataToken(int index)
	{
		return dataToken.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public T getDataNode(int index)
	{
		return dataNode.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Integer getLabel(int index)
	{
		return label.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Integer getLeftMost(int index)
	{
		return leftMost.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Integer getPre(int index)
	{
		// return pre.get(index);
		return label.get(index + 1) - 1;
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
