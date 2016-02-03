/*******************************************************************************
 * Source File: ParametricEvaluationCompiler.java
 ******************************************************************************/
package net.ruready.parser.logical.manager;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Alternation;
import net.ruready.common.parser.core.manager.Empty;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Repetition;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.common.parser.core.tokens.Symbol;
import net.ruready.parser.arithmetic.manager.NoControlArithmeticCompiler;
import net.ruready.parser.logical.assembler.LogicalAssemblerID;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.math.exports.SequenceTrack;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that recognizes the logical structure of a
 * student's response. The response includes multiple statements separated by
 * commas; each statement is one relation between pairs of expressions, or an
 * expression.
 * <p>
 * The grammar rules are:<br>
 * <code>
 * 	   response     = statement (STATEMENT_SEPARATOR statement)*;
 *     statement    = singleExpr | relation;
 *     singleExpr	= arithmeticExpression;
 *     relation     = arithmeticExpression relationOp arithmeticExpression;
 *     relationOp   = "=" | "!=" | "&lt;=" | "&gt;=" | "&lt;" | "&gt;";  | ":=";
 * </code>
 * Here <code>arithmeticExpression</code> is any arithmetic expression that
 * can be parsed by an arithmetic parser like
 * {@link NoControlArithmeticCompiler#parser()}.
 * <p>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br> (c) 2006-07 Continuing
 * Education , University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah
 * @version Jul 2, 2007
 */
public class LogicalCompiler implements AbstractCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(LogicalCompiler.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------

	// Parser of arithmetic expressions
	private final Parser arithmeticExpression;

	// Assembler factory; makes the same syntax usable for multiple purposes
	private final AbstractAssemblerFactory factory;

	// -------------------------------------------------
	// Temporary variables to alleviate cyclic
	// grammar dependencies. Must be lazy-initialized.
	// -------------------------------------------------

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser from options.
	 * 
	 * @param factory
	 *            assembler factory to use
	 * @param arithmeticExpression
	 *            parser of arithmetic expressions
	 */
	public LogicalCompiler(final Parser arithmeticExpression,
			final AbstractAssemblerFactory factory)
	{
		super();
		this.arithmeticExpression = arithmeticExpression;
		this.factory = factory;
	}

	// ========================= IMPLEMENTATION: Compiler ==================

	/**
	 * Main parser call (with an optional control sequence). Returns a parser
	 * that will recognize logical expressions.
	 * 
	 * @return a parser that will recognize logical expressions
	 */
	public Parser parser()
	{
		// Temporary variables to alleviate cyclic
		// grammar dependencies. Must be lazy-initialized.
		// Set global assembler
		Sequence parser = new SequenceTrack("global response");
		parser.add(response());
		parser.setAssembler(factory.createAssembler(LogicalAssemblerID.RESPONSE,
				ParserNames.LOGICAL_COMPILER.STATEMENT_FENCE));
		return parser;
	}

	// ========================= METHODS ===================================

	// ========================= PARSER CONTROL COMPILATION ================

	// ========================= LOGICAL PARSER COMPILATION ================

	/**
	 * Returns a parser that will recognize a response consisting of a sequence
	 * of statements separated by commas. The grammar rule is: <br>
	 * <code>response = statement (STATEMENT_SEPARATOR statement)*; </code>
	 * 
	 * @return a parser that will recognize a response
	 */
	public Parser response()
	{
		// The rule is implemented as
		// 
		// response = empty statement seq;
		// seq = STATEMENT_SEPARATOR statement;
		// 
		// empty uses an assembler to set up a fence
		// retrieved by the response assembler.

		// logger.debug("response()");
		// seq = STATEMENT_SEPARATOR statement;
		Sequence seq = new SequenceTrack();
		seq.add(new Symbol(ParserNames.LOGICAL_COMPILER.STATEMENT_SEPARATOR)
				.setAssembler(factory.createAssembler(LogicalAssemblerID.DISCARD)));
		seq.add(statement());

		// response = empty statement seq*;
		Sequence response = new SequenceTrack("response");
		response.add(new Empty().setAssembler(factory.createAssembler(
				LogicalAssemblerID.SET_FENCE,
				ParserNames.LOGICAL_COMPILER.STATEMENT_FENCE)));
		response.add(statement());
		response.add(new Repetition(seq));

		return response;
	}

	/**
	 * Returns a parser that for the grammar rule: statement = singleExpr |
	 * relation
	 * 
	 * @return a parser that will recognize statements
	 */
	private Parser statement()
	{
		// logger.debug("statement()");
		Alternation statement = new Alternation("statement");
		statement.add(singleExpr());
		statement.add(relation());
		statement.setAssembler(factory.createAssembler(LogicalAssemblerID.STATEMENT));
		return statement;
	}

	/**
	 * Returns a parser that for the grammar rule: singleExpr = expr (note: this
	 * is an expr with a special assembler)
	 * 
	 * @return a parser that will recognize single-expression statements
	 */
	private Parser singleExpr()
	{
		// logger.debug("singleExpr()");
		Sequence singleExpr = new SequenceTrack("singleExpr");
		singleExpr.add(arithmeticExpression.setAssembler(factory
				.createAssembler(LogicalAssemblerID.ARITHMETIC_EXPRESSION)));
		// singleExpr.add(new Num());
		singleExpr.setAssembler(factory
				.createAssembler(LogicalAssemblerID.SINGLE_EXPRESSION));
		return singleExpr;
	}

	/**
	 * Returns a parser that for the grammar rule: relation = expr relationOp
	 * expr;
	 * 
	 * @return a parser that will recognize relations
	 */
	private Parser relation()
	{
		// logger.debug("relation()");
		Sequence relation = new Sequence("relation");
		relation.add(arithmeticExpression.setAssembler(factory
				.createAssembler(LogicalAssemblerID.ARITHMETIC_EXPRESSION)));
		relation.add(relationOp());
		relation.add(arithmeticExpression.setAssembler(factory
				.createAssembler(LogicalAssemblerID.ARITHMETIC_EXPRESSION)));
		relation.setAssembler(factory.createAssembler(LogicalAssemblerID.RELATION));
		return relation;
	}

	/**
	 * Returns a parser that for the grammar rule:
	 * <p>
	 * <code>
	 * relationOp = "=" | "!=" | "&lt;=" | "&gt;=" | "&lt;" | "&gt;" | ":=";
	 * </code>
	 * 
	 * @return a parser that recognizes relational operations
	 */
	private Parser relationOp()
	{
		// logger.debug("relationOp()");
		Alternation relationOp = new Alternation("relationOp");
		for (RelationOperation op : RelationOperation.values()) {
			Symbol symbol = new Symbol(op.toString());
			relationOp.add(symbol);
		}
		relationOp.setAssembler(factory
				.createAssembler(LogicalAssemblerID.RELATION_SYMBOL));
		return relationOp;
	}

	// ========================= UTILITY OBJECT COMPILATION ================

	// ========================= GETTERS & SETTERS =========================

}
