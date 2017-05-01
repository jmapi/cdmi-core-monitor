/**
 * ThreadPool.java
 *
 * (C) Copyright IBM Corp. 2005, 2011
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1649779    2007-02-01  lupusalex    Indication listener threads freeze
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3206904    2011-03-11  lupusalex    Indication listener deadlock causes JVM to run out sockets
 */
package org.sblim.cimclient.internal.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class ThreadPool implements a pool that manages threads and executes
 * submitted tasks using this threads.
 * 
 */
public class ThreadPool {

	/**
	 * Class Worker implements a worker thread used by the thread pool
	 * 
	 */
	private static class Worker extends Thread {

		private volatile boolean iAlive;

		private final ThreadPool iPool;

		private Runnable iTask;

		private Thread iRunThread;

		/**
		 * Ctor.
		 * 
		 * @param pool
		 *            The owning pool
		 * @param name
		 *            The name of the thread
		 */
		public Worker(ThreadPool pool, String name) {
			super(pool.getGroup(), name);
			this.iPool = pool;
			setDaemon(true);
		}

		@Override
		public void start() {
			this.iAlive = true;
			super.start();
		}

		/**
		 * Kills the thread
		 */
		public void kill() {
			this.iAlive = false;
			if (this.iRunThread != null) {
				this.iRunThread.interrupt();
			}
		}

		/**
		 * Waits for a new task execute
		 * 
		 * @return The task or <code>null</code>
		 * @throws InterruptedException
		 */
		private Runnable waitForTask() throws InterruptedException {
			if (this.iTask != null) {
				Runnable tsk = this.iTask;
				this.iTask = null;
				return tsk;
			}
			if (this.iAlive && (this.iTask == null)) {
				this.iTask = this.iPool.getNextTask(this);
			}
			return null;
		}

		@Override
		public void run() {
			// System.out.println("new worker "+getId());
			this.iRunThread = Thread.currentThread();
			while (this.iAlive) {
				try {
					Runnable tsk = waitForTask();
					if (tsk != null) {
						this.iPool.taskStarted();
						try {
							tsk.run();
						} catch (Throwable t) {
							LogAndTraceBroker.getBroker().trace(Level.FINE,
									"Exception while executing task from thread pool", t);
						}
						this.iPool.taskCompleted();
					}
				} catch (InterruptedException e) {
					/**/
				}
			}
			this.iPool.removeWorker(this);
			// System.out.println("dead worker "+getId());
		}
	}

	private ThreadGroup iGroup;

	private AtomicInteger iIdleThreads = new AtomicInteger(0);

	private List<Worker> iThreadPool = new LinkedList<Worker>();

	private BlockingQueue<Runnable> iQueue = new LinkedBlockingQueue<Runnable>();

	private long iIdleTimeout;

	private int iMaxPoolSize;

	private int iMinPoolSize;

	private int iToleratedBacklog;

	private int iCntr = 0;

	private boolean iShutdown = false;

	private String iWorkerName;

	/**
	 * Ctor
	 * 
	 * @param pMinPoolSize
	 *            The minimal pool size. The pool will always keep at least this
	 *            number of worker threads alive even in no load situations.
	 * @param pMaxPoolSize
	 *            The maximal pool size. The pool will create up to that number
	 *            of worker threads on heavy load.
	 * @param pToleratedBacklog
	 *            The task backlog that is tolerated before an additional worker
	 *            is created
	 * @param pToleratedIdle
	 *            The idle time of a worker that is tolerated before the worker
	 *            is destroyed
	 * @param pGroup
	 *            Then thread group to put the worker threads in
	 * @param pWorkerName
	 *            The name to use for worker threads
	 */
	public ThreadPool(int pMinPoolSize, int pMaxPoolSize, int pToleratedBacklog,
			long pToleratedIdle, ThreadGroup pGroup, String pWorkerName) {
		this.iGroup = pGroup != null ? pGroup : new ThreadGroup("TreadPool Group");
		this.iMinPoolSize = pMinPoolSize;
		this.iMaxPoolSize = pMaxPoolSize;
		this.iToleratedBacklog = pToleratedBacklog;
		this.iIdleTimeout = pToleratedIdle;
		this.iWorkerName = pWorkerName != null ? pWorkerName : "Worker ";
	}

