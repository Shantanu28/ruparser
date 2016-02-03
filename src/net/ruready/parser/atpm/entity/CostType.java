/*****************************************************************************************
 * Source File: CostType.java
 ****************************************************************************************/
package net.ruready.parser.atpm.entity;

/**
 * Possible costs of tree node editing due to different outcomes of node comparisons
 * during the edit distance algorithm and nodal mapping generation. Includes all possible
 * types of node comparisons. A specific edit distance computer may use only some of the
 * types, not necessarily all of them. We assume that {@link CostType#toString()} is a
 * unique identifier, like <code>name()</code>.
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
public enum CostType
{
	// ========================= CONSTANTS =================================

	// Cost of adding a new node or deleting an existing node
	INSERT_DELETE,

	// Cost of deleting an existing node
	// DELETE,

	// Nodes are equal (up to a tolerance)
	EQUAL,

	// Nodes are unequal and have the same type
	UNEQUAL_SAME_TYPE,

	// Nodes are unequal, have the same type and are both operations
	UNEQUAL_SAME_TYPE_OPERATION,

	// Nodes are unequal and have different types
	UNEQUAL_DIFFERENT_TYPE,

	// Nodes are unequal, have different types, and one of the tokens is an
	// operation
	UNEQUAL_DIFFERENT_TYPE_OPERATION,

	// One node is fictitious and the other is not
	UNEQUAL_FICTITIOUS;

	// ========================= FIELDS ====================================

	// ========================= METHODS ===================================

}
