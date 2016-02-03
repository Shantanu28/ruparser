/*****************************************************************************************
 * Source File: TestUnarySwitchFromFile.java
 ****************************************************************************************/
package test.ruready.parser.absolute;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.absolute.exports.RelationTransposerHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.manager.LogicalMatchingProcessor;
import net.ruready.parser.service.manager.SaveTargetAdapter;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs tests of transposing relations into a canonical form.
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
 * @version Jul 19, 2007
 */
class TestRelationTransposerFromFile extends TestAbsoluteCanonicalizationStepFromFile
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestRelationTransposerFromFile.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestRelationTransposerFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName, options);
	}

	// ========================= IMPLEMENTATION: DefaultTestFileReader =====

	/**
	 * Parse output string into expected results, which are stored in instance variables.
	 * 
	 * @param outputStr
	 *            a single test's output string from data file
	 */
	@Override
	protected void parseOutputString(String outputStr)
	{
		// String format is "syntaxTreeString"

		// Convert output string back to tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request.getOptions());
		matcher.match(outputStr);
		this.expectedTestOutput = new AbsoluteCanonicalizationStepTestOutput(outputStr
				.trim());
	}

	/**
	 * Print a test result in the format that parseOutput reads from the data file.
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

		// Run arithmetic matching and then absolute canonicalization on the
		// request
		request.setInputString(input.getExpression());

		// Run arithmetic matching and then absolute canonicalization on the
		// request
		RequestHandler tp2 = new RelationTransposerHandler(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE, null);
		RequestHandler tp1 = new SaveTargetAdapter(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE, tp2);
		RequestHandler tp0 = new LogicalMatchingProcessor(tp1);
		tp0.run(request);

		// Read results. We assume all expressions in the file are legal,
		// hence target should be non-null at this stage.
		MathTarget target = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);

		this.actualTestOutput = new AbsoluteCanonicalizationStepTestOutput(target
				.getSyntax().toString().trim());
	}
}
