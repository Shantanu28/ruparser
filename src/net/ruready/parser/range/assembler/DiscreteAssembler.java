package net.ruready.parser.range.assembler;

import java.util.List;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.range.DiscreteRange;
import net.ruready.parser.range.ParamRangeParser;
import net.ruready.parser.range.RangeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Append a discrete parameter range to the target parameter range map.
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
 * @version Jul 27, 2007
 */
class DiscreteAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(DiscreteAssembler.class);

	/**
	 * Append a parameter range to the target parameter range map. This pops all the
	 * elements above the symbol fence and the symbol, and creates a new range entry in
	 * target.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void workOn(Assembly a)
	{
		// Pop all elements above the symbol fence
		List<Token> allValues = Assembler.elementsAbove(a, ParamRangeParser.SYMBOL_FENCE);

		// Pop the parameter's symbol (already a string)
		String symbol = (String) a.pop();

		// Get target
		RangeMap target = (RangeMap) a.getTarget();

		// Make discrete Range
		DiscreteRange set = new DiscreteRange();

		for (int i = 0; i < allValues.size(); i++)
		{
			// Each element is supposedly a double
			double value = allValues.get(i).nval();
			set.add(new ComplexValue(value));
		}

		// Add a new entry string in target
		target.put(symbol, set);
	}
}
