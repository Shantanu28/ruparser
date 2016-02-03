/*****************************************************************************************
 * Source File: RedundancyRemover.java
 ****************************************************************************************/
package net.ruready.parser.absolute.manager;

import net.ruready.parser.logical.entity.value.RelationOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Transposes relations to a canonical form. Two types of children transpositions are
 * performed:
 * <ul>
 * <li><code>x &gt; y</code> is transposed to <code>y &lt; x</code>.</li>
 * <li><code>y = x</code> is transposed to <code>x = y</code>.</li>
 * </ul>
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
 * @version Jul 19, 2007
 */
public class RelationTransposer extends AbsoluteCanonicalizer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RelationTransposer.class);

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
	public RelationTransposer(final MathTarget target)
	{
		super();
		this.target = target;
	}

	// ========================= IMPLEMENTATION: AbsoluteCanonicalizer =====

	/**
	 * Post-traversal ordering implied. Using negative checks to decide whether to process
	 * this node.
	 * 
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#isProcessedPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected boolean isProcessedPost(SyntaxTreeNode thisNode)
	{
		// logger.debug("Considering thisNode " + thisNode + "...");
		MathToken data = thisNode.getData();

		// Not a relation, don't process
		if (data.getValueID() != MathValueID.LOGICAL_RELATION_OPERATION)
		{
			// logger.debug("process=false, not a relation");
			return false;
		}

		// Cast to a friendlier version
		RelationOperation value = (RelationOperation) data.getValue();

		// Process if relation has an inverse
		return value.hasInverse();
	}

	/**
	 * @see net.ruready.parser.absolute.manager.AbsoluteCanonicalizer#processPost(net.ruready.parser.math.entity.SyntaxTreeNode)
	 */
	@Override
	protected void processPost(SyntaxTreeNode thisNode)
	{
		logger.debug("processPost(thisNode = " + thisNode + ")");

		// Cast to a friendlier version
		MathToken data = thisNode.getData();
		RelationOperation relation = (RelationOperation) data.getValue();
		RelationOperation inverseRelation = relation.inverse();

		// Fetch children
		SyntaxTreeNode left = thisNode.getChild(RelationOperation.LEFT);
		SyntaxTreeNode right = thisNode.getChild(RelationOperation.RIGHT);

		if (relation == inverseRelation)
		{
			// ======================================
			// y = x ==> x = y
			// ======================================
			// If r is commutative, make sure left <= right according
			// to the natural children ordering.

			if (left.compareTo(right) > 0)
			{
				// Transpose children ordering
				thisNode.setChildAt(RelationOperation.LEFT, right);
				thisNode.setChildAt(RelationOperation.RIGHT, left);
			}

		}
		else if (!relation.isCanonical())
		{
			// ======================================
			// x > y ==> y < x
			// ======================================
			// Allow only canonical relations

			// Set r -> inverse(r) at root node
			data.setValue(inverseRelation);

			// Transpose children ordering
			thisNode.setChildAt(RelationOperation.LEFT, right);
			thisNode.setChildAt(RelationOperation.RIGHT, left);
		}
		logger.debug("Updated tree " + target.getSyntax());
	}
}
