/*****************************************************************************************
 * Source File: ArithmeticEvaluator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.manager;

import java.util.Stack;

import net.ruready.common.exception.SystemException;
import net.ruready.common.exception.UnsupportedOpException;
import net.ruready.common.tree.SimpleTreeVisitor;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.ArithmeticValueVisitor;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Numerically evaluate an arithmetic expression syntax tree.
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
 * @version Jul 6, 2007
 */
public class ArithmeticEvaluator extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
		implements Evaluator<NumericalValue>, ArithmeticValueVisitor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ArithmeticEvaluator.class);

	// ========================= FIELDS =====================================

	// Variable map. An entry can be ("x", null)
	// (for a symbolic variable) or ("x", 1.5) (for a
	// variable that assumes a unique value).
	private VariableMap variables;

	// Stack to hold intermediate arithmetic results
	private Stack<NumericalValue> stack;

	// Local variable that holds the number of children of a tree node
	private int numChildren;

	// Final evaluation result
	private NumericalValue result;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an evaluator object with no variables.
	 */
	public ArithmeticEvaluator()
	{
		super();
	}

	/**
	 * Create an evaluator object from variable values.
	 * 
	 * @param variables
	 *            holds variable values
	 */
	public ArithmeticEvaluator(final VariableMap variables)
	{
		super();
		this.variables = variables;
	}

	// ========================= IMPLEMENTATION: SimpleTreeVisitor =========

	/**
	 * @param thisNode
	 * @return
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePre(net.ruready.common.tree.MutableTreeNode)
	 */
	@Override
	protected Object executePre(SyntaxTreeNode thisNode)
	{
		return null;
	}

	/**
	 * @see net.ruready.common.tree.SimpleTreeVisitor#executePost(net.ruready.common.tree.AbstractListTreeNode)
	 */
	@Override
	protected Object executePost(SyntaxTreeNode thisNode)
	{
		// Post-traversal ordering: evaluate children before parent
		// Cast to a friendlier version
		numChildren = thisNode.getNumChildren();
		MathToken rootToken = thisNode.getData();
		// Value needs to be arithmetic
		ArithmeticValue value = (ArithmeticValue) rootToken.getValue();
		value.accept(this);
		return null;
	}

	/**
	 * Evaluate the tree.
	 * 
	 * @param rootNode
	 *            the root node of the tree.
	 */
	@Override
	protected void executeOnTree(SyntaxTreeNode rootNode)
	{
		// Initialize
		stack = new Stack<NumericalValue>();
		// Run evaluation
		super.executeOnTree(rootNode);
		// Pop final evaluation result from stack
		result = stack.pop();
	}

	// ========================= IMPLEMENTATION: Evaluator =================

	/**
	 * @see net.ruready.parser.evaluator.manager.Evaluator#evaluate(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	public NumericalValue evaluate(SyntaxTreeNode rootNode)
	{
		this.executeOnTree(rootNode);
		return result;
	}

	/**
	 * Set a new map of parameter values to be used in the evaluation.
	 * 
	 * @param variable
	 *            variable-to-value map
	 */
	public void setVariableMap(final VariableMap variables)
	{
		this.variables = variables;
	}

	// ========================= IMPLEMENTATION: FullMathValueVisitor =============

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue,
	 *      java.lang.Object[])
	 */
	public void visit(ComplexConstantValue value)
	{
		// Push the constant onto the stack; will serve as an operand
		stack.push(value.getNumericalValue());
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue,
	 *      java.lang.Object[])
	 */
	public void visit(RealConstantValue value)
	{
		// Push the constant onto the stack; will serve as an operand
		stack.push(value.getNumericalValue());
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction,
	 *      java.lang.Object[])
	 */
	public void visit(BinaryFunction value)
	{
		// Retrieve two operands from the stack
		NumericalValue operand2 = stack.pop();
		NumericalValue operand1 = stack.pop();
		// Push the result onto the stack
		stack.push(value.eval(operand1, operand2));
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(BinaryOperation value)
	{
		// Retrieve two operands from the stack
		NumericalValue operand2 = stack.pop();
		NumericalValue operand1 = stack.pop();
		// Push the result onto the stack
		stack.push(value.eval(operand1, operand2));
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(MultinaryOperation value)
	{
		// logger.debug("stack " + stack);
		// Retrieve n operands from the stack in reverse order
		NumericalValue[] operands = new NumericalValue[numChildren];
		for (int i = numChildren - 1; i >= 0; i--)
		{
			operands[i] = stack.pop();
			// logger.debug("i " + i + " " + operands[i].getClass());
		}
		// Push the result onto the stack
		stack.push(value.eval(operands));
	}

	/**
	 * @param value
	 * @param args
	 */
	private void visit(NumericalValue value)
	{
		// Push the value onto the stack; will serve as an operand
		stack.push(value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue,
	 *      java.lang.Object[])
	 */
	public void visit(ComplexValue value)
	{
		this.visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue,
	 *      java.lang.Object[])
	 */
	public void visit(IntegerValue value)
	{
		this.visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue,
	 *      java.lang.Object[])
	 */
	public void visit(RationalValue value)
	{
		this.visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RealValue,
	 *      java.lang.Object[])
	 */
	public void visit(RealValue value)
	{
		this.visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(UnaryOperation value)
	{
		// Retrieve one operand from the stack
		NumericalValue operand1 = stack.pop();
		// Push the result onto the stack
		stack.push(value.eval(operand1));
	}

	/**
	 * @param value
	 * @see net.ruready.parser.math.entity.visitor.ArithmeticValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue)
	 */
	public void visit(ParenthesisValue value)
	{
		// Some string token, probably parentheses, ignore
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.Variable,
	 *      java.lang.Object[])
	 */
	public void visit(Variable value)
	{
		// Push the variable's numerical value onto the stack; will serve as an
		// operand
		NumericalValue variableNumericalValue = variables.getValue(value.getName());
		if (variableNumericalValue == null)
		{
			throw new UnsupportedOpException("Failed to evaluate value",
					ParserNames.KEY.MATH_EXCEPTION.INEVALUABLE, value.getName());
		}
		stack.push(variableNumericalValue);
	}

	// ========================= UNSUPPORTED TYPES =========================

	/**
	 * @see net.ruready.common.visitor.Visitor#visit(net.ruready.common.visitor.Visitable,
	 *      java.lang.Object[])
	 */
	public void visit(MathValue visitable)
	{
		throw new SystemException("visitable '" + visitable + "' of type "
				+ visitable.getClass() + " is not supported by this visitor");
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue,
	 *      java.lang.Object[])
	 */
	public void visit(ArithmeticLiteralValue value)
	{
		visit((MathValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.RelationOperation,
	 *      java.lang.Object[])
	 */
	public void visit(RelationOperation value)
	{
		visit((MathValue) value);
	}

	// ========================= GETTERS & SETTERS =========================

}
