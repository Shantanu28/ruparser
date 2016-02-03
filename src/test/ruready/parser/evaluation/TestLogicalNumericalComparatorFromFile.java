/*****************************************************************************************
 * Source File: TestNumericalComparatorFromFile.java
 ****************************************************************************************/
package test.ruready.parser.evaluation;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.misc.Auxiliary;
import net.ruready.parser.evaluator.exports.NumericalComparisonHandler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.LogicalMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.LogicalMatchingProcessor;
import net.ruready.parser.service.manager.SaveSyntaxTreeAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.ruready.parser.rl.TestingNames;

/**
 * Reads and runs math logical expression numerical comparison tests from a file.
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
 * @version Jul 16, 2007
 */
class TestLogicalNumericalComparatorFromFile extends TestFileReader implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestLogicalNumericalComparatorFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// Convenient local variables

	// Expression #0 to compare with Expression #1
	private String expression0;

	// Expression #1 to compare with Expression #0
	private String expression1;

	// Represents # significant digits for comparison of logical results.
	private double precisionTol = ParserNames.OPTIONS.DEFAULT_PRECISION_TOL;

	// Saves the default parser options for tests
	private ParserOptions defaultOptions;

	// variables holding actual test results
	private boolean actualResult;

	// variables holding expected test results
	private boolean expectedResult;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestLogicalNumericalComparatorFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		request.setOptions(options);
		defaultOptions = request.getOptions();
		precisionTol = options.getPrecisionTol();
		LogicalMatchingUtil.runLogicalSetup(request);
	}

	// ========================= IMPLEMENTATION: TestFileReader ============

	/**
	 * @see test.ruready.common.test.TestFileReader#setUp()
	 */
	@Override
	protected void setUp()
	{
		// Done in constructor instead
		// Run the logical setup phase on a request.
		// RequestHandler tp = new logicalSetupProcessor(null);
		// tp.run(request);
	}

	/**
	 * @see test.ruready.common.test.TestFileReader#Assert.assertEquals(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected boolean assertEquals(String inputStr, String outputStr)
	{
		logger.debug("############## Processing: inputStr " + inputStr + " output "
				+ outputStr + " ##############");

		// Parse input string
		parseInputString(inputStr);

		// Parse output string
		parseOutputString(outputStr);

		// Run test and get actual results
		runTest();

		// Compare results
		return compareExpectedActual();
	}

	// ========================= PUBLIC STATIC METHODS =====================

	// ========================= PRIVATE STATIC METHODS ====================

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Test the redundancy remover on an input string. The string is matched and
	 * absolutely canonicalized.
	 * 
	 * @param expression0
	 *            Expression #0 to compare with Expression #1
	 * @param expression1
	 *            Expression #1 to compare with Expression #0
	 */
	private void runTest()
	{
		// Run logical matching on expression #0
		request.setInputString(expression0);
		RequestHandler tp = new LogicalMatchingProcessor(null);
		tp.run(request);
		tp = new SaveSyntaxTreeAdapter(
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE);
		tp.run(request);

		// Run logical matching on expression #1
		request.setInputString(expression1);
		tp = new LogicalMatchingProcessor(null);
		tp.run(request);
		tp = new SaveSyntaxTreeAdapter(ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.RESPONSE);
		tp.run(request);

		// Set options
		ParserOptions specificOptions = defaultOptions.clone();
		specificOptions.setPrecisionTol(precisionTol);
		request.setOptions(specificOptions);
		// logger.debug("defaultOptions " + defaultOptions);
		// logger.debug("specificOptions " + specificOptions);

		// Numerically compare the resulting syntax trees
		tp = new NumericalComparisonHandler();
		tp.run(request);

		// Read results. We assume all expressions in the file are legal.
		actualResult = ((Boolean) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT))
				.booleanValue();

		request.setOptions(defaultOptions);
	}

	/**
	 * Parse input string into test inputs, which are stored in instance variables.
	 * 
	 * @param inputStr
	 *            a single test's input string from data file
	 */
	private void parseInputString(String inputStr)
	{
		// String format is "expression1 ~ expression2 [ ~ precisionTol ] "
		// The "~" symbol must not appear in the parser's grammar
		String[] parts = inputStr.split(TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR,
				3);

		expression0 = parts[0];
		expression1 = parts[1];

		if (parts.length > 2)
		{
			precisionTol = Double.parseDouble(parts[2]);
		}
		else
		{
			precisionTol = defaultOptions.getPrecisionTol();
		}
	}

	/**
	 * Parse output string into expected results, which are stored in instance variables.
	 * 
	 * @param outputStr
	 *            a single test's output string from data file
	 */
	private void parseOutputString(String outputStr)
	{
		// Parse # redundant
		expectedResult = Boolean.parseBoolean(outputStr);
	}

	/**
	 * Compare expected and actual test results.
	 * 
	 * @return expected results == actual results
	 */
	private boolean compareExpectedActual()
	{
		// Negative checks
		if (expectedResult != actualResult)
		{
			logger.error("line " + this.getNumLinesRead() + " expected " + expectedResult
					+ " actual " + actualResult);
			return false;
		}
		return true;
	}

}
