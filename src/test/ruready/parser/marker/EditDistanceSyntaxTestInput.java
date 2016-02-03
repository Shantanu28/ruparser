/*******************************************************************************
 * Source File: EditDistanceSyntaxTestInput.java
 ******************************************************************************/
package test.ruready.parser.marker;

import net.ruready.common.junit.entity.TestInput;
import net.ruready.parser.math.entity.SyntaxTreeNode;

/**
 * Edit distance data file test inputs for syntax trees.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class EditDistanceSyntaxTestInput implements TestInput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Reference tree's String representation
	private final SyntaxTreeNode referenceTree;

	// Response tree's String representation
	private final SyntaxTreeNode responseTree;

	// ========================= CONStringUCTORS ==============================

	/**
	 * ConStringuct a test input container from fields.
	 * 
	 * @param referenceTree
	 * @param responseTree
	 */
	public EditDistanceSyntaxTestInput(final SyntaxTreeNode referenceTree,
			final SyntaxTreeNode responseTree)
	{
		super();
		this.referenceTree = referenceTree;
		this.responseTree = responseTree;
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the referenceTree
	 */
	public SyntaxTreeNode getReferenceTree()
	{
		return referenceTree;
	}

	/**
	 * @return the responseTree
	 */
	public SyntaxTreeNode getResponseTree()
	{
		return responseTree;
	}

}
