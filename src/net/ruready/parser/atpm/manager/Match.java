/*******************************************************
 * Source File: Match.java
 *******************************************************/
package net.ruready.parser.atpm.manager;

import net.ruready.common.misc.Immutable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A triplet consisting of two ints (a Pair) and a cost (double). Used for the
 * edit distance algorithm (a tree-to-tree mapping entry).
 * 
 * @immutable
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 27, 2007
 */
final class Match implements Immutable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Match.class);

	// ========================= FIELDS =====================================

	private final Integer left;

	private final Integer right;

	private double ty;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param pair
	 * @param ty
	 */
	public Match(int left, int right, double ty)
	{
		this.left = left;
		this.right = right;
		this.ty = ty;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the left
	 */
	public Integer getLeft()
	{
		return left;
	}

	/**
	 * @return the right
	 */
	public Integer getRight()
	{
		return right;
	}

	/**
	 * @return Returns the ty.
	 */
	public double getTy()
	{
		return ty;
	}
}
