/*******************************************************************************
 * Source File: ParametricEvaluationCompiler.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.manager;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Alternation;
import net.ruready.common.parser.core.manager.Empty;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Repetition;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.common.parser.core.tokens.CaselessLiteral;
import net.ruready.common.parser.core.tokens.Num;
import net.ruready.common.parser.core.tokens.Symbol;
import net.ruready.parser.arithmetic.assembler.ArithmeticAssemblerID;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;
import net.ruready.parser.math.exports.SequenceTrack;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that recognizes arithmetic expressions with
 * symbols. It recognizes expressions according to the following rules: <br>
 * <code>
 *        expr           = term (plusTerm | minusTerm)*;
 *        plusTerm       = &quot;+&quot; term;
 *        minusTerm      = &quot;-&quot; term;
 *        term           = factor (timesFactor | 
 *                         divideFactor | modFactor
 *                         (implicitMultiplication ? (| impTimesFactor) : ())
 *                         )*;
 *        factor         = signOp* part;
 *        signOp         = &quot;-&quot; | &quot;+&quot;;
 *        part           = phrase powFactor | phrase;
 *        timesFactor    = &quot;*&quot; factor;
 *        timesFactor    = &quot;/&quot; factor;
 *        impTimesFactor = Empty part;
 *        powFactor      = &quot;&circ;&quot; factor;
 *        phrase         = parenExpr | funcExpr | 
 *                         func2Expr | argument;
 *        parenExpr      = PARENTHESIS_OPEN expr PARENTHESIS_CLOSE;
 *        funcExpr       = func UNARY_OP_OPEN expr UNARY_OP_CLOSE;
 *        func2Expr      = func2 BINARY_FUNC_OPEN expr BINARY_FUNC_SEPARATOR expr BINARY_FUNC_CLOSE;
 *        func           = &quot;ln&quot; | &quot;sin&quot; | &quot;cos&quot; | ... |
 *                         &quot;+&quot; | &quot;-&quot;; 
 *        func2          = &quot;+&quot; | &quot;-&quot; | &quot;*&quot; | &quot;/&quot; | &quot;&circ;&quot; |
 *                         &quot;log&quot; | &quot;root&quot; | ...; 
 *        argument       = mathConstant | variable | Num;
 *        mathConstant   = &quot;pi&quot; | &quot;e&quot; | ... ;
 *        variable       = s1 | ... | sN; (permitted vars)
 * </code>
 * <p>
 * These rules recognize conventional arithmetic operator precedence and
 * associativity. They also avoid the problem of left recursion, and their
 * implementation avoids problems with the infinite loop inherent in the cyclic
 * dependencies of the rules. In other words, the rules may look simple, but
 * their structure is subtle.
 * <p>
 * To control the parser's arithmetic mode, precision etc. with non-sticky flags
 * for an expression, use the following grammar instead of a plain 'expr':
 * <code>
 *       controlledExpr = control expr;
 *       control        = Empty | &quot;@&quot; precision mode impMult &quot;@&quot;;
 *       precision      = Empty | Num;
 *       mode           = Empty | &quot;d&quot; | &quot;i&quot;
 *                        | &quot;q&quot; | &quot;c&quot; ;
 *       impMult        = Empty | &quot;*&quot; | &quot;!&quot;;
 * </code>
 * <p>
 * In the control sequence syntax, "*" refers to the sign of multiplication,
 * meaning that it MUST appear and implicit multiplication is not allowed.
 * <code>ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED</code> means that
 * multiplication signs are not required, and implicit multiplication is
 * allowed.<br>
 * Control sequences are case insensitive.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education 1901 East South Campus Dr., Room 2197-E (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> Scientific Computing
 *         and Imaging Institute University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 05/12/2005
 */
