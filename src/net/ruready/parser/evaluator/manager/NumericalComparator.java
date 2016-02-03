/*****************************************************************************************
 * Source File: NumericalComparator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.manager;

import java.util.Comparator;
import java.util.List;

import net.ruready.common.discrete.Identifiable;
import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.evaluator.entity.MeshGenerator;
import net.ruready.parser.evaluator.entity.SampleGenerator;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.options.exports.VariableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NumericalComparator<R extends TolerantlyComparable<? super R> & Comparable<? super R>>
		implements Comparator<SyntaxTreeNode>, Identifiable<MathExpressionType>
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(NumericalComparator.class);

	// ========================= FIELDS ====================================

	// -------------------------------------------------
	// Required input
	// -------------------------------------------------

	/**
	 * Identifier of the type of the numerical comparator instance.
	 */
	private final MathExpressionType id;

	/**
	 * Evaluates syntax trees into numerical/logico-numerical results.
	 */
	private final Evaluator<R> evaluator;

	/**
	 * Represents # significant digits for comparison of arithmetic results.
	 */
	private final double precisionTol;

	// -------------------------------------------------
	// Internal variables
	// -------------------------------------------------
	// Used to generate samples of multiple variables
	private final MeshGenerator mesh;

	// -------------------------------------------------
	// Output
	// -------------------------------------------------

	// The last sample of variable values for which the comparison is done.
	private VariableMap sample;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a numerical comparator of two syntax trees from options.
	 * 
	 * @param id
	 *            Identifier of the type of the numerical comparator instance.
	 * @param variables
	 *            Variable names; variables are assumed to be symbolic. All
	 *            numerical values in this map will be overridden in this
	 *            object.
	 * @param generator
	 *            Generates variable sample values
	 * @param precisionTol
	 *            Represents # significant digits for comparison of arithmetic
	 *            results.
	 */
	public NumericalComparator(final MathExpressionType id,
			final VariableMap variables, final SampleGenerator generator,
			final Evaluator<R> evaluator, double precisionTol)
	{
		super();
		this.id = id;

		// Prepare samples
		List<NumericalValue> samples = generator.getSamples();
		logger.debug(samples);
		// Create a mesh generator and iterate over the mesh
		mesh = new MeshGenerator(variables, samples);

		this.evaluator = evaluator;
		this.precisionTol = precisionTol;
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathExpressionType getIdentifier()
	{
		return id;
	}

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return id.getType();
	}

	// ========================= IMPLEMENTATION: Comparator ====================

	/**
	 * Numerically compare two syntax trees. This violates the general contract
	 * of <code>Object.compareTo()</code>, but because this is a custom
	 * comparator we can afford this. Equality (<code>0</code> return value)
	 * is obtained when the two expressions are relatively equal up to the
	 * tolerance specified by the parser options field of this object.
	 * Otherwise, <code>-1</code> is returned if the comparsion has failed
	 * (e.g. one of the syntax trees is <code>null</code> or they evaluated to
	 * <code>NaN</code> or <code>Infinity</code> for all variable values),
	 * or <code>1</code> is returned if comparison was successful and the
	 * syntax trees were not equal.
	 * 
	 * @see java.util.Comparator#compare(java.lang.SyntaxTreeNode,
	 *      java.lang.Object)
	 */
	public int compare(SyntaxTreeNode arg0, SyntaxTreeNode arg1)
	{
		// logger.debug("Comparing arg0 " + arg0 + " arg1 " + arg1 + "
		// precisionTol " + precisionTol);
		if ((arg0 == null) || (arg1 == null))
		{
			return -1;
		}

		// logger.debug("tree " + tree);
		int goodSamples = 0;
		int count = 0;
		sample = null;

		if (logger.isDebugEnabled())
		{
			logger.debug("Iterating over the mesh");
		}
		while (mesh.hasNext())
		{
			// Get variable values
			sample = mesh.next();

			// Construct an evaluation object
			evaluator.setVariableMap(sample);

			// Evaluate both expressions
			R x0 = evaluator.evaluate(arg0);
			R x1 = evaluator.evaluate(arg1);
			int comparisonResult = x0.tolerantlyEquals(x1, precisionTol);
			// logger.debug("Sample #" + count + " - evaluated: " + " x0=" + x0
			// + " x1="
			// + x1 + " comparison " + comparisonResult);

			switch (comparisonResult)
			{
			case TolerantlyComparable.NOT_EQUAL_WHEN_NAN:
			{
				// If it's infinity vs. -infinity, infinity vs. NaN
				// or NaN vs. -infinity, they're different (compare
				// re part vs. re part, im part vs. im part).
				logger.debug("Sample #" + count
						+ " Found infinity/NaN expression difference: "
						+ " sample " + sample + " x0 " + x0 + " x1 " + x1);
				return 1;
			}

			case TolerantlyComparable.INDETERMINATE:
			{
				// Can't decide on infinity vs. infinity, -infinity vs.
				// -infinity, NaN vs. NaN ==> bad sample, skip.
				count++;
				break; // and continue the loop
			}

			case TolerantlyComparable.EQUAL:
			{
				// Values are equal
				count++;
				goodSamples++;
				break; // and continue the loop
			}

			default:
			{
				// Values are not equal
				logger.debug("Sample #" + count
						+ " Found expression difference: " + " sample "
						+ sample + " x0 " + x0 + " x1 " + x1);
				return 1;
			}
			} // switch (comparisonResult)
		}

		// At this point the trees are equal, or we can't determine equality.
		if (goodSamples == 0)
		{
			logger.warn("No good samples found for Grid equality!");
			sample = null;
			return 1;
		}
		logger.debug("Expressions are equivalent");
		return 0;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Returns the last sample of variable values for which the comparison is
	 * done.
	 * 
	 * @return the last sample of variable values for which the comparison is
	 *         done.
	 */
	public VariableMap getSample()
	{
		return sample;
	}

	/**
	 * @return the precisionTol
	 */
	public double getPrecisionTol()
	{
		return precisionTol;
	}
}
