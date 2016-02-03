/*****************************************************************************************
 * Source File: ArithmeticEvaluator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.manager;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.VariableMap;

/**
 * Numerically evaluate a math expression syntax tree.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public interface Evaluator<R extends TolerantlyComparable<? super R> & Comparable<? super R>>
{
	/**
	 * Evaluate a tree.
	 * 
	 * @param rootNode
	 *            the root node of the tree.
	 * @return the result of evaluation. This might be a numerical value or a logical tree
	 *         of numerical values
	 */
	R evaluate(SyntaxTreeNode rootNode);

	/**
	 * Set a new map of parameter values to be used in the evaluation.
	 * 
	 * @param variables
	 *            variable-to-value map
	 */
	void setVariableMap(final VariableMap variables);
}
