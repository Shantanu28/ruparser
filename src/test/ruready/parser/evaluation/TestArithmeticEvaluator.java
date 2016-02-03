/*****************************************************************************************
 * Source File: TestArithmeticEvaluator.java
 ****************************************************************************************/
package test.ruready.parser.evaluation;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.real.RealUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.service.manager.SingleArithmeticEvaluationProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test procedures related to the arithmetic parser (tokenizer, arithmetic
 * compiler + matcher) on strings.
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
 * @version Sep 26, 2007
 */
public class TestArithmeticEvaluator extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestArithmeticEvaluator.class);

	// ========================= FIELDS ====================================

	/**
	 * Used for multiple string parsing + evaluation.
	 */
	private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	@Test
	public void testArithmeticEvaluation()
	{
		// Prepare options
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");
		request.setOptions(options);

		// Do the setup only once
		this.setup();

		// Test multiple strings
		this.testArithmeticEvaluation("2+3", new ComplexValue(2.0 + 3.0));
		this.testArithmeticEvaluation("2-3", new ComplexValue(2.0 - 3.0));
		this.testArithmeticEvaluation("2*3", new ComplexValue(2.0 * 3.0));
		this.testArithmeticEvaluation("2/3", new ComplexValue(2.0 / 3.0));
	}

	/**
	 * Test the arithmetic setup and evaluation using a data file.
	 */
	@Test
	public void testArithmeticEvaluationFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestArithmeticEvaluatorFromFile(
				TestingNames.FILE.PARSER.EVALUATOR.ARITHMETIC.EVALUATOR,
				options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Run the arithmetic setup phase on the instance request field.
	 */
	private void setup()
	{
		// Process request
		RequestHandler processor = new ArithmeticSetupProcessor(null);
		processor.run(request);
	}

	/**
	 * Numerically match and evaluate a string.
	 * 
	 * @param inputString
	 * @return evaluation result
	 */
	private NumericalValue evaluateString(String inputString)
	{
		// Set new input
		request.setInputString(inputString);

		// Process request
		RequestHandler processor = new SingleArithmeticEvaluationProcessor(null);
		processor.run(request);

		// Return evaluation result
		return (NumericalValue) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT);
	}

	/**
	 * Test the arithmetic evaluation against a goldren standard.
	 * 
	 * @param inputString
	 * @param expectedResult
	 */
	private void testArithmeticEvaluation(String inputString,
			NumericalValue expectedResult)
	{
		logger.debug("Evaluating '" + inputString + "'");
		NumericalValue actualResult = evaluateString(inputString);
		logger.debug("Expected result: '" + expectedResult + "'");
		logger.debug("Actual   result: '" + actualResult + "'");

		Assert.assertEquals(TolerantlyComparable.EQUAL, expectedResult
				.tolerantlyEquals(actualResult, RealUtil.MACHINE_DOUBLE_ERROR));
	}
}
