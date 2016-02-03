package net.ruready.parser.logical.entity.value;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

/**
 * Relational/comparison operations between a pair of <code>Expression<code>s:
 * <code>=, !=, <=, >=, <, ></code>.
 * <p>           
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * (c) 2006-07 Continuing Education , University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i>
 *          Academic Outreach and Continuing Education (AOCE)
 *          1901 East South Campus Dr., Room 2197-E
 *          University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i>
 *          AOCE, Room 2197-E, University of Utah
 *
 * @version Jul 2, 2007
 */
public enum RelationOperation implements LogicalValue
{
	// ========================= ENUMERATED CONSTANTS ======================

	/**
	 * Used in equations "x assigns_the_value_of y", non-commutative. An expression "x is
	 * interpreted as the statement "x := null".
	 */
	ASSIGN
	{
		@Override
		public String toString()
		{
			return ":=";
		}

		/**
		 * Does not have an inverse.
		 * 
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return null;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return true;
		}
	},

	/**
	 * Used in equations "x = y"; commutative.
	 */
	EQ
	{
		@Override
		public String toString()
		{
			return "=";
		}

		/**
		 * Commutative; is the inverse of itself.
		 * 
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return EQ;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return true;
		}
	},

	/**
	 * Not equals relation.
	 */
	NE
	{
		@Override
		public String toString()
		{
			return "!=";
		}

		/**
		 * Commutative; is the inverse of itself.
		 * 
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return NE;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return true;
		}
	},

	/**
	 * Less-than-or-equals relation.
	 */
	LE
	{
		@Override
		public String toString()
		{
			return "<=";
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return GE;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return true;
		}
	},

	/**
	 * Greater-than-or-equals relation.
	 */
	GE
	{
		@Override
		public String toString()
		{
			return ">=";
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return LE;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return false;
		}
	},

	/**
	 * Less-than relation.
	 */
	LT
	{
		@Override
		public String toString()
		{
			return "<";
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return GT;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return true;
		}
	},

	/**
	 * Greater-than relation.
	 */
	GT
	{
		@Override
		public String toString()
		{
			return ">";
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return LT;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return false;
		}
	},

	/**
	 * Rule implication, as in : depdendentRule :- indepdendentRuleSet
	 */
	IF
	{
		@Override
		public String toString()
		{
			return ":-";
		}

		/**
		 * Does not have an inverse.
		 * 
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#inverse()
		 */
		@Override
		public RelationOperation inverse()
		{
			return null;
		}

		/**
		 * @see net.ruready.parser.logical.entity.value.RelationOperation#isCanonical()
		 */
		@Override
		public boolean isCanonical()
		{
			return true;
		}
	};

	// ========================= CONSTANTS =================================

	// The implicit relational operation for a single-expression-relation
	public final static RelationOperation SINGLE_EXPRESSION_OP = RelationOperation.ASSIGN;

	// Left/first operand child node of a relation index in a syntax tree
	public static final int LEFT = 0;

	// Right/second operand child node of a relation index in a syntax tree
	public static final int RIGHT = 1;

	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: Object ====================

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
		RelationOperation other = (RelationOperation) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
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
		return MathValueID.LOGICAL_RELATION_OPERATION;
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

	// ========================= METHODS ===================================

	/**
	 * "Inverse" operation of this operation, in the sense of a op b = b inv_op a. inv_op
	 * cannot be equal to op. If it does, this method returns null.
	 * 
	 * @return inverse operation
	 */
	public abstract RelationOperation inverse();

	/**
	 * Does the operation have an "inverse" operation of this operation in the sense of a
	 * op b = b inv_op a.
	 * 
	 * @return is operation "anti-commutative" = has an "inverse"
	 */
	public boolean hasInverse()
	{
		return inverse() != null;
	}

	/**
	 * Is this operation in canonical form.
	 * <ul>
	 * <li>If the operation does not have an inverse, it is always in canonical form.</li>
	 * <li>If the operation has an inverse, then:
	 * <ul>
	 * <li>If it equals its inverse, it is in canonical form.</li>
	 * <li>If it does not equal its inverse, either it is in canonical form, or its
	 * inverse is, but not both.</li>
	 * </ul>
	 * </ul>
	 * By convention, the canonical invertible operations whose inverse is not themselves
	 * are <Code>&lt;=, &lt;</code>.
	 * 
	 * @return Is this operation in canonical form
	 */
	public abstract boolean isCanonical();

	// ========================= GETTERS & SETTERS =========================
}
