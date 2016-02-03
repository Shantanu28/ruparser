/*****************************************************************************************
 * Source File: ParserOptions.java
 ****************************************************************************************/
package net.ruready.parser.options.exports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.common.pointer.ValueObject;
import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ConstantValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.rounder.NumericalRounder;
import net.ruready.parser.arithmetic.entity.numericalvalue.rounder.NumericalRounderFactory;
import net.ruready.parser.atpm.entity.CostType;
import net.ruready.parser.atpm.manager.WeightedNodeComparisonCost;
import net.ruready.parser.options.entity.DefaultVariableMap;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A bean centralizing parser controls and options.
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
 * @version Jul 18, 2007
 */
public class ParserOptions implements PubliclyCloneable, ValueObject
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParserOptions.class);

	// ========================= FIELDS ====================================

	// Type of supported mathematical expressions
	private MathExpressionType mathExpressionType = MathExpressionType.LOGICAL;

	// Variable map. An entry can be ("x", null)
	// (for a symbolic variable) or ("x", 1.5) (for a
	// variable that assumes a unique value).
	private VariableMap variables = new DefaultVariableMap();

	// Allow-implicit-multiplication (like "2x" or "2 x")-or-not flag
	private boolean implicitMultiplication = true;

	// Represents # significant digits for computation
	// and comparison of arithmetic results. If
	// rational arithmetic is used, we approximate
	// floats by rationals to this precision.
	private double precisionTol = ParserNames.OPTIONS.DEFAULT_PRECISION_TOL;

	// Arithmetic mode (rational/double/...)
	private ArithmeticMode arithmeticMode = ArithmeticMode.COMPLEX;

	// Allow/Disable arithmetic control sequences in arithmetic compiler
	private boolean arithmeticCompilerControl = true;

	// Fallback for arithmetic matching:
	// if true, will return a null target or the best match if a
	// complete match is not found; no exception is thrown. If this
	// flag false, the parser will aim for a complete match, and if
	// not found, throw an exception.
	private boolean matcherFallBack = false;

	// erroneous-element sub-total score weight in the score formula;
	// must be in [0,1].
	private double errorWeight = ParserNames.OPTIONS.DEFAULT_ERROR_WEIGHT;

	// Type of analysis to be performed. A default value must be specified.
	private AnalysisID analysisID = AnalysisID.ATPM;

	// Edit distance minimization - cost function knobs
	private Map<CostType, Double> costMap = new HashMap<CostType, Double>();

	// Minimum number of RC iterations in the analysis phase
	private int minAnalysisIterations = ParserNames.OPTIONS.DEFAULT_MIN_ANALYSIS_ITERATIONS;

	// Maximum number of RC iterations in the analysis phase
	private int maxAnalysisIterations = ParserNames.OPTIONS.DEFAULT_MAX_ANALYSIS_ITERATIONS;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an empty request.
	 */
	public ParserOptions()
	{
		costMap = new HashMap<CostType, Double>(WeightedNodeComparisonCost
				.getDefaultCostMap());
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
		result = PRIME * result + ((analysisID == null) ? 0 : analysisID.hashCode());
		result = PRIME * result + (arithmeticCompilerControl ? 1231 : 1237);
		result = PRIME * result + (matcherFallBack ? 1231 : 1237);
		result = PRIME * result
				+ ((arithmeticMode == null) ? 0 : arithmeticMode.hashCode());
		result = PRIME * result + ((costMap == null) ? 0 : costMap.hashCode());
		long temp;
		temp = Double.doubleToLongBits(errorWeight);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + (implicitMultiplication ? 1231 : 1237);
		result = PRIME * result + maxAnalysisIterations;
		temp = Double.doubleToLongBits(precisionTol);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((variables == null) ? 0 : variables.hashCode());
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
		final ParserOptions other = (ParserOptions) obj;
		if (analysisID == null)
		{
			if (other.analysisID != null)
				return false;
		}
		else if (!analysisID.equals(other.analysisID))
			return false;
		if (arithmeticCompilerControl != other.arithmeticCompilerControl)
			return false;
		if (matcherFallBack != other.matcherFallBack)
			return false;
		if (arithmeticMode == null)
		{
			if (other.arithmeticMode != null)
				return false;
		}
		else if (!arithmeticMode.equals(other.arithmeticMode))
			return false;
		if (costMap == null)
		{
			if (other.costMap != null)
				return false;
		}
		else if (!costMap.equals(other.costMap))
			return false;
		if (Double.doubleToLongBits(errorWeight) != Double
				.doubleToLongBits(other.errorWeight))
			return false;
		if (implicitMultiplication != other.implicitMultiplication)
			return false;
		if (maxAnalysisIterations != other.maxAnalysisIterations)
			return false;
		if (Double.doubleToLongBits(precisionTol) != Double
				.doubleToLongBits(other.precisionTol))
			return false;
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
	 * Print parser options.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target (or part of a target) of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public ParserOptions clone()
	{
		try
		{
			ParserOptions copy = (ParserOptions) super.clone();

			// Deep copy tokens. Token is immutable so this is fine.
			copy.setVariables((VariableMap) variables.clone());

			return copy;
		}
		catch (CloneNotSupportedException e)
		{
			// this shouldn't happen, because we are Cloneable
			throw new InternalError("clone() failed: " + e.getMessage());
		}
	}

	// ========================= METHODS ===================================

	/**
	 * Get a variable by name.
	 * 
	 * @param name
	 *            variable name
	 * @return corresponding variable in the variable map
	 */
	public Variable getVariable(String var)
	{
		return variables.get(var);
	}

	/**
	 * Returns the list of variable names currently used by the parser.
	 * 
	 * @return the list of variable names recognized by the parser
	 */
	public Set<String> getVariableNames()
	{
		return variables.getNames();
	}

	/**
	 * Get the number of variables.
	 * 
	 * @return size of variable map
	 */
	public int getNumVariables()
	{
		return variables.size();
	}

	/**
	 * Add a symbolic variable. If the variable has an invalid name, an exception will be
	 * thrown.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or "ttt"). The
	 *            corresponding value in the variable map is <code>null</code>.
	 * @throws MathParserException
	 *             if variable's string is not a word.
	 */
	public void addSymbolicVariable(String var) throws MathParserException
	{
		variables.addSymbolic(var);
	}

	/**
	 * Add a numeric/assigned variable. If the variable has an invalid name, an exception
	 * will be thrown.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or "ttt").
	 * @param value
	 *            variable's value. If null, the variable will be symbolic (not assigned
	 *            any value). value must NOT equal SYMBOLIC_VALUE.
	 * @throws MathParserException
	 *             if variable's string is not a word.
	 */
	public void addNumericalVariable(String var, NumericalValue value)
		throws MathParserException
	{
		variables.addNumerical(var, value);
	}

	/**
	 * Set a variable to be symbolic.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or "ttt").
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	public void setSymbolicVariable(String var) throws MathParserException
	{
		variables.setSymbolic(var);
	}

	/**
	 * Set a variable's value.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or "ttt").
	 * @param value
	 *            variable's value. value must NOT equal SYMBOLIC_VALUE.
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	public void setNumericalVariable(String var, NumericalValue value)
		throws MathParserException
	{
		variables.setNumerical(var, value);
	}

	/**
	 * Remove a variable.
	 * 
	 * @param var
	 *            the variable's symbol string. Must be a word (e.g. "x2" or "ttt").
	 * @throws MathParserException
	 *             if variable's string is not found in the variable list.
	 */
	public void removeVariable(String var) throws MathParserException
	{
		variables.remove(var);
	}

	/**
	 * Remove all symbolic/assigned variables.
	 */
	public void removeAllVariables()
	{
		variables.removeAll();
	}

	/**
	 * @param d
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(double)
	 */
	public NumericalValue createValue(double d) throws NumberFormatException
	{
		return arithmeticMode.createValue(d);
	}

	/**
	 * @param v
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public NumericalValue createValue(NumericalValue v)
	{
		return arithmeticMode.createValue(v);
	}

	/**
	 * @param constant
	 * @param v
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#createConstantValue(net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant,
	 *      net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public ConstantValue createConstantValue(Constant constant, NumericalValue v)
	{
		return arithmeticMode.createConstantValue(constant, v);
	}

	/**
	 * @param v
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#round(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue,
	 *      double)
	 */
	@SuppressWarnings("unchecked")
	public NumericalValue round(NumericalValue v)
	{
		NumericalRounder<NumericalValue> rounder = new NumericalRounderFactory()
				.createType(arithmeticMode);
		logger.debug("v " + v + " tol " + precisionTol);
		logger.debug("rounded " + rounder.round(v, precisionTol));
		return rounder.round(v, precisionTol);
	}

	/**
	 * @param v
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue,
	 *      double)
	 */
	@SuppressWarnings("unchecked")
	public String format(NumericalValue v)
	{
		NumericalRounder<NumericalValue> rounder = new NumericalRounderFactory()
				.createType(arithmeticMode);
		// NumericalFormat<NumericalValue> formatter = new
		// NumericalFormatFactory()
		// .createType(arithmeticMode);
		// return formatter.format(rounder.round(v, precisionTol));

		logger.debug("v " + v + " tol " + precisionTol);
		logger.debug("rounded " + rounder.round(v, precisionTol));
		return rounder.round(v, precisionTol).toString();
	}

	// ========================= STATIC METHODS ============================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the implicitMultiplication
	 */
	public boolean isImplicitMultiplication()
	{
		return implicitMultiplication;
	}

	/**
	 * @param implicitMultiplication
	 *            the implicitMultiplication to set
	 */
	public void setImplicitMultiplication(boolean implicitMultiplication)
	{
		this.implicitMultiplication = implicitMultiplication;
	}

	/**
	 * @return the mode
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return arithmeticMode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setArithmeticMode(ArithmeticMode arithmeticMode)
	{
		this.arithmeticMode = arithmeticMode;
	}

	/**
	 * @return the precisionTol
	 */
	public double getPrecisionTol()
	{
		return precisionTol;
	}

	/**
	 * @param precisionTol
	 *            the precisionTol to set
	 */
	public void setPrecisionTol(double precisionTol)
	{
		this.precisionTol = precisionTol;
	}

	/**
	 * @return the variables
	 */
	public VariableMap getVariables()
	{
		return variables;
	}

	/**
	 * @param variables
	 *            the variables to set
	 */
	public void setVariables(VariableMap variables)
	{
		this.variables = variables;
	}

	/**
	 * @return the arithmeticCompilerControl
	 */
	public boolean isArithmeticCompilerControl()
	{
		return arithmeticCompilerControl;
	}

	/**
	 * @param arithmeticCompilerControl
	 *            the arithmeticCompilerControl to set
	 */
	public void setArithmeticCompilerControl(boolean arithmeticCompilerControl)
	{
		this.arithmeticCompilerControl = arithmeticCompilerControl;
	}

	/**
	 * @return the matcherFallBack
	 */
	public boolean isMatcherFallBack()
	{
		return matcherFallBack;
	}

	/**
	 * @param matcherFallBack
	 *            the matcherFallBack to set
	 */
	public void setMatcherFallBack(boolean matcherFallBack)
	{
		this.matcherFallBack = matcherFallBack;
	}

	/**
	 * @return the errorWeight
	 */
	public double getErrorWeight()
	{
		return errorWeight;
	}

	/**
	 * @param errorWeight
	 *            the errorWeight to set
	 */
	public void setErrorWeight(double errorWeight)
	{
		this.errorWeight = errorWeight;
	}

	/**
	 * @return the costMap
	 */
	public Map<CostType, Double> getCostMap()
	{
		return costMap;
	}

	/**
	 * @param costMap
	 *            the costMap to set
	 */
	public void setCostMap(Map<CostType, Double> costMap)
	{
		this.costMap = costMap;
	}

	/**
	 * @return the analysisID
	 */
	public AnalysisID getAnalysisID()
	{
		return analysisID;
	}

	/**
	 * @param analysisID
	 *            the analysisID to set
	 */
	public void setAnalysisID(AnalysisID analysisID)
	{
		this.analysisID = analysisID;
	}

	/**
	 * @return the minAnalysisIterations
	 */
	public int getMinAnalysisIterations()
	{
		return minAnalysisIterations;
	}

	/**
	 * @param minAnalysisIterations
	 *            the minAnalysisIterations to set
	 */
	public void setMinAnalysisIterations(int minAnalysisIterations)
	{
		this.minAnalysisIterations = minAnalysisIterations;
		// Must be at >= 1
		if (this.minAnalysisIterations < 1)
		{
			this.minAnalysisIterations = 1;
		}
	}

	/**
	 * @return the maxAnalysisIterations
	 */
	public int getMaxAnalysisIterations()
	{
		return maxAnalysisIterations;
	}

	/**
	 * @param maxAnalysisIterations
	 *            the maxAnalysisIterations to set
	 */
	public void setMaxAnalysisIterations(int maxAnalysisIterations)
	{
		this.maxAnalysisIterations = maxAnalysisIterations;
		// Must be at >= 1
		if (this.maxAnalysisIterations < 1)
		{
			this.maxAnalysisIterations = 1;
		}
	}

	/**
	 * @return the mathExpressionType
	 */
	public MathExpressionType getMathExpressionType()
	{
		return mathExpressionType;
	}

	/**
	 * @param mathExpressionType
	 *            the mathExpressionType to set
	 */
	public void setMathExpressionType(MathExpressionType mathExpressionType)
	{
		this.mathExpressionType = mathExpressionType;
	}
}
