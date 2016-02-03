/*******************************************************
 * Source File: AssessmentTestScoreComputerHandler.java
 *******************************************************/
package net.ruready.parser.score.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.score.manager.AssessmentTestScoreComputer;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A parser output port that prints the syntax tree as an HTML string.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class AssessmentTestScoreComputerHandler extends ScoreComputerHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(AssessmentTestScoreComputerHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an HTML printer handler.
	 */
	public AssessmentTestScoreComputerHandler()
	{
		this(null);
	}

	/**
	 * Initialize an HTML printer handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public AssessmentTestScoreComputerHandler(RequestHandler nextNode)
	{
		super(nextNode);
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
		return "Assessment test score computer";
	}

	// ========================= IMPLEMENTATION: ScoreComputerHandler ======

	/**
	 * Process the target -- construct an HTML printer for it. This is a hook.
	 * By default, this does nothing. This should also set the #scoreComputer
	 * field.
	 * 
	 * @param request
	 *            request to read results from
	 */
	@Override
	protected void processRequest(ParserRequest request)
	{
		scoreComputer = new AssessmentTestScoreComputer(request, options.getErrorWeight());
	}
}
