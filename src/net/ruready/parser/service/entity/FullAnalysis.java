/*****************************************************************************************
 * Source File: FullAnalysis.java
 ****************************************************************************************/
package net.ruready.parser.service.entity;

import java.util.List;

import net.ruready.common.misc.Immutable;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Parser's full output: an object containing an input (reference) expression and its
 * analysis, including the arithmetic target. This object can be cached if the analysis
 * needs to be run against multiple responses.
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
 * @immutable
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Sep 8, 2007
 */
public class FullAnalysis implements Immutable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(FullAnalysis.class);

	// ========================= FIELDS ====================================

	// Input string
	private final String inputString;

	// Arithmetic target, includes the syntax tree
	private MathTarget arithmeticTarget;

	// Optional analysis object (makes sense only after comparing the input
	// string with another specific string)
	private Analysis analysis;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Create an analysis from data fields.
	 * 
	 * @param inputString
	 *            input expression's string
	 */
	public FullAnalysis(final String inputString)
	{
		super();
		this.inputString = inputString;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Input string " + inputString + " target " + arithmeticTarget
				+ " analysis " + analysis;
	}

	// ========================= METHODS ====================================

	/**
	 * @return the analysis
	 */
	public Analysis getAnalysis()
	{
		return analysis;
	}

	/**
	 * @param analysis
	 *            the analysis to set
	 */
	public void setAnalysis(Analysis analysis)
	{
		this.analysis = analysis;
	}

	/**
	 * @return the arithmeticTarget
	 */
	public MathTarget getArithmeticTarget()
	{
		return arithmeticTarget;
	}

	/**
	 * @param arithmeticTarget
	 *            the arithmeticTarget to set
	 */
	public void setArithmeticTarget(MathTarget arithmeticTarget)
	{
		this.arithmeticTarget = arithmeticTarget;
	}

	/**
	 * @return the inputString
	 */
	public String getInputString()
	{
		return inputString;
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getArithmeticMode()
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return arithmeticTarget.getArithmeticMode();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getExtraneous()
	 */
	public List<MathToken> getExtraneous()
	{
		return arithmeticTarget.getExtraneous();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getFirstSyntaxErrorMessage()
	 */
	public InternationalizableErrorMessage getFirstSyntaxErrorMessage()
	{
		return arithmeticTarget.getFirstSyntaxErrorMessage();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getOptions()
	 */
	public ParserOptions getOptions()
	{
		return arithmeticTarget.getOptions();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getPrecisionTol()
	 */
	public double getPrecisionTol()
	{
		return arithmeticTarget.getPrecisionTol();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getSyntax()
	 */
	public SyntaxTreeNode getSyntax()
	{
		return arithmeticTarget.getSyntax();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getSyntaxErrors()
	 */
	public List<InternationalizableErrorMessage> getSyntaxErrors()
	{
		return arithmeticTarget.getSyntaxErrors();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#getTokens()
	 */
	public List<Token> getTokens()
	{
		return arithmeticTarget.getTokens();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#hasErrors()
	 */
	public boolean hasErrors()
	{
		return arithmeticTarget.hasErrors();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#isImplicitMultiplication()
	 */
	public boolean isImplicitMultiplication()
	{
		return arithmeticTarget.isImplicitMultiplication();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#numExtraneous()
	 */
	public int numExtraneous()
	{
		return arithmeticTarget.numExtraneous();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#numRedundant()
	 */
	public int numRedundant()
	{
		return arithmeticTarget.numRedundant();
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.MathTarget#toMathTokenArray()
	 */
	public List<MathToken> toMathTokenArray()
	{
		return arithmeticTarget.toMathTokenArray();
	}

	// ========================= GETTERS & SETTERS ==========================

}
