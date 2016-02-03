/*******************************************************************************
 * Source File: ATPMEditDistanceComputer.java
 ******************************************************************************/
package net.ruready.parser.atpm.manager;

import java.io.Serializable;

import net.ruready.common.tree.AbstractListTreeNode;

/**
 * An object that an ATPM edit distance and a nodal mapping between a pair of
 * trees.
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
public interface ATPMEditDistanceComputer<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
		extends EditDistanceComputer<D, T>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================
}
