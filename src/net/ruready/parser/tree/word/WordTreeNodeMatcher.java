/*******************************************************
 * Source File: WordTreeNodeMatcher.java
 *******************************************************/
package net.ruready.parser.tree.word;

import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.tree.exports.AbstractTargetMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * his runs the {@link WordTreeNodeCompiler} against an input String (See the
 * grammar rules in {@link WordTreeNodeCompiler}).
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 12, 2007
 */
public class WordTreeNodeMatcher extends AbstractTargetMatcher
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(WordTreeNodeMatcher.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a syntax tree string parser from options. Uses the
	 * {@link WordTreeNodeCompiler} to prepare the parser used by the matcher.
	 * 
	 * @param options
	 *            control options object
	 */
	public WordTreeNodeMatcher(final ParserOptions options)
	{
		super(options, new WordTreeNodeCompiler());
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
