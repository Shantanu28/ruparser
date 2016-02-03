/*******************************************************
 * Source File: RandomSampleGenerator.java
 *******************************************************/
package net.ruready.parser.evaluator.entity;

import java.util.ArrayList;
import java.util.List;

import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generates a random set of logarithmically (on the average at least) spaced
 * grid of samples.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 25, 2007
 */

public class RandomSampleGenerator implements SampleGenerator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RandomSampleGenerator.class);

	// Exponents refer to this base
	private final double base = 10.0;

	// ========================= FIELDS ====================================

	// Number of samples to be generated
	private int numSamples;

	// minimum and maximum of dynamic range of the grid
	private double exponentRangeMin = -5.0;

	private double exponentRangeMax = 5.0;

	// arithmetic mode
	private ArithmeticMode mode;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a uniform grid sample generator
	 * 
	 * @param arithmeticMode
	 *            arithmetic mode; samples have the corresponding numerical
	 *            value type
	 * @param exponentRangeMin
	 *            minimum exponent in the grid's dynamic range
	 * @param exponentRangeMax
	 *            maximum exponent in the grid's dynamic range
	 * @param numSamples
	 *            maximum number of samples to be generated
	 */
	public RandomSampleGenerator(ArithmeticMode mode, double exponentRangeMin,
			double exponentRangeMax, int numSamples)
	{
		super();
		this.mode = mode;
		this.numSamples = numSamples;
		this.exponentRangeMin = exponentRangeMin;
		this.exponentRangeMax = exponentRangeMax;
		if (numSamples < 2) {
			throw new IllegalArgumentException("# Samples must be at least 2, but was" + numSamples);
		}
		if (exponentRangeMin >= exponentRangeMax) {
			throw new IllegalArgumentException("Empty sample dynamic grid range requested: " + "["
					+ exponentRangeMin + "," + exponentRangeMax + "]");
		}
	}

	// ========================= IMPLEMENTATION: SampleGenerator ====

	/**
	 * Generate random samples for a variable. We purposely include outliers
	 * (uniformly spaced on a logarithmetic scale): very large small/large,
	 * negative/positive, real/complex x's), for crazy cases.
	 * 
	 * @see net.ruready.parser.evaluator.entity.SampleGenerator#getSamples()
	 */
	public List<NumericalValue> getSamples()
	{
		/*
		 * NumericalValue meshSize = rangeMax.MINUS(rangeMin).OVER(
		 * rangeMin.createValue(numSamples - 1.0)); NumericalValue sample =
		 * rangeMin; for (int i = 0; i < numSamples; i++) {
		 * samples.add((NumericalValue) sample.clone());
		 * sample.PLUS_EQUAL(meshSize); } return samples;
		 */
		List<NumericalValue> samples = new ArrayList<NumericalValue>();

		// We construct random samples in cells of a
		// logarithmically-uniform grid
		// between base^-maxExpoent and base^maxExpoent (mirror
		// imaging it to negative numbers). This
		// should probably depend on machine precision.
		double meshSize = (exponentRangeMax - exponentRangeMin) / (numSamples / 3 + 1);

		// Loop over logarithmic grid and append to samples until
		// we exahust the nSamples quota
		for (int i = 0; i <= numSamples / 3; i++) {
			double left = exponentRangeMin + i * meshSize;
			double right = left + meshSize;
			double exponent = left + (right - left) * Math.random();
			// double exponent = left;
			double sample = Math.pow(base, exponent);

			// Positive sample
			samples.add(mode.createValue(sample));

			// Negative sample
			samples.add(mode.createValue(-sample));

			if (mode.compareTo(ArithmeticMode.COMPLEX) >= 0) {
				// Complex sample, if we are in complex arithmetic mode
				samples.add(mode.createValue(new ComplexValue(-sample, sample)));
			}
			else {
				// Another real sample
				samples.add(mode.createValue(1.1 * sample));
			}
		}
		return samples;
	}

	/**
	 * @see net.ruready.parser.evaluator.entity.SampleGenerator#setNumSamples(int)
	 */
	public void setNumSamples(int numSamples)
	{
		// Round down to a multiple of 3
		this.numSamples = 3 * (numSamples / 3);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Compute number of samplesn for each variable from the number of
	 * variables, given all the constraints on the minimum and maximum sample
	 * size. n has to be small for efficiency: the total #configuration is
	 * n^(number of parameters) On the other hand, the higher n, the more
	 * confident we can be in the result of equalizing two expressions. Criteria
	 * to choose "nSamples": - At most MAX_SAMPLES total configurations - At
	 * most MAX_VAR_SAMPLES
	 * 
	 * @param nVariables #
	 *            [symbolic] variables
	 * @return #samples per variable (n)
	 */
	public static int computeNumSamples(int nVariables)
	{
		return Math.max(1, Math.min(ParserNames.EVALUATOR.MAX_SAMPLES_PER_VARIABLE, (int) (Math
				.floor(Math.pow(ParserNames.EVALUATOR.MAX_SAMPLES, 1.0 / nVariables)))));
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the exponentRangeMax
	 */
	public double getExponentRangeMax()
	{
		return exponentRangeMax;
	}

	/**
	 * @return the exponentRangeMin
	 */
	public double getExponentRangeMin()
	{
		return exponentRangeMin;
	}

	/**
	 * @return the numSamples
	 */
	public int getNumSamples()
	{
		return numSamples;
	}
}