public class ArithmeticCompiler implements AbstractCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ArithmeticCompiler.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options
	protected final ParserOptions options;

	// Assembler factory; makes the same syntax usable for multiple purposes
	protected final AbstractAssemblerFactory factory;

	// -------------------------------------------------
	// Temporary variables to alleviate cyclic
	// grammar dependencies. Must be lazy-initialized.
	// -------------------------------------------------
	// cycle: expr -> term -> factor -> part -> phrase -> expr
	private Sequence _expr = null;

	// cycle: factor -> part -> factor
	private Sequence _factor = null;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser from options.
	 * 
	 * @param options
	 *            control options object
	 * @param factory
	 *            assembler factory to use
	 */
	public ArithmeticCompiler(final ParserOptions options,
			final AbstractAssemblerFactory factory)
	{
		super();
		this.options = options;
		this.factory = factory;
	}

	// ========================= IMPLEMENTATION: Compiler ==================

	/**
	 * Main parser call (with an optional control sequence). Returns a parser
	 * that will recognize controlled arithmetic expressions. The grammar rule
	 * is controlledExpr = control expr if the control flag is specified in the
	 * options object; or just expr, if control is false.
	 * 
	 * @return a parser that will recognize controlled arithmetic expressions.
	 */
	public Parser parser()
	{
		initialize();

		Parser parser = null;
		if (options.isArithmeticCompilerControl()) {
			parser = controlledExpr();
		}
		else {
			Sequence globalExpr = new SequenceTrack("arithmetic expression");
			globalExpr.add(expr());
			globalExpr.setAssembler(factory
					.createAssembler(ArithmeticAssemblerID.EXPRESSION));
			parser = globalExpr;
		}
		return parser;
	}

	// ========================= METHODS ===================================

	/**
	 * Initialize local variables. Must be called at the beginning of parser
	 * compilation.
	 */
	protected void initialize()
	{
		// Temporary variables to alleviate cyclic
		// grammar dependencies. Must be lazy-initialized.
		_expr = null;
		_factor = null;
	}

	// ========================= PARSER CONTROL COMPILATION ================

	/**
	 * Returns a parser that will recognize controlled arithmetic expressions.
	 * The grammar rule is controlledExpr = control expr;
	 * 
	 * @return a parser that will recognize controlled arithmetic expressions.
	 */
	private Parser controlledExpr()
	{
		// expr = arithmetic expression with an appropriate
		// global assembler attached to it
		Sequence globalExpr = new SequenceTrack(
				"(global) controlled arithmetic expression");
		globalExpr.add(expr());
		globalExpr
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.EXPRESSION));

		// controllExpr = control expr;
		Sequence controlledExpr = new SequenceTrack("controlled arithmetic expression");
		controlledExpr.add(control());
		controlledExpr.add(globalExpr);

		return controlledExpr;
	}

	/**
	 * Returns a parser that will recognize parser control sequences. The
	 * grammar rule is control = Empty | "@" precision mode "@";
	 * 
	 * @return a parser that will recognize control sequences
	 */
	private Parser control()
	{
		// Note: setting precision()'s assembler here means
		// it can only be called once, otherwise other calls
		// will share the same assembler or override the
		// assembler and that's not what we want. Similarly
		// for mode(), impMult().

		// precisionSeq = Empty | Num
		Alternation precisionSeq = new Alternation("precision sequence");
		precisionSeq.add(new Empty());
		precisionSeq.add(new Num().setAssembler(factory
				.createAssembler(ArithmeticAssemblerID.PRECISION)));

		// modeSeq = Empty | mode
		Alternation modeSeq = new Alternation("arithmetic mode sequence");
		modeSeq.add(new Empty());
		modeSeq.add(mode().setAssembler(
				factory.createAssembler(ArithmeticAssemblerID.MODE)));

		// impMultSeq = Empty |
		// ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED | '*'
		Alternation impMultSeq = new Alternation("implicit multiplication mode sequence");
		impMultSeq.add(new Empty());
		impMultSeq.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED)
				.setAssembler(factory
						.createAssembler(ArithmeticAssemblerID.IMP_MULT_MODE)));
		impMultSeq.add(new Symbol('*').setAssembler(factory
				.createAssembler(ArithmeticAssemblerID.IMP_MULT_MODE)));

		// controlSeq = "@" precisionSeq modeSeq impMultSeq "@";
		Sequence controlSeq = new SequenceTrack("control sequence");
		controlSeq.add(new Symbol("@").discard());
		controlSeq.add(precisionSeq);
		controlSeq.add(modeSeq);
		controlSeq.add(impMultSeq);
		controlSeq.add(new Symbol("@").discard());

		// control = Empty | controlSeq;
		Alternation control = new Alternation("control sequence");
		control.add(new Empty());
		control.add(controlSeq);

		return control;
	}

	/**
	 * Returns a parser that will recognize parser arithmetic mode control
	 * sequence. The grammar rule is mode = Empty | &quot;d&quot; |
	 * &quot;i&quot; | &quot;q&quot; | &quot;c&quot; ;
	 * 
	 * @return a parser that will recognize parser arithmetic mode control
	 *         sequence
	 */
	private Parser mode()
	{
		Alternation modeParser = new Alternation("arithmetic mode");
		for (ArithmeticMode t : ArithmeticMode.values()) {
			String modeControlSeq = t.controlSequence();
			CaselessLiteral sy = new CaselessLiteral(modeControlSeq);
			modeParser.add(sy);
		}
		return modeParser;
	}

	// ========================= ARITHMETIC PARSER COMPILATION =============

	/**
	 * Returns a parser that will recognize an arithmetic expressions. The
	 * grammar rule is
	 * <p>
	 * <code>
	 * expr = term (plusTerm | minusTerm)*;
	 * </code>
	 * 
	 * @return a parser that will recognize an arithmetic expression
	 */
	protected Parser expr()
	{
		// This use of a variable avoids the infinite
		// recursion inherent in the grammar; factor depends
		// on powFactor, and powFactor depends on factor.
		if (_expr == null) {
			// logger.debug("expr()");
			// Must init this variable before all calls
			_expr = new SequenceTrack("expr");

			// =====================================================
			// The rule is implemented as
			// 
			// expr = term opTerm*;
			// opTerm = plusTerm | minusTerm;
			// =====================================================

			// opTerm = plusTerm | minusTerm;
			// Note: No assemblers are attached to these terms because
			// we will process these terms in opTerms' assembler
			Alternation opTerm = new Alternation();
			opTerm.add(opParser(BinaryOperation.PLUS, term()));
			opTerm.add(opParser(BinaryOperation.MINUS, term()));

			// expr = term opTerm*;
			_expr.add(term());
			_expr.add(new Repetition(opTerm));
		}
		return _expr;
	}

	/**
	 * Returns a parser that for the grammar rule:
	 * <p>
	 * <code>
	 * term = factor (timesFactor | divideFactor | modFactor
	 * (implicitMultiplication ? (| impTimesFactor) : ()) )*;
	 * </code>
	 * If implicit multiplication is allowed, we add to the alternation
	 * impTimesFactor.
	 * 
	 * @return a parser that recognizes terms
	 */
	private Parser term()
	{
		// =====================================================
		// The rule is implemented as
		//
		// term = factor opFactor*;
		// opFactor = timesFactor | divideFactor | impTimesFactor |
		// modFactor;
		// =====================================================
		Alternation opFactor = new Alternation("multinary operand factor");
		opFactor.add(opParser(BinaryOperation.TIMES, factor()));
		opFactor.add(opParser(BinaryOperation.DIVIDE, factor()));
		opFactor.add(opParser(BinaryOperation.MOD, factor()));
		// Always add implicit multiplication parser and assembler. If the
		// control sequence changes the imp. mult. option, the assembler
		// will throw an exception.
		opFactor.add(impTimesFactor());

		// term = factor multinaryOpFactor*;
		Sequence term = new SequenceTrack("term");
		term.add(factor());
		term.add(new Repetition(opFactor));

		return term;

	}

	/**
	 * Returns a parser for the grammar rule: <br>
	 * <blockquote>
	 * 
	 * <pre>
	 * factor = signOp * part;
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @return a parser that will recognize factors
	 */
	private Parser factor()
	{
		// This use of a variable avoids the infinite
		// recursion inherent in the grammar; factor depends
		// on part, that depends on powFactor, and
		// powFactor depends on factor.
		if (_factor == null) {
			// logger.debug("factor()");
			// Must init this variable before all calls
			_factor = new SequenceTrack("factor");

			// The rule is implemented as
			// 
			// factor = signSeq part;
			// signSeq = signOp*;
			// 
			// signOps uses a pre-assembler to set up a fence
			// retrieved by the factor assembler.

			// signSeq = signOp*;
			Repetition signSeq = new Repetition(signOp().setAssembler(
					factory.createAssembler(ArithmeticAssemblerID.UNARY_OP_SYMBOL)));
			signSeq.setPreAssembler(factory.createAssembler(
					ArithmeticAssemblerID.SET_FENCE,
					ParserNames.ARITHMETIC_COMPILER.SIGNOP_FENCE));

			// factor = signSeq part;
			_factor.add(signSeq);
			_factor.add(part());
			_factor.setAssembler(factory.createAssembler(ArithmeticAssemblerID.SIGN_OP,
					ParserNames.ARITHMETIC_COMPILER.SIGNOP_FENCE));
		}
		return _factor;
	}

	/**
	 * Returns a parser that for the grammar rule: signOp = "-" | "+"; This
	 * parser is used in term().
	 * 
	 * @return a parser that recognizes additive operations
	 */
	private Parser signOp()
	{
		// // logger.debug("signOp()");
		Alternation signOp = new Alternation("signOp");
		List<UnaryOperation> operations = new ArrayList<UnaryOperation>();
		operations.add(UnaryOperation.PLUS);
		operations.add(UnaryOperation.MINUS);
		for (UnaryOperation op : operations) {
			Symbol symbol = new Symbol(op.toString());
			signOp.add(symbol);
		}
		return signOp;
	}

	/**
	 * Returns a parser that for the grammar rule: part = phrase powFactor |
	 * phrase;
	 */
	private Parser part()
	{
		Alternation part = new Alternation("part");
		Sequence s = new SequenceTrack("power term");
		s.add(phrase());
		s.add(opParser(BinaryOperation.POWER, factor()));
		part.add(s);
		part.add(phrase());

		/* ====== TESTING ===== */
		// part.add(new Num().setAssembler(new DataAssembler()));
		/* ====== TESTING ===== */

		return part;
	}

	/**
	 * Returns a parser that for the grammar rule: phrase = parenExpr | funcExpr |
	 * func2Expr | argument;
	 * 
	 * @return a parser that recognizes parenthesized/ functional expressions
	 */
	private Parser phrase()
	{
		Alternation phrase = new Alternation("phrase");
		// phrase = parenExpr | funcExpr | func2Expr | argument;
		phrase.add(parenExpr().setAssembler(
				factory.createAssembler(ArithmeticAssemblerID.PARENTHESIS)));
		phrase.add(funcExpr().setAssembler(
				factory.createAssembler(ArithmeticAssemblerID.UNARY_OP)));
		phrase.add(func2Expr().setAssembler(
				factory.createAssembler(ArithmeticAssemblerID.BINARY_FUNC)));
		phrase.add(argument());
		return phrase;
	}

	/**
	 * Returns a parser that for the grammar rule: parenExpr = PARENTHESIS_OPEN
	 * expr PARENTHESIS_CLOSE;
	 * 
	 * @return a parser that recognizes parenthesized expressions
	 */
	private Parser parenExpr()
	{
		// Do not discard the parenthesis symbols. The pair will be treeated
		// as a unary operation in phrase().
		Sequence parenExpr = new SequenceTrack("parenExpr");
		parenExpr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_OPEN)
				.setAssembler(factory
						.createAssembler(ArithmeticAssemblerID.UNARY_OP_SYMBOL)));
		parenExpr.add(expr());
		parenExpr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.PARENTHESIS_CLOSE)
				.setAssembler(factory
						.createAssembler(ArithmeticAssemblerID.UNARY_OP_SYMBOL)));
		return parenExpr;
	}

	/**
	 * Returns a parser that for the grammar rule: funcExpr = func UNARY_OP_OPEN
	 * expr UNARY_OP_CLOSE;
	 * 
	 * @return a parser that recognizes functional expressions (unary functions)
	 */
	private Parser funcExpr()
	{
		// Discard parenthesis here. They are part of
		// a function's syntax.
		Sequence funcExpr = new SequenceTrack("funcExpr");
		funcExpr.add(func().setAssembler(
				factory.createAssembler(ArithmeticAssemblerID.UNARY_OP_SYMBOL)));
		funcExpr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.UNARY_OP_OPEN)
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.DISCARD)));
		funcExpr.add(expr());
		funcExpr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.UNARY_OP_CLOSE)
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.DISCARD)));
		return funcExpr;
	}

	/**
	 * Returns a parser that for the grammar rule: func = func1 | ... | funcK;
	 * where the funcs are hard-coded strings (caseless Literals) representing
	 * unary arithmetic functions like "sin" or "cos". For each symbol we add an
	 * assembler that replaces the top token in the stack with the result of the
	 * function.
	 */
	private Parser func()
	{
		// See comment in the calling phrase(). We do not
		// implement the Empty option any more here. So:
		// func = func1 | ... | funcK;
		Alternation func = new Alternation("func");
		for (UnaryOperation t : UnaryOperation.values()) {
			CaselessLiteral sy = new CaselessLiteral(t.toString());
			func.add(sy);
		}
		return func;
	}

	/**
	 * Returns a parser that for the grammar rule: func2Expr = func2
	 * BINARY_FUNC_OPEN expr BINARY_FUNC_SEPARATOR expr BINARY_FUNC_CLOSE;
	 * 
	 * @param op
	 *            binary function (=func2)
	 * @return a parser that recognizes functional expressions (binary
	 *         functions)
	 */
	private Parser func2Expr()
	{
		// Discard parenthesis and comma here.
		// They are part of a function's syntax.
		Sequence func2Expr = new SequenceTrack("func2Expr");
		func2Expr.add(func2().setAssembler(
				factory.createAssembler(ArithmeticAssemblerID.BINARY_FUNC_SYMBOL)));
		func2Expr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.BINARY_FUNC_OPEN)
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.DISCARD)));
		func2Expr.add(expr());
		func2Expr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.BINARY_FUNC_SEPARATOR)
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.DISCARD)));
		func2Expr.add(expr());
		func2Expr.add(new Symbol(ParserNames.ARITHMETIC_COMPILER.BINARY_FUNC_CLOSE)
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.DISCARD)));
		return func2Expr;
	}

	/**
	 * Returns a parser that for the grammar rule: func = func1 | ... | funcK;
	 * where the funcs are hard-coded strings (caseless Literals) representing
	 * unary arithmetic functions like "sin" or "cos". For each symbol we add an
	 * assembler that replaces the top token in the stack with the result of the
	 * function.
	 */
	private Parser func2()
	{
		// See comment in the calling phrase(). We do not
		// implement the Empty option any more here. So:
		// func = func1 | ... | funcK;
		Alternation func2 = new Alternation("func2");
		for (BinaryFunction t : BinaryFunction.values()) {
			CaselessLiteral sy = new CaselessLiteral(t.toString());
			func2.add(sy);
		}
		return func2;
	}

	/**
	 * Returns a parser that for the grammar rule: argument = mathConstant |
	 * variable | Num; This parser adds an assembler to Num, that will replace
	 * the top token in the stack with the token's Double value.
	 */
	private Parser argument()
	{
		Alternation argument = new Alternation("argument");
		argument.add(mathConstant());
		argument.add(variable());
		argument.add(new Num().setAssembler(factory
				.createAssembler(ArithmeticAssemblerID.NUM)));
		return argument;
	}

	/**
	 * Returns a parser that for the grammar rule: mathConstant = const1 | ... |
	 * constL, where the consts are hard-coded strings (caseless Literals)
	 * representing arithmetic constas like "pi" or "e". For each symbol we add
	 * an assembler that replaces the top token in the stack with the
	 * mathematical constant.
	 */
	private Parser mathConstant()
	{
		Alternation constant = new Alternation("mathConstant");

		// Real-arithmetic mode constants. If this mode is not
		// supported, the assemblers will throw an exception during parsing.
		for (RealConstant t : RealConstant.values()) {
			CaselessLiteral sy = new CaselessLiteral(t.toString());
			sy.setAssembler(factory.createAssembler(ArithmeticAssemblerID.REAL_CONSTANT,
					t));
			constant.add(sy);
		}

		// Complex-arithmetic mode constants. If this mode is not
		// supported, the assemblers will throw an exception during parsing.
		for (ComplexConstant t : ComplexConstant.values()) {
			CaselessLiteral sy = new CaselessLiteral(t.toString());
			sy.setAssembler(factory.createAssembler(
					ArithmeticAssemblerID.COMPLEX_CONSTANT, t));
			constant.add(sy);
		}
		return constant;
	}

	/**
	 * Returns a parser that for the grammar rule: variable = s1 | ... | sN;
	 * This parser is an alternation of known variable symbols (caseless
	 * Literals). For each variable we add an assembler that replaces the top
	 * token in the stack with the variable's current value (assumed to be
	 * known). These are the variables that can appear on the left hand side of
	 * an equality. Variables may of course <i>not</i> be "e" or "pi" or other
	 * reserved mathematical keywords.
	 */
	private Parser variable()
	{
		// logger.debug("variable()");
		// logger.debug("variables="+variables);
		Alternation variable = new Alternation("variable");
		for (String var : options.getVariableNames()) {
			CaselessLiteral s = new CaselessLiteral(var);
			// s.setAssembler(new VariableAssembler(var));
			s.setAssembler(factory.createAssembler(ArithmeticAssemblerID.VARIABLE));
			variable.add(s);
			// logger.debug("Added variable \Names.MISC.EMPTY_STRING + entry +
			// "\Names.MISC.EMPTY_STRING);
		}
		return variable;
	}

	// ========================= UTILITY OBJECT COMPILATION ================

	/**
	 * Returns a parser that for the grammar rule: opParser = 'op' parser; where
	 * op can be +,-,*,/,^. This parser has an assembler that will pop two
	 * numbers from the stack and push their product.
	 * 
	 * @param op
	 *            binary arithmetic operation
	 */
	private Parser opParser(BinaryOperation op, Parser p)
	{
		// // logger.debug("opParser(), header="+op.header());
		Sequence s = new SequenceTrack(op + " " + p);
		s.add(new Symbol(op.toString()).setAssembler(factory
				.createAssembler(ArithmeticAssemblerID.BINARY_OP_SYMBOL)));
		s.add(p);
		s.setAssembler(factory.createAssembler(ArithmeticAssemblerID.BINARY_OP));
		return s;
	}

	/**
	 * Returns a parser that recognizes the grammar rule:
	 * <p>
	 * <code>
	 * impTimesFactor = Empty part;
	 * </code>
	 * 
	 * @return parser for the implicit multiplication grammar rule
	 */
	private Parser impTimesFactor()
	{
		// impTimesFactor = Empty part;
		Sequence impTimesFactor = new SequenceTrack("implicit multiplication factor");
		impTimesFactor.add(new Empty().setAssembler(factory
				.createAssembler(ArithmeticAssemblerID.IMP_MULT_SYMBOL)));
		impTimesFactor.add(part());
		impTimesFactor.setAssembler(factory
				.createAssembler(ArithmeticAssemblerID.BINARY_OP));
		return impTimesFactor;
	}

	// ========================= GETTERS & SETTERS =========================

}
