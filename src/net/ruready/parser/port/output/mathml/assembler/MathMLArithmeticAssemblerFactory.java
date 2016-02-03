/*****************************************************************************************
 * Source File: MathMLAssemblerFactory.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import net.ruready.common.exception.SystemException;
import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.assembler.AssemblerIdentifier;
import net.ruready.common.parser.core.assembler.SetFenceAssembler;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.assembler.ArithmeticAssemblerID;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstant;

/**
 * A factory to instantiate assembler types for the arithmetic parser. This is the only
 * public class in the assembler package, and serves as the package's proxy.
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
public class MathMLArithmeticAssemblerFactory implements AbstractAssemblerFactory
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser assembler factory.
	 */
	public MathMLArithmeticAssemblerFactory()
	{

	}

	// ========================= IMPLEMENTATION: AbstractAssemblerFactory ==

	/**
	 * @see net.ruready.parser.arithmetic.entity.AbstractAssemblerFactory#createAssembler(net.ruready.parser.arithmetic.assembler.ArithmeticAssemblerID,
	 *      java.lang.Object[])
	 */
	public Assembler createAssembler(AssemblerIdentifier id, Object... args)
	{
		switch ((ArithmeticAssemblerID) id)
		{

			/**
			 * Generic binary function processor.
			 */
			case BINARY_FUNC:
			{

				return new BinaryFunctionAssembler();

			}

				/**
				 * Recognizes and processes a binary function symbol.
				 */
			case BINARY_FUNC_SYMBOL:
			{

				return new BinaryFunctionSymbolAssembler();

			}

				/**
				 * Generic binary operation processor.
				 */
			case BINARY_OP:
			{

				return new BinaryOperationAssembler();

			}

				/**
				 * Recognizes and processes a binary operation symbol.
				 */
			case BINARY_OP_SYMBOL:
			{

				return new BinaryOperationSymbolAssembler();

			}

				/**
				 * Recognizes and processes a complex-valued constant symbol.
				 */
			case COMPLEX_CONSTANT:
			{

				return new ComplexConstantAssembler((ComplexConstant) args[0]);

			}

				/**
				 * Processes and discards an assembly token.
				 */
			case DISCARD:
			{

				return new DiscardAssembler();

			}

				/**
				 * Expression processor. Runs before the optional control sequence is
				 * processed.
				 */
			case EXPRESSION:
			{

				return new ArithmeticExpressionAssembler();

			}

				/**
				 * Changes implicit multiplication mode (affects the parser grammar).
				 */
			case IMP_MULT_MODE:
			{

				return new ImpMultModeAssembler();

			}

				/**
				 * Processes an the implicit multiplication "*" symbol.
				 */
			case IMP_MULT_SYMBOL:
			{

				return new ImpMultSymbolAssembler();

			}

				/**
				 * Changes the working arithmetic (affects the parser grammar).
				 */
			case MODE:
			{

				return new ModeAssembler();

			}

				/**
				 * Recognizes and processes a number.
				 */
			case NUM:
			{

				return new NumAssembler();

			}

				/**
				 * Recognizes and processes a pair of parenthesis.
				 */
			case PARENTHESIS:
			{

				return new ParenthesisAssembler();

			}

				/**
				 * Changes the working precision (affects the parser grammar).
				 */
			case PRECISION:
			{

				return new PrecisionAssembler();

			}

				/**
				 * Recognizes and processes a real-valued constant symbol.
				 */
			case REAL_CONSTANT:
			{

				return new RealConstantAssembler((RealConstant) args[0]);

			}

				/**
				 * Sets a fence for a multi-nary or sign assemblers.
				 */
			case SET_FENCE:
			{

				return new SetFenceAssembler((Token) args[0]);

			}

				/**
				 * Processes a unary sign operation.
				 */
			case SIGN_OP:
			{

				return new SignOperationAssembler((Token) args[0]);

			}

				/**
				 * A unary function/operation processor.
				 */
			case UNARY_OP:
			{

				return new UnaryOperationAssembler();

			}

				/**
				 * Recognizes and processes a unary function/operation's symbol.
				 */
			case UNARY_OP_SYMBOL:
			{

				return new UnaryOperationSymbolAssembler();

			}

				/**
				 * Recognizes and processes a variable's symbol (e.g. "x").
				 */
			case VARIABLE:
			{

				return new VariableAssembler();

			}

			default:
			{
				throw new SystemException(
						"Unsupported assembler type " + id);
			}
		}
	}
	// ========================= FIELDS ====================================

}
