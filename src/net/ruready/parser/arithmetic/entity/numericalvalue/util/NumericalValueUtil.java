/*****************************************************************************************
 * Source File: ErrorUtil.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.util;

import net.ruready.common.exception.UnsupportedOpException;
import net.ruready.common.misc.Utility;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to numerical values.
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
 * @version Aug 14, 2007
 */
public class NumericalValueUtil implements Utility
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NumericalValueUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private NumericalValueUtil()
	{

	}

	// ========================= METHODS ===================================

	/**
	 * Throw an i18nalizble {@link UnsupportedOpException} because a function is not
	 * supported on this domain of numbers.
	 * 
	 * @param functionName
	 *            name of unsupported function
	 * @return an UnsupportedOpException for this function
	 */
	public static UnsupportedOpException unsupportedOperationException(
			final String functionName)
	{
		return new UnsupportedOpException("Unsupported numerical operation: "
				+ functionName, ParserNames.KEY.MATH_EXCEPTION.UNSUPPORTED_OPERATION,
				functionName);
	}

}
