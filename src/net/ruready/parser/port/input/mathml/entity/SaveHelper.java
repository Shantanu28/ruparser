/*****************************************************************************************
 * Source File: SaveHelper.java
 ****************************************************************************************/
package net.ruready.parser.port.input.mathml.entity;

import net.ruready.common.parser.xml.helper.GriddyHelper;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticLiteralValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A helper that saves a tag as a token on the target's stack, but takes no other
 * operations.
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
 * @version Sep 8, 2007
 */
class SaveHelper extends GriddyHelper
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SaveHelper.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Construct a helper that saves a tag as a token on the target's stack, but takes no
	 * other operations.
	 * 
	 * @param tagName
	 *            final String tagName
	 */
	public SaveHelper(final String tagName)
	{
		super(tagName);
	}

	// ========================= IMPLEMENTATION: Helper ====================

	/**
	 * Create a math element to wrap the most recently pushed element on the target's
	 * stack.
	 * 
	 * @see net.ruready.parser.core.xml.Helper#endElement(java.lang.Object)
	 */
	@Override
	public void processEndElement(Object target)
	{
		// Cast to a friendlier version
		ConverterTarget myTarget = (ConverterTarget) target;

		// Pop the latest element
		MathElement element = myTarget.pop();

		// "Wrap" the element with a the discarded tag; the string
		// representation doesn't change because this element is discarded. The
		// discarded element only plays a role in the order of operands in
		// ApplyFuncHelper.
		MathElement newElement = new MathElement(element.getString(),
				new ArithmeticLiteralValue(tagName));

		// Push the new element in place of the old latest element
		myTarget.push(newElement);
	}
}