	/**
	 * Submits a task for execution
	 * 
	 * @param task
	 *            The task
	 * @return <code>true</code> if the task was executed or enqueued,
	 *         <code>false</code> otherwise.
	 */
	public synchronized boolean execute(Runnable task) {
		if (this.iShutdown) return false;

		for (int i = this.iThreadPool.size(); i < this.iMinPoolSize; ++i) {
			createWorker();
		}

		int totalIdle = this.iIdleThreads.get();

		boolean added = this.iQueue.offer(task);

		if (totalIdle > 0) { return added; }

		// is maximal pool size reached ?
		boolean mayCreateWorker = (this.iMaxPoolSize == -1)
				|| (this.iThreadPool.size() < this.iMaxPoolSize);
		// create a new worker when backlog exceeds toleration level or we
		// have no workers at all
		if (mayCreateWorker
				&& ((this.iQueue.size() > this.iToleratedBacklog) || this.iThreadPool.size() == 0)) {
			createWorker();
		}
		return added;
	}

	/**
	 * Creates a new worker thread
	 * 
	 */
	private synchronized void createWorker() {
		Worker worker = new Worker(this, this.iWorkerName + getID());
		this.iThreadPool.add(worker);
		this.iIdleThreads.incrementAndGet();
		worker.start();
	}

	/**
	 * Removes a worker from the pool.
	 * 
	 * @param worker
	 *            The worker
	 */
	protected synchronized void removeWorker(Worker worker) {
		if (worker != null && this.iThreadPool != null) {
			this.iIdleThreads.decrementAndGet();
			this.iThreadPool.remove(worker);

			// handle race condition where last worker calls removeWorker()
			// parallel to call into execute()
			if (this.iThreadPool.isEmpty() && !this.iQueue.isEmpty()) {
				// System.out.print("bingo");
				createWorker();
			}
		}
	}

	/**
	 * Gets the associated thread group
	 * 
	 * @return The thread group
	 */
	protected ThreadGroup getGroup() {
		return this.iGroup;
	}

	/**
	 * Get a new task. If no task was available during the timeout period the
	 * calling worker might be killed if more than the minimum number of workers
	 * exist
	 * 
	 * @param worker
	 *            The worker asking for a new task
	 * @return The next available task from the queue. If no task is available
	 *         waits for idle timeout and returns null afterwards.
	 * @throws InterruptedException
	 *             on interrupt
	 */
	public Runnable getNextTask(Worker worker) throws InterruptedException {
		Runnable task = this.iQueue.poll(this.iIdleTimeout, TimeUnit.MILLISECONDS);
		if (task == null && this.iThreadPool.size() > this.iMinPoolSize) {
			worker.kill();
		}
		return task;
	}

	/**
	 * Notifies the pool that at task was started. Effectively decrements the
	 * idle worker count.
	 */
	public void taskStarted() {
		/* int idleCount = */this.iIdleThreads.decrementAndGet();
		// if (idleCount < 0) System.err.println("idlecount negative");
	}

	/**
	 * Notifies the pool that at task was completed. Effectively increments the
	 * idle worker count.
	 */
	public void taskCompleted() {
		/* int idleCount = */this.iIdleThreads.incrementAndGet();
		// if (idleCount > this.iThreadPool.size())
		// System.err.println("idlecount exceeds total workers");
	}

	/**
	 * Shuts down the thread pool and all workers
	 */
	public synchronized void shutdown() {
		if (!this.iShutdown) {
			this.iShutdown = true;
			if (this.iThreadPool != null) {
				List<Worker> workers = this.iThreadPool;
				this.iThreadPool = null;
				for (Worker worker : workers) {
					worker.kill();
				}
			}
		}
	}

	/**
	 * Generates an &quot;unique&quot; id for a worker thread
	 * 
	 * @return The id
	 */
	private String getID() {
		if (++this.iCntr >= 10000) this.iCntr = 1;
		return String.valueOf(this.iCntr);
	}

}
