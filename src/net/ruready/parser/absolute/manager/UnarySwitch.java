/*****************************************************************************************
 * Source File: UnarySwitch.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.manager.SyntaxTreeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Switches a unary minus with a multiplication or division operation.
 * <p>
 * Examples (square brackets denote operation precedence):<br>
 * 1. [-x]*y -> -[x*y]<br>
 * 2. x*-y -> -[x*y]<br>
 * 3. -x*-y -> -[x*-y]<br>
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Jun 4, 2007
 */
public class UnarySwitch extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(UnarySwitch.class);

	// ========================= FIELDS ====================================

	// arithmetic target object where redundant tokens are stored
	// outside the syntax tree
	private final MathTarget target;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize redundancy remover.
	 * 
	 * @param target
	 *            arithmetic target where redundant tokens are stored
	 */
	public UnarySwitch(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Post-traversal ordering implied. Using positive checks to decide whether to process
	 * this node.
	 * 
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken thisData = thisNode.getData();

		// Process only binary *, /
		if (thisData.getValueID() != MathValueID.ARITHMETIC_BINARY_OPERATION)
		{
			return false;
		}
		BinaryOperation thisValue = (BinaryOperation) thisData.getValue();
		return (thisValue == BinaryOperation.TIMES)
				|| (thisValue == BinaryOperation.DIVIDE);
	}

	/**
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");
		// MathToken thisData = thisNode.getData();
		// BinaryOp thisValue = (BinaryOp) thisData.getValue();

		// ---------------------------------------------------------------
		// * Replace
		// thisValue { - { childA } childB}
		// by
		// - { thisValue { childA childB } }
		// * Similarly if the - is in front of childB.
		// ---------------------------------------------------------------

		// Loop over children and look for a unary minus
		for (SyntaxTreeNode child : thisNode.getChildren())
		{
			MathToken childData = child.getData();
			if (UnarySwitch.isUnaryMinus(childData))
			{
				// This is a unary minus child, process and break the loop

				// Remove the child under the parent; grandchild replaces child
				thisNode.removeChildTree(child);

				// Create a new branch newParent to replace thisNode's parent
				SyntaxTreeNode newParent = new SyntaxTreeNode(childData);
				SyntaxTreeUtil.replaceParentNode(target, thisNode, newParent);

				break;
			}
		}

		logger.debug("Updated tree " + target.getSyntax());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Check if this is a unary minus operation.
	 * 
	 * @param thisData
	 *            a node's data
	 * @return is data a unary minus operation.
	 */
	private static boolean isUnaryMinus(MathToken thisData)
	{
		return ((thisData.getValueID() == MathValueID.ARITHMETIC_UNARY_OPERATION) && ((UnaryOperation) thisData
				.getValue() == UnaryOperation.MINUS));
	}
}
