/*****************************************************************************************
 * Source File: DataAssembler.java
 ****************************************************************************************/
package net.ruready.parser.tree.syntax;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.util.EnumUtil;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.math.entity.value.MathValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler that pops a single tree node data from the assembly's stack and processes
 * it.
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
class DataAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(DataAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Replace the top token in the stack with the token's Double value. Add the number to
	 * the target's lists of expression parts.
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		MathTarget target = (MathTarget) a.getTarget();

		// Pop the data's status, value and type from the assembly
		Token statusToken = (Token) a.pop();
		Token valueToken = (Token) a.pop();
		Token typeToken = (Token) a.pop();
		// logger.debug("statusToken " + statusToken + " valueToken " +
		// valueToken +
		// " typeToken " + typeToken);
		// Convert to friendlier versions
		MathValueID type = EnumUtil.createFromString(MathValueID.class, typeToken.sval());
		String valueStr = valueToken.toString();
		MathValue value = type.createMathValue(valueStr, target.getArithmeticMode());
		MathTokenStatus status =
				EnumUtil.createFromString(MathTokenStatus.class, statusToken.sval());

		// Create a new tree with a single node
		MathToken data = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER, value);
		data.setStatus(status);
		SyntaxTreeNode tree = new SyntaxTreeNode(data);

		// Push tree result onto stack
		a.push(tree);
		// logger.debug("end, a=" + a);
	}
}
