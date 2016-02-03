/*****************************************************************************************
 * Source File: ParametricEvaluationTarget.java
 ****************************************************************************************/
package net.ruready.parser.param.entity;

import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An arithmetic target constructed by the math expression arithmetic parser.
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
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 26, 2007
 */
public class ParametricEvaluationTarget implements PubliclyCloneable
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParametricEvaluationTarget.class);

	// ========================= FIELDS ====================================

	// Used for multiple string parsing + evaluation during parametric string
	// matching and evaluation
	private final ParserRequest request;

	// Variable map. An entry can be ("x", null)
	// (for a symbolic variable) or ("x", 1.5) (for a
	// variable that assumes a unique value).
	private final VariableMap variables;

	// Evaluated string built by the parametric evaluation parser
	private StringBuffer evalString = TextUtil.emptyStringBuffer();

	/**
	 * Is evaluated string fully legal or not. It might be illegal from the parametric
	 * parser syntax rules perspective, or may contain some invalid expressions. In either
	 * case we set this field to <code>false</code> as well.
	 */
	private boolean legal = true;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an initial arithmetic target.
	 * 
	 * @param request
	 *            Used for multiple string parsing + evaluation during parametric string
	 *            matching and evaluation
	 * @param variables
	 *            Variable map. An entry can be ("x", null) (for a symbolic variable) or
	 *            ("x", 1.5) (for a variable that assumes a unique value).
	 */
	public ParametricEvaluationTarget(final ParserRequest request,
			final VariableMap variables)
	{
		super();
		this.request = request;
		this.variables = variables;
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#ring()
	 */
	@Override
	public String toString()
	{
		return evalString.toString();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target (or part of a target) of an assembly.
	 * <p>
	 * Note: the <code>request</code> field is soft-copied here.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public ParametricEvaluationTarget clone()
	{
		// try {
		// Soft-copy the options link -- this is always a link to an external
		// object
		VariableMap copyVariables = (VariableMap) variables.clone();
		ParametricEvaluationTarget copy = new ParametricEvaluationTarget(request,
				copyVariables);
		copy.evalString = new StringBuffer(evalString);
		copy.legal = legal;
		return copy;
		// }
		//
		// catch (CloneNotSupportedException e) {
		// // this shouldn't happen,
		// // because we are Cloneable
		// throw new InternalError("clone() failed: " + e.getMessage());
		// }

	}

	// ========================= METHODS ===================================

	/**
	 * Append a string to the evaluated string.
	 * 
	 * @param suffix
	 *            string to be appended
	 */
	public void appendToEvalString(final String suffix)
	{
		evalString.append(suffix);
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @param evalString
	 *            the evalString to set
	 */
	public void setEvalString(StringBuffer evalString)
	{
		this.evalString = evalString;
	}

	/**
	 * @return the request
	 */
	public ParserRequest getRequest()
	{
		return request;
	}

	/**
	 * @return the variables
	 */
	public VariableMap getVariables()
	{
		return variables;
	}

	/**
	 * @return the legal
	 */
	public boolean isLegal()
	{
		return legal;
	}

	/**
	 * @param legal the legal to set
	 */
	public void setLegal(boolean legal)
	{
		this.legal = legal;
	}

}
