/*******************************************************************************
 * Source File: TestParametricEvaluationFromFile.java
 ******************************************************************************/
package test.ruready.parser.param;

import java.util.StringTokenizer;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.parser.options.entity.DefaultVariableMap;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.ParametricEvaluationUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.service.manager.ParametricEvaluationProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs arithmetic evaluation tests from a file.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9399<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Nov 21, 2007
 */
public class TestParametricEvaluationFromFile extends TestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestParametricEvaluationFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(
			new ParserOptions(), null);

	// Relative tolerance for numerical comparisons
	// private final double tol;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @param tol
	 *            Relative tolerance for numerical comparisons
	 * @throws ApplicationException
	 */
	public TestParametricEvaluationFromFile(String fileName)
			throws ApplicationException
	{
		super(fileName);

		// Initialize parametric evaluation parser
		ParametricEvaluationUtil.runParametricEvaluationSetup(request);
	}

	// ========================= IMPLEMENTATION: TestFileReader ============

	/**
	 * @see test.ruready.common.test.TestFileReader#Assert.assertEquals(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected boolean assertEquals(String inputStr, String outputStr)
	{
		logger.debug("Reading line " + this.getNumLinesRead() + ": inputStr "
				+ inputStr);

		// ================================
		// Parse input
		// ================================
		StringTokenizer tokens = new StringTokenizer(inputStr, "|");
		String inputString = null;
		ParserOptions options = new ParserOptions();
		options.setPrecisionTol(1e-6);
		if (tokens.countTokens() == 1)
		{
			// No declared variables
			inputString = inputStr;
		}
		else if (tokens.countTokens() == 2)
		{
			// Both input string and variables are present
			inputString = tokens.nextToken();
			VariableMap variables = new DefaultVariableMap(tokens.nextToken(),
					true);
			// Create new options in the request
			options.setVariables(variables);
		}
		request.setOptions(options);
		logger.debug("Variables: " + options.getVariables());
		// Prepare arithmetic parser with these variables
		RequestHandler tp = new ArithmeticSetupProcessor(null);
		tp.run(request);

		// ================================
		// Prepare actual output
		// ================================

		// Run arithmetic matching on request
		request.setInputString(inputString);
		tp = new ParametricEvaluationProcessor(null);
		tp.run(request);
		String actualResult = ((String) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.PARAMETRIC_EVALUATION.EVAL_STRING))
				.trim();

		// ================================
		// Prepare expected output
		// ================================
		// String expectedResult = ComplexValue.parseComplex(outputStr);
		String expectedResult = outputStr.trim();
		boolean comparison = expectedResult.equals(actualResult);

		if (!comparison)
		{
			logger.error("Line " + this.getNumLinesRead() + ": inputStr "
					+ inputStr + " expected " + expectedResult + " actual "
					+ actualResult + " equal? " + comparison);
		}

		// Restore original options for subsequent tests

		return comparison;
	}

	// ========================= PUBLIC STATIC METHODS =====================

	// ========================= PRIVATE STATIC METHODS ====================

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

}
