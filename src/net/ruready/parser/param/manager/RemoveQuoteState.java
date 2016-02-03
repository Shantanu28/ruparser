package net.ruready.parser.param.manager;

import java.io.IOException;
import java.io.PushbackReader;

import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.common.parser.core.tokens.TokenizerState;
import net.ruready.parser.math.entity.FictitiousMathToken;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A quoteState returns a quoted string token from a reader. This state will collect
 * characters until it sees a match to the character that the tokenizer used to switch to
 * this state. For example, if a tokenizer uses a double- quote character to enter this
 * state, then <code>
 * nextToken()</code> will search for another double-quote until it
 * finds one or finds the end of the reader. This state will remove the quotes before and
 * after the string.
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
class RemoveQuoteState extends TokenizerState
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(FictitiousMathToken.class);

	// ========================= FIELDS ====================================

	// Working character buffer
	private char charbuf[] = new char[16];

	// ========================= CONSTRUCTORS ==============================

	// ========================= Implementation: TokenizerState ============

	/**
	 * Return a quoted string token from a reader. This method will collect characters
	 * until it sees a match to the character that the tokenizer used to switch to this
	 * state.
	 * 
	 * @param r
	 * @param cin
	 * @param t
	 * @return a quoted string token from a reader
	 * @throws IOException
	 * @see net.ruready.common.parser.core.tokens.TokenizerState#nextToken(java.io.PushbackReader,
	 *      int, net.ruready.common.parser.core.tokens.Tokenizer)
	 */
	@Override
	public Token nextToken(PushbackReader r, int cin, Tokenizer t) throws IOException
	{
		int i = 0;
		// Do not add "cin" to "charbuf", unlike QuoteState
		// charbuf[i++] = (char) cin;
		int c;
		do
		{
			c = r.read();
			if (c < 0)
			{
				c = cin;
			}
			checkBufLength(i);
			// Do not add "cin" to "charbuf", unlike QuoteState
			if (c != cin)
			{
				charbuf[i++] = (char) c;
			}
		}
		while (c != cin);

		String sval = String.copyValueOf(charbuf, 0, i);
		return new Token(Token.TT_QUOTED, sval, 0);
		// return new Token(Token.TT_WORD, sval, 0);
	}

	// ========================= METHODS ===================================

	/**
	 * Fatten up charbuf as necessary.
	 * 
	 * @param i
	 */
	protected void checkBufLength(int i)
	{
		if (i >= charbuf.length)
		{
			char nb[] = new char[charbuf.length * 2];
			System.arraycopy(charbuf, 0, nb, 0, charbuf.length);
			charbuf = nb;
		}
	}

}
