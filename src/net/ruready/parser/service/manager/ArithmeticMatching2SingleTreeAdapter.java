/*******************************************************
 * Source File: ArithmeticMatching2SingleTreeAdapter.java
 *******************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler connects the result of arithmetic matching (an arithmetic
 * target) to the required input for all single-tree processors, e.g., absolute
 * canonicalization, numerical evaluation.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
@Deprecated
public class ArithmeticMatching2SingleTreeAdapter extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(ArithmeticMatching2SingleTreeAdapter.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone tokenizer handler.
	 */
	public ArithmeticMatching2SingleTreeAdapter()
	{
		super();
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ArithmeticMatching2SingleTreeAdapter(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @param request
	 * @return
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@Override
	protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		// ParserRequest parserRequest = (ParserRequest) request;
		// SyntaxTreeNode tree = (SyntaxTreeNode) request
		// .getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);
		// SyntaxTreeNode tree = ((ParametricEvaluationTarget) request
		// .getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH))
		// .getSyntax();

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		// request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.SINGLE_TREE,
		// tree);

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
		return ParserNames.HANDLER.ADAPTER.NAME + CommonNames.MISC.TAB_CHAR
				+ "Arithmetic matching -> single tree";
	}
}
