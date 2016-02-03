/*******************************************************
 * Source File: RedundancyRemoverTestOutput.java
 *******************************************************/
package test.ruready.parser.absolute;


/**
 * Redundancy removal test outputs.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class RedundancyRemoverTestOutput extends AbsoluteCanonicalizationStepTestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// # redundant tokens
	private final int numRedundant;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct test output (results) container from fields.
	 * 
	 * @param treeString
	 * @param numRedundant
	 */
	public RedundancyRemoverTestOutput(String treeString, final int numRedundant)
	{
		super(treeString);
		this.numRedundant = numRedundant;
	}

	// ========================= IMPLEMENTATION: Object ====================
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + numRedundant;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		final RedundancyRemoverTestOutput other = (RedundancyRemoverTestOutput) obj;
		if (numRedundant != other.numRedundant) return false;
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the numRedundant
	 */
	public int getNumRedundant()
	{
		return numRedundant;
	}

}
