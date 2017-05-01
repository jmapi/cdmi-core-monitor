/**
 * ServiceLocationEnumeration.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
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
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 1949918    2008-04-08  raman_arora  Malformed service URL crashes SLP discovery
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sblim.slp;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * The ServiceLocationEnumeration class is the return type for all Locator SLP
 * operations. The Java API library may implement this class to block until
 * results are available from the SLP operation, so that the client can achieve
 * asynchronous operation by retrieving results from the enumeration in a
 * separate thread. Clients use the superclass nextElement() method if they are
 * unconcerned with SLP exceptions (this method will never ever throw one).
 */
public interface ServiceLocationEnumeration extends Enumeration<Object> {

	/**
	 * Return the next value or block until it becomes available.
	 * 
	 * @return The next value
	 * @throws ServiceLocationException
	 *             Thrown if the SLP operation encounters an error.
	 * @throws NoSuchElementException
	 *             If there are no more elements to return.
	 */
	public abstract Object next() throws ServiceLocationException, NoSuchElementException;

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
	public abstract Object nextException() throws NoSuchElementException;

	/**
	 * @return true if there exists another element in Exception table
	 * 
	 */
	public abstract boolean hasMoreExceptions();
}
