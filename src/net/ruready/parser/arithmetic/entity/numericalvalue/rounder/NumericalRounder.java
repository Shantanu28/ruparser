/*******************************************************************************
 * Source File: NumericalFormat.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.rounder;

import net.ruready.common.discrete.Identifiable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * Rounds numerical values to a certain relative tolerance.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 *         <br>(c) 2006-07 Continuing Education , University of Utah . All
 *         copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 27, 2007
 */
public interface NumericalRounder<T extends NumericalValue> extends
		Identifiable<ArithmeticMode>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Round a numerical value to so many significant digits.
	 * 
	 * @param value
	 *            numerical alue to be printed
	 * @param tol
	 *            round-off tolerance. For <code>n</code> significant digits,
	 *            use <code>tol=10^{-n}</code>.
	 * @return rounded value
	 */
	public T round(final T value, final double tol);
}
