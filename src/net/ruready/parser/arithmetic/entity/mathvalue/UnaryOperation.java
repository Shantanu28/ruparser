/*****************************************************************************************
 * Source File: UnaryOp.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

/**
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Dec 9, 2005 All supported arithmetic unary functions, like "sin" or "cos".
 */
public enum UnaryOperation implements ArithmeticValue
{
	// ========================= ENUMERATED TYPES ==========================

	ABS
	{
		@Override
		public String toString()
		{
			return "abs";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ABS();
		}
	},

	ARCCOS
	{
		@Override
		public String toString()
		{
			return "arccos";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ACOS();
		}
	},

	ARCCOSH
	{
		@Override
		public String toString()
		{
			return "arccosh";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ACOSH();
		}
	},

	ARCSIN
	{
		@Override
		public String toString()
		{
			return "arcsin";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ASIN();
		}
	},

	ARCSINH
	{
		@Override
		public String toString()
		{
			return "arcsinh";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ASINH();
		}
	},

	ARCTAN
	{
		@Override
		public String toString()
		{
			return "arctan";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ATAN();
		}
	},

	ARCTANH
	{
		@Override
		public String toString()
		{
			return "arctanh";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.ATANH();
		}
	},

	CBRT
	{
		@Override
		public String toString()
		{
			return "cbrt";
		}

		/*
		 * // 8731 unicode character doesn't seem to be recognized by IE * Generate an
		 * HTML string from an operand. @param x operand's HTML value. @param whiteSpace
		 * if true, adds some white space in the HTML string (has no effect here) @return
		 * HTML value of operand after applying this unary operation @Override public
		 * String toHTML(String x, boolean whiteSpace) { // Make sure to add function's
		 * parentheses // around operand in the HTML representation return "&#8731;" + "<span
		 * style=\"text-decoration:overline\">" + x + "</span>"; }
		 */

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.CBRT();
		}
	},

	EMPTY
	{
		@Override
		public String toString()
		{
			return "empty";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x;
		}
	},

	CEILING
	{
		@Override
		public String toString()
		{
			return "ceiling";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.CEIL();
		}
	},

	COS
	{
		@Override
		public String toString()
		{
			return "cos";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.COS();
		}
	},

	COSH
	{
		@Override
		public String toString()
		{
			return "cosh";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.COSH();
		}
	},

	COT
	{
		@Override
		public String toString()
		{
			return "cot";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.COT();
		}
	},

	CSC
	{
		@Override
		public String toString()
		{
			return "csc";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.CSC();
		}
	},

	DIVIDE
	{
		// Unary form of a multiplication: "1/x"
		@Override
		public String toString()
		{
			return "/";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.RECIP();
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation#isSymbolOp()
		 */
		@Override
		public boolean isSymbolOp()
		{
			return true;
		}

		/**
		 * Binary operation counterpart of this operation. If it does not have a one,
		 * returns <code>null</code>.
		 * 
		 * @return binary operation counterpart of this operation
		 */
		@Override
		public BinaryOperation binaryOpForm()
		{
			return BinaryOperation.DIVIDE;
		}
	},

	EXP
	{
		@Override
		public String toString()
		{
			return "exp";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.EXP();
		}
	},

	FACTORIAL
	// factorial of integers/NumericalValues (gamma function for
	// non-integer arguments)
	{
		@Override
		public String toString()
		{
			return "factorial";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.FACT();
		}
	},

	FLOOR
	{
		@Override
		public String toString()
		{
			return "floor";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.FLOOR();
		}
	},

	LN
	// ln is base-e (natural)
	{
		@Override
		public String toString()
		{
			return "ln";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.LN();
		}
	},

	LOG
	// Log base 10
	{
		@Override
		public String toString()
		{
			return "log";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.LOG10();
		}
	},

	MINUS
	{
		// Unary form of a minus: "0-x"
		@Override
		public String toString()
		{
			return "-";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.NEGATE();
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation#isSignOp()
		 */
		@Override
		public boolean isSignOp()
		{
			return true;
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation#isSymbolOp()
		 */
		@Override
		public boolean isSymbolOp()
		{
			return true;
		}

		/**
		 * Binary operation counterpart of this operation. If it does not have a one,
		 * returns <code>null</code>.
		 * 
		 * @return binary operation counterpart of this operation
		 */
		@Override
		public BinaryOperation binaryOpForm()
		{
			return BinaryOperation.MINUS;
		}
	},

	PLUS
	{
		// Unary form of a plus: "0+x"
		@Override
		public String toString()
		{
			return "+";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x;
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation#isSignOp()
		 */
		@Override
		public boolean isSignOp()
		{
			return true;
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation#isSymbolOp()
		 */
		@Override
		public boolean isSymbolOp()
		{
			return true;
		}

		/**
		 * Binary operation counterpart of this operation. If it does not have a one,
		 * returns <code>null</code>.
		 * 
		 * @return binary operation counterpart of this operation
		 */
		@Override
		public BinaryOperation binaryOpForm()
		{
			return BinaryOperation.PLUS;
		}

		/**
		 * If this operation has a multi-nary form (e.g. +, *), return it. If the unary
		 * form doesn't exist, returns null.
		 * 
		 * @return multi-nary form of this operation; if not found, null
		 */
		@Override
		public MultinaryOperation multinaryOpForm()
		{
			return MultinaryOperation.PLUS;
		}
	},

	ROOT
	// Square root
	{
		@Override
		public String toString()
		{
			return "root";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.SQRT();
		}
	},

	SEC
	{
		@Override
		public String toString()
		{
			return "sec";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.SEC();
		}
	},

	SGN
	{
		// Real part sign
		@Override
		public String toString()
		{
			return "sgn";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.SGN();
		}
	},

	SIN
	{
		@Override
		public String toString()
		{
			return "sin";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.SIN();
		}
	},

	SINH
	{
		@Override
		public String toString()
		{
			return "sinh";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.SINH();
		}
	},

	SQRT
	{
		@Override
		public String toString()
		{
			return "sqrt";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.SQRT();
		}
	},

	TIMES
	{
		// Unary form of a multiplication: "1*x"
		@Override
		public String toString()
		{
			return "*";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x;
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation#isSymbolOp()
		 */
		@Override
		public boolean isSymbolOp()
		{
			return true;
		}

		/**
		 * Binary operation counterpart of this operation. If it does not have a one,
		 * returns <code>null</code>.
		 * 
		 * @return binary operation counterpart of this operation
		 */
		@Override
		public BinaryOperation binaryOpForm()
		{
			return BinaryOperation.TIMES;
		}

		/**
		 * If this operation has a multi-nary form (e.g. +, *), return it. If the unary
		 * form doesn't exist, returns null.
		 * 
		 * @return multi-nary form of this operation; if not found, null
		 */
		@Override
		public MultinaryOperation multinaryOpForm()
		{
			return MultinaryOperation.TIMES;
		}
	},

	TAN
	{
		@Override
		public String toString()
		{
			return "tan";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.TAN();
		}
	},

	TANH
	{
		@Override
		public String toString()
		{
			return "tanh";
		}

		@Override
		public NumericalValue eval(NumericalValue x)
		{
			return x.TANH();
		}
	};

	// ========================= CONSTANTS =================================

	// Number of operands of a unary operation
	public static final int NUM_OPERANDS = 1;

	// The only operand child node index in a syntax tree
	public static final int ARG = 0;

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: Visitable<Visitor<MathValue>>

	/**
	 * Let a visitor process this value type. Part of the TreeVisitor pattern. This calls
	 * back the visitor's <code>visit()</code> method with this item type. Must be
	 * overridden by every item sub-class.
	 * 
	 * @param <B>
	 *            specific visitor type; this method may be implemented for multiple
	 *            classes of {@link Visitable}.
	 * @param visitor
	 *            item visitor whose <code>visit()</code> method is invoked
	 * @see net.ruready.common.visitor.Visitable#accept(net.ruready.common.visitor.Visitor)
	 */
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		MathValueVisitorUtil.accept(this, visitor);
	}

	// ========================= IMPLEMENTATION: MathValue =================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isArithmeticOperation()
	 */
	public boolean isArithmeticOperation()
	{
		return true;
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isNumerical()
	 */
	public boolean isNumerical()
	{
		return false;
	}

	// ========================= IMPLEMENTATION: TolerantlyComparable ======

	/**
	 * Result of equality of two tokens up to a finite tolerance. Delegates to their
	 * values. If at least one of the values is <code>null</code>, they are not equal.
	 * 
	 * @param obj
	 *            The other <code>MathToken</code> object.
	 * @param tol
	 *            tolerance of equality, if we compare to a finite precision (for n digits
	 *            of accuracy, use tol = 10^{-n}).
	 * @return the result of tolerant equality of two evaluable quantities. Returns
	 *         {@link #EQUAL} if they are tolerantly equal; returns {@link #INDETERMINATE}
	 *         if tolerant equality cannot be returned; otherwise, returns a number that
	 *         is different from both constants, e.g., {@link #NON_EQUAL}.
	 */
	final public int tolerantlyEquals(MathValue obj, double tol)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		// logger.trace("MathToken tolerantlyEquals(): this.class " +
		// getClass() + " obj.class " + obj.getClass());
		if (this == obj)
		{
			return TolerantlyComparable.EQUAL;
		}
		// The following clause is usually to be avoided, but doesn't violate
		// the symmetry principle because this method is declared final. On
		// the other hand, we do need sub-classes to be compared with
		// MathToken.
		if ((obj == null) || (getClass() != obj.getClass()))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		UnaryOperation other = (UnaryOperation) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathValueID getIdentifier()
	{
		return MathValueID.ARITHMETIC_UNARY_OPERATION;
	}

	// ========================= METHODS ===================================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "UnaryOp";
	}

	/**
	 * Do arithmetic operation represented by this <code>ArithmeticUnary</code> on a
	 * number (op(x)).
	 * 
	 * @param x
	 *            the operand.
	 * @return result of unary operation.
	 */
	public abstract NumericalValue eval(NumericalValue x);

	/**
	 * Is this a sign operation (unary +/-) or not.
	 * 
	 * @return is this a sign operation (unary +/-) or not
	 */
	public boolean isSignOp()
	{
		return false;
	}

	/**
	 * Is the string representation of this operation a symbol (like +,-) or not.
	 * 
	 * @return is the string representation of this operation a symbol (like +,-) or not
	 */
	public boolean isSymbolOp()
	{
		return false;
	}

	/**
	 * Binary operation counterpart of this operation. If it does not have a one, returns
	 * <code>null</code>.
	 * 
	 * @return binary operation counterpart of this operation
	 */
	public BinaryOperation binaryOpForm()
	{
		return null;
	}

	/**
	 * Binary operation inverse of the binary operation counterpart of this operation. If
	 * it does not have a one, returns <code>null</code>.
	 * 
	 * @return binary operation counterpart of this operation
	 */
	public BinaryOperation binaryOpInverse()
	{
		return (binaryOpForm() == null) ? null : binaryOpForm().inverse();
	}

	/**
	 * If this operation has a multi-nary form (e.g. +, *), return it. If the unary form
	 * doesn't exist, returns null.
	 * 
	 * @return multi-nary form of this operation; if not found, null
	 */
	public MultinaryOperation multinaryOpForm()
	{
		return null;
	}

	/**
	 * multi-nary operation form of the binary inverse of this operation. If it does not
	 * have a one, returns <code>null</code>.
	 * 
	 * @return multi-nary operation form of the binary inverse of this operation
	 */
	public MultinaryOperation multinaryOpInverse()
	{
		BinaryOperation binaryOpForm = binaryOpForm();
		if (binaryOpForm == null)
		{
			return null;
		}
		BinaryOperation binaryOpInverse = binaryOpForm.inverse();
		if (binaryOpInverse == null)
		{
			return null;
		}
		UnaryOperation unaryOpInverse = binaryOpInverse.unaryOpForm();
		if (unaryOpInverse == null)
		{
			return null;
		}
		return unaryOpInverse.multinaryOpForm();
	}
}
