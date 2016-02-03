/*******************************************************
 * Source File: AssessmentTestScoreComputer.java
 *******************************************************/
/**
 * File: AssessmentTestScoreComputer.java
 */
package net.ruready.parser.score.manager;

import net.ruready.parser.marker.entity.Analysis;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.exports.ParserRequestUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Assessment test score computer. The response score is given by the formula
 * <p>
 * score = a * (c/(c+e)) + (1-a) * equiv
 * <p>
 * c = # correct elements<br>
 * e = total # erroneous elements<br>
 * equiv = (response is equivalent to reference) ? 1 : 0<br>
 * a = parameter in [0,1] (optimized by a previous validation study to be 0.7)
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah
 * 
 * University of Utah, Salt Lake City, UT 84112 Protected by U.S. Provisional
 * Patent U-4003, February 2006
 * 
 * @version May 15, 2007
 */

public class AssessmentTestScoreComputer implements ScoreComputer
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AssessmentTestScoreComputer.class);

	// ========================= FIELDS ====================================

	// Parser request to draw data from
	private final ParserRequest request;

	// Cached score (computed once, may be accessed multiple times)
	private final double score;

	// Weights for score components
	private final double errorWeight;

	private final double correctnessWeight;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an assessment test score computer.
	 * 
	 * @param request
	 *            parser request to draw data from
	 * @param errorWeight
	 *            erroneous-element sub-total score weight in the score formula;
	 *            must be in [0,1]
	 */
	public AssessmentTestScoreComputer(final ParserRequest request, final double errorWeight)
	{
		super();
		this.request = request;
		this.errorWeight = errorWeight;
		this.correctnessWeight = 1 - this.errorWeight;
		// Must be invoked AFTER all fields are initialized
		this.score = this.computeScore();
	}

	// ========================= IMPLEMENTATION: Marker ===========

	/**
	 * @see net.ruready.parser.score.manager.ScoreComputer#getScore()
	 */
	public double getScore()
	{
		return score;
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @return
	 */
	private double computeScore()
	{
		// Operates on the latest result available in the request
		Analysis analysis = ParserRequestUtil.getLatestMarkerAnalysis(request);
		boolean equivalent = ParserRequestUtil.isEquivalent(request);
		return 100.0 * errorWeight * analysis.getCorrectElementFraction() + correctnessWeight
				* ((equivalent) ? 100.0 : 0.0);
	}
}
