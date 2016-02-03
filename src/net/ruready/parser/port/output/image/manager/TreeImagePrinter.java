/*******************************************************
 * Source File: TreeImagePrinter.java
 *******************************************************/
package net.ruready.parser.port.output.image.manager;

import java.awt.image.BufferedImage;

import net.ruready.common.math.basic.IntegerPair;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.port.output.image.entity.PlotTree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a marked syntax tree into a highlighted ASCII string.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 13, 2007
 */
/**
 * Long description ...
 *
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i>
 *          Academic Outreach and Continuing Education (AOCE)
 *          1901 East South Campus Dr., Room 2197-E
 *          University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i>
 *          AOCE, Room 2197-E, University of Utah
 *            
 *          University of Utah, Salt Lake City, UT 84112
 * (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @version May 22, 2007
 */
public class TreeImagePrinter extends ImagePrinter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TreeImagePrinter.class);

	// ========================= FIELDS ====================================

	private PlotTree plotTree;
	
	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an HTMLPrinter.
	 * 
	 * @param target
	 *            arithmetic target object containing a syntax tree and a list
	 *            of extraneous tokens
	 * @param options
	 *            Parser control options
	 */
	public TreeImagePrinter(MathTarget target, ParserOptions options)
	{
		super(target, options);
	}

	// ========================= IMPLEMENTATION: ImagePrinter ==============

	/**
	 * @see net.ruready.parser.port.output.image.manager.ImagePrinter#buildOutputImage()
	 */
	@Override
	protected BufferedImage buildOutputImage()
	{
		plotTree = new PlotTree(target);
		return plotTree.getOutput();
	}

	/**
	 * @see net.ruready.parser.port.output.image.manager.ImagePrinter#getSize()
	 */
	@Override
	public IntegerPair getSize()
	{
		return plotTree.getChartSize();
	}

	// ========================= PRIVATE METHODS ===========================

	// ========================= GETTERS & SETTERS =========================

}
