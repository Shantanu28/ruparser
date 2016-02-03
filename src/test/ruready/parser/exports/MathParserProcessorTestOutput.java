/*******************************************************
 * Source File: MathParserProcessorTestOutput.java
 *******************************************************/
package test.ruready.parser.exports;

import java.util.Map;

import net.ruready.common.math.real.RealUtil;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;
import net.ruready.common.junit.entity.TestOutput;

/**
 * Math parser demo data file test output struct.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class MathParserProcessorTestOutput implements TestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	private final Analysis analysis;

	private final boolean equivalent;

	private final double totalScore;

	private final String HTMLString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser test output container from fields.
	 * 
	 * @param analysis
	 * @param equivalent
	 * @param string
	 * @param totalScore
	 */
	public MathParserProcessorTestOutput(final Analysis analysis,
			final boolean equivalent, final double totalScore, final String string)
	{
		super();
		this.analysis = analysis;
		this.equivalent = equivalent;
		HTMLString = string;
		this.totalScore = totalScore;
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
		result = PRIME * result + ((HTMLString == null) ? 0 : HTMLString.hashCode());
		result = PRIME * result + ((analysis == null) ? 0 : analysis.hashCode());
		result = PRIME * result + (equivalent ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(totalScore);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Using tolerant equality for double fields, not the default generated
	 * equals() methods by eclipse that's currently used here.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final MathParserProcessorTestOutput other = (MathParserProcessorTestOutput) obj;
		if (HTMLString == null)
		{
			if (other.HTMLString != null) return false;
		}
		else if (!HTMLString.equals(other.HTMLString)) return false;
		if (analysis == null)
		{
			if (other.analysis != null) return false;
		}
		else if (!analysis.equals(other.analysis)) return false;
		if (equivalent != other.equivalent) return false;
		// if (Double.doubleToLongBits(totalScore) !=
		// Double.doubleToLongBits(other.totalScore))
		if (!RealUtil.doubleEquals(totalScore, other.totalScore,
				ParserNames.OPTIONS.MACHINE_PRECISION)) return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "equivalent " + equivalent
				+ ((equivalent) ? " " : CommonNames.MISC.EMPTY_STRING) + " analysis "
				+ analysis + " HTML String " + HTMLString + " totalScore " + totalScore;
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

	/**
	 * @return
	 * @see net.ruready.parser.marker.entity.Analysis#getScore()
	 */
	public double getMarkerSubTotalScore()
	{
		return analysis.getScore();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the analysis
	 */
	public Analysis getAnalysis()
	{
		return analysis;
	}

	/**
	 * @return the equivalent
	 */
	public boolean isEquivalent()
	{
		return equivalent;
	}

	/**
	 * @return the hTMLString
	 */
	public String getHTMLString()
	{
		return HTMLString;
	}

	/**
	 * @return the totalScore
	 */
	public double getTotalScore()
	{
		return totalScore;
	}

}
