/*******************************************************
 * Source File: TestTreeCommutativeDepthFromFile.java
 *******************************************************/
package test.ruready.parser.relative;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.misc.Auxiliary;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.relative.manager.TreeCommutativeDepth;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs tests of commutative multi-nary operation children branch
 * sorting from a data file. In fact, tests also run some required abs. can.
 * steps prior to the actual collapsing step.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 4, 2007
 */
class TestTreeCommutativeDepthFromFile extends TestRelativeCanonicalizationStepFromFile
		implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestTreeCommutativeDepthFromFile.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestTreeCommutativeDepthFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName, options);
	}

	// ========================= IMPLEMENTATION: DefaultTestFileReader =====

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: DefaultTestFileReader =====

	/**
	 * Parses an input string into test inputs, which are stored in instance
	 * variables. Sets the <code>testInput</code> instance field.
	 * 
	 * @param inputStr
	 *            a single test's input string from data file
	 * @see test.ruready.common.util.reader.DefaultTestFileReader#parseInputString(java.lang.String)
	 */
	@Override
	final protected void parseInputString(String inputStr)
	{
		// Read input tree string and convert it to a tree
		String referenceString = inputStr.trim();
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request.getOptions());
		matcher.match(referenceString);
		SyntaxTreeNode tree = matcher.getSyntax();
		// logger.debug("referenceString " + referenceString);
		// logger.debug("referenceTree:\n" + referenceTree);

		this.testInput = new SingleTreeTestInput(tree);
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
		// String format is "treeDepth"
		int commDepth = Integer.parseInt(outputStr);

		this.expectedTestOutput = new TreeCommutativeDepthTestOutput(commDepth);
	}

	/**
	 * Print a test result in the format that parseOutput reads from the data
	 * file.
	 * 
	 * @param result
	 *            test result object
	 * @return string representation in the data file's format
	 */
	@Override
	protected String encodeTestOutput(TestOutput testOutput)
	{
		// Cast to a friendlier version
		TreeCommutativeDepthTestOutput output = (TreeCommutativeDepthTestOutput) testOutput;

		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(output.getCommDepth());

		return s.toString();
	}

	/**
	 * Test the redundancy remover on an input string. The string is matched and
	 * absolutely canonicalized.
	 * 
	 * @param aTestInput
	 *            test input data struct
	 * @see test.ruready.common.util.reader.DefaultTestFileReader#runTest(test.ruready.common.test.TestInput)
	 */
	@Override
	protected void runTest(TestInput aTestInput)
	{
		// Cast to a friendlier version
		SingleTreeTestInput input = (SingleTreeTestInput) aTestInput;

		// Compute commutative depth
		int commDepth = TreeCommutativeDepth.compute(input.getTree());

		// Prepare results object

		this.actualTestOutput = new TreeCommutativeDepthTestOutput(commDepth);
	}
}
