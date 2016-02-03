/*****************************************************************************************
 * Source File: TestTreeInfo.java
 ****************************************************************************************/
package test.ruready.parser.marker;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.atpm.manager.TreeInfoR1;
import net.ruready.parser.atpm.manager.TreeInfoVisitor;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * A temporary test suite for the <code>TreeInfoR1</code> bean. Once this bean
 * type is removed, we can get rid of these tests or incoporate them into other
 * tests.
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
public class TestTreeInfo extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestTreeInfo.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test the tree info bean using a data file.
	 */
	public void xxxtestFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestElementMarkerFromFile(
				TestingNames.FILE.PARSER.MARKER.ELEMENT, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	/**
	 * Test some hard-coded tree beans.
	 */
	private void testHardcodedData(String treeString, SyntaxTreeNode tree,
			List<SyntaxTreeNode> tokens)
	{
		logger.info("========= Testing Tree Info Bean =========");
		TreeInfoR1<MathToken, SyntaxTreeNode> treeInfo = new TreeInfoR1<MathToken, SyntaxTreeNode>(
				treeString, tokens);
		TreeInfoVisitor<MathToken, SyntaxTreeNode> treeInfoVisitor = new TreeInfoVisitor<MathToken, SyntaxTreeNode>(
				tree);
		logger.info("treeString " + treeString);
		logger.info("tree " + tree);
		logger.info(treeInfo.toString());
		logger.info(treeInfoVisitor.toString());
		Assert.assertEquals(treeInfo, treeInfoVisitor);
	}

	/**
	 * Test some hard-coded tree beans.
	 */
	@Test
	public void testHardcodedData()
	{
		final int dummy = CommonNames.MISC.INVALID_VALUE_INTEGER;
		{
			String treeString = "{+{*{2}{34567}{4}}{1}{3}}";

			List<MathToken> tokens = new ArrayList<MathToken>();
			tokens.add(new MathToken(dummy, BinaryOperation.PLUS));
			tokens.add(new MathToken(dummy, BinaryOperation.TIMES));
			tokens.add(new MathToken(dummy, new ComplexValue(2.0)));
			tokens.add(new MathToken(dummy, new ComplexValue(34567.0)));
			tokens.add(new MathToken(dummy, new ComplexValue(4.0)));
			tokens.add(new MathToken(dummy, new ComplexValue(1.0)));
			tokens.add(new MathToken(dummy, new ComplexValue(3.0)));

			SyntaxTreeNode tree = new SyntaxTreeNode(tokens.get(0));
			SyntaxTreeNode branch0 = new SyntaxTreeNode(tokens.get(1));
			SyntaxTreeNode branch00 = new SyntaxTreeNode(tokens.get(2));
			branch0.addChild(branch00);
			SyntaxTreeNode branch01 = new SyntaxTreeNode(tokens.get(3));
			branch0.addChild(branch01);
			SyntaxTreeNode branch02 = new SyntaxTreeNode(tokens.get(4));
			branch0.addChild(branch02);
			tree.addChild(branch0);
			SyntaxTreeNode branch1 = new SyntaxTreeNode(tokens.get(5));
			tree.addChild(branch1);
			SyntaxTreeNode branch2 = new SyntaxTreeNode(tokens.get(6));
			tree.addChild(branch2);

			// Prepare node list ordered by original assembly element apperance
			List<SyntaxTreeNode> nodes = new ArrayList<SyntaxTreeNode>();
			nodes.add(tree);
			nodes.add(branch0);
			nodes.add(branch00);
			nodes.add(branch01);
			nodes.add(branch02);
			nodes.add(branch1);
			nodes.add(branch2);

			this.testHardcodedData(treeString, tree, nodes);
		}
		//		
		// {
		// String treeString = "{12{3}{45}}";
		// List<MathToken> tokens = new ArrayList<MathToken>();
		// final int dummy = dummy;
		// tokens.add(new MathToken(dummy, new ComplexValue(12.0)));
		// tokens.add(new MathToken(dummy, new ComplexValue(3.0)));
		// tokens.add(new MathToken(dummy, new ComplexValue(4.0)));
		// this.testHardcodedData(treeString, tokens);
		// }
	}

	// ========================= PRIVATE METHODS ===========================
}
