/*******************************************************
 * Source File: ParserServiceProvider.java
 *******************************************************/
package net.ruready.parser.service.exports;

import net.ruready.common.discrete.EnumeratedFactory;
import net.ruready.common.rl.parser.ParserService;
import net.ruready.common.rl.parser.ParserServiceID;

/**
 * An factory that instantiates different parser service types.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version May 14, 2007
 */
public interface ParserServiceProvider extends
		EnumeratedFactory<ParserServiceID, ParserService>
{

}
