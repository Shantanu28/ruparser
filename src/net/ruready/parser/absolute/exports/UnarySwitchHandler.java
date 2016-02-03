/*****************************************************************************************
 * Source File: UnarySwitchHandler.java
 ****************************************************************************************/
package net.ruready.parser.absolute.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.parser.absolute.manager.AbsoluteCanonicalizer;
import net.ruready.parser.absolute.manager.UnarySwitch;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler runs the unary - / binary *,/ switch step of the absolute canonicalization
 * phase.
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
public class UnarySwitchHandler extends AbsoluteCanonicalizationStepHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(UnarySwitchHandler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * @param attributeNameTarget
	 * @param name
	 * @param nextNode
	 */
	public UnarySwitchHandler(String attributeNameTarget, RequestHandler nextNode)
	{
		super(attributeNameTarget, ParserNames.ABSOLUTE_CANONICALIZATION.UNARY_SWITCH,
				nextNode);
	}

	/**
	 * @param attributeNameTarget
	 * @param name
	 */
	public UnarySwitchHandler(String attributeNameTarget)
	{
		super(attributeNameTarget, ParserNames.ABSOLUTE_CANONICALIZATION.UNARY_SWITCH);
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
		AbsoluteCanonicalizer canonicalizer = new UnarySwitch(target);
		canonicalizer.executeOnTree(tree);
	}
}
