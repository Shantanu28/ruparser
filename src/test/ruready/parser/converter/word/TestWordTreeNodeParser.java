/*****************************************************************************************
 * Source File: TestWordTreeNodeParser.java
 ****************************************************************************************/
package test.ruready.parser.converter.word;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.common.tree.AsciiPrinter;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.tree.word.WordTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test syntax tree conversion to a string, and converting back to a tree of
 * word nodes, using the syntax tree node parser.
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
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Sep 26, 2007
 */
public class TestWordTreeNodeParser extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestWordTreeNodeParser.class);

	// ========================= FIELDS ====================================

	/**
	 * Parser options used throughout the tests.
	 */
	private ParserOptions options;

	// ========================= SETUP METHODS =============================

	/**
	 * @see net.ruready.common.junit.exports.TestBase#initialize()
	 */
	@Before
	public void initialize()
	{
		// Add parser options here
		options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");
	}

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testWordTreeNodeParser1()
	{
		// a:D { }

		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER,
				new ArithmeticLiteralValue("a")));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testWordTreeNodeParser2()
	{
		// a:C { }

		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER,
				new ArithmeticLiteralValue("a"), MathTokenStatus.CORRECT));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testWordTreeNodeParser3()
	{
		// a:C { b:C { } c:D { } }

		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER,
				new ArithmeticLiteralValue("a"), MathTokenStatus.CORRECT));
		tree.addChild(new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER,
				new ArithmeticLiteralValue("b"), MathTokenStatus.CORRECT)));
		tree.addChild(new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER,
				new ArithmeticLiteralValue("c"), MathTokenStatus.DISCARDED)));
		this.testTree(tree);
	}

	/**
	 * Test the element marker using a data file.
	 */
	@Test
	public void testFromFile()
	{
		// Add default options here
		ParserOptions fileOptions = new ParserOptions();
		fileOptions.addSymbolicVariable("x");
		fileOptions.addSymbolicVariable("y");
		fileOptions.addSymbolicVariable("z");

		TestFileReader t = new TestWordTreeNodeParserFromFile(
				TestingNames.FILE.PARSER.TREE.WORD.WORD_PARSER, fileOptions);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	// ========================= PRIVATE METHODS ===========================

	private void testTree(SyntaxTreeNode tree)
	{
		logger.info("=============== TESTING tree ====================");

		// Convert tree to string
		AsciiPrinter<MathToken, SyntaxTreeNode> printer = new AsciiPrinter<MathToken, SyntaxTreeNode>(
				new AsciiPrinter.NodeDataPrinter<MathToken>()
				{
					public String print(MathToken data)
					{
						// return CommonNames.MISC.EMPTY_STRING +
						// data.getValue();
						// return data.toString();
						StringBuffer s = TextUtil.emptyStringBuffer();
						s.append(data.getValue());
						s.append(CommonNames.TREE.SEPARATOR);
						s.append(data.getStatus());
						return s.toString();

					}
				});
		String string = printer.print(tree);
		logger.info("string         = '" + string + "'");

		// Convert string back to tree
		WordTreeNodeMatcher matcher = new WordTreeNodeMatcher(options);
		matcher.match(string);
		SyntaxTreeNode actualTree = matcher.getSyntax();

		logger.info("expected tree  = " + tree);
		logger.info("actual tree    = " + actualTree);
		logger.info("equals?       " + tree.equals(actualTree));

		Assert.assertEquals(true, tree.equals(actualTree));
	}
}
