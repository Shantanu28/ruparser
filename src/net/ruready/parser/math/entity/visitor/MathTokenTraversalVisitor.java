/*******************************************************************************
 * Source File: MathTokenTraversalVisitor.java
 ******************************************************************************/
package net.ruready.parser.math.entity.visitor;

import net.ruready.common.visitor.Visitor;
import net.ruready.parser.math.entity.MathToken;

/**
 * A naming interface for all visitor classes that set math tokens' traversal
 * indices. (There might be other math token visitors other than this one.)
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 12, 2007
 */
public interface MathTokenTraversalVisitor extends Visitor<MathToken>
{

	// ========================= ABSTRACT METHODS ===========================

	/**
	 * Return the traversal index to be set in the next processed math token
	 * 
	 * @return the traversal index to be set in the next processed math token
	 */
	int getTraversalIndex();
}
