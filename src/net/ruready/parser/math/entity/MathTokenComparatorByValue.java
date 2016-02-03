/*******************************************************
 * Source File: MathTokenComparatorByValue.java
 *******************************************************/
package net.ruready.parser.math.entity;

import java.util.Comparator;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of math tokens in a syntax tree based on their value. Status is
 * discarded here. This is useful in marker's "highlight" generation.
 * 
 * @todo add multi-nary operations
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jan 26, 2007
 */
public class MathTokenComparatorByValue implements Comparator<MathToken>
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
	private static final Log logger = LogFactory.getLog(MathTokenComparatorByValue.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an operator precedence comparator.
	 */
	public MathTokenComparatorByValue()
	{

	}

	// ========================= IMPLEMENTATION: Comparator ================

	/**
	 * A comparator of math tokens in a syntax tree based on their element index
	 * in the original assembly of the expression.
	 * 
	 * @param element1
	 *            left operand to be compared
	 * @param element2
	 *            right operand to be compared
	 * @return result of comparison by the <code>0th element</code> field of
	 *         both operands
	 */
	public int compare(MathToken element1, MathToken element2)
	{
		// Identical to MathToken#compareTo() except status comparison
		// logger.debug("compareTo(), this " + this + " element2 " + element2);
		// Compare value types by their natural ordering
		int compareType = element1.getValueID().compareTo(element2.getValueID());
		if (compareType != 0) {
			// logger
			// .debug(" type " + getValueID() + "," + element2.getValueID() + "
			// ==>
			// "
			// + compareType);
			return compareType;
		}

		int compareValue;
		// type is equal, compare by numerical value if this value is numerical;
		// element2wise, compare the value string representations
		if (element1.isNumerical()) {
			NumericalValue thisNumericalValue = (NumericalValue) element1.getValue();
			NumericalValue element2NumericalValue = (NumericalValue) element2.getValue();
			compareValue = (thisNumericalValue == null) ? ((element2NumericalValue == null) ? 0
					: -1) : thisNumericalValue.compareTo(element2NumericalValue);
			// logger.debug(" value " + thisNumericalValue + "," +
			// element2NumericalValue + " ==> "
			// + compareValue);
		}
		else {
			// compare by value string representation
			compareValue = (element1.getValue() == null) ? ((element2.getValue() == null) ? 0 : -1)
					: element1.getValue().toString().compareTo(element2.getValue().toString());
			// logger.debug(" value string "+value+","+element2.getValue()+" ==>
			// "
			// +compareValue);
		}
		return compareValue;
	}

	// ========================= PRIVATE METHODS ===========================
}
