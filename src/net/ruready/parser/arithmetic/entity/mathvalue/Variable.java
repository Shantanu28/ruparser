/*****************************************************************************************
 * Source File: Variable.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.parser.core.entity.Match;
import net.ruready.common.parser.core.tokens.TokenAssembly;
import net.ruready.common.parser.core.tokens.Word;
import net.ruready.common.text.TextUtil;
import net.ruready.common.util.EnumUtil;
import net.ruready.common.util.HashCodeUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.LiteralValue;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A numerical or a symbolic variable in mathematical expressions analyzed by the parser.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 27, 2007
 */
public class Variable implements ArithmeticValue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Variable.class);

	// ========================= FIELDS ====================================

	// String corresponding to this variable
	private String name;

	// value (if null, this is a symbolic variable)
	private NumericalValue value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a symbolic variable with a name. The name is converted to lower case.
	 * 
	 * @param name
	 *            variable name (must follow conventions)
	 */
	public Variable(String name)
	{
		super();
		this.name = ((name == null) ? null : name.toLowerCase());
	}

	/**
	 * Create a new numerical variable with a name and value. The name is converted to
	 * lower case.
	 * 
	 * @param name
	 *            variable name (must follow conventions)
	 * @param value
	 *            numerical value
	 */
	public Variable(String name, NumericalValue value)
	{
		super();
		this.name = ((name == null) ? null : name.toLowerCase());
		this.value = value;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Prints the variable's name.
	 */
	@Override
	public String toString()
	{
		// return (value == null) ? name : (name + Names.TREE.SEPARATOR +
		// value);
		return name;
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
		result = HashCodeUtil.hash(result, name);
		result = HashCodeUtil.hash(result, value);

		// Optional fields that are likely to be different for different
		// instances

		return result;
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
		return MathValueID.ARITHMETIC_VARIABLE;
	}

	// ========================= IMPLEMENTATION: MathValue ====================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "Var";
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isArithmeticOperation()
	 */
	public boolean isArithmeticOperation()
	{
		return false;
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
		LiteralValue other = (LiteralValue) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= METHODS ===================================

	/**
	 * Equality between two variables. Both names and values must be equal. If at least
	 * one of the names is <code>null</code>, this method returns <code>false</code>.
	 * However, if both values are <code>null</code>, the method returns true if name
	 * equality holds as explained.
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
		Variable other = (Variable) obj;

		return (((name == null) ? false : name.equals(other.getName())) && ((value == null) ? (other
				.getValue() == null)
				: value.equals(other.getValue())));
	}

	// ========================= METHODS ===================================

	/**
	 * Is variable symbolic.
	 * 
	 * @return is variable symbolic (is value = null)
	 */
	public boolean isSymbolic()
	{
		return (value == null);
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Check if this is a valid variable value. Must be a non-reserved word.
	 * 
	 * @param var
	 *            variable's name
	 * @return is it a valid name
	 */
	public static boolean isValidVariableName(String var)
	{
		// Check if var is empty
		if (TextUtil.isEmptyString(var))
		{
			return false;
		}

		// Check if this is a word
		try
		{
			TokenAssembly in = new TokenAssembly(var);
			Match out = new Word().completeMatch(in);
			if (out == null)
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}

		// Conflict with reserved words (case insensitive, note that all
		// reserved words are by convention all lower-case)
		String toLowerVar = var.toLowerCase();
		if ((EnumUtil.valueOf(UnaryOperation.class, toLowerVar) != null)
				|| (EnumUtil.valueOf(BinaryFunction.class, toLowerVar) != null)
				|| (EnumUtil.valueOf(RealConstant.class, toLowerVar) != null)
				|| (EnumUtil.valueOf(ComplexConstant.class, toLowerVar) != null)
				|| ParserNames.RESERVED_WORDS.ALL.contains(toLowerVar)
		// hard-coded Complex constants - no need to, covered by ComplexConstant
		// || Complex.UNIT_IM_SYMBOL.toLowerCase().equals(toLowerVar)
		// TODO: add mathml tag names here
		)
		{
			return false;
		}

		return true;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public NumericalValue getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(NumericalValue value)
	{
		this.value = value;
	}

}
