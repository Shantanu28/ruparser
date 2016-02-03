/*****************************************************************************************
 * Source File: FullMathValueVisitor.java
 ****************************************************************************************/
package net.ruready.parser.math.entity.visitor;

import net.ruready.common.visitor.Visitor;
import net.ruready.parser.math.entity.value.MathValue;

/**
 * A base class for {@link MathValue} visitors. Allows a meta-visitor (arbiter) to access
 * the visitor classes.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 24, 2007
 */
public interface AbstractMathValueVisitor extends Visitor<MathValue>
{
	// ========================= ABSTRACT METHODS ==========================
}
