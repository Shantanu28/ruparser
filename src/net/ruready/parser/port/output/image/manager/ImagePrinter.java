/*******************************************************
 * Source File: ImagePrinter.java
 *******************************************************/
package net.ruready.parser.port.output.image.manager;

import java.awt.image.BufferedImage;

import net.ruready.common.math.basic.IntegerPair;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.options.exports.ParserOptions;
import net.ruready.parser.port.output.manager.TargetPrinter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts a marked syntax tree within a target into a highlighted text string.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 13, 2007
 */
public abstract class ImagePrinter implements TargetPrinter
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ImagePrinter.class);

	// ========================= FIELDS ====================================

	// arithmetic target object containing a syntax tree and a list of
	// extraneous math tokens
	protected MathTarget target;

	// Parser control options
	protected ParserOptions options;

	// The generated output image
	private BufferedImage output;

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize an tree printer.
	 * 
	 * @param target
	 *            arithmetic target object containing a syntax tree and a list
	 *            of extraneous tokens
	 * @param options
	 *            Parser control options
	 */
	public ImagePrinter(MathTarget target, ParserOptions options)
	{
		super();
		this.target = target;
		this.options = options;
		output = this.buildOutputImage();
	}

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Generate the output image (to be saved in the output variable -- a
	 * caching strategy).
	 * 
	 * @return output stream containing the target's image
	 */
	abstract protected BufferedImage buildOutputImage();

	/**
	 * Return the output image size (height, width).
	 * 
	 * @return the output image size (height, width)
	 */
	abstract public IntegerPair getSize();

	// ========================= IMPLEMENTATION: TargetPrinter =============

	/**
	 * @return the output stream
	 */
	final public BufferedImage getOutput()
	{
		return output;
	}

	// ========================= PUBLIC METHODS ============================

	// ========================= PRIVATE METHODS ===========================

	// ========================= GETTERS & SETTERS =========================

}
