/*******************************************************
 * Source File: Unary2MultinaryConverter.java
 *******************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.manager.SyntaxTreeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a unary operation into a multi-nary operation.
 * <p>
 * EXample: -x -> +-x
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 1, 2007
 */
public class Unary2MultinaryConverter extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Unary2MultinaryConverter.class);

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
	public Unary2MultinaryConverter(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Is <code>thisData</code> a unary operation with an inverse.
	 * Post-traversal ordering implied.
	 * 
	 * @param thisData
	 *            a node's data
	 * @return is <code>thisData</code> a unary operation with an inverse
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken thisData = thisNode.getData();
		// logger
		// .debug("valueID " + thisData.getValueID() + " value "
		// + thisData.getValue());
		// logger.debug("unary value? "
		// + (thisData.getValueID() == MathValueID.ARITHMETIC_UNARY_OPERATION));
		// if (thisData.getValueID() == MathValueID.ARITHMETIC_UNARY_OPERATION) {
		// logger.debug("multinary inverse "
		// + (((UnaryOp) thisData.getValue()).multinaryOpInverse()));
		// }
		return ((thisData.getValueID() == MathValueID.ARITHMETIC_UNARY_OPERATION) && (((UnaryOperation) thisData
				.getValue()).multinaryOpInverse() != null));
	}

	/**
	 * Process a unary operation that satisfies
	 * {@link #isUnaryOpCase(MathToken)}. Converts -y (unary -) into +-y where
	 * the + is multinary and the - is unary.
	 * 
	 * @param thisNode
	 *            processed node (syntax tree)
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");
		MathToken thisData = thisNode.getData();
		UnaryOperation thisValue = (UnaryOperation) thisData.getValue();

		// Create a fictitious multi-nary operation token for the inverse
		// operation of thisNode's value
		MultinaryOperation multinaryInverse = thisValue.multinaryOpInverse();
		MathToken newParentData = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				multinaryInverse, MathTokenStatus.FICTITIOUS_CORRECT);

		// Create a new branch newParent to replace thisNode's parent
		SyntaxTreeNode newParent = new SyntaxTreeNode(newParentData);
		SyntaxTreeUtil.replaceParentNode(target, thisNode, newParent);

		logger.debug("Updated tree " + target.getSyntax());
	}
	// ========================= PRIVATE METHODS ===========================
}
