/*****************************************************************************************
 * Source File: SingleStringProcessor.java
 ****************************************************************************************/
package net.ruready.parser.service.manager;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.RequestHandlerChain;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.options.entity.MathExpressionType;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This handler saves an arithmetic target prepared by previous handlers. It serves an
 * adapter between those handlers and subsequent handlers that use the request attribute
 * set here.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 17, 2007
 */
public class SingleStringProcessor extends RequestHandlerChain
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SingleStringProcessor.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRCUTORS ==============================

	/**
	 * Initialize a a single string processor.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name to save arithmetic target under
	 * @param attributeNameTree
	 *            attribute name to save syntax tree under
	 * @param mathExpressionType
	 *            type of supported mathematical expressions (arithmetic or logical)
	 * @param nextNode
	 *            The next node in the chain. If <code>null</code>, the request
	 *            processing chain will terminate after this handler handles the request.
	 */
	public SingleStringProcessor(final String attributeNameTarget,
			final String attributeNameTree, final MathExpressionType mathExpressionType,
			RequestHandler nextNode)
	{
		super(null, nextNode);

		// ------------------------------------------------------------------
		// Don't use a static block to define chain order, because it
		// depends on instance parameters
		// ------------------------------------------------------------------
		// This is where the order of operations of this chain is defined.
		// Handlers will be invoked in their order of appearance on the list.

		List<RequestHandler> tempList = new ArrayList<RequestHandler>();

		// Matching
		switch (mathExpressionType)
		{
			case ARITHMETIC:
			{
				tempList.add(new ArithmeticMatchingProcessor());
				break;
			}
			case LOGICAL:
			{
				tempList.add(new LogicalMatchingProcessor());
				break;
			}
		}

		tempList.add(new SaveTargetAdapter(attributeNameTarget));
		tempList.add(new AbsoluteCanonicalizationProcessor(attributeNameTarget, null));
		tempList.add(new SaveSyntaxTreeAdapter(attributeNameTree));

		RequestHandler[] specificHandlerList = tempList
				.toArray(new RequestHandler[tempList.size()]);

		this.initialize(specificHandlerList);

	}

	// ========================= IMPLEMENTATION: RequestHandler ============

	/**
	 * Returns the name of this handler.
	 * 
	 * @return the name of this handler.
	 */
	@Override
	public String getName()
	{
		return ParserNames.HANDLER.PROCESSOR.NAME + CommonNames.MISC.TAB_CHAR + "Single string";
	}

}
