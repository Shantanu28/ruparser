package net.ruready.parser.range;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import net.ruready.common.math.real.RandomUtil;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DiscreteRange extends LinkedHashSet<NumericalValue> implements Range
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(DiscreteRange.class);

	// For printouts and parsing of set ranges
	public static final String DELIMITER = ",";

	// ========================= FIELDS ====================================

	// ################## FIELDS #######################
	public DiscreteRange()
	{
		super();
	}

	// ################## METHODS #######################

	/**
	 * @see net.ruready.parser.range.Range#randomPick()
	 */
	public NumericalValue randomPick()
	{
		// In a LinkedHashSet
		// elements are sorted by their order of insertion.
		Object[] array = toArray();
		int index = RandomUtil.randomInInterval(0, size() - 1);
		return (NumericalValue) array[index];
	}

	/**
	 * Print an <code>SetRange</code>: this prints all elements in the same delimited
	 * format as ParamRangeParser can parse.
	 */
	@Override
	public String toString()
	{
		String s = CommonNames.MISC.EMPTY_STRING;
		// Sort by lexicographic order of values
		SortedSet<NumericalValue> keySet = Collections
				.synchronizedSortedSet(new TreeSet<NumericalValue>(this));
		Iterator<NumericalValue> it = keySet.iterator();
		while (it.hasNext())
		{
			s = s + (it.next()).toString();
			if (it.hasNext())
			{
				s = s + DELIMITER;
			}
		}
		return s;
	}

}
