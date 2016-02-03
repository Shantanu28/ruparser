/*****************************************************************************************
 * Source File: MathML2MathParserConverter.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import java.util.EmptyStackException;
import java.util.Hashtable;

import net.ruready.common.parser.xml.XmlParser;
import net.ruready.common.parser.xml.helper.DiscardHelper;
import net.ruready.common.parser.xml.helper.Helper;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.exception.MathParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXParseException;

/**
 * A converter of MathML content language into our math parser's text syntax.
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
public class MathML2MathParserConverter extends XmlParser
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathML2MathParserConverter.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options
	// private final ParserOptions options;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a converter from options.
	 * 
	 * @param options
	 *            control options object
	 */
	public MathML2MathParserConverter(final ParserOptions options)
	{
		super();
		// this.options = options;
		this.target = new ConverterTarget(options);
	}

	// ========================= IMPLEMENTATION: XmlParser =================

	/**
	 * @see net.ruready.parser.core.xml.XmlParser#helpers()
	 */
	@Override
	protected Hashtable<String, Helper> helpers()
	{
		Hashtable<String, Helper> helpers = new Hashtable<String, Helper>();

		// ========================================
		// Logical data structures
		// ========================================

		helpers.put("vector", new VectorHelper("vector"));

		// ========================================
		// Logical functions and operators
		// ========================================

		// Unary/binary/multi-nary operations
		helpers.put("equivalent", new FunctionHelper("equivalent", ":="));
		helpers.put("eq", new FunctionHelper("eq", "="));
		helpers.put("neq", new FunctionHelper("neq", "!="));
		helpers.put("lt", new FunctionHelper("lt", "<"));
		helpers.put("leq", new FunctionHelper("leq", "<="));
		helpers.put("gt", new FunctionHelper("gt", ">"));
		helpers.put("geq", new FunctionHelper("geq", ">="));
		helpers.put("implies", new FunctionHelper("implies", ":-"));

		// ========================================
		// Functions applied to arguments
		// ========================================

		helpers.put("apply", new ApplyFuncHelper("apply"));

		// ========================================
		// Discarded tags
		// ========================================

		// Appears around the entire expression
		helpers.put("math", new DiscardHelper("math"));

		// ========================================
		// Operands
		// ========================================

		// Includes one-letter constants like "e", "i", "j"
		helpers.put("ci", new OperandHelper("ci"));
		// Numbers
		helpers.put("cn", new OperandHelper("cn"));

		// ========================================
		// Constants
		// ========================================
		helpers.put("exponentiale", new ConstantHelper("exponentiale", "e"));
		helpers.put("pi", new ConstantHelper("pi", "pi"));
		helpers.put("eulergamma", new ConstantHelper("eulergamma", "gamma"));
		helpers.put("imaginaryi", new ConstantHelper("imaginaryi", "i"));

		// ========================================
		// Tags surrounding operands
		// ========================================

		// Qualifiers whose tag bodies are treated as operands but we
		// still need to save them in the target to have the correct
		// argument order in ApplyFuncHelper
		helpers.put("degree", new SaveHelper("degree"));
		helpers.put("logbase", new SaveHelper("logbase"));

		// ========================================
		// Arithmetic functions and operators
		// ========================================

		// Unary/binary/multi-nary operations
		helpers.put("plus", new FunctionHelper("plus", "+"));
		helpers.put("minus", new FunctionHelper("minus", "-"));
		helpers.put("times", new FunctionHelper("times", "*"));
		helpers.put("divide", new FunctionHelper("divide", "/"));
		helpers.put("power", new FunctionHelper("power", "^"));

		// Unary functions
		helpers.put("abs", new FunctionHelper("abs"));
		helpers.put("arccos", new FunctionHelper("arccos"));
		helpers.put("arccosh", new FunctionHelper("arccosh"));
		helpers.put("arcsin", new FunctionHelper("arcsin"));
		helpers.put("arcsinh", new FunctionHelper("arcsinh"));
		helpers.put("arctan", new FunctionHelper("arctan"));
		helpers.put("arctanh", new FunctionHelper("arctanh"));
		helpers.put("ceiling", new FunctionHelper("ceiling"));
		helpers.put("cos", new FunctionHelper("cos"));
		helpers.put("cosh", new FunctionHelper("cosh"));
		helpers.put("cot", new FunctionHelper("cot"));
		helpers.put("csc", new FunctionHelper("csc"));
		helpers.put("exp", new FunctionHelper("exp"));
		helpers.put("factorial", new FunctionHelper("factorial"));
		helpers.put("floor", new FunctionHelper("floor"));
		helpers.put("ceiling", new FunctionHelper("ceiling"));
		helpers.put("ln", new FunctionHelper("ln"));
		helpers.put("sec", new FunctionHelper("ln"));
		helpers.put("sin", new FunctionHelper("sin"));
		helpers.put("sinh", new FunctionHelper("sinh"));
		helpers.put("tan", new FunctionHelper("tan"));
		helpers.put("tanh", new FunctionHelper("tanh"));

		// Binary functions
		helpers.put("log", new FunctionHelper("log"));
		helpers.put("root", new FunctionHelper("root"));
		helpers.put("rem", new FunctionHelper("rem"));

		return helpers;
	}

	// ========================= METHODS ===================================

	/**
	 * Convert MathML content into our math parser's text syntax.
	 * 
	 * @param mathMLString
	 *            expression string in the MathML content language
	 * @return the equivalent string in the math parser's text syntax
	 */
	public String convert(String mathMLString)
	{
		try
		{
			this.parseString(mathMLString);
			ConverterTarget myTarget = (ConverterTarget) target;
			// Pop the final result from the target's stack. Stack may be empty
			// if the expression is a legal empty MathML expression.
			MathElement element = myTarget.pop();
			return element.getString();
		}
		catch (SAXParseException e)
		{
			// The input from the equation editor could not even be converted
			// to MathML. Return an empty string so that the rest of the parser
			// demo can be run.
			return CommonNames.MISC.EMPTY_STRING;
		}
		catch (EmptyStackException e)
		{
			// Empty expression, return an empty string
			return CommonNames.MISC.EMPTY_STRING;
		}
		catch (Exception e)
		{
			throw new MathParserException(mathMLString, e.getMessage());
		}
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
