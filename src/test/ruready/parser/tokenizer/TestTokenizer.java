/*****************************************************************************************
 * Source File: TestTokenizer.java
 ****************************************************************************************/
package test.ruready.parser.tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.parser.core.tokens.Tokenizer;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.util.ArrayUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.DefaultParserRequest;
import net.ruready.parser.service.exports.ParserRequest;
import net.ruready.parser.service.manager.TokenizerProcessor;
import net.ruready.parser.tokenizer.manager.MathTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;

/**
 * Test item's static getThisClass() method for different item types.
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
public class TestTokenizer extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestTokenizer.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testTokenizer()
	{
		String inputString = "x+5*yy-3 :=";
		String[] expectedTokens =
		{ "x", "+", "5.0", "*", "yy", "-", "3.0", ":=" };
		this.testTokenizer(inputString, expectedTokens);
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param inputString
	 * @param expectedTokens
	 */
	private void testTokenizer(String inputString, String[] expectedTokens)
	{
		// Prepare Golden standard
		List<Token> expected = new ArrayList<Token>();
		for (int i = 0; i < expectedTokens.length; i++)
		{
			expected.add(new Token(expectedTokens[i]));
		}

		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		ParserRequest request = new DefaultParserRequest(options, inputString);

		// Process request
		RequestHandler tp = new TokenizerProcessor(null);
		tp.run(request);

		// Read results
		Tokenizer tokenizer = (Tokenizer) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.TOKENIZER.TOKENIZER);
		logger.info(tokenizer);

		Assembly assembly = (Assembly) request
				.getAttribute(ParserNames.REQUEST.ATTRIBUTE.ASSEMBLY.TOKENIZER);
		logger.info("inputString = '" + inputString + "'");
		logger.debug("actual assembly " + assembly
				+ CommonNames.MISC.NEW_LINE_CHAR);

		List<Token> actual = new ArrayList<Token>();
		while (assembly.hasMoreElements())
		{
			Token t = (Token) assembly.nextElement();
			actual.add(t);
			logger.debug("token " + t + " type " + t.ttype());
		}
		logger.info("expected    = " + expected);
		logger.info("actual      = " + actual);
		logger.info("equals?       "
				+ ArrayUtil.listStringEquals(expected, actual));

		Assert.assertEquals(true, ArrayUtil.listStringEquals(expected, actual));
	}

	/**
	 * Demonstrate the math parser's customized {@link Tokenizer} class directly
	 * (not through request handlers).
	 */
	@Test
	public void testMathTokenizer() throws IOException
	{
		Set<String> variables = new HashSet<String>();
		variables.add("x");

		// Tokenizer t = new EmulatedMathTokenizer(variables);
		Tokenizer t = new MathTokenizer(variables, ArithmeticMode.COMPLEX);
		t.setString("x+5*yy-3 :=");

		// Check tokens
		Token[] expectedTokens = new Token[]
		{ new Token(Token.TT_WORD, "x", 0), new Token(Token.TT_SYMBOL, "+", 0),
				new Token(Token.TT_NUMBER, CommonNames.MISC.EMPTY_STRING, 5),
				new Token(Token.TT_SYMBOL, "*", 0),
				new Token(Token.TT_WORD, "yy", 0),
				new Token(Token.TT_SYMBOL, "-", 0),
				new Token(Token.TT_NUMBER, CommonNames.MISC.EMPTY_STRING, 3.0),
				new Token(Token.TT_SYMBOL, ":=", 0) };

		checkTokens(t, expectedTokens);
	}

	// ========================= PRIVATE METHODS ============================

	private static void checkTokens(Tokenizer t, Token[] expectedTokens)
			throws IOException
	{
		for (int j = 0; j < expectedTokens.length; j++)
		{
			Token token = t.nextToken();
			Token expectedToken = expectedTokens[j];
			if (token.equals(Token.EOF))
			{
				break;
			}
			logger.debug("token " + token + " type " + token.ttype()
					+ " expected " + expectedToken + " type "
					+ expectedToken.ttype());
			Assert.assertEquals(token.equals(expectedToken), true);
		}
	}
}
