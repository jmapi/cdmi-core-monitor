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
 * 1892103    2008-02-12  ebak         SLP improvements
 * 1949918    2008-04-08  raman_arora  Malformed service URL crashes SLP discovery
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp.internal.ua;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;

import org.sblim.slp.internal.TRC;
import org.sblim.slp.internal.msg.ReplyMessage;

/**
 * ResultTable
 * 
 */
public class ResultTable implements Iterator<Object> {

	private ArrayList<DatagramRequester> iRequesters = new ArrayList<DatagramRequester>();

	private ArrayList<Exception> iExceptions = new ArrayList<Exception>();

	/*
	 * iInSet contains the results which are not read out. iOutSet contains the
	 * results which are read out.
	 */
	private LinkedHashSet<Object> iInSet = new LinkedHashSet<Object>();

	private LinkedHashSet<Object> iOutSet = new LinkedHashSet<Object>();

	/**
	 * ResultTable has to know which Requesters provide results. Requester have
	 * to be registered before hasNext() is called.
	 * 
	 * @see #unregisterRequester(DatagramRequester)
	 * @param pReq
	 */
	public synchronized void registerRequester(DatagramRequester pReq) {
		for (int i = 0; i < this.iRequesters.size(); i++)
			if (pReq == this.iRequesters.get(i)) return;
		this.iRequesters.add(pReq);
	}

	/**
	 * If the Requester's sequence is completed, Requester has to be
	 * unregistered otherwise hasNext() will block.
	 * 
	 * @param pReq
	 */
	public synchronized void unregisterRequester(DatagramRequester pReq) {
		for (int i = 0; i < this.iRequesters.size(); i++) {
			if (pReq == this.iRequesters.get(i)) {
				this.iRequesters.remove(i);
				if (this.iRequesters.size() == 0) wakeUp();
				return;
			}
		}
	}

	/**
	 * addResults
	 * 
	 * @param pReplyMsg
	 */
	public void addResults(ReplyMessage pReplyMsg) {
		addResults(pReplyMsg.getResultIterator());
	}

	/**
	 * addResults
	 * 
	 * @param pResItr
	 */
	public synchronized void addResults(Iterator<?> pResItr) {
		if (pResItr == null) return;
		while (pResItr.hasNext())
			addResult(pResItr.next());
		// waking up hasNext()
		if (this.iInSet.size() > 0) wakeUp();
	}

	/**
	 * addExceptions
	 * 
	 * @param pReplyMsg
	 */
	public synchronized void addExceptions(ReplyMessage pReplyMsg) {
		addExceptions(pReplyMsg.getExceptionIterator());
	}

	/**
	 * addExceptions
	 * 
	 * @param pExceptionItr
	 */
	public synchronized void addExceptions(Iterator<?> pExceptionItr) {
		if (pExceptionItr == null) return;
		while (pExceptionItr.hasNext())
			addException((Exception) pExceptionItr.next());
	}

	/**
	 * addException
	 * 
	 * @param pE
	 */
	public synchronized void addException(Exception pE) {
		this.iExceptions.add(pE);
		if (this.iExceptions.size() > 0) wakeUp();
	}

	/**
	 * getTotalResponses
	 * 
	 * @return int
	 */
	public synchronized int getTotalResponses() {
		return this.iOutSet.size() + this.iInSet.size();
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 * @return true if there are results or exceptions to read
	 */
	public synchronized boolean hasNext() {
		if (hasData()) return true;
		// no more requester, no chance for result
		if (this.iRequesters.size() == 0) return false;
		/*
		 * wait wake up if iInSet is extended or all Requesters are unregistered
		 */
		try {
			wait();
		} catch (InterruptedException e) {
			TRC.error(e);
		}
		return hasData();
	}

	/**
	 * @see java.util.Iterator#next()
	 * @return a result or an Exception
	 */
	public synchronized Object next() throws NoSuchElementException {
		Iterator<Object> itr = this.iInSet.iterator();
		Object res = itr.next();
		this.iInSet.remove(res);
		this.iOutSet.add(res);
		return res;
	}

	/**
	 * @return next element in Exception table
	 * @throws NoSuchElementException
	 */
	public Object nextException() throws NoSuchElementException {
		Iterator<Exception> itr = this.iExceptions.iterator();
		Object res = itr.next();
		this.iExceptions.remove(res);
		return res;
	}

	/**
	 * @return next element in Exception table
	 */
	public boolean hasMoreExceptions() {
		return this.iExceptions.size() > 0;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void addResult(Object pResult) {
		if (this.iOutSet.contains(pResult) || this.iInSet.contains(pResult)) return;
		this.iInSet.add(pResult);
	}

	private void wakeUp() {
		try {
			notifyAll();
		} catch (IllegalMonitorStateException e) {
			TRC.error(e);
		}
	}

	private boolean hasData() {
		return this.iInSet.size() > 0;
	}

}
