/*******************************************************************************
 * Source File: ParametricEvaluationUtil.java
 ******************************************************************************/
package net.ruready.parser.service.entity;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.misc.Utility;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserException;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.LogicalMatchingProcessor;
import net.ruready.parser.service.manager.LogicalSetupProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to logical matching.
 * <p>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br> (c) 2006-07 Continuing
 * Education , University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah
 * @version Jul 9, 2007
 */
public class LogicalMatchingUtil implements Utility
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(LogicalMatchingUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private LogicalMatchingUtil()
	{

	}

	// ========================= CONSTRUCTORS ==============================

	// ========================= PUBLIC STATIC METHODS =====================

	/**
	 * Run the logical setup phase on a request.
	 * 
	 * @param request
	 */
	public static void runLogicalSetup(ParserRequest request)
	{
		// Process request
		RequestHandler tp = new LogicalSetupProcessor(null);
		tp.run(request);
	}

	/**
	 * Test logical matching on a single string. Assumes that the logical setup
	 * is already done.
	 * 
	 * @param request
	 * @param inputString
	 *            input string
	 * @return was the string completely matched or not
	 */
	public static void testLogicalMatching(ParserRequest request, String inputString)
	{
		// Prepare a new request
		request.setInputString(inputString);

		// Run logical matching on request
		request.setInputString(inputString);
		RequestHandler tp = new LogicalMatchingProcessor(null);
		tp.run(request);
	}

	/**
	 * Test logical matching on a single string and output the syntax tree to
	 * the request. Assumes that the logical setup is already done.
	 * 
	 * @param request
	 * @param inputString
	 *            input string
	 * @return was the string completely matched or not
	 */
	public static void testLogicalMatchingSyntax(ParserRequest request, String inputString)
	{
		// Prepare a new request
		request.setInputString(inputString);

		// Run logical matching on request
		request.setInputString(inputString);
		RequestHandler tp = new LogicalMatchingProcessor(null);
		tp.run(request);
		// tp = new LogicalMatching2SingleTreeAdapter(null);
		// tp.run(request);
	}

	/**
	 * Test the logical setup and matching with the parser options of this
	 * instance.
	 * 
	 * @param inputString
	 *            input string
	 * @return was the string completely matched or not
	 */
	public static boolean isLogicallyMatched(ParserRequest request, String inputString)
	{
		boolean completeMatch = false;
		try {
			LogicalMatchingUtil.testLogicalMatching(request, inputString);
			// Read results
			Boolean temp = (Boolean) request
					.getAttribute(ParserNames.REQUEST.ATTRIBUTE.LOGICAL.COMPLETE_MATCH);
			if (temp != null) {
				completeMatch = temp.booleanValue();
			}
		}
		catch (MathParserException e) {
			logger.debug(e);
		}

		return completeMatch;
	}

	/**
	 * Run logical matching on a string and output its syntax tree (before any
	 * canonicalization).
	 * 
	 * @param inputString
	 *            input string
	 * @return syntax tree
	 */
	public static SyntaxTreeNode generateSyntaxTree(ParserRequest request,
			String inputString)
	{
		try {
			LogicalMatchingUtil.testLogicalMatchingSyntax(request, inputString);
			// Read results
			MathTarget target = (MathTarget) request
					.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);
			SyntaxTreeNode tree = target.getSyntax();
			return tree;
		}
		catch (MathParserException e) {
			logger.debug(e);
			return null;
		}

	}

	// ========================= PRIVATE STATIC METHODS ====================
}
