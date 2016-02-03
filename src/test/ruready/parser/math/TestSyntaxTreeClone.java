/*****************************************************************************************
 * Source File: TestSyntaxTreeClone.java
 ****************************************************************************************/
package test.ruready.parser.math;

import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.MathTokenStatus;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;

/**
 * Test procedures related to cloning syntax trees and targets.
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
public class TestSyntaxTreeClone extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestSyntaxTreeClone.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test syntax tree cloning. We experienced problems debugging RC and the
	 * analysis phase when a cloned tree was still coupled to the original tree.
	 * This test verifies that the two trees are decoupled.
	 */
	@Test
	public void testSyntaxTreeCloning()
	{
		// Prepare the original tree; no children
		MathToken rootData = new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.PLUS);
		SyntaxTreeNode original = new SyntaxTreeNode(rootData);
		MathToken childData = new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.MINUS);
		original.addChild(new SyntaxTreeNode(childData));

		// ==============================================
		// Clone original -> copy
		// ==============================================
		SyntaxTreeNode copy = original.clone();
		// Print both trees
		logger.debug("############ Start ############");
		logger.debug("original " + original + "@"
				+ Integer.toHexString(original.hashCode()));
		logger.debug("copy     " + copy + "@"
				+ Integer.toHexString(copy.hashCode()));
		Assert.assertEquals(true, original.equals(copy));

		// ==============================================
		// Change the original's root data
		// ==============================================
		logger.debug("############ Changing root data ############");
		copy = original.clone();
		original.setData(new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
				UnaryOperation.DIVIDE));
		logger.debug("original " + original + "@"
				+ Integer.toHexString(original.hashCode()));
		logger.debug("copy     " + copy + "@"
				+ Integer.toHexString(copy.hashCode()));
		Assert.assertEquals(false, original.equals(copy));

		// ==============================================
		// Change the internals of original's root data
		// ==============================================
		logger.debug("############ Changing root data internals ############");
		copy = original.clone();
		original.getData().setStatus(MathTokenStatus.CORRECT);
		logger.debug("original " + original + "@"
				+ Integer.toHexString(original.hashCode()));
		logger.debug("copy     " + copy + "@"
				+ Integer.toHexString(copy.hashCode()));
		Assert.assertEquals(false, original.equals(copy));

		// ==============================================
		// Add a child to original
		// ==============================================
		copy = original.clone();
		MathToken childData2 = new MathToken(
				CommonNames.MISC.INVALID_VALUE_INTEGER, UnaryOperation.TIMES);
		original.addChild(new SyntaxTreeNode(childData2));

		// Print both trees
		logger.debug("############ Added child #2 to original ############");
		logger.debug("original " + original + "@"
				+ Integer.toHexString(original.hashCode()));
		logger.debug("original.children " + original.getChildren());
		logger.debug("copy.children     " + copy.getChildren());
		logger.debug("copy     " + copy + "@"
				+ Integer.toHexString(copy.hashCode()));
		// They two trees should NOT be equal
		Assert.assertEquals(false, original.equals(copy));
	}

	// ========================= PRIVATE METHODS ===========================
}
