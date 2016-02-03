/*****************************************************************************************
 * Source File: TestAsciiPrinterFromFile.java
 ****************************************************************************************/
package test.ruready.parser.port.output.text;

import java.io.OutputStream;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.misc.Auxiliary;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.port.output.text.exports.AsciiPrinterOutputPort;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.service.manager.ElementMarkerProcessor;
import net.ruready.parser.service.manager.SingleStringProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ruready.common.junit.exports.TestFileReader;
import test.ruready.parser.rl.TestingNames;

/**
 * Reads and runs ASCII printer tests from a file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */
class TestAsciiPrinterFromFile extends TestFileReader implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestAsciiPrinterFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// Test inputs: reference and response expression strings
	private String referenceStr;

	private String responseStr;

	// Test results format. This is a struct.
	private class TestResult
	{
		public String ASCIIString;

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
					+ ((ASCIIString == null) ? 0 : ASCIIString.hashCode());
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
			if (ASCIIString == null)
			{
				if (other.ASCIIString != null)
					return false;
			}
			else if (!ASCIIString.equals(other.ASCIIString))
				return false;
			return true;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "ASCII String " + ASCIIString;
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
	public TestAsciiPrinterFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		request.setOptions(options);
		ArithmeticMatchingUtil.runArithmeticSetup(request);
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
	 * Test the ASCII printer on an input string. The string is matched and absolutely
	 * canonicalized.
	 * 
	 * @param inputStr
	 *            input string
	 */
	private void runTest(String inputStr)
	{
		// Parse reference:
		// Run arithmetic matching, absolute canonicalization and element
		// analysis on the request
		request.setInputString(referenceStr);
		RequestHandler tp0 = new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE, request.getOptions()
						.getMathExpressionType(), null);
		tp0.run(request);

		// Parse response:
		// Run arithmetic matching, absolute canonicalization and element
		// analysis on the request
		request.setInputString(responseStr);
		RequestHandler tp1 = new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.RESPONSE, request.getOptions()
						.getMathExpressionType(), null);
		tp1.run(request);

		// Run analysis phase
		RequestHandler tp3 = new ElementMarkerProcessor(null);
		tp3.run(request);

		// Plug in the ASCII printer of the response arithmetic target
		String outputAttribute = ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.ASCII.STRING;
		RequestHandler tp4 = new AsciiPrinterOutputPort(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE, outputAttribute);
		tp4.run(request);

		// Read results. We assume all expressions in the file are legal,
		// hence target should be non-null at this stage.
		actualResult.ASCIIString = ((OutputStream) request.getAttribute(outputAttribute))
				.toString();
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
		String[] parts = inputStr.trim().split(
				TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR, 2);

		referenceStr = parts[0];
		responseStr = parts[1];
	}

	/**
	 * Parse output string into expected results, which are stored in instance variables.
	 * 
	 * @param outputStr
	 *            a single test's output string from data file
	 */
	private void parseOutputString(String outputStr)
	{
		expectedResult.ASCIIString = outputStr.trim();
	}

	/**
	 * Compare expected and actual test results.
	 * 
	 * @return expected results == actual results
	 */
	private boolean compareExpectedActual()
	{
		// Negative checks
		if (!expectedResult.equals(actualResult))
		{
			logger.error("line " + this.getNumLinesRead() + " result:"
					+ CommonNames.MISC.NEW_LINE_CHAR + "expected " + expectedResult
					+ CommonNames.MISC.NEW_LINE_CHAR + "actual   " + actualResult);
			return false;
		}
		return true;
	}
}
