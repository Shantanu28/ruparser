/*****************************************************************************************
 * Source File: MinimalStandAloneResourceLocator.java
 ****************************************************************************************/
package test.ruready.parser.imports;

import net.ruready.common.rl.MinimalResourceLocator;
import net.ruready.common.rl.ResourceLocator;
import net.ruready.common.rl.parser.ParserService;
import net.ruready.common.rl.parser.ParserServiceID;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.service.exports.ParserServiceProvider;
import net.ruready.parser.service.exports.SimpleParserServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A minimal stand-alone resource locator for the testing package of the parser library.
 * Only initializes logging. No database source. Is basically the minimal resource locator
 * with additional parser services.
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
 * @version Aug 13, 2007
 */
public class ParserResourceLocator extends MinimalResourceLocator
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ParserResourceLocator.class);

	/**
	 * Singleton instance this class.
	 */
	private static volatile ResourceLocator instance;

	/**
	 * A factory of parser services.
	 */
	private ParserServiceProvider parserServiceProvider;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * This is a singleton. Prevents instantiation.
	 */
	protected ParserResourceLocator()
	{
		super();
	}

	/**
	 * Return the single instance of this type.
	 * 
	 * @return the single instance of this type
	 */
	public static ResourceLocator getInstance()
	{
		if (instance == null)
		{
			synchronized (ParserResourceLocator.class)
			{
				if (instance == null)
				{
					instance = new ParserResourceLocator();
					// Load from properties files whose name is hard-coded in
					// the Names class
					instance.setUp(ParserNames.RESOURCE_LOCATOR.PARSER_CONFIG_FILE);
				}
			}
		}
		return instance;
	}

	// ========================= IMPLEMENTATION: ResourceLocator ===========

	/**
	 * Set up: configure and initialize the resource locators using a properties file to
	 * specify the type of objects this locator should find.
	 * 
	 * @param configFileName
	 *            properties file name to be searched for in the class path
	 */
	@Override
	synchronized public void setUp(final String configFileName)
	{
		super.setUp(configFileName);

		// Initialize the parser service provider
		parserServiceProvider = new SimpleParserServiceProvider();
	}

	/**
	 * @see net.ruready.common.rl.ResourceLocator#getParserService(net.ruready.common.rl.parser.ParserServiceID,
	 *      java.lang.Object[])
	 */
	@Override
	public ParserService getParserService(final ParserServiceID parserServiceIdentifier,
			Object... args)
	{
		return parserServiceProvider.createType(parserServiceIdentifier, args);
	}

	// ========================= METHODS ===================================

}
