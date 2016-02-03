/*******************************************************
 * Source File: SampleGenerator.java
 *******************************************************/
package net.ruready.parser.evaluator.entity;

import java.util.List;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * An interface for classes generating numerical sample values of variables.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 15, 2007
 */
public interface SampleGenerator
{
	// ========================= CONSTANTS =================================

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Set the number of samples to be generated.
	 * 
	 * @param numSamples
	 *            number of samples to be generated
	 */
	void setNumSamples(int numSamples);

	/**
	 * Return the list of generated samples
	 * 
	 * @return list of samples
	 */
	List<NumericalValue> getSamples();
}
