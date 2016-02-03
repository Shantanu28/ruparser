/*****************************************************************************************
 * Source File: NodeMapping.java
 ****************************************************************************************/
package net.ruready.parser.atpm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.ruready.common.text.TextUtil;
import net.ruready.common.tree.AbstractListTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A nodal mapping between two syntax trees. Implements
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
public class NodeMapping<D extends Serializable & Comparable<? super D>, T extends AbstractListTreeNode<D, ?>>
		implements List<NodeMatch<D, T>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NodeMapping.class);

	// ========================= FIELDS ====================================

	private List<NodeMatch<D, T>> list = new ArrayList<NodeMatch<D, T>>();

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an empty nodal mapping.
	 */
	public NodeMapping()
	{
		super();
	}

	// ========================= IMPLEMENTATION: Object =============

	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return list.hashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		return list.equals(o);
	}

	/**
	 * Print the contents the tree-to-tree mapping.
	 * 
	 * @return A string describing the tree-to-tree mapping.
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		for (NodeMatch<D, T> entry : this)
		{
			s = s.append(entry.toString());
		}
		return s.toString();
	}

	// ========================= METHODS ===================================

	/**
	 * Print the contents the tree-to-tree mapping in detail.
	 * 
	 * @return A string describing the tree-to-tree mapping.
	 */
	public String toStringDetailed()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		for (NodeMatch<D, T> entry : this)
		{
			s = s.append(entry.toStringDetailed());
		}
		return s.toString();
	}

	/**
	 * Append an element at the end nodal mapping.
	 * 
	 * @param leftIndex
	 * @param leftData
	 * @param leftNode
	 * @param rightIndex
	 * @param rightData
	 * @param rightNode
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(final int leftIndex, final D leftData, final T leftNode,
			final int rightIndex, final D rightData, final T rightNode)
	{
		list.add(new NodeMatch<D, T>(leftIndex, leftData, leftNode, rightIndex,
				rightData, rightNode));
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, NodeMatch<D, T> element)
	{
		list.add(index, element);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(NodeMatch<D, T> e)
	{
		return list.add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends NodeMatch<D, T>> c)
	{
		return list.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends NodeMatch<D, T>> c)
	{
		return list.addAll(index, c);
	}

	/**
	 * @see java.util.List#clear()
	 */
	public void clear()
	{
		list.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o)
	{
		return list.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c)
	{
		return list.containsAll(c);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public NodeMatch<D, T> get(int index)
	{
		return list.get(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<NodeMatch<D, T>> iterator()
	{
		return list.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<NodeMatch<D, T>> listIterator()
	{
		return list.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<NodeMatch<D, T>> listIterator(int index)
	{
		return list.listIterator(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public NodeMatch<D, T> remove(int index)
	{
		return list.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o)
	{
		return list.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c)
	{
		return list.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c)
	{
		return list.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public NodeMatch<D, T> set(int index, NodeMatch<D, T> element)
	{
		return list.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size()
	{
		return list.size();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<NodeMatch<D, T>> subList(int fromIndex, int toIndex)
	{
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray()
	{
		return list.toArray();
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.List#toArray(Z[])
	 */
	public <Z> Z[] toArray(Z[] a)
	{
		return list.toArray(a);
	}

	// ========================= GETTERS & SETTERS =========================

}
