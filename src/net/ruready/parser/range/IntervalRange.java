package net.ruready.parser.range;

import net.ruready.common.math.basic.SimpleInterval;
import net.ruready.common.math.real.RandomUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.tokenizer.manager.MathTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntervalRange extends SimpleInterval<Integer> implements Range
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathTokenizer.class);

	// For printouts and parsing of interval ranges
	public static final String DELIMITER = ":";

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	public IntervalRange(final Integer low, final Integer high)
	{
		super(low, high);
	}

	// ========================= IMPLEMENTATION: Range =====================

	/**
	 * @see net.ruready.parser.range.Range#randomPick()
	 */
	public NumericalValue randomPick()
	{
		// That appeared in the original implementation but
		// seems dubious. If rand is static, it's initialized
		// once and then we should keep getting new independent
		// random picks.
		// rand.setSeed(new Date().getTime());

		// Generate a random integer in the range
		// and convert it to complex (don't know why, really)
		int value = RandomUtil.randomInInterval(low, high);
		return new ComplexValue(1.0 * value);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Print an <code>IntervalRange</code>: this prints all its low and high in the
	 * same format as ParamRangeParser can parse.
	 */
	@Override
	public String toString()
	{
		return low + DELIMITER + high;
	}

}
