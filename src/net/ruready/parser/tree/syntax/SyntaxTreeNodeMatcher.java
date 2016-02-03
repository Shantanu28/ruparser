/*****************************************************************************************
 * Source File: SyntaxTreeNodeMatcher.java
 ****************************************************************************************/
package net.ruready.parser.tree.syntax;

import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.tree.exports.AbstractTargetMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This runs the {@link SyntaxTreeNodeCompiler} against an input String (See the grammar
 * rules in {@link SyntaxTreeNodeCompiler}).
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education 1901 East South Campus Dr., Room 2197-E (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> Scientific Computing and
 *         Imaging Institute University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 05/12/2005
 */
public class SyntaxTreeNodeMatcher extends AbstractTargetMatcher
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SyntaxTreeNodeMatcher.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a syntax tree string parser from options. Uses the
	 * {@link SyntaxTreeNodeCompiler} to prepare the parser used by the matcher.
	 * 
	 * @param options
	 *            control options object
	 */
	public SyntaxTreeNodeMatcher(final ParserOptions options)
	{
		super(options, new SyntaxTreeNodeCompiler());
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
