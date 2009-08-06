package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaAssignment;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.ParserField;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
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
	
	private interface ISemanticHandler {
		void onAssignment(JavaAssignment assignment);
		void onDefinition(VariableDefinition definition);
		void onExpression(ANTLRExpression expression);
	}

	private static class VariableNameFixer extends SemanticsSwitch<INull> {
		private final ISemanticHandler mySemanticHandler;
		
		public VariableNameFixer(ISemanticHandler semanticHandler) {
			mySemanticHandler = semanticHandler;
		}

		@Override
		public INull doSwitch(EObject theEObject) {
			if (theEObject == null) {
				return INull.NULL;
			}
			return super.doSwitch(theEObject);
		}

		@Override
		public INull caseVariableDefinition(VariableDefinition object) {
			mySemanticHandler.onDefinition(object);
			return INull.NULL;
		}
		
		@Override
		public INull caseJavaAssignment(JavaAssignment object) {
			mySemanticHandler.onAssignment(object);
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
		private final ISemanticHandler mySemanticHandler;
		private final VariableNameFixer myVariableNameFixer;
		
		public StatementTraverser(ISemanticHandler semanticHandler) {
			myVariableNameFixer = new VariableNameFixer(semanticHandler);
			mySemanticHandler = semanticHandler;
		}

		@Override
		public INull caseBeforeAfter(BeforeAfter object) {
			myVariableNameFixer.doSwitch(object.getBefore());
			myVariableNameFixer.doSwitch(object.getAfter());
			return INull.NULL;
		}
		
		@Override
		public INull caseANTLRExpression(ANTLRExpression object) {
			mySemanticHandler.onExpression(object);
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
			final NameScope ruleLevelScope = fieldScope.createChildScope();
			final Set<Variable> definedVars = new HashSet<Variable>();
			if (rule instanceof SyntacticalRule) {
				rule.setName(uniqueVariableName(rule.getName(), ruleNameScope));
				
				SyntacticalRule synRule = (SyntacticalRule) rule;

				definedVars.add(synRule.getResultVariable());
				definedVars.addAll(synRule.getParameters());
				for (Variable variable : definedVars) {
					fixVariableName(variable, ruleLevelScope);
				}
			} else {
				rule.setName(uniqueConstName(rule.getName(), ruleNameScope));
			}

			// -> local variables (including those defined by expressions)
			addMissingDefinitions(rule,	definedVars);
			
			new StatementTraverser(new ISemanticHandler() {
				
				@Override
				public void onExpression(ANTLRExpression expression) {
					fixVariableName(expression.getAssignToVariable(), ruleLevelScope);
				}
				
				@Override
				public void onDefinition(VariableDefinition definition) {
					fixVariableName(definition.getVariable(), ruleLevelScope);
				}
				
				@Override
				public void onAssignment(JavaAssignment assignment) {
				}
			}).doSwitch(rule);
		}
	}

	private StatementTraverser addMissingDefinitions(Rule rule,
			final Set<Variable> definedVars) {
		final Set<Variable> usedVars = new HashSet<Variable>();
		StatementTraverser statementTraverser = new StatementTraverser(new ISemanticHandler() {
			
			@Override
			public void onExpression(ANTLRExpression expression) {
				definedVars.add(expression.getAssignToVariable());
			}
			
			@Override
			public void onAssignment(JavaAssignment assignment) {
				usedVars.add(assignment.getVariable());
			}

			@Override
			public void onDefinition(VariableDefinition definition) {
				definedVars.remove(definition.getVariable());
			}
		});
		statementTraverser.doSwitch(rule);
		usedVars.removeAll(definedVars);
		
		if (!usedVars.isEmpty()) {
			CodeBlock definitionBlock = SemanticsFactory.eINSTANCE.createCodeBlock();
			for (Variable variable : usedVars) {
				VariableDefinition definition = SemanticsFactory.eINSTANCE.createVariableDefinition();
				definition.setVariable(variable);
				definitionBlock.getStatements().add(definition);
			}
			JavaStatement before = rule.getBefore();
			if (before != null) {
				definitionBlock.getStatements().add(before);
			}
			rule.setBefore(definitionBlock);
		}
		return statementTraverser;
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
