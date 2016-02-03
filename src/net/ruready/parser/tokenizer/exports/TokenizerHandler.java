/*****************************************************************************************
 * Source File: TokenizerHandler.java
 ****************************************************************************************/
package net.ruready.parser.tokenizer.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.tokenizer.entity.MathAssembly;
import net.ruready.parser.tokenizer.manager.MathTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler runs the tokenization phase of the parser. The input string is read from
 * the request.
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
public class TokenizerHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TokenizerHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone tokenizer handler.
	 */
	public TokenizerHandler()
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
	public TokenizerHandler(RequestHandler nextNode)
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
		String inputString = parserRequest.getInputString();
		ParserOptions options = parserRequest.getOptions();

		// ====================================================
		// Business logic
		// ====================================================

		// Initialize and customize the tokenizer
		Tokenizer t =
				new MathTokenizer(options.getVariableNames(), options.getArithmeticMode());

		// Create an element assembly from the input string
		t.setString(inputString);
		Assembly assembly = new MathAssembly(t);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.TOKENIZER.TOKENIZER, t);
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.ASSEMBLY.TOKENIZER, assembly);

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
		return "Tokenizer";
	}
}
