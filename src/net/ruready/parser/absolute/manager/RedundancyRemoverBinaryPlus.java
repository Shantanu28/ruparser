/*******************************************************************************
 * Source File: RedundancyRemoverBinaryPlus.java
 ******************************************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Removes redundant binary plus operations from a syntax tree. This includes
 * the cases a+-b -> a-b and a+-b-c (where the second minus happens to take
 * precedence to the plus operation in the tree's inferred operation precedence) ->
 * a-b-c.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 1, 2007
 */
public class RedundancyRemoverBinaryPlus extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(RedundancyRemoverBinaryPlus.class);

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
	public RedundancyRemoverBinaryPlus(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Post-traversal ordering implied. Using negative checks to decide whether
	 * to process this node.
	 * 
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken data = thisNode.getData();

		// Token is not an invertible binary or already marked as redundant
		// ==> don't process
		if ((data.getStatus() == MathTokenStatus.REDUNDANT)
				|| (!AbsoluteCanonicalizationUtil.isInvertibleBinary(data))) {
			// logger.debug("process=false, already redundant or not an
			// invertible binary");
			return false;
		}

		// Invertible binary + but its inverse does not have a unary form ==>
		// don't process
		BinaryOperation thisOp = (BinaryOperation) data.getValue();
		UnaryOperation unaryInverse = thisOp.inverse().unaryOpForm();
		if (unaryInverse == null) {
			// logger.debug("process=false, Invertible binary + but its inverse
			// does" + " not have a unary form ==> don't process");
			return false;
		}

		SyntaxTreeNode rightChild = thisNode.getChild(BinaryValue.RIGHT);
		// Binary + but the right operand is not the unary op form of the
		// inverse of thisNode ==> don't process
		MathToken rightChildData = thisNode.getChildData(BinaryValue.RIGHT);

		if ((rightChild.getValueID() != MathValueID.ARITHMETIC_UNARY_OPERATION)
				|| (((UnaryOperation) rightChildData.getValue()) != unaryInverse)) {
			// logger.debug("process=false, binary + but right child is not
			// the inverse binary, but " + rightChildData.getValue());
			return false;
		}

		// All other cases ==> process
		return true;
	}

	/**
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");

		// Set data status to redundant and move it
		// to list of extraneous tokens in the arithmetic target
		MathToken data = thisNode.getData();
		data.setStatus(MathTokenStatus.REDUNDANT);
		target.addExtraneous(data);

		// Replace thisNode.data by rightChild.data (containing the correct
		// reference to the original assembly token) with the inverse -binary-
		// operation as its new value
		SyntaxTreeNode rightChild = thisNode.getChild(BinaryValue.RIGHT);
		BinaryOperation thisOp = (BinaryOperation) thisNode.getValue();
		thisNode.setData(rightChild.getData().cloneWithNewValue(thisOp.inverse()));

		// Get rid of rightChild; move its child in its place
		SyntaxTreeNode rightGrandChild = rightChild.getChild(UnaryOperation.ARG);

		// Replace thisNode.parent with thisNode.rightChild
		thisNode.replaceChild(rightChild, rightGrandChild);

		logger.debug("Updated tree " + target.getSyntax());
	}

}
