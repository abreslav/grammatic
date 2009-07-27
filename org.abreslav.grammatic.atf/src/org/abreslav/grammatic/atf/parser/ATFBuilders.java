package org.abreslav.grammatic.atf.parser;

import static org.abreslav.grammatic.atf.ATFMetadata.AFTER;
import static org.abreslav.grammatic.atf.ATFMetadata.ASSIGNED_TO_ATTRIBUTES;
import static org.abreslav.grammatic.atf.ATFMetadata.ASSOCIATED_FUNCTION_NAME;
import static org.abreslav.grammatic.atf.ATFMetadata.ATF_NAMESPACE;
import static org.abreslav.grammatic.atf.ATFMetadata.BEFORE;
import static org.abreslav.grammatic.atf.ATFMetadata.DEFAULT_NAMESPACE;
import static org.abreslav.grammatic.atf.ATFMetadata.SEMANTIC_MODULE;
import static org.abreslav.grammatic.atf.ATFMetadata.SYNTACTIC_FUNCTION;
import static org.abreslav.grammatic.atf.ATFMetadata.TOKEN;
import static org.abreslav.grammatic.atf.ATFMetadata.TOKEN_CLASSES;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.addAttributeValue;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.addAttributeValueToSymbolAttribute;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.addContainedValueToSymbolAttribute;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.addCrossReferencedValue;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.addMapEnty;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.createCrossReferencesValue;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.getAttribute;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.getGrammarAssignment;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.getSymbolAttribute;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.getSymbolVariable;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.getVariable;
import static org.abreslav.grammatic.atf.parser.AspectDefinitionUtils.hasAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.ATFAttributeAssignment;
import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFExpression;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.AtfFactory;
import org.abreslav.grammatic.atf.Block;
import org.abreslav.grammatic.atf.CollectionAppending;
import org.abreslav.grammatic.atf.FunctionCall;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.Statement;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.template.parser.IImportsBuilders;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.AspectdefFactory;
import org.abreslav.grammatic.metadata.aspectdef.AssignmentRule;
import org.abreslav.grammatic.metadata.aspectdef.GrammarAssignment;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.SymbolReferenceQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;
import org.abreslav.grammatic.utils.IErrorHandler;

public class ATFBuilders implements IATFBuilders {
	// TODO : Reasonable imports
	private static final String DEFAULT_FUNCTION = "<default function>";
	
	private final ITypeSystemBuilder<?> myTypeSystemBuilder;
	private final IErrorHandler<RuntimeException> myErrorHandler;
	private final ATFImportsBuilders myImportsBuilders;
	
	private final SemanticResolver mySemanticResolver = new SemanticResolver();
	
	private AssignmentRule myCurrentAssignment;
	private Namespace myCurrentNamespace;
	
	public ATFBuilders(IATFModuleLoader parser, ITypeSystemBuilder<?> typeSystemBuilder, IErrorHandler<RuntimeException> errorHandler) {
		myErrorHandler = errorHandler;
		myImportsBuilders = new ATFImportsBuilders(errorHandler, parser, mySemanticResolver);
		myTypeSystemBuilder = typeSystemBuilder;
	}
	
	private void reportError(String string, Object... params) {
		myErrorHandler.reportError(string, params);		
	}
	
	private Namespace getCurrentNamespace() {
		if (myCurrentNamespace == null) {
			throw new IllegalStateException();
		}
		return myCurrentNamespace;
	}
	
	public IImportsBuilders getImportsBuilders() {
		return myImportsBuilders;
	}

