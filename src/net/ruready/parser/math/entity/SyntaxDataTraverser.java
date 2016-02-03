/*****************************************************************************************
 * Source File: SyntaxDataTraverser.java
 ****************************************************************************************/
package net.ruready.parser.math.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ruready.common.tree.SimpleTreeVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Traverses a math expression syntax tree into a list of math tokens.
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
class SyntaxDataTraverser extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SyntaxDataTraverser.class);

	// ========================= FIELDS =====================================

	// Output list
	private List<MathToken> tokens = new ArrayList<MathToken>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a tree node traverser.
	 */
	private SyntaxDataTraverser()
	{

	}

	/**
	 * A facade to be called instead of constructing this object. Traverses a syntax tree.
	 * 
	 * @param syntax
	 *            syntax tree [root node]
	 */
	public static List<MathToken> traverse(SyntaxTreeNode syntax)
	{
		// Traverse the tree into a token list
		SyntaxDataTraverser traverser = new SyntaxDataTraverser();
		traverser.executeOnTree(syntax);
		List<MathToken> tokens = traverser.getTokens();

		// Sort the list by math token values, stati and primary element index
		// in the original assembly
		Collections.sort(tokens, new MathTokenComparatorFull());
		return tokens;
	}

	// ========================= IMPLEMENTATION: TreeCommutativeDepth ============

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePre(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePre(SyntaxTreeNode thisNode)
	{
		// Add token of this node to global token list
		tokens.add(thisNode.getData());
		return null;
	}

	/**
	 * @param thisNode
	 * @return
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePost(net.ruready.common.tree.MutableTreeNode)
	 */
	@Override
	protected Object executePost(SyntaxTreeNode thisNode)
	{
		return null;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the tokens
	 */
	private List<MathToken> getTokens()
	{
		return tokens;
	}
}
