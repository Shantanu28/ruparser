/*******************************************************
 * Source File: VariableMap.java
 *******************************************************/
package net.ruready.parser.options.exports;

import java.util.Iterator;
import java.util.Set;

import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.service.exception.MathParserException;

/**
 * An interface for a data structure that holds and manipulates a list of
 * variables.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 15, 2007
 */
public interface VariableMap extends PubliclyCloneable
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Get an iterator over variables in the map. Variables are returned in
	 * alphabetical variable name.
	 * 
	 * @return variable iterator
	 */
	Iterator<Variable> iterator();

	/**
	 * Get a variable by name.
	 * 
	 * @param name
	 *            variable name
	 * @return corresponding variable in the variable map
	 */
	Variable get(String var);

	/**
	 * Get a variable's value by name. Converts the variable string to lower
	 * case before searching for it in the variable map.
	 * 
	 * @param name
	 *            variable name
	 * @return corresponding variable value in the variable map
	 */
	NumericalValue getValue(String var);

	/**
	 * Returns the list of variable names currently used by the parser.
	 * 
	 * @return the list of variable names recognized by the parser
	 */
	Set<String> getNames();

	/**
	 * Get the number of variables.
	 * 
	 * @return size of variable map
	 */
	int size();

	/**
	 * Get the number of symbolic variables.
	 * 
	 * @return size of variable map
	 */
	int numSymbolic();

	/**
	 * Get the number of numerical variables.
	 * 
	 * @return size of variable map
	 */
	int numNumerical();

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
	void addSymbolic(String var) throws MathParserException;

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
	void addNumerical(String var, NumericalValue value) throws MathParserException;

	/**
	 * Set a variable to be symbolic.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	void setSymbolic(String var) throws MathParserException;

	/**
	 * Set a variable to a numerical value.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @param value
	 *            variable's value. value must NOT equal SYMBOLIC_VALUE.
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	void setNumerical(String var, NumericalValue value) throws MathParserException;

	/**
	 * Remove a variable.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or
	 *            "ttt").
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	void remove(String var) throws MathParserException;

	/**
	 * Remove all symbolic/assigned variables.
	 */
	void removeAll();

	/**
	 * @return a flag whether symbolic variables exist in this mesh or not.
	 */
	boolean symbolicVarsExist();
}
