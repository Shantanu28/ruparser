/*******************************************************************************
 * Source File: NumericalFormat.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.format;

import net.ruready.common.discrete.Identifiable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * Numerical value formatting generic manipulator type.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         Protected by U.S. Provisional Patent U-4003, February 2006
 * @version May 13, 2007
 */
public interface NumericalFormat<T extends NumericalValue> extends
		Identifiable<ArithmeticMode>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Format a numerical value into a string.
	 * 
	 * @param value
	 *            numerical alue to be printed
	 * @return rounded value string representation
	 */
	public String format(final T value);
}
