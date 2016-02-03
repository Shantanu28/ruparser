/*****************************************************************************************
 * Source File: MathParserDemoWithImagesService.java
 ****************************************************************************************/
package net.ruready.parser.service.exports;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.RequestHandlerChain;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.rl.parser.ParserService;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.parser.analysis.exports.AutoAnalysisHandler;
import net.ruready.parser.evaluator.exports.NumericalComparisonHandler;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.port.output.image.exports.TreeImagePrinterOutputPort;
import net.ruready.parser.port.output.text.exports.HtmlPrinterOutputPort;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.score.exports.AssessmentTestScoreComputerHandler;
import net.ruready.parser.service.manager.ArithmeticSetupProcessor;
import net.ruready.parser.service.manager.LogicalSetupProcessor;
import net.ruready.parser.service.manager.SetInputStringAdapter;
import net.ruready.parser.service.manager.SingleStringProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Executes a prepared processing chain for a parser demo. Runs the full parser flow on a
 * pair of strings and generates an analysis object and an HTML string. Includes tree
 * image generation for both the reference and response.
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
class MathParserDemoWithImagesService extends RequestHandlerChain implements
		ParserService
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(MathParserDemoWithImagesService.class);

	// ========================= FIELDS ====================================

	// Inputs: reference + response strings
	private String referenceString;

	private String responseString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser demo flow with a <code>null</code> next node (stand-alone chain).
	 * 
	 * @param referenceString
	 *            input reference string
	 * @param responseString
	 *            input response string
	 * @param mathExpressionType
	 *            type of supported mathematical expressions (arithmetic or logical)
	 */
	public MathParserDemoWithImagesService(final String referenceString,
			final String responseString, final MathExpressionType mathExpressionType)
	{
		this(referenceString, responseString, mathExpressionType, null);
	}

	/**
	 * Create a parser demo flow with a <code>null</code> next node (stand-alone chain).
	 * 
	 * @param referenceString
	 *            input reference string
	 * @param responseString
	 *            input response string
	 * @param mathExpressionType
	 *            type of supported mathematical expressions (arithmetic or logical)
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this processor is
	 *            completed. If <code>null</code>, the request processing chain will
	 *            terminate after this handler handles the request.
	 */
	public MathParserDemoWithImagesService(final String referenceString,
			final String responseString, final MathExpressionType mathExpressionType,
			RequestHandler nextNode)
	{
		super(null, nextNode);

		this.referenceString = referenceString;
		this.responseString = responseString;

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

		// ==================================
		// Setup phase
		// ==================================

		switch (mathExpressionType)
		{
			case ARITHMETIC:
			{
				// Run the arithmetic setup phase only on a request.
				tempList.add(new ArithmeticSetupProcessor());
				break;
			}
			case LOGICAL:
			{
				// Run the logical setup phase on a request; contains arithmetic setup.
				tempList.add(new LogicalSetupProcessor());
				break;
			}
		}

		// ==================================
		// Matching phase
		// ==================================

		// Parse reference:
		// Run matching, absolute canonicalization and element
		// analysis on the request
		tempList.add(new SetInputStringAdapter(referenceString));
		tempList.add(new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE, mathExpressionType,
				null));

		// Parse response:
		// Run matching, absolute canonicalization and element
		// analysis on the request
		tempList.add(new SetInputStringAdapter(responseString));
		tempList.add(new SingleStringProcessor(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
				ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.RESPONSE, mathExpressionType,
				null));

		// Run numerical comparison (evaluation phase)
		tempList.add(new NumericalComparisonHandler());

		// ==================================
		// Analysis phase
		// ==================================

		// Auto-detect analysis type and run it; iterative relative
		// canonicalization in case of ATPM analysis.
		tempList.add(new AutoAnalysisHandler(null));

		// Element marker
		// tempList.add(new ElementMarkerProcessor(null));

		// ATPM marker
		// tempList.add(new ATPMMarkerProcessor(null));

		// Automatically decides what marker to invoke based on the parser
		// options at run time
		// tempList.add(new AutoMarkerHandler(null));

		// ==================================
		// Output ports
		// ==================================

		// Plug in a score computer (standard assessment test formula; no hints
		// in the demo)
		// tempList.add(new VerboseRequestHandler(new
		// AssessmentTestScoreComputerHandler()));
		tempList.add(new AssessmentTestScoreComputerHandler());

		// Plug in the HTML printer of the reference arithmetic target
		tempList.add(new HtmlPrinterOutputPort(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.HTML.REFERENCE_STRING));

		// Plug in the HTML printer of the response arithmetic target
		tempList.add(new HtmlPrinterOutputPort(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
				ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.HTML.RESPONSE_STRING));

		// Plug in the image printers for the reference and response targets
		tempList.add(new TreeImagePrinterOutputPort(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.IMAGE.TREE_REFERENCE,
				ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.IMAGE.TREE_SIZE_REFERENCE));
		tempList.add(new TreeImagePrinterOutputPort(
				ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE,
				ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.IMAGE.TREE_RESPONSE,
				ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.IMAGE.TREE_SIZE_RESPONSE));

		// Save chain in this object
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
		return ParserServiceID.MATH_DEMO_WITH_IMAGES;
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
		return ParserNames.HANDLER.SERVICE.NAME + CommonNames.MISC.TAB_CHAR
				+ "Parser Demo + images";
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the referenceString
	 */
	public String getReferenceString()
	{
		return referenceString;
	}

	/**
	 * @return the responseString
	 */
	public String getResponseString()
	{
		return responseString;
	}

}
