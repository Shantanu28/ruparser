/*****************************************************************************************
 * Source File: DefaultColorFactory.java
 ****************************************************************************************/
package net.ruready.parser.port.output.image.entity;

import java.awt.Color;

import net.ruready.common.exception.SystemException;
import net.ruready.parser.math.entity.MathTokenStatus;

/**
 * A factory to instantiate default colors for a tree plotter.
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
public class DefaultColorFactory implements AbstractColorFactory
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser assembler factory.
	 */
	public DefaultColorFactory()
	{

	}

	// ========================= IMPLEMENTATION: AbstractColorFactory ==

	/**
	 * @see net.ruready.parser.port.output.image.entity.AbstractColorFactory#textColor(net.ruready.parser.math.entity.MathTokenStatus)
	 */
	public Color textColor(MathTokenStatus status)
	{
		switch (status)
		{

			case DISCARDED:
			{
				return Color.BLACK;
			}

			case FICTITIOUS_CORRECT:
			{
				return Color.BLACK;
			}

			case REDUNDANT:
			{
				return Color.BLACK;
			}

			case WRONG:
			{
				return Color.RED;
			}

			case UNRECOGNIZED:
			{
				return Color.BLACK;
			}

			case MISSING:
			{
				return Color.BLUE;
			}

			case CORRECT:
			{
				return Color.BLACK;
			}

			default:
			{
				throw new SystemException("Unsupported status "
						+ status);
			}
		} // switch (status)
	}

	/**
	 * @see net.ruready.parser.port.output.image.entity.AbstractColorFactory#backgroundColor(net.ruready.parser.math.entity.MathTokenStatus)
	 */
	public Color backgroundColor(MathTokenStatus status)
	{
		switch (status)
		{

			case DISCARDED:
			{
				return Color.WHITE;
			}

			case FICTITIOUS_CORRECT:
			{
				return Color.WHITE;
			}

			case REDUNDANT:
			{
				return Color.CYAN;
			}

			case WRONG:
			{
				return Color.YELLOW;
			}

			case UNRECOGNIZED:
			{
				return Color.YELLOW;
			}

			case MISSING:
			{
				return Color.YELLOW;
			}

			case CORRECT:
			{
				return Color.GREEN;
			}

			default:
			{
				throw new SystemException("Unsupported status "
						+ status);
			}
		} // switch (status)
	}

	// ========================= FIELDS ====================================

}
