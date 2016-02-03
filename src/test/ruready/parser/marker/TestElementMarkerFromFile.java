/*****************************************************************************************
 * Source File: TestElementMarkerFromFile.java
 ****************************************************************************************/
package test.ruready.parser.marker;

import java.util.HashMap;
import java.util.Map;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.junit.entity.TestOutput;
import net.ruready.common.junit.exports.DefaultTestFileReader;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.service.manager.ElementMarkerProcessor;
import net.ruready.parser.service.manager.SingleStringProcessor;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.ruready.parser.exports.ParserServiceTestInput;
import test.ruready.parser.rl.TestingNames;

/**
 * Reads and runs word tree string -> tree Converter regression tests from a file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */
class TestElementMarkerFromFile extends DefaultTestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestElementMarkerFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestElementMarkerFromFile(String fileName, ParserOptions options)
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
	 * Parse input string into test inputs, which are stored in instance variables.
	 * 
	 * @param inputStr
	 *            a single test's input string from data file
	 */
	@Override
	protected void parseInputString(String inputStr)
	{
		// String format is "expression1 ~ expression2"
		// The "~" symbol must not appear in the parser's grammar
		String[] parts = inputStr.trim().split(
				TestingNames.STRING.REFERENCE_RESPONSE_SEPARATOR, 2);

		this.testInput = new ParserServiceTestInput(parts[0], parts[1], null);
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
		// String format is "elementCount* editDistance score syntaxTreeString"
		// * means looping over all math token stati in their natural order.
		int numParts = MathTokenStatus.values().length;
		String[] parts = outputStr.trim().split("\\s", numParts + 4);
		for (String part : parts)
		{
			logger.debug(part);
		}
		int count = 0;
		// Parse element counts and put them in a map
		Map<MathTokenStatus, Integer> elementCountMap = new HashMap<MathTokenStatus, Integer>();
		for (MathTokenStatus status : MathTokenStatus.values())
		{
			logger.debug("status " + status + " part " + parts[count]);
			elementCountMap.put(status, Integer.parseInt(parts[count++]));
		}

		// Parse edit distance
		double editDistance = Double.parseDouble(parts[count++]);

		// Parse correct-element-fraction score
		double correctElementFraction = Double.parseDouble(parts[count++]);

		// Parse element marker sub-total score
		double elementScore = Double.parseDouble(parts[count++]);

		// Convert the rest of output string to a syntax tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(request.getOptions());
		matcher.match(parts[count++]);
		SyntaxTreeNode syntaxTree = matcher.getSyntax();

		this.expectedTestOutput = new ElementMarkerTestOutput(elementCountMap,
				editDistance, correctElementFraction, syntaxTree, elementScore);
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
		ElementMarkerTestOutput output = (ElementMarkerTestOutput) testOutput;

		StringBuffer s = TextUtil.emptyStringBuffer();
		for (MathTokenStatus status : MathTokenStatus.values())
		{
			s.append(output.getElementCountMap().get(status));
			s.append(" ");
		}

		s.append(output.getEditDistance());
		s.append(" ");
		s.append(output.getCorrectElementFraction());
		s.append(" ");
		s.append(output.getScore());
		s.append(" ");
		s.append(output.getSyntaxTree());

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

		// Run arithmetic matching, absolute canonicalization and element
		// analysis on the request
		request.setInputString(input.getReferenceString());
		RequestHandler tp0 = new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE, request.getOptions()
						.getMathExpressionType(), null);
		tp0.run(request);

		// Parse response:
		// Run arithmetic matching, absolute canonicalization and element
		// analysis on the request
		request.setInputString(input.getResponseString());
		RequestHandler tp1 = new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.RESPONSE, request.getOptions()
						.getMathExpressionType(), null);
		tp1.run(request);

		// Run analysis phase
		RequestHandler tp3 = new ElementMarkerProcessor(null);
		tp3.run(request);

		// Read results. We assume all expressions in the file are legal,
		// hence target should be non-null at this stage.
		// Marker marker = (Marker) request
		// .getAttribute(ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.ELEMENTS_ANALYZER);
		MathTarget responseTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);

		this.actualTestOutput = new ElementMarkerTestOutput((Analysis) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.ELEMENTS_RESULT));

		MathTarget referenceTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE);

		logger.debug("referenceTarget.syntax = " + referenceTarget.getSyntax());
		logger.debug("responseTarget.syntax  = " + responseTarget.getSyntax());
	}
}
