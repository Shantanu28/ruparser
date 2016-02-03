/*****************************************************************************************
 * Source File: FullMathValueVisitor.java
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

/**
 * A unifying interface for all mathematical values, variables and operations. Part of a
 * visitor pattern framework for mathematical values and tokens appearing in parser syntax
 * trees. Encapsulates algorithms of <code>MathValue</code>s.
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
public interface FullMathValueVisitor extends AbstractMathValueVisitor
{
	// =====================================
	// Arithmetic values
	// =====================================

	void visit(ArithmeticLiteralValue value);

	void visit(BinaryFunction value);

	void visit(BinaryOperation value);

	void visit(ComplexConstantValue value);

	void visit(ComplexValue value);

	void visit(IntegerValue value);

	void visit(MultinaryOperation value);

	void visit(ParenthesisValue value);

	void visit(RealConstantValue value);

	void visit(RealValue value);

	void visit(RationalValue value);

	void visit(UnaryOperation value);

	void visit(Variable value);

	// =====================================
	// Logical values
	// =====================================

	void visit(EmptyValue value);

	void visit(ResponseValue value);

	void visit(RelationOperation value);
}
