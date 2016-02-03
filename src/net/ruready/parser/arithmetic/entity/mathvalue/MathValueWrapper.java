/*****************************************************************************************
 * Source File: MathValueWrapper.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.value.LiteralValue;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.ArithmeticValueVisitor;
import net.ruready.parser.math.entity.visitor.FullMathValueVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A wrapper around any object that also implements the <code>MathValue</code>
 * interface. Useful to wrap around tokens like parentheses in the arithmetic parser,
 * during syntax tree building.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 24, 2007
 */
public abstract class MathValueWrapper<T> implements ArithmeticValue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathValueWrapper.class);

	// ========================= FIELDS ====================================

	// String corresponding to this variable
	private T object;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a wrapper around an object;
	 * 
	 * @param object
	 *            object to be wrapped
	 */
	public MathValueWrapper(T object)
	{
		super();
		this.object = object;
	}

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
		// It seems like casting is necessary, because there is no obvious way
		// to dynamically cast to the correct visitor type at run-time without
		// complicating the Visitor's or Arbiter's interfaces.
		if (visitor instanceof ArithmeticValueVisitor)
		{
			((ArithmeticValueVisitor) visitor).visit(this);
		}
		else if (visitor instanceof FullMathValueVisitor)
		{
			((FullMathValueVisitor) visitor).visit(this);
		}
		else
		{
			visitor.visit(this);
		}
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
		if ((obj == null) || (!(obj instanceof MathValueWrapper)))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		LiteralValue other = (LiteralValue) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the object
	 */
	public T getObject()
	{
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(T object)
	{
		this.object = object;
	}
}
