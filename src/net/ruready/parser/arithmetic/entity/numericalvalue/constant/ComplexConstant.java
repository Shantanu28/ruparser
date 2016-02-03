/*******************************************************************************
 * Source File: ComplexConstant.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.constant;

import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;

/**
 * A discrete set of supported complex-valued mathematical constants like "i".
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 11, 2007
 */
public enum ComplexConstant implements Constant
{
	// ========================= CONSTANTS =================================

	I(ComplexValue.UNIT_IM_VALUE)
	{
		@Override
		public String toString()
		{
			return "i";
		}

		@Override
		public String toHTML()
		{
			return "i";
		}
	},

	J(ComplexValue.UNIT_IM_VALUE)
	{
		@Override
		public String toString()
		{
			return "j";
		}

		@Override
		public String toHTML()
		{
			return "j";
		}
	};

	// ========================= FIELDS ====================================

	// Numerical value of the constant
	private final ComplexValue value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param value
	 */
	private ComplexConstant(final ComplexValue value)
	{
		this.value = value;
	}

	// ========================= IMPLEMENTATION: Constant ==================

	/**
	 * @return the value
	 */
	public ComplexValue getValue()
	{
		return value;
	}

	// ========================= METHODS ===================================

	/**
	 * Return the HTML representation of this object.
	 * 
	 * @return the HTML representation of this object
	 */
	public abstract String toHTML();
}
