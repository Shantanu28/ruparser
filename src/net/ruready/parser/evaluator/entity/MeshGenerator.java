/*****************************************************************************************
 * Source File: MeshGenerator.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.entity;

import java.util.Iterator;
import java.util.List;

import net.ruready.common.exception.SystemException;
import net.ruready.common.math.basic.MultiDimensionalIterator;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.options.exports.VariableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generate and iterate over a d-dimensional tensor product of 1-D variable samples. Here
 * <code>d</code> is the number of symbolic variables. Numerical variables in the
 * original variable map passed into this class' constructor are fixed throughout the
 * iteration.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 27, 2007
 */
public class MeshGenerator implements Iterator<VariableMap>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MeshGenerator.class);

	// ========================= FIELDS ====================================

	// Variable map. An entry can be ("x", null)
	// (for a symbolic variable) or ("x", 1.5) (for a
	// variable that assumes a unique value).
	// Stored for internal book-keeping of variable values during iteration.
	private VariableMap variables;

	// Holds a clone of the original list of variables.
	// TODO: (low priority) improve this awful way of saving which variable
	// is symbolic
	private VariableMap originalVariables;

	// Multi-dimensional iterator
	private Iterator<int[]> iterator;

	// Grid values for each symbolic variable (1-D array)
	private List<NumericalValue> samples;

	// Number of symbolic variables in the variable map
	private int numDims;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a multi-dimensional mesh generator from variables and 1D values.
	 * 
	 * @param originalVariables
	 *            variable map
	 * @param samples
	 *            list of samples to be used for each symbolic variable in the variable
	 *            map during iteration
	 */
	public MeshGenerator(VariableMap originalVariables, List<NumericalValue> samples)
	{
		super();
		// Clone the map parameter so that we don't override it during iteration
		this.originalVariables = (VariableMap) originalVariables.clone();
		this.variables = (VariableMap) originalVariables.clone();
		this.samples = samples;
		this.numDims = originalVariables.numSymbolic();

		// Initialize multi-dimensional iterator
		int numSamples = samples.size();
		int[] lower = new int[numDims];
		int[] upper = new int[numDims];
		for (int i = 0; i < numDims; i++)
		{
			lower[i] = 0;
			upper[i] = numSamples - 1;
		}
		this.iterator = new MultiDimensionalIterator(lower, upper);
	}

	// ========================= IMPLEMENTATION: Iterator ==================

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public VariableMap next()
	{
		// Iterate over variable map; for each symbolic variable encountered
		// (variable order should be -deterministic-; see the documentation of
		// the VariableMap.iterator() method).
		int[] sub = this.iterator.next();
		// logger.debug("sub " + ArrayUtil.arrayToString(sub));
		Iterator<Variable> variableIterator = variables.iterator();
		int count = 0;
		while (variableIterator.hasNext())
		{
			Variable var = variableIterator.next();
			// Note: check if the corresponding variable in the -original-
			// map is symbolic, otherwise once we set it to be numeric in
			// variables
			// it will never show up as symbolic again!
			if (originalVariables.get(var.getName()).isSymbolic())
			{
				// logger.debug("count " + count);
				// logger.debug(sub[count]);
				// logger.debug("var " + var);
				// logger.debug("samples[" + sub[count] + "] = "
				// + this.samples.get(sub[count]));
				var.setValue(this.samples.get(sub[count]));
				count++;
			}
		}
		return variables;
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		throw new SystemException(
				"Mesh generator cannot remove elements");
	}
}
