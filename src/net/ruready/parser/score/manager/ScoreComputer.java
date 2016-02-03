/*******************************************************
 * Source File: ScoreComputer.java
 *******************************************************/
package net.ruready.parser.score.manager;

/**
 * An object that scores a response based on all the information gathered along
 * the parser processing chain so far.
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
public interface ScoreComputer
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Returns a response score based on all the information gathered along the
	 * parser processing chain so far.
	 * 
	 * @return reponse total score in [0,100]
	 */
	double getScore();
}
