/*******************************************************
 * Source File: TargetPrinter.java
 *******************************************************/
package net.ruready.parser.port.output.manager;

/**
 * Print a marked syntax tree within a target into an output stream.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 13, 2007
 */
public interface TargetPrinter
{
	// ========================= CONSTANTS =================================

	/**
	 * Return the output stream constructed for the target and syntax tree.
	 * 
	 * @return output stream representing the syntax tree
	 */
	Object getOutput();

}
