/**
 * (C) Copyright IBM Corp. 2007, 2009
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
 * 1678915    2007-03-12  lupusalex    Integrated WBEM service discovery via SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.cimclient.internal.discovery.slp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.cim.CIMObjectPath;
import javax.security.auth.Subject;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientFactory;

import org.sblim.cimclient.discovery.WBEMServiceAdvertisement;
import org.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sblim.slp.ServiceURL;

/**
 * Class WBEMServiceAdvertisementSLP is the SLP specific implementation if the
 * WBEMServiceAdvertisement interface.
 * 
 * @invariant iDA != null && iServiceUrl != null && iAttributeMap != null
 * @since 2.0.2
 */
public class WBEMServiceAdvertisementSLP implements WBEMServiceAdvertisement {

	private static final Pattern ATTR_PATTERN = Pattern.compile("[(]?(.+)=([^)]+)[)]?");

	private String iDA;

	private ServiceURL iServiceUrl;

	private Map<String, String> iAttributeMap;

	private boolean iExpired = false;

	/**
	 * Ctor.
	 * 
	 * @param pDA
	 *            The Directory Agent from which this advertisement was received
	 * @param pUrl
	 *            The SLP service url returned by Locator.findServices().
	 * @param pAttributes
	 *            The attribute list (List&lt;String&gt;) where each entry looks
	 *            either like this <code>key=value</code> or this
	 *            <code>(key=value)</code>.
	 */
	public WBEMServiceAdvertisementSLP(String pDA, ServiceURL pUrl, List<String> pAttributes) {
		if (pDA == null || pUrl == null) throw new IllegalArgumentException(
				"Directory and URL must not be null");
		this.iDA = pDA;
		this.iServiceUrl = pUrl;
		parseAttributes(pAttributes);

	}

	private void parseAttributes(List<String> pAttributes) {
		this.iAttributeMap = new HashMap<String, String>();
		Iterator<String> iter = pAttributes.iterator();
		while (iter.hasNext()) {
			String attribute = iter.next();
			Matcher matcher = ATTR_PATTERN.matcher(attribute);
			if (matcher.matches()) {
				String key = matcher.group(1).trim();
				String value = matcher.group(2).trim();
				this.iAttributeMap.put(key, value);
			} else {
				LogAndTraceBroker.getBroker().trace(Level.FINE,
						"SLP discovery returned invalid attribute: " + attribute);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.discovery.WbemService#getAttribute(java.lang.String)
	 */
	public String getAttribute(String pAttributeName) {
		return this.iAttributeMap.get(pAttributeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sblim.cimclient.discovery.WbemService#getAttributes()
	 */
	public Set<Entry<String, String>> getAttributes() {
		return this.iAttributeMap.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sblim.cimclient.discovery.WbemService#getConcreteServiceType()
	 */
	public String getConcreteServiceType() {
		return this.iServiceUrl.getServiceType().getConcreteTypeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sblim.cimclient.discovery.WbemService#getServiceUrl()
	 */
	public String getServiceUrl() {
		return getConcreteServiceType() + "://" + this.iServiceUrl.getHost() + ":"
				+ String.valueOf(this.iServiceUrl.getPort());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.discovery.WBEMServiceAdvertisement#createClient(javax
	 * .security.auth.Subject, java.util.Locale[])
	 */
	public WBEMClient createClient(Subject pSubject, Locale[] pLocales) throws Exception {
		String communication = getAttribute(COMM_MECHANISM);
		if (communication.equalsIgnoreCase("OTHER")) {
			communication = getAttribute(OTHER_COMM_MECHN_DESC);
		}
		WBEMClient client = WBEMClientFactory.getClient(communication);
		CIMObjectPath path = new CIMObjectPath(this.iServiceUrl.getServiceType()
				.getConcreteTypeName(), this.iServiceUrl.getHost(), String.valueOf(this.iServiceUrl
				.getPort()), null, null, null);
		client.initialize(path, pSubject, pLocales);
		return client;
	}

	public String getDirectory() {
		return this.iDA;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		// buffer.append(iServiceUrl.getServiceType());
		// buffer.append("://");
		// buffer.append(iServiceUrl.getHost());
		buffer.append(getServiceUrl());
		buffer.append("{slp da=\"");
		buffer.append(this.iDA);
		buffer.append("\", attributes=[");
		Iterator<Entry<String, String>> iter = this.iAttributeMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			buffer.append("\"");
			buffer.append(entry.getKey());
			buffer.append("=");
			buffer.append(entry.getValue());
			buffer.append("\"");
			if (iter.hasNext()) buffer.append(", ");
		}
		buffer.append("]}");
		return buffer.toString();
	}

	public String[] getInteropNamespaces() {
		return parseCommaSeparatedList(getAttribute(INTEROP_NS));
	}

	private String[] parseCommaSeparatedList(String pAttribute) {
		String[] split = pAttribute.split("[,\\n]");
		int emptyCount = 0;
		for (int i = 0; i < split.length; ++i) {
			split[i] = split[i].trim();
			if (split[i].length() == 0) {
				++emptyCount;
			}
		}
		if (emptyCount > 0) {
			String[] result = new String[split.length - emptyCount];
			int j = 0;
			for (int i = 0; i < split.length; ++i) {
				if (split[i].length() > 0) {
					result[j++] = split[i];
				}
			}
			return result;
		}
		return split;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.discovery.WBEMServiceAdvertisement#getServiceId()
	 */
	public String getServiceId() {
		return getAttribute(SERVICE_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sblim.cimclient.discovery.WBEMServiceAdvertisement#isExpired()
	 */
	public boolean isExpired() {
		return this.iExpired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.discovery.WBEMServiceAdvertisement#setExpired(boolean
	 * )
	 */
	public void setExpired(boolean pExpired) {
		this.iExpired = pExpired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pObj) {
		if (pObj == this) { return true; }
		if (pObj != null && pObj instanceof WBEMServiceAdvertisement) {
			WBEMServiceAdvertisement that = (WBEMServiceAdvertisement) pObj;
			return getServiceUrl().equals(that.getServiceUrl())
					&& getDirectory().equals(that.getDirectory());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.iServiceUrl.hashCode() + this.iDA.hashCode();
	}
}
