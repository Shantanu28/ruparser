/*****************************************************************************************
 * Source File: ComplexConstantValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.constant;

import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a real- or complex-valued mathematical constant like "i", and implements
 * {@link NumericalValue}. Cannot do this directly with an enum like
 * {@link ComplexConstant}.
 * 
 * @immutable
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
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 14, 2007
 */
public class ComplexConstantValue extends ComplexValue implements ConstantValue
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
	private static final Log logger = LogFactory.getLog(ComplexConstantValue.class);

	// ========================= FIELDS ====================================

	/**
	 * Corresponding enum type representing this constant.
	 */
	private final Constant constant;

	/**
	 * Numerical value of this constant.
	 */
	private final ComplexValue value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a real- or complex-valued mathematical constant.
	 * 
	 * @param constant
	 *            Corresponding enum type
	 * @param value
	 *            value of constant (must be a {@link ComplexValue} because this class
	 *            extends {@link ComplexValue})
	 */
	public ComplexConstantValue(final Constant constant, final ComplexValue value)
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

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target (or part of a target) of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public ComplexConstantValue clone()
	{
		// try {
		ComplexConstantValue copy = (ComplexConstantValue) super.clone();
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
	public ComplexValue getNumericalValue()
	{
		return value;
	}

	// ========================= GETTERS & SETTERS =========================
}
