/**
 * (C) Copyright IBM Corp. 2006, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1565892    2006-11-14  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2787464    2009-05-05  blaschke-oss lang exception in Chinese env with Java client 2.0.7
 */
package org.sblim.cimclient.internal.logging;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Class MessageLoader encapsulates the access to the resource file containing
 * log messages.
 * 
 */
public final class MessageLoader {

	private static final String BUNDLE_NAME = "org.sblim.cimclient.internal.logging.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE_LOCAL
	/* = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()) */;

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,
			Locale.ENGLISH);

	/*
	 * Use English if default locale not supported (doing it here handles
	 * exception, assigning above does not)
	 */
	static {
		ResourceBundle bundleLocal;
		try {
			bundleLocal = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
		} catch (MissingResourceException e) {
			bundleLocal = RESOURCE_BUNDLE;
		}
		RESOURCE_BUNDLE_LOCAL = bundleLocal;
	}

	private MessageLoader() {
	// prevent instantiation
	}

	/**
	 * Returns the English message for a given key.
	 * 
	 * @param pKey
	 *            The key
	 * @return The message
	 */
	public static String getMessage(String pKey) {
		try {
			return RESOURCE_BUNDLE.getString(pKey);
		} catch (MissingResourceException e) {
			return '!' + pKey + '!';
		}
	}

	/**
	 * Returns the localized message for a given key.
	 * 
	 * @param pKey
	 *            The key
	 * @return The message
	 */
	public static String getLocalizedMessage(String pKey) {
		try {
			return RESOURCE_BUNDLE_LOCAL.getString(pKey);
		} catch (MissingResourceException e) {
			return '!' + pKey + '!';
		}
	}

	/**
	 * Returns the level of a given message id based on the naming convention.
	 * The convention is:
	 * <code>CIM&lt;unique 4 digit id&gt;&lt;level token&gt;</code>, e.g.
	 * <code>CIM1234S</code>. Valid level tokens are <code>S, W, I</code> and
	 * <code>C</code> for <code>SEVERE, WARNING, INFO</code> and
	 * <code>CONFIG</code> respectively.
	 * 
	 * @param pKey
	 *            The message id
	 * @return The level
	 */
	public static Level getLevel(String pKey) {
		char level = pKey.charAt(pKey.length() - 1);
		switch (level) {
			case 'S':
				return Level.SEVERE;
			case 'W':
				return Level.WARNING;
			case 'I':
				return Level.INFO;
			case 'C':
				return Level.CONFIG;
			default:
				return null;
		}
	}
}
