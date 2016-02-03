/*****************************************************************************************
 * Source File: DefaultParserBD.java
 ****************************************************************************************/
package net.ruready.parser.service.exports;

import net.ruready.common.rl.ResourceLocator;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.param.entity.ParametricEvaluationTarget;
import net.ruready.parser.service.manager.AbstractParserManager;

/**
 * A default BD that implements the parser manager interface. It relies on a static hook
 * to instantiate a specific manager implementation and a resource locator.
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
 * @version Jul 26, 2007
 */
public class DefaultParserBD implements AbstractParserBD
{
	// ========================= CONSTANTS =================================

	/**
	 * We construct a manager and delegate all methods to it.
	 */
	protected final AbstractParserManager manager;

	/**
	 * Using this specific resource locator.
	 */
	protected final ResourceLocator resourceLocator;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize this BD from fields.
	 * 
	 * @param manager
	 *            wrapped manager
	 * @param resourceLocator
	 *            using this specific resource locator
	 */
	protected DefaultParserBD(final AbstractParserManager manager,
			final ResourceLocator resourceLocator)
	{
		super();
		this.manager = manager;
		this.resourceLocator = resourceLocator;
	}

	// ========================= IMPLEMENTATION: AbstractParserBD ======

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#convertMathML2Parser(java.lang.String,
	 *      net.ruready.parser.options.exports.ParserOptions)
	 */
	public String convertMathML2Parser(final String mathMLString,
			final ParserOptions options)
	{
		return manager.convertMathML2Parser(mathMLString, options);
	}

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#convertParser2MathML(java.lang.String,
	 *      net.ruready.parser.options.exports.ParserOptions)
	 */
	public String convertParser2MathML(final String parserString,
			final ParserOptions options)
	{
		return manager.convertParser2MathML(parserString, options);
	}

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#runDemo(java.lang.String,
	 *      java.lang.String, net.ruready.parser.options.exports.ParserOptions)
	 */
	public ParserRequest runDemo(final String referenceString,
			final String responseString, final ParserOptions options)
	{
		return manager.runDemo(referenceString, responseString, options);
	}

	/**
	 * @see net.ruready.parser.service.manager.AbstractParserManager#evaluate(java.lang.String,
	 *      net.ruready.parser.options.exports.ParserOptions)
	 */
	public ParametricEvaluationTarget evaluate(final String parametricString,
			final ParserOptions options)
	{
		return manager.evaluate(parametricString, options);
	}

	// ========================= GETTERS & SETTERS =========================
}
