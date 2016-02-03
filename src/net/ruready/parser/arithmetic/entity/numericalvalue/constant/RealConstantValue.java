/*****************************************************************************************
 * Source File: RealConstantValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.constant;

import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a real-valued mathematical constant like "i", and implements
 * {@link NumericalValue}. Cannot do this directly with an enum like {@link RealConstant}.
 * 
 * @immutable
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Jun 11, 2007
 */
public class RealConstantValue extends RealValue implements ConstantValue
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RealConstantValue.class);

	// ========================= FIELDS ====================================

	/**
	 * Corresponding enum type representing this constant.
	 */
	private final Constant constant;

	/**
	 * Numerical value of this constant.
	 */
	private final RealValue value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a real--valued mathematical constant.
	 * 
	 * @param constant
	 *            Corresponding enum type
	 * @param value
	 *            value of constant (must be a {@link RealValue} because this class
	 *            extends {@link RealValue})
	 */
	public RealConstantValue(final Constant constant, final RealValue value)
	{
		super(value);
		this.constant = constant;
		this.value = value;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return constant.toString();
	}

	// ========================= METHODS ===================================

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target (or part of a target) of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public RealConstantValue clone()
	{
		// try {
		RealConstantValue copy = (RealConstantValue) super.clone();
		return copy;
		// }
		// catch (CloneNotSupportedException e) {
		// // this shouldn't happen, because we are Cloneable
		// throw new InternalError("clone() failed: " + e.getMessage());
		// }
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	@Override
	public MathValueID getIdentifier()
	{
		return MathValueID.ARITHMETIC_CONSTANT;
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
	@Override
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		MathValueVisitorUtil.accept(this, visitor);
	}

	// ========================= IMPLEMENTATION: ConstantValue =============

	/**
	 * @return the constant
	 */
	public Constant getConstant()
	{
		return constant;
	}

	/**
	 * @return the value
	 */
	public RealValue getNumericalValue()
	{
		return value;
	}

	// ========================= GETTERS & SETTERS =========================
}
