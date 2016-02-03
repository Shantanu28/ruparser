/*****************************************************************************************
 * Source File: AsciiPrinter.java
 ****************************************************************************************/
package net.ruready.parser.port.output.text.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a marked syntax tree into a highlighted ASCII string.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9399<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Nov 1, 2007
 */
public class AsciiPrinter extends TextPrinter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AsciiPrinter.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an HTMLPrinter.
	 * 
	 * @param target
	 *            arithmetic target object containing a syntax tree and a list of
	 *            extraneous tokens
	 * @param options
	 *            Parser control options
	 */
	public AsciiPrinter(MathTarget target, ParserOptions options)
	{
		super(target, options);
	}

	// ========================= IMPLEMENTATION: TargetPrinter =============

	/**
	 * @see net.ruready.parser.port.html.manager.ImagePrinter#buildOutputFromSnippet(java.util.List)
	 */
	@Override
	protected ByteArrayOutputStream buildOutputFromSnippet(List<StringBuffer> textSnippets)
	{
		try
		{
			// Concatenate the snippets into the final string and add separators
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			for (int index = 0; index < textSnippets.size(); index++)
			{
				output.write(textSnippets.get(index).toString().getBytes());
				// Add separator
				if (index < textSnippets.size() - 1)
				{
					output.write(ParserNames.PORT.OUTPUT.ASCII.SEPARATOR.toString()
							.getBytes());
				}
			}
			return output;
		}
		catch (IOException e)
		{
			throw new ApplicationException("Could not generate HTML string, "
					+ e.toString());
		}
	}

	/**
	 * @see net.ruready.parser.port.html.manager.ImagePrinter#buildSnippet(net.ruready.parser.core.parse.tokens.Token,
	 *      net.ruready.parser.math.entity.MathTokenStatus)
	 */
	@Override
	protected StringBuffer buildSnippet(Token token, MathTokenStatus status)
	{
		StringBuffer buffer = new StringBuffer(token.toString());
		buffer.append(ParserNames.PORT.OUTPUT.ASCII.PARENTHESIS_OPEN);
		buffer.append(status);
		buffer.append(ParserNames.PORT.OUTPUT.ASCII.PARENTHESIS_CLOSE);
		return buffer;
	}

	// ========================= PRIVATE METHODS ===========================

	// ========================= GETTERS & SETTERS =========================

}
