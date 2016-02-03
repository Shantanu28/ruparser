/*****************************************************************************************
 * Source File: MathToken.java
 ****************************************************************************************/
package net.ruready.parser.math.entity;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.common.pointer.ValueObject;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.common.util.HashCodeUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.common.visitor.Visitor;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathTokenTraversalVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a single token in a mathematical expression. This includes information on
 * its index in the assembly, its type, its mark-up status. This can be a list of tokens
 * that represent one operation: a pair of parentheses, or a sequence of unary "+"'s
 * ("-"'s).
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
public class MathToken implements ValueObject, PubliclyCloneable, Comparable<MathToken>,
		TolerantlyComparable<MathToken>, Visitable<MathTokenTraversalVisitor>
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
	private static final Log logger = LogFactory.getLog(MathToken.class);

	// ========================= FIELDS ====================================

	/**
	 * Status of the token (correct, discarded, etc.)
	 */
	protected MathTokenStatus status = MathTokenStatus.DISCARDED;

	/**
	 * NumericalValue of this token. For an operation, this is a BinaryOp/UnaryOp/...; for
	 * a number, this is a NumericalNumericalValue; and so on. We sort of assume that
	 * value is always non-null.
	 */
	MathValue value = null;

	/**
	 * List of elements in the token array of the original assembly that correspond to
	 * this math token. Note that a token may be non- continugous in the original string
	 * (e.g. parentheses).
	 */
	private List<Integer> elements = new ArrayList<Integer>();

	/**
	 * Holds a traversal index. This is a temporary field, so it is safe to assume it
	 * holds the result of the last traversal operation run on the tree. See
	 * {@link SetTraversalIndex}. May be set by classes that are "friends" of this class
	 * (implement the {@link Visitor}<code>MathToken</code> interface).
	 */
	private int traversalIndex;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a math token from a value only.
	 * 
	 * @param value
	 *            Mathematical value of object (numeric, string, token, etc.)
	 */
	private MathToken(MathValue value)
	{
		super();
		this.value = value;
	}

	/**
	 * Construct a math token.
	 * 
	 * @param element
	 *            index of at least one element in the original assembly's token array
	 *            that this object corresponds to
	 * @param value
	 *            Mathematical value of object (numeric, string, token, etc.)
	 */
	public MathToken(int element, MathValue value)
	{
		super();
		this.value = value;
		add(element);
	}

	/**
	 * Construct a math token.
	 * 
	 * @param element
	 *            index of at least one element in the original assembly's token array
	 *            that this object corresponds to
	 * @param value
	 *            Mathematical value of object (numeric, string, token, etc.)
	 * @param status
	 *            status (markup) of this token
	 */
	public MathToken(int element, MathValue value, MathTokenStatus status)
	{
		super();
		this.value = value;
		this.status = status;
		add(element);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Print a math token. The format is "ID:VALUE:STATUS" where ID is the math value's
	 * ID, VALUE is the value, and STATUS is the token's status.
	 * 
	 * @return A string with the token analysis' internal info.
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(this.getValueID());
		s.append(CommonNames.TREE.SEPARATOR);
		s.append(value);
		s.append(CommonNames.TREE.SEPARATOR);
		s.append(status);
		return s.toString();
	}

	/**
	 * Must be overridden when <code>equals()</code> is.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int result = HashCodeUtil.SEED;
		// Mandatory fields used in equals()
		result = HashCodeUtil.hash(result, value);
		// Optional fields that are likely to be different for different
		// instances
		result = HashCodeUtil.hash(result, status);
		return result;
	}

	/**
	 * Compare two math tokens based on their test data level.
	 * 
	 * @param other
	 *            Other math token to compare with this one.
	 * @return result of equality
	 */
	@Override
	public boolean equals(Object obj)
	{
		// TODO: replace with Double equality
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		if (this == obj)
		{
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}
		// Cast to friendlier version
		MathToken other = (MathToken) obj;

		return (this.compareTo(other) == 0);
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public MathToken clone()
	{
		// try {
		// MathToken copy = (MathToken) super.clone();
		//
		// // NumericalValue is immutable so it was already copied at this
		// // point
		//
		// return copy;
		// }
		// catch (CloneNotSupportedException e) {
		// // this shouldn't happen, because we are Cloneable
		// throw new InternalError("clone() failed: " + e.getMessage());
		// }

		// I am not trusting automatic copying using super.clone() when we
		// have potentially mutable fields. Let's do it manually here
		// until proven too cumbersome.
		MathToken copy = new MathToken(value);
		copy.status = status;
		copy.elements = new ArrayList<Integer>(elements);
		copy.traversalIndex = traversalIndex;
		return copy;
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Compare two math tokens based on their type, value and status. Element indices are
	 * ignored. This defines a consistent partial-order relation on the space of
	 * <code>MathToken</code>s.
	 * 
	 * @param other
	 *            Other <code>MathToken</code> to compare with this one.
	 * @return result of comparison (-1, 0, 1)
	 */
	public int compareTo(MathToken other)
	{
		// logger.debug("compareTo(), this " + this + " other " + other);
		// Compare value types by their natural ordering
		int compareType = this.getValueID().compareTo(other.getValueID());
		if (compareType != 0)
		{
			// logger.debug(" type " + getValueID() + "," + other.getValueID() +
			// " ==> "
			// + compareType);
			return compareType;
		}

		int compareValue;
		// type is equal, compare by numerical value if this value is numerical;
		// otherwise, compare the value string representations
		// No need to check that both values are numerical, because we already
		// know their type is equal.
		if (value.isNumerical() /* && other.value.isNumerical() */)
		{
			NumericalValue thisNumericalValue = (NumericalValue) value;
			NumericalValue otherNumericalValue = (NumericalValue) other.value;
			compareValue = (thisNumericalValue == null) ? ((otherNumericalValue == null) ? 0
					: -1)
					: thisNumericalValue.compareTo(otherNumericalValue);
			// logger.debug(" value " + thisNumericalValue + "," +
			// otherNumericalValue + " ==> "
			// + compareValue);
		}
		else
		{
			// compare by value string representation
			compareValue = (value == null) ? ((other.value == null) ? 0 : -1) : this
					.getValueAsString().compareTo(other.getValueAsString());
			// logger.debug(" value string " + value + "," + other.value +
			// " ==> "
			// + compareValue);
		}
		if (compareValue != 0)
		{
			return compareValue;
		}

		// Compare by status
		int compareStatus = status.compareTo(other.getStatus());
		// if (compareStatus != 0) {
		// logger.debug(" status " + status + "," + other.getStatus() + " ==> "
		// + compareStatus);
		return compareStatus;
		// }
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
	final public int tolerantlyEquals(MathToken obj, double tol)
	{
		return (value == null) ? TolerantlyComparable.NOT_EQUAL : value.tolerantlyEquals(
				obj.value, tol);
	}

	// ========================= IMPLEMENTATION: Visitable<MTTravVis.> =====

	/**
	 * Sets the traversal index of this object using a visitor class, and then lets a
	 * traversal visitor process this object using the normal <code>visit()</code>
	 * call-back.
	 * 
	 * @param visitor
	 *            item visitor whose <code>visit()</code> method is invoked
	 */
	public void accept(MathTokenTraversalVisitor visitor)
	{
		this.traversalIndex = visitor.getTraversalIndex();
		visitor.visit(this);
	}

	// ========================= METHODS ===================================

	/**
	 * Clear the elements list and then add a the argument to it.
	 * 
	 * @param arg0
	 * @return
	 */
	public void setPrimaryElement(Integer arg0)
	{
		elements.clear();
		elements.add(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#add(java.lang.Integer)
	 */
	public void add(Integer arg0)
	{
		elements.add(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#remove(java.lang.Integer)
	 */
	public boolean remove(Integer arg0)
	{
		return elements.remove(arg0);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Integer get(int index)
	{
		return elements.get(index);
	}

	/**
	 * Print the details of a math token.
	 * 
	 * @return a string containing the math token's details
	 */
	public String toStringDetailed()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		Object[] o = new Object[4];
		int count = 0;
		o[count++] = (value == null) ? CommonNames.MISC.NULL_TO_STRING : this
				.getValueID().toString();
		o[count++] = status.toString();
		o[count++] = (value == null) ? CommonNames.MISC.NULL_TO_STRING : this
				.getValueAsString();
		s.append(String.format("type %-7s  status %-12s  value %-9s", o));
		s.append(" elements ");
		s.append(elements);
		return s.toString();
	}

	/**
	 * Return a deep copy of this object and set a new value.
	 * 
	 * @param newValue
	 *            new value
	 * @return a deep copy of this object with a <code>value = newValue</code> field
	 */
	public MathToken cloneWithNewValue(final MathValue newValue)
	{
		MathToken copy = new MathToken(newValue);
		copy.status = status;
		copy.elements = new ArrayList<Integer>(elements);
		copy.traversalIndex = traversalIndex;
		return copy;
	}

	/**
	 * @param <B>
	 * @param visitor
	 * @see net.ruready.common.visitor.Visitable#accept(net.ruready.common.visitor.Visitor)
	 */
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		value.accept(visitor);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return Returns the status.
	 */
	public MathTokenStatus getStatus()
	{
		return status;
	}

	/**
	 * Set a new status for this math token. <i>Warning: if the token's current status is
	 * <code>FICTITIOUS_CORRECT</code>, it won't be changed by this method.</i>
	 * 
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(MathTokenStatus status)
	{
		if (this.status != MathTokenStatus.FICTITIOUS_CORRECT)
		{
			this.status = status;
		}
		// else if (this.status != status)
		// {
		// logger.debug("Skipped setStatus(), this " + this + " new status " + status);
		//		}
	}

	/**
	 * Returns the value of this <code>MathToken</code>.
	 * 
	 * @return Returns the value.
	 */
	public MathValue getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(MathValue value)
	{
		this.value = value;
	}

	/**
	 * Is this a sign operation or not
	 * 
	 * @return a boolean -- is this a sign operation or not
	 */
	public boolean isSignOp()
	{
		// Delegate to value
		return value.isSignOp();
	}

	/**
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.MathValue#isArithmeticOperation()
	 */
	public boolean isArithmeticOperation()
	{
		return value.isArithmeticOperation();
	}

	/**
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.MathValue#isNumerical()
	 */
	public boolean isNumerical()
	{
		// TODO: use the Dangling Composite pattern (Google it if you don't
		// remember what it is) to get rid of the null checks
		return (value == null) ? false : value.isNumerical();
	}

	/**
	 * @return
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathValueID getValueID()
	{
		// TODO: use the Dangling Composite pattern (Google it if you don't
		// remember what it is) to get rid of the null checks
		return (value == null) ? null : value.getIdentifier();
	}

	public String getValueAsString()
	{
		// TODO: use the Dangling Composite pattern (Google it if you don't
		// remember what it is) to get rid of the null checks
		return (value == null) ? null : value.toString();
	}

	/**
	 * @return the elements
	 */
	public List<Integer> getElements()
	{
		return elements;
	}

	/**
	 * @return the traversalIndex
	 */
	public int getTraversalIndex()
	{
		return traversalIndex;
	}

	/**
	 * @param traversalIndex
	 *            the traversalIndex to set
	 */
	public void setTraversalIndex(int traversalIndex)
	{
		this.traversalIndex = traversalIndex;
	}
}
