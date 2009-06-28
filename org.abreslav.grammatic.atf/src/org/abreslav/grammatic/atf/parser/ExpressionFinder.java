package org.abreslav.grammatic.atf.parser;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.ExactExpressionQuery;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.IterationQuery;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SequenceQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;
import org.abreslav.grammatic.query.util.QuerySwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ExpressionFinder<D> extends
		QuerySwitch<VariableDefinition, D, IReplacementStrategy<D>> {
	
	private VariableDefinition myVariableDefinition;
	
	@Override
	public VariableDefinition caseRuleQuery(RuleQuery object,
			D data, IReplacementStrategy<D> additionalData) {
		ArrayList<ProductionQuery> copy = new ArrayList<ProductionQuery>(object.getDefinitions());
		for (ProductionQuery productionQuery : copy) {
			doSwitch(productionQuery, data, additionalData);
		}
		return myVariableDefinition;
	}

	@Override
	public VariableDefinition caseProductionQuery(
			ProductionQuery object, D data, IReplacementStrategy<D> additionalData) {
		doSwitch(object.getDefinition(), data, additionalData);
		return myVariableDefinition;
	}

	@Override
	public VariableDefinition caseAlternativeQuery(
			AlternativeQuery object, D data, IReplacementStrategy<D> additionalData) {
		ArrayList<ExpressionQuery> copy = new ArrayList<ExpressionQuery>(object.getDefinitions());
		for (ExpressionQuery expressionQuery : copy) {
			doSwitch(expressionQuery, data, additionalData);
		}
		return myVariableDefinition;
	}

	@Override
	public VariableDefinition caseIterationQuery(IterationQuery object,
			D data, IReplacementStrategy<D> additionalData) {
		doSwitch(object.getDefinition(), data, additionalData);
		return myVariableDefinition;
	}

	@Override
	public VariableDefinition caseSequenceQuery(SequenceQuery object,
			D data, IReplacementStrategy<D> additionalData) {
		ArrayList<ExpressionQuery> copy = new ArrayList<ExpressionQuery>(object.getDefinitions());
		for (ExpressionQuery expressionQuery : copy) {
			doSwitch(expressionQuery, data, additionalData);
		}
		return myVariableDefinition;
	}

	@Override
	public VariableDefinition caseExactExpressionQuery(
			ExactExpressionQuery object, D data,
			IReplacementStrategy<D> additionalData) {
		Expression expression = object.getExpression();
		if (!additionalData.needsToBeReplaced(expression, data)) {
			return null;
		}
		String name = additionalData.getName(data);
		
		EObject container = object.eContainer();
		EStructuralFeature containingFeature = object.eContainingFeature();
		VariableReference variableReference;
		if (containingFeature.isMany()) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) container.eGet(containingFeature);
			int indexOf = list.indexOf(object);
			variableReference = createVariableReference(name);
			list.set(indexOf, variableReference);
		} else {
			variableReference = createVariableReference(name);
			container.eSet(containingFeature, variableReference);
		}
		variableReference.getAttributes().addAll(object.getAttributes());
		variableReference.getVariable().setValue(object);
		return myVariableDefinition;
	}

	private VariableReference createVariableReference(String name) {
		if (myVariableDefinition == null) {
			myVariableDefinition = QueryFactory.eINSTANCE.createVariableDefinition();
			myVariableDefinition.setName(name);
		}
		VariableReference reference = QueryFactory.eINSTANCE.createVariableReference();
		reference.setVariable(myVariableDefinition);
		return reference;
	}

	@Override
	public VariableDefinition defaultCase(EObject object, D data,
			IReplacementStrategy<D> additionalData) {
		return null;
	}
}