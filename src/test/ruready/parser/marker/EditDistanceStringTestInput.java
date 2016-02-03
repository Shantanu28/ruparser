/*******************************************************************************
 * Source File: EditDistanceStringTestInput.java
 ******************************************************************************/
package test.ruready.parser.marker;

import net.ruready.common.junit.entity.TestInput;
import net.ruready.common.tree.ListTreeNode;

/**
 * Edit distance data file test inputs for tree strings.
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
class EditDistanceStringTestInput implements TestInput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Reference tree's String representation
	private final ListTreeNode<String> referenceTree;

	// Response tree's String representation
	private final ListTreeNode<String> responseTree;

	// ========================= CONStringUCTORS ==============================

	/**
	 * ConStringuct a test input container from fields.
	 * 
	 * @param referenceTree
	 * @param responseTree
	 */
	public EditDistanceStringTestInput(
			final ListTreeNode<String> referenceTree,
			final ListTreeNode<String> responseTree)
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
	public ListTreeNode<String> getReferenceTree()
	{
		return referenceTree;
	}

	/**
	 * @return the responseTree
	 */
	public ListTreeNode<String> getResponseTree()
	{
		return responseTree;
	}

}
