/*****************************************************************************************
 * Source File: SetTraversalVisitor.java
 ****************************************************************************************/
package net.ruready.parser.relative.manager;

import net.ruready.common.misc.Auxiliary;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.visitor.MathTokenTraversalVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sets pre- or post- traversal index of math token.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, USyntaxTreeNode 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, USyntaxTreeNode 84112
 *         Protected by U.S. Provisional Patent U-4003, February 2006
 * @version Jun 12, 2007
 */
class SetTraversalVisitor implements Auxiliary, MathTokenTraversalVisitor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SetTraversalVisitor.class);

	// ========================= FIELDS =====================================

	// ====================
	// Inputs
	// ====================

	/**
	 * Traversal index to set in the token
	 */
	private final int traversalIndex;

	// ====================
	// Convenient local variables
	// ====================

	// ====================
	// Outputs
	// ====================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * @param mathToken
	 * @param traversalIndex
	 */
	public SetTraversalVisitor(final MathToken mathToken, final int traversalIndex)
	{
		super();
		this.traversalIndex = traversalIndex;
		mathToken.accept(this);
	}

	// ========================= IMPLEMENTATION: Visitor ======================

	/**
	 * @param visitable
	 * @see net.ruready.common.visitor.Visitor#visit(net.ruready.common.visitor.Visitable)
	 */
	public void visit(MathToken visitable)
	{
		// accept() takes care of that
	}

	// ========================= IMPLEMENTATION: MathTokenTraversalVisitor ====

	/**
	 * Return the traversal index to be set in the next processed math token
	 * 
	 * @return the traversal index to be set in the next processed math token
	 */
	public int getTraversalIndex()
	{
		return traversalIndex;
	}

	// ========================= GETTERS & SETTERS =========================

}
