/*****************************************************************************************
 * Source File: ShashaEditDistanceComputer.java
 ****************************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.common.util.ArrayUtil;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.atpm.entity.NodeMapping;
import net.ruready.parser.atpm.entity.NodeMatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Java translation and modification of the Shasha et. al edit distance C implementation
 * described below.
 * <p>
 * Program name: tdistance-stringlabel.c
 * <p>
 * To compile: gcc tdistance-stringlabel.c
 * <p>
 * To run the program, place the following two trees (in parenthesized preorder form) in a
 * file FILE :
 * <p>
 * {RX{FT{SP}{FR{SQ}{WII{SOP}}}{IP{SZQ}}{EQ{SGP}}}} {RX{FR{SOP}}}
 * <p>
 * The program takes FILE as input and then finds the edit distance and mapping between
 * the two trees. The two trees can have strings, as opposed to single letters, as node
 * labels.
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
 * @version Aug 16, 2007
 */
public class ShashaEditDistanceComputer<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, T>>
		implements ATPMEditDistanceComputer<D, T>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ShashaEditDistanceComputer.class);

	// ========================= FIELDS ====================================

	// ===============================
	// Input fields
	// ===============================

	// Computes the cost function for edit distance minimization
	private final NodeComparisonCost<D> costComputer;

	// ===============================
	// Convenient local variables and
	// internal data structures
	// ===============================

	private final TreeInfo<D, T> referenceTreeInfo;

	private final TreeInfo<D, T> responseTreeInfo;

	private double[][] treedist;

	private double[][] tempdist;

	// Cost of inserting a new node into a tree or deleting a node from a tree
	private final double insertDeleteCost;

	// Cost of inserting a new node (final)
	private final double insertCost;

	// Cost of deleting a node from a tree (final)
	private final double deleteCost;

	// ===============================
	// Output fields
	// ===============================

	// Final edit distance (minimizer)
	private final double editDistance;

	// Nodal mapping
	private final NodeMapping<D, T> mapping;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Run the edit distance computation on a pair of trees. After construction is
	 * complete, use the getter methods on this object to retrieve the results.
	 * 
	 * @param referenceTree
	 *            reference target syntax tree
	 * @param responseTree
	 *            response target syntax tree
	 * @param costComputer
	 *            Computes the cost function for edit distance minimization
	 */
	public ShashaEditDistanceComputer(final T referenceTree, final T responseTree,
			final NodeComparisonCost<D> costComputer)
	{
		logger.debug("referenceTree:\n" + referenceTree);
		logger.debug("responseTree :\n" + responseTree);
		referenceTreeInfo = new TreeInfoVisitor<D, T>(referenceTree);
		responseTreeInfo = new TreeInfoVisitor<D, T>(responseTree);
		// logger.debug("responseTreeInfo:\n" + responseTreeInfo);
		// logger.debug("referenceTreeInfo:\n" + referenceTreeInfo);
		this.costComputer = costComputer;
		this.insertDeleteCost = costComputer.getInsertDeleteCost();

		// The following two fields must be equal to this.insertDeleteCost.
		// They are merely for notation convenience in this class
		this.insertCost = this.insertDeleteCost;
		this.deleteCost = this.insertDeleteCost;

		// Run the edit distance algorithm
		this.editDistance = this.computeTreeDistance();
		this.mapping = this.computeMapping(responseTreeInfo.getTreeSize(),
				referenceTreeInfo.getTreeSize());
		logger.debug("treedist = \n" + ArrayUtil.array2DToString(treedist));
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
		s.append(editDistance);
		s.append(" mapping: ");
		s.append(mapping);
		return s.toString();
	}

	// ========================= IMPLEMENTATION: EditDistanceComputer ======

	/**
	 * Returns the cost type corresponding to the outcome of comparing two tree node
	 * labels (tokens) using the cost computer of this object.
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
	 * @return the editDistance
	 */
	public double getEditDistance()
	{
		return editDistance;
	}

	/**
	 * Return the edit distance between subtrees <code>i</code> and <code>j</code> of
	 * the two original trees.
	 * 
	 * @param i
	 *            post-traversal node <i>1-based</i> index of a node in the reference
	 *            tree
	 * @param j
	 *            post-traversal node <i>1-based</i> index of a node in the response tree
	 * @return the editDistance between the reference sub-tree rooted at <code>i</code>
	 *         and the response sub-tree rooted at <code>j</code>
	 */
	public double getSubTreeEditDistance(final int i, final int j)
	{
		return treedist[i][j];
	}

	/**
	 * @return Returns the mapping.
	 * @see EditDistanceComputer#getMapping()
	 */
	public NodeMapping<D, T> getMapping()
	{
		return mapping;
	}

	/**
	 * Returns the edit distance between a tree and an empty tree. In this case, it is the
	 * size of the tree times the cost of inserting/deleting a node.
	 * 
	 * @return the editDistance between a tree and an empty tree
	 */
	public double getEditDistanceTreeEmpty(T tree)
	{
		return tree.getSize() * costComputer.getInsertDeleteCost();
	}

	/**
	 * Returns the reference tree size (#nodes).
	 * 
	 * @return reference tree size (#nodes)
	 * @see net.ruready.parser.atpm.manager.TreeInfo#getTreeSize()
	 */
	public int getReferenceTreeSize()
	{
		return responseTreeInfo.getTreeSize();
	}

	/**
	 * Returns the response tree size (#nodes).
	 * 
	 * @return response tree size (#nodes)
	 * @see net.ruready.parser.atpm.manager.TreeInfo#getTreeSize()
	 */
	public int getResponseTreeSize()
	{
		return referenceTreeInfo.getTreeSize();
	}

	// ========================= PUBLIC METHODS ============================

	/**
	 * Print the details of edit distance analysis. Must be called after object
	 * construction is complete.
	 * 
	 * @return a string containing the math token's details
	 */
	public String toStringDetailed()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();

		s.append("Cost computer : ");
		s.append(this.costComputer);
		s.append(CommonNames.MISC.NEW_LINE_CHAR);

		s.append("Edit distance : ");
		s.append(editDistance);
		s.append(CommonNames.MISC.NEW_LINE_CHAR);
		s.append("Node mapping  :\n");
		s.append(this.toString());
		s.append(CommonNames.MISC.NEW_LINE_CHAR);
		s.append("Node index mapping: ");
		s.append(mapping);
		s.append(CommonNames.MISC.NEW_LINE_CHAR);
		s.append("Tree1 info    :\n");
		s.append(responseTreeInfo);
		s.append(CommonNames.MISC.NEW_LINE_CHAR);
		s.append("Tree2 info    :\n");
		s.append(referenceTreeInfo);
		s.append(CommonNames.MISC.NEW_LINE_CHAR);

		return s.toString();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * The main function of the tree editing distance algorithm. Inputs: Two Normlized
	 * tree. (See tree.c for normlized tree) outputs: Editing distance between two trees.
	 * Results: Matrix 'treedist' and 'tempdist'. In 'treedist' are all distance between
	 * any subtree to any subtree. In 'tempdist' are all forest to forest distance
	 * D[1..i,1..j]. Side Effects: See above.
	 */
	private double computeTreeDistance()
	{
		int n = responseTreeInfo.getTreeSize();
		int m = referenceTreeInfo.getTreeSize();
		// logger.debug("n "+n+" m "+m);
		treedist = new double[n + 1][];
		tempdist = new double[n + 1][];
		for (int i = 0; i <= n; i++)
		{
			treedist[i] = new double[m + 1];
			tempdist[i] = new double[m + 1];
		}
		for (int i = 1; i <= responseTreeInfo.getCompSetSize(); i++)
		{
			for (int j = 1; j <= referenceTreeInfo.getCompSetSize(); j++)
			{
				evalDistance(responseTreeInfo.getCompSet(i), referenceTreeInfo
						.getCompSet(j));
			}
		}
		return treedist[n][m];
	}

	/**
	 * Return cost of inequality beteen two tree node labels. Equality normally means
	 * string equality, except numbers, which are compared up to the prescribed relative
	 * tolerance.
	 * 
	 * @return If the labels are equal, return 0.0; Else, if the left or right label are
	 *         an operation, return RELABEL_COST_OP. Else, return RELABEL_COST
	 */
	private double labelsEqualCost(int i, int j)
	{
		D tokenLeft = responseTreeInfo.getDataToken().get(responseTreeInfo.getPre(i - 1));
		D tokenRight = referenceTreeInfo.getDataToken().get(
				referenceTreeInfo.getPre(j - 1));
		// logger.debug("gamma(" + tokenLeft + "," + tokenRight + ") = "
		// + costComputer.getComparisonCost(tokenLeft, tokenRight));
		return costComputer.getComparisonCost(tokenLeft, tokenRight);
	}

	/**
	 * A function to compute tree distance between subtree rooted at k and subtree rooted
	 * at l assuming that all subtree to subtree distance needed in computation is
	 * available in 'treedist' matrix. Results: All subtree to subtree distance for
	 * subtree pair such that they have same left most leave decandants. Result are in
	 * 'treedist' matrix. Side Effects: In 'tempdist' matrix, from l(k)-1, l(l)-1 to k,l
	 * will be all forests distance generated in computation.
	 */
	private void evalDistance(int k, int l)
	{
		double item0, item1, item2, mmin;
		int lk = responseTreeInfo.getLeftMost(k);
		int ll = referenceTreeInfo.getLeftMost(l);
		tempdist[lk - 1][ll - 1] = 0.0;
		// logger.debug("k "+k+" l "+l+" lk "+lk+" ll "+ll);
		for (int i = lk; i <= k; ++i)
		{
			tempdist[i][ll - 1] = tempdist[i - 1][ll - 1] + deleteCost;
		}
		for (int j = ll; j <= l; ++j)
		{
			// logger.debug("j "+j);
			tempdist[lk - 1][j] = tempdist[lk - 1][j - 1] + insertCost;
		}
		for (int i = lk; i <= k; ++i)
		{
			for (int j = ll; j <= l; ++j)
			{
				item0 = tempdist[i - 1][j] + deleteCost;
				item1 = tempdist[i][j - 1] + insertCost;
				mmin = Math.min(item0, item1);
				if ((responseTreeInfo.getLeftMost(i) == lk)
						&& (referenceTreeInfo.getLeftMost(j) == ll))
				{
					item2 = tempdist[i - 1][j - 1] + labelsEqualCost(i, j);
					treedist[i][j] = Math.min(mmin, item2);
					tempdist[i][j] = treedist[i][j];
				}
				else
				{
					item2 = tempdist[responseTreeInfo.getLeftMost(i) - 1][referenceTreeInfo
							.getLeftMost(j) - 1]
							+ treedist[i][j];
					tempdist[i][j] = Math.min(mmin, item2);
				}
			}
		}
	}

	/**
	 * Produces the mapping between subtree i and subtree j assuming that 'tree-dist' has
	 * been called before it. Results: Mapping between subtree i and subtree j, the result
	 * is in 'M'. Side Effects: 'tempdist' matrix will be changed.
	 */
	private NodeMapping<D, T> computeMapping(int i, int j)
	{
		int k, l;
		Match t;

		int n = responseTreeInfo.getTreeSize();
		int m = referenceTreeInfo.getTreeSize();

		final Stack<Match> stack = new Stack<Match>();
		// TODO: check that ArrayList doesn't need to be synchronized for
		// the sort operation below
		final NodeMapping<D, T> list = new NodeMapping<D, T>();

		if ((i == n) && (j == m))
		{
			evalMap(stack, n, m);
		}
		else
		{
			evalDistance(i, j);
			evalMap(stack, i, j);
		}

		while (!stack.isEmpty())
		{
			t = stack.pop();
			if (t.getTy() != 0)
			{
				k = t.getLeft();
				l = t.getRight();
				evalDistance(k, l);
				evalMap(stack, k, l);
			}
			else
			{
				// t.getPair() is 1-based, like the rest of this
				// code. We move here to 0-based by subtracting
				// 1 from t.
				// map.add(t.getLeft(), t.getRight());

				D leftToken = null;
				T leftNode = null;
				int leftIndex = t.getRight();
				if (leftIndex > 0)
				{
					leftIndex--;
					leftToken = referenceTreeInfo.getDataToken(referenceTreeInfo
							.getPre(leftIndex));
					leftNode = referenceTreeInfo.getDataNode(referenceTreeInfo
							.getPre(leftIndex));
				}

				D rightToken = null;
				T rightNode = null;
				int rightIndex = t.getLeft();
				if (rightIndex > 0)
				{
					rightIndex--;
					rightToken = responseTreeInfo.getDataToken(responseTreeInfo
							.getPre(rightIndex));
					rightNode = responseTreeInfo.getDataNode(responseTreeInfo
							.getPre(rightIndex));
				}

				list.add(new NodeMatch<D, T>(leftIndex, leftToken, leftNode, rightIndex,
						rightToken, rightNode));
			}
		}

		// TODO: for optimization, omit this sort if element order is not
		// important
		Collections.sort(list);
		return list;
	}

	/**
	 * produces the tree-to-tree mapping. The function back-tracks the forest distance
	 * matrix 'tempdist'. Results: None. Side Effects: Push some new triple to stack 'S'.
	 * Triplet (i, j, 0) means that pair (i, j) will be in mapping. Triplet (i, j, 1)
	 * means taht best mapping between subtree i and subtree j will be part of current
	 * mapping.
	 */
	private void evalMap(final Stack<Match> stack, int k, int l)
	{
		double[] item = new double[3];
		int lk = responseTreeInfo.getLeftMost(k);
		int ll = referenceTreeInfo.getLeftMost(l);
		int i = k;
		int j = l;

		while ((i >= lk) || (j >= ll))
		{
			if (i < lk)
			{
				for (int h = j; h >= ll; h--)
				{
					stack.push(new Match(CommonNames.MISC.INVALID_VALUE_INTEGER, h, 0));
				}
				break;
			}
			if (j < ll)
			{
				for (int h = i; h >= lk; h--)
				{
					stack.push(new Match(h, CommonNames.MISC.INVALID_VALUE_INTEGER, 0));
				}
				break;
			}
			// logger.debug("i "+i+" j "+j);
			item[0] = tempdist[i - 1][j] + deleteCost;
			item[1] = tempdist[i][j - 1] + insertCost;
			if (responseTreeInfo.getLeftMost(i) == lk
					&& referenceTreeInfo.getLeftMost(j) == ll)
			{
				item[2] = tempdist[i - 1][j - 1] + labelsEqualCost(i, j);
				int pos = ArrayUtil.min_pos(item);
				if (pos == 0)
				{
					stack.push(new Match(i, CommonNames.MISC.INVALID_VALUE_INTEGER, 0));
					i = i - 1;
				}
				else if (pos == 1)
				{
					stack.push(new Match(CommonNames.MISC.INVALID_VALUE_INTEGER, j, 0));
					j = j - 1;
				}
				else
				{
					stack.push(new Match(i, j, 0));
					i = i - 1;
					j = j - 1;
				}
			}
			else
			{
				item[2] = tempdist[responseTreeInfo.getLeftMost(i) - 1][referenceTreeInfo
						.getLeftMost(j) - 1]
						+ treedist[i][j];
				int pos = ArrayUtil.min_pos(item);
				if (pos == 0)
				{
					stack.push(new Match(i, CommonNames.MISC.INVALID_VALUE_INTEGER, 0));
					i = i - 1;
				}
				else if (pos == 1)
				{
					stack.push(new Match(CommonNames.MISC.INVALID_VALUE_INTEGER, j, 0));
					j = j - 1;
				}
				else
				{
					stack.push(new Match(i, j, 1));
					i = responseTreeInfo.getLeftMost(i) - 1;
					j = referenceTreeInfo.getLeftMost(j) - 1;
				}
			}
		}
	}

	// ========================= GETTERS & SETTERS =========================
}
