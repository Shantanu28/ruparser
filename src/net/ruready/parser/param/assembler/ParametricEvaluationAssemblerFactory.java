/*****************************************************************************************
 * Source File: ParametricEvaluationAssemblerFactory.java
 ****************************************************************************************/
package net.ruready.parser.param.assembler;

import net.ruready.common.exception.SystemException;
import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.assembler.AssemblerIdentifier;

/**
 * A factory to instantiate assembler types for the parametric evaluation parser. This is
 * the only public class in the assembler package, and serves as the package's proxy.
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
public class ParametricEvaluationAssemblerFactory implements AbstractAssemblerFactory
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser assembler factory.
	 */
	public ParametricEvaluationAssemblerFactory()
	{

	}

	// ========================= IMPLEMENTATION: AbstractColorFactory ==

	/**
	 * @param identifier
	 * @param args
	 * @return
	 * @see net.ruready.common.parser.core.assembler.AbstractAssemblerFactory#createAssembler(net.ruready.common.parser.core.assembler.AssemblerIdentifier,
	 *      java.lang.Object[])
	 */
	public Assembler createAssembler(AssemblerIdentifier identifier, Object... args)
	{
		switch ((ParametricEvaluationAssemblerID) identifier)
		{

			/**
			 * Appends a number to the evaluated string.
			 */
			case APPEND_NUM:
			{
				return new AppendNumAssembler();
			}

				/**
				 * Appends a word to the evaluated string.
				 */
			case APPEND_WORD:
			{
				return new AppendWordAssembler();
			}

				/**
				 * Evaluates a mathematical expression using the mathematical parser and
				 * appends the result to the evaluated string.
				 */
			case EXPRESSION_EVALUATION:
			{
				return new ExpressionEvaluationAssembler();
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