	@Override
	public IAtfTypeBuilder getAtfTypeBuilder() {
		return new IAtfTypeBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public Object atfType(Object type) {
				myTypeSystemBuilder.addType(type);
				return type;
			}
		};
	}

	@Override
	public IAtfModuleBuilder getAtfModuleBuilder() {
		return new IAtfModuleBuilder() {

			private AspectDefinition myAspectDefinition;

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				GrammarAssignment grammarAssignment = getGrammarAssignment(myAspectDefinition);
				Attribute attribute = getAttribute(grammarAssignment, 
						ATF_NAMESPACE, ATFMetadata.USED_SEMANTIC_MODULES);
				for (SemanticModule semanticModule : myImportsBuilders.getImportedModules()) {
					addCrossReferencedValue(attribute, semanticModule);
				}
				myAspectDefinition = null;
			}
			
			@Override
			public AspectDefinition createResult() {
				myAspectDefinition = AspectdefFactory.eINSTANCE.createAspectDefinition();
				return myAspectDefinition;
			}

			@Override
			public void semanticModuleDeclaration(
					SemanticModule semanticModuleDeclaration) {
				GrammarAssignment grammarAssignment = getGrammarAssignment(myAspectDefinition);
				Attribute targetAttribute = getAttribute(grammarAssignment, ATF_NAMESPACE, SEMANTIC_MODULE); 
				addCrossReferencedValue(targetAttribute, semanticModuleDeclaration);
				
				mySemanticResolver.registerModule(semanticModuleDeclaration);
			}

		};
	}
	
	@Override
	public ISemanticModuleBuilder getSemanticModuleBuilder() {
		return new ISemanticModuleBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public SemanticModule semanticModule(SemanticModule semanticModuleDeclaration) {
				return semanticModuleDeclaration;
			}
			
		};
	}
	
	@Override
	public ITopLevelDeclarationBuilder getTopLevelDeclarationBuilder() {
		return new ITopLevelDeclarationBuilder() {

			private AspectDefinition myAspectDef;

			@Override
			public void init(AspectDefinition aspectDef) {
				myAspectDef = aspectDef;
			}

			@Override
			public void release() {
				myAspectDef = null;
			}

			@Override
			public void syntacticRuleDeclaration(
					AssignmentRule syntacticRuleDeclaration) {
				myAspectDef.getAssignmentRules().add(syntacticRuleDeclaration);
			}

			@Override
			public void tokenRuleDeclaration(AssignmentRule tokenRuleDeclaration) {
				myAspectDef.getAssignmentRules().add(tokenRuleDeclaration);
			}
			
		};
	}
	
	@Override
	public ISemanticModuleDeclarationBuilder getSemanticModuleDeclarationBuilder() {
		return new ISemanticModuleDeclarationBuilder() {

			private SemanticModule mySemanticModule;
			private final Set<String> myFunctionNames = new HashSet<String>();

			@Override
			public void init() {
				mySemanticModule = AtfFactory.eINSTANCE.createSemanticModule();
			}
			
			@Override
			public void release() {
				mySemanticModule = null;
				myFunctionNames.clear();
			}
			
			@Override
			public SemanticModule getResult() {
				return mySemanticModule;
			}

			@Override
			public void name(String name) {
				mySemanticModule.setName(name);
			}

			@Override
			public void semanticFunction(FunctionSignature semanticFunction) {
				if (myFunctionNames.contains(semanticFunction.getName())) {
					reportError("Function name already used in semantic module %s: %s", mySemanticModule.getName(), semanticFunction.getName());
				}
				mySemanticModule.getFunctions().add(semanticFunction);
			}

		};
	}
	
	@Override
	public ISemanticFunctionBuilder getSemanticFunctionBuilder() {
		return new ISemanticFunctionBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public FunctionSignature semanticFunction(
					FunctionSignature functionSignature) {
				return functionSignature;
			}
			
		};
	}
	
	@Override
	public IFunctionSignatureBuilder getFunctionSignatureBuilder() {
		return new IFunctionSignatureBuilder() {

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}
			
			@Override
			public FunctionSignature functionSignature(String name,
					List<ATFAttribute> in, List<ATFAttribute> out) {
				FunctionSignature signature = AtfFactory.eINSTANCE.createFunctionSignature();
				signature.setName(name);
				signature.getInputAttributes().addAll(in);
				if (out != null) {
					signature.getOutputAttributes().addAll(out);
				}
				return signature;
			}
		};
	}

	@Override
	public ITupleBuilder getTupleBuilder() {
		return new ITupleBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public List<ATFAttribute> tuple(
					List<ATFAttribute> attributeDeclarationList) {
				return attributeDeclarationList == null 
					? Collections.<ATFAttribute>emptyList() 
					: attributeDeclarationList;
			}
			
		};
	}
	
	@Override
	public IAttributeDeclarationListBuilder getAttributeDeclarationListBuilder() {
		return new IAttributeDeclarationListBuilder() {

			private List<ATFAttribute> myResult;

			@Override
			public void init() {
				myResult = new ArrayList<ATFAttribute>();
			}
			
			@Override
			public void release() {
				myResult = null;
			}
			
			@Override
			public List<ATFAttribute> getResult() {
				return myResult;
			}
			
			@Override
			public void attributeDeclaration(ATFAttribute attributeDeclaration) {
				myResult.add(attributeDeclaration);
			}

		};
	}
	
	@Override
	public IAttributeDeclarationBuilder getAttributeDeclarationBuilder() {
		return new IAttributeDeclarationBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public ATFAttribute attributeDeclaration(Object atfType, String name) {
				ATFAttribute attribute = AtfFactory.eINSTANCE.createATFAttribute();
				attribute.setName(name);
				attribute.setType(atfType);
				return attribute;
			}
			
		};
	}

	@Override
	public ISyntacticRuleDeclarationBuilder getSyntacticRuleDeclarationBuilder() {
		return new ISyntacticRuleDeclarationBuilder() {
			

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				myCurrentAssignment = null;
				
				mySemanticResolver.leaveSymbolScope();
			}
			
			@Override
			public AssignmentRule createResult() {
				AssignmentRule assignmentRule = AspectdefFactory.eINSTANCE.createAssignmentRule();
				assignmentRule.setMatchesLowerBound(1);
				assignmentRule.setMatchesUpperBound(1);
				myCurrentAssignment = assignmentRule;
				return assignmentRule;
			}

			@Override
			public void ruleQueryContainer(
					QueryContainer<RuleQuery> ruleQueryContainer) {
				myCurrentAssignment.setQueryContainer(ruleQueryContainer);
				
				mySemanticResolver.enterSymbolScope(getSymbolVariable(myCurrentAssignment).getName());
			}
			
		};
	}
	
	@Override
	public IAssociatedFunctionsBuilder getAssociatedFunctionsBuilder() {
		return new IAssociatedFunctionsBuilder() {
			
			private SemanticModule mySemanticModule;
			
			@Override
			public void init(AssignmentRule ar) {
			}
			
			@Override
			public void release() {
				mySemanticModule = null;
			}

			@Override
			public void defaultSyntacticFunction(
					Namespace defaultSyntacticFunction) {
				addContainedValueToSymbolAttribute(myCurrentAssignment, 
						ATF_NAMESPACE, ATFMetadata.DEFAULT_NAMESPACE, defaultSyntacticFunction);
				// nothing to do
			}

			@Override
			public void defaultSyntacticFunction1(
					Namespace defaultSyntacticFunction) {
				defaultSyntacticFunction(defaultSyntacticFunction);
			}

			@Override
			public void semanticFunction(FunctionSignature semanticFunction) {
				if (mySemanticModule == null) {
					mySemanticModule = AtfFactory.eINSTANCE.createSemanticModule();
					addContainedValueToSymbolAttribute(myCurrentAssignment, 
							ATF_NAMESPACE, SEMANTIC_MODULE, mySemanticModule);
				}
				mySemanticModule.getFunctions().add(semanticFunction);
				
				mySemanticResolver.registerFunction(semanticFunction);
			}

			@Override
			public void syntacticFunction(Namespace syntacticFunction) {
//				addContainedValueToSymbolAttribute(myCurrentAssignment, 
//						ATF_NAMESPACE, SYNTACTIC_FUNCTION_NAMESPACES, syntacticFunction);
			}
			
		};
	}
	
	@Override
	public IDefaultSyntacticFunctionBuilder getDefaultSyntacticFunctionBuilder() {
		return new IDefaultSyntacticFunctionBuilder() {

			private FunctionSignature myFunctionSignature;

			@Override
			public void init() {
				myFunctionSignature = AtfFactory.eINSTANCE.createFunctionSignature();
				VariableDefinition symbolVariable = getSymbolVariable(myCurrentAssignment);
				// Renamed afterwards by post-processor
				myFunctionSignature.setName(symbolVariable.getName());
				
				addContainedValueToSymbolAttribute(myCurrentAssignment, ATF_NAMESPACE, ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION, myFunctionSignature);
				
				mySemanticResolver.enterFunctionScope(myFunctionSignature.getName());
			}

			@Override
			public void release() {
				createLocalSemanticModule("<default: to be set by post-processor>");
				myFunctionSignature = null;
				
				mySemanticResolver.leaveFunctionScope();
				myCurrentNamespace = null;
			}
			
			@Override
			public Namespace createResult() {
				myCurrentNamespace = MetadataFactory.eINSTANCE.createNamespace();
				String name = getSymbolVariable(myCurrentAssignment).getName();
				myCurrentNamespace.setUri("namespace://" + name + "/default");
				addContainedValueToSymbolAttribute(myCurrentAssignment, ATF_NAMESPACE, DEFAULT_NAMESPACE, myCurrentNamespace);
				addContainedValueToSymbolAttribute(myCurrentAssignment, myCurrentNamespace, SYNTACTIC_FUNCTION, myFunctionSignature);
				return myCurrentNamespace;
			}

			@Override
			public void inputAttributes(List<ATFAttribute> inputAttributes) {
				myFunctionSignature.getInputAttributes().addAll(inputAttributes);
				mySemanticResolver.registerAttributes(inputAttributes);
			}

			@Override
			public void outputAttributes(List<ATFAttribute> outputAttributes) {
				myFunctionSignature.getOutputAttributes().addAll(outputAttributes);
				mySemanticResolver.registerAttributes(outputAttributes);
			}
			
		};
	}
	
	@Override
	public ISyntacticFunctionBuilder getSyntacticFunctionBuilder() {
		return new ISyntacticFunctionBuilder() {
			
			private Namespace myNamespace;
			private String myName;

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				createLocalSemanticModule(myName);
				myNamespace = null;
				myCurrentNamespace = null;
				mySemanticResolver.leaveFunctionScope();
				myName = null;
			}

			@Override
			public Namespace createResult() {
				myNamespace = MetadataFactory.eINSTANCE.createNamespace();
				myCurrentNamespace = myNamespace;
				return myNamespace;
			}

			@Override
			public void functionSignature(FunctionSignature functionSignature) {
				String name = functionSignature.getName();
				myName = name;
				
				mySemanticResolver.enterFunctionScope(name);
				mySemanticResolver.registerAttributes(functionSignature.getInputAttributes());
				mySemanticResolver.registerAttributes(functionSignature.getOutputAttributes());
				
				myNamespace.setUri("namespace://" + name);
				addContainedValueToSymbolAttribute(myCurrentAssignment, 
						myNamespace, SYNTACTIC_FUNCTION, functionSignature);

				Namespace namespace = myNamespace;
				AssignmentRule currentAssignment = myCurrentAssignment;
				addMapEnty(currentAssignment, ATFMetadata.FUNCTION_NAME_TO_NAMESPACE, name, namespace);
				addMapEnty(currentAssignment, ATFMetadata.FUNCTION_NAME_TO_FUNCTION, name, functionSignature);
//				addContainedValueToSymbolAttribute(myCurrentAssignment, 
//						ATF_NAMESPACE, SYNTACTIC_FUNCTIONS, functionSignature);
			}
			
		};
	}
	
	@Override
	public ILocalDeclarationsBuilder getLocalDeclarationsBuilder() {
		return new ILocalDeclarationsBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}
			
			@Override
			public void attributeDeclaration(ATFAttribute attributeDeclaration) {
				mySemanticResolver.registerAttribute(attributeDeclaration);
			}

			@Override
			public void semanticFunction(FunctionSignature semanticFunction) {
//				addContainedValueToSymbolAttribute(myCurrentAssignment, 
//						myCurrentNamespace, SEMANTIC_FUNCTIONS, semanticFunction);
				
				mySemanticResolver.registerFunction(semanticFunction);
			}
			
		};
	}
	
	@Override
	public ITokenRuleDeclarationBuilder getTokenRuleDeclarationBuilder() {
		return new ITokenRuleDeclarationBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public AssignmentRule tokenRuleDeclaration(String name, Set<String> classes) {
				/*
				 *  <name> : ..
				 *  	@<name>.token; 
				 */
				AssignmentRule assignmentRule = AspectdefFactory.eINSTANCE.createAssignmentRule();
				QueryContainer<RuleQuery> container = QueryFactory.eINSTANCE.createQueryContainer();
				RuleQuery ruleQuery = QueryFactory.eINSTANCE.createRuleQuery();
				SymbolQuery symbolQuery = QueryFactory.eINSTANCE.createSymbolQuery();
				symbolQuery.setName(name);
				ruleQuery.setSymbol(symbolQuery);
				ProductionQuery productionQuery = QueryFactory.eINSTANCE.createProductionQuery();
				productionQuery.setDefinition(QueryFactory.eINSTANCE.createSequenceWildcard());
				ruleQuery.getDefinitions().add(productionQuery);
				container.setQuery(ruleQuery);
				assignmentRule.setQueryContainer(container);
				assignmentRule.setMatchesLowerBound(1);
				assignmentRule.setMatchesUpperBound(1);
				getSymbolAttribute(assignmentRule, ATF_NAMESPACE, TOKEN);
				if (classes != null) {
					for (String string : classes) {
						addAttributeValueToSymbolAttribute(assignmentRule, ATF_NAMESPACE, TOKEN_CLASSES, string);
					}
				}
				return assignmentRule;
			}
			
		};
	}
	
	@Override
	public IIdListBuilder getIdListBuilder() {
		return new IIdListBuilder() {
			
			private Set<String> myResult;

			@Override
			public void init() {
				myResult = new HashSet<String>();
			}
			
			@Override
			public void release() {
				myResult = null;
			}

			@Override
			public Set<String> getResult() {
				return myResult;
			}
			
			@Override
			public void name(String name) {
				myResult.add(name);
			}
			
		};
	}
	
	///////////////////////////////////////////////////////
	
	@Override
	public ILabelDefinitionBuilder getLabelDefinitionBuilder() {
		return new ILabelDefinitionBuilder() {

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public void labelDefinition(List<Attribute> labelList,
					Statement statement) {
				for (Attribute attribute : labelList) {
					attribute.setValue(createCrossReferencesValue(statement));
				}
			}

			@Override
			public void nl(Set<VariableDefinition> nl) {
				// nothing to do
			}
			
		};
	}
	
	@Override
	public ILabelListBuilder getLabelListBuilder() {
		return new ILabelListBuilder() {
			
			private List<Attribute> myResult;

			@Override
			public void init() {
				myResult = new ArrayList<Attribute>();
			}
			
			@Override
			public void release() {
				myResult = null;
			}

			@Override
			public List<Attribute> getResult() {
				return myResult;
			}

			@Override
			public void label(Attribute attribute) {
				myResult.add(attribute);
			}
			
		};
	}
	
	@Override
	public ILabelBuilder getLabelBuilder() {
		return new ILabelBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public Attribute after() {
				return getSymbolAttribute(myCurrentAssignment, 
						myCurrentNamespace, AFTER);
			}

			@Override
			public Attribute before() {
				return getSymbolAttribute(myCurrentAssignment, 
						myCurrentNamespace, BEFORE);
			}

			@Override
			public Attribute label(VariableDefinition simpleLabel, String name) {
				if (name == null) {
					name = AFTER;
				}
				return getAttribute(myCurrentAssignment, simpleLabel, myCurrentNamespace, name);
			}
			
		};
	}
	
	@Override
	public ISimpleLabelBuilder getSimpleLabelBuilder() {
		return new ISimpleLabelBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}
			
			@Override
			public VariableDefinition name(String name) {
				VariableDefinition variable = getVariable(myCurrentAssignment, name);
				if (variable == null) {
					reportError("Unknown label: variable %s in a function for %s not found", 
							name, getSymbolVariable(myCurrentAssignment).getName());
					todoAppend();
				}
				return variable;
			}
			
			@Override
			public VariableDefinition character(char character) {
				VariableDefinition variable = lookUpCharacter(myCurrentAssignment.getQueryContainer(), character);
				processLabelVariable(variable, "'" + character + "'");
				return variable;
			}

			@Override
			public VariableDefinition string(String string) {
				string = string.substring(1, string.length() - 1);
				VariableDefinition variable = lookUpString(myCurrentAssignment.getQueryContainer(), string);
				processLabelVariable(variable, "'" + string + "'");
				return variable;
			}

			private void processLabelVariable(VariableDefinition variable,
					String label) {
				if (variable == null) {
					VariableDefinition symbolVariable = getSymbolVariable(myCurrentAssignment);
					reportError("'%s': Label not found: %s", symbolVariable.getName(), label);
				} else {
					myCurrentAssignment.getQueryContainer().getVariableDefinitions().add(variable);
				}
			}
			
		};
	}
	
	@Override
	public IStatementBuilder getStatementBuilder() {
		return new IStatementBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public Statement appending(CollectionAppending appending) {
				return appending;
			}

			@Override
			public Statement assignment(ATFAttributeAssignment assignment) {
				return assignment;
			}

			@Override
			public Statement block(Block block) {
				return block;
			}

			@Override
			public Statement functionCall(FunctionCall functionCall) {
				return functionCall;
			}
			
		};
	}
	
	@Override
	public IAssignmentBuilder getAssignmentBuilder() {
		return new IAssignmentBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public ATFAttributeAssignment assignment(
					ATFAttributeAssignment attributeReference,
					ATFExpression atfExpression) {
				List<ATFAttributeReference> leftSide = attributeReference.getLeftSide();
				int leftCount = leftSide.size();
				if (leftCount > 1) {
					if (false == atfExpression instanceof FunctionCall) {
						reportError("Only functions can be assignedto tuples");
					}
					FunctionCall functionCall = (FunctionCall) atfExpression;
					FunctionSignature function = functionCall.getFunction();

					if (checkOutputCount(leftCount, function)) {
						Iterator<ATFAttributeReference> iterator = leftSide.iterator();
						for (ATFAttribute attribute : function.getOutputAttributes()) {
							ATFAttributeReference reference = iterator.next();
							mySemanticResolver.suggestAttributeName(attribute, reference.getAttribute().getName());
						}
					}
				} else if (leftCount == 1) {
					addTypingConstraintForSingleAssignment(leftSide.get(0).getAttribute(), atfExpression);
				} else {
					throw new IllegalStateException("Number of attributes on the left is less than one");
				}
				attributeReference.setRightSide(atfExpression);
				return attributeReference;
			}

		};
	}
	
	@Override
	public IAttributeReferenceBuilder getAttributeReferenceBuilder() {
		return new IAttributeReferenceBuilder() {
			
			private ATFAttributeAssignment myAssignment;

			@Override
			public void init() {
				myAssignment = AtfFactory.eINSTANCE.createATFAttributeAssignment();
			}
			
			@Override
			public void release() {
				myAssignment = null;
			}

			@Override
			public ATFAttributeAssignment getResult() {
				return myAssignment;
			}

			@Override
			public ATFAttributeAssignment singleAttributeReference(
					ATFAttributeReference singleAttributeReference) {
				myAssignment.getLeftSide().add(singleAttributeReference);
				return myAssignment;
			}

			@Override
			public void singleAttributeReference1(
					ATFAttributeReference singleAttributeReference) {
				singleAttributeReference(singleAttributeReference);
			}
			
		};
	}
	
	@Override
	public ISingleAttributeReferenceBuilder getSingleAttributeReferenceBuilder() {
		return new ISingleAttributeReferenceBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public ATFAttributeReference singleAttributeReference(String name) {
				ATFAttributeReference reference = AtfFactory.eINSTANCE.createATFAttributeReference();
				reference.setAttribute(mySemanticResolver.getAttribute(name));
				return reference;
			}
			
		};
	}
	
	@Override
	public IAtfExpressionBuilder getAtfExpressionBuilder() {
		return new IAtfExpressionBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public ATFExpression functionCall(FunctionCall functionCall) {
				return functionCall;
			}

			@Override
			public ATFExpression singleAttributeReference(
					ATFAttributeReference singleAttributeReference) {
				return singleAttributeReference;
			}

			@Override
			public ATFExpression text(ATFAttribute text) {
				return wrapIntoReference(text);
			}

			@Override
			public ATFExpression defaultAttributeReference(
					ATFAttribute defaultAttributeReference) {
				return wrapIntoReference(defaultAttributeReference);
			}
			
		};
	}
	
	@Override
	public IFunctionCallBuilder getFunctionCallBuilder() {
		return new IFunctionCallBuilder() {
			
			private FunctionCall myFunctionCall;

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				myFunctionCall = null;
			}

			@Override
			public FunctionCall createResult() {
				myFunctionCall = AtfFactory.eINSTANCE.createFunctionCall();
				return myFunctionCall;
			}

			@Override
			public void functionReference(FunctionSignature functionReference) {
				myFunctionCall.setFunction(functionReference);
			}
			
		};
	}
	
	@Override
	public IFunctionReferenceBuilder getFunctionReferenceBuilder() {
		return new IFunctionReferenceBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public FunctionSignature functionReference(String module,
					String name) {
				if (module != null) {
					return mySemanticResolver.getFunctionFromModule(module, name);
				}
				FunctionSignature function = mySemanticResolver.getLocalFunction(name);
				if (function != null) {
					return function;
				}
				return mySemanticResolver.getFunctionStub(name);
			}
			
		};
	}
	
	@Override
	public IArgumentsBuilder getArgumentsBuilder() {
		return new IArgumentsBuilder() {
			
			private FunctionCall myFunctionCall;

			@Override
			public void init(FunctionCall call) {
				myFunctionCall = call;
			}
			
			@Override
			public void release() {
				FunctionSignature function = myFunctionCall.getFunction();
				int inputCount = myFunctionCall.getArguments().size();
				if (mySemanticResolver.isFreshStub(function)) {
					mySemanticResolver.suggestInputCount(function, inputCount);
					
					Iterator<ATFExpression> iterator = myFunctionCall.getArguments().iterator();
					for (ATFAttribute inputAttribute : function.getInputAttributes()) {
						ATFExpression expression = iterator.next();
						String name;
						if (expression instanceof ATFAttributeReference) {
							ATFAttributeReference ref = (ATFAttributeReference) expression;
							name = ref.getAttribute().getName();
						} else if (expression instanceof FunctionCall) {
							FunctionCall call = (FunctionCall) expression;
							name = call.getFunction().getName();
						} else {
							throw new IllegalStateException("Unknown exression type");
						}
						mySemanticResolver.suggestAttributeName(inputAttribute, name);
					}
					
				} else {
					int size = function.getInputAttributes().size();
					if (size != inputCount) {
						reportError("%s.%s: Inconsistent call of %s -- wrong arguments count (expected %d, but is %d)", 
								mySemanticResolver.getCurrentSymbol(),
								mySemanticResolver.getCurrentFunction(),
								function.getName(),
								inputCount,
								size);
						todoAppend();
					}
				}
				addTypingConstraintsForInputAttributes(myFunctionCall);

				myFunctionCall = null;
			}

			@Override
			public void atfExpression(ATFExpression atfExpression) {
				myFunctionCall.getArguments().add(atfExpression);
			}
			
		};
	}
	
	@Override
	public ITextBuilder getTextBuilder() {
		return new ITextBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public ATFAttribute text(VariableDefinition variable) {
				return mySemanticResolver.getTextAttribute(variable);
			}

		};
	}
	
	@Override
	public IDefaultAttributeReferenceBuilder getDefaultAttributeReferenceBuilder() {
		return new IDefaultAttributeReferenceBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public ATFAttribute defaultAttributeReference(
					VariableDefinition variable) {
				return mySemanticResolver.getDefaultAttribute(variable);
			}
			
		};
	}
	
	@Override
	public IAppendingBuilder getAppendingBuilder() {
		return new IAppendingBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public CollectionAppending appending(
					ATFAttributeReference singleAttributeReference,
					ATFExpression atfExpression) {
				CollectionAppending appending = AtfFactory.eINSTANCE.createCollectionAppending();
				appending.setLeftSide(singleAttributeReference);
				appending.setRightSide(atfExpression);
				
				return appending;
			}
			
		};
	}
	
	@Override
	public IBlockBuilder getBlockBuilder() {
		return new IBlockBuilder() {
			
			private Block myBlock;

			@Override
			public void init() {
				myBlock = AtfFactory.eINSTANCE.createBlock();
			}
			
			@Override
			public void release() {
				myBlock = null;
			}

			@Override
			public Block getResult() {
				return myBlock;
			}

			@Override
			public void statement(Statement statement) {
				myBlock.getStatements().add(statement);
			}
			
		};
	}
	
	@Override
	public IVariableBuilder getVariableBuilder() {
		return new IVariableBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public VariableDefinition variable(String name) {
				VariableDefinition variable = getVariable(myCurrentAssignment, name);
				if (variable == null) {
					reportError("No variable %s in query for symbol %s", name, getSymbolVariable(myCurrentAssignment).getName());
					todoAppend();
				}
				return variable;
			}

		};
	}
	
	@Override
	public INameListBuilder getNameListBuilder() {
		return new INameListBuilder() {
			
			private Set<VariableDefinition> myResult;

			@Override
			public void init() {
				myResult = new HashSet<VariableDefinition>();
			}
			
			@Override
			public void release() {
				myResult = null;
			}

			@Override
			public Set<VariableDefinition> getResult() {
				return myResult;
			}

			@Override
			public void variable(VariableDefinition variable) {
				assertSymbolVariable(variable);
				myResult.add(variable);
			}

		};
	}
	
	@Override
	public ISymbolMappingBuilder getSymbolMappingBuilder() {
		return new ISymbolMappingBuilder() {

			private Set<VariableDefinition> myVariables;

			@Override
			public void init(Set<VariableDefinition> names) {
				myVariables = names;
			}

			@Override
			public void release() {
				myVariables = null;
			}

			@Override
			public void symbolMapping(
					ATFAttributeAssignment attributeAssignment,
					String associatedFunctionReference,
					List<ATFExpression> expressionList) {
				List<ATFAttributeReference> leftSide 
					= attributeAssignment == null ? null : attributeAssignment.getLeftSide();
				for (VariableDefinition variable : myVariables) {
					writeSymbolMapping(variable, leftSide,
							associatedFunctionReference, expressionList);
				}
			}

		};
	}
	
	@Override
	public IExpressionListBuilder getExpressionListBuilder() {
		return new IExpressionListBuilder() {
			
			private List<ATFExpression> myResult;

			@Override
			public void init() {
				myResult = new ArrayList<ATFExpression>();
			}
			
			@Override
			public void release() {
				myResult = null;
			}

			@Override
			public void atfExpression(ATFExpression atfExpression) {
				myResult.add(atfExpression);
			}

			@Override
			public List<ATFExpression> getResult() {
				return myResult;
			}
			
		};
	}
	
	@Override
	public IAssociatedFunctionReferenceBuilder getAssociatedFunctionReferenceBuilder() {
		return new IAssociatedFunctionReferenceBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public String name(String name) {
				return name;
			}

			@Override
			public String hash() {
				return DEFAULT_FUNCTION;
			}
			
		};
	}
	
	//////////////////////////////////////////////////////////////////////////////
	
	private void writeSymbolMapping(VariableDefinition variable,
			List<ATFAttributeReference> leftSide,
			String associatedFunctionReference,
			List<ATFExpression> arguments) {
		if (hasAttribute(myCurrentAssignment, variable, myCurrentNamespace, ATFMetadata.ASSOCIATED_WITH_DEFAULT_FUNCTION)
				|| hasAttribute(myCurrentAssignment, variable, myCurrentNamespace, ASSOCIATED_FUNCTION_NAME)) {
			reportError("Variable '%s' is already associated with a function call", variable.getName());
			todoAppend();
		}
		if (leftSide != null) {
			Attribute assignedTo = getAttribute(myCurrentAssignment, 
					variable, myCurrentNamespace, ASSIGNED_TO_ATTRIBUTES);
			for (ATFAttributeReference ref : leftSide) {
				addCrossReferencedValue(assignedTo, EMFProxyUtil.copy(ref));
			}
		}
		
		if (associatedFunctionReference == DEFAULT_FUNCTION) {
			getAttribute(myCurrentAssignment, variable, myCurrentNamespace, ATFMetadata.ASSOCIATED_WITH_DEFAULT_FUNCTION);
		} else {
			Attribute associatedFunctionCall = getAttribute(myCurrentAssignment, 
					variable, myCurrentNamespace, ASSOCIATED_FUNCTION_NAME);
			addAttributeValue(associatedFunctionCall, associatedFunctionReference);
		}
		Attribute args = getAttribute(myCurrentAssignment, variable, 
				myCurrentNamespace, ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
		for (ATFExpression expression : arguments) {
			addCrossReferencedValue(args, expression);
		}
	}

	private ATFAttributeReference wrapIntoReference(ATFAttribute text) {
		ATFAttributeReference ref = AtfFactory.eINSTANCE.createATFAttributeReference();
		ref.setAttribute(text);
		return ref;
	}
	
	private static VariableDefinition lookUpCharacter(QueryContainer<? extends Query> container, char c) {
		return new ExpressionFinder<Character>().doSwitch(container.getQuery(), c, IReplacementStrategy.CHARACTER_REPLACEMENT_STRATEGY);
	}
	
	private static VariableDefinition lookUpString(QueryContainer<? extends Query> container, String s) {
		return new ExpressionFinder<String>().doSwitch(container.getQuery(), s, IReplacementStrategy.STRING_REPLACEMENT_STRATEGY);
	}

	private void addTypingConstraintsForInputAttributes(
			FunctionCall functionCall) {
		Iterator<ATFExpression> expIterator = functionCall.getArguments().iterator();
		for (ATFAttribute inputAttribute : functionCall.getFunction().getInputAttributes()) {
			ATFExpression expression = expIterator.next();
			addTypingConstraintForSingleAssignment(inputAttribute,
					expression);
		}
	}

	private void addTypingConstraintForSingleAssignment(
			ATFAttribute inputAttribute, ATFExpression expression) {
		if (expression instanceof FunctionCall) {
			FunctionCall call = (FunctionCall) expression;
			FunctionSignature calledFunction = call.getFunction();
			if (checkOutputCount(1, calledFunction)) {
				ATFAttribute outputAttribute = calledFunction.getOutputAttributes().get(0);
				mySemanticResolver.suggestAttributeName(
						outputAttribute, 
						inputAttribute.getName());
			}
		}
	}

	/* returns true if we need to suggest attribute names */
	private boolean checkOutputCount(int leftCount,
			FunctionSignature function) {
		int rightOutputCount = function.getOutputAttributes().size();
		if (leftCount != rightOutputCount) {
			if (rightOutputCount == 0 
					&& mySemanticResolver.isStub(function)) {
				mySemanticResolver.suggestOutputCount(function, leftCount);
				return true;
			} else {
				reportError("Function %s: %d output attributes expected", function.getName(), leftCount);
				todoAppend();
			}
		}
		return false;
	}

	private void assertSymbolVariable(VariableDefinition variable) {
		Query query = variable.getValue();
		while (query instanceof VariableReference) {
			query = ((VariableReference) query).getVariable().getValue();
		}
		if (false == query instanceof SymbolReferenceQuery) {
			reportError("%s in query for symbol %s is not a symbol reference. Only symbol refernces may be mapped", 
							variable.getName(), 
							getSymbolVariable(myCurrentAssignment).getName());
		}
	}
	
	private void createLocalSemanticModule(String name) {
		Set<FunctionSignature> allLocalFunctions = mySemanticResolver.getAllLocalFunctions();
		if (!allLocalFunctions.isEmpty()) {
			SemanticModule semanticModule = AtfFactory.eINSTANCE.createSemanticModule();
			semanticModule.setName(name);
			semanticModule.getFunctions().addAll(allLocalFunctions);
			addContainedValueToSymbolAttribute(myCurrentAssignment, 
					myCurrentNamespace, SEMANTIC_MODULE, 
					semanticModule);
		}
	}

	private static void todoAppend() {
		throw new IllegalStateException("This method shuold be modified to support multi error reporting");
	}

	private class SemanticResolver implements ISemanticModuleHandler {
		
		private final Map<String, Map<String, FunctionSignature>> myModules = new HashMap<String, Map<String,FunctionSignature>>();
		private final Map<String, FunctionSignature> mySymbolScope = new HashMap<String, FunctionSignature>();
		private final Map<String, FunctionSignature> myFunctionScope = new HashMap<String, FunctionSignature>();
		private final Map<String, FunctionSignature> myStubScope = new HashMap<String, FunctionSignature>();
		private final Set<FunctionSignature> myStubs = new HashSet<FunctionSignature>();
		private final Set<FunctionSignature> myFreshStubs = new HashSet<FunctionSignature>();

		private final Map<String, ATFAttribute> myAttributes = new HashMap<String, ATFAttribute>();
		private final Map<VariableDefinition, ATFAttribute> myTextAttributes = new HashMap<VariableDefinition, ATFAttribute>();
		private final Map<VariableDefinition, ATFAttribute> myDefaultAttributes = new HashMap<VariableDefinition, ATFAttribute>();
		
		private Map<String, FunctionSignature> myCurrentScope = Collections.emptyMap();
		private String myCurrentSymbol;
		private String myCurrentFunction;

		public FunctionSignature getLocalFunction(String name) {
			if (myCurrentScope != myFunctionScope) {
				throw new IllegalStateException("Called outside function scope");
			}
			FunctionSignature functionSignature = myCurrentScope.get(name);
			return functionSignature;
		}
		
		public Set<FunctionSignature> getAllLocalFunctions() {
			if (myCurrentScope != myFunctionScope) {
				throw new IllegalStateException("Called outside function scope");
			}
			return myStubs;
		}

		public FunctionSignature getFunctionFromModule(String moduleName, String name) {
			Map<String, FunctionSignature> module = myModules.get(moduleName);
			if (module == null) {
				reportError("%s.%s: No such module: %s", myCurrentSymbol, myCurrentFunction, moduleName);
				todoAppend();
			}
			FunctionSignature functionSignature = module.get(name);
			if (functionSignature == null) {
				reportError("%s.%s: No such function in module %s: %s", myCurrentSymbol, myCurrentFunction, moduleName, name);
				todoAppend();
			}
			return functionSignature;
		}
		
		public void registerModule(SemanticModule module) {
			String moduleName = module.getName();
			if (myModules.containsKey(moduleName)) {
				reportError("Module name already used: %s", moduleName);
				todoAppend();
			}
			HashMap<String, FunctionSignature> moduleMap = new HashMap<String, FunctionSignature>();
			for (FunctionSignature functionSignature : module.getFunctions()) {
				moduleMap.put(functionSignature.getName(), functionSignature);
			}
			myModules.put(moduleName, moduleMap);
		}
		
		public void enterSymbolScope(String name) {
			myCurrentSymbol = name;
			myCurrentScope = mySymbolScope;
		}
		
		public void leaveSymbolScope() {
			mySymbolScope.clear();
			myCurrentScope = Collections.emptyMap();
			myCurrentSymbol = "";
		}
		
		public void enterFunctionScope(String name) {
			myCurrentFunction = name;
			myFunctionScope.putAll(mySymbolScope);
			myCurrentScope = myFunctionScope;
		}
		
		public void leaveFunctionScope() {
			myFunctionScope.clear();
			myCurrentScope = mySymbolScope;
			myStubScope.clear();
			myStubs.clear();
			myAttributes.clear();
			myCurrentFunction = "";
			myDefaultAttributes.clear();
			myTextAttributes.clear();
		}
		
		public void registerFunction(FunctionSignature functionSignature) {
			String name = functionSignature.getName();
			if (myCurrentScope.containsKey(name)) {
				reportError("%s.%s: Function name redefined: %s", myCurrentSymbol, myCurrentFunction, name);
				todoAppend();
			}
			myCurrentScope.put(name, functionSignature);
			
			// If the function is defined locally, we add it to stubs
			if (myCurrentScope == myFunctionScope) {
				myStubs.add(functionSignature);
			}
		}
		
		public boolean isStub(FunctionSignature functionSignature) {
			return myStubs.contains(functionSignature);
		}
		
		public boolean isFreshStub(FunctionSignature functionSignature) {
			return myFreshStubs.contains(functionSignature);
		}
		
		public FunctionSignature getFunctionStub(String name) {
			if (myCurrentScope != myFunctionScope) {
				throw new IllegalStateException("Must be inside a function to define function stubs");
			}
			FunctionSignature functionSignature = myStubScope.get(name);
			if (functionSignature == null) {
				functionSignature = AtfFactory.eINSTANCE.createFunctionSignature();
				functionSignature.setName(name);
				myFreshStubs.add(functionSignature);
				myStubs.add(functionSignature);
				myStubScope.put(name, functionSignature);
			} 
			return functionSignature;
		}
		
		public void suggestInputCount(FunctionSignature functionSignature, int inputCount) {
			if (!isFreshStub(functionSignature)) {
				throw new IllegalStateException("Must be called only on fresh stubs");
			}
			createAttributes(functionSignature.getInputAttributes(), inputCount);
			myFreshStubs.remove(functionSignature);
		}
		
		public void suggestOutputCount(FunctionSignature functionSignature, int outputCount) {
			List<ATFAttribute> outputAttributes = functionSignature.getOutputAttributes();
			if (!outputAttributes.isEmpty()) {
				throw new IllegalStateException("Must be called for function with no output yet");
			}
			createAttributes(outputAttributes, outputCount);
		}
		
		public void suggestAttributeName(ATFAttribute attribute, String name) {
			if (attribute.getName() == null) {
				attribute.setName(name);
			}
		}

		private void createAttributes(List<ATFAttribute> dest, int count) {
			for (int i = 0; i < count; i++) {
				ATFAttribute attribute = AtfFactory.eINSTANCE.createATFAttribute();
				dest.add(attribute);
			}
		}
		
		public String getCurrentFunction() {
			return myCurrentFunction;
		}
		
		public String getCurrentSymbol() {
			return myCurrentSymbol;
		}
		
		public void registerAttribute(ATFAttribute attribute) {
			if (myCurrentScope != myFunctionScope) {
				throw new IllegalStateException("Attribute defined outside a function");
			}
			String name = attribute.getName();
			ATFAttribute stored = myAttributes.get(name);
			if (stored == attribute) {
				return;
			}
			if (stored == null) {
				myAttributes.put(name, attribute);
				return;
			}
			reportError("%s.%s: Attrbute %s already defined", myCurrentSymbol, myCurrentFunction, name);
		}
		
		public void registerAttributes(Collection<ATFAttribute> attributes) {
			for (ATFAttribute attribute : attributes) {
				registerAttribute(attribute);
			}
		}
		
		public ATFAttribute getAttribute(String name) {
			if (myCurrentScope != myFunctionScope) {
				throw new IllegalStateException("Attribute requested outside a function");
			}
			ATFAttribute attribute = myAttributes.get(name);
			if (attribute == null) {
				attribute = AtfFactory.eINSTANCE.createATFAttribute();
				attribute.setName(name);
				myAttributes.put(name, attribute);
			}
			return attribute;
		}

		public ATFAttribute getTextAttribute(VariableDefinition variable) {
			ATFAttribute attribute = myTextAttributes.get(variable);
			if (attribute != null) {
				return attribute;
			}
			attribute = AtfFactory.eINSTANCE.createATFAttribute();
			myTextAttributes.put(variable, attribute);
			attribute.setType(myTypeSystemBuilder.getStringType());
			attribute.setName("#text(" + variable.getName() + ")");

			Attribute assignTextTo = AspectDefinitionUtils.getAttribute(myCurrentAssignment, 
					variable, getCurrentNamespace(), ATFMetadata.ASSIGN_TEXT_TO_ATTRIBUTE);
			ATFAttributeReference ref = AtfFactory.eINSTANCE.createATFAttributeReference();
			ref.setAttribute(attribute);
			addCrossReferencedValue(assignTextTo, ref);
			return attribute;
		}
		
		public ATFAttribute getDefaultAttribute(VariableDefinition variable) {
			ATFAttribute attribute = myDefaultAttributes.get(variable);
			if (attribute != null) {
				return attribute;
			}
			assertSymbolVariable(variable);
			attribute = AtfFactory.eINSTANCE.createATFAttribute();
			myDefaultAttributes.put(variable, attribute);
			attribute.setName(variable.getName() + "#");
			
			
			writeSymbolMapping(variable, 
					Collections.singletonList(wrapIntoReference(attribute)), 
					DEFAULT_FUNCTION, Collections.<ATFExpression>emptyList());
			return attribute;
		}
	}

}
