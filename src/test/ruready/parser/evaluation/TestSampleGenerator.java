/*****************************************************************************************
 * Source File: TestSampleGenerator.java
 ****************************************************************************************/
package test.ruready.parser.evaluation;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.real.RealUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.evaluator.entity.MeshGenerator;
import net.ruready.parser.evaluator.entity.SampleGenerator;
import net.ruready.parser.evaluator.entity.UniformSampleGenerator;
import net.ruready.parser.options.entity.DefaultVariableMap;
import net.ruready.parser.options.exports.VariableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import test.ruready.common.rl.TestBase;

/**
 * Testing numerical sample generators for the parser evaluation phase.
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
 * @version Jul 16, 2007
 */
public class TestSampleGenerator extends TestBase
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(TestSampleGenerator.class);

	// ========================= IMPLEMENTATION: TestBase ==================

	// ========================= PUBLIC METHODS ============================

	/**
	 * Test uniform grid sample generator in multiple arithmetic modes.
	 */
	@Test
	public void testUniformSampleGenerator()
	{
		SampleGenerator generator = new UniformSampleGenerator(new RealValue(
				1.0), new RealValue(2.0), 6);
		List<NumericalValue> expected = new ArrayList<NumericalValue>();
		expected.add(new RealValue(1.0));
		expected.add(new RealValue(1.2));
		expected.add(new RealValue(1.4));
		expected.add(new RealValue(1.6));
		expected.add(new RealValue(1.8));
		expected.add(new RealValue(2.0));

		List<NumericalValue> actual = generator.getSamples();
		logger.info(actual);

		Assert.assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++)
		{
			Assert.assertEquals(TolerantlyComparable.EQUAL, expected.get(i)
					.tolerantlyEquals(actual.get(i),
							RealUtil.MACHINE_DOUBLE_ERROR));
		}
	}

	/**
	 * Test a mesh generator.
	 */
	@Test
	public void testMeshGenerator()
	{
		// Prepare variables
		VariableMap variables = new DefaultVariableMap();
		variables.addSymbolic("x");
		variables.addNumerical("y", new RealValue(3.0));
		variables.addSymbolic("z");
		logger.info(variables);

		// Prepare samples
		SampleGenerator generator = new UniformSampleGenerator(new RealValue(
				1.0), new RealValue(2.0), 6);
		List<NumericalValue> samples = generator.getSamples();
		logger.info(samples);

		// Create a mesh generator and iterate over the mesh
		MeshGenerator mesh = new MeshGenerator(variables, samples);
		logger.info("Iterating over the mesh");
		int count = 0;
		while (mesh.hasNext())
		{
			VariableMap actual = mesh.next();
			logger.debug(count + " " + actual);
			// Assuming a lexicographical order of the iteration,
			// when count=22 the values should be:
			// z:1.5999999999999999 y:3 x:1.9999999999999998
			if (count == 22)
			{
				VariableMap expected = new DefaultVariableMap();
				expected.addNumerical("z", new RealValue(1.6));
				expected.addNumerical("y", new RealValue(3.0));
				expected.addNumerical("x", new RealValue(2.0));
				Assert.assertEquals(expected.size(), actual.size());
				for (String name : expected.getNames())
				{
					NumericalValue expectedValue = expected.getValue(name);
					NumericalValue actualValue = actual.getValue(name);
					// logger.trace(expectedValue + " " + actualValue);
					// logger.trace(expectedValue.getClass() + " " +
					// actualValue.getClass());
					Assert.assertEquals(TolerantlyComparable.EQUAL,
							expectedValue.tolerantlyEquals(actualValue, 1e-10));
				}

			}
			count++;
		}
	}

	// ========================= PRIVATE METHODS ===========================
}
