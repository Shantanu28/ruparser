/*****************************************************************************************
 * Source File: FullMathValueVisitor.java
 ****************************************************************************************/
package net.ruready.parser.math.entity.visitor;

import net.ruready.parser.logical.entity.value.RelationOperation;

/**
 * A unifying interface for all logical values. Part of a visitor pattern framework for
 * mathematical values and tokens appearing in parser syntax trees. Encapsulates
 * algorithms of <code>MathValue</code>s in logical expression syntax trees.
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
public interface LogicalValueVisitor extends AbstractMathValueVisitor
{
	// =====================================
	// Logical values
	// =====================================

	void visit(RelationOperation value);
}
