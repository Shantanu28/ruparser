/*****************************************************************************************
 * Source File: ParametricEvaluationMatcherHandler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.parser.arithmetic.manager.ArithmeticMatcher;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler runs arithmetic matching on a string. Assumes that an arithmetic compiler
 * already exists in the request.
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
public class ArithmeticMatcherHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ArithmeticMatcherHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone tokenizer handler.
	 */
	public ArithmeticMatcherHandler()
	{
		super();
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public ArithmeticMatcherHandler(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	@Override
	protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		ParserRequest parserRequest = (ParserRequest) request;
		ParserOptions options = parserRequest.getOptions();
		String inputString = parserRequest.getInputString();
		Parser arithmeticParser =
				(Parser) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.ARITHMETIC.PARSER);

		// ====================================================
		// Business logic
		// ====================================================

		// Create a wrapper object that will run the arithmetic parser's
		// matching
		ArithmeticMatcher arithmeticMatcher =
				new ArithmeticMatcher(options, arithmeticParser);
		arithmeticMatcher.match(inputString);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.ARITHMETIC.COMPLETE_MATCH,
				arithmeticMatcher.isCompleteMatch());
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH, arithmeticMatcher
				.getTarget());

		return false;
	}

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return "Arithmetic matcher";
	}
}
