/*****************************************************************************************
 * Source File: TestOps.java
 ****************************************************************************************/
package test.ruready.parser.logical;

import net.ruready.common.util.LogicalUtil;
import net.ruready.parser.logical.entity.value.RelationOperation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;

/**
 * Test procedures related to the logical parser (tokenizer, logical compiler +
 * matcher) on strings.
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

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= TESTING ===================================

	/**
	 * Test the number of entities (unary and binary operation and function
	 * entities).
	 */
	@Test
	public void testEntities()
	{
		logger.info("# relations         " + RelationOperation.values().length);
		Assert.assertEquals(8, RelationOperation.values().length);
	}

	/**
	 * Test the axioms assumed by the methods of relational operations (an
	 * axiomatic approach to testing the correctness of relations' method).
	 */
	@Test
	public void testRules()
	{
		logger.info("Testing the axiom: !hasInverse() ==> isCanonical()");
		for (RelationOperation r : RelationOperation.values())
		{
			Assert.assertEquals(false, !r.hasInverse() && !r.isCanonical());
		}

		for (RelationOperation r : RelationOperation.values())
		{
			logger.debug("Testing operation " + r + "...");

			// If the operation does not have an inverse, it is always in
			// canonical
			// form.
			logger.debug("Axiom: !r.hasInverse() ==> r.isCanonical()");
			Assert.assertEquals(true, LogicalUtil.implies(!r.hasInverse(), r
					.isCanonical()));
			// If the operation has an inverse, then:

			// If it equals its inverse, it is in canonical form.
			logger
					.debug("Axiom: r.hasInverse() && (r == r.inverse()) ==> r.isCanonical()");
			Assert.assertEquals(true, LogicalUtil.implies(r.hasInverse()
					&& (r == r.inverse()), r.isCanonical()));

			// If it does not equal its inverse, either it is in canonical form,
			// or
			// its inverse is, but not both
			logger
					.debug("Axiom: r.hasInverse() && (r != r.inverse()) ==> r.isCanonical() ^ r.inverse().isCanonical()");
			if (r.hasInverse())
			{
				// The if statement bypasses a NPE on r.inverse().isCanonical()
				// for
				// some cases where the "if" part is false, hence the
				// implication
				// is true and we can skip this check.
				Assert.assertEquals(true, LogicalUtil.implies(r.hasInverse()
						&& (r != r.inverse()), r.isCanonical()
						^ r.inverse().isCanonical()));
			}
		}

	}
}
