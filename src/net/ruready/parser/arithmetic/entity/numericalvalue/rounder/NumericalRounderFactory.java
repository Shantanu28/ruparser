/*****************************************************************************************
 * Source File: NumericalRounderFactory.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.rounder;

import net.ruready.common.discrete.EnumeratedFactory;
import net.ruready.common.exception.SystemException;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An simple factory that instantiates different encryptor service types within this JVM.
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
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Oct 16, 2007
 */
@SuppressWarnings("unchecked")
public class NumericalRounderFactory implements
		EnumeratedFactory<ArithmeticMode, NumericalRounder<NumericalValue>>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(NumericalRounderFactory.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser service provider that instantiates different parser service types
	 * within this JVM.
	 */
	public NumericalRounderFactory()
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
	public NumericalRounder createType(ArithmeticMode identifier, Object... args)
	{
		switch (identifier)
		{
			// Integer number formatting
			case INTEGER:
			{
				return new IntegerRounder();
			}

				// Rational number formatting
			case RATIONAL:
			{
				return new RationalRounder();
			}

				// Real number formatting
			case REAL:
			{
				return new RealRounder();
			}

				// Complex number formatting
			case COMPLEX:
			{
				return new ComplexRounder();
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
