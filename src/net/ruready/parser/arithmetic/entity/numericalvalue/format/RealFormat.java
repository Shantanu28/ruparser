/*****************************************************************************************
 * Source File: RealFormat.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.format;

import net.ruready.common.math.real.RealUtil;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A real number formatter.
 * <p/>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p/>
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
 * @version Oct 10, 2007
 */
class RealFormat implements NumericalFormat<RealValue> {
    // ========================= CONSTANTS =================================

    /**
     * A logger that helps identify this class' printouts.
     */
    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(RealFormat.class);

    // ========================= FIELDS ====================================

    // ========================= CONSTRUCTORS ==============================

    // ========================= IMPLEMENTATION: NumericalFormat ===========

    /**
     * @see net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormat#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
     */
    public String format(final RealValue value) {
        double d = value.getValue();
        // Make sure that r is printed as an integer
        // if it is [close to] one.
        long dInt = java.lang.Math.round(d);
        // Seems like "u" is too strict. So have 10*u
        // as the threshold.
        final double tol = RealUtil.MACHINE_DOUBLE_ERROR;
        if (RealUtil.doubleEquals(1.0 * dInt, d, tol)) {
            return CommonNames.MISC.EMPTY_STRING + dInt;
        }
        return CommonNames.MISC.EMPTY_STRING + d;
    }

    // ========================= IMPLEMENTATION: Identifiable ==============

    /**
     * @see net.ruready.common.discrete.Identifiable#getIdentifier()
     */
    public ArithmeticMode getIdentifier() {
        return ArithmeticMode.REAL;
    }

    /**
     * @see net.ruready.common.discrete.Identifier#getType()
     */
    public String getType() {
        return getIdentifier().getType();
    }

    // ========================= PRIVATE METHODS ===========================

    // ################## MAIN TESTING CALL #######################
    public static void main(String[] args) {
        Object[] temp = new Object[1];
        temp[0] = new Double(2.8200000000000003);
        String format = "%.3f";
        logger.info(String.format(format, temp));
        temp[0] = new Double(9.00000000000003);
        format = "%.5f";
        logger.info(String.format(format, temp));

        RealFormat f = new RealFormat();

        logger.info(f.format(new RealValue(2)));
        logger.info(f.format(new RealValue(2.000000001)));

        logger.info(f.format(new RealValue(2.1)));
        logger.info(f.format(new RealValue(210)));

        logger.info(f.format(new RealValue(1.0000)));
        logger.info(f.format(new RealValue(1.0001)));
    }
}
