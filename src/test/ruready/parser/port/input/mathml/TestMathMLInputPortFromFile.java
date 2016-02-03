/*******************************************************
 * Source File: TestMathMLInputPortFromFile.java
 *******************************************************/
package test.ruready.parser.port.input.mathml;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.misc.Auxiliary;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.port.input.mathml.exports.MathMLInputPort;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ruready.common.junit.exports.TestFileReader;

/**
 * Reads and runs redundancy removal tests from a file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */
class TestMathMLInputPortFromFile extends TestFileReader implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestMathMLInputPortFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// Test input: MathML content string
	private String mathMLString;

	// A struct holding the test results
	// TODO: use tolerant equality for double fields, not the default generated
	// equals() methods by eclipse that's currently used here.
	private class TestResult
	{
		public String parserString;

		TestResult()
		{
			super();
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result
					+ ((parserString == null) ? 0 : parserString.hashCode());
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final TestResult other = (TestResult) obj;
			if (parserString == null) {
				if (other.parserString != null)
					return false;
			}
			else if (!parserString.equals(other.parserString))
				return false;
			return true;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "parserString " + parserString;
		}

	}

	// variables holding the actual and expected test results.
	private TestResult actualResult = new TestResult();

	private TestResult expectedResult = new TestResult();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestMathMLInputPortFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		request.setOptions(options);
		// No need to set up -- the math demo flow is self contained
		// ParametricEvaluationUtil.runArithmeticSetup(request);
	}

	// ========================= IMPLEMENTATION: TestFileReader ============

	/**
	 * @see test.ruready.common.test.TestFileReader#setUp()
	 */
	@Override
	protected void setUp()
	{
		// Run the arithmetic setup phase on a request.
		RequestHandler tp = new ArithmeticSetupProcessor(null);
		tp.run(request);
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
		runTest(inputStr);

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
	 * @param inputStr
	 *            input string
	 */
	private void runTest(String inputStr)
	{
		// Run the math parser demo flow on the request
		RequestHandler rh = new MathMLInputPort(this.mathMLString);
		rh.run(request);

		// Read results. We assume all expressions in the file are legal
		actualResult.parserString = request.getInputString();
	}

	/**
	 * Parse input string into test inputs, which are stored in instance
	 * variables.
	 * 
	 * @param inputStr
	 *            a single test's input string from data file
	 */
	private void parseInputString(String inputStr)
	{
		// String format is "mathMLString"
		mathMLString = inputStr.trim();
	}

	/**
	 * Parse output string into expected results, which are stored in instance
	 * variables.
	 * 
	 * @param outputStr
	 *            a single test's output string from data file
	 */
	private void parseOutputString(String outputStr)
	{
		// String format is "mathParserString"
		expectedResult.parserString = outputStr.trim();
	}

	/**
	 * Compare expected and actual test results.
	 * 
	 * @return expected results == actual results
	 */
	private boolean compareExpectedActual()
	{
		// Negative checks
		if (!expectedResult.equals(actualResult)) {
			logger.error("line " + this.getNumLinesRead() + " result:"
					+ CommonNames.MISC.NEW_LINE_CHAR + "expected " + expectedResult
					+ CommonNames.MISC.NEW_LINE_CHAR + "actual   " + actualResult);
			return false;
		}
		return true;
	}

}
