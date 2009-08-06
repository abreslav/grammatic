package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.semantics.ImplementationPoolField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.ParserField;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.abreslav.grammatic.parsingutils.NameScope;
import org.abreslav.grammatic.parsingutils.ScopeUtils;

public class NameBeautifier {

	public void beautifyNames(ANTLRGrammar grammar) {
		NameScope rootScope = createKeywordSafeScope();

		NameScope moduleNameScope = createKeywordSafeScope();
		
		grammar.setName(uniqueTypeName(grammar.getName(), rootScope));
		
		NameScope fieldScope = rootScope.createChildScope();
		fieldScope.registerName("input");
		fieldScope.registerName("state");
		
		NameScope constructorParametersScope = rootScope.createChildScope();

		for (ModuleImplementationField moduleField : grammar.getModuleFields()) {
			ModuleImplementation module = moduleField.getModule();
			beautifyModuleName(module, moduleNameScope);
			moduleField.getField().setType(module.getName());
			moduleField.getConstructorParameter().setType(module.getName());
			
			processParserField(fieldScope, constructorParametersScope, moduleField);
		}
		
		for (ImplementationPoolField poolField : grammar.getPoolFields()) {
			processParserField(fieldScope, constructorParametersScope, poolField);
			// -> individual implementations
			ModuleImplementationProvider provider = poolField.getProvider();
			NameScope providerScope = createKeywordSafeScope();
			for (ModuleImplementation moduleImplementation : provider.getModuleImplementations()) {
				beautifyModuleName(moduleImplementation, providerScope);
			}
		}
		// rules
		// -> parameters and returns
		// -> local variables (including those defined by expressions)
		
		
		
	}

	private void beautifyModuleName(ModuleImplementation module,
			NameScope moduleNameScope) {
		module.setName(moduleNameScope.getUniqueName("I" + JavaUtils.applyTypeNameConventions(module.getName())));
	}

	private NameScope createKeywordSafeScope() {
		NameScope result = NameScope.createTopLevelScope();
		ScopeUtils.registerJavaKeywords(result);
		ScopeUtils.registerNames(result, ANTLRMetadata.getAntlrKeywords());
		return result;
	}

	private void processParserField(NameScope fieldScope,
			NameScope constructorParametersScope, ParserField parserField) {
		// field name
		Variable field = parserField.getField();
		String fieldName = "my" + JavaUtils.applyTypeNameConventions(field.getName());
		field.setName(uniqueVariableName(fieldName, fieldScope));
		// constructor parameter name
		Variable constructorParameter = parserField.getConstructorParameter();
		constructorParameter.setName(uniqueVariableName(constructorParameter.getName(), constructorParametersScope));
	}
	
	private static String uniqueVariableName(String name, NameScope scope) {
		return scope.getUniqueName(JavaUtils.applyVarNameConventions(name));
	}
	
	private static String uniqueTypeName(String name, NameScope scope) {
		return scope.getUniqueName(JavaUtils.applyTypeNameConventions(name));
	}
	
	private static String uniqueConstName(String name, NameScope scope) {
		return scope.getUniqueName(JavaUtils.applyConstNameConventions(name));
	}
}
