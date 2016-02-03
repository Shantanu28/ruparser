/*******************************************************
 * Source File: AbsoluteCanonicalizationStepTestOutput.java
 *******************************************************/
package test.ruready.parser.absolute;

import net.ruready.common.junit.entity.TestOutput;

/**
 * Absolute canonicalization step test outputs - base class.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class AbsoluteCanonicalizationStepTestOutput implements TestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Canonicalized tree's string representation
	private final String treeString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a test output (results) container from fields.
	 * 
	 * @param editDistance
	 * @param nodalMappingString
	 */
	public AbsoluteCanonicalizationStepTestOutput(final String treeString)
	{
		super();
		this.treeString = treeString;
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
		result = PRIME * result + ((treeString == null) ? 0 : treeString.hashCode());
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
		final AbsoluteCanonicalizationStepTestOutput other = (AbsoluteCanonicalizationStepTestOutput) obj;
		if (treeString == null) {
			if (other.treeString != null) return false;
		}
		else if (!treeString.equals(other.treeString)) return false;
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the treeString
	 */
	public String getTreeString()
	{
		return treeString;
	}

}
