/*******************************************************
 * Source File: SetInputStringAdapter.java
 *******************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler sets a new input string in the request.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class SetInputStringAdapter extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SetInputStringAdapter.class);

	// ========================= FIELDS ====================================

	// New input string
	private String inputString;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone syntax tree saving handler.
	 * 
	 * @param inputString
	 *            new input string to be set in the request
	 */
	public SetInputStringAdapter(String inputString)
	{
		super();
		this.inputString = inputString;
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param inputString
	 *            new input string to be set in the request
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public SetInputStringAdapter(String inputString, RequestHandler nextNode)
	{
		super(nextNode);
		this.inputString = inputString;
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	@Override
	protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		ParserRequest parserRequest = (ParserRequest) request;

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		parserRequest.setInputString(inputString);

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
		return ParserNames.HANDLER.ADAPTER.NAME + CommonNames.MISC.TAB_CHAR + "Input string";
	}
}
