/*******************************************************************************
 * Source File: TestAbsoluteCanonicalizationFromFile.java
 ******************************************************************************/
package test.ruready.parser.absolute;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.manager.SingleStringProcessor;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs tests of the entire absolute canonicalization phase from a
 * data file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 4, 2007
 */
class TestAbsoluteCanonicalizationFromFile extends
		TestAbsoluteCanonicalizationStepFromFile
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestAbsoluteCanonicalizationFromFile.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestAbsoluteCanonicalizationFromFile(String fileName,
			ParserOptions options) throws ApplicationException
	{
		super(fileName, options);
	}

	// ========================= IMPLEMENTATION: DefaultTestFileReader =====

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
		// String format is "syntaxTreeString"

		// Convert output string back to tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request
				.getOptions());
		matcher.match(outputStr);
		this.expectedTestOutput = new AbsoluteCanonicalizationStepTestOutput(
				outputStr.trim());
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
		AbsoluteCanonicalizationStepTestOutput output = (AbsoluteCanonicalizationStepTestOutput) testOutput;

		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(output.getTreeString());

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
		AbsoluteCanonicalizationStepTestInput input = (AbsoluteCanonicalizationStepTestInput) aTestInput;

		// Run the entire single-string processing chain, which includes the
		// absolute canonicalization and all other required tasks, on the
		// request
		request.setInputString(input.getExpression());
		RequestHandler rh = new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE, request.getOptions()
						.getMathExpressionType(), null);
		rh.run(request);

		// Read results. We assume all expressions in the file are legal,
		// hence target should be non-null at this stage.
		MathTarget target = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);

		this.actualTestOutput = new AbsoluteCanonicalizationStepTestOutput(
				target.getSyntax().toString().trim());
	}
}
