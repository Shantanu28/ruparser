/*******************************************************************************
 * Source File: SimpleWordTreeNodeDataCompiler.java
 ******************************************************************************/
package net.ruready.parser.tree.simpleword;

import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Alternation;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.tokens.Word;
import net.ruready.common.tree.ListTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that recognizes a {@link ListTreeNode}<code><String></code>
 * object's node. The grammar rules are:
 * <p>
 * <code>
 *   data  = value | markedData;
 *   markedData = value Names.TREE.SEPARATOR status;
 *   value = Word;
 * </code>
 * To make sure that the parenthesis token is not broken into two tokens, a
 * custom tokenizer is used.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 28, 2007
 */
class SimpleWordTreeNodeDataCompiler implements AbstractCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(SimpleWordTreeNodeDataCompiler.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options; currently not in use
	// private ParserOptions options;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a tree string parser from options.
	 * 
	 * @param options
	 *            control options object
	 */
	public SimpleWordTreeNodeDataCompiler(
	/* ParserOptions options, */)
	{

	}

	// ========================= METHODS ===================================

	// ========================= IMPLEMENTATION: AbstractCompiler ==========

	/**
	 * Returns a parser that for the grammar rule:
	 * <p>
	 * <code>data = value | markedData;</code>
	 * 
	 * @return a parser that recognizes a tree node data
	 */
	public Parser parser()
	{
		// data = data = value | markedData;
		Alternation data = new Alternation("data");
		data.add(value().setAssembler(new ValueAssembler()));
		return data;
	}

	// ========================= TREE NODE DATA PARSER COMPILATION =========

	/**
	 * Returns a parser that for the grammar rule:
	 * <p>
	 * <code>
	 * value = Word;
	 * </code>
	 * 
	 * @return a parser that recognizes a word value
	 */
	private Parser value()
	{
		Alternation value = new Alternation("value");
		value.add(new Word());
		return value;
	}

	// ========================= UTILITY OBJECT COMPILATION ================

}
