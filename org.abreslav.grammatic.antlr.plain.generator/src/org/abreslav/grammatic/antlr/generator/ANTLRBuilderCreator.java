package org.abreslav.grammatic.antlr.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.antlr.generator.antlr.ANTLRAlternative;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRCharacterRange;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLREmpty;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRExpression;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRGrammar;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRIteration;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRProduction;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRSequence;
import org.abreslav.grammatic.antlr.generator.antlr.AntlrFactory;
import org.abreslav.grammatic.antlr.generator.antlr.Argument;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValue;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValueReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.Combination;
import org.abreslav.grammatic.antlr.generator.antlr.Import;
import org.abreslav.grammatic.antlr.generator.antlr.LexicalLiteral;
import org.abreslav.grammatic.antlr.generator.antlr.LexicalRule;
import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.ParameterReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.ResultArgument;
import org.abreslav.grammatic.antlr.generator.antlr.Rule;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;
import org.abreslav.grammatic.antlr.generator.antlr.TokenSwitch;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Assignment;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Builder;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderFactory;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderMethod;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderPoolVariable;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuildersFactory;
import org.abreslav.grammatic.antlr.generator.antlr.builders.CodeBlock;
import org.abreslav.grammatic.antlr.generator.antlr.builders.MethodCall;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Statement;
import org.abreslav.grammatic.antlr.generator.antlr.builders.VariableDefinition;
import org.abreslav.grammatic.antlr.generator.antlr.util.AntlrSwitch;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.abreslav.grammatic.parsingutils.NameScope;
import org.abreslav.grammatic.parsingutils.ScopeUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class ANTLRBuilderCreator {

	private static final String NAME_RETRIEVAL_ERROR = new String("error");

	private static final ExpressionTraverser<Boolean, Void> ourResultArgumentFinder = new ExpressionTraverser<Boolean, Void>(false) {
		@Override
		public Boolean caseRuleCall(RuleCall object, Void data) {
			EList<Argument> arguments = object.getArguments();
			for (Argument argument : arguments) {
				if (argument instanceof ResultArgument) {
					return true;
				}
			}
			return false;
		}
	
		@Override
		public Boolean defaultCase(EObject object, Void data) {
			return false;
		}
		
		@Override
		protected boolean isReturnNeeded(Boolean result) {
			return result;
		}
	};

	private static final AntlrSwitch<Boolean, Void> ourSingleCallChecker = new AntlrSwitch<Boolean, Void>() {

		@Override
		public Boolean caseCombination(Combination object, Void data) {
			for (ANTLRExpression expression : object.getExpressions()) {
				if (!doSwitch(expression, data)) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public Boolean caseANTLRIteration(ANTLRIteration object, Void data) {
			switch (object.getType()) {
			case ZERO_OR_ONE:
				return doSwitch(object.getExpression(), data);
			default:
				ANTLRExpression expression = object.getExpression();
				if (expression instanceof RuleCall) {
					Rule rule = ((RuleCall) expression).getRule();
					if (rule instanceof SyntacticalRule) {
						SyntacticalRule syntacticalRule = (SyntacticalRule) rule;
						return JavaUtils.isVoid(syntacticalRule.getResultVariable().getType());
					}
				}
				return false;
			}
		}
		
		@Override
		public Boolean defaultCase(EObject object, Void data) {
			return true;
		}
	};

	 private static final AntlrSwitch<String, Void> ourNameRetriever = new AntlrSwitch<String, Void>() {
		@Override
		public String caseRuleCall(RuleCall object, Void data) {
			String variableName = object.getVariableName();
			return variableName == null ? object.getRule().getName() : variableName;
		}
		
		@Override
		public String caseLexicalLiteral(LexicalLiteral object, Void data) {
			return object.getValue();
		}
		
		@Override
		public String caseANTLRSequence(ANTLRSequence object, Void data) {
			return super.caseANTLRSequence(object, data);
		}
		
		@Override
		public String caseANTLRCharacterRange(ANTLRCharacterRange object, Void data) {
			char lo = (char) object.getLowerBound();
			if (!ANTLRUtils.isSingleCharacter(object)) {
				return String.format("from_%c_to_%c", lo, (char) object.getUpperBound());
			}
			return "" + lo;
		}
		
		@Override
		public String caseTokenSwitch(TokenSwitch object, Void data) {
			String name = object.getVariableName();
			return name == null ? "token" : name;
		}
		
		@Override
		public String caseANTLREmpty(ANTLREmpty object, Void data) {
			return "empty";
		}
		
		@Override
		public String defaultCase(EObject object, Void data) {
			return NAME_RETRIEVAL_ERROR;
		}
	};
	
	public Collection<BuilderFactory> createBuilders(ANTLRGrammar frontGrammar, BuilderFactory frontFactory, Map<Rule, BuilderFactory> ruleOrigins) {
		NameScope grammarScope = ScopeUtils.getSafeToplevelScope();
		grammarScope.registerName("options");
		grammarScope.registerName("grammar");
		grammarScope.registerName("tree");
		
		List<Rule> rules = frontGrammar.getRules();
		
		// To avoid duplication of variable names with rule names 
		for (Rule rule : rules) {
			// TODO: transform lexical and syntactical names according to case requirements 
			rule.setName(grammarScope.getUniqueName(rule.getName()));
		}
		Set<String> imports = new LinkedHashSet<String>();
		Map<BuilderFactory, BuilderPoolVariable> factoryToVar = new HashMap<BuilderFactory, BuilderPoolVariable>();		
		Collection<BuilderFactory> factories = new ArrayList<BuilderFactory>();
		NameScope fieldScope = ScopeUtils.getSafeToplevelScope();
//		BuilderFactory frontFactory = createBuilderFactory(frontGrammar.getPackage(), frontGrammar.getName(), frontGrammar.getImports());
		for (Rule someRule : rules) {
			if (false == someRule instanceof SyntacticalRule) {
				continue;
			}
			
			SyntacticalRule rule = (SyntacticalRule) someRule;
			
			BuilderFactory builderFactory = ruleOrigins.get(rule);
			if (builderFactory == null) {
				builderFactory = frontFactory;
			}
			BuilderPoolVariable poolVar = factoryToVar.get(builderFactory);
			if (poolVar == null) {
				factories.add(builderFactory);
				addFactoryImports(frontGrammar.getPackage(), imports, builderFactory);
				poolVar = BuildersFactory.eINSTANCE.createBuilderPoolVariable();
				String basicName = getBasicName(builderFactory);
				poolVar.setFieldName(fieldScope.getUniqueName("my" + basicName));
				poolVar.setConstructorParameterName(JavaUtils.applyVarNameConventions(basicName));
				poolVar.setBuilderFactory(builderFactory);
				frontGrammar.getPoolVariables().add(poolVar);
				factoryToVar.put(builderFactory, poolVar);
			}
			createBuilder(rule, grammarScope.createChildScope(), poolVar);
		}

		setImports(frontGrammar, imports);
		
		return factories;
		
	}

	private String getBasicName(BuilderFactory builderFactory) {
		return builderFactory.getFactoryInterfaceName().substring(1);
	}
	
//	private static BuilderFactory createBuilderFactory(
//			String pack, String grammarName, Collection<Import> imports) {
//		BuilderFactory factory = BuildersFactory.eINSTANCE.createBuilderFactory();
//		factory.setFactoryInterfaceName(getFactoryName(grammarName));
//		factory.setPoolsClassName(getPoolsClassName(grammarName));
//		factory.setPackage(pack);
//		factory.getImports().addAll(imports);
//		return factory;
//	}


	private void addFactoryImports(String frontPackage, Set<String> imports, BuilderFactory factory) {
		for (Import imp : factory.getImports()) {
			imports.add(imp.getImported());
		}
		String factoryPackage = factory.getPackage();
		String factoryName = factory.getFactoryInterfaceName();
		String factoryFullName = getFullName(factoryPackage, factoryName); 
		if (!factoryPackage.equals(frontPackage)) {
			imports.add(factoryFullName);
			imports.add(getFullName(factoryPackage, factory.getPoolsClassName()));
		}
		imports.add(factoryFullName + ".*");
	}

	private String getFullName(String factoryPackage, String factoryName) {
		return factoryPackage == null ? factoryName : factoryPackage + "." + factoryName;
	}

	private void setImports(ANTLRGrammar treeGrammar, Set<String> imports) {
		for (String importString : imports) {
			Import imp = AntlrFactory.eINSTANCE.createImport();
			imp.setImported(importString);
			treeGrammar.getImports().add(imp);
		}
	}

	private void createBuilder(final SyntacticalRule rule, NameScope ruleVariablesScope, BuilderPoolVariable poolVar) {
		registerRuleHeaderVariables(rule, ruleVariablesScope);
		rule.getResultVariable().setName(ruleVariablesScope.getUniqueName("result"));
		
		final Builder builder = BuildersFactory.eINSTANCE.createBuilder();
		final String ruleName = rule.getName();
		String builderNameSuffix = getBuilderNameSuffix(ruleName);
		builder.setName("I" + builderNameSuffix);
		
		final NameScope methodNameScope = ScopeUtils.getSafeToplevelScope();
		methodNameScope.registerName("release");
		
		BuilderMethod initMethod = createInitMethod(rule, builder,
				methodNameScope);

		BuilderMethod factoryMethod = createFactoryMethod(rule, builder);
		final String builderVarName = ruleVariablesScope.getUniqueName("builder");
		VariableDefinition builderVar = putBuilderCreationStatement(poolVar, 
				rule, factoryMethod, builderVarName, initMethod);

		createReleaseMethod(builder);
		
		MethodCall releaseCall = BuildersFactory.eINSTANCE.createMethodCall();
		releaseCall.setBuilderVariableName(poolVar.getFieldName());
		// HACK: There's no such method modeled
		BuilderMethod releaseBuilderMethod = BuildersFactory.eINSTANCE.createBuilderMethod();
		releaseBuilderMethod.setName("release" + builderNameSuffix);
		releaseCall.setMethod(releaseBuilderMethod);
		ParameterReferenceArgument argument = AntlrFactory.eINSTANCE.createParameterReferenceArgument();
		argument.setParameter(builderVar);
		releaseCall.getArguments().add(argument);
		rule.setBuilderReleaseStatement(releaseCall);
		
		EList<ANTLRProduction> productions = rule.getProductions();
		final boolean singleProduction = productions.size() == 1;
		for (ANTLRProduction production : productions) {
			final NameScope productionScope = ruleVariablesScope.createChildScope();
			ANTLRExpression expression = production.getExpression();
			boolean passesResultArgument = passesResultArgument(expression);
			if (passesResultArgument) {
				putCreateResultStatement(builder, rule, production, methodNameScope, builderVarName);
			}
			
			String ruleResultType = rule.getResultVariable().getType();
			if (!passesResultArgument && hasSingleCallsOnly(expression)) {
				final BuilderMethod method = BuildersFactory.eINSTANCE.createBuilderMethod();
				final MethodCall methodCall = BuildersFactory.eINSTANCE.createMethodCall();
				methodCall.setBuilderVariableName(builderVarName);
				final NameScope parameterScope = ScopeUtils.getSafeToplevelScope();
				traverseThroughInformativeItems(expression, productionScope, new IItemHandler() {

					@Override
					public void handleItem(AssignableValue value, String type,
							String basicName, Argument argument) {
						if (!singleProduction) {
							// Set special name for productions with a single special symbol 
							if (method.getName() == null) {
								method.setName(methodNameScope.getUniqueName(basicName));
							} else {
								method.setName(ruleName);
							}
						}
						// TODO: We think that in all-single-calls production we don't need to group symbols
						Parameter parameter = AntlrFactory.eINSTANCE.createParameter();
						parameter.setName(parameterScope.getUniqueName(basicName));
						parameter.setType(type);
						method.getParameters().add(parameter);
						
						methodCall.getArguments().add(argument);
					}
					
				});
				if (singleProduction) {
					method.setName(methodNameScope.getUniqueName(rule.getName()));
				} else {
					// If method's name was not set by previous, then no informative items were found by the 
					// parameter adding algorithm, try the default one:
					if (method.getName() == null) {
						method.setName(getSingleCallsMethodName(methodNameScope, expression, rule.getName()));
					}
				}
				method.setType(ruleResultType);
				
				methodCall.setMethod(method);

				if (!isTrivial(method)) {
					production.setAfter(assignIfNotVoid(rule.getResultVariable(), methodCall));
					builder.getMethods().add(method);
				}
			} else {
				final Map<Rule, BuilderMethod> rulesToMethods = new HashMap<Rule, BuilderMethod>();
				traverseThroughInformativeItems(expression, productionScope, new IItemHandler() {

					@Override
					public void handleItem(AssignableValue value, String type,
							String basicName, Argument argument) {
						BuilderMethod method;
						if (value instanceof RuleCall) {
							RuleCall call = (RuleCall) value;
							if (call.isSeparate()) {
								method = createBuilderMethod(methodNameScope, type, basicName);
							} else {
								method = rulesToMethods.get(call.getRule());
							}
							if (method == null) {
								method = createBuilderMethod(
										methodNameScope, type, basicName);
								rulesToMethods.put(call.getRule(), method);
							}
						} else {
							method = createBuilderMethod(methodNameScope, type, basicName);
						}

						MethodCall methodCall = BuildersFactory.eINSTANCE.createMethodCall();
						methodCall.setBuilderVariableName(builderVarName);
						methodCall.getArguments().add(argument);
						
						methodCall.setMethod(method);
						value.setAfter(methodCall);
					}

					private BuilderMethod createBuilderMethod(
							final NameScope methodNameScope, String type,
							String basicName) {
						BuilderMethod method = BuildersFactory.eINSTANCE.createBuilderMethod();
						method.setName(methodNameScope.getUniqueName(basicName));
						method.setType("void");
						Parameter parameter = AntlrFactory.eINSTANCE.createParameter();
						parameter.setName(basicName);
						parameter.setType(type);
						method.getParameters().add(parameter);
						builder.getMethods().add(method);
						return method;
					}
					
				});
				if (!JavaUtils.isVoid(ruleResultType) && !passesResultArgument) {
					putGetResultStatement(rule, builder, production, builderVarName);
				}
			}
		}
		if (builder.getMethods().size() == 2) {
			// only init() and release() are present
			rule.setBuilderCreationStatement(null);
			rule.setBuilderReleaseStatement(null);
			return;
		}
		poolVar.getBuilderFactory().getMethods().add(factoryMethod);
		poolVar.getBuilderFactory().getBuilders().add(builder);
	}

	private Statement assignIfNotVoid(VariableDefinition resultVariable,
			MethodCall methodCall) {
		if (JavaUtils.isVoid(methodCall.getMethod().getType())) {
			return methodCall;
		}
		return assign(resultVariable, methodCall);
	}

	private Statement assign(VariableDefinition resultVariable,
			MethodCall methodCall) {
		Assignment assignment = BuildersFactory.eINSTANCE.createAssignment();
		assignment.setVariable(resultVariable);
		assignment.setMethodCall(methodCall);
		return assignment;
	}

	private BuilderMethod createReleaseMethod(final Builder builder) {
		BuilderMethod releaseMethod = BuildersFactory.eINSTANCE.createBuilderMethod();
		releaseMethod.setType("void");
		releaseMethod.setName("release");
		builder.getMethods().add(releaseMethod);
		return releaseMethod;
	}

	private BuilderMethod createInitMethod(final SyntacticalRule rule,
			final Builder builder, final NameScope methodNameScope) {
		BuilderMethod initMethod = BuildersFactory.eINSTANCE.createBuilderMethod();
		initMethod.setType("void");
		initMethod.setName(methodNameScope.getUniqueName("init"));
		builder.getMethods().add(initMethod);
		
		for (Parameter parameter : rule.getParameters()) {
			initMethod.getParameters().add((Parameter) EcoreUtil.copy(parameter));			
		}
		return initMethod;
	}

	private interface IItemHandler {
		void handleItem(AssignableValue value, String type, String basicName, Argument argument);
	}
	
	private void traverseThroughInformativeItems(ANTLRExpression expression, final NameScope productionScope, final IItemHandler handler) {
		new AntlrSwitch<Boolean, Boolean>() {
			
			private void handleItem(AssignableValue value, String type,
					String name) {
				String basicName = JavaUtils.applyVarNameConventions(name);
				String uniqueName = productionScope.getUniqueName(basicName);
				value.setVariableName(uniqueName);
				AssignableValueReferenceArgument argument = AntlrFactory.eINSTANCE.createAssignableValueReferenceArgument();
				argument.setAssignableValue(value);
				handler.handleItem(value, type, basicName, argument);
			}
			
			private boolean doSwitchAll(Combination object, Boolean informative) {
				boolean result = false;
				for (ANTLRExpression expression : object.getExpressions()) {
					result |= doSwitch(expression, informative);
				}
				return result;
			}
			
			@Override
			public Boolean caseANTLRSequence(ANTLRSequence object, Boolean informative) {
				boolean contentsInformative = informative;
				if (object.getExpressions().size() > 1) {
					contentsInformative = false;
				}
				boolean informativeFound = doSwitchAll(object, contentsInformative);
				if (!informativeFound && informative) {
					handleItem(object, "String", "sequence");
				}
				return false;
			}
			
			@Override
			public Boolean caseANTLRAlternative(ANTLRAlternative object, Boolean informative) {
				return doSwitchAll(object, true);
			}
			
			@Override
			public Boolean caseANTLRIteration(ANTLRIteration object, Boolean informative) {
				return doSwitch(object.getExpression(), true);
			}
			
			@Override
			public Boolean caseANTLRCharacterRange(ANTLRCharacterRange object, Boolean informative) {
				if (informative || !ANTLRUtils.isSingleCharacter(object)) {
					handleItem(object, "String", ourNameRetriever.caseANTLRCharacterRange(object, null));
					return true;
				}
				return false;
			}
			
			@Override
			public Boolean caseLexicalLiteral(LexicalLiteral object, Boolean informative) {
				if (informative) {
					handleItem(object, "String", ourNameRetriever.caseLexicalLiteral(object, null));
				}
				return informative;
			}
			
			@Override
			public Boolean caseRuleCall(RuleCall object, Boolean informative) {
				String type;
				Rule rule = object.getRule();
				if (rule instanceof LexicalRule) {
					type = "String";
				} else {
					SyntacticalRule syntacticalRule = (SyntacticalRule) rule;
					type = syntacticalRule.getResultVariable().getType();
				}
				if (!JavaUtils.isVoid(type)) {
					handleItem(object, type, ourNameRetriever.caseRuleCall(object, null));
				}
				return true;
			}
			
			@Override
			public Boolean caseTokenSwitch(TokenSwitch object, Boolean informative) {
				object.setVariableName("token");
				handleItem(object, "String", ourNameRetriever.caseTokenSwitch(object, null));
				return true;
			}
			
			@Override
			public Boolean defaultCase(EObject object, Boolean data) {
				return false;
			}
		}.doSwitch(expression, false);
	}
	
	private void putGetResultStatement(SyntacticalRule rule, Builder builder,
			ANTLRProduction production, String builderVarName) {
		BuilderMethod method = BuildersFactory.eINSTANCE.createBuilderMethod();
		method.setName("getResult");
		method.setType(rule.getResultVariable().getType());
		builder.getMethods().add(method);

		MethodCall methodCall = BuildersFactory.eINSTANCE.createMethodCall();
		methodCall.setBuilderVariableName(builderVarName);
		methodCall.setMethod(method);
		production.setAfter(assign(rule.getResultVariable(), methodCall));
	}

	private boolean hasSingleCallsOnly(ANTLRExpression expression) {
		return ourSingleCallChecker.doSwitch(expression, null);
	}

	private boolean passesResultArgument(ANTLRExpression expression) {
		return ourResultArgumentFinder.doSwitch(expression, null);
	}

	private void registerRuleHeaderVariables(SyntacticalRule rule, NameScope scope) {
		for (Parameter parameter : rule.getParameters()) {
			scope.registerName(parameter.getName());
		}
	}

	private BuilderMethod createFactoryMethod(SyntacticalRule rule, Builder builder) {
		BuilderMethod result = BuildersFactory.eINSTANCE.createBuilderMethod();
		result.setName(getBuilderFactoryMethodName(rule.getName()));
		result.setType(builder.getName());
		return result;
	}

	private VariableDefinition putBuilderCreationStatement(
			BuilderPoolVariable poolVar, SyntacticalRule rule, BuilderMethod factoryMethod, String builderVarName, BuilderMethod initMethod) {
		MethodCall getBuilderCall = BuildersFactory.eINSTANCE.createMethodCall();
		getBuilderCall.setBuilderVariableName(poolVar.getFieldName());
		getBuilderCall.setMethod(factoryMethod);
		VariableDefinition builderVar = BuildersFactory.eINSTANCE.createVariableDefinition();
		builderVar.setName(builderVarName);
		builderVar.setType(factoryMethod.getType());
		builderVar.setMethodCall(getBuilderCall);
		
		MethodCall initBuilderCall = BuildersFactory.eINSTANCE.createMethodCall();
		initBuilderCall.setBuilderVariableName(builderVarName);
		initBuilderCall.setMethod(initMethod);
		for (Parameter parameter : rule.getParameters()) {
			ParameterReferenceArgument arg = AntlrFactory.eINSTANCE.createParameterReferenceArgument();
			arg.setParameter(parameter);
			initBuilderCall.getArguments().add(arg);
		}
		
		CodeBlock block = BuildersFactory.eINSTANCE.createCodeBlock();
		block.getStatements().add(builderVar);
		block.getStatements().add(initBuilderCall);
		
		rule.setBuilderCreationStatement(block);
		
		return builderVar;
	}

	private void putCreateResultStatement(Builder builder, SyntacticalRule rule,
			ANTLRProduction production, NameScope methodNameScope, String builderVarName) {
		BuilderMethod method = BuildersFactory.eINSTANCE.createBuilderMethod();
		builder.getMethods().add(method);
		method.setType(rule.getResultVariable().getType());
		method.setName(methodNameScope.getUniqueName("createResult"));
		MethodCall methodCall = BuildersFactory.eINSTANCE.createMethodCall();
		methodCall.setMethod(method);
		methodCall.setBuilderVariableName(builderVarName);
		production.setBefore(assign(rule.getResultVariable(), methodCall));
	}

	private String getSingleCallsMethodName(final NameScope methodNameScope, ANTLRExpression expression,
			final String defaultName) {
		String name = ourNameRetriever.doSwitch(expression, null);
		String result = name == NAME_RETRIEVAL_ERROR ? defaultName : name;
		return JavaUtils.applyVarNameConventions(result);
	}

	private boolean isTrivial(BuilderMethod method) {
		return JavaUtils.isVoid(method.getType()) && method.getParameters().isEmpty();
	}

	public static String getFactoryName(String grammarName) {
		return "I" + JavaUtils.upcaseFirst(grammarName) + "Builders";
	}

	public static String getPoolsClassName(String grammarName) {
		return JavaUtils.upcaseFirst(grammarName) + "BuildersPools";
	}
	
	private static String getBuilderFactoryMethodName(String name) {
		return "get" + getBuilderNameSuffix(name);
	}

	private static String getBuilderNameSuffix(String name) {
		return JavaUtils.upcaseFirst(name) + "Builder";
	}

}
