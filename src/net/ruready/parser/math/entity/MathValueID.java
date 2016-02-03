/*****************************************************************************************
 * Source File: MathValueID.java
 ****************************************************************************************/
package net.ruready.parser.math.entity;

import net.ruready.common.discrete.Identifier;
import net.ruready.common.exception.SystemException;
import net.ruready.common.util.EnumUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.value.LiteralValue;
import net.ruready.parser.math.entity.value.MathValue;

/**
 * Possible math token types. Includes all math value types: numbers, operations,
 * functions, etc. <br>
 * Note 1: the toString() method must obey the general contract of the getType() method.<br>
 * Note 2: the numerical values of IDs must be running and start with zero.
 * <p>
 * Note: logical operations like <code>!, ^</code> etc. are deliberately not included in
 * the parser's design because we mainly deal with floating-point values in our
 * expressions.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 19, 2007
 */
public enum MathValueID implements Identifier
{
	// ========================= ENUMERATED TYPES ==========================

	/**
	 * A discarded token. Might be a fictitious token created during the parser analysis.
	 */
	DISCARDED(0)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "D";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			throw new SystemException(
					"Cannot convert a discarded token from a string to a math value");
		}
	},

	// ========================= ARITHMETIC PARSER TYPES ===================

	/**
	 * A numerical value.
	 */
	ARITHMETIC_NUMBER(1)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "N";
		}

		/**
		 * @param s
		 * @return
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return mode.createValue(s);
		}
	},

	/**
	 * A numerical constant (e.g. pi).
	 */
	ARITHMETIC_CONSTANT(2)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "C";
		}

		/**
		 * @param s
		 * @return
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			// Try matching a complex constant
			ComplexConstant complexConstant = EnumUtil.createFromString(
					ComplexConstant.class, s);
			if (complexConstant != null)
			{
				return mode.createConstantValue(complexConstant, complexConstant
						.getValue());
			}

			// Try matching a real constant
			RealConstant realConstant = EnumUtil.createFromString(RealConstant.class, s);
			if (realConstant != null)
			{
				return mode.createConstantValue(realConstant, realConstant.getValue());
			}

			// Fall back
			throw new SystemException("Cannot convert string '" + s
					+ "' to a math constant");
		}
	},

	/**
	 * A symbolic variable (e.g. "x").
	 */
	ARITHMETIC_VARIABLE(3)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "V";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return new Variable(s);
		}
	},

	/**
	 * A parentheses pair.
	 */
	ARITHMETIC_PARENTHESIS(4)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "P";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return new ParenthesisValue();
		}
	},

	/**
	 * A unary operation or function (e.g. "-" in "-x" or abs()).
	 */
	ARITHMETIC_UNARY_OPERATION(5)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "UOP";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return EnumUtil.createFromString(UnaryOperation.class, s);
		}
	},

	/**
	 * A binary operation (e.g. ^). Some binary operations are multi-nary (e.g. "+") and
	 * are instead assigned the ARITHMETIC_MULTINARY_OPERATION type. Those that are not
	 * multi-nary (e.g. "+") are assigned this type.
	 */
	ARITHMETIC_BINARY_OPERATION(6)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "BOP";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return EnumUtil.createFromString(BinaryOperation.class, s);
		}
	},

	/**
	 * A binary function (e.g. mod(x,y)).
	 */
	ARITHMETIC_BINARY_FUNCTION(7)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "BFUNC";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return EnumUtil.createFromString(BinaryFunction.class, s);
		}
	},

	/**
	 * A multi-nary operation (e.g. "+" with two or more arguments).
	 */
	ARITHMETIC_MULTINARY_OPERATION(8)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "MOP";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return EnumUtil.createFromString(MultinaryOperation.class, s);
		}
	},

	/**
	 * A discarded literal (generated for example for a <degree> or <logbase> MathML
	 * tags).
	 */
	LITERAL(9)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "L";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return new LiteralValue(s);
		}
	},

	// ========================= LOGICAL PARSER TYPES ======================

	/**
	 * A relational operation (e.g. "=", "<").
	 */
	LOGICAL_RELATION_OPERATION(10)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "R";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return EnumUtil.createFromString(RelationOperation.class, s);
		}
	},

	/**
	 * Dummy node generated for single expressions to make them look like // relations
	 */
	LOGICAL_EMPTY(11)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "E";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return EmptyValue.parseEmptyValue(s);
		}
	},

	/**
	 * A response root node in the expression's syntax tree.
	 */
	LOGICAL_RESPONSE(12)
	{
		/**
		 * Return the string representation of this ID.
		 * 
		 * @return the string representation of this ID
		 */
		@Override
		public String toString()
		{
			return "RES";
		}

		/**
		 * @param s
		 * @param mode
		 * @return
		 * @see net.ruready.parser.math.entity.MathValueID#createMathValue(java.lang.String,
		 *      net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
		 */
		@Override
		public MathValue createMathValue(String s, ArithmeticMode mode)
		{
			return ResponseValue.parseResponseValue(s);
		}
	};

	// ========================= FIELDS ====================================

	private int value;

	// ========================= CONSTRUCTORS ==============================

	private MathValueID(int value)
	{
		this.value = value;
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
	 * Convert a String to a <code>MathValue</code> of corresponding type to this math
	 * value type (an enumerated factory method).
	 * 
	 * @param s
	 *            string value
	 * @param mode
	 *            arithmetic mode (complex/real/...)
	 * @return <code>MathValue</code> object
	 */
	abstract public MathValue createMathValue(String s, ArithmeticMode mode);

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

}
