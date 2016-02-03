/*****************************************************************************************
 * Source File: ArithmeticEvaluator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.manager;

import net.ruready.common.exception.SystemException;
import net.ruready.common.rl.CommonNames;
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
import net.ruready.parser.logical.entity.value.EmptyValue;
import net.ruready.parser.logical.entity.value.LogicalValue;
import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.entity.SyntaxTreeNodeBranchComparator;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.FullMathValueVisitor;
import net.ruready.parser.options.exports.VariableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Numerically evaluate a logical expression syntax tree. Uses {@link ArithmeticEvaluator}
 * to evaluate arithmetic expression sub-trees.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public class LogicalEvaluator extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
		implements Evaluator<SyntaxTreeNode>, FullMathValueVisitor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(LogicalEvaluator.class);

	// ========================= FIELDS =====================================

	/**
	 * If true, we sort the result's statements and relations by their natural ordering.
	 */
	private final boolean sortResult;

	/**
	 * Variable map. An entry can be ("x", null) (for a symbolic variable) or ("x", 1.5)
	 * (for a variable that assumes a unique value).
	 */
	private VariableMap variables;

	/**
	 * Local variable that holds the currently processed tree node. The visit() methods
	 * need to use the node in addition to its data.
	 */
	private SyntaxTreeNode currentNode;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an logical evaluator object with no variables.
	 * 
	 * @param sortResult
	 *            if true, we sort the result's statements and relations by their natural
	 *            ordering.
	 */
	public LogicalEvaluator(final boolean sortResult)
	{
		super();
		this.sortResult = sortResult;
	}

	/**
	 * Create an logical evaluator object from variable values.
	 * 
	 * @param variables
	 *            holds variable values
	 * @param sortResult
	 *            if true, we sort the result's statements and relations by their natural
	 *            ordering.
	 */
	public LogicalEvaluator(final VariableMap variables, final boolean sortResult)
	{
		this.variables = variables;
		this.sortResult = sortResult;
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
		currentNode = thisNode;
		MathToken rootToken = thisNode.getData();
		rootToken.accept(this);
		return null;
	}

	// ========================= IMPLEMENTATION: Evaluator =================

	/**
	 * @see net.ruready.parser.evaluator.manager.Evaluator#evaluate(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	public SyntaxTreeNode evaluate(SyntaxTreeNode rootNode)
	{
		// Clone the tree to the result so that we can override nodes and data
		SyntaxTreeNode result = rootNode.clone();
		this.executeOnTree(result);

		if (sortResult)
		{
			// Sort the result's statements and relations by their natural ordering.
			result.setComparator(new SyntaxTreeNodeBranchComparator(null));
		}
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

	// ========================= IMPLEMENTATION: FullMathValueVisitor ==========

	// ========================= LOGICAL VALUES ============================

	/**
	 * @param value
	 */
	private void visit(LogicalValue value)
	{
		// Do nothing -- leave the result's logical structure intact.
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue,
	 *      java.lang.Object[])
	 */
	public void visit(ArithmeticLiteralValue value)
	{
		visit((LogicalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.RelationOperation,
	 *      java.lang.Object[])
	 */
	public void visit(RelationOperation value)
	{
		visit((LogicalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.logical.entity.value.EmptyValue)
	 */
	public void visit(EmptyValue value)
	{
		visit((LogicalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.logical.entity.value.ResponseValue)
	 */
	public void visit(ResponseValue value)
	{
		visit((LogicalValue) value);
	}

	// ========================= ARITHMETIC VALUES =========================

	/**
	 * @param visitable
	 */
	private void visit(ArithmeticValue visitable)
	{
		// Note: we don't know what sub-class of ArithmeticValue will
		// appear as the arithmetic's expression root node; therefore,
		// all sub-classes call this method in their visit() method below.

		// Evaluate the expression using an arithmetic evaluator
		Evaluator<NumericalValue> evaluator = new ArithmeticEvaluator(variables);
		NumericalValue expressionEvaluated = evaluator.evaluate(currentNode);

		// Replace the current node by its numerical evaluation
		MathToken newData = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				expressionEvaluated, MathTokenStatus.FICTITIOUS_CORRECT);
		currentNode.setData(newData);
		// By removing all children we also ensure that the internals of the
		// arithmetic expression are not processed by the logical evaluator.
		currentNode.removeAllChilds();
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue,
	 *      java.lang.Object[])
	 */
	public void visit(ComplexConstantValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue,
	 *      java.lang.Object[])
	 */
	public void visit(RealConstantValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction,
	 *      java.lang.Object[])
	 */
	public void visit(BinaryFunction value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(BinaryOperation value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(MultinaryOperation value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue,
	 *      java.lang.Object[])
	 */
	public void visit(ComplexValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue,
	 *      java.lang.Object[])
	 */
	public void visit(IntegerValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue,
	 *      java.lang.Object[])
	 */
	public void visit(RationalValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RealValue,
	 *      java.lang.Object[])
	 */
	public void visit(RealValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(UnaryOperation value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue,
	 *      java.lang.Object[])
	 */
	public void visit(ParenthesisValue value)
	{
		visit((ArithmeticValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.Variable,
	 *      java.lang.Object[])
	 */
	public void visit(Variable value)
	{
		visit((ArithmeticValue) value);
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

	// ========================= GETTERS & SETTERS =========================

}
