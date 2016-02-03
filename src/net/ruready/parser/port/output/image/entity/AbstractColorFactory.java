/*******************************************************
 * Source File: AbstractColorFactory.java
 *******************************************************/
package net.ruready.parser.port.output.image.entity;

import java.awt.Color;

import net.ruready.parser.math.entity.MathTokenStatus;

/**
 * An abstract factory to instantiate colors corresponding to tree node status
 * types.
 * 
 * @see {link MathTokenStatus}
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 19, 2007
 */
public interface AbstractColorFactory
{
	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Return the foreground (token text) color for drawing the token in a tree
	 * plot.
	 * 
	 * @return the foreground (token text) color for drawing the token in a tree
	 *         plot.
	 */
	public abstract Color textColor(MathTokenStatus status);

	/**
	 * Return the background color for drawing the token in a tree plot.
	 * 
	 * @return the background color for drawing the token in a tree plot.
	 */
	public abstract Color backgroundColor(MathTokenStatus status);

}
