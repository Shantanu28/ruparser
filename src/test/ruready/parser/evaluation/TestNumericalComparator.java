/*****************************************************************************************
 * Source File: TestNumericalComparator.java
 ****************************************************************************************/
package test.ruready.parser.evaluation;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test procedures related to numerical comparison of two mathematical
 * expressions.
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
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 16, 2007
 */
public class TestNumericalComparator extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestNumericalComparator.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	// private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test arithmetic numerical evaluation and comparison of pairs of
	 * expressions using a data file.
	 */
	public void xtestArithmeticFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");
		options.setMathExpressionType(MathExpressionType.ARITHMETIC);

		TestFileReader t = new TestArithmeticNumericalComparatorFromFile(
				TestingNames.FILE.PARSER.EVALUATOR.ARITHMETIC.COMPARISON,
				options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	/**
	 * Test logical numerical evaluation and comparison of pairs of expressions
	 * using a data file.
	 */
	@Test
	public void testLogicalFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");
		options.setMathExpressionType(MathExpressionType.LOGICAL);

		TestFileReader t = new TestLogicalNumericalComparatorFromFile(
				TestingNames.FILE.PARSER.EVALUATOR.LOGICAL.COMPARISON, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	// ========================= PRIVATE METHODS ===========================
}
