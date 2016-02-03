/*****************************************************************************************
 * Source File: TestOps.java
 ****************************************************************************************/
package test.ruready.parser.arithmetic;

import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;

/**
 * Test procedures related to the arithmetic parser (tokenizer, arithmetic
 * compiler + matcher) on strings.
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
public class TestOps extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TestOps.class);

	// ========================= IMPLEMENTATION: TestBase ============

	// ========================= TESTING ===================================

	/**
	 * Test the number of entities (unary and binary operation and function
	 * entities).
	 */
	@Test
	public void testEntities()
	{
		logger.info("# unary operations  " + UnaryOperation.values().length);
		logger.info("# binary operations " + BinaryOperation.values().length);
		logger.info("# binary functions  " + BinaryFunction.values().length);

		Assert.assertEquals(31, UnaryOperation.values().length);
		Assert.assertEquals(6, BinaryOperation.values().length);
		Assert.assertEquals(3, BinaryFunction.values().length);
	}
}
