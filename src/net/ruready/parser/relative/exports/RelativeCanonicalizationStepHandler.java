/*******************************************************************************
 * Source File: RelativeCanonicalizationStepHandler.java
 ******************************************************************************/
package net.ruready.parser.relative.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler executes one step of an absolute canonicalization on an
 * arithmetic target and its syntax tree.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
class RelativeCanonicalizationStepHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(RelativeCanonicalizationStepHandler.class);

	// ========================= FIELDS ====================================

	// Name of this canonicalization step
	private String name;

	// ED computer stored by a previously run marker handler
	protected EditDistanceComputer<MathToken, SyntaxTreeNode> editDistanceComputer;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a canonicalization step handler.
	 * 
	 * @param name
	 *            Name of this canonicalization step
	 */
	public RelativeCanonicalizationStepHandler(String name)
	{
		super();
		this.name = name;
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param name
	 *            Name of this canonicalization step
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public RelativeCanonicalizationStepHandler(String name,
			RequestHandler nextNode)
	{
		super(nextNode);
		this.name = name;
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Process the targets and their syntax trees. This is a hook. By default,
	 * this does nothing.
	 * 
	 * @param referenceTarget
	 *            reference string target
	 * @param responseTarget
	 *            student response target
	 */
	protected void processTarget(MathTarget referenceTarget,
			MathTarget responseTarget)
	{

	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	final protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		// ParserRequest parserRequest = (ParserRequest) request;
		// Assumes that all variables in the options object are
		// numerical and none are symbolic (null-valued)
		// ParserOptions options = parserRequest.getOptions();

		// Get the two targets
		MathTarget referenceTarget =
				(MathTarget) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE);
		MathTarget responseTarget =
				(MathTarget) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);

		// Get the ED computer
		editDistanceComputer =
				(EditDistanceComputer<MathToken, SyntaxTreeNode>) request
						.getAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.EDIT_DISTANCE_COMPUTER);

		// ====================================================
		// Business logic
		// ====================================================
		// Construct a canonicalizer object
		// logger.debug("Initial ref " + referenceTarget.getSyntax());
		// logger.debug("Initial resp " + responseTarget.getSyntax());
		this.processTarget(referenceTarget, responseTarget);

		// logger.debug("Final ref " + referenceTarget.getSyntax());
		// logger.debug("Final resp " + responseTarget.getSyntax());

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		// No need to attach, target is already attached and we change one of
		// its fields
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
		return ParserNames.HANDLER.ANALYSIS.RELATIVE_CANONICALIZATION.NAME
				+ CommonNames.MISC.TAB_CHAR + name;
	}
}
