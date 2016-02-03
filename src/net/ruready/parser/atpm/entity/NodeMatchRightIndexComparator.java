/*****************************************************************************************
 * Source File: NodeMatchRightIndexComparator.java
 ****************************************************************************************/
package net.ruready.parser.atpm.entity;

import java.io.Serializable;
import java.util.Comparator;

import net.ruready.common.tree.AbstractListTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of node matches by their right index field value. Handy in relative
 * canonicalization procedures.
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
public class NodeMatchRightIndexComparator<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
		implements Comparator<NodeMatch<D, T>>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(NodeMatchRightIndexComparator.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an operator precedence comparator.
	 */
	public NodeMatchRightIndexComparator()
	{

	}

	// ========================= IMPLEMENTATION: Comparator ================

	/**
	 * A comparator of node matches by their right index field value.
	 * 
	 * @param element1
	 *            left operand to be compared
	 * @param element2
	 *            right operand to be compared
	 * @return result of the integer comparison of the <code>element1.rightIndex</code>
	 *         and <code>element2.rightIndex</code>
	 */
	public int compare(NodeMatch<D, T> element1, NodeMatch<D, T> element2)
	{
		return new Integer(element1.getRightIndex()).compareTo(element2.getRightIndex());
	}

	// ========================= PRIVATE METHODS ===========================
}
