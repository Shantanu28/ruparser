/*****************************************************************************************
 * Source File: ArithmeticEvaluator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.manager;

import net.ruready.common.exception.SystemException;
import net.ruready.common.tree.SimpleTreeVisitor;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
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
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.FullMathValueVisitor;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Rounds a numerically evaluated logical tree. This means rounding each of the numerical
 * leaf nodes of the tree.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 16, 2007
 */
public class LogicalRounder extends SimpleTreeVisitor<MathToken, SyntaxTreeNode>
		implements Rounder<SyntaxTreeNode>, FullMathValueVisitor
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(LogicalRounder.class);

	// ========================= FIELDS =====================================

	// Parser control options containing rounding options
	private final ParserOptions options;

	// Local variable that holds the currently processed tree node.
	// The visit() methods need to use the node in addition to its data.
	private SyntaxTreeNode currentNode;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an logical object from variable values.
	 * 
	 * @param options
	 *            control options object
	 */
	public LogicalRounder(final ParserOptions options)
	{
		this.options = options;
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

	// ========================= IMPLEMENTATION: Rounder ===================

	/**
	 * @see net.ruready.parser.evaluator.manager.Rounder#round(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	public void round(SyntaxTreeNode rootNode)
	{
		// Clone the tree to the result so that we can override nodes and data
		SyntaxTreeNode result = rootNode.clone();
		this.executeOnTree(result);
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

	private void visit(NumericalValue visitable)
	{
		// Round evaluation result to tolerance specified by options;
		// replace the current node's value by the rounded value
		currentNode.setValue(options.round(visitable));
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue,
	 *      java.lang.Object[])
	 */
	public void visit(ComplexConstantValue value)
	{
		visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue,
	 *      java.lang.Object[])
	 */
	public void visit(RealConstantValue value)
	{
		visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue,
	 *      java.lang.Object[])
	 */
	public void visit(ComplexValue value)
	{
		visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue,
	 *      java.lang.Object[])
	 */
	public void visit(IntegerValue value)
	{
		visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue,
	 *      java.lang.Object[])
	 */
	public void visit(RationalValue value)
	{
		visit((NumericalValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RealValue,
	 *      java.lang.Object[])
	 */
	public void visit(RealValue value)
	{
		visit((NumericalValue) value);
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
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction,
	 *      java.lang.Object[])
	 */
	public void visit(BinaryFunction value)
	{
		visit((MathValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(BinaryOperation value)
	{
		visit((MathValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(MultinaryOperation value)
	{
		visit((MathValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation,
	 *      java.lang.Object[])
	 */
	public void visit(UnaryOperation value)
	{
		visit((MathValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue,
	 *      java.lang.Object[])
	 */
	public void visit(ParenthesisValue value)
	{
		visit((MathValue) value);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.FullMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.mathvalue.Variable,
	 *      java.lang.Object[])
	 */
	public void visit(Variable value)
	{
		visit((MathValue) value);
	}

	// ========================= GETTERS & SETTERS =========================

}
