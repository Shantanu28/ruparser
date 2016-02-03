/*******************************************************
 * Source File: ParserRequestUtil.java
 *******************************************************/
package net.ruready.parser.service.exports;

import java.io.OutputStream;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.HandlerMessage;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities related to parser request attribute input/output.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 14, 2007
 */
public class ParserRequestUtil
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParserRequestUtil.class);

	// ========================= CONSTRUCTORS ==============================

	/**
	 * <p>
	 * Hide constructor in utility class.
	 * </p>
	 */
	private ParserRequestUtil()
	{

	}

	// ========================= STATIC METHODS ============================

	/**
	 * Return the result of numerical comparison.
	 * 
	 * @param request
	 *            request to read data from
	 * @return are expressions equivalent or not
	 */
	public static boolean isEquivalent(ChainRequest request)
	{
		return ((Boolean) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.EVALUATOR.RESULT))
				.booleanValue();
	}

	/**
	 * Return the latest marker's result object.
	 * 
	 * @param request
	 *            request to read data from
	 * @return latest marker result object
	 */
	public static Analysis getLatestMarkerAnalysis(ChainRequest request)
	{
		return (Analysis) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.LATEST_RESULT);
	}

	/**
	 * Return the element marker result object.
	 * 
	 * @param request
	 *            request to read data from
	 * @return element marker result object
	 */
	public static Analysis getElementMarkerAnalysis(ChainRequest request)
	{
		return (Analysis) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.ELEMENTS_RESULT);
	}

	/**
	 * Return the ATPM marker result object.
	 * 
	 * @param request
	 *            request to read data from
	 * @return ATPM marker result object
	 */
	public static Analysis getATPMMarkerAnalysis(ChainRequest request)
	{
		return (Analysis) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.ATPM_RESULT);
	}

	/**
	 * Return the highlighted HTML reference string.
	 * 
	 * @param request
	 *            request to read data from
	 * @return the highlighted HTML reference string
	 */
	public static String getHTMLReferenceString(ChainRequest request)
	{
		return ((OutputStream) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.HTML.REFERENCE_STRING))
				.toString();
	}

	/**
	 * Return the highlighted HTML response string.
	 * 
	 * @param request
	 *            request to read data from
	 * @return the highlighted HTML response string
	 */
	public static String getHTMLResponseString(ChainRequest request)
	{
		return ((OutputStream) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.PORT.OUTPUT.HTML.RESPONSE_STRING))
				.toString();
	}

	/**
	 * Return the total score for the response.
	 * 
	 * @param request
	 *            request to read data from
	 * @return response total score
	 */
	public static double getResponseScore(ChainRequest request)
	{
		return ((Double) request.getAttribute(ParserNames.REQUEST.ATTRIBUTE.SCORE.SCORE))
				.doubleValue();
	}

	/**
	 * Print the request's message log
	 * 
	 * @param request
	 *            parser request to log
	 * @return string representation of message log
	 */
	public static String printMessages(ChainRequest request)
	{
		StringBuffer s = new StringBuffer("Message log:");
		s.append(CommonNames.MISC.NEW_LINE_CHAR);
		for (HandlerMessage message : request.getMessages()) {
			s.append(message);
			s.append(CommonNames.MISC.NEW_LINE_CHAR);
		}
		return s.toString();
	}
}
