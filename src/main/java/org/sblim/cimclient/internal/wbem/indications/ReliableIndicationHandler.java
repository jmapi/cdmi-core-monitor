/**
 * (C) Copyright IBM Corp. 2011, 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Dave Blaschke, IBM, blaschke@us.ibm.com  
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 3288721    2011-05-20  blaschke-oss Need the function of indication reordering
 * 3376657    2011-07-24  blaschke-oss Get reliable indication properties once
 * 3484022    2012-02-08  blaschke-oss Turn reliable indication mode on and off based on SC/SN
 * 3513228    2012-04-23  blaschke-oss Reliable Indications support can create lots of threads
 */

package org.sblim.cimclient.internal.wbem.indications;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import javax.cim.CIMInstance;
import javax.cim.CIMProperty;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * <code>ReliableIndicationHandler</code> is responsible for determining when to
 * dispatch reliable indications, which includes queuing unexpected indications,
 * caching all indications for the duration of their sequence identifier
 * lifetime, and logging missing, duplicate and out-of-order indications. This
 * functionality is based on the changes first introduced by DSP1054 v1.1.
 * 
 * The <code>handleIndication</code>, <code>areAllEmpty</code> and
 * <code>processAll</code> methods need to be synchronized because they are the
 * entry points into <code>ReliableIndicationHandler</code> - the first is the
 * public entry point that needs to be passed each and every reliable
 * indication, the other two are only called by the DataManager thread and hence
 * protected. All of the private methods are only called from one of the three
 * synchronized methods.
 * 
 * NOTE: <code>ReliableIndicationHandler</code> does NOT contain any logic for
 * handling sequence number wrapping. It was deemed unnecessary because of the
 * performance impact to each and every indication for a VERY rare occurrence:
 * sequence numbers are longs (signed 64-bit integers) that are are supposed to
 * start at 0, so even if the listener handled 1,000,00 indications per second,
 * it would take over 292,471 years for the sequence number to wrap.
 */
public class ReliableIndicationHandler {

	/**
	 * <code>ReliableIndication</code> represents an entry in the indication
	 * delivery queue, and contains all the information required to manage and
	 * dispatch the indication. Entries in the queue are stored in order of
	 * increasing sequence number and are dispatched when the missing
	 * indication(s) arrive or their sequence identifier lifetime expires. Only
	 * one sequence context can be in the queue at any one time.
	 */
	private class ReliableIndication {

		private long iDiscardTime;

		private String iId;

		private CIMInstance iIndication;

		private InetAddress iInetAddress;

		private long iSequenceNumber;

		public ReliableIndication(CIMInstance pIndication, long pSequenceNumber, long pDiscardTime,
				String pId, InetAddress pInetAddress) {
			this.iIndication = pIndication;
			this.iSequenceNumber = pSequenceNumber;
			this.iDiscardTime = pDiscardTime;
			this.iId = pId;
			this.iInetAddress = pInetAddress;
		}

		public long getDiscardTime() {
			return this.iDiscardTime;
		}

		public String getId() {
			return this.iId;
		}

		public CIMInstance getIndication() {
			return this.iIndication;
		}

		public InetAddress getInetAddress() {
			return this.iInetAddress;
		}

		public long getSequenceNumber() {
			return this.iSequenceNumber;
		}
	}

	/**
	 * <code>CacheEntry</code> represents an entry in the sequence identifier
	 * cache. The sequence identifier is comprised of the sequence context and
	 * sequence number, and must be cached for the duration of the sequence
	 * identifier lifetime. Entries in the cache are stored in order of
	 * increasing discard time.
	 */
	private class CacheEntry {

		private String iSeqContext;

		private long iSeqNumber;

		private long iDiscardTime;

		public CacheEntry(String pSequenceContext, long pSequenceNumber, long pDiscardTime) {
			this.iSeqContext = pSequenceContext;
			this.iSeqNumber = pSequenceNumber;
			this.iDiscardTime = pDiscardTime;
		}

		public long getDiscardTime() {
			return this.iDiscardTime;
		}

		public String getSequenceContext() {
			return this.iSeqContext;
		}

		public long getSequenceNumber() {
			return this.iSeqNumber;
		}
	}

	private LinkedList<ReliableIndication> iQueue = new LinkedList<ReliableIndication>();

