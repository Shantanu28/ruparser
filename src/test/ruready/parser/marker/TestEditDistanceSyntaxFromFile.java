/*******************************************************
 * Source File: TestEditDistanceSyntaxFromFile.java
 *******************************************************/
package test.ruready.parser.marker;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.junit.exports.DefaultTestFileReader;
import net.ruready.common.misc.Auxiliary;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.atpm.manager.NodeComparisonCost;
import net.ruready.parser.atpm.manager.ShashaEditDistanceComputer;
import net.ruready.parser.atpm.manager.WeightedNodeComparisonCost;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.ruready.parser.rl.TestingNames;

/**
 * Reads and runs tests of edit distance computation and nodal mapping
 * generation algorithm on word trees.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class TestEditDistanceSyntaxFromFile extends DefaultTestFileReader implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestEditDistanceSyntaxFromFile.class);

	// ========================= FIELDS ====================================

	// Parser options object
	private final ParserOptions options;

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestEditDistanceSyntaxFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		this.options = options;
		request.setOptions(options);
		ArithmeticMatchingUtil.runArithmeticSetup(request);
	}

	// ========================= IMPLEMENTATION: DefaultTestFileReader =====

	/**
	 * @see test.ruready.common.test.TestFileReader#setUp()
	 */
	@Override
	protected void setUp()
	{

	}

	/**
	 * Parses an input string into test inputs, which are stored in instance
	 * variables. Sets the <code>testInput</code> instance field.
	 * 
	 * @param inputStr
	 *            a single test's input string from data file
	 * @see test.ruready.common.util.reader.DefaultTestFileReader#parseInputString(java.lang.String)
	 */
	@Override
	protected void parseInputString(String inputStr)
	{
		// String format is "referenceExpression ~ responseExpression"
		String[] parts = inputStr.trim().split(
				TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR, 2);

		int count = 0;

		// Read reference tree string and convert it to a tree
		String referenceString = parts[count++];
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request.getOptions());
		matcher.match(referenceString);
		SyntaxTreeNode referenceTree = matcher.getSyntax();
		// logger.debug("referenceString " + referenceString);
		// logger.debug("referenceTree:\n" + referenceTree);

		// Read response tree string and convert it to a tree
		String responseString = parts[count++];
		matcher.match(responseString);
		SyntaxTreeNode responseTree = matcher.getSyntax();
		// logger.debug("responseString " + responseString);
		// logger.debug("responseTree:\n" + responseTree);

		this.testInput = new EditDistanceSyntaxTestInput(referenceTree, responseTree);
	}

	/**
	 * Parse output string into expected results, which are stored in instance
	 * variables.
	 * 
	 * @param outputStr
	 *            a single test's output string from data file
	 */
	@Override
	protected void parseOutputString(String outputStr)
	{
		// Format: editDistance nodalMapping
		String[] parts = outputStr.trim().split("\\s", 2);
		int count = 0;

		// Parse edit distance
		double editDistance = Double.parseDouble(parts[count++]);

		// Parse nodalMappingString
		String nodalMappingString = parts[count++];

		this.expectedTestOutput = new EditDistanceTestOutput(editDistance,
				nodalMappingString);
	}

	/**
	 * @see test.ruready.common.util.reader.DefaultTestFileReader#encodeTestOutput(test.ruready.common.test.TestOutput)
	 */
	@Override
	protected String encodeTestOutput(TestOutput testOutput)
	{
		// Cast to a friendlier version
		EditDistanceTestOutput output = (EditDistanceTestOutput) testOutput;

		// Format: editDistance nodalMapping
		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(output.getEditDistance());
		s.append(" ");
		s.append(output.getNodalMappingString());
		return s.toString();
	}

	/**
	 * @see test.ruready.common.util.reader.DefaultTestFileReader#runTest(test.ruready.common.test.TestInput)
	 */
	@Override
	protected void runTest(TestInput aTestInput)
	{
		// Cast to a friendlier version
		EditDistanceSyntaxTestInput input = (EditDistanceSyntaxTestInput) aTestInput;

		// Run edit distance algorithm
		NodeComparisonCost<MathToken> costComputer = null;
		costComputer = new WeightedNodeComparisonCost(options.getPrecisionTol(), options
				.getCostMap());
		// Run the edit distance algorithm
		EditDistanceComputer<MathToken, SyntaxTreeNode> e = new ShashaEditDistanceComputer<MathToken, SyntaxTreeNode>(
				input.getReferenceTree(), input.getResponseTree(), costComputer);

		this.actualTestOutput = new EditDistanceTestOutput(e.getEditDistance(), e
				.getMapping().toString());
	}
}
