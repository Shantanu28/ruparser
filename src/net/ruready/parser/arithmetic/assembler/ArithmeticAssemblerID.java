/*******************************************************************************
 * Source File: ParametricEvaluationAssemblerID.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.parser.core.assembler.AssemblerIdentifier;

/**
 * A factory to instantiate assembler types for the arithmetic parser. This is
 * the only public class in the assembler package, and serves as the package's
 * proxy.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 
 *         2006-07 Continuing Education , University of Utah . All copyrights
 *         reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jan 31, 2007
 */
public enum ArithmeticAssemblerID implements AssemblerIdentifier
{
	// ========================= ENUMERATED TYPES ==========================

	/**
	 * Generic binary function processor.
	 */
	BINARY_FUNC
	{

	},

	/**
	 * Recognizes and processes a binary function symbol.
	 */
	BINARY_FUNC_SYMBOL
	{

	},

	/**
	 * Generic binary operation processor.
	 */
	BINARY_OP
	{

	},

	/**
	 * Recognizes and processes a binary operation symbol.
	 */
	BINARY_OP_SYMBOL
	{

	},

	/**
	 * Recognizes and processes a complex-valued constant symbol.
	 */
	COMPLEX_CONSTANT
	{

	},

	/**
	 * Processes and discards an assembly token.
	 */
	DISCARD
	{

	},

	/**
	 * Expression processor. Runs before the optional control sequence is
	 * processed.
	 */
	EXPRESSION
	{

	},

	/**
	 * Global expression processor. Runs before the optional control sequence is
	 * processed.
	 * 
	 * @deprecated
	 */
	@Deprecated
	GLOBAL_EXPRESSION
	{

	},

	/**
	 * Changes implicit multiplication mode (affects the parser grammar).
	 */
	IMP_MULT_MODE
	{

	},

	/**
	 * Processes an the implicit multiplication "*" symbol.
	 */
	IMP_MULT_SYMBOL
	{

	},

	/**
	 * Changes the working arithmetic (affects the parser grammar).
	 */
	MODE
	{

	},

	/**
	 * Processes a multi-nary arithmetic operation (assembles and processes its
	 * children branches in the syntax tree).
	 */
	MULTINARY_OP
	{

	},

	/**
	 * Recognizes and processes a number.
	 */
	NUM
	{

	},

	/**
	 * Recognizes and processes a pair of parenthesis.
	 */
	PARENTHESIS
	{

	},

	/**
	 * Changes the working precision (affects the parser grammar).
	 */
	PRECISION
	{

	},

	/**
	 * Recognizes and processes a real-valued constant symbol.
	 */
	REAL_CONSTANT
	{

	},

	/**
	 * Sets a fence for a multi-nary or sign assemblers.
	 */
	SET_FENCE
	{

	},

	/**
	 * Processes a unary sign operation.
	 */
	SIGN_OP
	{

	},

	/**
	 * Gracefully handles a <code>SequenceTrack</code> failure..
	 */
	@Deprecated
	TRACK_FAIL
	{

	},

	/**
	 * A unary function/operation processor.
	 */
	UNARY_OP
	{

	},

	/**
	 * Recognizes and processes a unary function/operation's symbol.
	 */
	UNARY_OP_SYMBOL
	{

	},

	/**
	 * Recognizes and processes a variable's symbol (e.g. "x").
	 */
	VARIABLE
	{

	};

	// ========================= FIELDS ====================================

	// ========================= IMPLEMENTATION: Identifier ===================

	/**
	 * Return the object's type. This would normally be the object's
	 * <code>getClass().getSimpleName()</code>, but objects might be wrapped
	 * by Hibernate to return unexpected names. As a rule, the type string
	 * should not contain spaces and special characters.
	 */
	public String getType()
	{
		// The enumerated type's name satisfies all the type criteria, so use
		// it.
		return name();
	}

	// ========================= METHODS ===================================

}
