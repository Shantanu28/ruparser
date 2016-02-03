/*******************************************************
 * Source File: AbsoluteCanonicalizationStepHandler.java
 *******************************************************/
package net.ruready.parser.absolute.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
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
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
class AbsoluteCanonicalizationStepHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(AbsoluteCanonicalizationStepHandler.class);

	// ========================= FIELDS ====================================

	// Attribute name of the arithmetic target to process
	private String attributeNameTarget;

	// Name of this canonicalization step
	private String name;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a canonicalization step handler.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name of the arithmetic target to process
	 * @param name
	 *            Name of this canonicalization step
	 */
	public AbsoluteCanonicalizationStepHandler(String attributeNameTarget, String name)
	{
		super();
		this.attributeNameTarget = attributeNameTarget;
		this.name = name;
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name of the arithmetic target to process
	 * @param name
	 *            Name of this canonicalization step
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public AbsoluteCanonicalizationStepHandler(String attributeNameTarget, String name,
			RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameTarget = attributeNameTarget;
		this.name = name;
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Process the target and its syntax tree. This is a hook. By default, this
	 * does nothing.
	 * 
	 * @param target
	 *            arithmetic target to process
	 */
	protected void processTarget(MathTarget target)
	{

	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
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
		MathTarget target = (MathTarget) request
				.getAttribute(attributeNameTarget);
		// Get the syntax tree
		// SyntaxTreeNode tree = (SyntaxTreeNode) request
		// .getAttribute(ParserNames.REQUEST.ATTRIBUTE.SINGLE_TREE);
		// SyntaxTreeNode tree = target.getSyntax();

		// ====================================================
		// Business logic
		// ====================================================
		// Construct a canonicalizer object
		logger.debug("Initial tree " + target.getSyntax());
		processTarget(target);
		logger.debug("Final   tree " + target.getSyntax());
		// logger.debug(CommonNames.MISC.NEW_LINE_CHAR + target.toStringDetailed());

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
		return ParserNames.HANDLER.ANALYSIS.ABSOLUTE_CANONICALIZATION.NAME
				+ CommonNames.MISC.TAB_CHAR + name;
	}
}
