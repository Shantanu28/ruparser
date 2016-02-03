/*******************************************************
 * Source File: SingleArithmeticEvaluationProcessor.java
 *******************************************************/
package net.ruready.parser.service.manager;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.RequestHandlerChain;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.exports.ArithmeticMatcherHandler;
import net.ruready.parser.evaluator.exports.SingleArithmeticEvaluationHandler;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.tokenizer.exports.TokenizerHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Performs tokenization, matching and performing a single numerical evaluation
 * of a syntax tree.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 17, 2007
 */
public class SingleArithmeticEvaluationProcessor extends RequestHandlerChain
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SingleArithmeticEvaluationProcessor.class);

	private static RequestHandler[] specificHandlerList = null;

	/**
	 * Initialize the hard-coded handlers in this chain.
	 */
	static {

		// This is where the order of operations of this chain is defined.
		// Handlers will be invoked in their order of appearance on the list.
		List<RequestHandler> tempList = new ArrayList<RequestHandler>();

		tempList.add(new TokenizerHandler());
		tempList.add(new ArithmeticMatcherHandler());
		// tempList.add(new ArithmeticMatching2SingleTreeAdapter());
		tempList.add(new SingleArithmeticEvaluationHandler());

		specificHandlerList = tempList.toArray(new RequestHandler[tempList.size()]);
	}

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an arithmetic grammar matching processor.
	 * 
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this processor
	 *            is completed. If <code>null</code>, the request processing
	 *            chain will terminate after this handler handles the request.
	 */
	public SingleArithmeticEvaluationProcessor(RequestHandler nextNode)
	{
		super(specificHandlerList, nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return ParserNames.HANDLER.PROCESSOR.NAME + CommonNames.MISC.TAB_CHAR
				+ "Single arithmetic evaluation";
	}

}