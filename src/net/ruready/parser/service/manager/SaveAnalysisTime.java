/*******************************************************
 * Source File: SaveAnalysisTime.java
 *******************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.marker.entity.Analysis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler saves the analysis time in the analysis object.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class SaveAnalysisTime extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SaveAnalysisTime.class);

	// ========================= FIELDS ====================================

	// Attribute name to read & save analysis object under
	private final String attributeNameAnalysis;

	// Analysis time to save
	private final double elapsedTime;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone syntax tree saving handler.
	 * 
	 * @param attributeNameAnalysis
	 *            attribute name to read and save analysis object under
	 * @param elapsedTime
	 *            Analysis time to save
	 */
	public SaveAnalysisTime(String attributeNameAnalysis, double elapsedTime)
	{
		super();
		this.attributeNameAnalysis = attributeNameAnalysis;
		this.elapsedTime = elapsedTime;
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param attributeNameAnalysis
	 *            Attribute name to save syntax tree under
	 * @param elapsedTime
	 *            Analysis time to save
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public SaveAnalysisTime(String attributeNameAnalysis, double elapsedTime,
			RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameAnalysis = attributeNameAnalysis;
		this.elapsedTime = elapsedTime;
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	@Override
	protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		Analysis analysis = (Analysis) request.getAttribute(attributeNameAnalysis);

		// ====================================================
		// Businses logic
		// ====================================================
		analysis.setElapsedTime(elapsedTime);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(attributeNameAnalysis, analysis);

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
		return "Save analysis time" + CommonNames.MISC.TAB_CHAR + CommonNames.TREE.BRACKET_OPEN
				+ attributeNameAnalysis + CommonNames.TREE.BRACKET_CLOSE;
	}
}
