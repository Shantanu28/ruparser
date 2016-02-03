/*****************************************************************************************
 * Source File: ArithmeticEvaluator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.manager;

/**
 * Round a math expression syntax tree or a numerical value.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public interface Rounder<T extends Comparable<T>>
{
	/**
	 * Round a numerical object.
	 * 
	 * @param object
	 *            the object to be rounded.
	 */
	void round(T object);
}
