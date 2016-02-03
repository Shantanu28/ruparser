/*****************************************************************************************
 * Source File: MathElement.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A mathematical element or expression identified in a MathML content language string.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9399<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Oct 5, 2007
 */
class MathElement implements Comparable<MathElement>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathElement.class);

	// ========================= FIELDS ====================================

	// String representation of the element
	private final String string;

	// Value of the root node of the element's syntax tree
	private MathValue value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a mathematical element.
	 * 
	 * @param string
	 *            string representation of the element
	 * @param value
	 *            Value of the root node of the element's syntax tree. May be a function,
	 *            operation or argument
	 */
	public MathElement(final String string, MathValue value)
	{
		super();
		this.string = string;
		this.value = value;
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
		// s.append(value.getIdentifier().toString());
		s.append(value.simpleName());
		s.append(CommonNames.TREE.PARENTHESIS_OPEN);
		s.append(value);
		s.append(CommonNames.TREE.PARENTHESIS_CLOSE);
		s.append(CommonNames.TREE.SEPARATOR);
		s.append(string);
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
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((string == null) ? 0 : string.hashCode());
		result = PRIME * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Compare two math elements based on their values.
	 * 
	 * @param other
	 *            Other <code>MathToken</code> to compare with this one.
	 * @return result of comparison (-1, 0, 1)
	 */
	public int compareTo(MathElement other)
	{
		int compareValue;
		// Compare by numerical value if both values are numerical.
		// Otherwise, compare the value string representations
		if (value.isNumerical() && other.value.isNumerical())
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
			compareValue = (value == null) ? ((other.value == null) ? 0 : -1) : value
					.toString().compareTo(other.value.toString());
			// logger.debug(" value string " + value + "," + other.value +
			// " ==> "
			// + compareValue);
		}
		return compareValue;
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
		MathElement other = (MathElement) obj;

		return (this.compareTo(other) == 0);
	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathValueID getValueID()
	{
		return (value == null) ? null : value.getIdentifier();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the string
	 */
	public String getString()
	{
		return string;
	}

	/**
	 * @return the value
	 */
	public MathValue getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	void setValue(MathValue value)
	{
		this.value = value;
	}

}
