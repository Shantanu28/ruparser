/*****************************************************************************************
 * Source File: AbsoluteCanonicalizationProcessor.java
 ****************************************************************************************/
package net.ruready.parser.service.manager;

import java.util.ArrayList;
import java.util.List;

import net.ruready.common.chain.RequestHandler;
import net.ruready.common.chain.RequestHandlerChain;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.absolute.exports.AssociationSwitchHandler;
import net.ruready.parser.absolute.exports.Binary2MultinaryConverterHandler;
import net.ruready.parser.absolute.exports.MultinaryCollapserHandler;
import net.ruready.parser.absolute.exports.RedundancyRemovalHandler;
import net.ruready.parser.absolute.exports.RedundancyRemoverBinaryPlusHandler;
import net.ruready.parser.absolute.exports.RelationTransposerHandler;
import net.ruready.parser.absolute.exports.UnarySwitchHandler;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A prepared parser processing chain that tokenizes and runs arithmetic matching on a
 * string. Assumes that an arithmetic compiler already exists in the request.
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
public class AbsoluteCanonicalizationProcessor extends RequestHandlerChain
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(AbsoluteCanonicalizationProcessor.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create an arithmetic grammar matching processor.
	 * 
	 * @param attributeNameTarget
	 *            Attribute name of the arithmetic target to process
	 * @param nextNode
	 *            The next node to invoke in a super-chain after this processor is
	 *            completed. If <code>null</code>, the request processing chain will
	 *            terminate after this handler handles the request.
	 */
	public AbsoluteCanonicalizationProcessor(String attributeNameTarget,
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

		// Remove redundant (), unary +
		tempList.add(new RedundancyRemovalHandler(attributeNameTarget));

		// Switch association to left whenver possible (a+[b-c] -> [a+b]-c)
		tempList.add(new AssociationSwitchHandler(attributeNameTarget));

		// Remove redundant binary plus ops (a+-b -> a-b)
		tempList.add(new RedundancyRemoverBinaryPlusHandler(attributeNameTarget));

		// Switch the order of unary - and binary *, /
		tempList.add(new UnarySwitchHandler(attributeNameTarget));

		// Transpose relations like x > y to y < x
		tempList.add(new RelationTransposerHandler(attributeNameTarget));

		// Convert all unary operations like +,-,*/ to multi-nary form
		// tempList.add(new
		// Unary2MultinaryConverterHandler(attributeNameTarget));

		// Convert all binary operations like +,-,*/ to multi-nary form
		tempList.add(new Binary2MultinaryConverterHandler(attributeNameTarget));

		// Collapse multi-nary operation branches & sort branches
		tempList.add(new MultinaryCollapserHandler(attributeNameTarget));

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
		return ParserNames.HANDLER.PROCESSOR.NAME + CommonNames.MISC.TAB_CHAR
				+ "Absolute canonicalization";
	}
}
