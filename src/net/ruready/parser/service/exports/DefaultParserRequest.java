/*******************************************************************************
 * Source File: DefaultParserRequest.java
 ******************************************************************************/
package net.ruready.parser.service.exports;

import net.ruready.common.chain.DefaultChainRequest;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultParserRequest extends DefaultChainRequest implements
		ParserRequest
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(DefaultParserRequest.class);

	// ========================= FIELDS ====================================

	// Parser control options
	private ParserOptions options;

	// Input string
	private String inputString;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser request.
	 * 
	 * @param options
	 *            parser control options
	 * @param inputString
	 *            string to be parsed
	 */
	public DefaultParserRequest(ParserOptions options, String inputString)
	{
		super();
		this.options = options;
		this.inputString = inputString;
	}

	// ========================= IMPLEMENTATION: Object =============

	// ========================= IMPLEMENTATION: ParserRequest =============

	/**
	 * @return the inputString
	 */
	public String getInputString()
	{
		return inputString;
	}

	/**
	 * @param inputString
	 *            the inputString to set
	 */
	public void setInputString(String inputString)
	{
		this.inputString = inputString;
	}

	/**
	 * @return the options
	 */
	public ParserOptions getOptions()
	{
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(ParserOptions options)
	{
		this.options = options;
	}

}
