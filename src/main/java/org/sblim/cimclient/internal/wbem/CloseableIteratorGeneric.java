/**
 * (C) Copyright IBM Corp. 2009
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
 * --------------------------------------------------------------------------
 * 2878054    2009-10-25  raman_arora  Pull Enumeration Feature (PULL Parser)
 */

package org.sblim.cimclient.internal.wbem;

import java.util.Iterator;

import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;

/**
 * Class CloseableIteratorGeneric creates new CloseableIterator from an Iterator
 * and WBEMException.
 * 
 * @param <E>
 *            : Type
 */
public class CloseableIteratorGeneric<E> implements CloseableIterator<Object> {

	private Iterator<E> iterator;

	private WBEMException iWBEMException;

	/**
	 * Ctor. : creates new CloseableIterator from an Iterator and WBEMException.
	 * 
	 * @param pIterator
	 *            : Iterator to be used in closeableIterator
	 * @param pException
	 *            : WBEMException thrown by parser (this can be null)
	 */
	public CloseableIteratorGeneric(Iterator<E> pIterator, WBEMException pException) {
		this.iterator = pIterator;
		this.iWBEMException = pException;
	}

	/**
	 * Ctor. : creates new CloseableIterator from an Iterator.
	 * 
	 * @param pIterator
	 *            : Iterator to be used in closeableIterator
	 */
	public CloseableIteratorGeneric(Iterator<E> pIterator) {
		this(pIterator, null);
	}

	public void close() {
		this.iterator = null;
		this.iWBEMException = null;
	}

	/**
	 * Returns WBEMException
	 * 
	 * @return WBEMException : This can be null
	 * 
	 */
	public WBEMException getWBEMException() {
		return this.iWBEMException;
	}

	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public Object next() {
		return this.iterator.next();
	}

	/**
	 * iterator.remove() is not supported
	 */
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove elements from iterator");
	}
}
