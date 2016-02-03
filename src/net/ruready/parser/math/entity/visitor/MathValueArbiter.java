/*****************************************************************************************
 * Source File: FullMathValueVisitor.java
 ****************************************************************************************/
package net.ruready.parser.math.entity.visitor;

import net.ruready.common.visitor.Arbiter;
import net.ruready.parser.math.entity.value.MathValue;

/**
 * Allows arbitration between different {@link Visitor<MathValue>} sub-classes inside the
 * {@link MathValue#accept()} method.
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
public interface MathValueArbiter extends Arbiter<AbstractMathValueVisitor>
{
	// =====================================
	// MathValue visitor types
	// =====================================

	void visit(ArithmeticValueVisitor visitor);

	void visit(LogicalValueVisitor visitor);

	void visit(FullMathValueVisitor visitor);
}
