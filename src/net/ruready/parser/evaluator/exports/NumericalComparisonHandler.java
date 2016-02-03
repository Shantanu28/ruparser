/*****************************************************************************************
 * Source File: NumericalComparisonHandler.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.evaluator.entity.RandomSampleGenerator;
import net.ruready.parser.evaluator.entity.SampleGenerator;
import net.ruready.parser.evaluator.manager.NumericalComparator;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler compares two syntax trees by numerically evaluating them for multiple
 * values generate by a sample generator.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class NumericalComparisonHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NumericalComparisonHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone tokenizer handler.
	 */
	public NumericalComparisonHandler()
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
	public NumericalComparisonHandler(RequestHandler nextNode)
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
		// Assumes that all variables in the options object are
		// numerical and none are symbolic (null-valued)
		ParserOptions options = parserRequest.getOptions();

		// Get the two syntax trees
		SyntaxTreeNode referenceTree = (SyntaxTreeNode) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.REFERENCE);
		// Get the two syntax trees
		SyntaxTreeNode responseTree = (SyntaxTreeNode) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.SYNTAX_TREE.RESPONSE);

		// ====================================================
		// Business logic
		// ====================================================

		// Prepare samples; using00 uniform samples for now.
		// TODO: change that random samples
		// SampleGenerator generator = new UniformSampleGenerator(new
		// ComplexValue(1.0), new ComplexValue(2.0), 6);
		SampleGenerator generator = new RandomSampleGenerator(
				options.getArithmeticMode(), -5.0, 5.0, 15);

		// Construct an evaluation object
		NumericalComparator<?> comparator = new NumericalComparatorFactory().createType(
				options.getMathExpressionType(), options, generator);
		// logger.debug("tree " + tree);
		boolean result = (comparator.compare(referenceTree, responseTree) == 0);
		VariableMap sample = comparator.getSample();

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT, result);
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.SAMPLE, sample);

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
		return "Numerical comparison";
	}
}
