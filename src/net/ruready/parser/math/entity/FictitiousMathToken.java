/*******************************************************************************
 * Source File: FictitiousMathToken.java
 ******************************************************************************/
package net.ruready.parser.math.entity;

import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A fictitious math token that's generated for canonicalization purposes. Its
 * status is always <code>MathTokenStatus.DISCARDED</code> and cannot be
 * changed, so in particular it cannot be "highlighted" in the HTML string
 * representation of the mathematical expression it belongs to.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 1, 2007
 */
public class FictitiousMathToken extends MathToken
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(FictitiousMathToken.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a fictious math token.
	 * 
	 * @param value
	 *            Mathematical value of object (numeric, string, token, etc.)
	 */
	public FictitiousMathToken(ArithmeticValue value)
	{
		super(CommonNames.MISC.INVALID_VALUE_INTEGER, value, MathTokenStatus.DISCARDED);
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	// ========================= IMPLEMENTATION: Comparable ================

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Set the status of this element. This method has no effect.
	 * 
	 * @param status
	 *            The status to set.
	 */
	@Override
	public void setStatus(MathTokenStatus status)
	{
		// Has no effect
	}
}
