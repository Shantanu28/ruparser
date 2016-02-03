/*******************************************************
 * Source File: BogusCanonicalizerHandler.java
 *******************************************************/
package net.ruready.parser.relative.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler takes two syntax trees as an input and illustrates a handler
 * code that runs a relative canonicalization step. It only prints the trees.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 6, 2007
 */
public class BogusCanonicalizerHandler extends RelativeCanonicalizationStepHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(BogusCanonicalizerHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * @param name
	 */
	public BogusCanonicalizerHandler()
	{
		super(ParserNames.RELATIVE_CANONICALIZATION.BOGUS);
	}

	/**
	 * @param name
	 * @param nextNode
	 */
	public BogusCanonicalizerHandler(RequestHandler nextNode)
	{
		super(ParserNames.RELATIVE_CANONICALIZATION.BOGUS, nextNode);
	}

	// ========================= IMPLEMENTATION: AbsCanStepHandler (...) ===

	/**
	 * @see net.ruready.parser.relative.exports.RelativeCanonicalizationStepHandler#processTarget(net.ruready.parser.math.entity.MathTarget,
	 *      net.ruready.parser.math.entity.MathTarget)
	 */
	@Override
	protected void processTarget(MathTarget referenceTarget, MathTarget responseTarget)
	{
		// Do nothing, just print the targets
		logger.debug("Initial ref  " + referenceTarget.getSyntax());
		logger.debug("Initial resp " + responseTarget.getSyntax());
	}
}
