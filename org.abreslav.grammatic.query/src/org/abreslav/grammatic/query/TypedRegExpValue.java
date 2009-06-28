/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.abreslav.grammatic.metadata.Value;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Typed Reg Exp Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.TypedRegExpValue#getWildCardSeparatedFragments <em>Wild Card Separated Fragments</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getTypedRegExpValue()
 * @generated
 */
public interface TypedRegExpValue extends AttributeValueQuery {
	/**
	 * Returns the value of the '<em><b>Wild Card Separated Fragments</b></em>' containment reference list.
	 * The list contents are of type {@link org.abreslav.grammatic.metadata.Value}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wild Card Separated Fragments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wild Card Separated Fragments</em>' containment reference list.
	 * @see org.abreslav.grammatic.query.QueryPackage#getTypedRegExpValue_WildCardSeparatedFragments()
	 * @generated
	 */
	EList<Value> getWildCardSeparatedFragments();

} // TypedRegExpValue
