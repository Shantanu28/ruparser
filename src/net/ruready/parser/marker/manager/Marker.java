/*******************************************************************************
 * Source File: Marker.java
 ******************************************************************************/
package net.ruready.parser.marker.manager;

import java.util.Map;

import net.ruready.common.discrete.Identifiable;
import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;

/**
 * An object that analyzes a response target vs. a reference target and
 * generates<br>
 * <ol>
 * <li>A nodal mapping between the two trees (which response node corresponds
 * to which reference node).</li>
 * <li>Sets the status of math tokens of both the response and reference trees.</li>
 * <li>Math token (element) counts for each status type.
 * </ol>
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 11, 2007
 */
public interface Marker extends Identifiable<AnalysisID>
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Run the analysis on a response target vs. a reference target.
	 */
	void compare();

	/**
	 * Returns the reference target.
	 * 
	 * @return the reference target
	 */
	MathTarget getReferenceTarget();

	/**
	 * Returns the response target.
	 * 
	 * @return the response target.
	 */
	MathTarget getResponseTarget();

	/**
	 * Return the number of elements marked with a certain status.
	 * 
	 * @param status
	 *            math token status
	 * @return number of elements that have this status after
	 *         <code>compare()</code> is invoked
	 */
	int getNumElements(MathTokenStatus status);

	/**
	 * Return a map of status to element count (for all stati). This invokes the
	 * {@link #getNumElements(MathTokenStatus)} for all stati and stacks the
	 * results in a map.
	 * 
	 * @return a map of status to element count
	 */
	Map<MathTokenStatus, Integer> getNumElementMap();

	/**
	 * Returns the edit distance between the two trees.
	 * 
	 * @return the edit distance between the two trees
	 */
	double getDistance();

	/**
	 * The correct element fractions is defined as c/(c+e) where c = # correct,
	 * e = # errors = #missing + # unrecognized + #wrong.
	 * 
	 * @see net.ruready.parser.marker.manager.Marker#getDistance()
	 */
	double getCorrectElementFraction();

	/**
	 * Returns the marker-based score of the response. This is not the total
	 * score for the response. It is also optional and may serve as a basis for
	 * score computers, or they may ignore it and compute a score based on other
	 * data and/or other fields in the marker's <code>Analysis</code> target
	 * object.
	 * 
	 * @return a sub-total element-based response score in [0,100]
	 */
	double getScore();

	/**
	 * Return the edit distance computer prepared by this marker.
	 * 
	 * @return the edit distance computer prepared by this marker
	 */
	EditDistanceComputer<MathToken, SyntaxTreeNode> getEditDistanceComputer();
}
