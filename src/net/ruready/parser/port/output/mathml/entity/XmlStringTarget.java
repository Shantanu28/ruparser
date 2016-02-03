/*****************************************************************************************
 * Source File: XmlStringTarget.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.entity;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ConstantValue;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A target object constructed by the MathML-to-math-parser converter. Contains a
 * manipulatable stack of mathematical elements that contains functions, operators and
 * arguments during parsing of a MathML expression.
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
 * @version Sep 8, 2007
 */
public class XmlStringTarget implements PubliclyCloneable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(XmlStringTarget.class);

	// ========================= FIELDS ====================================

	/**
	 * Parser control options.
	 */
	private ParserOptions options;

	/**
	 * XML string built during matching and assembly.
	 */
	private StringBuffer xmlString = null;

	/**
	 * Extraneous tokens removed from the syntax tree.
	 */
	private final List<InternationalizableErrorMessage> syntaxErrors = new ArrayList<InternationalizableErrorMessage>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((options == null) ? 0 : options.hashCode());
		result = PRIME * result
				+ ((syntaxErrors == null) ? 0 : syntaxErrors.toString().hashCode());
		result = PRIME * result + ((xmlString == null) ? 0 : xmlString.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final XmlStringTarget other = (XmlStringTarget) obj;
		if (options == null)
		{
			if (other.options != null)
				return false;
		}
		else if (!options.equals(other.options))
			return false;
		if (syntaxErrors == null)
		{
			if (other.syntaxErrors != null)
				return false;
		}
		else if (!syntaxErrors.equals(other.syntaxErrors))
			return false;
		if (xmlString == null)
		{
			if (other.xmlString != null)
				return false;
		}
		else if (!xmlString.equals(other.xmlString))
			return false;
		return true;
	}

	/**
	 * Create an empty XML string target. The XML string is set to <code>null</code>.
	 * 
	 * @param options
	 *            control options object
	 */
	public XmlStringTarget(final ParserOptions options)
	{
		super();
		this.options = options;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return (xmlString == null) ? CommonNames.MISC.NULL_TO_STRING : xmlString
				.toString();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public XmlStringTarget clone()
	{
		// try {
		// XmlStringTarget copy = (XmlStringTarget) super.clone();
		// copy.options = options;
		XmlStringTarget copy = new XmlStringTarget(this.options);
		if (this.xmlString != null)
		{
			copy.setXmlString(new StringBuffer(this.xmlString));
		}

		// Soft copy options; they are always a link to an external options
		// object
		// logger.debug("this " + this + " copy " + copy);
		// logger.debug("this " + Integer.toHexString(this.hashCode()) + " copy
		// "
		// + Integer.toHexString(copy.hashCode()));
		// for (int i = 0; i < this.syntaxErrors.size(); i++) {
		// logger.debug("i " + i + this.syntaxErrors.get(i));
		// copy.addSyntaxError((HandlerMessage) this.syntaxErrors.get(i).clone());
		// }
		for (InternationalizableErrorMessage error : this.syntaxErrors)
		{
			copy.addSyntaxError(error.clone());
		}
		return copy;
		// }
		// catch (CloneNotSupportedException e) {
		// // this shouldn't happen, because we are Cloneable
		// throw new InternalError("clone() failed: " + e.getMessage());
		// }
	}

	// ========================= METHODS ===================================

	/**
	 * Add a syntax error to the list of errors in this target.
	 * 
	 * @param o
	 *            new syntax error
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addSyntaxError(InternationalizableErrorMessage o)
	{
		return syntaxErrors.add(o);
	}

	/**
	 * Returns true iff there syntax errors were detected during matching.
	 * 
	 * @return <code>true</code> iff the syntax error list is non-empty
	 */
	public boolean hasErrors()
	{
		return !syntaxErrors.isEmpty();
	}

	/**
	 * Return the first syntax error in the error list. Assumes the list is non-empty.
	 * 
	 * @return the first syntax error in the error list. Assumes the list is non-empty.
	 */
	public InternationalizableErrorMessage getFirstSyntaxErrorMessage()
	{
		return syntaxErrors.get(0);
	}

	/**
	 * @param d
	 * @return
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#createValue(double)
	 */
	public NumericalValue createValue(double d) throws NumberFormatException
	{
		return options.createValue(d);
	}

	/**
	 * @param constant
	 * @param v
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#createConstantValue(net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant,
	 *      net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public ConstantValue createConstantValue(Constant constant)
	{
		NumericalValue v = this.createValue(constant.getValue());
		return options.createConstantValue(constant, v);
	}

	/**
	 * @param v
	 * @return
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public NumericalValue createValue(NumericalValue v)
	{
		return this.getArithmeticMode().createValue(v);
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#isImplicitMultiplication()
	 */
	public boolean isImplicitMultiplication()
	{
		return options.isImplicitMultiplication();
	}

	/**
	 * @param implicitMultiplication
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#setImplicitMultiplication(boolean)
	 */
	public void setImplicitMultiplication(boolean implicitMultiplication)
	{
		options.setImplicitMultiplication(implicitMultiplication);
	}

	/**
	 * @param precisionTol
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#setPrecisionTol(double)
	 */
	public void setPrecisionTol(double precisionTol)
	{
		options.setPrecisionTol(precisionTol);
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#getArithmeticMode()
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return options.getArithmeticMode();
	}

	/**
	 * @param arithmeticMode
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#setArithmeticMode(net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
	 */
	public void setArithmeticMode(ArithmeticMode arithmeticMode)
	{
		options.setArithmeticMode(arithmeticMode);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the xmlString
	 */
	public StringBuffer getXmlString()
	{
		return xmlString;
	}

	/**
	 * @param xmlString
	 *            the xmlString to set
	 */
	public void setXmlString(StringBuffer xmlString)
	{
		this.xmlString = xmlString;
	}

	/**
	 * @return the syntaxErrors
	 */
	public List<InternationalizableErrorMessage> getSyntaxErrors()
	{
		return syntaxErrors;
	}

}
