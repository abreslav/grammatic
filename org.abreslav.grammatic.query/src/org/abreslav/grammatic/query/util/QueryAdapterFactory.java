package org.abreslav.grammatic.query.util;


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
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.QueryPackage
 * @generated
 */
public class QueryAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static QueryPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = QueryPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QuerySwitch<Adapter, Void, Void> modelSwitch =
		new QuerySwitch<Adapter, Void, Void>() {
			@Override
			public <T extends Query> Adapter caseQueryContainer(QueryContainer<T> object, Void data, Void additionalData) {
				return createQueryContainerAdapter();
			}
			@Override
			public Adapter caseQuery(Query object, Void data, Void additionalData) {
				return createQueryAdapter();
			}
			@Override
			public <D extends Query> Adapter caseCommutativeOperationQuery(CommutativeOperationQuery<D> object, Void data, Void additionalData) {
				return createCommutativeOperationQueryAdapter();
			}
			@Override
			public Adapter caseRuleQuery(RuleQuery object, Void data, Void additionalData) {
				return createRuleQueryAdapter();
			}
			@Override
			public Adapter caseSymbolQuery(SymbolQuery object, Void data, Void additionalData) {
				return createSymbolQueryAdapter();
			}
			@Override
			public Adapter caseProductionQuery(ProductionQuery object, Void data, Void additionalData) {
				return createProductionQueryAdapter();
			}
			@Override
			public Adapter caseExpressionQuery(ExpressionQuery object, Void data, Void additionalData) {
				return createExpressionQueryAdapter();
			}
			@Override
			public Adapter caseEmptyQuery(EmptyQuery object, Void data, Void additionalData) {
				return createEmptyQueryAdapter();
			}
			@Override
			public Adapter caseSymbolReferenceQuery(SymbolReferenceQuery object, Void data, Void additionalData) {
				return createSymbolReferenceQueryAdapter();
			}
			@Override
			public Adapter caseExactExpressionQuery(ExactExpressionQuery object, Void data, Void additionalData) {
				return createExactExpressionQueryAdapter();
			}
			@Override
			public Adapter caseLexicalWildcardQuery(LexicalWildcardQuery object, Void data, Void additionalData) {
				return createLexicalWildcardQueryAdapter();
			}
			@Override
			public Adapter caseVariableDefinition(VariableDefinition object, Void data, Void additionalData) {
				return createVariableDefinitionAdapter();
			}
			@Override
			public Adapter caseVariableReference(VariableReference object, Void data, Void additionalData) {
				return createVariableReferenceAdapter();
			}
			@Override
			public Adapter caseAlternativeQuery(AlternativeQuery object, Void data, Void additionalData) {
				return createAlternativeQueryAdapter();
			}
			@Override
			public Adapter caseSequenceQuery(SequenceQuery object, Void data, Void additionalData) {
				return createSequenceQueryAdapter();
			}
			@Override
			public Adapter caseSequenceWildcard(SequenceWildcard object, Void data, Void additionalData) {
				return createSequenceWildcardAdapter();
			}
			@Override
			public Adapter caseAlternativeWildcard(AlternativeWildcard object, Void data, Void additionalData) {
				return createAlternativeWildcardAdapter();
			}
			@Override
			public Adapter caseIterationQuery(IterationQuery object, Void data, Void additionalData) {
				return createIterationQueryAdapter();
			}
			@Override
			public Adapter caseMetadataQuery(MetadataQuery object, Void data, Void additionalData) {
				return createMetadataQueryAdapter();
			}
			@Override
			public Adapter caseAttributeQuery(AttributeQuery object, Void data, Void additionalData) {
				return createAttributeQueryAdapter();
			}
			@Override
			public Adapter caseAttributeValueQuery(AttributeValueQuery object, Void data, Void additionalData) {
				return createAttributeValueQueryAdapter();
			}
			@Override
			public Adapter caseAttributeType(AttributeType object, Void data, Void additionalData) {
				return createAttributeTypeAdapter();
			}
			@Override
			public Adapter caseAttributePresence(AttributePresence object, Void data, Void additionalData) {
				return createAttributePresenceAdapter();
			}
			@Override
			public Adapter caseExactValue(ExactValue object, Void data, Void additionalData) {
				return createExactValueAdapter();
			}
			@Override
			public Adapter caseTypedRegExpValue(TypedRegExpValue object, Void data, Void additionalData) {
				return createTypedRegExpValueAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object, Void data, Void additionalData) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target, null, null);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.QueryContainer <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.QueryContainer
	 * @generated
	 */
	public Adapter createQueryContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.Query <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.Query
	 * @generated
	 */
	public Adapter createQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.CommutativeOperationQuery <em>Commutative Operation Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.CommutativeOperationQuery
	 * @generated
	 */
	public Adapter createCommutativeOperationQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.RuleQuery <em>Rule Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.RuleQuery
	 * @generated
	 */
	public Adapter createRuleQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.SymbolQuery <em>Symbol Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.SymbolQuery
	 * @generated
	 */
	public Adapter createSymbolQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.ProductionQuery <em>Production Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.ProductionQuery
	 * @generated
	 */
	public Adapter createProductionQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.ExpressionQuery <em>Expression Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.ExpressionQuery
	 * @generated
	 */
	public Adapter createExpressionQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.EmptyQuery <em>Empty Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.EmptyQuery
	 * @generated
	 */
	public Adapter createEmptyQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.SymbolReferenceQuery <em>Symbol Reference Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.SymbolReferenceQuery
	 * @generated
	 */
	public Adapter createSymbolReferenceQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.ExactExpressionQuery <em>Exact Expression Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.ExactExpressionQuery
	 * @generated
	 */
	public Adapter createExactExpressionQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.LexicalWildcardQuery <em>Lexical Wildcard Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.LexicalWildcardQuery
	 * @generated
	 */
	public Adapter createLexicalWildcardQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.VariableDefinition <em>Variable Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.VariableDefinition
	 * @generated
	 */
	public Adapter createVariableDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.VariableReference <em>Variable Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.VariableReference
	 * @generated
	 */
	public Adapter createVariableReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.AlternativeQuery <em>Alternative Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.AlternativeQuery
	 * @generated
	 */
	public Adapter createAlternativeQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.SequenceQuery <em>Sequence Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.SequenceQuery
	 * @generated
	 */
	public Adapter createSequenceQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.SequenceWildcard <em>Sequence Wildcard</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.SequenceWildcard
	 * @generated
	 */
	public Adapter createSequenceWildcardAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.AlternativeWildcard <em>Alternative Wildcard</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.AlternativeWildcard
	 * @generated
	 */
	public Adapter createAlternativeWildcardAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.IterationQuery <em>Iteration Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.IterationQuery
	 * @generated
	 */
	public Adapter createIterationQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.MetadataQuery <em>Metadata Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.MetadataQuery
	 * @generated
	 */
	public Adapter createMetadataQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.AttributeQuery <em>Attribute Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.AttributeQuery
	 * @generated
	 */
	public Adapter createAttributeQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.AttributeValueQuery <em>Attribute Value Query</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.AttributeValueQuery
	 * @generated
	 */
	public Adapter createAttributeValueQueryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.AttributeType <em>Attribute Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.AttributeType
	 * @generated
	 */
	public Adapter createAttributeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.AttributePresence <em>Attribute Presence</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.AttributePresence
	 * @generated
	 */
	public Adapter createAttributePresenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.ExactValue <em>Exact Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.ExactValue
	 * @generated
	 */
	public Adapter createExactValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.TypedRegExpValue <em>Typed Reg Exp Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.TypedRegExpValue
	 * @generated
	 */
	public Adapter createTypedRegExpValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //QueryAdapterFactory