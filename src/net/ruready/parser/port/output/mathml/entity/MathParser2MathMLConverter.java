/*****************************************************************************************
 * Source File: MathParser2MathMLConverter.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.entity;

import net.ruready.common.parser.core.entity.Match;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.parser.arithmetic.manager.ArithmeticCompiler;
import net.ruready.parser.logical.manager.LogicalCompiler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.port.output.mathml.assembler.MathMLArithmeticAssemblerFactory;
import net.ruready.parser.port.output.mathml.assembler.MathMLLogicalAssemblerFactory;
import net.ruready.parser.service.exception.MathParserException;
import net.ruready.parser.tokenizer.entity.MathAssembly;
import net.ruready.parser.tokenizer.manager.MathTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A converter of our math parser's text syntax into MathML content language.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 20, 2007
 */
public class MathParser2MathMLConverter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathParser2MathMLConverter.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options
	private final ParserOptions options;

	// A parser of math expression text syntax strings
	private final Parser parser;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a converter from options.
	 * 
	 * @param options
	 *            control options object
	 */
	public MathParser2MathMLConverter(final ParserOptions options)
	{
		super();
		this.options = options;

		// Initialize parser; include both logical and arithmetic expressions
		// in the parser's language
		ArithmeticCompiler arithmeticCompiler = new ArithmeticCompiler(options,
				new MathMLArithmeticAssemblerFactory());
		AbstractCompiler logicalCompiler = new LogicalCompiler(arithmeticCompiler
				.parser(), new MathMLLogicalAssemblerFactory());
		this.parser = logicalCompiler.parser();
	}

	// ========================= IMPLEMENTATION: XmlParser =================

	// ========================= METHODS ===================================

	/**
	 * Convert our math parser's text syntax string into MathML content.
	 * 
	 * @param parserString
	 *            parser input string
	 * @param equivalent
	 *            string in MathML content format
	 */
	public String convert(String parserString)
	{
		XmlStringTarget target = this.match(parserString);
		return target.toString();
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Parse (match) an expression from in a string. The resulting target holds the
	 * corresponding MathML content XML string.
	 * 
	 * @param parserString
	 *            the string to parse.
	 * @return XML string target object containing the corresponding MathML content XML
	 *         string
	 * @throws MathParserException
	 *             if this parser does not recognize the given string as a valid
	 *             expression.
	 */
	public XmlStringTarget match(String parserString)
	{
		// Initialize and customize the tokenizer
		Tokenizer t = new MathTokenizer(options.getVariableNames(), options
				.getArithmeticMode());
		t.setString(parserString);
		MathAssembly in = new MathAssembly(t);

		// ------------------------------------------------------------------
		// Note: The assemblers plugged into the matching process will build
		// an XML string target.
		// ------------------------------------------------------------------

		// Initialize the target with a syntax tree containing input parser
		// options. The root node data is fictitious and will be removed by
		// assemblers, but they still use (and possibly update, hence we clone)
		// the options provided here.
		in.setTarget(new XmlStringTarget(options));

		// Parse s
		logger.debug("-------------- PARSING begin --------------");
		logger.debug("parserString = " + parserString);
		logger.debug("in = " + in + " tokens " + in.toTokenArray());
		Match out = parser.completeMatch(in);
		logger.debug("out = " + out);
		XmlStringTarget target = null;

		if (out != null)
		{
			// Found complete match, check for syntax errors detected during
			// assembly
			target = (XmlStringTarget) out.getTarget();
			// completeMatch = true;

			if (out.hasErrors())
			{
				// Exception during matching, throw an exception describing
				// the cause of failure
				InternationalizableErrorMessage error = out.getFirstSyntaxErrorMessage();
				logger.debug("Found syntax errors: " + error.getMessage());
				throw new MathParserException(parserString, error.getMessage(), error
						.getKey(), error.getArgs());
			}
			if (target.hasErrors())
			{
				// Exception during assembly, throw an exception describing
				// the cause of failure
				InternationalizableErrorMessage error = target
						.getFirstSyntaxErrorMessage();
				logger.debug("Found syntax errors: " + error.getMessage());
				throw new MathParserException(parserString, error.getMessage(), error
						.getKey(), error.getArgs());
			}
		}
		else
		{
			throw new MathParserException(parserString, "input string matching failed");
		}

		logger.debug("target = " + target);
		logger.debug("-------------- PARSING end   --------------");
		return target;
	}

	// ========================= GETTERS & SETTERS =========================

	// /**
	// * @return the options
	// */
	// public ParserOptions getOptions()
	// {
	// return options;
	// }

}
