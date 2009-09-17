package org.abreslav.grammatic.astrans.semantics;

import static org.abreslav.grammatic.metadata.util.MetadataUtils.createAttributeValue;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class SymbolSemanticalDescriptor {

	private static final String OUTPUT_ATTRIBUTES = "outputAttributes";
	private static final String INPUT_ATTRIBUTES = "inputAttributes";

	public static SymbolSemanticalDescriptor create() {
		return new SymbolSemanticalDescriptor();
	}
	
	public static void write(SymbolSemanticalDescriptor descriptor, Symbol symbol, IWritableAspect writableAspect) {
		writableAspect.setAttribute(symbol, SemanticalMetadata.SEMANTICAL_NAMESPACE, 
				INPUT_ATTRIBUTES, createAttributeValue(descriptor.getInputAttributes()));
		writableAspect.setAttribute(symbol, SemanticalMetadata.SEMANTICAL_NAMESPACE, 
				OUTPUT_ATTRIBUTES, createAttributeValue(descriptor.getOutputAttributes()));
	}	
	
	public static SymbolSemanticalDescriptor read(IMetadataStorage symbolMetadata) {
		SymbolSemanticalDescriptor descriptor = new SymbolSemanticalDescriptor();
		List<SemanticalAttribute> inputAttributes = symbolMetadata.readObjects(INPUT_ATTRIBUTES);
		if (inputAttributes != null) {
			descriptor.getInputAttributes().addAll(inputAttributes);
		}
		List<SemanticalAttribute> outputAttributes = symbolMetadata.readObjects(OUTPUT_ATTRIBUTES);
		if (outputAttributes != null) {
			descriptor.getOutputAttributes().addAll(outputAttributes);
		}
		return descriptor;
	}
	
	private final List<SemanticalAttribute> myInputAttributes = new ArrayList<SemanticalAttribute>(0);
	private final List<SemanticalAttribute> myOutputAttributes = new ArrayList<SemanticalAttribute>(0);
	
	private SymbolSemanticalDescriptor() {
	}
	
	public List<SemanticalAttribute> getInputAttributes() {
		return myInputAttributes;
	}
	
	public List<SemanticalAttribute> getOutputAttributes() {
		return myOutputAttributes;
	}
}
