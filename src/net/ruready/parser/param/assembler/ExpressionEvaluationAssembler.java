/*****************************************************************************************
 * Source File: AppendWordAssembler.java
 ****************************************************************************************/
package net.ruready.parser.param.assembler;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.evaluator.manager.ArithmeticEvaluator;
import net.ruready.parser.param.entity.ParametricEvaluationTarget;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserException;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.SingleArithmeticEvaluationProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Evaluate an expression using the arithmetic parser and an {@link ArithmeticEvaluator},
 * round the resulting number, and append it to the evaluated string in the target.
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
class ExpressionEvaluationAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(ExpressionEvaluationAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Replace the top token in the stack with the token's Double value. Add the number to
	 * the target's lists of expression parts.
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// Pop a token from the assembly
		Token t = (Token) a.pop();
		String expression = new String(t.sval());
		logger.debug("expression '" + expression + "'");

		// Get required info/options for math parsing from the target
		ParametricEvaluationTarget target = (ParametricEvaluationTarget) a.getTarget();

		// Parse and numerically evaluate the expression
		ParserRequest request = target.getRequest();
		request.setInputString(expression);
		try
		{
			RequestHandler tp = new SingleArithmeticEvaluationProcessor(null);
			tp.run(request);
			NumericalValue evaluatedExpression =
					(NumericalValue) request
							.getAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT);
			logger.debug("evaluatedExpression " + evaluatedExpression);
			// Add the evaluated expression to the target's evalString.
			// evalString should be automatically correctly formatted after
			// matching.
			target.appendToEvalString(evaluatedExpression.toString());
		}
		catch (MathParserException e)
		{
			// Unrecognized expression, append a default string
			target
					.appendToEvalString(ParserNames.PARAMETRIC_EVALUATION.UNKNOWN_EXPRESSION);
			target.setLegal(false);
		}
	}

}
