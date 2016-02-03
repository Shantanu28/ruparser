/*******************************************************
 * Source File: SaveSyntaxTreeAdapter.java
 *******************************************************/
package net.ruready.parser.service.manager;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler saves the syntax tree of the arithmetic target prepared by
 * previous handlers. It serves an adapter between those handlers and subsequent
 * handlers that use the request attribute set here.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class SaveSyntaxTreeAdapter extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SaveSyntaxTreeAdapter.class);

	// ========================= FIELDS ====================================

	// Attribute name to save syntax tree under
	private String attributeNameTree;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a stand-alone syntax tree saving handler.
	 * 
	 * @param attributeNameTree
	 *            Attribute name to save syntax tree under
	 */
	public SaveSyntaxTreeAdapter(String attributeNameTree)
	{
		super();
		this.attributeNameTree = attributeNameTree;
	}

	/**
	 * Initialize a tokenizer handler.
	 * 
	 * @param attributeNameTree
	 *            Attribute name to save syntax tree under
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public SaveSyntaxTreeAdapter(String attributeNameTree, RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameTree = attributeNameTree;
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
		SyntaxTreeNode tree = target.getSyntax();

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		request.setAttribute(attributeNameTree, tree);

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
		return ParserNames.HANDLER.ADAPTER.NAME + CommonNames.MISC.TAB_CHAR
				+ "Save syntax tree";
	}
}
