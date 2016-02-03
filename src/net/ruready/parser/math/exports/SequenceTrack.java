/*****************************************************************************************
 * Source File: SequenceTrack.java
 ****************************************************************************************/
package net.ruready.parser.math.exports;

import net.ruready.common.parser.core.assembler.TrackAssembler;
import net.ruready.common.parser.core.entity.Match;
import net.ruready.common.parser.core.entity.Matches;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.parser.core.manager.Parser;
import net.ruready.common.parser.core.manager.Sequence;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.text.TextUtil;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exception.MathParserTrackException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link SequenceTrack} is a sequence that applies a {@link TrackAssembler}
 * (instead of throwing an exception) if the sequence begins but does not
 * complete.
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
 * Please contact these numbers immediately if you receive this file without
 * permission from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Sep 8, 2007
 */
public class SequenceTrack extends Sequence
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SequenceTrack.class);

	// ========================= FIELDS ====================================

	// Local variables that hold the error parts
	private String after;

	private String expected;

	private String found;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs a nameless <code>SequenceTrack</code>.
	 */
	public SequenceTrack()
	{

	}

	/**
	 * Constructs a <code>SequenceTrack</code> with the given name.
	 * 
	 * @param name
	 *            a name to be known by
	 */
	public SequenceTrack(String name)
	{
		super(name);
	}

	// ========================= IMPLEMENTATION: Sequence ==================

	/**
	 * Given a collection of assemblies, this method matches this track against
	 * all of them, and returns a new collection of the assemblies that result
	 * from the matches. If the match begins but does not complete, this method
	 * applies its fail assembler (more graceful than throwing an exception, as
	 * in Metsker's original code).
	 * 
	 * @return a list of assemblies that result from matching against a
	 *         beginning set of assemblies
	 * @param list
	 *            a list of assemblies to match against
	 */
	@Override
	public Matches match(Matches in)
	{
		boolean inTrack = false;
		Matches last = in;
		Matches out = in;
		// logger.debug("matching, in " + in);
		for (Parser p : subparsers)
		{
			out = p.matchAndAssemble(last);
			// logger.debug("Parser " + p + " inTrack " + inTrack + " out " +
			// out);
			if (out.isEmpty())
			{
				if (inTrack)
				{
					// Track failed

					// Original Metsker code
					// throwTrackException(last, p);

					// The problem with throwing an exception is that the
					// matching process is potentially prematurely terminated.
					// Instead, add a syntax error to the list of output
					// matches.
					boolean emptyParts = this.prepareErrorParts(last, p);
					if (emptyParts)
					{
						String message = "Track failed";
						out.addSyntaxError(new InternationalizableErrorMessage(
								message,
								ParserNames.KEY.MATH_EXCEPTION.EXCEPTION));
					}
					else
					{
						String message = "Track failed: "
								+ ParserNames.KEY.MATH_EXCEPTION.SEQUENCE_TRACK
								+ " after '" + after + "' expected '"
								+ expected + "' found '" + found + "'";
						out.addSyntaxError(new InternationalizableErrorMessage(
								message,
								ParserNames.KEY.MATH_EXCEPTION.SEQUENCE_TRACK,
								after, expected, found));
					}
					
					// Don't -display- an error message because this might only
					// be a tentative match, not the final match.
					// logger.debug("Track failed; parser: " + p + " out " +
					// out);

				}
				return out;
			}
			inTrack = true;
			last = out;
		}
		return out;
	}

	/**
	 * Prepare the error parts, showing how far the match had progressed, what
	 * it found next, and what it was expecting.
	 * 
	 * @return do parts exist: <code>true</code> iff at least one error part
	 *         is non-empty (i.e. <code>false</code> iff all are empty)
	 */
	private boolean prepareErrorParts(Matches previousState, Parser p)
	{
		boolean empty = true;
		// TODO: convert -nothing- and parser names from literal strings to
		// references to an error message + parser names' resource file
		Match best = this.best(previousState);
		after = best.consumed(" ");
		if (TextUtil.isEmptyString(after))
		{
			after = "-nothing-";
		}
		else
		{
			empty = false;
		}

		expected = p.toString();

		Object next = best.peek();
		if (next == null)
		{
			found = "-nothing-";
		}
		else
		{
			found = next.toString();
			empty = false;
		}

		return empty;
	}

	/**
	 * Throw an exception showing how far the match had progressed, what it
	 * found next, and what it was expecting.
	 */
	@Deprecated
	protected void throwTrackException(Matches previousState, Parser p)
	{
		this.prepareErrorParts(previousState, p);
		throw new MathParserTrackException(CommonNames.MISC.NONE, this
				.getName(), after, expected, found);
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

}
