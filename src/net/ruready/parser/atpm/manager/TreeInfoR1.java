/*******************************************************************************
 * Source File: TreeInfoR1.java
 ******************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.ruready.common.rl.CommonNames;
import net.ruready.common.tree.AbstractListTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A tree information structure for the edit distance algorithm. This is NOT a
 * tree; see also util.tree.Tree class. This class is immutable. Taken from
 * Release 1 (R1) -- depends on the tree representation of the tree. Use
 * {@link TreeInfoVisitor} instead.
 * 
 * @immutable
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 27, 2007
 */
public final class TreeInfoR1<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, T>>
		implements TreeInfo<D, T>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TreeInfoR1.class);

	// ========================= FIELDS ====================================

	private final List<Integer> label = new ArrayList<Integer>();

	private final List<Integer> leftMost = new ArrayList<Integer>();

	private final List<Integer> compSet = new ArrayList<Integer>();

	private final List<D> dataToken = new ArrayList<D>();

	private final List<T> dataNode = new ArrayList<T>();

	private final int treeSize;

	private final int compSetSize;

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
	public TreeInfoR1(String str, List<T> tokens)
	{
		Stack<Integer> stack = new Stack<Integer>();
		boolean start = true;
		char[] s = str.toCharArray();
		int n = 1, labelIndex = 1, labelCounter = 0;
		label.add(0);
		leftMost.add(0);
		compSet.add(0);
		// pre.add(0);
		stack.push(0);
		String token = CommonNames.MISC.EMPTY_STRING;
		for (int i = 0; i < str.length(); i++) {
			logger
					.debug("############################################################");
			logger.debug("i " + i + " s[i] " + s[i] + " start " + start);
			logger.debug("stack        " + stack);
			logger.debug("labelIndex   " + labelIndex);
			logger.debug("labelCounter " + labelCounter);
			logger.debug("token        " + token);

			if (s[i] == '{') {
				if (!start) {
					// A
					// Done when this node is processed in pre-traversal order
					// (i.e. right before the node's children are processed)
					logger.debug("Adding to dataToken token #" + labelCounter
							+ " = " + tokens.get(labelCounter));
					// Add label to dataLabel and
					// the corresponding token to dataToken and node
					dataToken.add(tokens.get(labelCounter).getData());
					dataNode.add(tokens.get(labelCounter));
					labelCounter++;

					token = CommonNames.MISC.EMPTY_STRING;
					start = true;
				}
			}
			else if (s[i] == '}') {
				if (!start) {
					// B
					// Done when this node is processed in pre-traversal order
					// (i.e. right before the node's children are processed)
					logger.debug("Adding to dataToken token #" + labelCounter
							+ " = " + tokens.get(labelCounter));
					// Add label to dataLabel and
					// the corresponding token to dataToken
					dataToken.add(tokens.get(labelCounter).getData());
					dataNode.add(tokens.get(labelCounter));
					labelCounter++;
					// Reset token to accumulate next label's string
					token = CommonNames.MISC.EMPTY_STRING;
					start = true;
				}
				// C
				// Done when this node is processed in post-traversal order
				// (i.e. right after the node's children have been processed)
				logger.debug("Adding to leftMost, label");
				leftMost.add(stack.pop());
				label.add(stack.pop());
				n++;
				logger.debug("stack.peek() " + stack.peek()
						+ " leftMost.peek() "
						+ leftMost.get(leftMost.size() - 1));
				if (stack.peek() != leftMost.get(leftMost.size() - 1)) {
					// D
					logger.debug("Adding to compSet");
					compSet.add(n - 1);
				}
				logger.debug(this.toString());
			}
			else {
				if (start) {
					// E
					// Done when this node is processed in pre-traversal order
					// (i.e. right before the node's children are processed)
					logger.debug("Pushing onto stack");
					stack.push(labelIndex++);
					stack.push(n);
					start = false;
				}
				// F
				token = token + s[i];
			}
		}

		treeSize = n - 1;
		compSetSize = compSet.size() - 1;
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
		result = PRIME * result + ((compSet == null) ? 0 : compSet.hashCode());
		result = PRIME * result + compSetSize;
		result = PRIME * result
				+ ((dataToken == null) ? 0 : dataToken.hashCode());
		result = PRIME * result + ((label == null) ? 0 : label.hashCode());
		result = PRIME * result
				+ ((leftMost == null) ? 0 : leftMost.hashCode());
		result = PRIME * result + treeSize;
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

		// Note the weird equals() but it's temporary anyway, to show the
		// equivalent of TreeInfoR1 class algorithms.
		if (obj.getClass() != TreeInfoVisitor.class)
			return false;
		final TreeInfoVisitor<?, ?> other = (TreeInfoVisitor<?, ?>) obj;

		if (compSet == null) {
			if (other.compSet != null)
				return false;
		}
		else if (!compSet.equals(other.compSet))
			return false;
		if (compSetSize != other.compSetSize)
			return false;
		if (dataToken == null) {
			if (other.dataToken != null)
				return false;
		}
		else if (!dataToken.equals(other.dataToken))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		}
		else if (!label.equals(other.label))
			return false;
		if (leftMost == null) {
			if (other.leftMost != null)
				return false;
		}
		else if (!leftMost.equals(other.leftMost))
			return false;
		if (treeSize != other.treeSize)
			return false;
		return true;
	}

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
	public T getDataNode(int index)
	{
		return dataNode.get(index);
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

	// ========================= STATIC METHODS ============================

	public static StringBuffer printIntegerList(String title, List<?> list)
	{
		StringBuffer s = new StringBuffer(title);
		s.append(" = [");
		for (Object i : list) {
			s = s.append(i);
			s = s.append(" ");
		}
		s = s.append("]\n");
		return s;
	}

	// ========================= GETTERS & SETTERS =========================
}
