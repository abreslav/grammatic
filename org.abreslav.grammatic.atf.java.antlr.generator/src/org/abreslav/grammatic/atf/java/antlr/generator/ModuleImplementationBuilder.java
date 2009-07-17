package org.abreslav.grammatic.atf.java.antlr.generator;

import java.util.List;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.parser.JavaTypeStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.eclipse.emf.ecore.EGenericType;

public class ModuleImplementationBuilder {
	
	public static final ModuleImplementationBuilder INSTANCE = new ModuleImplementationBuilder();
	
	private final IStringRepresentationProvider<EGenericType> myStringRepresentationProvider = new JavaTypeStringRepresentationProvider();
	
	private ModuleImplementationBuilder() {}
	
	public ModuleImplementation buildModuleImplementation(SemanticModule semanticModule) {
		ModuleImplementation result = SemanticsFactory.eINSTANCE.createModuleImplementation();
		result.setName(semanticModule.getName());
		buildImplMethods(semanticModule.getFunctions(), result.getMethods());
		return result;
	}

	private void buildImplMethods(List<FunctionSignature> functions,
			List<Method> methods) {
		for (FunctionSignature function : functions) {
			Method method = SemanticsFactory.eINSTANCE.createMethod();
			method.setName(function.getName());
			method.setType(getMethodType(function.getOutputAttributes()));
			
			buildParameters(function.getInputAttributes(), method.getParameters());
			
			methods.add(method);
		}
	}

	private String getMethodType(List<ATFAttribute> outputAttributes) {
		String type;
		if (outputAttributes.isEmpty()) {
			type = "void";
		} else {
			if (outputAttributes.size() > 1) {
				throw new IllegalArgumentException("Multiple return values are not supported");
			}
			type = getTypeString(outputAttributes.get(0).getType());
		}
		return type;
	}

	private void buildParameters(List<ATFAttribute> inputAttributes,
			List<Variable> parameters) {
		for (ATFAttribute atfAttribute : inputAttributes) {
			Variable parameter = SemanticsFactory.eINSTANCE.createVariable();
			parameter.setName(atfAttribute.getName());
			parameter.setType(getTypeString(atfAttribute.getType()));
			parameters.add(parameter);
		}
	}

	private String getTypeString(Object typeObject) {
		return myStringRepresentationProvider.getStringRepresentation((EGenericType) typeObject);
	}
}
