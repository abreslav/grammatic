package org.abreslav.grammatic.query.util;


import java.util.List;

import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.AlternativeWildcard;
import org.abreslav.grammatic.query.AttributePresence;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.AttributeType;
import org.abreslav.grammatic.query.AttributeValueQuery;
import org.abreslav.grammatic.query.CommutativeOperationQuery;
import org.abreslav.grammatic.query.EmptyQuery;
import org.abreslav.grammatic.query.ExactExpressionQuery;
import org.abreslav.grammatic.query.ExactValue;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.IterationQuery;
import org.abreslav.grammatic.query.LexicalWildcardQuery;
import org.abreslav.grammatic.query.MetadataQuery;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryPackage;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SequenceQuery;
import org.abreslav.grammatic.query.SequenceWildcard;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.SymbolReferenceQuery;
import org.abreslav.grammatic.query.TypedRegExpValue;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.QueryPackage
 * @generated
 */
public class QuerySwitch<R, D1, A> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static QueryPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QuerySwitch() {
		if (modelPackage == null) {
			modelPackage = QueryPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public R doSwitch(EObject theEObject, D1 data, A additionalData) {
		return doSwitch(theEObject.eClass(), theEObject, data, additionalData);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected R doSwitch(EClass theEClass, EObject theEObject, D1 data, A additionalData) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject, data, additionalData);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject, data, additionalData) :
					doSwitch(eSuperTypes.get(0), theEObject, data, additionalData);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected R doSwitch(int classifierID, EObject theEObject, D1 data, A additionalData) {
		switch (classifierID) {
			case QueryPackage.QUERY_CONTAINER: {
				QueryContainer<?> queryContainer = (QueryContainer<?>)theEObject;
				R result = caseQueryContainer(queryContainer, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.QUERY: {
				Query query = (Query)theEObject;
				R result = caseQuery(query, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY: {
				CommutativeOperationQuery<?> commutativeOperationQuery = (CommutativeOperationQuery<?>)theEObject;
				R result = caseCommutativeOperationQuery(commutativeOperationQuery, data, additionalData);
				if (result == null) result = caseQuery(commutativeOperationQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.RULE_QUERY: {
				RuleQuery ruleQuery = (RuleQuery)theEObject;
				R result = caseRuleQuery(ruleQuery, data, additionalData);
				if (result == null) result = caseQuery(ruleQuery, data, additionalData);
				if (result == null) result = caseCommutativeOperationQuery(ruleQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.SYMBOL_QUERY: {
				SymbolQuery symbolQuery = (SymbolQuery)theEObject;
				R result = caseSymbolQuery(symbolQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(symbolQuery, data, additionalData);
				if (result == null) result = caseQuery(symbolQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.PRODUCTION_QUERY: {
				ProductionQuery productionQuery = (ProductionQuery)theEObject;
				R result = caseProductionQuery(productionQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(productionQuery, data, additionalData);
				if (result == null) result = caseQuery(productionQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.EXPRESSION_QUERY: {
				ExpressionQuery expressionQuery = (ExpressionQuery)theEObject;
				R result = caseExpressionQuery(expressionQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(expressionQuery, data, additionalData);
				if (result == null) result = caseQuery(expressionQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.EMPTY_QUERY: {
				EmptyQuery emptyQuery = (EmptyQuery)theEObject;
				R result = caseEmptyQuery(emptyQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(emptyQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(emptyQuery, data, additionalData);
				if (result == null) result = caseQuery(emptyQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.SYMBOL_REFERENCE_QUERY: {
				SymbolReferenceQuery symbolReferenceQuery = (SymbolReferenceQuery)theEObject;
				R result = caseSymbolReferenceQuery(symbolReferenceQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(symbolReferenceQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(symbolReferenceQuery, data, additionalData);
				if (result == null) result = caseQuery(symbolReferenceQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.EXACT_EXPRESSION_QUERY: {
				ExactExpressionQuery exactExpressionQuery = (ExactExpressionQuery)theEObject;
				R result = caseExactExpressionQuery(exactExpressionQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(exactExpressionQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(exactExpressionQuery, data, additionalData);
				if (result == null) result = caseQuery(exactExpressionQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.LEXICAL_WILDCARD_QUERY: {
				LexicalWildcardQuery lexicalWildcardQuery = (LexicalWildcardQuery)theEObject;
				R result = caseLexicalWildcardQuery(lexicalWildcardQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(lexicalWildcardQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(lexicalWildcardQuery, data, additionalData);
				if (result == null) result = caseQuery(lexicalWildcardQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.VARIABLE_DEFINITION: {
				VariableDefinition variableDefinition = (VariableDefinition)theEObject;
				R result = caseVariableDefinition(variableDefinition, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.VARIABLE_REFERENCE: {
				VariableReference variableReference = (VariableReference)theEObject;
				R result = caseVariableReference(variableReference, data, additionalData);
				if (result == null) result = caseExpressionQuery(variableReference, data, additionalData);
				if (result == null) result = caseMetadataQuery(variableReference, data, additionalData);
				if (result == null) result = caseQuery(variableReference, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ALTERNATIVE_QUERY: {
				AlternativeQuery alternativeQuery = (AlternativeQuery)theEObject;
				R result = caseAlternativeQuery(alternativeQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(alternativeQuery, data, additionalData);
				if (result == null) result = caseCommutativeOperationQuery(alternativeQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(alternativeQuery, data, additionalData);
				if (result == null) result = caseQuery(alternativeQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.SEQUENCE_QUERY: {
				SequenceQuery sequenceQuery = (SequenceQuery)theEObject;
				R result = caseSequenceQuery(sequenceQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(sequenceQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(sequenceQuery, data, additionalData);
				if (result == null) result = caseQuery(sequenceQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.SEQUENCE_WILDCARD: {
				SequenceWildcard sequenceWildcard = (SequenceWildcard)theEObject;
				R result = caseSequenceWildcard(sequenceWildcard, data, additionalData);
				if (result == null) result = caseExpressionQuery(sequenceWildcard, data, additionalData);
				if (result == null) result = caseMetadataQuery(sequenceWildcard, data, additionalData);
				if (result == null) result = caseQuery(sequenceWildcard, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ALTERNATIVE_WILDCARD: {
				AlternativeWildcard alternativeWildcard = (AlternativeWildcard)theEObject;
				R result = caseAlternativeWildcard(alternativeWildcard, data, additionalData);
				if (result == null) result = caseQuery(alternativeWildcard, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ITERATION_QUERY: {
				IterationQuery iterationQuery = (IterationQuery)theEObject;
				R result = caseIterationQuery(iterationQuery, data, additionalData);
				if (result == null) result = caseExpressionQuery(iterationQuery, data, additionalData);
				if (result == null) result = caseMetadataQuery(iterationQuery, data, additionalData);
				if (result == null) result = caseQuery(iterationQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.METADATA_QUERY: {
				MetadataQuery metadataQuery = (MetadataQuery)theEObject;
				R result = caseMetadataQuery(metadataQuery, data, additionalData);
				if (result == null) result = caseQuery(metadataQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ATTRIBUTE_QUERY: {
				AttributeQuery attributeQuery = (AttributeQuery)theEObject;
				R result = caseAttributeQuery(attributeQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ATTRIBUTE_VALUE_QUERY: {
				AttributeValueQuery attributeValueQuery = (AttributeValueQuery)theEObject;
				R result = caseAttributeValueQuery(attributeValueQuery, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ATTRIBUTE_TYPE: {
				AttributeType attributeType = (AttributeType)theEObject;
				R result = caseAttributeType(attributeType, data, additionalData);
				if (result == null) result = caseAttributeValueQuery(attributeType, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.ATTRIBUTE_PRESENCE: {
				AttributePresence attributePresence = (AttributePresence)theEObject;
				R result = caseAttributePresence(attributePresence, data, additionalData);
				if (result == null) result = caseAttributeValueQuery(attributePresence, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.EXACT_VALUE: {
				ExactValue exactValue = (ExactValue)theEObject;
				R result = caseExactValue(exactValue, data, additionalData);
				if (result == null) result = caseAttributeValueQuery(exactValue, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			case QueryPackage.TYPED_REG_EXP_VALUE: {
				TypedRegExpValue typedRegExpValue = (TypedRegExpValue)theEObject;
				R result = caseTypedRegExpValue(typedRegExpValue, data, additionalData);
				if (result == null) result = caseAttributeValueQuery(typedRegExpValue, data, additionalData);
				if (result == null) result = defaultCase(theEObject, data, additionalData);
				return result;
			}
			default: return defaultCase(theEObject, data, additionalData);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends Query> R caseQueryContainer(QueryContainer<T> object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseQuery(Query object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Commutative Operation Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Commutative Operation Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <D extends Query> R caseCommutativeOperationQuery(CommutativeOperationQuery<D> object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rule Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseRuleQuery(RuleQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Symbol Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Symbol Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseSymbolQuery(SymbolQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Production Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Production Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseProductionQuery(ProductionQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Expression Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Expression Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseExpressionQuery(ExpressionQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Empty Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Empty Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseEmptyQuery(EmptyQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Symbol Reference Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Symbol Reference Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseSymbolReferenceQuery(SymbolReferenceQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Exact Expression Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Exact Expression Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseExactExpressionQuery(ExactExpressionQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Lexical Wildcard Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Lexical Wildcard Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseLexicalWildcardQuery(LexicalWildcardQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseVariableDefinition(VariableDefinition object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseVariableReference(VariableReference object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Alternative Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alternative Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAlternativeQuery(AlternativeQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sequence Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sequence Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseSequenceQuery(SequenceQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sequence Wildcard</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sequence Wildcard</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseSequenceWildcard(SequenceWildcard object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Alternative Wildcard</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alternative Wildcard</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAlternativeWildcard(AlternativeWildcard object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Iteration Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Iteration Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseIterationQuery(IterationQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Metadata Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Metadata Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseMetadataQuery(MetadataQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAttributeQuery(AttributeQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Value Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Value Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAttributeValueQuery(AttributeValueQuery object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAttributeType(AttributeType object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Presence</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Presence</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAttributePresence(AttributePresence object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Exact Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Exact Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseExactValue(ExactValue object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Typed Reg Exp Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Typed Reg Exp Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseTypedRegExpValue(TypedRegExpValue object, D1 data, A additionalData) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public R defaultCase(EObject object, D1 data, A additionalData) {
		return null;
	}

} //QuerySwitch