package org.abreslav.grammatic.astrans.semantics;

import static org.abreslav.grammatic.metadata.util.MetadataUtils.createAttributeValue;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class ExpressionSemanticalDescriptor {

	private static final String ASSIGNMENTS = "assignments";
	private static final String DECLARED_ATTRIBUTES = "declaredAttributes";

	public static ExpressionSemanticalDescriptor create() {
		return new ExpressionSemanticalDescriptor();
	}
	
	public static void write(ExpressionSemanticalDescriptor descriptor, Expression expression, IWritableAspect writableAspect) {
		writableAspect.setAttribute(expression, SemanticalMetadata.SEMANTICAL_NAMESPACE, 
				DECLARED_ATTRIBUTES, createAttributeValue(descriptor.getDeclaredAttributes()));
		writableAspect.setAttribute(expression, SemanticalMetadata.SEMANTICAL_NAMESPACE, 
				ASSIGNMENTS, createAttributeValue(descriptor.getAssignments()));
	}	
	
	public static ExpressionSemanticalDescriptor read(IMetadataStorage symbolMetadata) {
		ExpressionSemanticalDescriptor descriptor = new ExpressionSemanticalDescriptor();
		List<SemanticalAttribute> attributes = symbolMetadata.readObjects(DECLARED_ATTRIBUTES);
		if (attributes != null) {
			descriptor.getDeclaredAttributes().addAll(attributes);
		}
		List<SemanticalAssignment> assignments = symbolMetadata.readObjects(ASSIGNMENTS);
		if (assignments != null) {
			descriptor.getAssignments().addAll(assignments);
		}
		return descriptor;
	}
	
	private final List<SemanticalAttribute> myDeclaredAttributes = new ArrayList<SemanticalAttribute>(0);
	private final List<SemanticalAssignment> myAssignments = new ArrayList<SemanticalAssignment>();
	
	private ExpressionSemanticalDescriptor() {
	}
	
	public List<SemanticalAttribute> getDeclaredAttributes() {
		return myDeclaredAttributes;
	}
	
	public List<SemanticalAssignment> getAssignments() {
		return myAssignments;
	}
	
}
