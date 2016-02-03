/*******************************************************************************
 * Source File: TestLogicalMatcherFromFile.java
 ******************************************************************************/
package test.ruready.parser.logical;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.service.entity.LogicalMatchingUtil;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads and runs logical matching tests from a file.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */

class TestLogicalMatcherFromFile extends TestFileReader
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestLogicalMatcherFromFile.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation
	private ParserRequest request = new DefaultParserRequest(null, null);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param fileName
	 * @param options
	 * @throws ApplicationException
	 */
	public TestLogicalMatcherFromFile(String fileName, ParserOptions options)
			throws ApplicationException
	{
		super(fileName);
		request.setOptions(options);
		LogicalMatchingUtil.runLogicalSetup(request);
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
		boolean actualCompleteMatch = LogicalMatchingUtil.isLogicallyMatched(request,
				inputStr);
		logger.debug("inputStr " + inputStr + " expected " + expectedCompleteMatch
				+ " actual " + actualCompleteMatch);
		return expectedCompleteMatch == actualCompleteMatch;
	}

	// ========================= PUBLIC STATIC METHODS =====================

	// ========================= PRIVATE STATIC METHODS ====================

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

}
