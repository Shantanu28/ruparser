/*****************************************************************************************
 * Source File: TestLogicalMatcher.java
 ****************************************************************************************/
package test.ruready.parser.arithmetic;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
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
 * Test procedures related to the arithmetic numerical evaluation of expression.
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
public class TestArithmeticMatcher extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestArithmeticMatcher.class);

	// ========================= IMPLEMENTATION: TestBase ============

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test the arithmetic setup phase.
	 */
	@Test
	public void testArithmeticSetup()
	{
		// Prepare a request
		ParserOptions options = new ParserOptions();
		ParserRequest request = new DefaultParserRequest(options, null);

		ArithmeticMatchingUtil.runArithmeticSetup(request);

		// Check that the parser object exists. Can't do much more checking
		// beyond that at this point.
		Parser arithmeticParser = (Parser) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.ARITHMETIC.PARSER);

		Assert.assertEquals(true, arithmeticParser != null);
	}

	/**
	 * Test the arithmetic setup and matching for some hard-coded strings.
	 */
	@Test
	public void testArithmeticMatching()
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

		{
			String inputStr = "+1+2";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							UnaryOperation.PLUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							BinaryOperation.PLUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(2.0)), };
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[2]);
			SyntaxTreeNode branch1 = new SyntaxTreeNode(tokens[0]);
			branch1.addChild(new SyntaxTreeNode(tokens[1]));
			target.addChild(branch1);
			SyntaxTreeNode branch2 = new SyntaxTreeNode(tokens[3]);
			target.addChild(branch2);
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

		{
			String inputStr = "+1-2";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							UnaryOperation.PLUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							BinaryOperation.MINUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(2.0)), };
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[2]);
			SyntaxTreeNode branch1 = new SyntaxTreeNode(tokens[0]);
			branch1.addChild(new SyntaxTreeNode(tokens[1]));
			target.addChild(branch1);
			target.addChild(new SyntaxTreeNode(tokens[3]));
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

		{
			String inputStr = "-1+2";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							UnaryOperation.MINUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							BinaryOperation.PLUS),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(2.0)), };
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[2]);
			SyntaxTreeNode branch1 = new SyntaxTreeNode(tokens[0]);
			branch1.addChild(new SyntaxTreeNode(tokens[1]));
			target.addChild(branch1);
			target.addChild(new SyntaxTreeNode(tokens[3]));
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

		{
			String inputStr = "1+2+3";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(2.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(3.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							BinaryOperation.PLUS), };
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[3]);
			SyntaxTreeNode branch1 = new SyntaxTreeNode(tokens[3]);
			branch1.addChild(new SyntaxTreeNode(tokens[0]));
			branch1.addChild(new SyntaxTreeNode(tokens[1]));
			target.addChild(branch1);
			target.addChild(new SyntaxTreeNode(tokens[2]));
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

		{
			String inputStr = "1 2 3";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(2.0)),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							ArithmeticMode.COMPLEX.createValue(3.0)),

					// Fictitious tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							BinaryOperation.TIMES), };
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[3]);
			SyntaxTreeNode branch1 = new SyntaxTreeNode(tokens[3]);
			branch1.addChild(new SyntaxTreeNode(tokens[0]));
			branch1.addChild(new SyntaxTreeNode(tokens[1]));
			target.addChild(branch1);
			target.addChild(new SyntaxTreeNode(tokens[2]));
			this.testArithmeticMatching(request, options, inputStr, true,
					target);
		}

		/*
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "-3")); Assert.assertEquals(true,
		 * this.testArithmeticMatching(options, "---3"));
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "-+-+3")); Assert.assertEquals(true,
		 * this.testArithmeticMatching(options, "+3+3"));
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "3++3")); Assert.assertEquals(true,
		 * this.testArithmeticMatching(options, "+3++3"));
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "3-3")); Assert.assertEquals(true,
		 * this.testArithmeticMatching(options, "+3-3"));
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "-3+3")); Assert.assertEquals(true,
		 * this.testArithmeticMatching(options, "-3-3"));
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "-3--3")); Assert.assertEquals(true,
		 * this.testArithmeticMatching(options, "-3-+3"));
		 * Assert.assertEquals(true, this.testArithmeticMatching(options,
		 * "-3+-3"));
		 */
	}

	/**
	 * Test the arithmetic setup and matching using a data file.
	 */
	@Test
	public void testArithmeticMatchingFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestArithmeticMatcherFromFile(
				TestingNames.FILE.PARSER.ARITHMETIC.MATCHER, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	/**
	 * Test the syntax tree structure generated by the arithmetic matcher. Tests
	 * are read from a dat afile and parsed using the syntax tree string parser.
	 */
	@Test
	public void testArithmeticMatchingSyntaxFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestArithmeticMatcherSyntaxFromFile(
				TestingNames.FILE.PARSER.ARITHMETIC.MATCHER_SYNTAX, options);
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

}