	private LinkedList<CacheEntry> iCache = new LinkedList<CacheEntry>();

	private CIMEventDispatcher iDispatcher;

	private LogAndTraceBroker iLogger = LogAndTraceBroker.getBroker();

	private long iIndentifierLifetime;

	// private long iLastArrivalTime;

	private String iLastSequenceContext;

	private Long iLastSequenceNumber;

	private long iExpectedSequenceNumber = 0;

	private boolean iIsFirstIndication = true;

	/**
	 * Constructs a <code>ReliableIndicationHandler</code> instance that uses
	 * the specified event dispatcher and sequence identifier lifetime to handle
	 * reliable indications.
	 * 
	 * @param pDispatcher
	 *            <code>CIMEventDispatcher</code> that does the actual
	 *            indication dispatching.
	 * @param pIdentiferLifetime
	 *            Sequence identifier lifetime.
	 */
	public ReliableIndicationHandler(CIMEventDispatcher pDispatcher, long pIdentiferLifetime) {
		this.iDispatcher = pDispatcher;
		this.iIndentifierLifetime = pIdentiferLifetime;
	}

	/**
	 * Adds a reliable indication to the indication delivery queue.
	 * 
	 * @param pIndication
	 *            Reliable indication to be queued for delivery.
	 */
	private void addToQueue(ReliableIndication pIndication) {
		int size = this.iQueue.size();

		if ((size == 0)
				|| (this.iQueue.getLast().getSequenceNumber()) < pIndication.getSequenceNumber()) {
			this.iQueue.addLast(pIndication);
		} else {
			int i;
			for (i = size - 1; i >= 0; i--) {
				ReliableIndication indication = this.iQueue.get(i);
				if (indication.getSequenceNumber() < pIndication.getSequenceNumber()) {
					this.iQueue.add(i + 1, pIndication);
					// printQueue();
					processQueue();
					return;
				}
			}
			this.iQueue.addFirst(pIndication);
		}

		// printQueue();
		// Process queue if there are two or more entries
		if (size > 0) processQueue();
	}

	/**
	 * Delivers all reliable indications in the indication delivery queue.
	 */
	private void flushQueue() {
		if (this.iQueue.isEmpty()) return;
		for (int i = this.iQueue.size() - 1; i >= 0; i--) {
			ReliableIndication indication = this.iQueue.removeFirst();
			logMissingQueueEntries(indication.getSequenceNumber());

			this.iDispatcher.dispatchEvent(new CIMEvent(indication.getIndication(), indication
					.getId(), indication.getInetAddress()));

			this.iExpectedSequenceNumber = indication.getSequenceNumber() + 1;
		}
	}

	/**
	 * Determines if the indication delivery queue is empty.
	 * 
	 * @return <code>true</code> if indication delivery queue is empty,
	 *         <code>false</code> otherwise.
	 */
	private boolean isQueueEmpty() {
		return this.iQueue.isEmpty();
	}

	/**
	 * Logs any missing reliable indications at the front of the indication
	 * delivery queue.
	 * 
	 * @param pSequenceNumber
	 *            Sequence number of first indication present in delivery queue.
	 */
	private void logMissingQueueEntries(long pSequenceNumber) {
		if (pSequenceNumber > this.iExpectedSequenceNumber) {
			for (long l = this.iExpectedSequenceNumber; l < pSequenceNumber; l++) {
				this.iLogger.trace(Level.FINE, "Missing indication #" + l + " detected");
			}
		}
	}

	/**
	 * Prints all reliable indications in the indication delivery queue.
	 */
	// private void printQueue() {
	// System.out.println("ReliableInidcation queue >");
	// for (int i = 0; i < this.iQueue.size(); i++) {
	// ReliableIndication indication = this.iQueue.get(i);
	// System.out.println("  Q[" + i + "]: " + indication.getSequenceNumber() +
	// "@"
	// + indication.getDiscardTime());
	// }
	// }
	/* */

