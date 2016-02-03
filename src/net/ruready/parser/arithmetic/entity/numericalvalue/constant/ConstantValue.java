/*****************************************************************************************
 * Source File: ConstantValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.constant;

import net.ruready.common.misc.Immutable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * A naming interface for classes that represent mathematical constants and implements
 * <code>NumericalValue</code>.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Jun 10, 2007
 */
public interface ConstantValue extends NumericalValue, Immutable
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Return the enum constant corresponding to this constant.
	 * 
	 * @return the enum constant corresponding to this constant
	 */
	Constant getConstant();

	/**
	 * Return the numerical value of this constant. Usually the {@link ConstantValue}
	 * instance wraps a {@link NumericalValue} object, which is returned by this method.
	 * 
	 * @return the numerical value of this constant
	 */
	NumericalValue getNumericalValue();
}
