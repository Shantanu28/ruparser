/*******************************************************************************
 * Source File: TreeInfo.java
 ******************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;
import java.util.List;

import net.ruready.common.misc.Immutable;
import net.ruready.common.tree.AbstractListTreeNode;

/**
 * Abstract for a tree information structure for the edit distance algorithm.
 * This is NOT a tree; see also util.tree.Tree class.
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

public interface TreeInfo<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
		extends Immutable
{
	// ========================= ABSTRACT METHODS ==========================

	/**
	 * @return Returns the comp. set size.
	 */
	int getCompSetSize();

	/**
	 * @return Returns the treeSize.
	 */
	int getTreeSize();

	/**
	 * @return Returns the dataToken.
	 */
	List<D> getDataToken();

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	Integer getCompSet(int index);

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	D getDataToken(int index);

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	T getDataNode(int index);

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	Integer getLabel(int index);

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	Integer getLeftMost(int index);

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	Integer getPre(int index);
}
