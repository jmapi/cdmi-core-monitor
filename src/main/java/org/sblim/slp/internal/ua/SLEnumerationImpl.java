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
 * @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 1804402    2007-11-10  ebak         IPv6 ready SLP - revision 4
 * 1892103    2008-02-12  ebak         SLP improvements
 * 1949918    2008-04-08  raman_arora  Malformed service URL crashes SLP discovery
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sblim.slp.internal.ua;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.sblim.slp.ServiceLocationEnumeration;
import org.sblim.slp.ServiceLocationException;
import org.sblim.slp.ServiceURL;
import org.sblim.slp.internal.SLPDefaults;
import org.sblim.slp.internal.TRC;
import org.sblim.slp.internal.msg.DADescriptor;
import org.sblim.slp.internal.msg.RequestMessage;
import org.sblim.slp.internal.msg.ServiceRequest;

/**
 * SLEnumerationImpl
 * 
 */
public class SLEnumerationImpl implements ServiceLocationEnumeration {

	private RequestMessage iReqMsg;

	private ResultTable iResultTable;

	private List<InetAddress> iDAList;

	private boolean iInited = false;

	private boolean iIsDASrvRequest;

	private DatagramRequester iMCastRequester;

	/**
	 * Ctor.
	 * 
	 * @param pReqMsg
	 * @param pDAList
	 */
	public SLEnumerationImpl(RequestMessage pReqMsg, List<InetAddress> pDAList) {
		this.iReqMsg = pReqMsg;
		this.iResultTable = new ResultTable();
		this.iDAList = pDAList;
		this.iIsDASrvRequest = isDASrvRequest();
	}

	/**
	 * This implementation can throw RuntimeExceptions. They can be ignored or
	 * used for analysis.
	 * 
	 * @see org.sblim.slp.ServiceLocationEnumeration#next()
	 */
	public Object next() throws NoSuchElementException {
		Object obj = this.iResultTable.next();
		if (obj instanceof Exception) throw new RuntimeException((Exception) obj);
		if (this.iIsDASrvRequest) {
			// DADescriptor is internal -> converting to ServiceURL
			DADescriptor daDesc = (DADescriptor) obj;
			return new ServiceURL(daDesc.getURL(), ServiceURL.LIFETIME_MAXIMUM);
		}
		return obj;
	}

	/**
	 * @return next Object in Exception table
	 * @throws NoSuchElementException
	 * 
	 *             This in internal implementation to get list of all exceptions
	 *             thrown/caught by parser This can throw RuntimeExceptions.
	 *             They can be ignored or used for analysis.
	 * 
	 *             use hasNextException to check whether there exists another
	 *             element in Exception table
	 */
	public Object nextException() throws NoSuchElementException {
		return this.iResultTable.nextException();
	}

	/**
	 * @return true if there exists another element in Exception table
	 * 
	 */
	public boolean hasMoreExceptions() {
		return this.iResultTable.hasMoreExceptions();
	}

	/*
	 * states: - init - initiated - finished
	 */
	public boolean hasMoreElements() {
		if (!this.iInited) {
			List<InetAddress> daList = null;
			/*
			 * OpenSLP DA doesn't reply to unicasted DA discovery, therefore if
			 * the discoverable service type is service:directory-agent a
			 * multicasting is done.
			 */
			if (!this.iIsDASrvRequest) {
				try {
					daList = getDAList(this.iReqMsg.getScopeList());
				} catch (Exception e) {
					throw new RuntimeException(new ServiceLocationException(
							ServiceLocationException.INTERNAL_ERROR, e));
				}
			}
			try {
				if (daList == null || daList.size() == 0) {
					setupMulticasting();
				} else {
					setupUnicasting(daList);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			this.iInited = true;
		}
		return this.iResultTable.hasNext();
	}

	public Object nextElement() throws NoSuchElementException {
		return next();
	}

	/**
	 * For diagnostic only!
	 * 
	 * @return int
	 */
	public int getPort() {
		return this.iMCastRequester == null ? -1 : this.iMCastRequester.getPort();
	}

	private boolean isDASrvRequest() {
		if (!(this.iReqMsg instanceof ServiceRequest)) return false;
		ServiceRequest srvReq = (ServiceRequest) this.iReqMsg;
		return SLPDefaults.DA_SERVICE_TYPE.equals(srvReq.getServiceType());
	}

	/**
	 * @param pScopes
	 * @return List of DA address InetAddresses
	 * @throws UnknownHostException
	 */
	private List<InetAddress> getDAList(List<String> pScopes) throws UnknownHostException,
			IOException {
		if (this.iDAList != null && this.iDAList.size() > 0) return this.iDAList;

		// return cached DA list from previous discovery
		List<String> scopes = DACache.getDiscoverableScopeList(pScopes);
		if (scopes != null) {
			ResultTable resultTable = new ResultTable();
			ServiceRequest srvReq = new ServiceRequest(null, SLPDefaults.DA_SERVICE_TYPE, scopes,
					null, null);
			// multicast DA discovery
			DatagramRequester requester = new DatagramRequester(srvReq, resultTable);
			requester.start(false);
			if (resultTable.hasNext()) {
				List<DADescriptor> daList = new ArrayList<DADescriptor>();
				while (resultTable.hasNext()) {
					try {
						daList.add((DADescriptor) resultTable.next());
					} catch (RuntimeException e) {
						TRC.warning(e.getMessage(), e);
					}
				}
				DACache.setDAList(scopes, daList);
			}
		}
		return getInetAddresses(DACache.getDAList(pScopes));
	}

	private List<InetAddress> getInetAddresses(List<String> pAddrStrList) {
		if (pAddrStrList == null) return null;
		List<InetAddress> list = new ArrayList<InetAddress>(pAddrStrList.size());
		Iterator<String> strItr = pAddrStrList.iterator();
		while (strItr.hasNext()) {
			String srvURLStr = strItr.next();
			try {
				ServiceURL srvURL = new ServiceURL(srvURLStr, ServiceURL.LIFETIME_DEFAULT);
				list.add(InetAddress.getByName(srvURL.getURLPath()));
			} catch (Exception e) {
				TRC.error("Failed to get InetAddress for srvURLStr=" + srvURLStr, e);
			}
		}
		TRC.info("number of discovered DAs:" + list.size());
		return list;
	}

	private void setupUnicasting(List<InetAddress> pDAList) throws IOException {
		Iterator<InetAddress> itr = pDAList.iterator();
		while (itr.hasNext()) {
			InetAddress address = itr.next();
			new DatagramRequester(this.iReqMsg, this.iResultTable, address).start(true);
		}
	}

	private void setupMulticasting() throws ServiceLocationException, IOException {
		try {
			this.iMCastRequester = new DatagramRequester(this.iReqMsg, this.iResultTable);
			this.iMCastRequester.start(true);
		} catch (UnknownHostException e) {
			throw new ServiceLocationException(ServiceLocationException.NETWORK_ERROR, e);
		}
	}

}
