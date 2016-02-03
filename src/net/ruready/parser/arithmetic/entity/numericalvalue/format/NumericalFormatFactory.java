/*****************************************************************************************
 * Source File: NumericalRounderFactory.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.format;

import net.ruready.common.discrete.EnumeratedFactory;
import net.ruready.common.exception.SystemException;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An simple factory that instantiates different encryptor service types within this JVM.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 14, 2007
 */
public class NumericalFormatFactory implements
		EnumeratedFactory<ArithmeticMode, NumericalFormat<? extends NumericalValue>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NumericalFormatFactory.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser service provider that instantiates different parser service types
	 * within this JVM.
	 */
	public NumericalFormatFactory()
	{
		super();
	}

	// ========================= IMPLEMENTATION: EnumeratedFactory =========

	/**
	 * @param identifier
	 * @param args
	 * @return
	 * @see net.ruready.common.discrete.EnumeratedFactory#createType(net.ruready.common.discrete.Identifier,
	 *      java.lang.Object[])
	 */
	@Deprecated
	public NumericalFormat<? extends NumericalValue> createType(
			ArithmeticMode identifier, Object... args)
	{
		switch (identifier)
		{
			// Integer number formatting
			case INTEGER:
			{
				return new IntegerFormat();
			}

				// Rational number formatting
			case RATIONAL:
			{
				return new RationalFormat();
			}

				// Real number formatting
			case REAL:
			{
				return new RealFormat();
			}

				// Complex number formatting
			case COMPLEX:
			{
				return new ComplexFormat();
			}

			default:
			{
				throw new SystemException(
						"Unsupported parser service type " + identifier);
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
