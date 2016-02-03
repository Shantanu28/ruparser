/*******************************************************
 * Source File: RelativeCanonicalizationStepTestOutput.java
 *******************************************************/
package test.ruready.parser.relative;

import net.ruready.common.junit.entity.TestOutput;

/**
 * RC step test outputs - base class.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class RelativeCanonicalizationStepTestOutput implements TestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Canonicalized tree's string representation
	private final String responseTreeString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a test output (results) container from fields.
	 * 
	 * @param editDistance
	 * @param nodalMappingString
	 */
	public RelativeCanonicalizationStepTestOutput(final String responseTreeString)
	{
		super();
		this.responseTreeString = responseTreeString;
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
		result = PRIME * result
				+ ((responseTreeString == null) ? 0 : responseTreeString.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		final RelativeCanonicalizationStepTestOutput other = (RelativeCanonicalizationStepTestOutput) obj;
		if (responseTreeString == null) {
			if (other.responseTreeString != null)
				return false;
		}
		else if (!responseTreeString.equals(other.responseTreeString))
			return false;
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the responseTreeString
	 */
	public String getResponseTreeString()
	{
		return responseTreeString;
	}

}
