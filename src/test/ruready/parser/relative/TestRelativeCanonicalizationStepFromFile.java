/*******************************************************
 * Source File: TestRelativeCanonicalizationStepFromFile.java
 *******************************************************/
package test.ruready.parser.relative;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.DefaultTestFileReader;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.ruready.parser.rl.TestingNames;

/**
 * Reads and runs tests of an relative canonicalization step from a data file.
 * This is a base class extended by the various steps to form concrete test
 * cases. Most of them have a common input string and input format so we put
 * related methods here to avoid code duplication.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 4, 2007
 */

abstract class TestRelativeCanonicalizationStepFromFile extends
		DefaultTestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestRelativeCanonicalizationStepFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	protected ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestRelativeCanonicalizationStepFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		request.setOptions(options);
		ArithmeticMatchingUtil.runArithmeticSetup(request);
	}

	// ========================= IMPLEMENTATION: DefaultTestFileReader =====

	/**
	 * @see test.ruready.common.util.TestFileReader#setUp()
	 */
	@Override
	protected void setUp()
	{
		// Run the arithmetic setup phase on a request.
		RequestHandler tp = new ArithmeticSetupProcessor(null);
		tp.run(request);
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

		this.testInput = new RelativeCanonicalizationStepTestInput(referenceTree,
				responseTree);
	}
}
