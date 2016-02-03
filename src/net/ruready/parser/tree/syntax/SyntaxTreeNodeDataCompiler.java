/*******************************************************************************
 * Source File: SyntaxTreeNodeDataCompiler.java
 ******************************************************************************/
package net.ruready.parser.tree.syntax;

import net.ruready.common.exception.SystemException;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Alternation;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.common.parser.core.tokens.CaselessLiteral;
import net.ruready.common.parser.core.tokens.Num;
import net.ruready.common.parser.core.tokens.Symbol;
import net.ruready.common.parser.core.tokens.Word;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that recognizes {@link SyntaxTreeNode} object's
 * node The grammar rules are:
 * <p>
 * <code>
 *   data = typeAndValue Names.TREE.SEPARATOR status;
 *   typeAndValue = type1 Names.TREE.SEPARATOR value(type1) | ... | typeN Names.TREE.SEPARATOR value(typeN);
 *   value(type) = (Word, Num, or a list of recognized keywords, depending on the type)
 *   status = status1 | ... | statusN;
 * </code>
 * To make sure that the parenthesis token is not broken into two tokens, a
 * custom tokenizer is used.
 * 
 * @todo TODO Some two tests in MathParserDemoProcessor.dat cause problems due
 *       to Java heap size - out of memory error. # I am suspecting that
 *       SyntaxTreeNodeDataCompiler is the problem -- defining # the data
 *       grammar rule as ('V' ':' Word) | ('V' ':' Num ) | ... might help #
 *       alleviate this difficulty. For now we just disable these test cases.
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 28, 2007
 */
