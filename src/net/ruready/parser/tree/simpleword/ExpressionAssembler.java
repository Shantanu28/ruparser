/*****************************************************************************************
 * Source File: ExpressionEvaluationAssembler.java
 ****************************************************************************************/
package net.ruready.parser.tree.simpleword;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.tree.AbstractListTreeNode;
import net.ruready.parser.tree.exports.AbstractTargetMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler for post-processing, at the end of an arithmetic expression matching. To
 * be used in conjunction with {@link AbstractTargetMatcher}.
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
 * @version Sep 8, 2007
 */
class ExpressionAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ExpressionAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Post-processing of an arithmetic expression target object. Rounds the results,
	 * performs analysis on the tokens, etc.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a + ", target " + target);

		// Pop final syntax tree from stack
		AbstractListTreeNode<?, ?> e = (AbstractListTreeNode<?, ?>) a.pop();

		// ========================
		// Target post-processing
		// ========================

		// Set the completed syntax tree target to be the assembly's target.
		a.setTarget(e);

		// logger.debug("end, a=" + a);
	}
}
