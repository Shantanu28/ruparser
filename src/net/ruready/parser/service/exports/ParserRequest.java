/*******************************************************
 * Source File: ParserRequest.java
 *******************************************************/
package net.ruready.parser.service.exports;

import net.ruready.common.chain.ChainRequest;
import net.ruready.parser.options.exports.ParserOptions;

/**
 * A parser request that holds parser options in addition to standard chain
 * request attributes.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 15, 2007
 */
public interface ParserRequest extends ChainRequest
{
	/**
	 * Returns the parser options.
	 * 
	 * @return an <code>Object</code> containing parser control options
	 */
	ParserOptions getOptions();

	/**
	 * Set new parser options.
	 * 
	 * @param options
	 *            the options to set
	 */
	void setOptions(ParserOptions options);

	/**
	 * Returns the input string to be parsered.
	 * 
	 * @return an input <code>String</code> to be parsed
	 */
	String getInputString();

	/**
	 * Set a new input string.
	 * 
	 * @param inputString
	 *            an input <code>String</code> to be parsed
	 */
	void setInputString(String inputString);

}
