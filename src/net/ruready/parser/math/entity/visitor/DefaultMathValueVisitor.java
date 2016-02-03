/*****************************************************************************************
 * Source File: DefaultMathValueVisitor.java
 ****************************************************************************************/

package net.ruready.parser.math.entity.visitor;

import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue;
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.value.MathValue;

/**
 * A convenient empty hook implementation of a math value visitor -- this is a stub.
 * <p>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * (c) 2006-07 Continuing Education , University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah
 * @version Jul 3, 2007
 */
public class DefaultMathValueVisitor implements FullMathValueVisitor
{

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue)
	 */
	public void visit(ComplexValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RealValue)
	 */
	public void visit(RealValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue)
	 */
	public void visit(RationalValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue)
	 */
	public void visit(IntegerValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction)
	 */
	public void visit(BinaryFunction value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation)
	 */
	public void visit(BinaryOperation value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation)
	 */
	public void visit(MultinaryOperation value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation)
	 */
	public void visit(UnaryOperation value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.Variable)
	 */
	public void visit(Variable value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue)
	 */
	public void visit(ArithmeticLiteralValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue)
	 */
	public void visit(ParenthesisValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.ArithmeticValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue)
	 */
	public void visit(ComplexConstantValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.ArithmeticValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue)
	 */
	public void visit(RealConstantValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.logical.entity.value.RelationOperation)
	 */
	public void visit(RelationOperation value)
	{

	}

	/**
	 * @param visitable
	 * @see net.ruready.common.visitor.Visitor#visit(net.ruready.common.visitor.Visitable)
	 */
	public void visit(MathValue visitable)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.logical.entity.value.EmptyValue)
	 */
	public void visit(EmptyValue value)
	{

	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.logical.entity.value.ResponseValue)
	 */
	public void visit(ResponseValue value)
	{

	}

}