class SyntaxTreeNodeDataCompiler implements AbstractCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SyntaxTreeNodeDataCompiler.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options; currently not in use
	// private ParserOptions options;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a tree string parser from options.
	 * 
	 * @param options
	 *            control options object
	 */
	public SyntaxTreeNodeDataCompiler(
	/* ParserOptions options, */)
	{

	}

	// ========================= METHODS ===================================

	// ========================= IMPLEMENTATION: AbstractCompiler ==========

	/**
	 * Returns a parser that for the grammar rule:
	 * <p>
	 * <code>data = typeAndValue Names.TREE.SEPARATOR status;</code>
	 * 
	 * @return a parser that recognizes a tree node data
	 */
	public Parser parser()
	{
		// data = type Names.TREE.SEPARATOR value Names.TREE.SEPARATOR
		// status;
		Sequence data = new Sequence("data");
		data.add(typeAndValue());
		data.add(new Symbol(CommonNames.TREE.SEPARATOR).discard());
		data.add(status());
		return data;
	}

	// ========================= TREE NODE DATA PARSER COMPILATION =========

	/**
	 * Returns a parser that recognizes math type + value strings. The grammar
	 * rule is:
	 * <p>
	 * <code>
	 * typeAndValue = type1 CommonNames.TREE.SEPARATOR value(type1) | ... | typeN CommonNames.TREE.SEPARATOR value(typeN);
	 * </code>
	 * where the types are hard-coded strings taken from the enumerated type
	 * <code>MathValueID</code>.
	 */
	private Parser typeAndValue()
	{
		// typeAndValue = type1 CommonNames.TREE.SEPARATOR value(type1) | ... | typeN
		// CommonNames.TREE.SEPARATOR value(typeN);
		Alternation typeAndValue = new Alternation("typeAndValue");
		for (MathValueID t : MathValueID.values()) {
			Sequence tv = new Sequence("Specific type+value");
			tv.add(new CaselessLiteral(t.toString()));
			tv.add(new Symbol(CommonNames.TREE.SEPARATOR).discard());
			tv.add(value(t));
			typeAndValue.add(tv);
		}
		return typeAndValue;
	}

	/**
	 * Returns a parser that recognizes math token stati. The grammar rule is:
	 * <p>
	 * <code>
	 * status = status1 | ... | statusN;
	 * </code> where the types are
	 * hard-coded strings taken from the enumerated type
	 * <code>MathTokenStatus</code>.
	 */
	private Parser status()
	{
		// status = status1 | ... | statusN;
		Alternation status = new Alternation("status");
		for (MathTokenStatus t : MathTokenStatus.values()) {
			status.add(new CaselessLiteral(t.toString()));
		}
		return status;
	}

	/**
	 * Returns a parser that recognizes math values corresponding to a certain
	 * math value ID.
	 * 
	 * @param t
	 *            math value ID
	 * @return a parser that recognizes math values corresponding to a certain
	 *         math value ID
	 */
	private Parser value(final MathValueID t)
	{
		switch (t)
		{
			case DISCARDED:
			{
				// Any discarded token: can be a word or a number
				Alternation value = new Alternation("value");
				value.add(new Word());
				value.add(new Num());
				return value;
			}

			case ARITHMETIC_NUMBER:
			{
				// Numerical values
				return new Num();
			}

			case ARITHMETIC_CONSTANT:
			{
				// Add all supported constants
				return mathConstant();
			}

			case ARITHMETIC_VARIABLE:
			{
				// Symbolic variables
				return new Word();
			}

			case ARITHMETIC_PARENTHESIS:
			{
				// Parentheses symbol
				return new CaselessLiteral(new ParenthesisValue().toString());
				// return new Symbol(CommonNames.TREE.PARENTHESIS);
			}

			case ARITHMETIC_UNARY_OPERATION:
			{
				// Unary operations / functions
				return unaryOp();
			}

			case ARITHMETIC_BINARY_OPERATION:
			{
				// Binary operation symbols
				return binaryOp();
			}

			case ARITHMETIC_BINARY_FUNCTION:
			{
				// Binary functions
				return binaryFunc();
			}

			case ARITHMETIC_MULTINARY_OPERATION:
			{
				// Binary operation symbols
				return multinaryOp();
			}

			case LITERAL:
			{
				// Literals are always words
				return new Word();
			}

			case LOGICAL_RELATION_OPERATION:
			{
				// Literals are always words
				return relationOp();
			}

			case LOGICAL_EMPTY:
			{
				// Literals are always words
				return empty();
			}

			case LOGICAL_RESPONSE:
			{
				// Literals are always words
				return response();
			}

			default:
			{
				// We shouldn't be here
				throw new SystemException(
						"Unrecognized math value type " + t);
			}
		}
	}

	/**
	 * Returns a parser that recognizes unary operations and functions.
	 */
	private Parser unaryOp()
	{
		Alternation func = new Alternation("unaryOp");

		// Add unary operations as symbols and unary functions as words
		for (UnaryOperation op : UnaryOperation.values()) {
			if (op.isSymbolOp()) {
				func.add(new Symbol(op.toString()));
			}
			else {
				func.add(new CaselessLiteral(op.toString()));
			}
		}

		return func;
	}

	/**
	 * Returns a parser that recognizes binary operations.
	 */
	private Parser binaryOp()
	{
		Alternation func = new Alternation("binaryOp");

		// Add binary operations as symbols
		for (BinaryOperation op : BinaryOperation.values()) {
			func.add(new Symbol(op.toString()));
		}

		return func;
	}

	/**
	 * Returns a parser that recognizes binary functions.
	 */
	private Parser binaryFunc()
	{
		Alternation func = new Alternation("binaryFunc");

		// Add binary functions as words
		for (BinaryFunction op : BinaryFunction.values()) {
			func.add(new CaselessLiteral(op.toString()));
		}

		return func;
	}

	/**
	 * Returns a parser that recognizes multi-nary operations.
	 */
	private Parser multinaryOp()
	{
		Alternation func = new Alternation("multinaryOp");

		// Add binary operations as symbols
		for (MultinaryOperation op : MultinaryOperation.values()) {
			func.add(new Symbol(op.toString()));
		}

		return func;
	}

	/**
	 * Returns a parser that for the grammar rule: mathConstant = const1 | ... |
	 * constL, where the consts are hard-coded strings (caseless Literals)
	 * representing arithmetic constas like "pi" or "e".
	 */
	private Parser mathConstant()
	{
		Alternation constant = new Alternation("mathConstant");

		for (RealConstant t : RealConstant.values()) {
			CaselessLiteral sy = new CaselessLiteral(t.toString());
			constant.add(sy);
		}

		for (ComplexConstant t : ComplexConstant.values()) {
			CaselessLiteral sy = new CaselessLiteral(t.toString());
			constant.add(sy);
		}
		return constant;
	}

	/**
	 * Returns a parser that recognizes relations.
	 */
	private Parser relationOp()
	{
		Alternation func = new Alternation("relationOp");

		// Add binary operations as symbols
		for (RelationOperation op : RelationOperation.values()) {
			func.add(new Symbol(op.toString()));
		}

		return func;
	}

	/**
	 * Returns a parser that recognizes logical expression root node data.
	 */
	private Parser response()
	{
		return new CaselessLiteral(new ResponseValue().toString());
	}

	/**
	 * Returns a parser that recognizes dummy relation nodes for single
	 * arithmetic expressions.
	 */
	private Parser empty()
	{
		return new CaselessLiteral(new EmptyValue().toString());
	}

	// ========================= UTILITY OBJECT COMPILATION ================

}
