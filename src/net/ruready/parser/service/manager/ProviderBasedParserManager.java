/*****************************************************************************************
 * Source File: DefaultParserManager.java
 ****************************************************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.TimedRequestHandler;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.param.entity.ParametricEvaluationTarget;
import net.ruready.parser.port.input.mathml.exports.MathMLInputPort;
import net.ruready.parser.port.output.mathml.exports.MathMLOutputPort;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.exports.ParserRequestUtil;
import net.ruready.parser.service.exports.ParserServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This service invokes the math expression parser for various applications: demo,
 * analyzing student responses during tests, etc.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version May 14, 2007
 */
public class ProviderBasedParserManager implements AbstractParserManager
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ProviderBasedParserManager.class);

	// ========================= FIELDS ====================================

	// Locator of the DAO factory that creates DAOs for Items
	protected final ParserServiceProvider parserServiceProvider;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser manager that uses a provider to call parser services.
	 * 
	 * @param parserServiceProvider
	 *            provides parser service instances
	 */
	public ProviderBasedParserManager(final ParserServiceProvider parserServiceProvider)

	{
		this.parserServiceProvider = parserServiceProvider;
	}

	// ========================= IMPLEMENTATION: AbstractParserManager ===

	// ========================= PARSER INVOKATION METHODS =================

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#runDemo(java.lang.String,
	 *      java.lang.String, net.ruready.parser.options.exports.ParserOptions)
	 */
	public ParserRequest runDemo(String referenceString, String responseString,
			ParserOptions options)
	{
		logger
				.debug("runDemo('" + referenceString + "' , '" + responseString + "'"
						+ ")");

		// Prepare a new request
		ParserRequest request = new DefaultParserRequest(null, null);
		request.setOptions(options);

		// Run the math parser demo flow on the request
		// RequestHandler rh = new MathParserDemoProcessor(referenceString,
		// responseString, null);
		RequestHandler demoHandler = (RequestHandler) parserServiceProvider.createType(
				ParserServiceID.MATH_DEMO_WITH_IMAGES, referenceString, responseString,
				options.getMathExpressionType());
		// Time the demo
		TimedRequestHandler rh0 = new TimedRequestHandler(demoHandler);
		rh0.run(request);

		// Print and clear the message log
		logger.debug(ParserRequestUtil.printMessages(request));
		request.clearMessages();

		// Save the analysis time in analysis object
		RequestHandler rh1 = new SaveAnalysisTime(
				ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.LATEST_RESULT, rh0
						.getElapsedTime());
		rh1.run(request);

		return request;
	}

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#convertMathMLToParser(java.lang.String,
	 *      net.ruready.parser.options.exports.ParserOptions)
	 */
	public String convertMathML2Parser(String mathMLString, ParserOptions options)
	{
		logger.debug("convertMathMLToParser('" + mathMLString + "'" + ")");

		// Prepare a new request
		ParserRequest request = new DefaultParserRequest(null, null);
		request.setOptions(options);

		// Run the math parser demo flow on the request
		RequestHandler rh = new MathMLInputPort(mathMLString);
		rh.run(request);
		logger.debug("Parser string: " + request.getInputString());
		return request.getInputString();
	}

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#convertMathMLToParser(java.lang.String,
	 *      net.ruready.parser.options.exports.ParserOptions)
	 */
	public String convertParser2MathML(String parserString, ParserOptions options)
	{
		logger.debug("convertParser2MathML('" + parserString + "'" + ")");

		// Prepare a new request
		ParserRequest request = new DefaultParserRequest(null, null);
		request.setInputString(parserString);
		request.setOptions(options);

		// Run the converter on the request
		String outputAttribute = ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.MATHML.STRING;
		RequestHandler rh = new MathMLOutputPort(outputAttribute);
		rh.run(request);

		// Read results
		logger.debug("MathML string: "
				+ ((String) request.getAttribute(outputAttribute)).toString());
		return ((String) request.getAttribute(outputAttribute)).toString();
	}

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#evaluate(java.lang.String,
	 *      net.ruready.parser.options.exports.ParserOptions)
	 */
	public ParametricEvaluationTarget evaluate(String parametricString,
			ParserOptions options)
	{
		logger.debug("evaluate('" + parametricString + "'" + ", variables "
				+ options.getVariables() + ")");

		// Prepare a new request
		ParserRequest request = new DefaultParserRequest(null, null);
		request.setOptions(options);

		// Run the evaluation
		// RequestHandler rh = new MathParserDemoProcessor(referenceString,
		// responseString, null);
		RequestHandler handler = (RequestHandler) parserServiceProvider.createType(
				ParserServiceID.PARAMETRIC_EVALUATION, parametricString);
		handler.run(request);

		// Return the evaluation result
		return (ParametricEvaluationTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.PARAMETRIC_EVALUATION);
	}

	// ========================= UTILITY AND TESTING METHODS ===============

}
