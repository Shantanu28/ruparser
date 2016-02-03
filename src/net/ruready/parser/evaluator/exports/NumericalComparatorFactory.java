/*****************************************************************************************
 * Source File: NumericalRounderFactory.java
 ****************************************************************************************/
package net.ruready.parser.evaluator.exports;

import net.ruready.common.discrete.EnumeratedFactory;
import net.ruready.common.exception.SystemException;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;
import net.ruready.parser.evaluator.entity.SampleGenerator;
import net.ruready.parser.evaluator.manager.ArithmeticEvaluator;
import net.ruready.parser.evaluator.manager.LogicalEvaluator;
import net.ruready.parser.evaluator.manager.NumericalComparator;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An simple factory that instantiates different numerical comparators based on parser
 * control options.
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
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Jul 18, 2007
 */
public class NumericalComparatorFactory implements
		EnumeratedFactory<MathExpressionType, NumericalComparator<?>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NumericalComparatorFactory.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser service provider that instantiates different parser service types
	 * within this JVM.
	 */
	public NumericalComparatorFactory()
	{
		super();
	}

	// ========================= IMPLEMENTATION: EnumeratedFactory =========

	/**
	 * @see net.ruready.common.discrete.EnumeratedFactory#createType(net.ruready.common.discrete.Identifier,
	 *      java.lang.Object[])
	 */
	public NumericalComparator<?> createType(MathExpressionType identifier,
			Object... args)
	{
		switch (identifier)
		{
			// Integer number formatting
			case ARITHMETIC:
			{
				ParserOptions options = (ParserOptions) args[0];
				SampleGenerator generator = (SampleGenerator) args[1];
				return new NumericalComparator<NumericalValue>(identifier, options
						.getVariables(), generator, new ArithmeticEvaluator(), options
						.getPrecisionTol());
			}

				// Rational number formatting
			case LOGICAL:
			{
				ParserOptions options = (ParserOptions) args[0];
				SampleGenerator generator = (SampleGenerator) args[1];
				return new NumericalComparator<SyntaxTreeNode>(identifier, options
						.getVariables(), generator, new LogicalEvaluator(true), options
						.getPrecisionTol());
			}

			default:
			{
				throw new SystemException(
						"Unsupported numerical comparator type " + identifier);
			}
		} // switch (identifier)
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Convert a digit number input into precision tolerance units.
	 * 
	 * @param digits
	 *            #significant digits
	 * @return corresponding relative tolerance of expression numerical comparison
	 */
	public static double digitsToPrecisionTol(int digits)
	{
		return Math.pow(10.0, -digits);
	}

}
