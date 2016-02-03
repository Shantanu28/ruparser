/*****************************************************************************************
 * Source File: ParametricEvaluationCompiler.java
 ****************************************************************************************/
package net.ruready.parser.param.manager;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Alternation;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Repetition;
import net.ruready.common.parser.core.tokens.Num;
import net.ruready.common.parser.core.tokens.QuotedString;
import net.ruready.common.parser.core.tokens.Symbol;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.common.parser.core.tokens.Word;
import net.ruready.parser.param.assembler.ParametricEvaluationAssemblerFactory;
import net.ruready.parser.param.assembler.ParametricEvaluationAssemblerID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that replaces parameter-dependent logical and mathematical
 * expressions with symbols by their numerical value. It uses MathParser to evaluation the
 * expression. The symbols must be assigned numerical values.
 * <p>
 * <code>
 *  paramStr       = element*;
 *  element        = '#' controlledExpr '#' | Word | Num | '-' | '.';
 *  controlledExpr = (see ArithmeticParser rules)
 * </code>
 * Word can contain anything except '#', '&' and '&tilde;', and can start with anything
 * except the usual number state tokens, '#' and '&tilde;'. An exception is that if a
 * sequence "&#" is found (as in "&#8730;"), it is regarded as a symbol to allow for HTML
 * number-encoded symbols to be correctly parsed.
 * <p>
 * controlExpr is identical to the <code>parser()</code> method in
 * <code>ArithmeticParser</code>.
 * <p>
 * These rules recognize that an expression must appear within '#' quotes. Symbol must not
 * be '#'. This is taken care of by the tokenizer.
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
public class ParametricEvaluationCompiler implements AbstractCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(ParametricEvaluationCompiler.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------

	// Assembler factory; makes the same syntax usable for multiple purposes
	private final AbstractAssemblerFactory factory =
			new ParametricEvaluationAssemblerFactory();

	// -------------------------------------------------
	// Temporary variables to alleviate cyclic
	// grammar dependencies. Must be lazy-initialized.
	// -------------------------------------------------

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser from options.
	 * 
	 * @param options
	 *            control options object
	 * @param factory
	 *            assembler factory to use
	 */
	public ParametricEvaluationCompiler()
	{
		super();
	}

	// ========================= IMPLEMENTATION: Compiler ==================

	/**
	 * Main parser call. Returns a parser that will recognize arithmetic expression in '#'
	 * quotes, or Terminal tokens that are not in this format. paramStr = ('#'
	 * controlledExpr '#' | Word | Num)*;
	 * 
	 * @return a parser that will recognize logical and arithmetic expressions in '#'
	 *         quotes and other tokens.
	 */
	public Parser parser()
	{
		// logger.out(Logger.Level.DEBUG, "paramStr()");
		// element = expression | Word | Num
		// | '-' | '.' | "&#" | "&";
		Alternation element = new Alternation("element");
		element.add(new QuotedString().setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.EXPRESSION_EVALUATION)));
		// element.add(new MyQuotedString().setAssembler(new
		// PlotFunctionAssembler()));
		element.add(new Word().setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.APPEND_WORD)));
		element.add(new Num().setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.APPEND_NUM)));
		element.add(new Symbol('-').setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.APPEND_WORD)));
		element.add(new Symbol('.').setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.APPEND_WORD)));
		element.add(new Symbol("&").setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.APPEND_WORD)));
		element.add(new Symbol("&#").setAssembler(factory
				.createAssembler(ParametricEvaluationAssemblerID.APPEND_WORD)));

		// paramStr = element*;
		Repetition paramStr = new Repetition(element);
		return paramStr;
	}

	// ========================= METHODS ===================================

	/**
	 * Returns a tokenizer that allows tokenizes based on a single delimiter, the
	 * character '#'. "~" is recognized as a symbol.
	 * 
	 * @return a tokenizer that breaks a string into tokens based on the delimiter
	 */
	public static Tokenizer tokenizer()
	{
		Tokenizer t = new Tokenizer();
		// Words can contain any character
		// except for '#','~'.
		t.wordState().setWordChars(0, 255, true);
		t.wordState().setWordChars('#', '#', false);
		// t.wordState().setWordChars('~', '~', false);
		t.wordState().setWordChars('&', '&', false);
		// t.wordState().setWordChars('{', '{', false);

		// Words can start with anything...
		t.setCharacterState(0, 255, t.wordState());

		// ...except the usual number chars...
		// (note: '-', '.' can thus also be single-char
		// symbols encountered in tokenizing).
		t.setCharacterState('0', '9', t.numberState());
		t.setCharacterState('-', '-', t.numberState());
		t.setCharacterState('.', '.', t.numberState());

		// ...and the reserved symbols.
		RemoveQuoteState rqs = new RemoveQuoteState();
		t.setCharacterState('#', '#', rqs);

		// RemoveMyQuoteState rmqs = new RemoveMyQuoteState();
		// t.setCharacterState('{', '{', rmqs);

		// t.setCharacterState('~', '~', t.symbolState());
		// t.symbolState().add("~");
		t.setCharacterState('&', '&', t.symbolState());
		t.symbolState().add("&");
		t.symbolState().add("&#");

		return t;
	}

	// ========================= PARSER CONTROL COMPILATION ================

	// ========================= PARAMETRIC PARSER COMPILATION =============

	// ========================= UTILITY OBJECT COMPILATION ================

	// ========================= GETTERS & SETTERS =========================

}
