/*****************************************************************************************
 * Source File: ExpressionEvaluationAssembler.java
 ****************************************************************************************/
package net.ruready.parser.logical.assembler;

import java.util.Iterator;
import java.util.List;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.logical.entity.value.ResponseValue;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler for post-processing, at the end of logical expression matching. Adds a
 * fictitious root node to the expression's syntax tree.
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
class ResponseAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ResponseAssembler.class);

	// ========================= FIELDS ====================================

	// Fence token
	private final Token fence;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a response assembler
	 * 
	 * @param fence
	 *            fence token (for recognizing multiple statements on the stack)
	 */
	public ResponseAssembler(final Token fence)
	{
		super();
		this.fence = fence;
	}

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
		MathTarget target = (MathTarget) a.getTarget();

		// =====================================================================
		// Make a new tree with a root node indicating that this is a logical
		// expression.
		// =====================================================================
		MathToken rootData = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				new ResponseValue(), MathTokenStatus.FICTITIOUS_CORRECT);
		SyntaxTreeNode response = new SyntaxTreeNode(rootData);

		// =====================================================================
		// Pop all statements above the fence and append them to the new tree
		// =====================================================================
		List<?> allTerms = Assembler.elementsAbove(a, fence);
		Iterator<?> iterator = allTerms.iterator();
		while (iterator.hasNext())
		{
			// Read statements and add them in reverse order to the response
			// syntax tree
			SyntaxTreeNode statement = (SyntaxTreeNode) iterator.next();
			response.addChild(0, statement);
		}

		// Save the tree in the target
		target.setSyntax(response);
	}

}
