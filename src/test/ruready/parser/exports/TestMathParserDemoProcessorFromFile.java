/*****************************************************************************************
 * Source File: TestMathParserDemoProcessorFromFile.java
 ****************************************************************************************/
package test.ruready.parser.exports;

import java.util.HashMap;
import java.util.Map;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.junit.exports.DefaultTestFileReader;
import net.ruready.common.misc.Auxiliary;
import net.ruready.common.rl.Environment;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.common.text.TextUtil;
import net.ruready.common.util.EnumUtil;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.exports.ParserRequestUtil;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.ruready.parser.imports.ParserEnvironment;
import test.ruready.parser.rl.TestingNames;

/**
 * Reads and runs math parser demo flow tests from a data file.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 30, 2007
 */
class TestMathParserDemoProcessorFromFile extends DefaultTestFileReader implements
		Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestMathParserDemoProcessorFromFile.class);

	// ========================= FIELDS ====================================

	/**
	 * An environment with a resource locator that has reference to a parser service
	 * provider.
	 */
	private Environment environment = new ParserEnvironment();

	/**
	 * Used for multiple string parsing + evaluation.
	 */
	private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestMathParserDemoProcessorFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
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
		// Initialize the resource locator
		environment.setUp();

		// Run the arithmetic setup phase on a request.
		RequestHandler tp = new ArithmeticSetupProcessor(null);
		tp.run(request);
	}

	/**
	 * @see test.ruready.common.test.TestFileReader#tearDown()
	 */
	@Override
	protected void tearDown()
	{
		environment.tearDown();
	}

	/**
	 * Parse input string into test inputs, which are stored in instance variables.
	 * 
	 * @param inputStr
	 *            a single test's input string from data file
	 */
	@Override
	protected void parseInputString(String inputStr)
	{
		// String format is "expression1 ~ expression2 ~ mathExpressionType "
		// The "~" symbol must not appear in the parser's grammar
		String[] parts = inputStr.trim().split(
				TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR, 3);

		this.testInput = new ParserServiceTestInput(parts[0], parts[1], EnumUtil
				.createFromString(MathExpressionType.class, parts[2].trim()));
	}

	/**
	 * Parse output string into expected results, which are stored in instance variables.
	 * 
	 * @param outputStr
	 *            a single test's output string from data file
	 */
	@Override
	protected void parseOutputString(String outputStr)
	{
		// String format is "elementCount* equivalent editDistance score ...
		// syntaxTreeString ~ HTMLString"
		// * means looping over all math token stati in their natural order.
		int numParts = MathTokenStatus.values().length;
		String[] parts = outputStr.trim().split("\\s", numParts + 6);
		// for (String part : parts) {
		// logger.debug(part);
		// }

		int count = 0;
		// Parse element counts and put them in a map
		Map<MathTokenStatus, Integer> elementCountMap = new HashMap<MathTokenStatus, Integer>();
		for (MathTokenStatus status : MathTokenStatus.values())
		{
			// logger.debug("status " + status + " part " + parts[count]);
			elementCountMap.put(status, Integer.parseInt(parts[count++]));
		}

		// Parse equivalence result
		boolean equivalent = Boolean.parseBoolean(parts[count++]);

		// Parse edit distance
		double editDistance = Double.parseDouble(parts[count++]);

		// Parse correct-element-fraction score
		double correctElementFraction = Double.parseDouble(parts[count++]);

		// Parse element marker sub-total score
		double elementScore = Double.parseDouble(parts[count++]);

		// Standard assessment test response total score
		double score = Double.parseDouble(parts[count++]);

		// Split the rest of the string using a separator
		String[] separatedParts = parts[count++].trim().split(
				TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR, 2);
		count = 0;

		// Convert the first separated part into a syntax tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request.getOptions());
		matcher.match(separatedParts[count++]);
		SyntaxTreeNode syntaxTree = matcher.getSyntax();

		Analysis analysis = new Analysis(elementCountMap, editDistance,
				correctElementFraction, syntaxTree, elementScore);

		// Convert the second separated part into an HTML string
		String HTMLString = separatedParts[count++].trim();

		this.expectedTestOutput = new MathParserProcessorTestOutput(analysis, equivalent,
				score, HTMLString);
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
		MathParserProcessorTestOutput output = (MathParserProcessorTestOutput) testOutput;

		StringBuffer s = TextUtil.emptyStringBuffer();
		for (MathTokenStatus status : MathTokenStatus.values())
		{
			s.append(output.getElementCountMap().get(status));
			s.append(" ");
		}

		s.append(output.isEquivalent());
		s.append(" ");
		s.append(output.getEditDistance());
		s.append(" ");
		s.append(output.getCorrectElementFraction());
		s.append(" ");
		s.append(output.getMarkerSubTotalScore());
		s.append(" ");
		s.append(output.getTotalScore());
		s.append(" ");
		s.append(output.getSyntaxTree().toString().trim());
		s.append(" ");
		s.append(TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR);
		s.append(" ");
		s.append(output.getHTMLString());

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
		ParserServiceTestInput input = (ParserServiceTestInput) aTestInput;

		// Run the math parser demo flow on the request
		RequestHandler rh = (RequestHandler) environment.getParserService(
				ParserServiceID.MATH_DEMO, input.getReferenceString(), input
						.getResponseString(), input.getMathExpressionType());
		rh.run(request);

		// Print and clear the message log
		logger.debug(ParserRequestUtil.printMessages(request));
		request.clearMessages();

		// Read results. We assume all expressions in the file are legal,

		Analysis analysis = ParserRequestUtil.getLatestMarkerAnalysis(request);
		boolean equivalent = ParserRequestUtil.isEquivalent(request);
		double score = ParserRequestUtil.getResponseScore(request);
		String HTMLString = ParserRequestUtil.getHTMLResponseString(request);

		// Some debugging printouts
		MathTarget responseTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);
		MathTarget referenceTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE);
		logger.debug("referenceTarget.syntax = " + referenceTarget.getSyntax());
		logger.debug("responseTarget.syntax  = " + responseTarget.getSyntax());

		this.actualTestOutput = new MathParserProcessorTestOutput(analysis, equivalent,
				score, HTMLString);
	}
}
