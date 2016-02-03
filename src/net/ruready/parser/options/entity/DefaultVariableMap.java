/*****************************************************************************************
 * Source File: DefaultVariableMap.java
 ****************************************************************************************/
package net.ruready.parser.options.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A parameter list for parametric questions. Print format is "x 1 y 6.5 etc."
 * to represent x=1, y=6.5, etc. In principle, x y etc. are complex-valued, but
 * for now we read/write them as real (use only their real part).
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
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 27, 2007
 */
public class DefaultVariableMap implements VariableMap
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(DefaultVariableMap.class);

	// ========================= FIELDS ====================================

	// Variable map. An entry can be ("x", null)
	// (for a symbolic variable) or ("x", 1.5) (for a
	// variable that assumes a unique value).
	protected Map<String, Variable> variables = new HashMap<String, Variable>();

	// ========================= ITERATORS =================================

	/**
	 * Iterates over variables in the variable map.
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
	 * Please contact these numbers immediately if you receive this file without
	 * permission from the authors. Thank you.<br>
	 * -------------------------------------------------------------------------
	 * 
	 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
	 * @version Jul 27, 2007
	 */
	public class ValueIterator implements Iterator<Variable>
	{
		// This is where we guarantee that the variables are always returned
		// in alphabetical order. Note: the used tree set is synchronized.
		private Iterator<String> iterator = Collections.synchronizedSortedSet(
				new TreeSet<String>(variables.keySet())).iterator();

		public ValueIterator()
		{

		}

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		public Variable next()
		{
			String name = iterator.next();
			return variables.get(name);
		}

		/**
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			iterator.remove();
		}

	}

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an empty request.
	 */
	public DefaultVariableMap()
	{

	}

	/**
	 * Parse a string into a variable map. Format:
	 * <p>
	 * var1:value1 ... varn:valuen (space-delimited)
	 * <p>
	 * if <code>valuesPresent=true</code>, or
	 * <p>
	 * var1 ... varn (space-delimited)
	 * <p>
	 * if <code>valuesPresent=false</code>. The values are assumed to be
	 * complex valued and are parsed using {@link ComplexValue}'s
	 * <code>parseComplex()</code> method.
	 * 
	 * @param s
	 *            string to parse
	 * @param valuesPresent
	 *            if true, will search for values, otherwise parses a
	 *            space-delimited list of variable names without values
	 */
	public DefaultVariableMap(final String s, final boolean valuesPresent)
	{
		StringTokenizer tokens = new StringTokenizer(s,
				CommonNames.MISC.EMPTY_STRING + CommonNames.MISC.SPACE_CHAR);
		if (valuesPresent)
		{
			// ===================================
			// Parse name-value pairs
			// ===================================
			while (tokens.hasMoreTokens())
			{
				String thisItem = tokens.nextToken();
				// If more than 2 parts are found, as in
				// "foo:and:bar", they will be regarded as Option("foo",
				// "and:bar").
				String[] parts = thisItem.split(CommonNames.VARIABLE.SEPARATOR,
						2);
				if (parts.length != 2)
				{
					throw new IllegalArgumentException(
							"failed to parse string '" + s
									+ "' into a variable map");
				}

				// Validate the variable name before adding it
				this
						.addNumerical(parts[0], ComplexValue
								.parseComplex(parts[1]));
			}
		}
		else
		{
			// ===================================
			// Parse names only
			// ===================================
			while (tokens.hasMoreTokens())
			{
				String thisItem = tokens.nextToken();
				// If more than 1 part are found, as in
				// "foo:and:bar", they will be regarded as Option("foo",
				// "and:bar").
				String[] parts = thisItem.split(CommonNames.VARIABLE.SEPARATOR,
						1);
				if (parts.length != 1)
				{
					throw new IllegalArgumentException(
							"failed to parse string '" + s
									+ "' into a variable map");
				}

				// Validate the variable name before adding it
				this.addSymbolic(parts[0]);
			}
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((variables == null) ? 0 : variables.hashCode());
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
		final DefaultVariableMap other = (DefaultVariableMap) obj;
		if (variables == null)
		{
			if (other.variables != null)
				return false;
		}
		else if (!variables.equals(other.variables))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		int count = 0;
		int numVariables = this.size();
		for (Map.Entry<String, Variable> entry : variables.entrySet())
		{
			String name = entry.getKey();
			s.append(name + CommonNames.VARIABLE.SEPARATOR
					+ this.getValue(name));
			if (count < numVariables - 1)
			{
				s.append(" ");
			}
			count++;
		}
		return s.toString();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to
	 * serve as a target (or part of a target) of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public DefaultVariableMap clone()
	{
		try
		{
			DefaultVariableMap copy = (DefaultVariableMap) super.clone();

			// Deep copy tokens. Token is immutable so this is fine.
			copy.setVariables(new HashMap<String, Variable>(variables));

			return copy;
		}
		catch (CloneNotSupportedException e)
		{
			// this shouldn't happen, because we are Cloneable
			throw new InternalError("clone() failed: " + e.getMessage());
		}
	}

	// ========================= IMPLEMENTATION: Marker ===============

	/**
	 * Get an iterator over variables in the map. Variables are returned in
	 * alphabetical variable name.
	 * 
	 * @return variable iterator
	 */
	public Iterator<Variable> iterator()
	{
		return new ValueIterator();
	}

	/**
	 * Get a variable by name.
	 * 
	 * @param name
	 *            variable name
	 * @return corresponding variable in the variable map
	 */
	public Variable get(String var)
	{
		return variables.get(var);
	}

	/**
	 * @see net.ruready.parser.options.exports.VariableMap#getValue(java.lang.String)
	 */
	public NumericalValue getValue(String var)
	{
		return (var == null) ? null : variables.get(var.toLowerCase())
				.getValue();
	}

	/**
	 * Returns the list of variable names currently used by the parser.
	 * 
	 * @return the list of variable names recognized by the parser
	 */
	public Set<String> getNames()
	{
		return variables.keySet();
	}

	/**
	 * Get the number of variables.
	 * 
	 * @return size of variable map
	 */
	public int size()
	{
		return variables.size();
	}

	/**
	 * Get the number of symbolic variables.
	 * 
	 * @return size of variable map
	 */
	public int numSymbolic()
	{
		int count = 0;
		for (Map.Entry<String, Variable> entry : variables.entrySet())
		{
			String name = entry.getKey();
			if (this.getValue(name) == null)
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Get the number of numerical variables.
	 * 
	 * @return size of variable map
	 */
	public int numNumerical()
	{
		int count = 0;
		for (Map.Entry<String, Variable> entry : variables.entrySet())
		{
			String name = entry.getKey();
			if (this.getValue(name) != null)
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Add a symbolic variable. If the variable has an invalid name, an
	 * exception will be thrown.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt"). The corresponding value in the variable map is
	 *            <code>null</code>.
	 * @throws MathParserException
	 *             if variable's string is not a word.
	 */
	public void addSymbolic(String var) throws MathParserException
	{
		this.addNumerical(var, null);
	}

	/**
	 * Add a numeric/assigned variable. If the variable has an invalid name, an
	 * exception will be thrown.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @param value
	 *            variable's value. If null, the variable will be symbolic (not
	 *            assigned any value). value must NOT equal SYMBOLIC_VALUE.
	 * @throws MathParserException
	 *             if variable's string is not a word.
	 */
	public void addNumerical(String var, NumericalValue value)
			throws MathParserException
	{
		// Check input validity
		if (!Variable.isValidVariableName(var))
		{
			throw new MathParserException("Failed to add a numerical variable",
					ParserNames.KEY.MATH_EXCEPTION.INVALID_VARIABLE, var);
		}

		// Note: variable names are always lower case in this object
		variables.put(var, new Variable(var.toLowerCase(), value));
	}

	/**
	 * Set a variable to be symbolic.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	public void setSymbolic(String var) throws MathParserException
	{
		this.setNumerical(var, null);
	}

	/**
	 * Set a variable's value.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @param value
	 *            variable's value. value must NOT equal SYMBOLIC_VALUE.
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	public void setNumerical(String var, NumericalValue value)
			throws MathParserException
	{
		// Check input validity
		if (variables.get(var) == null)
		{
			throw new MathParserException("Failed to set a numerical variable",
					ParserNames.KEY.MATH_EXCEPTION.VARIABLE_NOT_FOUND, var);
		}

		this.variables.put(var, new Variable(var, value));
	}

	/**
	 * Remove a variable.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	public void remove(String var) throws MathParserException
	{
		// Check input validity
		if (variables.get(var) == null)
		{
			throw new MathParserException("Cannot remove the variable " + var
					+ " because it does not exist",
					ParserNames.KEY.MATH_EXCEPTION.INVALID_VARIABLE, var);
		}

		variables.remove(var);
	}

	/**
	 * Remove all symbolic/assigned variables.
	 */
	public void removeAll()
	{
		variables.clear();
	}

	/**
	 * @return a flag whether symbolic variables exist in this mesh or not.
	 */
	public boolean symbolicVarsExist()
	{
		for (Variable variable : variables.values())
		{
			if (variable.getValue() == null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the maximum number of digits over all values in this map. Numbers <=
	 * 1 are considered to have one digit.
	 * 
	 * @return maximum number of digits over all values in this map
	 */
	public int maxNumDigits()
	{
		int maxNumDigits = 0;
		for (Variable variable : variables.values())
		{
			NumericalValue v = variable.getValue();
			double r = v.d_ABS();
			int numDigits;
			if (r <= 1 + 1e-15)
			{
				numDigits = 1;
			}
			else
			{
				numDigits = (int) Math.ceil(Math.log10(r));
			}
			if (maxNumDigits < numDigits)
			{
				maxNumDigits = numDigits;
			}
		}
		return maxNumDigits;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set the list of variables that are allowed in parsed strings. An entry
	 * can be ("x", null) (for a symbolic variable) or ("x", 1.5) (for a
	 * variable that assumes a unique value).
	 * 
	 * @param variables
	 *            A variable map.
	 * @throws MathParserException
	 *             if a variable's string is not a word.
	 */
	private void setVariables(HashMap<String, Variable> variables)
			throws MathParserException
	{
		this.removeAll();

		for (Map.Entry<String, Variable> entry : variables.entrySet())
		{
			// Check input validity and if OK, add to our map
			this.addNumerical(entry.getKey(), entry.getValue().getValue());
		}

		this.variables = variables;
	}
}
