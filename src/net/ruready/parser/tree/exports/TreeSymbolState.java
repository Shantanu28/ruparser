/*****************************************************************************************
 * Source File: TreeSymbolState.java
 ****************************************************************************************/
package net.ruready.parser.tree.exports;

import net.ruready.common.parser.core.tokens.SymbolState;

/**
 * Identical to Metzker's {@link SymbolState} except that it does not recognize ":-" as a
 * symbol.
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
class TreeSymbolState extends SymbolState
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs a symbol state with a default idea of what multi-character symbols to
	 * accept (as described in the class comment). Does not recognize ":-" as a symbol.
	 * 
	 * @return a state for recognizing standard symbol except ":-".
	 */
	public TreeSymbolState()
	{
		// Make sure base class doesn't initialize anything
		super(new Double(0.0));
		add("!=");
		// add(":-");
		add("<=");
		add(">=");
	}
}
