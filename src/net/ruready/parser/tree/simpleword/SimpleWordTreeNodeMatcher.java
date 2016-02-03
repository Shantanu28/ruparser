/*******************************************************
 * Source File: SimpleWordTreeNodeMatcher.java
 *******************************************************/
package net.ruready.parser.tree.simpleword;

import net.ruready.common.tree.ListTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.tree.exports.AbstractTreeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This runs the {@link SimpleWordTreeNodeCompiler} against an input String (See
 * the grammar rules in {@link SimpleWordTreeNodeCompiler}).
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 12, 2007
 */
public class SimpleWordTreeNodeMatcher extends AbstractTreeMatcher<ListTreeNode<String>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SimpleWordTreeNodeMatcher.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a syntax tree string parser from options. Uses the
	 * {@link SimpleWordTreeNodeCompiler} to prepare the parser used by the
	 * matcher.
	 * 
	 * @param options
	 *            control options object
	 */
	public SimpleWordTreeNodeMatcher(final ParserOptions options)
	{
		super(options, new SimpleWordTreeNodeCompiler());
	}

	// ========================= IMPLEMENTATION: AbstractTreeMatcher =======

	/**
	 * @see net.ruready.parser.tree.exports.AbstractTreeMatcher#createEmptyTree()
	 */
	@Override
	protected ListTreeNode<String> createEmptyTree()
	{
		return new ListTreeNode<String>(null);
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
