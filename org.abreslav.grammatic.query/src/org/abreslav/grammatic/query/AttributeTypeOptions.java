/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Attribute Type Options</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeTypeOptions()
 * @generated
 */
public enum AttributeTypeOptions implements Enumerator {
	/**
	 * The '<em><b>STRING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STRING_VALUE
	 * @generated
	 * @ordered
	 */
	STRING(0, "STRING", "STRING"),

	/**
	 * The '<em><b>INTEGER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INTEGER_VALUE
	 * @generated
	 * @ordered
	 */
	INTEGER(1, "INTEGER", "INTEGER"),

	/**
	 * The '<em><b>ID</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ID_VALUE
	 * @generated
	 * @ordered
	 */
	ID(2, "ID", "ID"),

	/**
	 * The '<em><b>PUNCTUATION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PUNCTUATION_VALUE
	 * @generated
	 * @ordered
	 */
	PUNCTUATION(3, "PUNCTUATION", "PUNCTUATION"),

	/**
	 * The '<em><b>TUPLE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TUPLE_VALUE
	 * @generated
	 * @ordered
	 */
	TUPLE(4, "TUPLE", "TUPLE"),

	/**
	 * The '<em><b>MULTI</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MULTI_VALUE
	 * @generated
	 * @ordered
	 */
	MULTI(5, "MULTI", "MULTI"),

	/**
	 * The '<em><b>EXPRESSION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EXPRESSION_VALUE
	 * @generated
	 * @ordered
	 */
	EXPRESSION(6, "EXPRESSION", "EXPRESSION");

	/**
	 * The '<em><b>STRING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STRING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STRING
	 * @generated
	 * @ordered
	 */
	public static final int STRING_VALUE = 0;

	/**
	 * The '<em><b>INTEGER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INTEGER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INTEGER
	 * @generated
	 * @ordered
	 */
	public static final int INTEGER_VALUE = 1;

	/**
	 * The '<em><b>ID</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ID</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ID
	 * @generated
	 * @ordered
	 */
	public static final int ID_VALUE = 2;

	/**
	 * The '<em><b>PUNCTUATION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PUNCTUATION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PUNCTUATION
	 * @generated
	 * @ordered
	 */
	public static final int PUNCTUATION_VALUE = 3;

	/**
	 * The '<em><b>TUPLE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TUPLE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TUPLE
	 * @generated
	 * @ordered
	 */
	public static final int TUPLE_VALUE = 4;

	/**
	 * The '<em><b>MULTI</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MULTI</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MULTI
	 * @generated
	 * @ordered
	 */
	public static final int MULTI_VALUE = 5;

	/**
	 * The '<em><b>EXPRESSION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EXPRESSION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EXPRESSION
	 * @generated
	 * @ordered
	 */
	public static final int EXPRESSION_VALUE = 6;

	/**
	 * An array of all the '<em><b>Attribute Type Options</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final AttributeTypeOptions[] VALUES_ARRAY =
		new AttributeTypeOptions[] {
			STRING,
			INTEGER,
			ID,
			PUNCTUATION,
			TUPLE,
			MULTI,
			EXPRESSION,
		};

	/**
	 * A public read-only list of all the '<em><b>Attribute Type Options</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<AttributeTypeOptions> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Attribute Type Options</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AttributeTypeOptions get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AttributeTypeOptions result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Attribute Type Options</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AttributeTypeOptions getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AttributeTypeOptions result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Attribute Type Options</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AttributeTypeOptions get(int value) {
		switch (value) {
			case STRING_VALUE: return STRING;
			case INTEGER_VALUE: return INTEGER;
			case ID_VALUE: return ID;
			case PUNCTUATION_VALUE: return PUNCTUATION;
			case TUPLE_VALUE: return TUPLE;
			case MULTI_VALUE: return MULTI;
			case EXPRESSION_VALUE: return EXPRESSION;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private AttributeTypeOptions(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //AttributeTypeOptions
