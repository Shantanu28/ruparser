/*******************************************************
 * Source File: TreeCommutativeDepthTestOutput.java
 *******************************************************/
package test.ruready.parser.relative;

import net.ruready.common.junit.entity.TestOutput;

/**
 * Commative tree depth computation - test outputs.
 * 
 * @immutable
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class TreeCommutativeDepthTestOutput implements TestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Tree's Commutative depth
	private final int commDepth;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a test output (results) container from fields.
	 * 
	 * @param commDepth
	 *            tree's Commutative depth
	 */
	public TreeCommutativeDepthTestOutput(final int commDepth)
	{
		super();
		this.commDepth = commDepth;
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
		result = PRIME * result + commDepth;
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
		final TreeCommutativeDepthTestOutput other = (TreeCommutativeDepthTestOutput) obj;
		if (commDepth != other.commDepth)
			return false;
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the commDepth
	 */
	public int getCommDepth()
	{
		return commDepth;
	}

}