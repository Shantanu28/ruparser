/*****************************************************************************************
 * Source File: TestParametricEvaluationFromFile.java
 ****************************************************************************************/
package test.ruready.parser.evaluation;

import java.util.StringTokenizer;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.options.entity.DefaultVariableMap;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.SingleArithmeticEvaluationProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs arithmetic evaluation tests from a file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */
class TestArithmeticEvaluatorFromFile extends TestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestArithmeticEvaluatorFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

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
	public TestArithmeticEvaluatorFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);

		request.setOptions(options);
		// this.tol = tol;

		ArithmeticMatchingUtil.runArithmeticSetup(request);
	}

	// ========================= IMPLEMENTATION: TestFileReader ============

	/**
	 * @see test.ruready.common.test.TestFileReader#Assert.assertEquals(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected boolean assertEquals(String inputStr, String outputStr)
	{
		logger.debug("Reading line " + this.getNumLinesRead() + ": inputStr " + inputStr);

		// ================================
		// Parse input
		// ================================
		// Save old options
		ParserOptions originalOptions = request.getOptions().clone();
		StringTokenizer tokens = new StringTokenizer(inputStr, "|");
		String inputString = null;
		if (tokens.countTokens() == 1)
		{
			// No declared variables
			inputString = inputStr;
		}
		else if (tokens.countTokens() == 2)
		{
			// Both input string and variables are present
			inputString = tokens.nextToken();
			VariableMap variables = new DefaultVariableMap(tokens.nextToken(), true);
			// Create new options in the request
			ParserOptions newOptions = request.getOptions();
			newOptions.setVariables(variables);
		}
		ParserOptions options = request.getOptions();

		// ================================
		// Prepare actual output
		// ================================

		// Run arithmetic matching on request
		request.setInputString(inputString);
		RequestHandler tp = new SingleArithmeticEvaluationProcessor(null);
		tp.run(request);
		NumericalValue actualResult = (NumericalValue) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT);
		// Violates PLK but it's OK here
		ArithmeticMode newMode = options.getArithmeticMode();

		// ================================
		// Prepare expected output
		// ================================
		// NumericalValue expectedResult = ComplexValue.parseComplex(outputStr);
		NumericalValue expectedResult = newMode.createValue(outputStr);
		boolean comparison = (expectedResult.tolerantlyEquals(actualResult, options
				.getPrecisionTol()) == TolerantlyComparable.EQUAL);

		if (!comparison)
		{
			logger.error("Line " + this.getNumLinesRead() + ": inputStr " + inputStr
					+ " expected " + expectedResult + " actual " + actualResult + " tol "
					+ options.getPrecisionTol() + " equal? " + comparison);
		}

		// Restore original options for subsequent tests
		request.setOptions(originalOptions);

		return comparison;
	}
	// ========================= PUBLIC STATIC METHODS =====================

	// ========================= PRIVATE STATIC METHODS ====================

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

}
