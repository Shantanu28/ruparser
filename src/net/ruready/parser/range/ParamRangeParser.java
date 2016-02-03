package net.ruready.parser.range;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.entity.Match;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.AbstractMatcher;
import net.ruready.common.parser.core.manager.Alternation;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Repetition;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.common.parser.core.tokens.Num;
import net.ruready.common.parser.core.tokens.Symbol;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.parser.core.tokens.TokenAssembly;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.common.parser.core.tokens.Word;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.range.assembler.RangeAssemblerFactory;
import net.ruready.parser.range.assembler.RangeAssemblerID;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that parses strings of parameter ranges and
 * creates a target that holds these parameter ranges. A range consists of a
 * comma delimited list of discrete values, or an interval of integer values.
 * <blockquote>
 * 
 * <pre>
 *         paramRangeStr  = param*;
 *         param          = Word range;
 *         range          = discrete | interval;
 *  	   discrete       = Num (&quot;,&quot; Num)*;
 *  	   interval       = Num &quot;:&quot; Num;
 * </pre>
 * 
 * </blockquote>
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
 * @version Jul 26, 2007
 */
public class ParamRangeParser implements AbstractCompiler, AbstractMatcher
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParamRangeParser.class);

	/**
	 * After encountering a parameter symbol, put this fence in the assembly's
	 * stack.
	 */
	public static final Token SYMBOL_FENCE = new Token("SYMBOL_FENCE");

	// ========================= FIELDS ====================================

	/**
	 * assembler factory to use in compiling the parser.
	 */
	private final AbstractAssemblerFactory factory = new RangeAssemblerFactory();

	// -------------------------------------------------
	// Output input
	// -------------------------------------------------
	// target object
	private RangeMap rangeMap;

	// Matching statistics
	private boolean completeMatch;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a parser of variable ranges .
	 */
	public ParamRangeParser()
	{
		super();
	}

	// ========================= IMPLEMENTATION: AbstractCompiler ==========

	/**
	 * Returns a parser that will recognize a list of parameter ranges.
	 * paramRangeStr = param*;
	 * 
	 * @return a parser that will recognize a list of parameter ranges.
	 */
	public Parser parser()
	{
		// logger.out(Logger.Level.DEBUG, "paramRangeStr()");
		// param = Word range;
		Sequence param = new Sequence("param");
		// Leave parameter's symbol on the stack
		param.add(new Word().setAssembler(factory
				.createAssembler(RangeAssemblerID.SYMBOL)));
		param.add(range());

		// paramRangeStr = param*;
		Repetition paramRangeStr = new Repetition(param);
		return paramRangeStr;
	}

	/**
	 * Returns a parser that for the grammar rule: range = discrete | interval;
	 * 
	 * @return a parser that recognizes a single range
	 */
	protected Parser range()
	{
		// range = discrete | interval;
		Alternation range = new Alternation("range");
		range.add(discrete().setAssembler(
				factory.createAssembler(RangeAssemblerID.DISCRETE)));
		range.add(interval().setAssembler(
				factory.createAssembler(RangeAssemblerID.INTERVAL)));
		return range;
	}

	/**
	 * Returns a parser that for the grammar rule: discrete = Num ("," Num)*;
	 * 
	 * @return a parser that recognizes a discrete range of numbers
	 */
	protected Parser discrete()
	{
		// anotherNum = ("," Num);
		Sequence anotherNum = new Sequence("anotherNum");
		anotherNum.add(new Symbol(DiscreteRange.DELIMITER).discard());
		// Leave number on the assembly
		anotherNum.add(new Num());

		// discrete = Num anotherNum*;
		Sequence discrete = new Sequence("discrete");
		// Leave number on the assembly
		discrete.add(new Num());
		discrete.add(new Repetition(anotherNum));
		return discrete;
	}

	/**
	 * Returns a parser that for the grammar rule: interval = Num ":" Num;
	 * 
	 * @return a parser that recognizes a discrete range of numbers
	 */
	protected Parser interval()
	{
		// interval = Num ":" Num;
		Sequence interval = new Sequence("interval");
		// Leave numbers on the assembly
		interval.add(new Num());
		interval.add(new Symbol(IntervalRange.DELIMITER).discard());
		interval.add(new Num());
		return interval;
	}

	// ################## PARSER CALLS #######################
	/**
	 * Returns the default tokenizer except that it does not recognize ":-" as a
	 * symbol. Thus "a -30:-1" can be matched.
	 * 
	 * @return a default tokenizer except ":-" symbol recognition
	 */
	public Tokenizer tokenizer()
	{
		// TODO: check if this is the correct tokenizer!
		// return new MathTokenizer(options.getVariableNames(),
		// options.getArithmeticMode());
		return new Tokenizer();
	}

	/**
	 * Parse a list of parameter ranges in a string and return a map of
	 * parameter -> parameter range.
	 * 
	 * @param string
	 *            the string to evaluate.
	 * @return map: parameter -> range
	 * @exception MathParserException
	 *                if this parser does not recognize a given sub-expression
	 *                of <code>s</code> as a valid mathematical expression.
	 */
	public void match(final String string) throws MathParserException
	{
		// Null strings can't be parsed, treat them as empty
		String s = (string == null) ? CommonNames.MISC.EMPTY_STRING : string;

		// Initialize and customize the tokenizer
		Tokenizer t = tokenizer();
		t.setString(s);
		Assembly in = new TokenAssembly(t);
		// Assembly in = new TokenAssembly(s);

		RangeMap target = new DelimitedRangeMap();
		in.setTarget(target);
		logger.debug("########### PARSING begin ##############");
		logger.debug("in = " + in);
		/*
		 * int i = 0; while (in.hasMoreElements()) { Token tok =
		 * (Token)in.nextElement(); logger.debug(i+" '" +tok+"' "+tok.ttype());
		 * i++; } in.unget(in.elementsConsumed());
		 */
		// Parse
		Match out = parser().completeMatch(in);
		logger.debug("out = " + out);
		if (out == null)
		{
			// TODO: have InternationalizableErrorMessage reference a key
			// instead of a
			// hard-coded message
			throw new MathParserException(s,
					"Improperly formed arithmetic expression");
		}
		// logger.out(Logger.Level.DEBUG, "out = "+out);

		logger.debug("target = " + out.getTarget());
		rangeMap = (RangeMap) out.getTarget();
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the arithmeticTarget
	 */
	public RangeMap getRangeMap()
	{
		return rangeMap;
	}

	/**
	 * @return the completeMatch
	 */
	public boolean isCompleteMatch()
	{
		return completeMatch;
	}
}
