/*****************************************************************************************
 * Source File: DataAssembler.java
 ****************************************************************************************/
package net.ruready.parser.tree.word;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler that pops a single tree node data from the assembly's stack and processes
 * it.
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
 * @version Sep 8, 2007
 */
class DataAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(DataAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Replace the top token in the stack with the token's Double value. Add the number to
	 * the target's lists of expression parts.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 * @see net.ruready.common.parser.core.assembler.Assembler#workOn(net.ruready.common.parser.core.entity.Assembly)
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		// There are two cases when popping node datat okens from the assembly's
		// stack:
		// 1) status, value
		// 2) value
		// This would require finding the type of token popped from the
		// assembly. A better solution is to do nothing here, and have other
		// package-private assemblers prepare everything.

		// Do nothing!
		// logger.debug("end , a=" + a);
	}
}
