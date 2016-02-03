/*****************************************************************************************
 * Source File: TestParametricEvaluation.java
 ****************************************************************************************/
package test.ruready.parser.range;

import net.ruready.parser.range.ParamRangeParser;
import net.ruready.parser.range.RangeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.parser.rl.ParserTestBase;

/**
 * Test the parsing of parameter range strings with {@link ParamRangeParser}.
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
 * @version Jul 27, 2007
 */
public class TestParamRangeParser extends ParserTestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestParamRangeParser.class);

	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: ParserTestBase ============

	// ========================= PUBLIC METHODS ============================

	// /**
	// * Test the arithmetic setup and evaluation using a data file.
	// */
	// @Ignore
	// public void testFromFile()
	// {
	// TestFileReader t = new TestParametricEvaluationFromFile(
	// TestingNames.FILE.PARSER.PARAMETRIC_EVALUATION.PARAMETRIC_EVALUATION);
	// t.test();
	// Assert.assertEquals(0, t.getNumErrors());
	// }

	/**
	 * Parse some hard-coded strings representing parameter ranges.
	 */
	@Test
	public void testHardcodedStrings()
	{
		// Input strings representing RangeMaps
		String[] testStrings =
		{ "d 1:2", "a 1:5   b 2,3,-4.5 c 5,8,5  ", "df -2:2",
				"d 1:5 b 2,3,-4.5 c 5,8,5", };

		// Expected toString() of the parsed RangeMaps. Variables
		// and discrete ranges will be sorted by ascending order;
		// also, RangeMap.toString() trims the string representation of the map.
		String[] expectedStrings =
		{ "d 1:2", "a 1:5 b -4.5,2,3 c 5,8", "df -2:2",
				"b -4.5,2,3 c 5,8 d 1:5", };

		// Run the test
		for (int i = 0; i < testStrings.length; i++)
		{
			logger.debug("############### TEST #" + i + " ##############");
			String originalText = testStrings[i];
			ParamRangeParser parser = new ParamRangeParser();
			parser.match(originalText);
			RangeMap paramRanges = parser.getRangeMap();
			logger.debug("Original string: " + originalText);
			logger.debug("Parsed into    : " + paramRanges);
			logger.debug("Random pick    : " + paramRanges.randomPick());
			Assert.assertEquals(expectedStrings[i], paramRanges.toString());
		}
	}

	// ========================= PRIVATE METHODS ===========================
}
