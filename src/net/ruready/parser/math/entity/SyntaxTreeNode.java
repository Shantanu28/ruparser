/*****************************************************************************************
 * Source File: SyntaxTreeNode.java
 ****************************************************************************************/
package net.ruready.parser.math.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.exception.SystemException;
import net.ruready.common.exception.UnsupportedOpException;
import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.pointer.GenericCloner;
import net.ruready.common.pointer.ShallowCloneable;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.common.tree.Printer;
import net.ruready.common.tree.TraversalOrder;
import net.ruready.common.tree.TraversalPrinter;
import net.ruready.common.tree.TreeNodeCounter;
import net.ruready.common.tree.TreeNodeTraverser;
import net.ruready.common.util.HashCodeUtil;
import net.ruready.parser.math.entity.value.MathValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A syntax tree of a mathematical expression.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 9, 2007
 */
public final class SyntaxTreeNode implements
		AbstractListTreeNode<MathToken, SyntaxTreeNode>, Comparable<SyntaxTreeNode>,
		TolerantlyComparable<SyntaxTreeNode>
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
	private static final Log logger = LogFactory.getLog(SyntaxTreeNode.class);

	// ========================= FIELDS ====================================

	// Data held at this tree node
	protected MathToken data;

	// The parent node
	protected SyntaxTreeNode parent;

	protected List<SyntaxTreeNode> children;

	// Comparator according to which children are sorted
	protected Comparator<? super SyntaxTreeNode> comparator;

	protected TraversalPrinter<MathToken, SyntaxTreeNode> printer;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Prevent no-argument construction.
	 */
	protected SyntaxTreeNode()
	{

	}

	/**
	 * Construct a syntax tree node.
	 * 
	 * @param data
	 *            root node data
	 */
	public SyntaxTreeNode(MathToken data)
	{
		initialize();
		this.data = data;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Compare two syntax trees based on their test data level.
	 * 
	 * @param other
	 *            Other syntax tree to be compared with this one.
	 */
	@Override
	public boolean equals(Object obj)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		if (this == obj)
		{
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}
		// Cast to friendlier version
		SyntaxTreeNode other = (SyntaxTreeNode) obj;

		return (this.compareTo(other) == 0);
	}

	/**
	 * Must be overridden when <code>equals()</code> is.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int result = HashCodeUtil.SEED;

		// Mandatory fields used in equals()
		result = HashCodeUtil.hash(result, data);
		for (SyntaxTreeNode child : getChildren())
		{
			result = HashCodeUtil.hash(result, child);
		}

		// Optional fields that are likely to be different for different
		// instances

		return result;
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * A shallow copy of this object, as opposed to {@link #clone()}, which is a deep
	 * copy.
	 * 
	 * @return a shallow copy of the receiving object
	 */
	@SuppressWarnings("unchecked")
	public SyntaxTreeNode shallowClone()
	{
		SyntaxTreeNode copy = new SyntaxTreeNode((MathToken) GenericCloner.clone(data));

		// Must clone printer otherwise printouts are weird. That's because
		// the printer has a reference to the tree. Assumes that the original
		// printer is of traversal printer type
		copy.setPrinter(new TraversalPrinter<MathToken, SyntaxTreeNode>(copy, printer));

		// Just copy references to these TreeNode fields
		copy.setComparator(this.getComparator());

		return copy;
	}

	/**
	 * @param destination
	 * @see net.ruready.common.pointer.ShallowCloneable#mergeInto(net.ruready.common.pointer.ShallowCloneable)
	 */
	public void mergeInto(ShallowCloneable destination)
	{
		throw new UnsupportedOpException("mergeInto(): not implemented yet");
	}

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target (or part of a target) of an assembly. Must be overridden for correct
	 * children tree type generation during cloning.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public SyntaxTreeNode clone()
	{
		// try {
		// Copy root node data
		SyntaxTreeNode copy = this.shallowClone();
		
		// Copy child trees (recursive calls)
		for (SyntaxTreeNode child : this.getChildren())
		{
			copy.addChild(child.clone());
		}

		// logger.debug("Inspecting this (to clone)");
		// new TreeReferenceInspector().executeOnTree(this);
		// logger.debug("Inspecting cloned copy");
		// new TreeReferenceInspector().executeOnTree(copy);

		// logger.debug("cloned() " + copy);
		return copy;
		// }
		/*
		 * catch (CloneNotSupportedException e) { // this shouldn't happen, because we are
		 * Cloneable throw new InternalError("clone() failed: " + e.getMessage()); }
		 */
	}

	// ========================= IMPLEMENTATION: Comparable ================

	/**
	 * Compare two syntax trees. This is a lexicographic ordering (first compare root data
	 * nodes, then compare children branches (recursively), and finally compare the number
	 * of children.
	 * 
	 * @param other
	 *            Other tree to be compared with this one.
	 */
	public int compareTo(SyntaxTreeNode other)
	{
		// null < any tree
		if (other == null)
		{
			return 1;
		}
		// Compare root data; must be a symmetric relation; either data may be null,
		// that has nothing to do with the general contract of Comparable in regard to
		// compareTo(null), because we're comparing tree nodes here, not node data.
		int compareData = (data == null) ? ((other.data == null) ? 0 : -1)
				: ((other.data == null) ? 1 : data.compareTo(other.data));
		// logger.debug("compareTo(): data " + data + " other.data " +
		// other.data
		// + " compareData " + compareData);
		if (compareData != 0)
		{
			return compareData;
		}

		// Compare children
		int thisSize = getNumChildren();
		int otherSize = other.getNumChildren();
		for (int i = 0; i < Math.min(thisSize, otherSize); i++)
		{
			int compareChild = children.get(i).compareTo(other.getChild(i));
			// logger.debug("compareTo(): i " + i + " child " + children.get(i) + "
			// otherChild " + other.getChild(i) + " compareChild " +
			// compareChild);
			if (compareChild != 0)
			{
				return compareChild;
			}

		}

		// Compare #children
		// logger.debug("compareTo(): comparing #children: this " + thisSize + "
		// other " + otherSize + " compare " + new
		// Integer(thisSize).compareTo(otherSize));
		return new Integer(thisSize).compareTo(otherSize);
	}

	// ========================= IMPLEMENTATION: TolerantlyComparable ========

	/**
	 * Result of equality of two syntax trees up to a finite tolerance. They are equal if
	 * and only if the rules of {@link #equals(Object)} method hold, with node data
	 * tolerant equality. This assumes that all nodes are
	 * 
	 * @param other
	 *            The other <code>ComplexValue</code> object.
	 * @param tol
	 *            tolerance of equality, if we compare to a finite precision (for n digits
	 *            of accuracy, use tol = 10^{-n}).
	 * @return the result of tolerant equality of two evaluable quantities. Returns
	 *         {@link #EQUAL} if they are tolerantly equal; returns {@link #INDETERMINATE}
	 *         if tolerant equality cannot be returned; otherwise, returns a number that
	 *         is different from both constants, e.g., {@link #NON_EQUAL}.
	 */
	final public int tolerantlyEquals(SyntaxTreeNode other, double tol)
	{
		// null != any tree
		if (other == null)
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Compare root data by tolerant equality.
		// Must be a symmetric relation; either data may be null,
		// that has nothing to do with the general contract of Comparable in regard to
		// compareTo(null), because we're comparing tree nodes here, not node data.
		int compareData = (data == null) ? ((other.data == null) ? 0 : -1)
				: ((other.data == null) ? 1 : data.tolerantlyEquals(other.data, tol));
		// logger.debug("compareTo(): data " + data + " other.data " +
		// other.data
		// + " compareData " + compareData);
		if (compareData != 0)
		{
			return compareData;
		}

		// Compare children
		int thisSize = getNumChildren();
		int otherSize = other.getNumChildren();
		for (int i = 0; i < Math.min(thisSize, otherSize); i++)
		{
			int compareChild = children.get(i).tolerantlyEquals(other.getChild(i), tol);
			// logger.debug("compareTo(): i " + i + " child " + children.get(i) + "
			// otherChild " + other.getChild(i) + " compareChild " +
			// compareChild);
			if (compareChild != 0)
			{
				return compareChild;
			}

		}

		// Compare #children
		return (thisSize == otherSize) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= METHODS ===================================

	/**
	 * Return the list of math tokens in the syntax tree, sorted by their order of
	 * apperance in the original assembly that this tree corresponds to.
	 * 
	 * @return list of math tokens in their order of apperance in the original assembly
	 *         that this tree corresponds to
	 */
	public List<MathToken> toMathTokenArray()
	{
		return SyntaxDataTraverser.traverse(this);
	}

	/**
	 * Return the status of the {@link MathToken} data stored at this node.
	 * 
	 * @return the status of the {@link MathToken} data stored at this node.
	 */
	public MathTokenStatus getStatus()
	{
		return data.getStatus();
	}

	/**
	 * Return the value of the {@link MathToken} data stored at this node.
	 * 
	 * @return the status of the {@link MathToken} data stored at this node.
	 */
	public MathValue getValue()
	{
		return data.getValue();
	}

	/**
	 * Set the value of the {@link MathToken} data stored at this node.
	 * 
	 * @param value
	 *            new value for this node's data
	 */
	public void setValue(MathValue value)
	{
		data.value = value;
	}

	/**
	 * Return the value ID of the {@link MathToken} data stored at this node.
	 * 
	 * @return the status of the {@link MathToken} data stored at this node.
	 */
	public MathValueID getValueID()
	{
		return data.getValueID();
	}

	/**
	 * @return the traversalIndex
	 */
	public int getTraversalIndex()
	{
		return data.getTraversalIndex();
	}

	/**
	 * Return the data of a child node.
	 * 
	 * @param index
	 *            index of child in the children list
	 * @return the data of a child node.
	 */
	public MathToken getChildData(int index)
	{
		SyntaxTreeNode child = this.getChildren().get(index);
		return child.data;
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Perform a arithmetic unary operation on a syntax tree: Add a root node to the tree
	 * with data "op", and set its left child to the original syntax tree.
	 * 
	 * @param opToken
	 *            unary arithmetic operation math token.
	 * @param syntaxTreeNode
	 *            tree node to put under the operation
	 * @return a new expression value representing op(syntaxTreeNode).
	 */
	public static SyntaxTreeNode op(MathToken opToken, SyntaxTreeNode syntaxTreeNode)
	{
		// Do not clone; just copy pointers
		SyntaxTreeNode result = new SyntaxTreeNode(opToken);
		result.addChild(syntaxTreeNode);
		// logger.debug("Unary op result " + result);
		// result.checkReferences();
		return result;
	}

	/**
	 * Perform a parenthesis operation on the expression variables. Add a root node to the
	 * tree with data "op", and set its left child to the original syntax
	 * 
	 * @param opToken
	 *            parenthesis operation math token.
	 * @param syntaxTreeNode
	 *            tree node to put under the operation
	 * @return a new expression value representing ( old_expression )
	 */
	@Deprecated
	public static SyntaxTreeNode parenthesis(MathToken ta, SyntaxTreeNode syntaxTreeNode)
	{
		// Do not clone; just copy pointers
		// This seems identical to op()
		// TODO: depracate this method?
		return SyntaxTreeNode.op(ta, syntaxTreeNode);
		/*
		 * // Add a root node to the tree with data // "op", its left child = previous
		 * tree. SyntaxTreeNode oldSyntax = (SyntaxTreeNode) this.clone();
		 * this.removeAll(); this.setData(ta); this.add(oldSyntax); //
		 * logger.debug("oldSyntax="+oldSyntax); // logger.debug("syntax ="+syntax);
		 */
	}

	/**
	 * Perform an arithmetic binary operation/function of two expressions. This means to
	 * operate (+,-,*,/,^) on each value in their stacks. We assume both expression value
	 * lists they are the same size.
	 * 
	 * @param opToken
	 *            binary arithmetic operation math token.
	 * @param e1
	 *            left argument of the operation.
	 * @param e2
	 *            right argument of the operation.
	 * @return a new expression value representing e1 op e2 (or op(e1,e2)). Nodal options
	 *         are inherited from e1.
	 */
	public static SyntaxTreeNode op(MathToken opToken, SyntaxTreeNode e1,
			SyntaxTreeNode e2)
	{
		// Construct a tree by making a root node with
		// "op", its left child = e1's tree, and its
		// right child = e2's tree.
		// Do not clone; just copy pointers
		SyntaxTreeNode e = new SyntaxTreeNode(opToken);
		e.addChild(e1);
		e.addChild(e2);
		// logger.debug("Binary op operand e1 " + e1);
		// logger.debug("Binary op operand e2 " + e2);
		// logger.debug("Binary op result e " + e);
		// e.checkReferences();
		return e;
	}

	/**
	 * Perform an arithmetic multi-nary operation/function of two expressions. This
	 * usually refers to a multi-nary +\- or *\/. The returned tree corresponds to the
	 * expression opToken(unaryOpTokens[0](terms[0]), ...,
	 * unaryOpTokens[n-1](terms[n-1])). It is assumed that unaryOps.size = terms.size = n.
	 * 
	 * @param multinaryOpToken
	 *            token of multi-nary arithmetic operation math token (+ or *)
	 * @param unaryOpTokens
	 *            tokens of preceding unary operations for each terms (+\- or *\/)
	 * @param terms
	 *            unary operation terms
	 * @return a new expression value representing opToken(unaryOpTokens[0](terms[0]),
	 *         ..., unaryOpTokens[n-1](terms[n-1])), where unaryOps.size = terms.size = n
	 */
	public static SyntaxTreeNode op(MathToken multinaryOpToken,
			List<MathToken> unaryOpTokens, List<SyntaxTreeNode> terms)
	{
		if (unaryOpTokens.size() != terms.size())
		{
			throw new ApplicationException(
					"Multinary op tree builder: # unary ops != # terms");
		}
		SyntaxTreeNode e = new SyntaxTreeNode(multinaryOpToken);
		boolean first = true;
		for (int i = 0; i < terms.size(); i++)
		{
			MathToken unaryOpToken = unaryOpTokens.get(i);
			SyntaxTreeNode term = terms.get(i);
			// If this is the first term and it has a sign op as its root node,
			// don't append another sign; otherwise, do.
			if (first)
			{
				if (term.data.isSignOp())
				{
					e.addChild(term);
				}
				else
				{
					SyntaxTreeNode temp = new SyntaxTreeNode(unaryOpToken);
					temp.addChild(term);
					e.addChild(temp);
				}
				first = false;
			}
			else
			{
				SyntaxTreeNode temp = new SyntaxTreeNode(unaryOpToken);
				temp.addChild(term);
				e.addChild(temp);
			}
		}
		// logger.debug("Multinary op result e " + e);
		// e.checkReferences();
		return e;
	}

	// ========================= IMPLEMENTATION: AbstractListTreeNode ==========

	/**
	 * @see net.ruready.common.tree.LinkedListTreeNode#printData()
	 */
	public String printData()
	{
		return CommonNames.MISC.EMPTY_STRING + data;
	}

	/**
	 * @return parent node of this node
	 */
	public SyntaxTreeNode getParent()
	{
		return parent;
	}

	/**
	 * Returns the [grand-...-grand-] parent node of this node.
	 * 
	 * @param height
	 *            number of tree levels to go up. {@link AbstractListTreeNode#getParent()}
	 *            means <code>height = 1</code> (the direct parent), and so on. A
	 *            non-positive <code>height</code> will return this node.
	 * @return [grand-...-grand-] parent node of this node
	 */
	public SyntaxTreeNode getSuperParent(final int height)
	{
		SyntaxTreeNode node = this;
		for (int i = 1; i <= height; i++)
		{
			if (node == null)
			{
				return null;
			}
			node = node.parent;
		}
		return node;
	}

	/**
	 * Return the list of siblings of this node. This will return <code>null</code> if
	 * the this node's parent field is <code>null</code>.
	 * 
	 * @return list of siblings of this node
	 * @see net.ruready.common.tree.AbstractListTreeNode#getSiblings()
	 */
	public List<SyntaxTreeNode> getSiblings()
	{
		return (parent == null) ? null : parent.getChildren();
	}

	/**
	 * @return the printer
	 */
	public TraversalPrinter<MathToken, SyntaxTreeNode> getPrinter()
	{
		return printer;
	}

	/**
	 * @param printer
	 *            the printer to set
	 */
	public void setPrinter(TraversalPrinter<MathToken, SyntaxTreeNode> printer)
	{
		this.printer = printer;
	}

	/**
	 * @param printer
	 *            the printer to set
	 */
	public void setPrinter(Printer<SyntaxTreeNode> printer)
	{
		throw new SystemException(
				"This tree type supports only TraversalPrinters");
	}

	/**
	 * @return the children
	 */
	public List<SyntaxTreeNode> getChildren()
	{
		return children;
	}

	/**
	 * Add a child to the children list.
	 * 
	 * @param child
	 *            The child to be added.
	 */
	public void addChild(SyntaxTreeNode child)
	{
		// logger
		// .debug("add(this @" + Integer.toHexString(this.hashCode()) + " " + this
		// + ", child @" + Integer.toHexString(child.hashCode()) + " "
		// + child + ")");
		// if (child.parent != null)
		// {
		// throw new SystemException(
		// "Cannot add child; remove it first from child's current parent" + " "
		// + child.parent);
		// }
		child.setParent(this);
		children.add(child);
		refresh();
		// logger.debug("Updated this " + this);
		// checkReferences();
		// logger.debug("After adding, this " + this);
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#addChilds(int,
	 *      net.ruready.common.tree.AbstractListTreeNode)
	 */
	public void addChild(int index, SyntaxTreeNode child)
	{
		// May be slow: we are not using a linked list
		// logger.debug("add(index = " + index + " child = " + child + ")");
		// if (child.parent != null)
		// {
		// throw new SystemException(
		// "Cannot add child; remove it first from child's current parent");
		// }
		child.setParent(this);
		children.add(index, child);
		refresh();
		// checkReferences();
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#getChild(int)
	 */
	public SyntaxTreeNode getChild(int index)
	{
		return children.get(index);
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#indexOf(net.ruready.common.tree.AbstractListTreeNode)
	 */
	public int indexOf(SyntaxTreeNode child)
	{
		return children.indexOf(child);
	}

	/**
	 * @param childId
	 * @return
	 * @see net.ruready.common.tree.AbstractListTreeNode#indexOf(java.lang.Long)
	 */
	public int indexOf(Long childId)
	{
		throw new SystemException(
				"Cannot be implemented until this tree node type has a unique identifier of type Long");
		// int i = 0;
		// for (SyntaxTreeNode<D> child : children)
		// {
		// if (childId.equals(child.getId()))
		// {
		// return i;
		// }
		// i++;
		// }
		// return CommonNames.MISC.INVALID_VALUE_INTEGER;
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#getData()
	 */
	public MathToken getData()
	{
		return data;
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#setChildAt(int, java.lang.Object)
	 */
	public void setChildAt(int index, SyntaxTreeNode child)
	{
		// logger.debug("setChildAt(index = " + index + " child = " + child + ")");
		// if (child.parent != null)
		// {
		// throw new SystemException(
		// "Cannot add child; remove it first from child's current parent");
		// }
		child.setParent(this);
		children.set(index, child);
		// checkReferences();
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#replaceChild(net.ruready.common.tree.AbstractListTreeNode,
	 *      net.ruready.common.tree.AbstractListTreeNode)
	 */
	public void replaceChild(SyntaxTreeNode oldChild, SyntaxTreeNode newChild)
	{
		// logger.debug("replaceChild(oldChild = " + oldChild + " newChild = " + newChild
		// + ")");
		int index = children.indexOf(oldChild);
		if (index >= 0)
		{
			// if (newChild.parent != null)
			// {
			// throw new SystemException(
			// "Cannot add child; remove it first from child's current parent");
			// }
			// oldChild.setParent(null);
			oldChild.parent = null;
			newChild.setParent(this);
			children.set(index, newChild);
		}
		// checkReferences();
	}

	/**
	 * Remove a child node under this node; all grandchildren (children of this child) are
	 * added under this node, at position <code>indexOf(child)</code>.
	 * 
	 * @param child
	 *            the child to remove
	 * @see net.ruready.common.tree.AbstractListTreeNode#removeChildNode(net.ruready.common.tree.AbstractListTreeNode)
	 */
	public void removeChildTree(SyntaxTreeNode child)
	{
		// logger.debug("removeChildNode(child = " + child + ")");
		int index = children.indexOf(child);
		if (index >= 0)
		{
			// Get a reference to grandchildren
			List<SyntaxTreeNode> grandChildren = child.getChildren();
			// Remove the child
			children.remove(index);
			// Set the grandchildren's new parent
			for (SyntaxTreeNode grandChild : grandChildren)
			{
				// if (grandChild.parent != null)
				// {
				// throw new SystemException(
				// "Cannot add child; remove it first from child's current parent");
				// }
				grandChild.setParent(this);
			}
			// Add grandchildren in place of the child
			children.addAll(index, grandChildren);
		}
		// checkReferences();
	}

	/**
	 * @see net.ruready.common.tree.AbstractListTreeNode#setData(java.lang.Comparable)
	 */
	public void setData(MathToken data)
	{
		this.data = data;
	}

	/**
	 * Set the parent of this node to <code>null</code>.
	 * <p>
	 * WARNING: use this method only if you know that there is indeed no parent.
	 * Otherwise, a parent still might have a child reference to this node whereas this
	 * node doesn't point to it any longer.
	 */
	public void removeParentReference()
	{
		parent = null;
	}

	/**
	 * Remove a child from the children list.
	 * 
	 * @param child
	 *            The child to be removed.
	 */
	public void removeChild(SyntaxTreeNode child)
	{
		logger
				.debug("remove(this @" + Integer.toHexString(this.hashCode()) + " "
						+ this + ", child @" + Integer.toHexString(child.hashCode())
						+ " " + child + ")");

		// child.setParent(null);
		child.parent = null;
		children.remove(child);
		refresh();
		// checkReferences();
	}

	/**
	 * Add all children in a list to this tree. This operation is delegated to the
	 * specific implementation of <code>children</code>.
	 * 
	 * @param newChildren
	 *            collection to be added
	 */
	public void addChilds(Collection<SyntaxTreeNode> newChildren)
	{
		for (SyntaxTreeNode child : newChildren)
		{
			// if (child.parent != null)
			// {
			// throw new SystemException(
			// "Cannot add child; remove it first from child's current parent");
			// }
			child.setParent(this);
		}
		children.addAll(newChildren);
		refresh();
		// checkReferences();
	}

	/**
	 * Remove all children from this tree. This operation is delegated to the specific
	 * implementation of <code>children</code>.
	 */
	public void removeAllChilds()
	{
		for (SyntaxTreeNode child : getChildren())
		{
			// child.setParent(null);
			child.parent = null;
		}
		children = new ArrayList<SyntaxTreeNode>();
		// checkReferences();
	}

	/**
	 * Returns the size of the tree, which is the total number of nodes in the tree.
	 * 
	 * @return the total number of nodes in the tree
	 */
	public int getSize()
	{
		return TreeNodeCounter.countNodes(this);
	}

	/**
	 * Returns the number of children (branches of this tree node).
	 * 
	 * @return Returns the number of children.
	 */
	public int getNumChildren()
	{
		return children.size();
	}

	/**
	 * Returns <code>true</code> if and only if the number of children is positive.
	 * 
	 * @return <code>true</code> if and only if this node has children
	 */
	public boolean hasChildren()
	{
		return children.size() > 0;
	}

	/**
	 * Remove this node from its parent's cghildren list. This should be done before
	 * deleting a node from the database.
	 * 
	 * @param comparator
	 *            the comparator to set
	 */
	public void removeFromParent()
	{
		if (parent != null)
		{
			parent.removeChild(this);
		}
	}

	/**
	 * Return the list of nodes in the syntax tree.
	 * 
	 * @param traversalOrder
	 *            node traversal order (e.g. pre/post)
	 * @return list of tree nodes
	 */
	public List<SyntaxTreeNode> toNodeList(final TraversalOrder traversalOrder)
	{
		return new TreeNodeTraverser<MathToken, SyntaxTreeNode>(traversalOrder)
				.traverse(this);
	}

	// ========================= METHODS ===================================

	/**
	 * Perform initializations: initialize the data structure holding the collection of
	 * children, set a default printer, etc.
	 */
	private void initialize()
	{
		// Initialize children
		children = new ArrayList<SyntaxTreeNode>();

		// Set printer options
		setPrinter(new TraversalPrinter<MathToken, SyntaxTreeNode>(this,
				CommonNames.TREE.BRACKET_OPEN, CommonNames.TREE.BRACKET_CLOSE,
				CommonNames.MISC.EMPTY_STRING, " ", true, false, true));

		// Default children ordering
		// setComparator(new TreeNodeComparator());
	}

	/**
	 * Refresh this tree node. This re-sorts children.
	 */
	public void refresh()
	{
		if ((comparator != null) && (children != null))
		{
			// Sort children
			Collections.sort(children, comparator);
		}
	}

	// ========================= PRINTOUT METHODS ==========================

	/**
	 * Print a tree in pre-traversal order.
	 * 
	 * @return a string with this tree in pre-traversal order.
	 */
	@Override
	public String toString()
	{
		return printer.toString();
	}

	// ========================= GETTERS & SETTERS ==========================

	/**
	 * Set the parent node of this node. The depth of this node is set to the parent's
	 * depth plus one. Depth updates are not cascaded to the children of this node; use
	 * refreshAll() to achieve that.<br>
	 * To keep the integrity of parent-child bi-directional references, child cannot stay
	 * under both the old parent (this.parent) and new parent (parent). Clone it and put
	 * the clone under the old parent, and move it under the new parent.
	 * 
	 * @param parent
	 *            parent node of this node to set
	 */
	private void setParent(SyntaxTreeNode parent)
	{
		// if (parent == null)
		// {
		// logger.debug("setParent(this @" + Integer.toHexString(this.hashCode()) + " "
		// + this + ", parent null )");
		// }
		// else
		// {
		// logger.debug("setParent(this @" + Integer.toHexString(this.hashCode()) + " "
		// + this + ", parent @" + Integer.toHexString(parent.hashCode()) + " "
		// + parent + ")");
		// }
		// if (this.parent != null)
		if ((this.parent != null) && (this.parent != parent))
		{
			// throw new SystemException("Cannot add child " + this
			// + " ; remove it first from child's current parent" + " "
			// + this.parent);

			// logger
			// .warn("Cannot add child " + this + " under new parent " + parent
			// + "; remove it first from child's current parent" + " "
			// + this.parent);
			// this.parent.children.remove(this);
			// this.parent.refresh();

			// To keep the integrity of parent-child bi-directional references,
			// child cannot stay under both the old parent (this.parent) and new parent
			// (parent). Clone it and put the clone under the old parent, and move it
			// under the new parent.
			SyntaxTreeNode copy = this.clone();
			int index = this.parent.indexOf(this);
			copy.parent = this.parent;
			// copy.setData(data);
			this.parent.children.set(index, copy);

			// this.removeFromParent();
		}
		this.parent = parent;
	}

	/**
	 * @return the comparator
	 */
	public Comparator<? super SyntaxTreeNode> getComparator()
	{
		return comparator;
	}

	/**
	 * Set a new comparator to define a new children ordering and refresh the node.
	 * Setting the comparator to <code>null</code> will keep the current children
	 * ordering.
	 * 
	 * @param comparator
	 *            the comparator to set
	 */
	public void setComparator(Comparator<? super SyntaxTreeNode> comparator)
	{
		setComparator(comparator, true);
	}

	/**
	 * Set a new comparator to define a new children ordering and refresh the node.
	 * Setting the comparator to <code>null</code> will keep the current children
	 * ordering.
	 * 
	 * @param comparator
	 *            the comparator to set
	 * @param doRefresh
	 *            refresh tree if and only if this flag is true
	 */
	private void setComparator(Comparator<? super SyntaxTreeNode> comparator,
			boolean doRefresh)
	{
		this.comparator = comparator;
		if (doRefresh)
		{
			refresh();
		}
	}

	public void checkReferences()
	{
		String thisHashCode = Integer.toHexString(hashCode());
		logger.debug("Checking references, this @" + thisHashCode);
		for (int i = 0; i < getNumChildren(); i++)
		{
			SyntaxTreeNode child = getChild(i);
			String parentHashCode = (child.getParent() == null) ? null : Integer
					.toHexString(child.getParent().hashCode());
			logger.debug("child[" + i + "] @" + Integer.toHexString(child.hashCode())
					+ " parent @" + parentHashCode
					+ ((child.getParent() == this) ? "     " : " !!! ") + " child "
					+ child + " parent " + child.getParent());
			if ((child.getParent() == this)
					&& (hashCode() != child.getParent().hashCode()))
			{
				throw new IllegalStateException(
						"equals/hashCode contract violation !!!!!!!!!!!!!!!!");
			}
			if (child.getParent() != this)
			{
				throw new IllegalStateException("child.parent != this !!!!!!!!!!!!!!!!");
			}
		}
		logger.debug("");
	}
}
