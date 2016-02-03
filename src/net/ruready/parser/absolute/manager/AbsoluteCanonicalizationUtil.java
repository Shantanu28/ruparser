/*******************************************************
 * Source File: AbsoluteCanonicalizationUtil.java
 *******************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.common.misc.Utility;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to absolute canonicalization steps.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 15, 2007
 */
public class AbsoluteCanonicalizationUtil implements Utility
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(AbsoluteCanonicalizationUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private AbsoluteCanonicalizationUtil()
	{

	}

	// ========================= CONSTRUCTORS ==============================

	// ========================= PUBLIC STATIC METHODS =====================

	/**
	 * Is this syntax tree token a unary plus operation.
	 * 
	 * @param data
	 *            syntax tree token
	 * @return is this syntax tree token a unary plus operation
	 */
	public static boolean isUnaryPlus(MathToken data)
	{
		return ((data.getValueID() == MathValueID.ARITHMETIC_UNARY_OPERATION) && ((UnaryOperation) data
				.getValue() == UnaryOperation.PLUS));
	}

	/**
	 * Is this syntax tree token a unary minus operation.
	 * 
	 * @param data
	 *            syntax tree token
	 * @return is this syntax tree token a minus plus operation
	 */
	public static boolean isUnaryMinus(MathToken data)
	{
		return ((data.getValueID() == MathValueID.ARITHMETIC_UNARY_OPERATION) && ((UnaryOperation) data
				.getValue() == UnaryOperation.MINUS));
	}

	/**
	 * Is this syntax tree token an invertible binary operation.
	 * 
	 * @param data
	 *            syntax tree token
	 * @return is this syntax tree token an invertible binary operation
	 */
	public static boolean isInvertibleBinary(MathToken data)
	{
		return ((data.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION) && (((BinaryOperation) data
				.getValue()).inverse() != null));
	}

	/**
	 * Is this syntax tree token a binary plus operation.
	 * 
	 * @param data
	 *            syntax tree token
	 * @return is this syntax tree token a binary plus operation
	 */
	public static boolean isBinaryPlus(MathToken data)
	{
		return ((data.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION) && ((BinaryOperation) data
				.getValue() == BinaryOperation.PLUS));
	}

	/**
	 * Is this syntax tree token a minus plus operation.
	 * 
	 * @param data
	 *            syntax tree token
	 * @return is this syntax tree token a minus plus operation
	 */
	public static boolean isBinaryMinus(MathToken data)
	{
		return ((data.getValueID() == MathValueID.ARITHMETIC_BINARY_OPERATION) && ((BinaryOperation) data
				.getValue() == BinaryOperation.MINUS));
	}

	/**
	 * Assuming that {@link #isBinaryOpCase(MathToken)} is true, is the value of
	 * <code>isBinaryOpCase</code> the multi-nary operation (if it's its
	 * inverse, this returns <code>false</code>).
	 * 
	 * @param thisData
	 *            a node's data
	 * @return assuming that {@link #isBinaryOpCase(MathToken)} is true, is the
	 *         value of <code>isBinaryOpCase</code> the multi-nary operation
	 *         (if it's its inverse, this returns <code>false</code>).
	 */
	public static boolean isBinaryOpMultinarySubCase(MathToken thisData)
	{
		BinaryOperation thisValue = (BinaryOperation) thisData.getValue();
		return (thisValue.multinaryOpForm() != null);
	}

	// ========================= PRIVATE STATIC METHODS ====================
}
