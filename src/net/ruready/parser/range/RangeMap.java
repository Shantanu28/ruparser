package net.ruready.parser.range;

import java.util.Iterator;
import java.util.Map;

import net.ruready.common.misc.Immutable;
import net.ruready.common.misc.PublicMapEntry;
import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.parser.options.exports.VariableMap;

/**
 * A map of parameter ranges. Needs to know to generate a random sample for each the
 * parameters in the map. Has to be PubliclyCloneable to serve as [part of] a parser's
 * target.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112<br>
 * -------------------------------------------------------------------------
 * 
 * @immutable
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 27, 2007
 */
public interface RangeMap extends PubliclyCloneable, Map<String, Range>, Immutable
{
	/**
	 * Get an iterator over entries in the map. Variables are returned in alphabetical
	 * variable name.
	 * 
	 * @return variable iterator
	 */
	Iterator<PublicMapEntry<String, Range>> iterator();

	/**
	 * Pick a random value for each parameter according to its range.
	 * 
	 * @return map of random parameter values.
	 */
	VariableMap randomPick();
}
