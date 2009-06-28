/**
 * 
 */
package org.abreslav.grammatic.query.interpreter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.grammar.ExpressionValue;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.IntegerValue;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.query.AttributePresence;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.AttributeType;
import org.abreslav.grammatic.query.AttributeValueQuery;
import org.abreslav.grammatic.query.ExactValue;
import org.abreslav.grammatic.query.TypedRegExpValue;
import org.abreslav.grammatic.query.util.QuerySwitch;
import org.eclipse.emf.ecore.EObject;


public class AttributeMatcher {
	private static final String NULL_URI = new String(); 
	
	private static final AttributeQuerySwitch ourInstance = new AttributeQuerySwitch();

	private final IMetadataProvider myMetadataProvider;
	
	/*package*/ AttributeMatcher(IMetadataProvider metadataProvider) {
		myMetadataProvider = metadataProvider;
	}

	public boolean matchAttributes(List<AttributeQuery> attributeQueries,
			EObject object) {
		Set<Attribute> attributes = myMetadataProvider.getAttributes(object);
		return matchAttributes(attributeQueries, attributes);
	}
	
	private static boolean matchAttributeValue(AttributeValueQuery attributeValue,
			Attribute attribute) {
		return ourInstance.doSwitch(attributeValue, attribute, null);
	}

	private static boolean matchAttributes(List<AttributeQuery> attributeQueries,
			Collection<Attribute> attributes) {
		Map<String, Map<String, Attribute>> namespaceMap = buildNamespaceMap(attributes);
		
		for (AttributeQuery attributeQuery : attributeQueries) {
			String namespaceUri = attributeQuery.getNamespaceUri();
			if (namespaceUri == null) {
				namespaceUri = NULL_URI;
			}
			
			Map<String, Attribute> attributeMap = namespaceMap.get(namespaceUri);
			if (attributeMap == null) {
				return false;
			}
			
			Attribute attribute = attributeMap.get(attributeQuery.getAttributeName());
			
			if (!AttributeMatcher.matchAttributeValue(attributeQuery.getAttributeValue(), attribute)) {
				return false;
			}
		}
		return true;
	}

	private static Map<String, Map<String, Attribute>> buildNamespaceMap(
			Collection<Attribute> attributes) {
		Map<String, Map<String, Attribute>> namespaceMap = new HashMap<String, Map<String,Attribute>>();
		namespaceMap.put(NULL_URI, new HashMap<String, Attribute>());
		for (Attribute attribute : attributes) {
			Namespace namespace = attribute.getNamespace();
			String nsUri = namespace != null ? namespace.getUri() : NULL_URI;
			Map<String, Attribute> attributeMap = namespaceMap.get(nsUri);
			if (attributeMap == null) {
				attributeMap = new HashMap<String, Attribute>();
				namespaceMap.put(nsUri, attributeMap);
			}
			attributeMap.put(attribute.getName(), attribute);
			
		}
		return namespaceMap;
	}
	
	private static class AttributeQuerySwitch extends QuerySwitch<Boolean, Attribute, Void> {
		
		@Override
		public Boolean caseAttributePresence(AttributePresence query, Attribute attribute, Void _null) {
			return (attribute != null) == query.isPresent();
		}
		
		@Override
		public Boolean caseAttributeType(AttributeType query, Attribute attribute, Void _null) {
			switch (query.getType()) {
			case EXPRESSION:
				return attribute.getValue() instanceof ExpressionValue;
			case ID:
				return attribute.getValue() instanceof IdValue;
			case INTEGER:
				return attribute.getValue() instanceof IntegerValue;
			case MULTI:
				return attribute.getValue() instanceof MultiValue;
			case PUNCTUATION:
				return attribute.getValue() instanceof PunctuationValue;
			case STRING:
				return attribute.getValue() instanceof StringValue;
			case TUPLE:
				return attribute.getValue() instanceof TupleValue;
			}
			throw new IllegalArgumentException("Unknown attribute type");
		}

		@Override
		public Boolean caseExactValue(ExactValue query, Attribute attribute, Void _null) {
			if (attribute == null) {
				return false;
			}
			return ValueMatcher.equals(query.getValue(), attribute.getValue());
		}
		
		@Override
		public Boolean caseTypedRegExpValue(TypedRegExpValue object, Attribute attribute, Void _null) {
			throw new UnsupportedOperationException();
		}
	}	
}
