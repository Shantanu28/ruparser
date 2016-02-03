/*****************************************************************************************
 * Source File: MathAssembly.java
 ****************************************************************************************/
package net.ruready.parser.tokenizer.entity;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.parser.core.tokens.TokenAssembly;
import net.ruready.common.parser.core.tokens.TokenString;
import net.ruready.common.parser.core.tokens.Tokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link MathAssembly} is a tweaked {@link Assembly} whose elements are Tokens. Tokens
 * are, roughly, the chunks of text that a {@link Tokenizer} returns. An
 * elementsConsumedAll() method is added to account for "implicit tokens" in the string,
 * of which we keep track in the "implicitTokens" field.
 * <p>
 * WARNING: do not use unget() on this type of Assembly unless you manually tweak the
 * implicitTokens counter.
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
public class MathAssembly extends TokenAssembly
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathAssembly.class);

	// ========================= FIELDS ====================================

	// # implicit tokens encountered during consumption
	private int implicitTokens = 0;

	// ========================= CONSTRUCTORS ==============================
	/**
	 * Constructs a MathAssembly on a TokenString constructed from the given String.
	 * 
	 * @param string
	 *            the string to consume
	 * @return a MathAssembly that will consume a tokenized version of the supplied String
	 */
	public MathAssembly(String s)
	{
		super(s);
	}

	/**
	 * Constructs a MathAssembly on a TokenString constructed from the given Tokenizer.
	 * 
	 * @param Tokenizer
	 *            the tokenizer to consume tokens from
	 * @return a MathAssembly that will consume a tokenized version of the supplied
	 *         Tokenizer
	 */
	public MathAssembly(Tokenizer t)
	{
		super(t);
	}

	/**
	 * Constructs a MathAssembly from the given TokenString.
	 * 
	 * @param tokenString
	 *            the tokenString to consume
	 * @return a MathAssembly that will consume the supplied TokenString
	 */
	public MathAssembly(TokenString tokenString)
	{
		super(tokenString);
	}

	// ========================= METHODS ===================================

	/**
	 * Returns the number of elements that have been consumed, including implicit tokens
	 * <p>
	 * elementsConsumedAll() = elementsConsumedAll() + getImplicitTokens().
	 * 
	 * @return the number of elements that have been consumed, including implicit tokens
	 */
	public int elementsConsumedAll()
	{
		return index + implicitTokens;
	}

	/**
	 * Increment the number of implicit tokens encountered.
	 */
	public void incrementImplicitTokens()
	{
		implicitTokens++;
	}

	/**
	 * Decrement the number of implicit tokens encountered.
	 */
	public void decrementImplicitTokens()
	{
		implicitTokens--;
	}

	/**
	 * Return the list of tokens in the internal {@link TokenString} by their order of
	 * apperance in the internal token array.
	 * 
	 * @return list of tokens in the internal {@link TokenString} by their order of
	 *         apperance in the internal token array.
	 */
	public List<Token> toTokenArray()
	{
		List<Token> tokens = new ArrayList<Token>();
		for (int i = 0; i < tokenString.length(); i++)
		{
			Token token = tokenString.tokenAt(i);
			tokens.add(token);
		}
		return tokens;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return Returns the implicitTokens.
	 */
	public int getImplicitTokens()
	{
		return implicitTokens;
	}

}
