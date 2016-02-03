/*****************************************************************************************
 * Source File: MathTokenizer.java
 ****************************************************************************************/
package net.ruready.parser.tokenizer.manager;

import java.util.Set;

import net.ruready.common.parser.core.tokens.IntegerNumberState;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A tokenizer that's used in all the mathematical expression parser.
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
 * @version Jul 27, 2007
 */
public class MathTokenizer extends Tokenizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathTokenizer.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Returns a tokenizer that does not allow a '-' sign to appear in a number. Words are
	 * not allowed to have '-' in them.
	 * 
	 * @param variables
	 *            set of permitted variable symbols. These are assigned to the tokenizer's
	 *            "symbol state".
	 * @param arithmeticMode
	 *            arithmetic mode. If it is integer or rational, numbers are not allowed
	 *            to contain decimal points. Expressions like "5.7" will be broken into
	 *            "5/./7" where the "." is a symbol. Otherwise, they are allowed.
	 */
	public MathTokenizer(Set<String> variables, ArithmeticMode arithmeticMode)
	{
		super();

		// ========================================
		// Customize the symbol state
		// ========================================

		// Remove the default symbols
		symbolState.clear();

		// Add all unary op symbols to the symbol state.
		for (UnaryOperation op : UnaryOperation.values())
		{
			if (op.isSymbolOp())
			{
				symbolState.add(op.toString());
			}
		}

		// Add all relational operation symbols to the symbol state
		for (RelationOperation op : RelationOperation.values())
		{
			// logger.debug("Adding op " + op + " as a symbol");
			symbolState.add(op.toString());
		}

		// Control sequence reserved symbols
		symbolState.add(CommonNames.MISC.EMPTY_STRING
				+ ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED);
		symbolState.add(CommonNames.MISC.EMPTY_STRING
				+ ParserNames.ARITHMETIC_COMPILER.MULT_REQUIRED);

		setCharacterState(ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED,
				ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED, symbolState);
		setCharacterState(ParserNames.ARITHMETIC_COMPILER.MULT_REQUIRED,
				ParserNames.ARITHMETIC_COMPILER.MULT_REQUIRED, symbolState);

		// Signed numbers are not allowed. This will break
		// "-1" into "-/1" and "+1" into "+/1".
		setCharacterState('-', '-', symbolState);
		setCharacterState('+', '+', symbolState);

		if (arithmeticMode.compareTo(ArithmeticMode.INTEGER) <= 0)
		{
			// Integer mode, don't allow decimal points
			setCharacterState('.', '.', symbolState);
			setCharacterState('0', '9', new IntegerNumberState());
		}

		// Any symbol on the variable's list is recognized as a symbol; if
		// variable names are words, this has no effect -- these variables
		// will be recognized as word tokens, not symbol tokens.
		for (String variable : variables)
		{
			symbolState.add(variable);
		}

		// ========================================
		// Customize word recognition
		// ========================================
		// Words are not allowed to have hyphens, stars or exclamation points
		wordState.setWordChars('-', '-', false);
		wordState.setWordChars(ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED,
				ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED, false);
		wordState.setWordChars(ParserNames.ARITHMETIC_COMPILER.MULT_REQUIRED,
				ParserNames.ARITHMETIC_COMPILER.MULT_REQUIRED, false);
	}

	// ========================= METHODS ===================================
}
