/*******************************************************************************
 * Source File: MathParserService.java
 ******************************************************************************/
package net.ruready.parser.service.exports;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.RequestHandlerChain;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.rl.parser.ParserService;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Runs the entire parser analysis on a pair of strings: reference vs. response.
 * Includes setup, matching, evaluation & numerical comparison, canonicalization
 * (absolute and relative), and generates edit distance and nodal mapping.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 17, 2007
 */
class MathParserService extends RequestHandlerChain implements ParserService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathParserService.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an arithmetic grammar matching processor.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name of the arithmetic target to process
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this processor
	 *            is completed. If <code>null</code>, the request processing
	 *            chain will terminate after this handler handles the request.
	 */
	public MathParserService(String attributeNameTarget, RequestHandler nextNode)
	{
		super(null, nextNode);

		// ------------------------------------------------------------------
		// Don't use a static block to define chain order, because it
		// depends on instance parameters
		// ------------------------------------------------------------------
		// This is where the order of operations of this chain is defined.
		// Handlers will be invoked in their order of appearance on the list.
		List<RequestHandler> tempList = new ArrayList<RequestHandler>();

		// TODO: add parser flow here
		// Run arithmetic matching on expression #0
		// request.setInputString(expression0);
		// RequestHandler tp = new ArithmeticMatchingProcessor(null);
		// tp.run(request);
		// tp = new
		// SaveSyntaxTreeAdapter(ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE);
		// tp.run(request);
		//
		// // Run arithmetic matching on expression #1
		// request.setInputString(expression1);
		// tp = new ArithmeticMatchingProcessor(null);
		// tp.run(request);
		// tp = new
		// SaveSyntaxTreeAdapter(ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.RESPONSE);
		// tp.run(request);
		//
		// // Set options
		// ParserOptions specificOptions =
		// (ParserOptions)defaultOptions.clone();
		// specificOptions.setPrecisionTol(precisionTol);
		// request.setOptions(specificOptions);
		// // logger.debug("defaultOptions " + defaultOptions);
		// // logger.debug("specificOptions " + specificOptions);
		//		
		// // Numerically compare the resulting syntax trees
		// tp = new NumericalComparisonHandler();
		// tp.run(request);

		RequestHandler[] specificHandlerList = tempList
				.toArray(new RequestHandler[tempList.size()]);

		this.initialize(specificHandlerList);

	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public ParserServiceID getIdentifier()
	{
		return ParserServiceID.MATH_PARSER;
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
		return ParserNames.HANDLER.SERVICE.NAME + CommonNames.MISC.TAB_CHAR + "Math Parser";
	}

}
