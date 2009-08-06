package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.Iterator;

import org.abreslav.grammatic.atf.java.antlr.ANTLRCombination;
import org.abreslav.grammatic.atf.java.antlr.ANTLRExpression;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.ANTLRIteration;
import org.abreslav.grammatic.atf.java.antlr.ANTLRProduction;
import org.abreslav.grammatic.atf.java.antlr.BeforeAfter;
import org.abreslav.grammatic.atf.java.antlr.Rule;
import org.abreslav.grammatic.atf.java.antlr.SyntacticalRule;
import org.abreslav.grammatic.atf.java.antlr.semantics.CodeBlock;
import org.abreslav.grammatic.atf.java.antlr.semantics.ImplementationPoolField;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.ParserField;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableDefinition;
import org.abreslav.grammatic.atf.java.antlr.semantics.util.SemanticsSwitch;
import org.abreslav.grammatic.atf.java.antlr.util.AntlrSwitch;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.abreslav.grammatic.parsingutils.NameScope;
import org.abreslav.grammatic.parsingutils.ScopeUtils;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

public class NameBeautifier {
	
	private static class VariableNameFixer extends SemanticsSwitch<INull> {
		private final NameScope myScope;
		
		@Override
		public INull doSwitch(EObject theEObject) {
			if (theEObject == null) {
				return INull.NULL;
			}
			return super.doSwitch(theEObject);
		}

		public VariableNameFixer(NameScope scope) {
			myScope = scope;
		}

		@Override
		public INull caseVariableDefinition(VariableDefinition object) {
			fixVariableName(object.getVariable(), myScope);
			return INull.NULL;
		}
		
		@Override
		public INull caseCodeBlock(CodeBlock object) {
			for (JavaStatement javaStatement : object.getStatements()) {
				doSwitch(javaStatement);
			}
			return INull.NULL;
		}
		
	}
	
	private static class StatementTraverser extends AntlrSwitch<INull> {
		private final VariableNameFixer myVariableNameFixer;
		private final NameScope myScope;

		public StatementTraverser(NameScope scope) {
			myVariableNameFixer = new VariableNameFixer(scope);
			myScope = scope;
		}
		
		@Override
		public INull caseBeforeAfter(BeforeAfter object) {
			myVariableNameFixer.doSwitch(object.getBefore());
			myVariableNameFixer.doSwitch(object.getAfter());
			return INull.NULL;
		}
		
		@Override
		public INull caseANTLRExpression(ANTLRExpression object) {
			fixVariableName(object.getAssignToVariable(), myScope);
			return null; // fall through
		}
		
		@Override
		public INull caseANTLRCombination(ANTLRCombination object) {
			for (ANTLRExpression antlrExpression : object.getExpressions()) {
				doSwitch(antlrExpression);
			}
			return null; // fall through
		}
		
		@Override
		public INull caseRule(Rule object) {
			for (ANTLRProduction antlrProduction : object.getProductions()) {
				doSwitch(antlrProduction);
			}
			return null; // fall through
		}
		
		@Override
		public INull caseANTLRProduction(ANTLRProduction object) {
			doSwitch(object.getExpression());
			return null; // fall through
		}
		
		@Override
		public INull caseANTLRIteration(ANTLRIteration object) {
			doSwitch(object.getExpression());
			return null; // fall through
		}
	}

	public void beautifyNames(ANTLRGrammar grammar) {

		
		NameScope rootScope = createKeywordSafeScope();
		grammar.setName(uniqueTypeName(grammar.getName(), rootScope));
		
		NameScope fieldScope = rootScope.createChildScope();
		fieldScope.registerName("input");
		fieldScope.registerName("state");
		
		NameScope moduleNameScope = createKeywordSafeScope();
		NameScope constructorParametersScope = rootScope.createChildScope();
		for (ModuleImplementationField moduleField : grammar.getModuleFields()) {
			ModuleImplementation module = moduleField.getModule();
			beautifyModuleName(module, moduleNameScope);
			
			processParserField(fieldScope, constructorParametersScope, moduleField);
		}
		
		for (ImplementationPoolField poolField : grammar.getPoolFields()) {
			processParserField(fieldScope, constructorParametersScope, poolField);
			ModuleImplementationProvider provider = poolField.getProvider();
			NameScope providerScope = createKeywordSafeScope();
			for (ModuleImplementation moduleImplementation : provider.getModuleImplementations()) {
				beautifyModuleName(moduleImplementation, providerScope);
			}
			
			Iterator<Method> releaseIterator = provider.getReleaseImplementationMethods().iterator();
			for (Method getMethod : provider.getGetImplementationMethods()) {
				// Strip initial "I"
				String baseName = getMethod.getType().getName().substring(1);
				getMethod.setName("get" + baseName);
				releaseIterator.next().setName("release" + baseName);
			}
		}
		
		NameScope ruleNameScope = createKeywordSafeScope();
		for (Rule rule : grammar.getRules()) {
			NameScope ruleLevelScope = fieldScope.createChildScope();
			if (rule instanceof SyntacticalRule) {
				rule.setName(uniqueVariableName(rule.getName(), ruleNameScope));
				
				SyntacticalRule synRule = (SyntacticalRule) rule;
				
				fixVariableName(synRule.getResultVariable(), ruleLevelScope);
				for (Variable variable : synRule.getParameters()) {
					fixVariableName(variable, ruleLevelScope);
				}
			} else {
				rule.setName(uniqueConstName(rule.getName(), ruleNameScope));
			}

			// -> local variables (including those defined by expressions)
			StatementTraverser statementTraverser = new StatementTraverser(ruleLevelScope);
			statementTraverser.doSwitch(rule);
		}
	}

	private static void fixVariableName(Variable variable, NameScope scope) {
		if (variable == null) {
			return;
		}
		variable.setName(uniqueVariableName(variable.getName(), scope));
	}

	private void beautifyModuleName(ModuleImplementation module,
			NameScope moduleNameScope) {
		module.setName(moduleNameScope.getUniqueName("I" + JavaUtils.applyTypeNameConventions(module.getName())));
		System.out.println(module.getName());
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
		fixVariableName(constructorParameter, constructorParametersScope);
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
