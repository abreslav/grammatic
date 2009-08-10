package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.atf.java.antlr.ANTLRCombination;
import org.abreslav.grammatic.atf.java.antlr.ANTLRExpression;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.ANTLRIteration;
import org.abreslav.grammatic.atf.java.antlr.ANTLRProduction;
import org.abreslav.grammatic.atf.java.antlr.BeforeAfter;
import org.abreslav.grammatic.atf.java.antlr.Rule;
import org.abreslav.grammatic.atf.java.antlr.RuleCall;
import org.abreslav.grammatic.atf.java.antlr.SyntacticalRule;
import org.abreslav.grammatic.atf.java.antlr.semantics.CodeBlock;
import org.abreslav.grammatic.atf.java.antlr.semantics.GrammarExpressionReference;
import org.abreslav.grammatic.atf.java.antlr.semantics.ImplementationPoolField;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaAssignment;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.ParserField;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableDefinition;
import org.abreslav.grammatic.atf.java.antlr.semantics.util.SemanticsSwitch;
import org.abreslav.grammatic.atf.java.antlr.util.AntlrSwitch;
import org.abreslav.grammatic.parsingutils.INameScope;
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

	private static class StatementProcessor extends SemanticsSwitch<INull> {
		private final ISemanticHandler mySemanticHandler;
		
		public StatementProcessor(ISemanticHandler semanticHandler) {
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
		private final StatementProcessor myStatementProcessor;
		
		public StatementTraverser(ISemanticHandler semanticHandler) {
			myStatementProcessor = new StatementProcessor(semanticHandler);
			mySemanticHandler = semanticHandler;
		}

		@Override
		public INull caseBeforeAfter(BeforeAfter object) {
			myStatementProcessor.doSwitch(object.getBefore());
			myStatementProcessor.doSwitch(object.getAfter());
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
	
	public final Set<ModuleImplementation> myBeautifiedModules = new HashSet<ModuleImplementation>();

	public void beautifyNames(ANTLRGrammar grammar) {
		INameScope rootScope = createKeywordSafeScope();
		grammar.setName(uniqueTypeName(grammar.getName(), rootScope));
		
		INameScope fieldScope = rootScope.createChildScope();
		fieldScope.registerName("input");
		fieldScope.registerName("state");
		fieldScope.registerName("tokenNames");
		
		INameScope moduleNameScope = createKeywordSafeScope();
		INameScope constructorParametersScope = rootScope.createChildScope();
		for (ModuleImplementationField moduleField : grammar.getModuleFields()) {
			ModuleImplementation module = moduleField.getModule();
			beautifyModuleName(module, moduleNameScope);
			processParserField(fieldScope, constructorParametersScope, moduleField);
			
			beautifyModule(module);
		}
		
		for (ImplementationPoolField poolField : grammar.getPoolFields()) {
			processParserField(fieldScope, constructorParametersScope, poolField);
			ModuleImplementationProvider provider = poolField.getProvider();
			INameScope providerScope = createKeywordSafeScope();
			for (ModuleImplementation moduleImplementation : provider.getModuleImplementations()) {
				beautifyModuleName(moduleImplementation, providerScope);
				Type providerInterface = provider.getProviderInterface();
				moduleImplementation.setPackage(providerInterface.getPackage() + "." + providerInterface.getName());
				beautifyModule(moduleImplementation);
			}
			
			for (Method method : provider.getGetImplementationMethods()) {
				// Strip initial "I"
				String baseName = method.getType().getName().substring(1);
				method.setName("get" + baseName);
			}
			for (Method method : provider.getReleaseImplementationMethods()) {
				// Strip initial "I"
				String baseName = method.getParameters().get(0).getType().getName().substring(1);
				method.setName("release" + baseName);
			}
		}
		
		INameScope ruleNameScope = createKeywordSafeScope();
		for (Rule rule : grammar.getRules()) {
			final INameScope ruleLevelScope = fieldScope.createChildScope();
			final INameScope expresionLabelsScope = NameScope.createMultiparentScope(ruleLevelScope, ruleNameScope);  
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
					Variable assignToVariable = expression.getAssignToVariable();
					if (assignToVariable != null) {
						fixVariableName(assignToVariable, expresionLabelsScope);
						ruleLevelScope.registerName(assignToVariable.getName());
					}
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

	private void beautifyModule(ModuleImplementation module) {
		for (Method method : module.getMethods()) {
			fixMethodParameters(method);
		}
	}

	private void fixMethodParameters(Method method) {
		INameScope parameterScope = createKeywordSafeScope();
		for (Variable variable : method.getParameters()) {
			fixVariableName(variable, parameterScope);
		}
	}

	private void addMissingDefinitions(Rule rule,
			final Set<Variable> initiallyDefinedVars) {
		final Set<Variable> definedVars = new HashSet<Variable>(initiallyDefinedVars);
		final Set<Variable> usedVars = new HashSet<Variable>();
		new StatementTraverser(new ISemanticHandler() {
			
			@Override
			public void onExpression(ANTLRExpression expression) {
				Variable assignToVariable = expression.getAssignToVariable();
				if (assignToVariable == null) {
					return;
				}
				 
				if (initiallyDefinedVars.contains(assignToVariable)) {
					introduceIntermediateVariable(expression);
					definedVars.add(assignToVariable);
				} else if (isNonSyntacticalReferenceWithVariable(expression)) {
					introduceIntermediateVariable(expression);
				} else {
					definedVars.add(assignToVariable);
				}
			}

			@Override
			public void onAssignment(JavaAssignment assignment) {
				usedVars.add(assignment.getVariable());
			}

			@Override
			public void onDefinition(VariableDefinition definition) {
				definedVars.add(definition.getVariable());
			}
		}).doSwitch(rule);
		usedVars.removeAll(definedVars);
		
		if (!usedVars.isEmpty()) {
			CodeBlock definitionBlock = SemanticsFactory.eINSTANCE.createCodeBlock();
			for (Variable variable : usedVars) {
				VariableDefinition definition = SemanticsFactory.eINSTANCE.createVariableDefinition();
				definition.setVariable(variable);
				definitionBlock.getStatements().add(definition);
			}
			rule.setBefore(StructureUtils.joinStatements(definitionBlock, rule.getBefore()));
		}
	}

	private void introduceIntermediateVariable(
			ANTLRExpression expression) {
		/*
		 * a=some {doIt();}
		 * is transformed into
		 * a1=some {a=a1; doIt();}
		 */
		Variable assignToVariable = expression.getAssignToVariable();
		Variable newVariable = SemanticsFactory.eINSTANCE.createVariable();
		newVariable.setName(assignToVariable.getName());
		newVariable.setType(assignToVariable.getType());
		
		GrammarExpressionReference ref = SemanticsFactory.eINSTANCE.createGrammarExpressionReference();
		ref.setExpression(expression);
		expression.setAssignToVariable(newVariable);

		JavaAssignment assignment = SemanticsFactory.eINSTANCE.createJavaAssignment();
		assignment.setVariable(assignToVariable);
		assignment.setValue(ref);
		expression.setAfter(StructureUtils.joinStatements(assignment, expression.getAfter()));
	}
	
	private boolean isNonSyntacticalReferenceWithVariable(ANTLRExpression expression) {
		if (expression.getAssignToVariable() == null) {
			return false;
		}
		if (false == expression instanceof RuleCall) {
			return true;
		}
		return false == ((RuleCall) expression).getRule() instanceof SyntacticalRule;
	}

	private static void fixVariableName(Variable variable, INameScope scope) {
		if (variable == null) {
			return;
		}
		variable.setName(uniqueVariableName(variable.getName(), scope));
	}

	private void beautifyModuleName(ModuleImplementation module,
			INameScope moduleNameScope) {
		if (myBeautifiedModules.contains(module)) {
			return;
		}
		myBeautifiedModules.add(module);
		module.setName(moduleNameScope.getUniqueName("I" + JavaUtils.applyTypeNameConventions(module.getName())));
	}

	private INameScope createKeywordSafeScope() {
		INameScope result = NameScope.createTopLevelScope();
		ScopeUtils.registerJavaKeywords(result);
		ScopeUtils.registerNames(result, ANTLRMetadata.getAntlrKeywords());
		return result;
	}

	private void processParserField(INameScope fieldScope,
			INameScope constructorParametersScope, ParserField parserField) {
		// field name
		Variable field = parserField.getField();
		String fieldName = "my" + JavaUtils.applyTypeNameConventions(field.getName());
		field.setName(uniqueVariableName(fieldName, fieldScope));
		// constructor parameter name
		Variable constructorParameter = parserField.getConstructorParameter();
		fixVariableName(constructorParameter, constructorParametersScope);
	}
	
	private static String uniqueVariableName(String name, INameScope scope) {
		return scope.getUniqueName(JavaUtils.applyVarNameConventions(name));
	}
	
	private static String uniqueTypeName(String name, INameScope scope) {
		return scope.getUniqueName(JavaUtils.applyTypeNameConventions(name));
	}
	
	private static String uniqueConstName(String name, INameScope scope) {
		return scope.getUniqueName(JavaUtils.applyConstNameConventions(name));
	}
}
