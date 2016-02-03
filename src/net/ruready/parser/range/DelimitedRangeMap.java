package net.ruready.parser.range;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.misc.ComparableMapIterator;
import net.ruready.common.misc.PublicMapEntry;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.options.entity.DefaultVariableMap;
import net.ruready.parser.options.exports.VariableMap;
import net.ruready.parser.param.exception.ParamEvalParserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A delimited parameter range list for parametric questions. Ranges are integer (e.g.
 * [6,8]). Printout format: x 1 2 y 6 8 etc. to represent 1 <= x <= 2, 6 <= y <= 8 etc.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @immutable
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 27, 2007
 */
public class DelimitedRangeMap implements RangeMap
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
	private static final Log logger = LogFactory.getLog(DelimitedRangeMap.class);

	// For parsing a string into a parameter map
	protected static final String delimiter = " ";

	// ========================= FIELDS ====================================

	/**
	 * An internal map of variable name-to-range.
	 */
	private Map<String, Range> rangeMap = new HashMap<String, Range>();

	// ========================= NESTED TYPES ==============================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct an empty parameter map.
	 */
	public DelimitedRangeMap()
	{
		super();
	}

	/**
	 * Parse a parameter map from a space delimited string using a tokenizer. Format: x 1
	 * 2 y 6 8 etc. to represent 1 <= x <= 2, 6 <= y <= 8 etc.
	 * 
	 * @param space
	 *            delimited keyword string.
	 * @return vector of keywords.
	 */
	public DelimitedRangeMap(String str) throws ParamEvalParserException
	{
		super();
		if (str == null)
		{
			return;
		}
		StringTokenizer tok = new StringTokenizer(str, delimiter);
		try
		{
			while (tok.hasMoreTokens())
			{
				// Format: (param low high)*
				String param = tok.nextToken();
				int low = new Integer(tok.nextToken()).intValue();
				int high = new Integer(tok.nextToken()).intValue();
				put(param, new IntervalRange(low, high));
			}
		}
		catch (Exception e)
		{
			// TODO: i18n this error message
			throw new ParamEvalParserException(str, new InternationalizableErrorMessage(
					"Bad format for parameter map", null));
		}
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Result of equality of two interval. They are equal if and only if their
	 * <code>low,high</code> fields are equal up to a relative tolerance of 1e-16.
	 * 
	 * @param o
	 *            The other <code>Interval</code> object.
	 * @return boolean The result of equality.
	 */
	@Override
	public boolean equals(Object obj)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		if (this == obj)
		{
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}
		// Cast to friendlier version
		DelimitedRangeMap other = (DelimitedRangeMap) obj;

		return rangeMap.equals(other.rangeMap);
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return rangeMap.hashCode();
	}

	/**
	 * Print a parameter map as a delimited string. Format: x 1 y 6.5 etc. to represent
	 * x=1, y=6.5 etc. Parameters are sorted by natural string ascending order.
	 * 
	 * @return String describing the map in the format (symbol value)* .
	 */
	@Override
	public String toString()
	{
		StringBuffer s = TextUtil.emptyStringBuffer();
		Iterator<PublicMapEntry<String, Range>> it = iterator();
		while (it.hasNext())
		{
			PublicMapEntry<String, Range> entry = it.next();
			String param = entry.getKey();
			// Append parameter entry to s
			// Note: Interval.toString() returns "[a,b]".
			// What we want is Interval.print() that returns
			// "a b".
			s = s.append(param).append(delimiter).append(entry.getValue());
			if (it.hasNext())
			{
				s = s.append(delimiter);
			}
		}
		return s.toString();
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a shallow copy of this object. Must be implemented for this object to serve
	 * as a target of an assembly.
	 * 
	 * @return a deep copy of this object
	 */
	@Override
	public DelimitedRangeMap clone()
	{
		// String, Range are immutable, so no need to deep copy map entries
		DelimitedRangeMap copy = new DelimitedRangeMap();
		copy.rangeMap = new HashMap<String, Range>(rangeMap);
		return copy;
	}

	// ========================= IMPLEMENTATION: RangeMap ==================

	/**
	 * Get an iterator over entries in the map. Variables are returned in alphabetical
	 * variable name.
	 * 
	 * @return variable iterator
	 * @see net.ruready.parser.range.RangeMap#iterator()
	 */
	public Iterator<PublicMapEntry<String, Range>> iterator()
	{
		return new ComparableMapIterator<String, Range>(this);
	}

	/**
	 * Pick a random value for each parameter according to its range. In this case we pick
	 * a uniformly random value in each parameter's interger interval range.
	 * 
	 * @return map of random parameter values.
	 * @see net.ruready.parser.range.RangeMap#randomPick()
	 */
	public VariableMap randomPick()
	{
		VariableMap paramValues = new DefaultVariableMap();
		for (Map.Entry<String, Range> entry : rangeMap.entrySet())
		{
			String name = entry.getKey();
			Range i = entry.getValue();
			// Pick a random number in the interval i
			NumericalValue value = i.randomPick();
			// logger.debug("param="+param+
			// " i="+i+" value="+value);
			paramValues.addNumerical(name, value);
		}

		logger.debug("Generated paramValues=" + paramValues);
		return paramValues;
	}

	// ========================= METHODS ===================================

	/**
	 * # parameters in the map.
	 * 
	 * @return # parameters in the map.
	 */
	public int size()
	{
		return rangeMap.size();
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		rangeMap.clear();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		return rangeMap.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		return rangeMap.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<String, Range>> entrySet()
	{
		return rangeMap.entrySet();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Range get(Object key)
	{
		return rangeMap.get(key);
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return rangeMap.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet()
	{
		return rangeMap.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Range put(String key, Range value)
	{
		return rangeMap.put(key, value);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String, ? extends Range> m)
	{
		rangeMap.putAll(m);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Range remove(Object key)
	{
		return rangeMap.remove(key);
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<Range> values()
	{
		return rangeMap.values();
	}

}
