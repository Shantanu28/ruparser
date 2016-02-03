/*******************************************************
 * Source File: UniformSampleGenerator.java
 *******************************************************/
package net.ruready.parser.evaluator.entity;

import java.util.ArrayList;
import java.util.List;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generates a uniformly spaced grid of samples.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 25, 2007
 */

public class UniformSampleGenerator implements SampleGenerator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(UniformSampleGenerator.class);

	// ========================= FIELDS ====================================

	// Number of samples to be generated
	private int numSamples;

	// minimum and maximum ranges of the grid
	private NumericalValue rangeMin;

	private NumericalValue rangeMax;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a uniform grid sample generator
	 * 
	 * @param rangeMin
	 *            minimum sample value
	 * @param rangeMax
	 *            maximum sample value
	 * @param numSamples #
	 *            samples
	 */
	public UniformSampleGenerator(NumericalValue rangeMin, NumericalValue rangeMax, int numSamples)
	{
		super();
		this.numSamples = numSamples;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		if (numSamples < 2) {
			throw new IllegalArgumentException("# Samples must be at least 2, but was" + numSamples);
		}
		if (rangeMin.compareTo(rangeMax) >= 0) {
			throw new IllegalArgumentException("Empty sample grid range requested: " + "["
					+ rangeMin + "," + rangeMax + "]");
		}
	}

	// ========================= IMPLEMENTATION: SampleGenerator ====

	/**
	 * @see net.ruready.parser.evaluator.entity.SampleGenerator#getSamples()
	 */
	public List<NumericalValue> getSamples()
	{
		List<NumericalValue> samples = new ArrayList<NumericalValue>();
		NumericalValue meshSize = rangeMax.MINUS(rangeMin).OVER(
				rangeMin.createValue(numSamples - 1.0));
		NumericalValue sample = rangeMin;
		for (int i = 0; i < numSamples; i++) {
			samples.add((NumericalValue) sample.clone());
			sample.PLUS_EQUAL(meshSize);
		}
		return samples;
	}

	/**
	 * @see net.ruready.parser.evaluator.entity.SampleGenerator#setNumSamples(int)
	 */
	public void setNumSamples(int numSamples)
	{
		this.numSamples = numSamples;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the rangeMax
	 */
	public NumericalValue getRangeMax()
	{
		return rangeMax;
	}

	/**
	 * @return the rangeMin
	 */
	public NumericalValue getRangeMin()
	{
		return rangeMin;
	}

	/**
	 * @return the numSamples
	 */
	public int getNumSamples()
	{
		return numSamples;
	}
}
