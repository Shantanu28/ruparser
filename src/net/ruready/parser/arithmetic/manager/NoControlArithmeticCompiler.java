/*******************************************************************************
 * Source File: ParametricEvaluationCompiler.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.manager;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.parser.arithmetic.assembler.ArithmeticAssemblerID;
import net.ruready.parser.math.exports.SequenceTrack;
import net.ruready.parser.options.exports.ParserOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Works just like {@link ArithmeticCompiler}, except that it does not support
 * control sequences.
 * <p>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br> (c) 2006-07 Continuing
 * Education , University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah
 * @version Jul 3, 2007
 */
public class NoControlArithmeticCompiler extends ArithmeticCompiler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(NoControlArithmeticCompiler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser from options.
	 * 
	 * @param options
	 *            control options object
	 * @param factory
	 *            assembler factory to use
	 */
	public NoControlArithmeticCompiler(final ParserOptions options,
			final AbstractAssemblerFactory factory)
	{
		super(options, factory);
	}

	// ========================= IMPLEMENTATION: Compiler ==================

	/**
	 * Main parser call (with an optional control sequence). Returns a parser
	 * that will recognize controlled arithmetic expressions. The grammar rule
	 * is controlledExpr = control expr if the control flag is specified in the
	 * options object; or just expr, if control is false.
	 * 
	 * @return a parser that will recognize controlled arithmetic expressions.
	 */
	@Override
	public Parser parser()
	{
		initialize();

		Sequence globalExpr = new SequenceTrack("arithmetic expression");
		globalExpr.add(expr());
		globalExpr
				.setAssembler(factory.createAssembler(ArithmeticAssemblerID.EXPRESSION));
		return globalExpr;
	}

	// ========================= METHODS ===================================

	// ========================= ARITHMETIC PARSER COMPILATION =============

	// ========================= UTILITY OBJECT COMPILATION ================

	// ========================= GETTERS & SETTERS =========================

}
