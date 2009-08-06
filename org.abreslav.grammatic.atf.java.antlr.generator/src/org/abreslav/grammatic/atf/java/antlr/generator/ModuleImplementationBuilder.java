package org.abreslav.grammatic.atf.java.antlr.generator;

import java.util.List;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.parser.JavaTypeStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.eclipse.emf.ecore.EGenericType;

public class ModuleImplementationBuilder {
	
	public static final ModuleImplementationBuilder INSTANCE = new ModuleImplementationBuilder();
	
	private final IStringRepresentationProvider<EGenericType> myStringRepresentationProvider = new JavaTypeStringRepresentationProvider();
	
	private ModuleImplementationBuilder() {}
	
	public ModuleImplementation buildModuleImplementation(SemanticModule semanticModule, IModuleImplementationBuilderTrace trace) {
		ModuleImplementation result = SemanticsFactory.eINSTANCE.createModuleImplementation();
		result.setName(semanticModule.getName());
		buildImplMethods(semanticModule.getFunctions(), result.getMethods(), trace);
		return result;
	}

	private void buildImplMethods(List<FunctionSignature> functions,
			List<Method> methods, IModuleImplementationBuilderTrace trace) {
		for (FunctionSignature function : functions) {
			Method method = SemanticsFactory.eINSTANCE.createMethod();
			method.setName(function.getName());
			method.setType(getMethodType(function.getOutputAttributes()));
			
			buildParameters(function.getInputAttributes(), method.getParameters());
			
			methods.add(method);
			trace.putFunctionToMethod(function, method);
		}
	}

	private Type getMethodType(List<ATFAttribute> outputAttributes) {
		Type type;
		if (outputAttributes.isEmpty()) {
			type = TypeUtils.getVoidType();
		} else {
			if (outputAttributes.size() > 1) {
				throw new IllegalArgumentException("Multiple return values are not supported");
			}
			type = TypeUtils.getType((EGenericType) outputAttributes.get(0).getType(), myStringRepresentationProvider);
		}
		return type;
	}

	private void buildParameters(List<ATFAttribute> inputAttributes,
			List<Variable> parameters) {
		for (ATFAttribute atfAttribute : inputAttributes) {
			Variable parameter = SemanticsFactory.eINSTANCE.createVariable();
			parameter.setName(atfAttribute.getName());
			parameter.setType(TypeUtils.getType((EGenericType) atfAttribute.getType(), myStringRepresentationProvider));
			parameters.add(parameter);
		}
	}
}
