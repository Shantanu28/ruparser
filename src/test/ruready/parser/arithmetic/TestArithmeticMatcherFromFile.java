/*******************************************************
 * Source File: TestLogicalMatcherFromFile.java
 *******************************************************/
package test.ruready.parser.arithmetic;

import net.ruready.common.exception.ApplicationException;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.entity.ArithmeticMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ruready.common.junit.exports.TestFileReader;

/**
 * Reads and runs arithmetic matching tests from a file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */

class TestArithmeticMatcherFromFile extends TestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestArithmeticMatcherFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestArithmeticMatcherFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		request.setOptions(options);
		ArithmeticMatchingUtil.runArithmeticSetup(request);
	}

	// ========================= IMPLEMENTATION: TestFileReader ============

	/**
	 * @see test.ruready.common.test.TestFileReader#Assert.assertEquals(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected boolean assertEquals(String inputStr, String outputStr)
	{
		logger.debug("Processing: inputStr " + inputStr + " output " + outputStr);
		boolean expectedCompleteMatch = Boolean.parseBoolean(outputStr);
		boolean actualCompleteMatch = ArithmeticMatchingUtil.isArithmeticallyMatched(
				request, inputStr);
		logger.debug("inputStr " + inputStr + " expected " + expectedCompleteMatch
				+ " actual " + actualCompleteMatch);
		return expectedCompleteMatch == actualCompleteMatch;
	}

	// ========================= PUBLIC STATIC METHODS =====================

	// ========================= PRIVATE STATIC METHODS ====================

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

}
