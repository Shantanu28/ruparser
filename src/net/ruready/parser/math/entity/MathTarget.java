/*****************************************************************************************
 * Source File: ParametricEvaluationTarget.java
 ****************************************************************************************/
package net.ruready.parser.math.entity;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.Constant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ConstantValue;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A target constructed by the math expression arithmetic and logical parsers. Represents
 * the expression as a syntax tree.
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
public class MathTarget implements PubliclyCloneable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathTarget.class);

	// ========================= FIELDS ====================================

	// These options are used in numerical evaluation and other processing
	// of this target.
	private final ParserOptions options;

	// A list of the original assembly's tokens, in their order of apperance
	// in the parsed string
	private final List<Token> tokens;

	// Syntax tree; reference may be updated in the life span of the target
	// instance hence is not final like the other fields.
	private SyntaxTreeNode syntax;

	// Extraneous tokens removed from the syntax tree
	private final List<MathToken> extraneous = new ArrayList<MathToken>();

	// Extraneous tokens removed from the syntax tree
	private final List<InternationalizableErrorMessage> syntaxErrors = new ArrayList<InternationalizableErrorMessage>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an initial arithmetic target.
	 * 
	 * @param options
	 *            control options for this node's processing
	 * @param tokens
	 *            list of the original assembly's tokens, in their order of apperance in
	 *            the parsed string
	 * @param syntax
	 *            syntax tree to import into this target (it is not cloned, only the
	 *            reference to <code>syntax</code> is copied)
	 */
	public MathTarget(ParserOptions options, List<Token> tokens, SyntaxTreeNode syntax)
	{
		super();
		this.options = options;
		this.tokens = tokens;
		this.syntax = syntax;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#ring()
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append("Tokens [");
		int count = 0;
		for (Token token : tokens)
		{
			s.append(token);
			if (count < tokens.size() - 1)
			{
				s.append("/");
			}
			count++;
		}
		s.append("]");

		s.append(" Syntax ");
		s.append(syntax.toString());

		return s.toString();
		// return syntax.toString();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target (or part of a target) of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public MathTarget clone()
	{
		// try {
		// Soft-copy the options link -- this is always a link to an external
		// object
		ParserOptions copyOptions = options;
		SyntaxTreeNode copySyntax =
				(syntax == null) ? null : (SyntaxTreeNode) syntax.clone();
		MathTarget copy =
				new MathTarget(copyOptions, new ArrayList<Token>(tokens), copySyntax);

		// Copy extraneous list
		for (MathToken mt : extraneous)
		{
			copy.addExtraneous(mt);
		}

		// Copy syntax error list
		for (InternationalizableErrorMessage error : this.syntaxErrors)
		{
			copy.addSyntaxError(error);
		}

		return copy;
		// }
		//
		// catch (CloneNotSupportedException e) {
		// // this shouldn't happen,
		// // because we are Cloneable
		// throw new InternalError("clone() failed: " + e.getMessage());
		// }

	}

	// ========================= METHODS ===================================

	/**
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#getArithmeticMode()
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return options.getArithmeticMode();
	}

	/**
	 * @param mode
	 * @see net.ruready.parser.options.exports.ParserOptions#setArithmeticMode(net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode)
	 */
	public void setArithmeticMode(ArithmeticMode arithmeticMode)
	{
		options.setArithmeticMode(arithmeticMode);
	}

	/**
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#isImplicitMultiplication()
	 */
	public boolean isImplicitMultiplication()
	{
		return options.isImplicitMultiplication();
	}

	/**
	 * @param implicitMultiplication
	 * @see net.ruready.parser.options.exports.ParserOptions#setImplicitMultiplication(boolean)
	 */
	public void setImplicitMultiplication(boolean implicitMultiplication)
	{
		options.setImplicitMultiplication(implicitMultiplication);
	}

	/**
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#getPrecisionTol()
	 */
	public double getPrecisionTol()
	{
		return options.getPrecisionTol();
	}

	/**
	 * @param precisionTol
	 * @see net.ruready.parser.options.exports.ParserOptions#setPrecisionTol(double)
	 */
	public void setPrecisionTol(double precisionTol)
	{
		options.setPrecisionTol(precisionTol);
	}

	/**
	 * @param d
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#createValue(double)
	 */
	public NumericalValue createValue(double d) throws NumberFormatException
	{
		return options.createValue(d);
	}

	/**
	 * @param v
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#createValue(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public NumericalValue createValue(NumericalValue v)
	{
		return options.createValue(v);
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
	 * Add an implicit token to the list of tokens at a specified index. Useful for
	 * implicit multiplication handling. Use sparingly.
	 * 
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void addImplicitToken(int index, Token element)
	{
		tokens.add(index, element);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addExtraneous(MathToken e)
	{
		return extraneous.add(e);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeExtraneous(Object o)
	{
		return extraneous.remove(o);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int numExtraneous()
	{
		return extraneous.size();
	}

	/**
	 * Return the number of redudant tokens. This is a sub-list of the extaneous token
	 * list.
	 * 
	 * @return the number of redudant tokens
	 */
	public int numRedundant()
	{
		int count = 0;
		for (MathToken m : extraneous)
		{
			if (m.getStatus() == MathTokenStatus.REDUNDANT)
			{
				count++;
			}
		}
		return count++;
	}

	/**
	 * @return
	 * @see net.ruready.parser.math.entity.SyntaxTreeNode#toMathTokenArray()
	 */
	public List<MathToken> toMathTokenArray()
	{
		return syntax.toMathTokenArray();
	}

	/**
	 * @return
	 * @see net.ruready.parser.options.exports.ParserOptions#getMathExpressionType()
	 */
	public MathExpressionType getMathExpressionType()
	{
		return options.getMathExpressionType();
	}

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
		logger.debug("Adding syntax error " + o + " args " + o.getArgs());
		return syntaxErrors.add(o);
	}

	/**
	 * Removes the top node from the target's syntax tree and replaces it by the first
	 * child of the root node. Typcically used when the root node is dummy or redundant
	 * and has exactly one child.
	 */
	public void replaceRootNodeByChild()
	{
		this.syntax = syntax.getChild(0);
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
	 * Print the details of an arithmetic target.
	 * 
	 * @return a string containing the math token's details
	 */
	public String toStringDetailed()
	{
		StringBuffer s =
				new StringBuffer("============ Arithmetic Target @"
						+ Integer.toHexString(hashCode()) + " ============\n");

		// Print syntax tree
		s.append("Syntax tree: ");
		s.append((syntax == null) ? CommonNames.MISC.NULL_TO_STRING
				: (syntax.toString() + '@' + Integer.toHexString(syntax.hashCode())));
		s.append(CommonNames.MISC.NEW_LINE_CHAR);

		// Print tokens
		s.append("Tokens: [");
		int count = 0;
		for (Token token : tokens)
		{
			s.append(token);
			if (count < tokens.size() - 1)
			{
				s.append("/");
			}
			count++;
		}
		s.append("]\n");

		// Print options of root syntax tree
		s.append("Options: ");
		s.append(options);
		s.append(CommonNames.MISC.NEW_LINE_CHAR);

		// Print a list of math tokens
		List<MathToken> mathTokens = this.toMathTokenArray();
		s.append("MathTokens (traversed):\n");
		for (MathToken mt : mathTokens)
		{
			s.append((mt == null) ? CommonNames.MISC.NULL_TO_STRING : mt
					.toStringDetailed());
			s.append(CommonNames.MISC.NEW_LINE_CHAR);
		}
		return s.toString();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the options
	 */
	public ParserOptions getOptions()
	{
		return options;
	}

	/**
	 * @return the syntax
	 */
	public SyntaxTreeNode getSyntax()
	{
		return syntax;
	}

	/**
	 * @param syntax
	 *            the syntax to set
	 */
	public void setSyntax(SyntaxTreeNode syntax)
	{
		this.syntax = syntax;
	}

	/**
	 * @return the tokens
	 */
	public List<Token> getTokens()
	{
		return tokens;
	}

	/**
	 * @return the extraneous
	 */
	public List<MathToken> getExtraneous()
	{
		return extraneous;
	}

	/**
	 * @return the syntaxErrors
	 */
	public List<InternationalizableErrorMessage> getSyntaxErrors()
	{
		return syntaxErrors;
	}

}
