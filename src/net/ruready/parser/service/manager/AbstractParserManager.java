/*******************************************************************************
 * Source File: AbstractParserManager.java
 ******************************************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.exception.ApplicationException;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.param.entity.ParametricEvaluationTarget;
import net.ruready.parser.service.exports.ParserRequest;

/**
 * This service allows parser invokation for various applications: parser demo,
 * response analysis during tests, etc.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public interface AbstractParserManager
{
	// ========================= ABSTRACT METHODS ==========================

	// ========================= PARSER INVOKATION METHODS =================

	/**
	 * Run the parser demo flow.
	 * 
	 * @param referenceString
	 *            input reference string
	 * @param responseString
	 *            input response string
	 * @param options
	 *            parser control options
	 * @return parser request containing the parser analysis results
	 * @throws ApplicationException
	 *             if a problem occurred during the parser flow
	 */
	ParserRequest runDemo(final String referenceString,
			final String responseString, final ParserOptions options);

	/**
	 * Convert MathML content into our math parser's text syntax.
	 * 
	 * @param mathMLString
	 *            expression string in the MathML content language
	 * @return the equivalent string in the math parser's text syntax
	 */
	String convertMathML2Parser(final String mathMLString,
			final ParserOptions options);

	/**
	 * Convert MathML content into our math parser's text syntax.
	 * 
	 * @param parserString
	 *            expression string in the math parser's text syntax
	 * @return the equivalent string in the MathML content language
	 */
	String convertParser2MathML(final String parserString,
			final ParserOptions options);

	/**
	 * Evaluate a parametric string.
	 * 
	 * @param parametricString
	 *            string containing parameters to be evaluated
	 * @param options
	 *            parser control options and in particular variable value map
	 * @return a target containing the evaluated string, success code and other
	 *         matching statistics
	 */
	ParametricEvaluationTarget evaluate(final String parametricString,
			final ParserOptions options);

	// ========================= UTILITY AND TESTING METHODS ===============
}
