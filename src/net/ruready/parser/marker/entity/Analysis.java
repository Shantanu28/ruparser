/*****************************************************************************************
 * Source File: Analysis.java
 ****************************************************************************************/
package net.ruready.parser.marker.entity;

import java.util.HashMap;
import java.util.Map;

import net.ruready.common.math.real.RealUtil;
import net.ruready.common.misc.Immutable;
import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Marker's output: an object containing analysis of the response expression vs. the
 * reference expression.
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
 * @immutable
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 16, 2007
 */
public class Analysis implements PubliclyCloneable, Immutable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Analysis.class);

	// ========================= FIELDS ====================================

	private final Map<MathTokenStatus, Integer> elementCountMap;

	private final double editDistance;

	private final double correctElementFraction;

	private final SyntaxTreeNode syntaxTree;

	// Refers to element-based score only, not the total score for the response
	private final double score;

	// Records the analysis time
	private double elapsedTime;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Create an analysis from data fields.
	 * 
	 * @param elementCountMap
	 * @param editDistance
	 * @param correctElementFraction
	 * @param syntaxTree
	 * @param score
	 *            Refers to element-based score only, not the total score for the response
	 * @param time
	 *            timer object that records the analysis time [secs]
	 */
	public Analysis(final Map<MathTokenStatus, Integer> elementCountMap,
			final double editDistance, final double correctElementFraction,
			final SyntaxTreeNode syntaxTree, final double score)
	{
		super();
		this.elementCountMap = elementCountMap;
		this.editDistance = editDistance;
		this.correctElementFraction = correctElementFraction;
		this.syntaxTree = syntaxTree;
		this.score = score;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(correctElementFraction);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(editDistance);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result
				+ ((elementCountMap == null) ? 0 : elementCountMap.hashCode());
		temp = Double.doubleToLongBits(score);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((syntaxTree == null) ? 0 : syntaxTree.hashCode());
		return result;
	}

	/**
	 * Note: <code>double</code> type fields are compared up to machine precision, not
	 * using <code>Double.doubleToLongBits()</code>.
	 * 
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
		final Analysis other = (Analysis) obj;
		if (Double.doubleToLongBits(correctElementFraction) != Double
				.doubleToLongBits(other.correctElementFraction))
			return false;
		// if (Double.doubleToLongBits(editDistance) !=
		// Double.doubleToLongBits(other.editDistance))
		if (!RealUtil.doubleEquals(editDistance, other.editDistance,
				ParserNames.OPTIONS.MACHINE_PRECISION))
			return false;
		if (elementCountMap == null)
		{
			if (other.elementCountMap != null)
				return false;
		}
		else if (!elementCountMap.equals(other.elementCountMap))
			return false;
		// if (Double.doubleToLongBits(score) !=
		// Double.doubleToLongBits(other.score))
		if (!RealUtil.doubleEquals(score, other.score,
				ParserNames.OPTIONS.MACHINE_PRECISION))
			return false;
		if (syntaxTree == null)
		{
			if (other.syntaxTree != null)
				return false;
		}
		else if (!syntaxTree.equals(other.syntaxTree))
			return false;
		// Exclude timer object comparison
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Element counts " + elementCountMap + " correctFraction "
				+ correctElementFraction + " distance " + editDistance + " score "
				+ score + " syntax tree " + syntaxTree;
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target of an assembly.
	 * <p>
	 * WARNING: the parameter class E must be immutable for this class to properly be
	 * cloned (and serve as part of a parser's target).
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public Analysis clone()
	{
//		try
//		{
			// Set final fields
			// Analysis copy = (Analysis) super.clone();
			Analysis copy = new Analysis(new HashMap<MathTokenStatus, Integer>(
					elementCountMap), editDistance, correctElementFraction, syntaxTree
					.clone(), score);

			// Set non-final fields
			copy.setElapsedTime(elapsedTime);

			return copy;
//		}
//		catch (CloneNotSupportedException e)
//		{
//			// this shouldn't happen, because we are Cloneable
//			throw new InternalError("clone() failed: " + e.getMessage());
//		}

	}

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * @return the correctElementFraction
	 */
	public double getCorrectElementFraction()
	{
		return correctElementFraction;
	}

	/**
	 * @return the editDistance
	 */
	public double getEditDistance()
	{
		return editDistance;
	}

	/**
	 * @return the elementCountMap
	 */
	public Map<MathTokenStatus, Integer> getElementCountMap()
	{
		return elementCountMap;
	}

	/**
	 * @return the syntaxTree
	 */
	public SyntaxTreeNode getSyntaxTree()
	{
		return syntaxTree;
	}

	/**
	 * @return the score
	 */
	public double getScore()
	{
		return score;
	}

	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime()
	{
		return elapsedTime;
	}

	/**
	 * @param elapsedTime
	 *            the elapsedTime to set
	 */
	public void setElapsedTime(double elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}

}
