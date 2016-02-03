/*****************************************************************************************
 * Source File: AbstractTreeNodeCompiler.java
 ****************************************************************************************/
package net.ruready.parser.tree.exports;

import java.io.Serializable;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.manager.AbstractCompiler;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Repetition;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.common.parser.core.tokens.Symbol;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.tree.assembler.TreeAssemblerFactory;
import net.ruready.parser.tree.assembler.TreeAssemblerID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides a parser that recognizes a string generated by
 * {@link SyntaxTreeNode#toString()}, and reconstructs the {@link SyntaxTreeNode} object.
 * symbols. The grammar rules are:
 * <p>
 * <code>
 *   tree  = data Names.TREE.BRACKET_OPEN tree* Names.TREE.BRACKET_CLOSE;
 *   data  = type Names.TREE.SEPARATOR value Names.TREE.SEPARATOR status;
 *   value = Word | Num | "( )";
 * </code>
 * To make sure that the parenthesis token is not broken into two tokens, a custom
 * tokenizer is used.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9399<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Sep 28, 2007
 */
public class AbstractTreeNodeCompiler<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, T>>
		implements AbstractCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(AbstractTreeNodeCompiler.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------
	// Parser control options; currently not in use
	// private ParserOptions options;

	// Parser of a tree node data
	private final Parser dataParser;

	// Data assembler that processed the results prepared by the data parser
	private final Assembler dataAssembler;

	// A post-processing expression assembler invoked at the end of matching
	private final Assembler expressionAssembler;

	// Assembler factory
	private final AbstractAssemblerFactory factory = new TreeAssemblerFactory<D, T>();

	// -------------------------------------------------
	// Temporary variables to alleviate cyclic
	// grammar dependencies. Must be lazy-initialized.
	// -------------------------------------------------
	// cycle: tree -> tree
	private Sequence _tree = null;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a tree string parser from options.
	 * 
	 * @param options
	 *            control options object
	 * @param dataParser
	 *            Parser of a tree node data
	 * @param dataAssembler
	 *            Data assembler that processed the results prepared by the data parser
	 * @param expressionAssembler
	 *            A post-processing expression assembler invoked at the end of matching
	 */
	public AbstractTreeNodeCompiler(
	/* ParserOptions options, */final Parser dataParser, final Assembler dataAssembler,
			final Assembler expressionAssembler)
	{
		super();
		// this.options = options;
		this.dataParser = dataParser;
		this.dataAssembler = dataAssembler;
		this.expressionAssembler = expressionAssembler;
	}

	// ========================= IMPLEMENTATION: Compiler ==================

	/**
	 * Main parser call (with an optional control sequence). Returns a parser that will
	 * recognize tree strings.
	 * 
	 * @return a parser that will recognize tree strings
	 */
	public Parser parser()
	{
		Sequence globalExpr = new Sequence("tree string");
		globalExpr.add(tree());
		globalExpr.setAssembler(expressionAssembler);
		return globalExpr;
	}

	// ========================= METHODS ===================================

	// ========================= ARITHMETIC PARSER COMPILATION =============

	/**
	 * Returns a parser that will recognize a tree. The grammar rule is
	 * <p>
	 * <code>
	 * tree  = data Names.TREE.BRACKET_OPEN tree* Names.TREE.BRACKET_CLOSE;
	 * </code>
	 * 
	 * @return a parser that will recognize a tree
	 */
	private Parser tree()
	{
		// This use of a variable avoids the infinite
		// recursion inherent in the grammar; factor depends
		// on powFactor, and powFactor depends on factor.
		if (_tree == null)
		{
			// logger.debug("tree()");
			// Must init this variable before all calls
			_tree = new Sequence("tree");

			// tree = data Names.TREE.BRACKET_OPEN tree*
			// Names.TREE.BRACKET_CLOSE;

			// This node's data
			_tree.add(dataParser.setAssembler(dataAssembler));

			// Opening bracket
			_tree.add(new Symbol(CommonNames.TREE.BRACKET_OPEN).discard());

			// ---------------------------------------------------------------
			// Children trees. The repetition parser stacks a fence at the
			// beginning of the children sequence using a pre assembler. The
			// fence is retrieved by its assmbler.
			// ---------------------------------------------------------------

			// A single child
			Parser child = tree();

			// The children sequence
			Repetition children = new Repetition(child);
			children.setPreAssembler(factory.createAssembler(TreeAssemblerID.SET_FENCE,
					ParserNames.TREE_STRING_COMPILER.CHILDREN_FENCE));
			children.setAssembler(factory.createAssembler(TreeAssemblerID.ADD_CHILDREN,
					ParserNames.TREE_STRING_COMPILER.CHILDREN_FENCE));

			_tree.add(children);

			// Closing bracket
			_tree.add(new Symbol(CommonNames.TREE.BRACKET_CLOSE).discard());

		}
		return _tree;
	}

	// ========================= UTILITY OBJECT COMPILATION ================

}
