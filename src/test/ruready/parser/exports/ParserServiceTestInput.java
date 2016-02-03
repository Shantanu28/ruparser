/*****************************************************************************************
 * Source File: ParserServiceTestInput.java
 ****************************************************************************************/
package test.ruready.parser.exports;

import net.ruready.common.junit.entity.TestInput;
import net.ruready.parser.options.entity.MathExpressionType;

/**
 * Parser test inputs: reference and response strings.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
public class ParserServiceTestInput implements TestInput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// Reference String's String representation
	private final String referenceString;

	// Response String's String representation
	private final String responseString;

	// Response String's String representation
	private final MathExpressionType mathExpressionType;

	// ========================= CONStringUCTORS ==============================

	/**
	 * ConStringuct a test input container from fields.
	 * 
	 * @param referenceString
	 * @param responseString
	 * @param mathExpressionType
	 */
	public ParserServiceTestInput(final String referenceString,
			final String responseString, final MathExpressionType mathExpressionType)
	{
		super();
		this.referenceString = referenceString;
		this.responseString = responseString;
		this.mathExpressionType = mathExpressionType;
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the referenceString
	 */
	public String getReferenceString()
	{
		return referenceString;
	}

	/**
	 * @return the responseString
	 */
	public String getResponseString()
	{
		return responseString;
	}

	/**
	 * @return the mathExpressionType
	 */
	public MathExpressionType getMathExpressionType()
	{
		return mathExpressionType;
	}

}
