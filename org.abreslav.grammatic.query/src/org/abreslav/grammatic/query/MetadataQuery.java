/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.MetadataQuery#getAttributes <em>Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getMetadataQuery()
 * @generated
 */
public interface MetadataQuery extends Query {
	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link org.abreslav.grammatic.query.AttributeQuery}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' containment reference list.
	 * @see org.abreslav.grammatic.query.QueryPackage#getMetadataQuery_Attributes()
	 * @generated
	 */
	EList<AttributeQuery> getAttributes();

} // MetadataQuery
