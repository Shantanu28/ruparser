/*******************************************************************************
 * Source File: TestSyntaxTreeNodeParserFromFile.java
 ******************************************************************************/
package test.ruready.parser.converter.syntax;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.tree.AsciiPrinter;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs string - to - syntax tree node representation converter back
 * to a tree.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 1, 2007
 */
class TestSyntaxTreeNodeParserFromFile extends TestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestSyntaxTreeNodeParserFromFile.class);

	// ========================= FIELDS ====================================

	// Parser options
	private final ParserOptions options;

	// A struct holding the test results
	private class TestResult
	{
		public String treeString;

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
					+ ((treeString == null) ? 0 : treeString.hashCode());
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
			if (treeString == null) {
				if (other.treeString != null)
					return false;
			}
			// Trim string representations before comparing
			else if (!treeString.trim().equals(other.treeString.trim()))
				return false;
			return true;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "treeString " + treeString;
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
	public TestSyntaxTreeNodeParserFromFile(String fileName,
			ParserOptions options) throws ApplicationException
	{
		super(fileName);
		this.options = options;
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

	}

	/**
	 * @see test.ruready.common.test.TestFileReader#Assert.assertEquals(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected boolean assertEquals(String inputStr, String outputStr)
	{
		logger.debug("############## Processing: inputStr " + inputStr
				+ " output " + outputStr + " ##############");

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
		// Convert tree to string
		AsciiPrinter<MathToken, SyntaxTreeNode> printer = new AsciiPrinter<MathToken, SyntaxTreeNode>(
				new AsciiPrinter.NodeDataPrinter<MathToken>()
				{
					public String print(MathToken data)
					{
						// return CommonNames.MISC.EMPTY_STRING + data.getValue();
						return data.toString();
						// StringBuffer s = new
						// StringBuffer(CommonNames.MISC.EMPTY_STRING);
						// s.append(data.getValue());
						// s.append(Names.TREE.SEPARATOR);
						// s.append(data.getStatus());
						// return s.toString();
					}
				});

		// Convert string to a tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(options);
		matcher.match(inputStr);
		SyntaxTreeNode actualTree = matcher.getSyntax();

		// Convert tree to a string
		String string = printer.print(actualTree);

		// Save results in the actual result set
		actualResult.treeString = string;
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
		// String format is "tree_string_representation"
		expectedResult.treeString = inputStr.trim();
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
		// String format is "legal", just ignore it for now. We're not using it
		// anywhere.
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
