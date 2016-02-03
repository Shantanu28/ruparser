/*****************************************************************************************
 * Source File: ParametricEvaluationAssemblerFactory.java
 ****************************************************************************************/
package net.ruready.parser.logical.assembler;

import net.ruready.common.exception.SystemException;
import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.assembler.AssemblerIdentifier;
import net.ruready.common.parser.core.assembler.SetFenceAssembler;
import net.ruready.common.parser.core.tokens.Token;

/**
 * A factory to instantiate assembler types for the logical parser. This is the only
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
 * @version Aug 16, 2007
 */
public class LogicalAssemblerFactory implements AbstractAssemblerFactory
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an logical parser assembler factory.
	 */
	public LogicalAssemblerFactory()
	{

	}

	// ========================= IMPLEMENTATION: AbstractColorFactory ==

	/**
	 * @see net.ruready.parser.logical.entity.AbstractAssemblerFactory#createAssembler(net.ruready.parser.logical.assembler.ArithmeticAssemblerID,
	 *      java.lang.Object[])
	 */
	public Assembler createAssembler(AssemblerIdentifier identifier, Object... args)
	{
		switch ((LogicalAssemblerID) identifier)
		{

			/**
			 * Arithmetic expression post-processor.
			 */
			case ARITHMETIC_EXPRESSION:
			{
				return new ArithmeticExpressionAssembler();
			}

				/**
				 * Processes and discards an assembly token.
				 */
			case DISCARD:
			{

				return new DiscardAssembler();
			}

				/**
				 * processes a single expression (treated as a statement).
				 */
			case SINGLE_EXPRESSION:
			{
				return new SingleExpressionAssembler();
			}

				/**
				 * processes a single statement.
				 */
			case STATEMENT:
			{
				return new StatementAssembler();
			}

				/**
				 * processes a single relation.
				 */
			case RELATION:
			{
				return new RelationAssembler();
			}

				/**
				 * Processes a relational operation symbol.
				 */
			case RELATION_SYMBOL:
			{
				return new RelationOperationSymbolAssembler();
			}

				/**
				 * A global response assembler.
				 */
			case RESPONSE:
			{
				return new ResponseAssembler((Token) args[0]);
			}

				/**
				 * Sets a fence for a multi-nary or sign assemblers.
				 */
			case SET_FENCE:
			{

				return new SetFenceAssembler((Token) args[0]);

			}

			default:
			{
				throw new SystemException(
						"Unsupported assembler type " + identifier);
			}
		}
	}

	// ========================= FIELDS ====================================

}
