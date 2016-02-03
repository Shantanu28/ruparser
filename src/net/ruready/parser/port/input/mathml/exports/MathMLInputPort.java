/*******************************************************
 * Source File: MathMLInputPort.java
 *******************************************************/
package net.ruready.parser.port.input.mathml.exports;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.port.input.exports.ParserInputPort;
import net.ruready.parser.port.input.mathml.entity.MathML2MathParserConverter;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A parser input port that converts MathML content input into a string that can
 * be parsed by the math parser.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 16, 2007
 */
public class MathMLInputPort extends ParserInputPort
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathMLInputPort.class);

	// ========================= FIELDS ====================================

	// String to be converted
	private String mathMLString;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a MathML input handler.
	 * 
	 * @param mathMLString
	 *            MathML content string to be converted into parser input
	 */
	public MathMLInputPort(String mathMLString)
	{
		super();
		this.mathMLString = mathMLString;
	}

	/**
	 * Initialize MathML input handler.
	 * 
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the
	 *            request processing chain will terminate after this handler
	 *            handles the request.
	 */
	public MathMLInputPort(RequestHandler nextNode)
	{
		super(nextNode);
	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return ParserNames.HANDLER.PORT.INPUT + CommonNames.MISC.TAB_CHAR + "MathML";
	}

	// ========================= IMPLEMENTATION: PrinterInputPort ==========

	/**
	 * Construct the math parser input string bu invoking the MathML content
	 * converter's <code>convert()</code> method.
	 * 
	 * @see net.ruready.parser.port.input.exports.ParserInputPort#getInput()
	 */
	@Override
	protected String getInput()
	{
		// If this is a null or empty string, return an empty, non-null string
		if (TextUtil.isEmptyTrimmedString(mathMLString)) {
			return CommonNames.MISC.EMPTY_STRING;
		}
		MathML2MathParserConverter converter = new MathML2MathParserConverter(this.getOptions());
		return converter.convert(mathMLString);
	}
}
