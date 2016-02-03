/*****************************************************************************************
 * Source File: TestRedundancyRemover.java
 ****************************************************************************************/
package test.ruready.parser.absolute;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test the removal of redundancy removal. This is part of a syntax tree's
 * absolute canonicalization.
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
 * @version Aug 15, 2007
 */
public class TestRedundancyRemover extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestRedundancyRemover.class);

	// ========================= IMPLEMENTATION: TestBase ============

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test the arithmetic setup and matching for some hard-coded strings.
	 */
	@Test
	public void testRR()
	{
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		// Prepare a request
		ParserRequest request = new DefaultParserRequest(options, null);

		// Run setup phase. Compiles the arithmetic parser and places it in
		// the request.
		ArithmeticMatchingUtil.runArithmeticSetup(request);

		{
			String inputStr = "1";
			MathToken[] tokens =
			{
			// Original tokens
			new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
					ArithmeticMode.COMPLEX.createValue(1.0))

			// Fictitious tokens
			};
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[0]);
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

		{
			String inputStr = "1+2";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							BinaryOperation.PLUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(2.0)), };
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[1]);
			target.addChild(new SyntaxTreeNode(tokens[0]));
			target.addChild(new SyntaxTreeNode(tokens[2]));
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

	}

	/**
	 * Test the redundancy remover using a data file.
	 */
	@Test
	public void testFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestRedundancyRemoverFromFile(
				TestingNames.FILE.PARSER.ABSOLUTE.REDUNDANCY_REMOVER, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Test the arithmetic setup and matching.
	 * 
	 * @param request
	 * @param options
	 * @param inputString
	 * @param expectedCompleteMatch
	 * @param expectedSyntax
	 */
	private void testArithmeticMatching(ParserRequest request,
			ParserOptions options, String inputString,
			boolean expectedCompleteMatch, SyntaxTreeNode expectedSyntax)
	{
		logger.debug("Matching '" + inputString + "'");

		// Run matching
		boolean completeMatch = ArithmeticMatchingUtil.isArithmeticallyMatched(
				request, inputString);

		// Read results
		Assert.assertEquals(expectedCompleteMatch, completeMatch);

		SyntaxTreeNode syntax = null;
		if (completeMatch)
		{
			MathTarget target = (MathTarget) request
					.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);
			syntax = target.getSyntax();
			logger.debug("Expected Syntax: '" + expectedSyntax + "'");
			logger.debug("Actual   Syntax: '" + syntax + "'");
			logger.debug("Actual   Target: '" + target + "'");
			Assert.assertEquals(true, expectedSyntax.equals(syntax));
		}
	}

	// ========================= TESTING ====================================
}
