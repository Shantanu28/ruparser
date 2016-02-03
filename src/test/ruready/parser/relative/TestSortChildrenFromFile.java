/*******************************************************
 * Source File: TestSortChildrenFromFile.java
 *******************************************************/
package test.ruready.parser.relative;

import java.util.ArrayList;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.atpm.exports.ATPMEditDistanceHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.relative.exports.SortChildrenHandler;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs tests of commutative multi-nary operation children branch
 * sorting from a data file. In fact, tests also run some required abs. can.
 * steps prior to the actual collapsing step.
 * 
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
 * @version Sep 8, 2007
 */
class TestSortChildrenFromFile extends TestRelativeCanonicalizationStepFromFile implements
		Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestSortChildrenFromFile.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestSortChildrenFromFile(String fileName, ParserOptions options)
			throws ApplicationException
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
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request.getOptions());
		matcher.match(outputStr);
		this.expectedTestOutput = new RelativeCanonicalizationStepTestOutput(outputStr
				.trim());
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
		RelativeCanonicalizationStepTestOutput output = (RelativeCanonicalizationStepTestOutput) testOutput;

		StringBuffer s = TextUtil.emptyStringBuffer();
		s.append(output.getResponseTreeString());

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
		RelativeCanonicalizationStepTestInput input = (RelativeCanonicalizationStepTestInput) aTestInput;

		// Save syntax trees in request, wrapped by fictitious targets for
		// handler interface ompliance
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				new MathTarget(request.getOptions(), new ArrayList<Token>(), input
						.getReferenceTree()));
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
				new MathTarget(request.getOptions(), new ArrayList<Token>(), input
						.getResponseTree()));

		// Run the edit distance algorithm because we must have a nodal mapping
		// before running RC steps.
		RequestHandler rh1 = new SortChildrenHandler(null);
		RequestHandler rh0 = new ATPMEditDistanceHandler(rh1);
		rh0.run(request);

		// Read results. We assume all expressions in the file are legal,
		// hence target should be non-null at this stage.
		MathTarget responseTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);

		this.actualTestOutput = new RelativeCanonicalizationStepTestOutput(responseTarget
				.getSyntax().toString().trim());
	}
}
