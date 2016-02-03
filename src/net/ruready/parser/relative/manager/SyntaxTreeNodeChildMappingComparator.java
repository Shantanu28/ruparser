/*******************************************************************************
 * Source File: SyntaxTreeNodeChildMappingComparator.java
 ******************************************************************************/
package net.ruready.parser.relative.manager;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.parser.math.entity.MathTokenComparatorFull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of a syntax tree node children list by integer weights assigned
 * to each of their indexOf() values in a {@link Map} object. This can for
 * instance be a Munkres assignment/mapping.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 11, 2007
 */
class SyntaxTreeNodeChildMappingComparator<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, T>>
		implements Auxiliary, Comparator<T>
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
	private static final Log logger =
			LogFactory.getLog(SyntaxTreeNodeChildMappingComparator.class);

	// ========================= FIELDS ====================================

	// Mapping with integer weights assigned to each node in the list
	private final Map<Integer, Integer> mapping;

	/**
	 * base index for mapping (e.g. 0-based, 1-based)
	 */
	private final int baseIndex;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a syntax tree node comparator.
	 * 
	 * @param mapping
	 *            Mapping with integer weights assigned to each node in the list
	 * @param baseIndex
	 *            base index for mapping (e.g. 0-based, 1-based)
	 */
	public SyntaxTreeNodeChildMappingComparator(
			final Map<Integer, Integer> mapping, final int baseIndex)
	{
		super();
		this.mapping = mapping;
		this.baseIndex = baseIndex;
	}

	// ========================= IMPLEMENTATION: Comparator ================

	/**
	 * A comparator of tree nodes based on comparing their math token data by
	 * {@link MathTokenComparatorFull}.
	 * 
	 * @param element1
	 *            left operand to be compared
	 * @param element2
	 *            right operand to be compared
	 * @return result of comparison
	 */
	public int compare(T element1, T element2)
	{
		// Assumes that both elements have a parent
		int key1 = element1.getParent().indexOf(element1) + baseIndex;
		int key2 = element2.getParent().indexOf(element2) + baseIndex;

		// Assumes that the mapping is non-null for both elements (in fact only
		// for element1) but in principle both need to be non-null
		//
		// Note: keys are 0-based, values are baseIndex-based, but value
		// ordering is the same for any base, so don't bother to convert values
		// to 0-based
		logger.debug("key1 " + key1 + " value1 " + mapping.get(key1));
		logger.debug("key2 " + key1 + " value2 " + mapping.get(key2));

		return mapping.get(key1).compareTo(mapping.get(key2));
	}
	// ========================= PRIVATE METHODS ===========================
}
