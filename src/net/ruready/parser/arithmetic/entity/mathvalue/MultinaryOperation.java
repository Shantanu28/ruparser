/*****************************************************************************************
 * Source File: MultinaryOp.java
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
 * /** Multi-nary operations of values (x1 op x2 op ... op xn).
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 20, 2007
 */
public enum MultinaryOperation implements Multinary
{
	// ========================= ENUMERATED TYPES ==========================

	PLUS
	{
		@Override
		public String toString()
		{
			return "+";
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.Multinary#eval(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue[])
		 */
		public NumericalValue eval(NumericalValue[] x)
		{
			assert (x.length >= 2);
			NumericalValue result = x[0];
			for (int i = 1; i < x.length; i++)
			{
				result = result.PLUS(x[i]);
			}
			return result;
		}

		@Override
		public boolean isCommutative()
		{
			return true;
		}

		@Override
		public boolean isLeftAssociated()
		{
			return false;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return UnaryOperation.PLUS;
		}
	},

	TIMES
	{
		@Override
		public String toString()
		{
			return "*";
		}

		/**
		 * @see net.ruready.parser.arithmetic.entity.mathvalue.Multinary#eval(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue[])
		 */
		public NumericalValue eval(NumericalValue[] x)
		{
			assert (x.length >= 2);
			NumericalValue result = x[0];
			for (int i = 1; i < x.length; i++)
			{
				result = result.TIMES(x[i]);
			}
			return result;
		}

		@Override
		public boolean isCommutative()
		{
			return true;
		}

		@Override
		public boolean isLeftAssociated()
		{
			return false;
		}

		@Override
		public UnaryOperation unaryOpForm()
		{
			return UnaryOperation.TIMES;
		}
	};

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
		return MathValueID.ARITHMETIC_MULTINARY_OPERATION;
	}

	// ========================= IMPLEMENTATION: MathValue =================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "MultinaryOp";
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
		MultinaryOperation other = (MultinaryOperation) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= METHODS ===================================

	/**
	 * Is the operation commutative or not.
	 * 
	 * @return is the operation commutative or not.
	 */
	public abstract boolean isCommutative();

	/**
	 * Does operation associate to the left or not. E.g., "-" associates to the left,
	 * because a-b-c = (a-b)-c. "^" associates to the right, because a^b^c = a^(b^c). If
	 * order of arguments doesn't matter ("associative operation") or not applicable, this
	 * method returns false.
	 * 
	 * @return Does operation associate to the left
	 */
	public abstract boolean isLeftAssociated();

	/**
	 * If this binary operation has a unary form (+,-,* have one), return it. If the unary
	 * form doesn't exist, returns null.
	 * 
	 * @return unary form of this binary operation; if not found, null
	 */
	public abstract UnaryOperation unaryOpForm();

}
