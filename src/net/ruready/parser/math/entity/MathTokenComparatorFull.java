/*******************************************************
 * Source File: MathTokenComparatorFull.java
 *******************************************************/
package net.ruready.parser.math.entity;

import java.util.Comparator;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A comparator of math tokens in a syntax tree that breaks all ties based on
 * ALL fields. Includes comparison of values, stati and primary element indices.
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
public class MathTokenComparatorFull implements Comparator<MathToken>
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
	private static final Log logger = LogFactory.getLog(MathTokenComparatorFull.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an operator precedence comparator.
	 */
	public MathTokenComparatorFull()
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
		MathValueID element1type = element1.getValueID();
		MathValueID element2type = element2.getValueID();
		// logger.debug("type1 " + element1type + " type2 " + element2type);
		int compareType = (element1type == null) ? ((element2type == null) ? 0 : -1)
				: ((element2type == null) ? 1 : element1type.compareTo(element2type));
		if (compareType != 0) {
			return compareType;
		}

		// Compare by value
		int compareValue;
		// type is equal, compare by numerical value if this value is numerical;
		// element2wise, compare the value string representations
		if (element1.isNumerical()) {
			NumericalValue thisNumericalValue = (NumericalValue) element1.getValue();
			NumericalValue element2NumericalValue = (NumericalValue) element2.getValue();
			compareValue = (thisNumericalValue == null) ? ((element2NumericalValue == null) ? 0
					: -1)
					: thisNumericalValue.compareTo(element2NumericalValue);
			// logger.debug(" value " + thisNumericalValue + "," +
			// element2NumericalValue + " ==> "
			// + compareValue);
		}
		else {
			// compare by value string representation
			compareValue = (element1.getValue() == null) ? ((element2.getValue() == null) ? 0
					: -1)
					: element1.getValue().toString().compareTo(
							element2.getValue().toString());
			// logger.debug(" value string "+value+","+element2.getValue()+" ==>
			// "
			// +compareValue);
		}
		if (compareValue != 0) {
			return compareValue;
		}

		// Compare by status
		int compareStatus = element1.getStatus().compareTo(element2.getStatus());
		if (compareStatus != 0) {
			// logger.debug(" status " + element1.getStatus() + "," +
			// element2.getStatus()
			// + " ==> " + compareStatus);
			return compareStatus;
		}

		// Finally, compare by element index
		int compareElement = new Integer(element1.get(0)).compareTo(element2.get(0));
		logger.debug(" status " + element1.get(0) + "," + element2.get(0) + " ==> "
				+ compareElement);
		// if (compareElement != 0) {
		return compareElement;
		// }
	}
	// ========================= PRIVATE METHODS ===========================
}
