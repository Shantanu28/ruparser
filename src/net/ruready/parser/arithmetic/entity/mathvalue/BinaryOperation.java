/*****************************************************************************************
 * Source File: BinaryOp.java
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
 * Binary operations of two doubles: +, -, etc. Distinguished from binary functions of two
 * doubles; see BinaryFunc. Operations' syntax is a op b, whereas functions' syntax is
 * <code>func(a,b)</code>.
 * <p>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br> (c) 2006-07 Continuing Education ,
 * University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah
 * @version Jul 2, 2007
 */
public enum BinaryOperation implements BinaryValue
{
	// ========================= CONSTANTS =================================

	/**
	 * The sum of a and b.
	 */
	PLUS
	{
		@Override
		public String toString()
		{
			return "+";
		}

		public NumericalValue eval(NumericalValue x, NumericalValue y)
		{
			return x.PLUS(y);
		}

		@Override
		public BinaryOperation inverse()
		{
			return BinaryOperation.MINUS;
		}

		@Override
		public boolean isCommutative()
		{
			return true;
		}

		@Override
		public AssociationType getAssociationType()
		{
			return AssociationType.BOTH;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return UnaryOperation.PLUS;
		}
	},

	/**
	 * The difference of a and b.
	 */
	MINUS
	{
		@Override
		public String toString()
		{
			return "-";
		}

		public NumericalValue eval(NumericalValue x, NumericalValue y)
		{
			return x.MINUS(y);
		}

		@Override
		public BinaryOperation inverse()
		{
			return BinaryOperation.PLUS;
		}

		@Override
		public boolean isCommutative()
		{
			return false;
		}

		@Override
		public AssociationType getAssociationType()
		{
			return AssociationType.LEFT;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return UnaryOperation.MINUS;
		}
	},

	/**
	 * The product of a and b.
	 */
	TIMES
	{
		@Override
		public String toString()
		{
			return "*";
		}

		public NumericalValue eval(NumericalValue x, NumericalValue y)
		{
			return x.TIMES(y);
		}

		@Override
		public BinaryOperation inverse()
		{
			return BinaryOperation.DIVIDE;
		}

		@Override
		public boolean isCommutative()
		{
			return true;
		}

		@Override
		public AssociationType getAssociationType()
		{
			return AssociationType.BOTH;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return UnaryOperation.TIMES;
		}
	},

	/**
	 * The quotient of a and b.
	 */
	DIVIDE
	{
		@Override
		public String toString()
		{
			return "/";
		}

		public NumericalValue eval(NumericalValue x, NumericalValue y)
		{
			return x.OVER(y);
		}

		@Override
		public BinaryOperation inverse()
		{
			return BinaryOperation.TIMES;
		}

		@Override
		public boolean isCommutative()
		{
			return false;
		}

		@Override
		public AssociationType getAssociationType()
		{
			return AssociationType.LEFT;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return UnaryOperation.DIVIDE;
		}
	},

	/**
	 * The b<i>th</i> power of a.
	 */
	POWER
	{
		@Override
		public String toString()
		{
			return "^";
		}

		public NumericalValue eval(NumericalValue x, NumericalValue y)
		{
			return x.POWER(y);
		}

		@Override
		public BinaryOperation inverse()
		{
			return null;
		}

		@Override
		public boolean isCommutative()
		{
			return false;
		}

		@Override
		public AssociationType getAssociationType()
		{
			return AssociationType.RIGHT;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return null;
		}
	},

	/**
	 * The remainder of a when divided by b over the integers.
	 */
	MOD
	{
		@Override
		public String toString()
		{
			return "%";
		}

		public NumericalValue eval(NumericalValue x, NumericalValue y)
		{
			return x.MOD(y);
		}

		@Override
		public BinaryOperation inverse()
		{
			return null;
		}

		@Override
		public boolean isCommutative()
		{
			return false;
		}

		@Override
		public AssociationType getAssociationType()
		{
			return AssociationType.LEFT;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return null;
		}
	};

	// ################## CONSTRUCTORS #######################

	public static BinaryOperation inverse(BinaryOperation s)
	{
		switch (s)
		{
			case PLUS:
			{
				return MINUS;
			}

			case MINUS:
			{
				return PLUS;
			}

			case TIMES:
			{
				return DIVIDE;
			}

			case DIVIDE:
			{
				return TIMES;
			}

			default:
			{
				break;
			}
		}
		return null;
	}

	// ========================= FIELDS ====================================

	// Implicit multiplication mode
	// protected boolean implicitMultiplication = false;

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

	// ========================= IMPLEMENTATION: Identifiable ===============

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
		return MathValueID.ARITHMETIC_BINARY_OPERATION;
	}

	// ========================= IMPLEMENTATION: MathValue =================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "BinaryOp";
	}

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

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isSignOp()
	 */
	public boolean isSignOp()
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
		BinaryOperation other = (BinaryOperation) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= METHODS ===================================

	/**
	 * Inverse operation of this operation. If it does not have a supported inverse,
	 * returns null.
	 * 
	 * @return inverse operation of this operation
	 */
	public abstract BinaryOperation inverse();

	/**
	 * Is the operation commutative or not.
	 * 
	 * @return is the operation commutative or not.
	 */
	public abstract boolean isCommutative();

	/**
	 * Does operation associate to the left, right, or both.
	 * 
	 * @return operation's association type
	 */
	public abstract AssociationType getAssociationType();

	/**
	 * If this binary operation has a unary form (+,-,* have one), return it. If the unary
	 * form doesn't exist, returns null.
	 * 
	 * @return unary form of this binary operation; if not found, null
	 */
	public abstract UnaryOperation unaryOpForm();

	/**
	 * If a parent and a child, which are both of the same operation type that has this
	 * association type, are considered for deciding whether parentheses around the child
	 * are required, the parentheses are required and not redundant if and only if the
	 * child's index in the parent's children array equals the result of this method.
	 * 
	 * @return child index for which parentheses around this operation are redundant, if
	 *         it is
	 */
	public int getRequiredParenthesesAroundChildIndex()
	{
		return getAssociationType().getRequiredParenthesesAroundChildIndex();
	}

	/**
	 * Is this operation associative.
	 * 
	 * @return <code>true</code> iff this operation is associative
	 */
	public boolean isAssociative()
	{
		return this.getAssociationType().isAssociative();
	}

	/**
	 * multi-nary operation inverse of the binary operation counterpart of this operation.
	 * If it does not have a one, returns <code>null</code>.
	 * 
	 * @return multi-nary operation counterpart of this operation
	 */
	public MultinaryOperation multinaryOpForm()
	{
		return (unaryOpForm() == null) ? null : unaryOpForm().multinaryOpForm();
	}

	// ========================= GETTERS & SETTERS =========================

}
