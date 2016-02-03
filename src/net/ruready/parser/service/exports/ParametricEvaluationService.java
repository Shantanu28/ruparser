/*****************************************************************************************
 * Source File: MathParserDemoService.java
 ****************************************************************************************/
package net.ruready.parser.service.exports;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.RequestHandlerChain;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.rl.parser.ParserService;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.manager.ParametricEvaluationProcessor;
import net.ruready.parser.service.manager.SetInputStringAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Evaluates a parametric string using the parametric evaluation parser that in turn
 * invokes the arithmetic parser as necessary.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 17, 2007
 */
class ParametricEvaluationService extends RequestHandlerChain implements ParserService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(ParametricEvaluationService.class);

	// ========================= FIELDS ====================================

	// Parametric string
	private final String parametricString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Evaluates a parametric string using the parametric evaluation parser that in turn
	 * invokes the arithmetic parser as necessary.
	 * 
	 * @param referenceString
	 *            Parametric string to be evaluated
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this processor is
	 *            completed. If <code>null</code>, the request processing chain will
	 *            terminate after this handler handles the request.
	 */
	public ParametricEvaluationService(final String parametricString,
			final RequestHandler nextNode)
	{
		super(null, nextNode);

		this.parametricString = parametricString;

		// ------------------------------------------------------------------
		// Don't use a static block to define chain order, because it
		// depends on instance parameters
		// ------------------------------------------------------------------
		// This is where the order of operations of this chain is defined.
		// Handlers will be invoked in their order of appearance on the list.
		List<RequestHandler> tempList = new ArrayList<RequestHandler>();

		// ==================================
		// Input ports
		// ==================================
		tempList.add(new SetInputStringAdapter(parametricString));

		// ==================================
		// Run evaluation
		// ==================================

		// Run the arithmetic setup phase on a request.
		tempList.add(new ParametricEvaluationProcessor(null));

		RequestHandler[] specificHandlerList = tempList
				.toArray(new RequestHandler[tempList.size()]);

		this.initialize(specificHandlerList);

		// The results may be accessed by the getter methods below after
		// start() is invoked.
	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public ParserServiceID getIdentifier()
	{
		return ParserServiceID.PARAMETRIC_EVALUATION;
	}

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return this.getIdentifier().getType();
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
		return ParserNames.HANDLER.SERVICE.NAME + CommonNames.MISC.TAB_CHAR + "Parser demo";
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the parametricString
	 */
	public String getParametricString()
	{
		return parametricString;
	}
}
