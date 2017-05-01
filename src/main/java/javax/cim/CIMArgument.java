/**
 * (C) Copyright IBM Corp. 2006, 2012
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 * 2944839    2010-02-08  blaschke-oss Remove redundant toString() methods
 * 3400209    2011-08-31  blaschke-oss Highlighted Static Analysis (PMD) issues
 * 3565581    2012-09-07  blaschke-oss TCK: remove unnecessary overriding methods
 */

package javax.cim;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:58 EST 2010
/**
 * This class represents an instance of a <code>CIMParameter</code> used for a
 * method invocation. A <code>CIMArgument</code> has a name, data type and
 * value. A <code>CIMArgument</code> corresponds to a <code>CIMParameter</code>
 * defined for a <code>CIMMethod</code>.
 * 
 * @param <E>
 *            Type parameter.
 * 
 * @see CIMParameter
 */
public class CIMArgument<E> extends CIMValuedElement<E> {

	private static final long serialVersionUID = 4727439564059428267L;

	/**
	 * Constructs a <code>CIMArgument</code> to be used for method invocations.
	 * A <code>CIMArgument</code> corresponds to a <code>CIMParameter</code>.
	 * For each <code>CIMParameter</code> being populated during a method
	 * invocation a <code>CIMArgument</code> object must be created.
	 * 
	 * @param pName
	 *            Name of the CIM argument.
	 * @param pType
	 *            <code>CIMDataType</code> of the argument.
	 * @param pValue
	 *            Value of the argument.
	 * @throws IllegalArgumentException
	 *             If the value does not match the type.
	 * @see CIMParameter
	 */
	public CIMArgument(String pName, CIMDataType pType, E pValue) throws IllegalArgumentException {
		super(pName, pType, pValue);
	}

	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code>
	 * and is a <code>CIMArgument</code> that represents the same name, type and
	 * value as this <code>CIMArgument</code>.
	 * 
	 * @param pObj
	 *            The object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof CIMArgument)) return false;
		return super.equals(pObj);
	}
}
