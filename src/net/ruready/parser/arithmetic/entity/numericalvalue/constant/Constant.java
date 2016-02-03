/*******************************************************
 * Source File: Constant.java
 *******************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.constant;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * A naming interface for classes that represent mathematical constants.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 10, 2007
 */
public interface Constant
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Return the numerical value of the constant.
	 * 
	 * @return the value of the constant
	 */
	NumericalValue getValue();
}
