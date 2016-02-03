/*****************************************************************************************
 * Source File: OperationPrecedenceUtil.java
 ****************************************************************************************/
package net.ruready.parser.absolute.entity;

import net.ruready.common.exception.SystemException;
import net.ruready.parser.arithmetic.entity.mathvalue.AssociationType;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of mathematical values and operations (based on their precedence).
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
 * @version Oct 16, 2007
 */
public class OperationPrecedenceUtil
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
	private static final Log logger = LogFactory.getLog(OperationPrecedenceUtil.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Hide constructor in a library.
	 */
	private OperationPrecedenceUtil()
	{

	}

	// ========================= PUBLIC METHODS ============================

	/**
	 * Is a parentheses pair around a child operation of a parent operation redundant or
	 * not.
	 * 
	 * @param parent
	 *            parent operation
	 * @param child
	 *            child operation
	 * @param childIndex
	 *            index of child in the parent's children list
	 * @return is parentheses around child redundant or not in this case
	 */
	public static boolean isParenAroundChildRedundant(final MathValue parent,
			final MathValue child, final int childIndex)
	{
		// logger.debug("isParenAroundChildRedundant()");
		// logger.debug("parent = " + parent.getIdentifier() + Names.TREE.SEPARATOR +
		// parent);
		// logger.debug("child = " + child.getIdentifier() + Names.TREE.SEPARATOR +
		// child);

		int comparePrecedence = OperationPrecedenceUtil.compare(child, parent);

		if ((comparePrecedence == 0)
				&& (parent.getIdentifier() == MathValueID.ARITHMETIC_BINARY_OPERATION)
				&& (child.getIdentifier() == MathValueID.ARITHMETIC_BINARY_OPERATION))
		{
			// ---------------------------------------------------------------
			// Case 1: child and parent have equal precedence values
			// and are both binary operations
			// ---------------------------------------------------------------
			BinaryOperation parentBinaryOp = (BinaryOperation) parent;
			BinaryOperation childBinaryOp = (BinaryOperation) child;

			if (childBinaryOp == parentBinaryOp)
			{
				// ---------------------------------------------------------------
				// Case 1.1: parent = child
				// ---------------------------------------------------------------
				// If the child's index != index of child for which the
				// operation's precedence type requires parentheses, parentheses
				// are redundant:
				// Case 1.1.1: Associative operation: any index implies
				// redundant parentheses.
				// Case 1.1.2: Op. associates to the left and (a o b) o c,
				// i.e. (childIndex != BinaryValue.RIGHT).
				// Case 1.1.3: Op. associates to the left and a o (b o c),
				// i.e. (childIndex != BinaryValue.LEFT).
				return childIndex != parentBinaryOp
						.getRequiredParenthesesAroundChildIndex();
			}

			// ---------------------------------------------------------------
			// Case 1.2: parent != child
			// ---------------------------------------------------------------
			// Case 1.2.1.: parent association type != child association
			// type and neither is associative. This can't happen in
			// normal arithmetic rules, so throwan exception. After all,
			// what's the definition of a o b x c
			// where o and x are both binary and at the same precedence
			// level? If they don't have the same association type, their
			// association depends on other operations, not only on
			// themselves. Bummer.
			AssociationType parentAssociation = parentBinaryOp.getAssociationType();
			AssociationType childAssociation = childBinaryOp.getAssociationType();
			if ((parentAssociation != childAssociation)
					&& (!parentBinaryOp.isAssociative())
					&& (!childBinaryOp.isAssociative()))
			{
				throw new SystemException(
						"Arithmetic rules require different operations of the same precedence level to have the same association type!");
			}

			// At this point, we assume that both operations have equal
			// association types. If the operation is associative, it is in
			// particular left- or right- associative so use the other
			// operation's association type to determine what is the joint
			// association type. If both are associative, throw an
			// exception. This can't be in normal arithmetic rule systems.
			if (parentBinaryOp.isAssociative() && (childBinaryOp.isAssociative()))
			{
				throw new SystemException(
						"Arithmetic rules require different operations of the same precedence level to not both be associative!");
			}
			if (parentBinaryOp.isAssociative())
			{
				parentAssociation = childAssociation;
			}
			else if (childBinaryOp.isAssociative())
			{
				childAssociation = parentAssociation;
			}

			// Case 1.2.2: both parent, child have the same association
			// and are not associative (if they are they've been converted
			// to either left- or right- association type).
			// Let parent = o and child = x.
			//
			// --- If left associated: ---
			// * If parent is commutative, then a o (b x c) = (b x c) o a
			// ==>
			// redundant.
			// * If parent is not commutative, then a o (b x c) ==> not
			// redundant.
			// * Always, then (a o b) x c ==> redundant.
			//
			// --- If right associated: ---
			// if parent is commutative, then (a o b) x c = c x (a o b) ==>
			// redundant.
			// if parent is not commutative, then (a o b) x c ==> not
			// redundant.
			// * Always, then a o (b x c) ==> redundant.
			//
			// In both association cases, parentheses redundant if and only
			// if Case 1.1's condition happens OR if the parent is
			// commutative.

			// Reference parentAssociation directly here, not
			// parentBinaryOp.getAssociationType(), because we've updated
			// parentAssociation in Case 1.1 to hold the desired association
			// type for Case 1.2.
			if (parentAssociation.isAssociative())
			{
				// parent+child can't be associative at this point
				throw new IllegalStateException(
						"In cases 1.2.2, the association type of both the parent and child must not be an association operation type");
			}
			return (childIndex != parentAssociation
					.getRequiredParenthesesAroundChildIndex())
					|| (parentBinaryOp.isCommutative());
		}

		// ---------------------------------------------------------------------
		// Case 2: child and parent have different precedence levels
		// and are not both binary operations
		// 
		// Case 2.1: Child precedes parent (comparePrecedence >= 0).
		// E.g. +(+2) or 2+(3*4) ==> redundant parentheses
		//
		// Case 2.2: Parent precedes child (comparePrecedence < 0).
		// E.g. 2*(3+4) ==> not redundant parentheses
		// ---------------------------------------------------------------------
		boolean redundant = (comparePrecedence >= 0);
		// logger.debug("Case 2, comparePrecedence " + comparePrecedence + "
		// ==> "+ (redundant ? "redundant" : "not redundant"));
		return redundant;

	}

	/**
	 * Check if a two tokens are binary operations of equal precedence values.
	 * 
	 * @param mathToken1
	 *            first math token
	 * @param mathToken2
	 *            second math token
	 * @return are both tokens binary operations of equal precedence values
	 */
	public static boolean isBothBinaryAndEqualPrecedence(final MathToken mathToken1,
			final MathToken mathToken2)
	{
		// logger.debug("isBothBinaryAndEqualPrecedence()");
		// logger.debug("mathToken1 = " + mathToken1.getValueID() +
		// Names.TREE.SEPARATOR +
		// mathToken1.getValueID());
		// logger.debug("mathToken2 = " + mathToken2.getValueID() +
		// Names.TREE.SEPARATOR +
		// mathToken2.getValueID());
		return ((mathToken1.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION)
				&& (mathToken2.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION) && (OperationPrecedenceUtil
				.compare(mathToken1.getValue(), mathToken2.getValue()) == 0));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Does one operation precede another or not. The two operations are compared based on
	 * their {@link #precendenceLevel(MathValue)} result. This does not take into account
	 * operation associative properties.
	 * 
	 * @param element1
	 *            left operand to be compared
	 * @param element2
	 *            right operand to be compared
	 * @return result of comparison (<code>-1, 0, 1) based on precedence level
	 */
	private static int compare(final MathValue element1, final MathValue element2)
	{
		// Compare based on operator precedence levels
		int element1Level = OperationPrecedenceUtil.precendenceLevel(element1);
		int element2Level = OperationPrecedenceUtil.precendenceLevel(element2);
		// logger.trace("element1Level " + element1Level + " element2Level "
		// + OperationPrecedenceUtil.precendenceLevel(element2));
		return new Integer(element1Level).compareTo(element2Level);
	}

	/**
	 * Return a code for precedence level of a mathematical value, operation or function.
	 * <p>
	 * <code>
	 * 1 || or(logical)
	 * <br>
	 * 2 &amp;&amp; and(logical)
	 * <br>
	 * 3 == != equal, not equal
	 * <br>
	 * 4 &lt; &gt; &lt;= &gt;= less, greater, less or equal, greater or equal
	 * <br>
	 * 5 + - addition, subtraction
	 * <br>
	 * 6 * / % multiplication, division, modulus
	 * <br>
	 * 7 ! + - not, unary +/-
	 * <br>
	 * 8 &circ; (power)
	 * <br>
	 * 9 func(.), func(.,.) functions of one or two expressions
	 * <br>
	 * 10 ( ) parentheses
	 * <br>
	 * 11 variables, constants, numerical values
	 * </code>
	 * <p>
	 * Note: Logical operations (||,&&,==,!=,<,>,<=,>=,!) are not yet implemented.
	 * 
	 * @return arithmetic operation/value precedence level
	 */
	private static int precendenceLevel(final MathValue value)
	{
		// logger.trace("Computing precedence level: mathToken" + " type " +
		// mathToken.getIdentifier()
		// + " numerical? " + value.isNumerical());

		// Check if this is a numerical value (instead of adding more cases
		// the switch statement that follows; this seems simpler)
		if (value.isNumerical())
		{
			// precedes everything
			return 11;
		}

		switch (value.getIdentifier())
		{
			case LOGICAL_RESPONSE:
			case LOGICAL_EMPTY:
			case LOGICAL_RELATION_OPERATION:
			{
				// Preceded by everything
				return -1;
			} // case ARITHMETIC_PARENTHESIS

			case ARITHMETIC_VARIABLE:
			{
				// Precedes everything
				return 11;
			} // case ARITHMETIC_PARENTHESIS

			case ARITHMETIC_PARENTHESIS:
			{
				// Precedes all other operations
				return 10;
			} // case ARITHMETIC_PARENTHESIS

			case ARITHMETIC_UNARY_OPERATION:
			{
				switch ((UnaryOperation) value)
				{
					case PLUS:
					case MINUS:
					{
						return 7;
					}

					default:
					{
						// Unary functions like abs(.)
						return 9;
					}
				} // switch ((UnaryOp) value)
			} // case ARITHMETIC_UNARY_OPERATION

			case ARITHMETIC_BINARY_OPERATION:
			{
				// Binary operation: +,-,*,/,^,%
				switch ((BinaryOperation) value)
				{
					case POWER:
					{
						return 8;
					}

					case TIMES:
					case DIVIDE:
					case MOD:
					{
						return 6;
					}

					case PLUS:
					case MINUS:
					{
						return 5;
					}

					default:
					{
						throw new SystemException(
								"Unknown binary operation " + value);
					}

				} // switch ((BinaryOp) value)
			} // case ARITHMETIC_BINARY_OPERATION

			case ARITHMETIC_BINARY_FUNCTION:
			{
				// binary functions like log(.,.)
				return 9;
			} // case ARITHMETIC_BINARY_FUNCTION

			default:
			{
				throw new SystemException(
						"Precedence level not implemented for operation type "
								+ value.getIdentifier());
			} // default

		} // switch (value.getIdentifier())

	}
}
