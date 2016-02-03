/*****************************************************************************************
 * Source File: ArithmeticLiteralValue.java
 ****************************************************************************************/
package net.ruready.parser.logical.entity.value;

import net.ruready.common.exception.SystemException;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.LiteralValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents the response root node in logical expresion syntax trees.
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
public class ResponseValue extends LiteralValue implements LogicalValue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ResponseValue.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a root node data for a logical expression. The name of this variable is by
	 * convention <code>ParserNames.RESERVED_WORDS.RESPONSE.</code>
	 */
	public ResponseValue()
	{
		super(ParserNames.RESERVED_WORDS.RESPONSE);
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Convert a String to a <code>ResponseValue</code> of corresponding type to this
	 * math value type (an enumerated factory method).
	 * 
	 * @param s
	 *            string value
	 * @param mode
	 *            arithmetic mode (complex/real/...)
	 * @return <code>ResponseValue</code> object
	 */
	public static ResponseValue parseResponseValue(String s)
	{
		if (ParserNames.RESERVED_WORDS.RESPONSE.equals(s))
		{
			return new ResponseValue();
		}

		throw new SystemException("Cannot convert the string '" + s
				+ "' a response root node value");
	}

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
	@Override
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		MathValueVisitorUtil.accept(this, visitor);
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	@Override
	public MathValueID getIdentifier()
	{
		return MathValueID.LOGICAL_RESPONSE;
	}

	// ========================= IMPLEMENTATION: MathValue ====================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	@Override
	public String simpleName()
	{
		return "Response";
	}

	// ========================= METHODS ===================================

	/**
	 * Equality between two literals based on their names. If at least one of the names is
	 * <code>null</code>, this method returns <code>false</code>. However, if both
	 * values are <code>null</code>, the method returns true if name equality holds as
	 * explained.
	 * 
	 * @param obj
	 *            another variable
	 * @return result of equality of variable names
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
		ResponseValue other = (ResponseValue) obj;

		return (getName() == null) ? false : getName().equals(other.getName());
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

}
