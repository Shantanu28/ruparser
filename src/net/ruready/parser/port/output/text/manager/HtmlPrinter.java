/*****************************************************************************************
 * Source File: HtmlPrinter.java
 ****************************************************************************************/
package net.ruready.parser.port.output.text.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ruready.common.exception.ApplicationException;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.util.XmlUtil;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a marked syntax tree into a highlighted HTML string.
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
public class HtmlPrinter extends TextPrinter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(HtmlPrinter.class);

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
	public HtmlPrinter(MathTarget target, ParserOptions options)
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
					output.write(ParserNames.PORT.OUTPUT.HTML.SEPARATOR.toString()
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
		String tag = "span";
		Map<String, String> attributes = new HashMap<String, String>();
		String className =
				ParserNames.PORT.OUTPUT.HTML.MATH_TOKEN_STATUS_PREFIX
						+ status.name().toLowerCase();
		attributes.put("class", className);
		StringBuffer buffer = XmlUtil.openTag(tag, attributes);
		buffer.append(token);
		buffer.append(XmlUtil.closeTag(tag));
		return buffer;
	}

	// ========================= PRIVATE METHODS ===========================

	// ========================= GETTERS & SETTERS =========================

}
