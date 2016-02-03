/*******************************************************
 * Source File: EditDistanceTestOutput.java
 *******************************************************/
package test.ruready.parser.marker;

import net.ruready.common.math.real.RealUtil;
import net.ruready.parser.rl.ParserNames;
import net.ruready.common.junit.entity.TestOutput;

/**
 * Edit distance data file test outputs.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class EditDistanceTestOutput implements TestOutput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Edit distance between the two trees
	private final double editDistance;

	// Nodal mapping's string representation
	private final String nodalMappingString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a test output (results) container from fields.
	 * 
	 * @param editDistance
	 * @param nodalMappingString
	 */
	public EditDistanceTestOutput(final double editDistance, final String nodalMappingString)
	{
		super();
		this.editDistance = editDistance;
		this.nodalMappingString = nodalMappingString;
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
		temp = Double.doubleToLongBits(editDistance);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result
				+ ((nodalMappingString == null) ? 0 : nodalMappingString.hashCode());
		return result;
	}

	/**
	 * Note: <code>double</code> type fields are compared up to machine
	 * precision, not using <code>Double.doubleToLongBits()</code>.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final EditDistanceTestOutput other = (EditDistanceTestOutput) obj;
		// if (Double.doubleToLongBits(editDistance) != Double
		// .doubleToLongBits(other.editDistance)) return false;
		// if (Double.doubleToLongBits(editDistance) !=
		// Double.doubleToLongBits(other.editDistance))
		if (!RealUtil.doubleEquals(editDistance, other.editDistance,
				ParserNames.OPTIONS.MACHINE_PRECISION)) return false;
		if (nodalMappingString == null)
		{
			if (other.nodalMappingString != null) return false;
		}
		else if (!nodalMappingString.equals(other.nodalMappingString)) return false;
		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the editDistance
	 */
	public double getEditDistance()
	{
		return editDistance;
	}

	/**
	 * @return the nodalMappingString
	 */
	public String getNodalMappingString()
	{
		return nodalMappingString;
	}

}
