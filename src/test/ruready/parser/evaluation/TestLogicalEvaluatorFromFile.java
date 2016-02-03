/*****************************************************************************************
 * Source File: TestLogicalEvaluatorFromFile.java
 ****************************************************************************************/
package test.ruready.parser.evaluation;

import java.util.StringTokenizer;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.entity.DefaultVariableMap;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.LogicalMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.SingleLogicalEvaluationProcessor;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs logical evaluation tests from a file.
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
class TestLogicalEvaluatorFromFile extends TestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestLogicalEvaluatorFromFile.class);

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
	public TestLogicalEvaluatorFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);

		request.setOptions(options);
		// this.tol = tol;

		LogicalMatchingUtil.runLogicalSetup(request);
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

		// Run logical matching on request
		request.setInputString(inputString);
		RequestHandler tp = new SingleLogicalEvaluationProcessor(null);
		tp.run(request);
		SyntaxTreeNode actualResult = (SyntaxTreeNode) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT);

		// ================================
		// Prepare expected output
		// ================================
		// Convert output string back to tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(options);
		matcher.match(outputStr);
		SyntaxTreeNode expectedResult = matcher.getSyntax();
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
