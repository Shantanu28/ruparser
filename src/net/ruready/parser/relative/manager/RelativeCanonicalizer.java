/*****************************************************************************************
 * Source File: RelativeCanonicalizer.java
 ****************************************************************************************/
package net.ruready.parser.relative.manager;

import net.ruready.parser.atpm.entity.NodeMatch;
import net.ruready.parser.atpm.manager.EditDistanceComputer;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A unifying abstraction / base class for relative canonicalization operations on a pair
 * of trees. By default, all hooks do nothing. Sub-classes can override some of them to
 * add their functionality.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Dec 23, 2006
 */
public abstract class RelativeCanonicalizer
{

	// ========================= CONSTANTS ==================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RelativeCanonicalizer.class);

	// ========================= FIELDS =====================================

	// Reference tree. Accessible to sub-classes.
	protected final SyntaxTreeNode reference;

	// Response tree. Accessible to sub-classes.
	protected final SyntaxTreeNode response;

	// ED computer prepared by a marker
	protected final EditDistanceComputer<MathToken, SyntaxTreeNode> editDistanceComputer;

	// ========================= CONSTRUCTORS ===============================

	/**
	 * Create a relative canonicalization operation processor.
	 * 
	 * @param reference
	 *            Reference tree
	 * @param response
	 *            Response tree
	 * @param editDistanceComputer
	 *            ED computer prepared by a marker
	 */
	public RelativeCanonicalizer(final SyntaxTreeNode reference,
			final SyntaxTreeNode response,
			final EditDistanceComputer<MathToken, SyntaxTreeNode> editDistanceComputer)
	{
		super();
		this.reference = reference;
		this.response = response;
		this.editDistanceComputer = editDistanceComputer;
		logger.debug("reference tree:\n" + reference);
		logger.debug("response tree :\n" + response);
	}

	// ========================= ABSTRACT METHODS ============================

	/**
	 * The result of this method will determine whether the node match will be processed.
	 * By default, this method returns <code>false</code>.
	 * 
	 * @param match
	 *            node match to be considered
	 * @return is this node match to be processed
	 */
	protected boolean isProcessed(NodeMatch<MathToken, SyntaxTreeNode> match)
	{
		return false;
	}

	/**
	 * Process the node match.
	 * 
	 * @param match
	 *            node match to be processed
	 */
	protected void processMatch(NodeMatch<MathToken, SyntaxTreeNode> match)
	{

	}

	/**
	 * Loop over the ED computer's nodal mapping elements and process those that qualify
	 * under the <code>isProcessed()</code> method contract.
	 */
	public abstract void execute();

	// ========================= METHODS =====================================

	// ========================= GETTERS & SETTERS =========================

}
