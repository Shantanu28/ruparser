/*******************************************************
 * Source File: RealConstant.java
 *******************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.constant;

import net.ruready.common.math.real.RealConstants;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;

/**
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Dec 9, 2005 Mathematical constants, like "pi" and "e".
 */
public enum RealConstant implements Constant
{
	// ========================= CONSTANTS =================================

	E(new RealValue(RealConstants.REAL_E))
	{
		@Override
		public String toString()
		{
			return "e";
		}

		@Override
		public String toHTML()
		{
			return "<span style=\"font-family:times;font-style:italic\">e</span>";
		}
	},

	PI(new RealValue(RealConstants.REAL_PI))
	{
		@Override
		public String toString()
		{
			return "pi";
		}

		@Override
		public String toHTML()
		{
			return "<span style=\"font-family:times\">&pi;</span>";
		}
	},

	GAMMA(new RealValue(RealConstants.REAL_GAMMA))
	{
		// Euler's gamma constant
		@Override
		public String toString()
		{
			return "gamma";
		}

		@Override
		public String toHTML()
		{
			return "<span style=\"font-family:times\">&gamma;</span>";
		}
	};

	// ========================= FIELDS ====================================

	// Numerical value of the constant
	private final RealValue value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param value
	 */
	private RealConstant(final RealValue value)
	{
		this.value = value;
	}

	// ========================= IMPLEMENTATION: Constant ==================

	/**
	 * @return the value
	 */
	public RealValue getValue()
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
