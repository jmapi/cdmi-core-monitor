/**
 * (C) Copyright IBM Corp. 2006, 2010
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Ramandeep S Arora, IBM, arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 2959264    2010-02-25  blaschke-oss Sync up javax.client.* javadoc with JSR48 1.0.0
 */
package javax.wbem.client;

import javax.wbem.CloseableIterator;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * This class is a container that stores the information from a Pull request.
 * 
 * @param <E>
 *            Type parameter.
 */
public class EnumerateResponse<E> {

	private String iContext;

	private CloseableIterator<E> iResponses;

	private boolean iEnd;

	/**
	 * Creates an <code>EnumerateResponse</code>.
	 * 
	 * @param pContext
	 *            The enumeration context returned. This will be used for any
	 *            future calls for this particular enumeration.
	 * @param pResponses
	 *            The results of the operation.
	 * @param pEnd
	 *            <code>true</code> if this is the last of the results;
	 *            <code>false</code> otherwise.
	 */
	public EnumerateResponse(String pContext, CloseableIterator<E> pResponses, boolean pEnd) {
		this.iContext = pContext;
		this.iResponses = pResponses;
		this.iEnd = pEnd;
	}

	/**
	 * Get the context that can be used for a subsequent pull request.
	 * 
	 * @return The Enumeration Context returned from the server.
	 */
	public String getContext() {
		return this.iContext;
	}

	/**
	 * Get the <code>CloseableIterator</code> for the returned CIM Elements.
	 * 
	 * @return <code>CloseableIterator</code> for the elements returned.
	 */
	public CloseableIterator<E> getResponses() {
		return this.iResponses;
	}

	/**
	 * If <code>true</code>, there are no more elements to be returned.
	 * 
	 * @return <code>true</code> if this is the last of the results;
	 *         <code>false</code> otherwise.
	 */
	public boolean isEnd() {
		return this.iEnd;
	}

	/**
	 * This iterates over the responses. As a side effect,
	 * CloseableIterator.hasNext() will not return anything after this is
	 * called.
	 * 
	 * public void debug() { CloseableIterator<E> iterator =
	 * this.getResponses(); while (iterator.hasNext())
	 * System.out.println(iterator.next());
	 * 
	 * System.out.println("Enum Context =" + this.getContext());
	 * System.out.println("Is End = " + this.isEnd());
	 * System.out.println("Done...\n"); }
	 */
}
