/*****************************************************************************************
 * Source File: SimpleParserServiceProvider.java
 ****************************************************************************************/
package net.ruready.parser.service.exports;

import net.ruready.common.exception.SystemException;
import net.ruready.common.rl.parser.ParserService;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.parser.options.entity.MathExpressionType;

/**
 * An simple factory that instantiates different parser service types within this JVM.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 30, 2007
 */
public class SimpleParserServiceProvider implements ParserServiceProvider
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser service provider that instantiates different parser service types
	 * within this JVM.
	 */
	public SimpleParserServiceProvider()
	{
		super();
	}

	// ========================= IMPLEMENTATION: EnumeratedFactory =========

	/**
	 * @see net.ruready.parser.service.exports.ParserServiceProvider#createType(net.ruready.parser.service.exports.ParserServiceID,
	 *      java.lang.Object[])
	 */
	public ParserService createType(ParserServiceID identifier, Object... args)
	{
		switch (identifier)
		{
			/**
			 * Math expression parser demo processor.
			 */
			case MATH_DEMO:
			{
				return new MathParserDemoService((String) args[0], (String) args[1],
						(MathExpressionType) args[2], null);
			}

				/**
				 * Math expression parser demo processor with tree image generation.
				 */
			case MATH_DEMO_WITH_IMAGES:
			{
				return new MathParserDemoWithImagesService((String) args[0],
						(String) args[1], (MathExpressionType) args[2], null);
			}

				/**
				 * Math expression parser (full flow).
				 */
			case MATH_PARSER:
			{
				throw new SystemException(
						"Math parser flow is not fully implemented yet");
			}

				/**
				 * Parametric evaluation of a string.
				 */
			case PARAMETRIC_EVALUATION:
			{
				return new ParametricEvaluationService((String) args[0], null);
			}

			default:
			{
				throw new SystemException(
						"Unsupported parser service type " + identifier);
			}
		} // switch (identifier)
	}

	// ========================= FIELDS ====================================

}
