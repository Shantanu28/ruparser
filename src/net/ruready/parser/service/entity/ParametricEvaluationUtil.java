/*******************************************************************************
 * Source File: ParametricEvaluationUtil.java
 ******************************************************************************/
package net.ruready.parser.service.entity;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.misc.Utility;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.ParametricEvaluationSetupProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to arithmetic matching tests.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 19, 2007
 */

public class ParametricEvaluationUtil implements Utility
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(ParametricEvaluationUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ParametricEvaluationUtil()
	{

	}

	// ========================= CONSTRUCTORS ==============================

	// ========================= PUBLIC STATIC METHODS =====================

	/**
	 * Run the arithmetic setup phase on a request.
	 * 
	 * @param request
	 */
	public static void runParametricEvaluationSetup(ParserRequest request)
	{
		// Prepare param. eval. parser
		RequestHandler tp = new ParametricEvaluationSetupProcessor(null);
		tp.run(request);
	}

	// ========================= PRIVATE STATIC METHODS ====================
}
