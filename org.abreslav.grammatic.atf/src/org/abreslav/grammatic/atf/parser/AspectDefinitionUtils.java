package org.abreslav.grammatic.atf.parser;

import static org.abreslav.grammatic.atf.ATFMetadata.ATF_NAMESPACE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.metadata.Annotated;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.AttributeValue;
import org.abreslav.grammatic.metadata.CrossReferenceValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.AspectdefFactory;
import org.abreslav.grammatic.metadata.aspectdef.Assignment;
import org.abreslav.grammatic.metadata.aspectdef.AssignmentRule;
import org.abreslav.grammatic.metadata.aspectdef.GrammarAssignment;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class AspectDefinitionUtils {
	/*package*/ static GrammarAssignment getGrammarAssignment(AspectDefinition aspectDef) {
		GrammarAssignment grammarAssignment = aspectDef.getGrammarAssignment();
		if (grammarAssignment == null) {
			grammarAssignment = AspectdefFactory.eINSTANCE.createGrammarAssignment();
			aspectDef.setGrammarAssignment(grammarAssignment);
		}
		return grammarAssignment;
	}

	/*package*/ static Attribute getAttribute(Annotated annotated, Namespace namespace, String name) {
		Attribute targetAttribute = getAttributeOrNull(annotated, namespace,
				name);
		if (targetAttribute == null) {
			targetAttribute = MetadataFactory.eINSTANCE.createAttribute();
			targetAttribute.setName(name);
			targetAttribute.setNamespace(namespace);
			annotated.getAttributes().add(targetAttribute);
		}
		return targetAttribute;
	}

	/*package*/ static Attribute getAttributeOrNull(Annotated annotated,
			Namespace namespace, String name) {
		Attribute targetAttribute = null;
		for (Attribute attribute : annotated.getAttributes()) {
			if (attribute.getNamespace() == namespace 
					&& name.equals(attribute.getName())) {
				targetAttribute = attribute;
				break;
			}
		}
		return targetAttribute;
	}
	
	/*package*/ static boolean hasAttribute(AssignmentRule assignmentRule, VariableDefinition variable,
			Namespace namespace, String name) {
		Assignment assignment = getAssignment(assignmentRule, variable);
		return getAttributeOrNull(assignment, namespace, name) != null;
	}
	
	/*package*/ static Assignment getAssignment(AssignmentRule assignmentRule,
			VariableDefinition variable) {
		for (Assignment assignment : assignmentRule.getAssignments()) {
			if (assignment.getVariable() == variable) {
				return assignment;
			}
		}
		Assignment assignment = AspectdefFactory.eINSTANCE.createAssignment();
		assignmentRule.getAssignments().add(assignment);
		assignment.setVariable(variable);
		return assignment;
	}

	/*package*/ static void addCrossReferencedValue(Attribute targetAttribute,
			EObject object) {
		CrossReferenceValue value = getCrossReferenceValue(targetAttribute);
		value.getValues().add(object);
	}

	private static CrossReferenceValue getCrossReferenceValue(
			Attribute targetAttribute) {
		CrossReferenceValue value = (CrossReferenceValue) targetAttribute.getValue();
		if (value == null) {
			value = MetadataFactory.eINSTANCE.createCrossReferenceValue();
			targetAttribute.setValue(value);
		}
		return value;
	}

	/*package*/ static void addAttributeValue(Attribute targetAttribute,
			Object object) {
		AttributeValue value = (AttributeValue) targetAttribute.getValue();
		if (value == null) {
			value = MetadataFactory.eINSTANCE.createAttributeValue();
			targetAttribute.setValue(value);
		}
		value.getValues().add(object);
	}
	
	/*package*/ static VariableDefinition getSymbolVariable(
			AssignmentRule assignmentRule) {
		RuleQuery query = (RuleQuery) assignmentRule.getQueryContainer().getQuery();
		VariableDefinition symbolVariable = query.getSymbolVariable();
		if (symbolVariable == null) {
			symbolVariable = QueryFactory.eINSTANCE.createVariableDefinition();
			SymbolQuery symbol = query.getSymbol();
			String name = symbol.getName();
			symbolVariable.setName(name == null ? "<symbol>" : name);
			symbolVariable.setValue(EMFProxyUtil.copy(symbol));
			query.setSymbolVariable(symbolVariable);
		}
		return symbolVariable;
	}

	/*package*/ static void addContainedValueToSymbolAttribute(
			AssignmentRule assignmentRule, Namespace namespace, String attributeName,
			EObject object) {
		Attribute attribute = getSymbolAttribute(assignmentRule, namespace,
				attributeName);
		addCrossReferencedValue(attribute, object);
	}
	
	/*package*/ static void addContainedValuesToSymbolAttribute(
			AssignmentRule assignmentRule, Namespace namespace, String attributeName,
			Collection<? extends EObject> objects) {
		Attribute attribute = getSymbolAttribute(assignmentRule, namespace,
				attributeName);
		CrossReferenceValue crossReferenceValue = getCrossReferenceValue(attribute);
		crossReferenceValue.getValues().addAll(objects);
	}

	/*package*/ static void addAttributeValueToSymbolAttribute(
			AssignmentRule assignmentRule, Namespace namespace, String attributeName,
			Object object) {
		Attribute attribute = getSymbolAttribute(assignmentRule, namespace,
				attributeName);
		addAttributeValue(attribute, object);
	}
	
	/*package*/ static Attribute getSymbolAttribute(AssignmentRule assignmentRule,
			Namespace namespace, String attributeName) {
		VariableDefinition symbolVariable = getSymbolVariable(assignmentRule);
		return getAttribute(assignmentRule, symbolVariable, namespace,
				attributeName);
	}

	/*package*/ static Attribute getAttribute(AssignmentRule assignmentRule,
			VariableDefinition symbolVariable, Namespace namespace,
			String attributeName) {
		Annotated assignment = getAssignment(assignmentRule, symbolVariable);
		Attribute attribute = getAttribute(assignment, namespace, attributeName);
		return attribute;
	}
	
	/*package*/ static VariableDefinition getVariable(AssignmentRule assignmentRule, String name) {
		QueryContainer<? extends Query> queryContainer = assignmentRule.getQueryContainer();
		EList<VariableDefinition> variableDefinitions = queryContainer.getVariableDefinitions();
		for (VariableDefinition variableDefinition : variableDefinitions) {
			if (name.equals(variableDefinition.getName())) {
				return variableDefinition;
			}
		}
		return null;
	}
	
	/*package*/ static <K, V> void addMapEnty(AssignmentRule currentAssignment,
			String attributeName, K key, V value) {
		Attribute attribute = getSymbolAttribute(currentAssignment, 
				ATF_NAMESPACE, attributeName);
		AttributeValue attributeValue = (AttributeValue) attribute.getValue();
		Map<K, V> map;
		if (attributeValue == null) {
			attributeValue = MetadataFactory.eINSTANCE.createAttributeValue();
			map = new HashMap<K, V>();
			attributeValue.getValues().add(map);
			attribute.setValue(attributeValue);
		} else {
			@SuppressWarnings("unchecked")
			Map<K, V> mapValue = (Map<K, V>) attributeValue.getValues().get(0);
			map = mapValue;
		}
		map.put(key, value);
	}

}
