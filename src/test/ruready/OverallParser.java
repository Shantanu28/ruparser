/*****************************************************************************************
 * Source File: OverallParser.java
 ****************************************************************************************/
package test.ruready;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.ruready.parser.absolute.SuiteAbsoluteCanonicalizer;
import test.ruready.parser.analysis.SuiteAnalysis;
import test.ruready.parser.arithmetic.SuiteArithmetic;
import test.ruready.parser.converter.SuiteTree;
import test.ruready.parser.evaluation.SuiteEvaluator;
import test.ruready.parser.exports.SuiteExports;
import test.ruready.parser.logical.SuiteLogical;
import test.ruready.parser.marker.SuiteMarker;
import test.ruready.parser.math.SuiteMath;
import test.ruready.parser.param.SuiteParam;
import test.ruready.parser.port.SuitePort;
import test.ruready.parser.range.SuiteRange;
import test.ruready.parser.relative.SuiteRelativeCanonicalizer;
import test.ruready.parser.tokenizer.SuiteTokenizer;

/**
 * A test suite that includes all tests in the parser component.
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
 * @version Aug 9, 2007
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{ SuiteAbsoluteCanonicalizer.class, SuiteAnalysis.class, SuiteMarker.class,
		SuiteArithmetic.class, SuiteExports.class, /* SuitePretty.class, */
		SuiteEvaluator.class, SuiteParam.class, SuiteRange.class,
		SuiteLogical.class, SuiteMath.class, SuitePort.class,
		SuiteRelativeCanonicalizer.class, SuiteTokenizer.class, SuiteTree.class })
public class OverallParser
{
}
