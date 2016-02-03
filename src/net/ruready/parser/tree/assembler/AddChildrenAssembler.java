/*******************************************************************************
 * Source File: AddChildrenAssembler.java
 ******************************************************************************/
package net.ruready.parser.tree.assembler;

import java.io.Serializable;
import java.util.List;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.tree.AbstractListTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop all children syntax trees from the assembly's stack, and add them to the
 * parent syntax tree node.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 21/12/2005
 */
class AddChildrenAssembler<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, T>>
		extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(AddChildrenAssembler.class);

	// ========================= FIELDS ====================================

	// Fence token
	private Token fence;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a multi-nary operation assembler.
	 * 
	 * @param fence
	 *            fence token
	 * @param multinaryOp
	 *            multi-nary operation
	 */
	public AddChildrenAssembler(Token fence)
	{
		super();
		this.fence = fence;
	}

	// ========================= IMPLEMENTATION: Assembler =================

	/**
	 * @see net.ruready.parser.core.parse.Assembler#workOn(net.ruready.parser.core.parse.Assembly)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);

		// Pop all terms above the fence
		List<T> children = Assembler.<T>elementsAbove(a, fence);
		// logger.debug("children " + children);

		// Pop the parent tree that was pushed onto the stack right
		// before its children
		T parent = (T) a.pop();
		// Must clear the children's vectors because during matching parent
		// may carry over some children from another matching attempt.
		parent.removeAllChilds();

		// Loop over children and add them to the parent in reverse ordering
		// (assembly stack is LIFO)
		for (T child : children) {
			parent.addChild(0, child);
		}
		// Push the resulting expression on the stack
		a.push(parent);

		// logger.debug("end, a=" + a);
	}

	// ========================= METHODS ===================================

}
