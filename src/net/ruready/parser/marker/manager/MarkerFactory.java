/*****************************************************************************************
 * Source File: MarkerFactory.java
 ****************************************************************************************/
package net.ruready.parser.marker.manager;

import net.ruready.common.discrete.EnumeratedFactory;
import net.ruready.common.exception.SystemException;
import net.ruready.parser.analysis.entity.AnalysisID;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.atpm.manager.ElementEditDistanceComputer;
import net.ruready.parser.atpm.manager.NodeComparisonCost;
import net.ruready.parser.atpm.manager.ShashaEditDistanceComputer;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.options.exports.ParserOptions;

/**
 * An simple factory that instantiates different parser analysis types and expression
 * string mark-up methods.
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
public class MarkerFactory implements EnumeratedFactory<AnalysisID, Marker>
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a parser service provider that instantiates different parser service types
	 * within this JVM.
	 */
	public MarkerFactory()
	{
		super();
	}

	// ========================= IMPLEMENTATION: MarkerFactory ==========

	/**
	 * @see net.ruready.parser.service.manager.MarkerProvider#createType(net.ruready.parser.exports.NodeMatchType,
	 *      java.lang.Object[])
	 */
	public Marker createType(AnalysisID identifier, Object... args)
	{
		switch (identifier)
		{
			// Element marker
			case ELEMENT:
			{
				if (args.length == 2)
				{
					// Without options
					return new ElementMarker((MathTarget) args[0], (MathTarget) args[1]);
				}
				else if (args.length == 3)
				{
					// With options
					return new ElementMarker((ParserOptions) args[0],
							(MathTarget) args[1], (MathTarget) args[2]);
				}
				throw new SystemException(
						"Unsupported ATPM marker with " + args.length + " arguments");
			}

				// ATPM (pattern) marker
			case ATPM:
			{
				if (args.length == 2)
				{
					// Without options
					return new ATPMMarker((MathTarget) args[0], (MathTarget) args[1]);
				}
				else if (args.length == 3)
				{
					// With options
					return new ATPMMarker((ParserOptions) args[0], (MathTarget) args[1],
							(MathTarget) args[2]);
				}
				throw new SystemException(
						"Unsupported ATPM marker with " + args.length + " arguments");
			}

			default:
			{
				throw new SystemException(
						"Unsupported analysis type " + identifier);
			}
		} // switch (identifier)
	}

	// ========================= FIELDS ====================================

	// ========================= METHODS ===================================

	/**
	 * Create an edit distance computer for a pair of trees. After construction is
	 * complete, use the getter methods on this object to retrieve the results.
	 * 
	 * @param identifier
	 *            marker type identifier to infer on the edit distance computer type
	 * @param costComputer
	 *            Computes the cost function for edit distance minimization
	 */
	public EditDistanceComputer<MathToken, SyntaxTreeNode> createEditDistanceComputer(
			AnalysisID identifier, final NodeComparisonCost<MathToken> costComputer,
			Object... args)
	{
		switch (identifier)
		{
			// Element marker's computer
			case ELEMENT:
			{
				return new ElementEditDistanceComputer<MathToken, SyntaxTreeNode>(
						(Marker) args[0], costComputer);
			}

				// ATPM (pattern) marker's computer
			case ATPM:
			{
				return new ShashaEditDistanceComputer<MathToken, SyntaxTreeNode>(
						(SyntaxTreeNode) args[0], (SyntaxTreeNode) args[1], costComputer);
			}

			default:
			{
				throw new SystemException(
						"Unsupported analysis type " + identifier);
			}
		} // switch (identifier)
	}

}
