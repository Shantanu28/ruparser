/*****************************************************************************************
 * Source File: TestSyntaxTreeNodeParser.java
 ****************************************************************************************/
package test.ruready.parser.converter.syntax;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.ParenthesisValue;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.tree.syntax.SyntaxTreeNodeMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test syntax tree conversion to a string, and converting back to a tree using
 * the syntax tree node parser.
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
public class TestSyntaxTreeNodeParser extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestSyntaxTreeNodeParser.class);

	// ========================= FIELDS ====================================

	// Parser options used throughout the tests
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
	public void testSyntaxTreeNodeParser1()

	// 2 (number)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ComplexValue(2.0)));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser2()
	// x (variable)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new Variable("x")));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser3()
	// ( ) (parenthesis)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ParenthesisValue()));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser4()
	// sin (unary function)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.SIN));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser5()
	// + (unary function)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.PLUS));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser6()
	// - (unary function)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.MINUS));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser7()
	// * (unary function; fictitious)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.TIMES));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser8()
	// / (unary function; fictitious)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.DIVIDE));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser9()
	// log (binary function)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryFunction.LOG));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser10()
	// + (binary operation)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.PLUS));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser11()
	// - (binary operation)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.PLUS));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser12()
	// * (binary operation)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.TIMES));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser13()
	// / (binary operation)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.DIVIDE));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser14()
	// % (binary operation)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.MOD));
		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser15()
	// 2+3
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.PLUS));
		SyntaxTreeNode branch1 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ComplexValue(2.0)));
		SyntaxTreeNode branch2 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ComplexValue(3.0)));
		tree.addChild(branch1);
		tree.addChild(branch2);

		this.testTree(tree);
	}

	/**
	 * Test the tokenizer.
	 */
	@Test
	public void testSyntaxTreeNodeParser16()
	// 2*(3+4)
	{
		SyntaxTreeNode tree = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.TIMES));
		SyntaxTreeNode branch0 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ComplexValue(2.0)));
		SyntaxTreeNode branch1 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ParenthesisValue()));
		SyntaxTreeNode branch10 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, BinaryOperation.TIMES));
		SyntaxTreeNode branch100 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ComplexValue(3.0)));
		SyntaxTreeNode branch101 = new SyntaxTreeNode(new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, new ComplexValue(4.0)));
		branch10.addChild(branch100);
		branch10.addChild(branch101);
		branch1.addChild(branch10);
		tree.addChild(branch0);
		tree.addChild(branch1);
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

		TestFileReader t = new TestSyntaxTreeNodeParserFromFile(
				TestingNames.FILE.PARSER.TREE.SYNTAX.SYNTAX_PARSER, fileOptions);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * @param tree
	 */
	private void testTree(SyntaxTreeNode tree)
	{
		logger.info("=============== TESTING tree ====================");

		// Convert tree to string
		String string = tree.toString();
		logger.info("string         = '" + string + "'");

		// Convert string back to tree
		SyntaxTreeNodeMatcher matcher = new SyntaxTreeNodeMatcher(options);
		matcher.match(string);
		SyntaxTreeNode actualTree = matcher.getSyntax();

		logger.info("expected tree  = " + tree);
		logger.info("actual tree    = " + actualTree);
		logger.info("equals?       " + tree.equals(actualTree));

		Assert.assertEquals(true, tree.equals(actualTree));
	}
}
