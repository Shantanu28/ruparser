package net.ruready.parser.range.assembler;

import java.util.List;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.range.IntervalRange;
import net.ruready.parser.range.ParamRangeParser;
import net.ruready.parser.range.RangeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Append an interval parameter range to the target parameter range map.
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
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 26, 2007
 */
class IntervalAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(IntervalAssembler.class);

	/**
	 * Append a parameter interval range to the target parameter range map. This pops two
	 * integers above the symbol fence and the symbol, and creates a new range entry in
	 * target.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void workOn(Assembly a)
	{
		// Pop all elements above the symbol fence.
		// There should be two (high popped first, low second)
		List<Token> allValues = Assembler.elementsAbove(a, ParamRangeParser.SYMBOL_FENCE);

		// Pop the parameter's symbol (already a string)
		String symbol = (String) a.pop();

		// Get target
		RangeMap target = (RangeMap) a.getTarget();

		// Make discrete Range
		// Each element is supposedly an integer; if not, round
		int high = (int) Math.round(allValues.get(0).nval());
		int low = (int) Math.round(allValues.get(1).nval());
		IntervalRange interval = new IntervalRange(low, high);

		// Add a new entry string in target
		target.put(symbol, interval);
	}
}
