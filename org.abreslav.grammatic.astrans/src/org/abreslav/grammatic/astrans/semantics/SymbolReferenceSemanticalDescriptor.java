package org.abreslav.grammatic.astrans.semantics;

import static org.abreslav.grammatic.metadata.util.MetadataUtils.createAttributeValue;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;

public class SymbolReferenceSemanticalDescriptor {

	private static final String ASSIGNED_TO = "assignedTo";
	private static final String ARGUMENTS = "arguments";

	public static SymbolReferenceSemanticalDescriptor create(ExpressionSemanticalDescriptor descriptor) {
		return new SymbolReferenceSemanticalDescriptor(descriptor);
	}
	
	public static SymbolReferenceSemanticalDescriptor create() {
		return new SymbolReferenceSemanticalDescriptor(ExpressionSemanticalDescriptor.create());
	}
	
	public static void write(SymbolReferenceSemanticalDescriptor descriptor, SymbolReference symbolReference, IWritableAspect writableAspect) {
		writableAspect.setAttribute(symbolReference, SemanticalMetadata.SEMANTICAL_NAMESPACE, 
				ASSIGNED_TO, createAttributeValue(descriptor.getAssignedTo()));
		writableAspect.setAttribute(symbolReference, SemanticalMetadata.SEMANTICAL_NAMESPACE, 
				ARGUMENTS, createAttributeValue(descriptor.getArguments()));
	}	
	
	private final List<SemanticalReference> myAssignedTo = new ArrayList<SemanticalReference>(0);
	private final List<SemanticalAttribute> myArguments = new ArrayList<SemanticalAttribute>(0);
	private final ExpressionSemanticalDescriptor myExpressionSemanticalDescriptor;
	
	private SymbolReferenceSemanticalDescriptor(
			ExpressionSemanticalDescriptor expressionSemanticalDescriptor) {
		myExpressionSemanticalDescriptor = expressionSemanticalDescriptor;
	}

	public List<SemanticalReference> getAssignedTo() {
		return myAssignedTo;
	}
	
	public List<SemanticalAttribute> getArguments() {
		return myArguments;
	}
	
	public ExpressionSemanticalDescriptor getExpressionSemanticalDescriptor() {
		return myExpressionSemanticalDescriptor;
	}
}
