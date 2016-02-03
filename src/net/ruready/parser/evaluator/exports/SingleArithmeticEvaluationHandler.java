/*****************************************************************************************
 * Source File: SingleArithmeticEvaluationHandler.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.evaluator.manager.ArithmeticEvaluator;
import net.ruready.parser.evaluator.manager.Evaluator;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler numerically evaluates a mathematical expression for a single variable
 * configuration (i.e. all variables at fixed numerical values, resulting in a single
 * numerical result).
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
public class SingleArithmeticEvaluationHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(SingleArithmeticEvaluationHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone tokenizer handler.
	 */
	public SingleArithmeticEvaluationHandler()
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
	public SingleArithmeticEvaluationHandler(RequestHandler nextNode)
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
		// Get the syntax tree
		// SyntaxTreeNode tree = (SyntaxTreeNode) request
		// .getAttribute(ParserNames.REQUEST.ATTRIBUTE.SINGLE_TREE);
		MathTarget target = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);
		SyntaxTreeNode tree = target.getSyntax();

		// ====================================================
		// Business logic
		// ====================================================

		// Construct an evaluation object
		Evaluator<NumericalValue> evaluator = new ArithmeticEvaluator(options
				.getVariables());
		// logger.debug("tree " + tree);
		NumericalValue result = evaluator.evaluate(tree);
		// Round evaluation result to tolerance specified by options
		result = options.round(result);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT, result);

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
		return "Single evaluation";
	}
}
