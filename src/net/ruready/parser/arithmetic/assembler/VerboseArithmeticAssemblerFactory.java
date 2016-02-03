/*****************************************************************************************
 * Source File: ParametricEvaluationAssemblerFactory.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.parser.core.assembler.AbstractAssemblerFactory;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.assembler.AssemblerIdentifier;
import net.ruready.common.parser.core.assembler.VerboseAssembler;

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
 * @version Aug 16, 2007
 */
public class VerboseArithmeticAssemblerFactory implements AbstractAssemblerFactory
{
	// ========================= CONSTRUCTORS ==============================

	/**
	 * Initialize an arithmetic parser assembler factory.
	 */
	public VerboseArithmeticAssemblerFactory()
	{

	}

	// ========================= IMPLEMENTATION: AbstractColorFactory ==

	/**
	 * @see net.ruready.parser.arithmetic.entity.AbstractAssemblerFactory#createAssembler(net.ruready.parser.arithmetic.assembler.ArithmeticAssemblerID,
	 *      java.lang.Object[])
	 */
	public Assembler createAssembler(AssemblerIdentifier identifier, Object... args)
	{
		Assembler rawAssembler = new ArithmeticAssemblerFactory().createAssembler(
				identifier, args);
		return new VerboseAssembler(rawAssembler);
	}

	// ========================= FIELDS ====================================

}
