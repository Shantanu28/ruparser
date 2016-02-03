/*******************************************************
 * Source File: SortChildrenHandler.java
 *******************************************************/
package net.ruready.parser.relative.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.relative.manager.RelativeCanonicalizer;
import net.ruready.parser.relative.manager.SortChildren;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler takes two syntax trees and an edit distance computer as an input
 * and runs the children sorting phase.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 6, 2007
 */
public class SortChildrenHandler extends RelativeCanonicalizationStepHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SortChildrenHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * @param name
	 */
	public SortChildrenHandler()
	{
		super(ParserNames.RELATIVE_CANONICALIZATION.SORT_CHIDLREN);
	}

	/**
	 * @param name
	 * @param nextNode
	 */
	public SortChildrenHandler(RequestHandler nextNode)
	{
		super(ParserNames.RELATIVE_CANONICALIZATION.SORT_CHIDLREN, nextNode);
	}

	// ========================= IMPLEMENTATION: AbsCanStepHandler (...) ===

	/**
	 * @see net.ruready.parser.relative.exports.RelativeCanonicalizationStepHandler#processTarget(net.ruready.parser.math.entity.MathTarget,
	 *      net.ruready.parser.math.entity.MathTarget)
	 */
	@Override
	protected void processTarget(MathTarget referenceTarget,
			MathTarget responseTarget)
	{
		// Get the syntax trees
		SyntaxTreeNode referenceTree = referenceTarget.getSyntax();
		SyntaxTreeNode responseTree = responseTarget.getSyntax();

		// Run the RC step
		RelativeCanonicalizer rc = new SortChildren(referenceTree, responseTree,
				this.editDistanceComputer);
		rc.execute();
	}
}
