/*******************************************************
 * Source File: SaveTargetAdapter.java
 *******************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler saves an arithmetic target prepared by previous handlers. It
 * serves an adapter between those handlers and subsequent handlers that use the
 * request attribute set here.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class SaveTargetAdapter extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SaveTargetAdapter.class);

	// ========================= FIELDS ====================================

	// Attribute name to save arithmetic target under
	private String attributeNameTarget;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone syntax tree saving handler.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name to save arithmetic target under
	 */
	public SaveTargetAdapter(String attributeNameTarget)
	{
		super();
		this.attributeNameTarget = attributeNameTarget;
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name to save arithmetic target under
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public SaveTargetAdapter(String attributeNameTarget, RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameTarget = attributeNameTarget;
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	@Override
	protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		MathTarget target = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.MATH);

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(attributeNameTarget, target);

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
		return ParserNames.HANDLER.ADAPTER.NAME + CommonNames.MISC.TAB_CHAR + "Save target";
	}
}
