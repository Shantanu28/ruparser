/*******************************************************
 * Source File: ParserHandler.java
 *******************************************************/
package net.ruready.parser.imports;

import net.ruready.common.chain.RequestHandler;

/**
 * An abstraction for processors that are part of parser processing chains.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 15, 2007
 */
public abstract class ParserHandler extends RequestHandler
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a stand-alone handler. <code>nextNode</code> is set to
	 * <code>null</code>.
	 */
	public ParserHandler()
	{
		super(null);
	}

	/**
	 * Initialize a handler in a chain.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public ParserHandler(RequestHandler nextNode)
	{
		super(nextNode);
	}
}
