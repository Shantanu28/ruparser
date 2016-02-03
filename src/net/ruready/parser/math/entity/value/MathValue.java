/*****************************************************************************************
 * Source File: MathValue.java
 ****************************************************************************************/
package net.ruready.parser.math.entity.value;

import net.ruready.common.discrete.Identifiable;
import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.misc.Immutable;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;

/**
 * A unifying interface for all mathematical values, variables and operations. Part of a
 * visitor pattern framework for mathematical values and tokens appearing in parser syntax
 * trees.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 24, 2007
 */
public interface MathValue extends TolerantlyComparable<MathValue>,
		Visitable<AbstractMathValueVisitor>, Identifiable<MathValueID>, Immutable
{
	/**
	 * Returns a short string representation of this math value type.
	 * 
	 * @return a short string representation of this math value type.
	 */
	String simpleName();

	/**
	 * Check if this token type has a numerical value.
	 * 
	 * @return Does this token type have a numerical value.
	 */
	boolean isNumerical();

	/**
	 * Check if this value represents an arithmetic operation.
	 * 
	 * @return Is this value represents an arithmetic operation.
	 */
	boolean isArithmeticOperation();

	/**
	 * Is this value a sign operation (unary +/-) or not.
	 * 
	 * @return is this value a sign operation (unary +/-) or not
	 */
	boolean isSignOp();

	// /**
	// * Returns the HTML representation of this object.
	// *
	// * @return the HTML representation of this object.
	// */
	// String toHTML();
}
