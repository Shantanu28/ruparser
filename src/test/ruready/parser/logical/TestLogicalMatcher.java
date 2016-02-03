/*****************************************************************************************
 * Source File: TestLogicalMatcher.java
 ****************************************************************************************/
package test.ruready.parser.logical;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.LogicalMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test procedures related to the logical numerical evaluation of expression.
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
 * @version Aug 16, 2007
 */
public class TestLogicalMatcher extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestLogicalMatcher.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test the logical setup phase.
	 */
	@Test
	public void testLogicalSetup()
	{
		// Prepare a request
		ParserOptions options = new ParserOptions();
		ParserRequest request = new DefaultParserRequest(options, null);

		LogicalMatchingUtil.runLogicalSetup(request);

		// Check that the parser object exists. Can't do much more checking
		// beyond that at this point.
		Parser logicalParser = (Parser) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.LOGICAL.PARSER);

		Assert.assertEquals(true, logicalParser != null);
	}

	/**
	 * Test the logical setup and matching for some hard-coded strings.
	 */
	@Test
	public void testLogicalMatching()
	{
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		// Prepare a request
		ParserRequest request = new DefaultParserRequest(options, null);

		// Run setup phase. Compiles the logical parser and places it in
		// the request.
		LogicalMatchingUtil.runLogicalSetup(request);

		{
			String inputStr = "1";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(0, ArithmeticMode.COMPLEX.createValue(1.0)),

					// Fictitious tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							new ResponseValue(),
							MathTokenStatus.FICTITIOUS_CORRECT),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							RelationOperation.ASSIGN,
							MathTokenStatus.FICTITIOUS_CORRECT),
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							new EmptyValue(),
							MathTokenStatus.FICTITIOUS_CORRECT), };
			SyntaxTreeNode expr = new SyntaxTreeNode(tokens[0]);
			SyntaxTreeNode relation = new SyntaxTreeNode(tokens[2]);
			SyntaxTreeNode empty = new SyntaxTreeNode(tokens[3]);
			relation.addChild(expr);
			relation.addChild(empty);
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[1]);
			target.addChild(relation);

			this.testLogicalMatching(request, options, inputStr, true, target);
		}

		{
			String inputStr = "1 < 2";
			MathToken[] tokens =
			{
					// Original tokens
					new MathToken(0, ArithmeticMode.COMPLEX.createValue(1.0)),
					new MathToken(1, RelationOperation.LT),
					new MathToken(2, ArithmeticMode.COMPLEX.createValue(2.0)),

					// Fictitious tokens
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
							new ResponseValue(),
							MathTokenStatus.FICTITIOUS_CORRECT), };
			SyntaxTreeNode exprLeft = new SyntaxTreeNode(tokens[0]);
			SyntaxTreeNode relation = new SyntaxTreeNode(tokens[1]);
			SyntaxTreeNode exprRight = new SyntaxTreeNode(tokens[2]);
			relation.addChild(exprLeft);
			relation.addChild(exprRight);
			SyntaxTreeNode target = new SyntaxTreeNode(tokens[3]);
			target.addChild(relation);

			this.testLogicalMatching(request, options, inputStr, true, target);
		}
	}

	/**
	 * Test the logical setup and matching using a data file.
	 */
	@Test
	public void testLogicalMatchingFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestLogicalMatcherFromFile(
				TestingNames.FILE.PARSER.LOGICAL.MATCHER, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	/**
	 * Test the syntax tree structure generated by the logical matcher. Tests
	 * are read from a dat afile and parsed using the syntax tree string parser.
	 */
	@Test
	public void testLogicalMatchingSyntaxFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestLogicalMatcherSyntaxFromFile(
				TestingNames.FILE.PARSER.LOGICAL.MATCHER_SYNTAX, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Test the logical setup and matching.
	 * 
	 * @param request
	 * @param options
	 * @param inputString
	 * @param expectedCompleteMatch
	 * @param expectedSyntax
	 */
	private void testLogicalMatching(ParserRequest request,
			ParserOptions options, String inputString,
			boolean expectedCompleteMatch, SyntaxTreeNode expectedSyntax)
	{
		logger.debug("Matching '" + inputString + "'");

		// Run matching
		boolean completeMatch = LogicalMatchingUtil.isLogicallyMatched(request,
				inputString);

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
