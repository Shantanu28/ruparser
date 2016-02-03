/*******************************************************
 * Source File: ATPMEditDistanceHandler.java
 *******************************************************/
package net.ruready.parser.atpm.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.atpm.manager.NodeComparisonCost;
import net.ruready.parser.atpm.manager.ShashaEditDistanceComputer;
import net.ruready.parser.atpm.manager.WeightedNodeComparisonCost;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler for the ATPM edit distance and nodal mapping computation.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class ATPMEditDistanceHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ATPMEditDistanceHandler.class);

	// ========================= FIELDS ====================================

	// Options object
	protected ParserOptions options;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a generic ATPM edit distance computation handler.
	 */
	public ATPMEditDistanceHandler()
	{
		super();
	}

	/**
	 * Initialize a generic ATPM edit distance computation handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ATPMEditDistanceHandler(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= ABSTRACT METHODS ==========================

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
		ParserRequest parserRequest = (ParserRequest) request;
		this.options = parserRequest.getOptions();

		// Get the two targets
		MathTarget referenceTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE);
		// Get the two syntax trees
		MathTarget responseTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);

		// ====================================================
		// Business logic
		// ====================================================

		// Use the comparison tolerance & cost map of the options object
		NodeComparisonCost<MathToken> costComputer = null;
		costComputer = new WeightedNodeComparisonCost(options.getPrecisionTol(), options
				.getCostMap());
		// Run the edit distance algorithm
		EditDistanceComputer<MathToken, SyntaxTreeNode> e = new ShashaEditDistanceComputer<MathToken, SyntaxTreeNode>(
				referenceTarget.getSyntax(), responseTarget.getSyntax(), costComputer);
		logger.debug(e);

		// ====================================================
		// Attach outputs to the request
		// ====================================================

		// Save the ED computer
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.EDIT_DISTANCE_COMPUTER,
				e);

		return false;
	}

	// ========================= GETTERS & SETTERS =========================

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return "Edit distance computer";
	}
}
