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
import net.ruready.parser.service.manager.ArithmeticMatchingProcessor;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to arithmetic matching.
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
public class ArithmeticMatchingUtil implements Utility
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ArithmeticMatchingUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ArithmeticMatchingUtil()
	{

	}

	// ========================= CONSTRUCTORS ==============================

	// ========================= PUBLIC STATIC METHODS =====================

	/**
	 * Run the arithmetic setup phase on a request.
	 * 
	 * @param request
	 */
	public static void runArithmeticSetup(ParserRequest request)
	{
		// Process request
		RequestHandler tp = new ArithmeticSetupProcessor(null);
		tp.run(request);
	}

	/**
	 * Test arithmetic matching on a single string. Assumes that the arithmetic
	 * setup is already done.
	 * 
	 * @param request
	 * @param inputString
	 *            input string
	 * @return was the string completely matched or not
	 */
	public static void testArithmeticMatching(ParserRequest request, String inputString)
	{
		// Prepare a new request
		request.setInputString(inputString);

		// Run arithmetic matching on request
		request.setInputString(inputString);
		RequestHandler tp = new ArithmeticMatchingProcessor(null);
		tp.run(request);
	}

	/**
	 * Test arithmetic matching on a single string and output the syntax tree to
	 * the request. Assumes that the arithmetic setup is already done.
	 * 
	 * @param request
	 * @param inputString
	 *            input string
	 * @return was the string completely matched or not
	 */
	public static void testArithmeticMatchingSyntax(ParserRequest request,
			String inputString)
	{
		// Prepare a new request
		request.setInputString(inputString);

		// Run arithmetic matching on request
		request.setInputString(inputString);
		RequestHandler tp = new ArithmeticMatchingProcessor(null);
		tp.run(request);
		// tp = new ArithmeticMatching2SingleTreeAdapter(null);
		// tp.run(request);
	}

	/**
	 * Test the arithmetic setup and matching with the parser options of this
	 * instance.
	 * 
	 * @param inputString
	 *            input string
	 * @return was the string completely matched or not
	 */
	public static boolean isArithmeticallyMatched(ParserRequest request,
			String inputString)
	{
		boolean completeMatch = false;
		try {
			ArithmeticMatchingUtil.testArithmeticMatching(request, inputString);
			// Read results
			Boolean temp = (Boolean) request
					.getAttribute(ParserNames.REQUEST.ATTRIBUTE.ARITHMETIC.COMPLETE_MATCH);
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
	 * Run arithmetic matching on a string and output its syntax tree (before
	 * any canonicalization).
	 * 
	 * @param inputString
	 *            input string
	 * @return syntax tree
	 */
	public static SyntaxTreeNode generateSyntaxTree(ParserRequest request,
			String inputString)
	{
		try {
			ArithmeticMatchingUtil.testArithmeticMatchingSyntax(request, inputString);
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
