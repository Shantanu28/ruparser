/*****************************************************************************************
 * Source File: MarkerHandler.java
 ****************************************************************************************/
package net.ruready.parser.marker.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.common.chain.HandlerMessage;
import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.imports.ParserHandler;
import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.marker.manager.Marker;
import net.ruready.parser.marker.manager.MarkerFactory;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A generic handler for the analysis phase.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class MarkerHandler extends ParserHandler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MarkerHandler.class);

	// ========================= FIELDS ====================================

	// Marker type
	private AnalysisID analysisID;

	// Attribute name of the analysis type
	private final String attributeNameAnalysisID;

	// Attribute name of the analysis result object
	private final String attributeNameResult;

	// Marker object
	protected Marker marker;

	// Options object
	protected ParserOptions options;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a generic marker handler.
	 * 
	 * @param analysisID
	 *            analysis identifier
	 * @param attributeNameResult
	 *            attribute name of the analysis result object
	 */
	public MarkerHandler(final AnalysisID analysisID, final String attributeNameResult)
	{
		super();
		this.analysisID = analysisID;
		this.attributeNameAnalysisID = null;
		this.attributeNameResult = attributeNameResult;
	}

	/**
	 * Initialize a generic marker handler.
	 * 
	 * @param analysisID
	 *            analysis identifier
	 * @param attributeNameResult
	 *            attribute name of the analysis result object
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public MarkerHandler(final AnalysisID analysisID, final String attributeNameResult,
			RequestHandler nextNode)
	{
		super(nextNode);
		this.analysisID = analysisID;
		this.attributeNameAnalysisID = null;
		this.attributeNameResult = attributeNameResult;
	}

	/**
	 * Initialize a generic marker handler.
	 * 
	 * @param attributeNameAnalysisID
	 *            request attribute name holding the analysis identifier
	 * @param attributeNameResult
	 *            attribute name of the analysis result object
	 */
	public MarkerHandler(final String attributeNameAnalysisID,
			final String attributeNameResult)
	{
		super();
		this.attributeNameAnalysisID = attributeNameAnalysisID;
		this.attributeNameResult = attributeNameResult;
	}

	/**
	 * Initialize a generic marker handler.
	 * 
	 * @param attributeNameAnalysisID
	 *            request attribute name holding the analysis identifier
	 * @param attributeNameResult
	 *            attribute name of the analysis result object
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public MarkerHandler(final String attributeNameAnalysisID,
			final String attributeNameResult, RequestHandler nextNode)
	{
		super(nextNode);
		this.attributeNameAnalysisID = attributeNameAnalysisID;
		this.attributeNameResult = attributeNameResult;
	}

	/**
	 * Initialize a generic marker handler with the default request attribute name holding
	 * the analysis identifier.
	 * 
	 * @param attributeNameResult
	 *            attribute name of the analysis result object
	 */
	public MarkerHandler(final String attributeNameResult)
	{
		this(ParserNames.REQUEST.ATTRIBUTE.MARKER.MARKER_TYPE, attributeNameResult);
	}

	/**
	 * Initialize a generic marker handler with the default request attribute name holding
	 * the analysis identifier.
	 * 
	 * @param attributeNameResult
	 *            attribute name of the analysis result object
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public MarkerHandler(final String attributeNameResult, RequestHandler nextNode)
	{
		this(ParserNames.REQUEST.ATTRIBUTE.MARKER.MARKER_TYPE, attributeNameResult,
				nextNode);
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Process the reference + response targets. This is a hook. By default, this does
	 * nothing. This should also set the marker field.
	 * 
	 * @param target
	 *            arithmetic target to process
	 */
	protected void processTargets(MathTarget referenceTarget, MathTarget responseTarget)
	{
		// Set and run the marker using the parser options found in the
		// request
		marker = new MarkerFactory().createType(this.getAnalysisID(), this.options,
				referenceTarget, responseTarget);
		marker.compare();
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * @see net.ruready.common.chain.RequestHandler#handle(net.ruready.common.chain.ChainRequest)
	 */
	@Override
	final protected boolean handle(ChainRequest request)
	{
		// ====================================================
		// Cast request to a friendlier version and get inputs
		// ====================================================
		ParserRequest parserRequest = (ParserRequest) request;
		this.options = parserRequest.getOptions();

		// Get the two targets
		MathTarget referenceTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.REFERENCE);
		MathTarget responseTarget = (MathTarget) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TARGET.RESPONSE);

		// If this handler was initialized with an attribute name, fetch
		// the analysis type from the request.
		if (this.attributeNameAnalysisID != null)
		{
			analysisID = (AnalysisID) request.getAttribute(attributeNameAnalysisID);
			if (analysisID == null)
			{
				// The analysis ID was not found in the request, read it from
				// the parser options instead
				analysisID = options.getAnalysisID();
			}
		}

		// ====================================================
		// Business logic
		// ====================================================
		// Construct a canonicalizer object
		// logger.debug("Before " + name + ": tree " + tree);
		processTargets(referenceTarget, responseTarget);
		// logger.debug("After " + name + ": tree " + tree);

		// Prepare an output object
		Analysis result = new Analysis(marker.getNumElementMap(), marker.getDistance(),
				marker.getCorrectElementFraction(), responseTarget.getSyntax(), marker
						.getScore());

		// ====================================================
		// Attach outputs to the request
		// ====================================================
		// Save the marker object
		request
				.setAttribute(ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.LATEST_MARKER,
						marker);

		// Save the ED computer
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.EDIT_DISTANCE_COMPUTER,
				marker.getEditDistanceComputer());

		// Save a copy of the target for future reference
		MathTarget copy = responseTarget.clone();
		request.setAttribute(ParserNames.REQUEST.ATTRIBUTE.MARKER.RESPONSE_TARGET, copy);

		// Save the result under the latest result attribute name and under the
		// custom attribute name of this handler, if a custom result attribute
		// name was specified for this handler
		request
				.setAttribute(ParserNames.REQUEST.ATTRIBUTE.ANALYSIS.LATEST_RESULT,
						result);
		request.addMessage(new HandlerMessage(this.getName(), "Edit distance: "
				+ result.getEditDistance()));

		if (attributeNameResult != null)
		{
			request.setAttribute(attributeNameResult, result);
		}

		return false;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the analysisID
	 */
	public AnalysisID getAnalysisID()
	{
		return analysisID;
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return ((analysisID == null) ? "Generic" : analysisID.toString())
				+ CommonNames.MISC.TAB_CHAR + "marker";
	}
}
