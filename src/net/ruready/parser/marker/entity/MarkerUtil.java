/*****************************************************************************************
 * Source File: MarkerUtil.java
 ****************************************************************************************/
package net.ruready.parser.marker.entity;

import java.util.List;

import net.ruready.common.rl.CommonNames;
import net.ruready.parser.marker.manager.Marker;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility methods used in parser markers.
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
 * @version Aug 16, 2007
 */
public class MarkerUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MarkerUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private MarkerUtil()
	{

	}

	// ========================= METHODS ===================================

	/**
	 * Return the number of errors e := #missing + # unrecognized + #wrong. Must be called
	 * after the marker's analysis has been completed.
	 * 
	 * @param marker
	 *            marker object performing the analysis
	 */
	public static int getNumErrors(Marker marker)
	{
		int e = 0;
		for (MathTokenStatus status : MathTokenStatus.values())
		{
			if (status.isError())
			{
				e += marker.getNumElements(status);
			}
		}
		return e;
	}

	/**
	 * Reset the stati of reference and response <code>MathToken</code> lists before
	 * comparing them in this marker. All reference tokens are marked as missing and all
	 * response tokens as unrecognized.
	 * 
	 * @param referenceTokens
	 *            list of reference expression tokens
	 * @param responseTokens
	 *            list of response expression tokens
	 */
	public static void resetTokens(List<MathToken> referenceTokens,
			List<MathToken> responseTokens)
	{
		logger.debug("<<<<< Initializing: >>>>>");
		for (MathToken mt : referenceTokens)
		{
			// logger.debug("Marking reference token " + mt + " as missing");
			mt.setStatus(MathTokenStatus.MISSING);
		}
		for (MathToken mt : responseTokens)
		{
			// logger.debug("Marking response token " + mt + " as unrecognized");
			mt.setStatus(MathTokenStatus.UNRECOGNIZED);
		}
		logger.debug(CommonNames.MISC.EMPTY_STRING);
	}
}
