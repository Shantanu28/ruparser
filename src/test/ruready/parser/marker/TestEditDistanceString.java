/*****************************************************************************************
 * Source File: TestEditDistanceString.java
 ****************************************************************************************/
package test.ruready.parser.marker;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.junit.exports.TestFileReader;
import net.ruready.common.math.real.RealUtil;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.common.tree.ListTreeNode;
import net.ruready.parser.atpm.entity.NodeMapping;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.atpm.manager.NodeComparisonCost;
import net.ruready.parser.atpm.manager.ShashaEditDistanceComputer;
import net.ruready.parser.atpm.manager.SimpleNodeComparisonCost;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;
import test.ruready.parser.rl.TestingNames;

/**
 * Test the edit distance computation and nodal mapping generation algorithm.
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
public class TestEditDistanceString extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestEditDistanceString.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test edit distance using a data file.
	 */
	@Test
	public void testFromFile()
	{
		// Add default options here
		ParserOptions options = new ParserOptions();
		options.addSymbolicVariable("x");
		options.addSymbolicVariable("y");
		options.addSymbolicVariable("z");

		TestFileReader t = new TestEditDistanceStringFromFile(
				TestingNames.FILE.PARSER.ATPM.EDIT_DISTANCE_STRING, options);
		t.test();

		Assert.assertEquals(0, t.getNumErrors());
	}

	/**
	 * Test some hard-coded tree beans.
	 */
	private void testHardcodedData(ListTreeNode<String> referenceTree,
			ListTreeNode<String> responseTree, double expectedEditDistance,
			NodeMapping<String, ListTreeNode<String>> expectedMapping)
	{
		// Set printer options
		// referenceTree.setPrinter(new TraversalPrinter(referenceTree,
		// Names.TREE.BRACKET_OPEN,
		// Names.TREE.BRACKET_CLOSE, CommonNames.MISC.EMPTY_STRING,
		// CommonNames.MISC.EMPTY_STRING, true, false, true));
		// responseTree.setPrinter(new TraversalPrinter(responseTree,
		// Names.TREE.BRACKET_OPEN,
		// Names.TREE.BRACKET_CLOSE, CommonNames.MISC.EMPTY_STRING,
		// CommonNames.MISC.EMPTY_STRING, true, false, true));
		logger.info("referenceTree\n" + referenceTree);
		logger.info("responseTree\n" + responseTree);

		NodeComparisonCost<String> costComputer = new SimpleNodeComparisonCost<String>();
		EditDistanceComputer<String, ListTreeNode<String>> e = new ShashaEditDistanceComputer<String, ListTreeNode<String>>(
				referenceTree, responseTree, costComputer);

		logger.debug("editDistance " + e.getEditDistance());
		logger.debug("mapping:\n" + e);

		Assert.assertEquals(expectedEditDistance, e.getEditDistance(),
				RealUtil.MACHINE_DOUBLE_ERROR);
		Assert.assertEquals(expectedMapping, e.getMapping());

		StringBuffer s = TextUtil.emptyStringBuffer();
		logger.debug("Subtree distance matrix = ");
		s.append(CommonNames.MISC.NEW_LINE_CHAR);
		for (int i = 1; i <= e.getReferenceTreeSize(); i++)
		{
			for (int j = 1; j <= e.getResponseTreeSize(); j++)
			{
				s.append(e.getSubTreeEditDistance(i, j)).append(
						CommonNames.MUNKRES.SEPARATOR);
			}
			s.append(CommonNames.MISC.NEW_LINE_CHAR);
		}
		logger.debug(s);
	}

	/**
	 * Test some hard-coded tree beans.
	 */
	@Test
	public void testHardcodedData()
	{
		final int dummy = CommonNames.MISC.INVALID_VALUE_INTEGER;
		{
			// Source: http://www.cis.njit.edu/~discdb/tmatch/source/tmatch.cgi
			// and http://web.njit.edu/~wangj/tmatch.html
			// and http://www.cis.njit.edu/~discdb/tmatch/demo.html
			// for demo of Sasha et al.'s ATPM algorithm.
			//
			// We'll use their example:
			// reference tree: {N{M{M{I{H}}{H}{I}}{B}}} (original example's
			// syntax)
			// response tree : {H{M{B}{I}{M{I}{H}{I}}}} (original example's
			// syntax)

			// Correct result:
			// Edit distance = 5.0
			// Nodal mapping:
			// ( 0 B )
			// ( 1 I )
			// ( 0 H )
			// ( 1 I 2 I )
			// ( 2 H 3 H )
			// ( 3 I 4 I )
			// ( 4 M 5 M )
			// ( 5 B )
			// ( 6 M 6 M )
			// ( 7 N 7 H )

			List<String> referenceNodes = new ArrayList<String>();
			// Nodes in post-traversal order
			// line with source
			referenceNodes.add("H");
			referenceNodes.add("I");
			referenceNodes.add("H");
			referenceNodes.add("I");
			referenceNodes.add("M");
			referenceNodes.add("B");
			referenceNodes.add("M");
			referenceNodes.add("N");
			List<ListTreeNode<String>> nodes = new ArrayList<ListTreeNode<String>>();
			for (String data : referenceNodes)
			{
				nodes.add(new ListTreeNode<String>(data));
			}
			nodes.get(1).addChild(nodes.get(0));
			nodes.get(4).addChild(nodes.get(1));
			nodes.get(4).addChild(nodes.get(2));
			nodes.get(4).addChild(nodes.get(3));
			nodes.get(6).addChild(nodes.get(4));
			nodes.get(6).addChild(nodes.get(5));
			nodes.get(7).addChild(nodes.get(6));
			ListTreeNode<String> referenceTree = nodes.get(nodes.size() - 1);

			List<String> responseNodes = new ArrayList<String>();
			// Nodes in post-traversal order
			// line with source
			responseNodes.add("B");
			responseNodes.add("I");
			responseNodes.add("I");
			responseNodes.add("H");
			responseNodes.add("I");
			responseNodes.add("M");
			responseNodes.add("M");
			responseNodes.add("H");
			nodes = new ArrayList<ListTreeNode<String>>();
			for (String data : responseNodes)
			{
				nodes.add(new ListTreeNode<String>(data));
			}
			// response tree : {H{M{B}{I}{M{I}{H}{I}}}}
			nodes.get(6).addChild(nodes.get(0));
			nodes.get(6).addChild(nodes.get(1));
			nodes.get(5).addChild(nodes.get(2));
			nodes.get(5).addChild(nodes.get(3));
			nodes.get(5).addChild(nodes.get(4));
			nodes.get(6).addChild(nodes.get(5));
			nodes.get(7).addChild(nodes.get(6));
			ListTreeNode<String> responseTree = nodes.get(nodes.size() - 1);

			final double expectedEditDistance = 5.0;
			// Ignoring left and right nodes in mapping elements (set to null).
			// Node do not participate in mapping comparisons.
			final NodeMapping<String, ListTreeNode<String>> expectedMapping = new NodeMapping<String, ListTreeNode<String>>();
			expectedMapping.add(dummy, null, null, 0, responseNodes.get(0),
					null);
			expectedMapping.add(dummy, null, null, 1, responseNodes.get(1),
					null);
			expectedMapping.add(0, referenceNodes.get(0), null, dummy, null,
					null);
			expectedMapping.add(1, referenceNodes.get(1), null, 2,
					responseNodes.get(2), null);
			expectedMapping.add(2, referenceNodes.get(2), null, 3,
					responseNodes.get(3), null);
			expectedMapping.add(3, referenceNodes.get(3), null, 4,
					responseNodes.get(4), null);
			expectedMapping.add(4, referenceNodes.get(4), null, 5,
					responseNodes.get(5), null);
			expectedMapping.add(5, referenceNodes.get(5), null, dummy, null,
					null);
			expectedMapping.add(6, referenceNodes.get(6), null, 6,
					responseNodes.get(6), null);
			expectedMapping.add(7, referenceNodes.get(7), null, 7,
					responseNodes.get(7), null);

			this.testHardcodedData(referenceTree, responseTree,
					expectedEditDistance, expectedMapping);
		}

	}

	// ========================= PRIVATE METHODS ===========================
}
