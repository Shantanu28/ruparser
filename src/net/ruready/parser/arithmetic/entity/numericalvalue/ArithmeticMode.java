/*****************************************************************************************
 * Source File: ArithmeticMode.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue;

import net.ruready.common.discrete.Identifier;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ConstantValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue;

/**
 * Types of arithmetic modes: rational arithmetic, real (double) arithmetic.
 * <p>
 * Note: DO NOT OVERRIDE <code>toString()</code>. It must return the same as
 * <code>name()</code> for web app drop down menu label internationalization to work
 * properly.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 15, 2007
 */
public enum ArithmeticMode implements Identifier
{
	// ========================= ENUMERATED TYPES ==========================

	INTEGER(0)
	{
		// TODO: Move control sequence conventions to Names.java
		@Override
		public String controlSequence()
		{
			return "i";
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(double)
		 */
		@Override
		public NumericalValue createValue(double d)
		{
			return new IntegerValue(d);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(String)
		 */
		@Override
		public NumericalValue createValue(String s)
		{
			return new IntegerValue(s);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public IntegerValue createValue(NumericalValue v)
		{
			switch (v.getArithmeticMode())
			{
				case INTEGER:
				{
					return (IntegerValue) v;
				}

				default:
				{
					throw new NumberFormatException(
							"Cannot create an integer value from numerical value of type "
									+ v.getArithmeticMode());
				}
			}
		}

		/**
		 * @param constant
		 * @param v
		 * @return
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createConstantValue(net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public ConstantValue createConstantValue(final Constant constant,
				final NumericalValue v)
		{
			throw new NumberFormatException(
					"Cannot create an integer constant from numerical value");
		}

	},

	RATIONAL(1)
	{
		@Override
		public String controlSequence()
		{
			return "q";
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(double)
		 */
		@Override
		public NumericalValue createValue(double d)
		{
			return new RationalValue(d);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(String)
		 */
		@Override
		public NumericalValue createValue(String s)
		{
			return new RationalValue(s);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public RationalValue createValue(NumericalValue v)
		{
			switch (v.getArithmeticMode())
			{
				case INTEGER:
				{
					return new RationalValue(((IntegerValue) v).getValue());
				}

				case RATIONAL:
				{
					return (RationalValue) v;
				}

				default:
				{
					throw new NumberFormatException(
							"Cannot create an constant value from numerical value of type "
									+ v.getArithmeticMode());
				}
			}
		}

		/**
		 * @param constant
		 * @param v
		 * @return
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createConstantValue(net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public ConstantValue createConstantValue(final Constant constant,
				final NumericalValue v)
		{
			throw new NumberFormatException(
					"Cannot create a rational constant from numerical value");
		}
	},

	REAL(2)
	{
		@Override
		public String controlSequence()
		{
			return "d";
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(double)
		 */
		@Override
		public NumericalValue createValue(double d)
		{
			return new RealValue(d);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(String)
		 */
		@Override
		public NumericalValue createValue(String s)
		{
			return new RealValue(s);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public RealValue createValue(NumericalValue v)
		{
			switch (v.getArithmeticMode())
			{
				case INTEGER:
				{
					return new RealValue(((IntegerValue) v).getValue());
				}

				case RATIONAL:
				{
					return new RealValue(((RationalValue) v).doubleValue());
				}

				case REAL:
				{
					return (RealValue) v;
				}

				default:
				{
					throw new NumberFormatException(
							"Cannot create an integer value from numerical value of type "
									+ v.getArithmeticMode());
				}
			}
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createConstantValue(net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public ConstantValue createConstantValue(final Constant constant,
				final NumericalValue v)
		{
			return new RealConstantValue(constant, this.createValue(v));
		}
	},

	COMPLEX(3)
	{
		@Override
		public String controlSequence()
		{
			return "c";
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(double)
		 */
		@Override
		public NumericalValue createValue(double d)
		{
			return new ComplexValue(d);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(String)
		 */
		@Override
		public NumericalValue createValue(String s)
		{
			return ComplexValue.parseComplex(s);
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public ComplexValue createValue(NumericalValue v)
		{
			switch (v.getArithmeticMode())
			{
				case INTEGER:
				{
					return new ComplexValue(((IntegerValue) v).getValue());
				}

				case RATIONAL:
				{
					return new ComplexValue(((RationalValue) v).doubleValue());
				}

				case REAL:
				{
					return new ComplexValue(((RealValue) v).getValue());
				}

				case COMPLEX:
				{
					return (ComplexValue) v;
				}

				default:
				{
					throw new NumberFormatException(
							"We shouldn't be here - invalid arithmetic mode?! "
									+ v.getArithmeticMode());
				}
			}
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createConstantValue(net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
		 */
		@Override
		public ConstantValue createConstantValue(final Constant constant,
				final NumericalValue v)
		{
			return new ComplexConstantValue(constant, this.createValue(v));
		}
	};

	// ========================= FIELDS ====================================

	// The larger the value, the larger the value field
	private int value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an arithmetic mode.
	 * 
	 * @param value
	 */
	private ArithmeticMode(int value)
	{
		this.value = value;
	}

	/**
	 * Create an arithmetic mode (factory method).
	 * 
	 * @param controlSequence
	 *            control sequence
	 * @return corresponding arithmetic mode
	 */
	public static ArithmeticMode create(String controlSequence)
	{
		if (controlSequence == null)
		{
			return null;
		}
		for (ArithmeticMode t : ArithmeticMode.values())
		{
			if (controlSequence.equals(t.controlSequence()))
			{
				return t;
			}
		}
		return null;
	}

	// ========================= IMPLEMENTATION: Identifier ===================

	/**
	 * Return the object's type. This would normally be the object's
	 * <code>getClass().getSimpleName()</code>, but objects might be wrapped by
	 * Hibernate to return unexpected names. As a rule, the type string should not contain
	 * spaces and special characters.
	 */
	public String getType()
	{
		// The enumerated type's name satisfies all the type criteria, so use
		// it.
		return name();
	}

	// ========================= METHODS ===================================

	/**
	 * Control sequence corresponding to this arithmetic mode.
	 * 
	 * @return control sequence corresponding to this arithmetic mode.
	 */
	abstract public String controlSequence();

	/**
	 * Convert a double to a <code>Value</code> of corresponding type to this arithmetic
	 * mode (an enumerated factory method).
	 * 
	 * @param d
	 *            double value
	 * @return <code>Value</code> object
	 */
	abstract public NumericalValue createValue(double d);

	/**
	 * Convert a string to a <code>Value</code> of corresponding type to this arithmetic
	 * mode (an enumerated factory method).
	 * 
	 * @param s
	 *            input string
	 * @return <code>Value</code> object
	 */
	abstract public NumericalValue createValue(String s);

	/**
	 * Convert a <code>NumericalValue</code> to the <code>NumericalValue</code>
	 * corresponding type to this arithmetic mode (an enumerated factory method). If the
	 * value is already of the correct type, the same reference is returned. Otherwise, a
	 * new object is created.
	 * 
	 * @param v
	 *            a value
	 * @return <code>Value</code> object
	 */
	abstract public NumericalValue createValue(NumericalValue v);

	/**
	 * Create a <code>ConstantValue</code> of the corresponding type to this arithmetic
	 * mode.
	 * 
	 * @param constant
	 *            mathematical constant
	 * @param value
	 *            numerical value of the constant; specifies a field of values to work
	 *            with that might be larger than the natural field of values that
	 *            <code>constant</code> originally belongs to
	 * @return <code>ConstantValue</code> object corresponding to the constant
	 */
	abstract public ConstantValue createConstantValue(final Constant constant,
			final NumericalValue v);

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Return the value of this arithmetic mode.
	 * 
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

}
