/*******************************************************
 * Source File: ElementMarkerTestOutput.java
 *******************************************************/
package test.ruready.parser.marker;

import java.util.Map;

import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.common.junit.entity.TestOutput;

/**
 * Element marker data file test output struct. Delegates to {@link Analysis}
 * to avoid coupling {@link Analysis} directly with the {@link TestOutput}
 * interface. in other words,this type acts like an adapter / decorator around
 * {@link Analysis}.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class ElementMarkerTestOutput implements TestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Internal object holding parser analysis results
	private final Analysis analysis;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a test output (results) container from fields.
	 * 
	 * @param analysis
	 *            Internal object holding parser analysis results
	 */
	public ElementMarkerTestOutput(final Analysis analysis)
	{
		super();
		this.analysis = analysis;
	}

	/**
	 * Create a test output (results) container from {@link Analysis} fields.
	 * 
	 * @param elementCountMap
	 * @param editDistance
	 * @param correctElementFraction
	 * @param syntaxTree
	 * @param score
	 *            Refers to element-based score only, not the total score for
	 *            the response
	 * @param time
	 *            timer object that records the analysis time [secs]
	 */
	public ElementMarkerTestOutput(final Map<MathTokenStatus, Integer> elementCountMap,
			final double editDistance, final double correctElementFraction,
			final SyntaxTreeNode syntaxTree, final double score)
	{
		super();
		this.analysis = new Analysis(elementCountMap, editDistance,
				correctElementFraction, syntaxTree, score);
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
		result = PRIME * result + ((analysis == null) ? 0 : analysis.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ElementMarkerTestOutput other = (ElementMarkerTestOutput) obj;
		if (analysis == null)
		{
			if (other.analysis != null) return false;
		}
		else if (!analysis.equals(other.analysis)) return false;
		return true;
	}

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#toString()
	 */
	@Override
	public String toString()
	{
		return analysis.toString();
	}

	// ========================= PUBLIC METHODS ============================

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getCorrectElementFraction()
	 */
	public double getCorrectElementFraction()
	{
		return analysis.getCorrectElementFraction();
	}

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getEditDistance()
	 */
	public double getEditDistance()
	{
		return analysis.getEditDistance();
	}

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getElapsedTime()
	 */
	public double getElapsedTime()
	{
		return analysis.getElapsedTime();
	}

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getScore()
	 */
	public double getScore()
	{
		return analysis.getScore();
	}

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getSyntaxTree()
	 */
	public SyntaxTreeNode getSyntaxTree()
	{
		return analysis.getSyntaxTree();
	}

	/**
	 * @param elapsedTime
	 * @see net.ruready.parser.marker.entity.Analysis#setElapsedTime(double)
	 */
	public void setElapsedTime(double elapsedTime)
	{
		analysis.setElapsedTime(elapsedTime);
	}

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getElementCountMap()
	 */
	public Map<MathTokenStatus, Integer> getElementCountMap()
	{
		return analysis.getElementCountMap();
	}

	// ========================= GETTERS & SETTERS =========================

}
