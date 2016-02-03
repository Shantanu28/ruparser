/*****************************************************************************************
 * Source File: TreeTokenizer.java
 ****************************************************************************************/
package net.ruready.parser.tree.exports;

import net.ruready.common.parser.core.tokens.Tokenizer;

/**
 * A tokenizer with a custom symbol state to propely recognize syntax tree tokens.
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
class TreeTokenizer extends Tokenizer
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs a tokenizer with a default state table (as described in the class
	 * comment).
	 * 
	 * @param symbolState
	 *            customized symbol state
	 */
	public TreeTokenizer()
	{
		// Do the rest of Tokenizer::Tokenizer()
		// setCharacterState(0, 255, symbolState()); // the default
		setCharacterState(0, 255, wordState()); // the default

		setCharacterState(0, ' ', whitespaceState());
		setCharacterState('a', 'z', wordState());
		setCharacterState('A', 'Z', wordState());
		setCharacterState(0xc0, 0xff, wordState());
		setCharacterState('0', '9', numberState());
		setCharacterState('-', '-', numberState());
		setCharacterState('.', '.', numberState());
		setCharacterState('"', '"', quoteState());
		setCharacterState('\'', '\'', quoteState());
		setCharacterState('/', '/', slashState());
	}

	/**
	 * Constructs a tokenizer to read from the supplied string.
	 * 
	 * @param symbolState
	 *            customized symbol state
	 * @param s
	 *            the string to read from
	 */
	public TreeTokenizer(String s)
	{
		setString(s);
	}

}
