/*****************************************************************************************
 * Source File: DefaultMathValueArbiter.java
 ****************************************************************************************/

package net.ruready.parser.math.entity.visitor;

import net.ruready.parser.math.entity.value.MathValue;

/**
 * A default implementation of {@link MathValueArbiter}.
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
 * @version Jul 17, 2007
 */
@SuppressWarnings("deprecation")
@Deprecated
public class DefaultMathValueArbiter implements MathValueArbiter
{
	// ========================= FIELDS ===================================

	/**
	 * The object to be visited.
	 */
	private final MathValue visitable;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize a visitor arbiter.
	 */
	public DefaultMathValueArbiter(final MathValue visitable)
	{
		this.visitable = visitable;
	}

	// ========================= STATIC METHODS ============================

	// ========================= IMPLEMENTATION: Arbiter ===================

	/**
	 * @see net.ruready.common.visitor.Arbiter#visit(net.ruready.common.visitor.Visitable,
	 *      net.ruready.common.visitor.Visitor)
	 */
	public void visit(AbstractMathValueVisitor visitor)
	{
		visitor.visit(visitable);
	}

	// ========================= IMPLEMENTATION: MathValueArbiter ==========

	/**
	 * @see net.ruready.parser.math.entity.visitor.MathValueArbiter#visit(net.ruready.parser.math.entity.value.MathValue,
	 *      net.ruready.parser.math.entity.visitor.FullMathValueVisitor)
	 */
	public void visit(FullMathValueVisitor visitor)
	{
		visitor.visit(visitable);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.MathValueArbiter#visit(net.ruready.parser.math.entity.value.MathValue,
	 *      net.ruready.parser.math.entity.visitor.ArithmeticValueVisitor)
	 */
	public void visit(ArithmeticValueVisitor visitor)
	{
		visitor.visit(visitable);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.MathValueArbiter#visit(net.ruready.parser.math.entity.value.MathValue,
	 *      net.ruready.parser.math.entity.visitor.LogicalValueVisitor)
	 */
	public void visit(LogicalValueVisitor visitor)
	{
		visitor.visit(visitable);
	}
}