	/**
	 * Processes the indication delivery queue and dispatches all reliable
	 * indications whose sequence identifier lifetime has expired or whose
	 * sequence numbers are in the expected order.
	 */
	private void processQueue() {
		if (this.iQueue.isEmpty()) return;
		long currentTime = System.currentTimeMillis();

		ReliableIndication indication = this.iQueue.getFirst();

		// First dispatch all indications whose lifetimes have expired
		while (indication.getDiscardTime() <= currentTime) {
			indication = this.iQueue.removeFirst();
			logMissingQueueEntries(indication.getSequenceNumber());

			this.iDispatcher.dispatchEvent(new CIMEvent(indication.getIndication(), indication
					.getId(), indication.getInetAddress()));

			this.iExpectedSequenceNumber = indication.getSequenceNumber() + 1;

			if (this.iQueue.isEmpty()) return;
			indication = this.iQueue.getFirst();
		}

		// Second dispatch all indications that are in the expected order
		while (indication.getSequenceNumber() == this.iExpectedSequenceNumber) {
			indication = this.iQueue.removeFirst();

			this.iDispatcher.dispatchEvent(new CIMEvent(indication.getIndication(), indication
					.getId(), indication.getInetAddress()));

			this.iExpectedSequenceNumber++;

			if (this.iQueue.isEmpty()) return;
			indication = this.iQueue.getFirst();
		}
	}

	/**
	 * Adds a reliable indication to the sequence identifier cache.
	 * 
	 * @param pSequenceContext
	 *            Sequence context of reliable indication to be cached.
	 * @param pSequenceNumber
	 *            Sequence number of reliable indication to be cached.
	 * @param pDiscardTime
	 *            Sequence identifier lifetime expiration time.
	 */
	private void addToCache(String pSequenceContext, long pSequenceNumber, long pDiscardTime) {
		CacheEntry newEntry = new CacheEntry(pSequenceContext, pSequenceNumber, pDiscardTime);
		int size = this.iCache.size();

		if ((size == 0) || (this.iCache.getLast().getDiscardTime() <= pDiscardTime)) {
			this.iCache.addLast(newEntry);
		} else {
			int i;
			for (i = size - 1; i >= 0; i--) {
				CacheEntry entry = this.iCache.get(i);
				if (entry.getDiscardTime() <= pDiscardTime) {
					this.iCache.add(i + 1, newEntry);
					// printCache();
					return;
				}
			}
			this.iCache.addFirst(newEntry);
		}

		// printCache();
	}

	/**
	 * Removes all entries in the sequence identifier cache.
	 */
	// private void flushCache() {
	// this.iCache.clear();
	// }
	/* */

	/**
	 * Determines if the sequence identifier cache is empty.
	 * 
	 * @return <code>true</code> if sequence identifier cache is empty,
	 *         <code>false</code> otherwise.
	 */
	private boolean isCacheEmpty() {
		return this.iCache.isEmpty();
	}

