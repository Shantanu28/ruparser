/*****************************************************************************************
 * Source File: TextPrinter.java
 ****************************************************************************************/
package net.ruready.parser.port.output.text.manager;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.port.output.manager.TargetPrinter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a marked syntax tree within a target into a highlighted text string.
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
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Sep 8, 2007
 */
public abstract class TextPrinter implements TargetPrinter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TextPrinter.class);

	// ========================= FIELDS ====================================

	// arithmetic target object containing a syntax tree and a list of
	// extraneous math tokens
	private MathTarget target;

	// Parser control options
	private ParserOptions options;

	// List of text snippets corresponding to the target's token list
	private List<StringBuffer> snippets = new ArrayList<StringBuffer>();

	// The generated output string
	private ByteArrayOutputStream output;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an tree printer.
	 * 
	 * @param target
	 *            arithmetic target object containing a syntax tree and a list
	 *            of extraneous tokens
	 * @param options
	 *            Parser control options
	 */
	public TextPrinter(MathTarget target, ParserOptions options)
	{
		super();
		this.target = target;
		this.options = options;
		this.buildOutput();
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Generate a text snippet for a token marked with a certain status. The
	 * snippet wraps the token with a span tag with class name
	 * "someFixedPrefix_status.name().toLowerCase()".
	 * 
	 * @param token
	 *            original assembly token
	 * @param status
	 *            math token status
	 * @return text snippet
	 */
	abstract protected StringBuffer buildSnippet(Token token,
			MathTokenStatus status);

	/**
	 * Generate a text snippet for a token marked with a certain status. The
	 * snippet wraps the token with a span tag with class name
	 * "someFixedPrefix_status.name().toLowerCase()".
	 * 
	 * @param token
	 *            original assembly token
	 * @param status
	 *            math token status
	 * @return text snippet
	 */
	abstract protected ByteArrayOutputStream buildOutputFromSnippet(
			List<StringBuffer> textSnippets);

	// ========================= IMPLEMENTATION: TargetPrinter =============

	/**
	 * @return the output stream
	 */
	final public OutputStream getOutput()
	{
		return output;
	}

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Generate a "highlighted" text string corresponding to the syntax tree. We
	 * loop over the list of math tokens and extraneous tokens, and add a
	 * highlight to a corresponding list of tokens HTML snippets, based on math
	 * token stati. After processing all tokens, we concatenate the snippets
	 * into the final string. This is a template method.
	 */
	@SuppressWarnings("unused")
	final private void buildOutput()
	{
		// logger.debug("generateString()");

		// Useful references
		List<Token> tokens = target.getTokens();
		List<MathToken> mathTokens = target.toMathTokenArray();
		// Add all extraneous tokens in the list
		mathTokens.addAll(target.getExtraneous());

		// First, set all snippets to empty strings
		for (Token token : tokens)
		{
			snippets.add(TextUtil.emptyStringBuffer());
		}

		// Loop over math tokens and set the corresponding tokens' snippets
		// according to the math tokens' stati
		// logger.debug("tokens " + tokens);
		// logger.debug("mathTokens " + mathTokens);
		for (MathToken mt : mathTokens)
		{
			MathTokenStatus status = mt.getStatus();
			// Process only non-fictitious tokens
			if (status != MathTokenStatus.FICTITIOUS_CORRECT)
			{
				for (Integer index : mt.getElements())
				{
					// If the math token is numerical, round tokens to the
					// precision specified by the options, unless it is a
					// mathematical constant (display its symbol in that case).
					// logger.debug("index " + index + " mt " + mt + "
					// numerical? "
					// + mt.isNumerical());
					Token token = (mt.isNumerical() && (mt.getValueID() != MathValueID.ARITHMETIC_CONSTANT)) ? new Token(
							options.format((NumericalValue) mt.getValue()))
							: tokens.get(index);
					StringBuffer snippet = this.buildSnippet(token, status);
					snippets.set(index, snippet);
				}
			}
		}

		// Fill in the remaining empty snippets for discarded tokens
		MathTokenStatus status = MathTokenStatus.DISCARDED;
		for (int index = 0; index < snippets.size(); index++)
		{
			if (snippets.get(index).length() == 0)
			{
				Token token = tokens.get(index);
				StringBuffer newSnippet = this.buildSnippet(token, status);
				snippets.set(index, newSnippet);
			}
		}

		// Concatenate the snippets into the final string
		output = this.buildOutputFromSnippet(snippets);
	}
	// ========================= GETTERS & SETTERS =========================

}
