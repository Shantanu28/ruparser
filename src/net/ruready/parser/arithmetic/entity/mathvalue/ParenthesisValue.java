/*****************************************************************************************
 * Source File: ParenthesisValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A numerical or a symbolic variable in mathematical expressions analyzed by the parser.
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
 * @version Aug 15, 2007
 */
public class ParenthesisValue extends TokenValue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParenthesisValue.class);

	// Parenthesis sign; may later change to an instance field fit multiple
	// parentheses
	public static final Token PARENTHESIS = new Token(CommonNames.TREE.PARENTHESIS);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	public ParenthesisValue()
	{
		super(ParenthesisValue.PARENTHESIS);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Paren";
	}

	// ========================= IMPLEMENTATION: Visitable<FullMathValueVisitor>

	/**
	 * Let a visitor process this value type. Part of the TreeVisitor pattern. This calls
	 * back the visitor's <code>visit()</code> method with this item type. Must be
	 * overridden by every item sub-class.
	 * 
	 * @param <B>
	 *            specific visitor type; this method may be implemented for multiple
	 *            classes of {@link Visitable}.
	 * @param visitor
	 *            item visitor whose <code>visit()</code> method is invoked
	 * @see net.ruready.common.visitor.Visitable#accept(net.ruready.common.visitor.Visitor)
	 */
	@Override
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		MathValueVisitorUtil.accept(this, visitor);
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathValueID getIdentifier()
	{
		return MathValueID.ARITHMETIC_PARENTHESIS;
	}

	// ========================= IMPLEMENTATION: MathValue =================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "Paren";
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isArithmeticOperation()
	 */
	public boolean isArithmeticOperation()
	{
		return false;
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isNumerical()
	 */
	public boolean isNumerical()
	{
		return false;
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isSignOp()
	 */
	public boolean isSignOp()
	{
		return false;
	}

	// ========================= METHODS ===================================

	// ========================= STATIC METHODS ============================

	// ========================= GETTERS & SETTERS =========================
}
