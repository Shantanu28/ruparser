/*******************************************************
 * Source File: Unary2MultinaryConverterHandler.java
 *******************************************************/
package net.ruready.parser.absolute.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.absolute.manager.AbsoluteCanonicalizer;
import net.ruready.parser.absolute.manager.Unary2MultinaryConverter;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler adapts arithmetic matching chain result to the single evaluation
 * chain required input.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class Unary2MultinaryConverterHandler extends AbsoluteCanonicalizationStepHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Unary2MultinaryConverterHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * @param attributeNameTarget
	 * @param name
	 * @param nextNode
	 */
	public Unary2MultinaryConverterHandler(String attributeNameTarget,
			RequestHandler nextNode)
	{
		super(attributeNameTarget, ParserNames.ABSOLUTE_CANONICALIZATION.UNARY2MULTINARY, nextNode);
	}

	/**
	 * @param attributeNameTarget
	 * @param name
	 */
	public Unary2MultinaryConverterHandler(String attributeNameTarget)
	{
		super(attributeNameTarget, ParserNames.ABSOLUTE_CANONICALIZATION.UNARY2MULTINARY);
	}

	// ========================= IMPLEMENTATION: AbsCanStepHandler (...) ===

	/**
	 * @see net.ruready.parser.absolute.exports.AbsoluteCanonicalizationStepHandler#processTarget(net.ruready.parser.math.entity.MathTarget)
	 */
	@Override
	protected void processTarget(MathTarget target)
	{
		// Get the syntax tree
		SyntaxTreeNode tree = target.getSyntax();

		// Run the abs. can. step
		AbsoluteCanonicalizer canonicalizer = new Unary2MultinaryConverter(target);
		canonicalizer.executeOnTree(tree);
	}
}
