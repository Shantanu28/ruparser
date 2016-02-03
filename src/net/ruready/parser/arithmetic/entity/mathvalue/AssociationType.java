/*****************************************************************************************
 * Source File: AssociationType.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.common.rl.CommonNames;

/**
 * Arithmetic operation association type. <code>BOTH</code> means that the operation is
 * associate.
 * <p>
 * Side note: commutativity does <i>not</i> imply associativity, which might be
 * hypothesized from the examining the four basic arithmetic operations. Counter example:
 * a o b := mean(a,b) = (a+b)/2. (1 o 1) o 3 = 2 != 1.5 = 1 o (1 o 3).
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jan 31, 2007
 */
public enum AssociationType
{
	// ========================= CONSTANTS =================================

	/**
	 * Associative operation: <br>
	 * a o b o c = a o (b o c) = (a o b) o c<br>
	 * Examine: plus
	 */
	BOTH
	{
		@Override
		public int getRequiredParenthesesAroundChildIndex()
		{
			return CommonNames.MISC.INVALID_VALUE_INTEGER;
		}
	},

	/**
	 * Operation associates to the left:<br>
	 * a o b o c = (a o b) o c<br>
	 * a o b o c != a o (b o c)<br>
	 * Example: minus
	 */
	LEFT
	{
		@Override
		public int getRequiredParenthesesAroundChildIndex()
		{
			return BinaryValue.RIGHT;
		}
	},

	/**
	 * Operation associates to the right:<br>
	 * a o b o c != (a o b) o c<br>
	 * a o b o c = a o (b o c)<br>
	 * Example: power
	 */
	RIGHT
	{
		@Override
		public int getRequiredParenthesesAroundChildIndex()
		{
			return BinaryValue.LEFT;
		}
	};

	// ========================= FIELDS ====================================

	// ========================= METHODS ===================================

	/**
	 * If a parent and a child, which are both of the same operation type that has this
	 * association type, are considered for deciding whether parentheses around the child
	 * are required, the parentheses are required and not redundant if and only if the
	 * child's index in the parent's children array equals the result of this method.
	 * 
	 * @return child index for which parentheses around this operation are redundant, if
	 *         it is
	 */
	public abstract int getRequiredParenthesesAroundChildIndex();

	/**
	 * Is this operation associative.
	 * 
	 * @return <code>true</code> iff this operation is associative
	 */
	public boolean isAssociative()
	{
		return this.getRequiredParenthesesAroundChildIndex() == CommonNames.MISC.INVALID_VALUE_INTEGER;
	}

}
