/*****************************************************************************************
 * Source File: MathParserException.java
 ****************************************************************************************/
package net.ruready.parser.service.exception;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.exception.InternationalizableErrorMessage;

/**
 * Signals that a given string is not recognizable as an arithmetic expression.
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
public class MathParserException extends ApplicationException
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	// ========================= FIELDS ====================================

	/**
	 * Math expression for which the error is reported.
	 */
	private final String expression;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Creates an i18nlizable exception from an i18n message.
	 * 
	 * @param expression
	 *            Math expression for which the error is reported
	 * @param errorMessage
	 *            i18n message
	 */
	public MathParserException(final String expression,
			final InternationalizableErrorMessage errorMessage)
	{
		super(errorMessage);
		this.expression = expression;
	}

	/**
	 * Creates an application exception.
	 * 
	 * @param expression
	 *            Math expression for which the error is reported
	 * @param message
	 *            literal error message
	 * @param args
	 *            pass these optional arguments to the actual error message specified by
	 *            the "message" reference label
	 */
	public MathParserException(final String expression, final String message,
			Object... args)
	{
		super(message, args);
		this.expression = expression;
	}

	/**
	 * Creates an i18nlizable exception with a custom key.
	 * 
	 * @param expression
	 *            Math expression for which the error is reported
	 * @param message
	 *            literal error message
	 * @param key
	 *            a key into an error message resource bundle such as Struts' application
	 *            resources bundle
	 * @param args
	 *            pass these optional arguments to the actual error message specified by
	 *            the "message" reference label
	 */
	public MathParserException(final String expression, final String message, String key,
			Object... args)
	{
		super(message, key, args);
		this.expression = expression;
	}

	/**
	 * Wraps and re-throws an exception as an application exception.
	 * 
	 * @param expression
	 *            Math expression for which the error is reported
	 * @param exception
	 *            wrapped exception
	 * @param message
	 *            literal error message
	 * @param args
	 *            pass these optional arguments to the actual error message specified by
	 *            the "message" reference label
	 */
	public MathParserException(final String expression, final Throwable exception,
			final String message, Object... args)
	{
		super(exception, message, args);
		this.expression = expression;
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the expression
	 */
	public String getExpression()
	{
		return expression;
	}
}