	/**
	 * Determines if the sequence context is in the sequence identifier cache.
	 * 
	 * @param pSequenceContext
	 *            Sequence context to look for.
	 * @return <code>true</code> if sequence context is in sequence identifier
	 *         cache, <code>false</code> otherwise.
	 */
	private boolean isInCache(String pSequenceContext) {
		if (this.iCache.isEmpty()) return false;
		Iterator<CacheEntry> iterator = this.iCache.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getSequenceContext().compareTo(pSequenceContext) == 0) return true;
		}
		return false;
	}

	/**
	 * Determines if the sequence identifier (context and number) is in the
	 * sequence identifier cache.
	 * 
	 * @param pSequenceContext
	 *            Sequence context to look for.
	 * @param pSequenceNumber
	 *            Sequence number to look for.
	 * @return <code>true</code> if sequence identifier is in sequence
	 *         identifier cache, <code>false</code> otherwise.
	 */
	private boolean isInCache(String pSequenceContext, long pSequenceNumber) {
		if (this.iCache.isEmpty()) return false;
		Iterator<CacheEntry> iterator = this.iCache.iterator();
		while (iterator.hasNext()) {
			CacheEntry entry = iterator.next();
			if ((entry.getSequenceContext().compareTo(pSequenceContext) == 0)
					&& (entry.getSequenceNumber() == pSequenceNumber)) return true;
		}
		return false;
	}

	/**
	 * Prints all entries in the sequence identifier cache.
	 */
	// private void printCache() {
	// System.out.println("ReliableIndication cache >");
	// for (int i = 0; i < this.iCache.size(); i++) {
	// CacheEntry entry = this.iCache.get(i);
	// System.out.println("  C[" + i + "]: " + entry.getSequenceContext() + ","
	// + entry.getSequenceNumber() + "@" + entry.getDiscardTime());
	// }
	// }
	/* */

	/**
	 * Processes the sequence identifier cache and removes all entries whose
	 * sequence identifier lifetime has expired.
	 */
	private void processCache() {
		if (this.iCache.isEmpty()) return;
		long currentTime = System.currentTimeMillis();

		for (int i = this.iCache.size() - 1; i >= 0; i--) {
			if (this.iCache.getFirst().getDiscardTime() > currentTime) break;
			this.iCache.removeFirst();
		}
	}

	/**
	 * Determines if both the indication delivery queue and sequence identifier
	 * cache are empty.
	 * 
	 * @return <code>true</code> if both the indication delivery queue and
	 *         sequence identifier cache are empty, <code>false</code>
	 *         otherwise.
	 */
	protected synchronized boolean areAllEmpty() {
		return isCacheEmpty() && isQueueEmpty();
	}

	/**
	 * Flushes both the indication delivery queue and sequence identifier cache.
	 */
	// private void flushAll() {
	// flushQueue();
	// flushCache();
	// }
	/* */

	/**
	 * Processes both the indication delivery queue and sequence identifier
	 * cache.
	 */
	protected synchronized void processAll() {
		processQueue();
		processCache();
	}

	/**
	 * Main worker routine for <code>ReliableIndicationHandler</code>. The
	 * indication is either sent directly to the <code>CIMEventDispatcher</code>
	 * or placed in the indication delivery queue to be dispatched later. All
	 * reliable indications are placed in the sequence identifier cache.
	 * 
	 * @param pIndication
	 *            Indication.
	 * @param pId
	 *            Indication destination URL.
	 * @param pInetAddress
	 *            Indication server IP.
	 */
	public synchronized void handleIndication(CIMInstance pIndication, String pId,
			InetAddress pInetAddress) {
		// Get current time
		long arrivalTime = System.currentTimeMillis();

		// Get reliable indication properties from indication
		CIMProperty<?> seqCtxProp = pIndication.getProperty("SequenceContext");
		CIMProperty<?> seqNumProp = pIndication.getProperty("SequenceNumber");

		// At this point indication is reliable or not reliable

		// Indication is not reliable, handle appropriately and deliver
		if (seqCtxProp == null || seqNumProp == null || seqCtxProp.getValue() == null
				|| seqNumProp.getValue() == null) {
			// Handle switch from reliable to not reliable
			if (this.iLastSequenceContext != null) {
				// Deliver all enqueued indications from previous context
				flushQueue();

				this.iLastSequenceContext = null;
				this.iLastSequenceNumber = null;
			}

			// Deliver indication
			this.iDispatcher.dispatchEvent(new CIMEvent(pIndication, pId, pInetAddress));
			this.iIsFirstIndication = false;
			return;
		}

		// At this point indication is reliable

		// Initial indication arrived, save knowledge about sequence identifier
		// and deliver
		if (this.iIsFirstIndication) {
			// Remember sequence context/number and arrival time
			this.iLastSequenceContext = (String) seqCtxProp.getValue();
			this.iLastSequenceNumber = (Long) seqNumProp.getValue();
			this.iExpectedSequenceNumber = this.iLastSequenceNumber.longValue() + 1;
			// this.iLastArrivalTime = arrivalTime;

			// Cache sequence identifier
			addToCache(this.iLastSequenceContext, this.iLastSequenceNumber.longValue(), arrivalTime
					+ this.iIndentifierLifetime);

			// Deliver indication
			this.iDispatcher.dispatchEvent(new CIMEvent(pIndication, pId, pInetAddress));
			this.iIsFirstIndication = false;
			return;
		}

		// At this point indication is reliable and previous indication was
		// reliable

		String seqCtx = (String) seqCtxProp.getValue();
		Long seqNum = (Long) seqNumProp.getValue();
		long seqNumVal = seqNum.longValue();

		// Indication arrived after sequence identifier lifetime of previous
		// indication expired, discard knowledge about previous sequence
		// identifier and deliver
		// if (arrivalTime > (this.iLastArrivalTime +
		// this.iIndentifierLifetime)) {
		// this.iLogger.trace(Level.FINE,
		// "Discarding knowledge of previous sequence identifier because lifetime expired");

		// Nothing should be cached or enqueued at this point
		// flushAll();

		// Remember sequence context/number and arrival time
		// this.iLastSequenceContext = seqCtx;
		// this.iLastSequenceNumber = seqNum;
		// this.iExpectedSequenceNumber = seqNumVal + 1;
		// this.iLastArrivalTime = arrivalTime;

		// Cache sequence identifier
		// addToCache(seqCtx, seqNumVal, arrivalTime +
		// this.iIndentifierLifetime);

		// Deliver indication
		// this.iDispatcher.dispatchEvent(new CIMEvent(pIndication, pId,
		// pInetAddress));
		// return;
		// }

		// Indication arrived with different sequence context than expected
		if (this.iLastSequenceContext == null || seqCtx.compareTo(this.iLastSequenceContext) != 0) {
			// Cached sequence context indicates this indication arrived
			// out-of-order from previous context, log and ignore
			if (isInCache(seqCtx)) {
				this.iLogger.trace(Level.FINE, "Out-of-order indication #" + seqNumVal
						+ " received from previous context; indication ignored, logged: "
						+ pIndication.toString());

				return;
			}

			this.iLogger.trace(Level.FINE,
					"Discarding knowledge of previous sequence identifier because context changed");

			// Deliver all enqueued indications from previous context
			flushQueue();

			// Remember sequence context and arrival time, sequence number
			// should be zero
			this.iLastSequenceContext = seqCtx;
			this.iLastSequenceNumber = Long.valueOf(0);
			this.iExpectedSequenceNumber = 1;
			// this.iLastArrivalTime = arrivalTime;

			// Cache sequence identifier
			addToCache(seqCtx, seqNumVal, arrivalTime + this.iIndentifierLifetime);

			// Expected sequence number, go ahead and deliver
			if (seqNumVal == 0) {
				this.iDispatcher.dispatchEvent(new CIMEvent(pIndication, pId, pInetAddress));
				return;
			}

			// Unexpected (non-zero) sequence number, enqueue
			this.iLastSequenceNumber = Long.valueOf(-1);
			this.iExpectedSequenceNumber = 0;
			addToQueue(new ReliableIndication(pIndication, seqNumVal, arrivalTime
					+ this.iIndentifierLifetime, pId, pInetAddress));

			return;
		}

		// Indication arrived with expected sequence number, go ahead and
		// deliver
		if (seqNumVal == this.iExpectedSequenceNumber) {
			// Remember sequence context/number and arrival time
			this.iLastSequenceNumber = seqNum;
			this.iExpectedSequenceNumber = seqNumVal + 1;
			// this.iLastArrivalTime = arrivalTime;

			// Cache sequence identifier
			addToCache(seqCtx, seqNumVal, arrivalTime + this.iIndentifierLifetime);

			// Deliver indication
			this.iDispatcher.dispatchEvent(new CIMEvent(pIndication, pId, pInetAddress));

			return;
		}

		// Duplicate indication arrived, log and ignore
		if (isInCache(seqCtx, seqNumVal)) {
			this.iLogger.trace(Level.FINE, "Duplicate indication #" + seqNumVal
					+ " received; indication ignored");

			// Cache sequence identifier (duplicate entries okay and much easier
			// than deleting/adding or moving)
			addToCache(seqCtx, seqNumVal, arrivalTime + this.iIndentifierLifetime);

			return;
		}

		// Out-of-order indication arrived, log and ignore
		if (seqNumVal < this.iExpectedSequenceNumber) {
			this.iLogger.trace(Level.FINE, "Out-of-order indication #" + seqNumVal + " received (#"
					+ this.iExpectedSequenceNumber + " expected); indication ignored, logged: "
					+ pIndication.toString());

			// Cache sequence identifier
			addToCache(seqCtx, seqNumVal, arrivalTime + this.iIndentifierLifetime);

			return;
		}

		// Indication with higher sequence number than expected received, cache
		// and enqueue it
		addToCache(seqCtx, seqNumVal, arrivalTime + this.iIndentifierLifetime);
		addToQueue(new ReliableIndication(pIndication, seqNumVal, arrivalTime
				+ this.iIndentifierLifetime, pId, pInetAddress));

		return;
	}
}
