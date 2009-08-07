package org.abreslav.grammatic.atf.types.checker;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.abreslav.grammatic.atf.ISyntacticFunctionHandler;
import org.abreslav.grammatic.atf.Statement;
import org.abreslav.grammatic.atf.SyntacticFunctionTraverser;
import org.abreslav.grammatic.atf.interpreter.ExpressionTraverser;
import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.ErrorType;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.TypeVariable;
import org.abreslav.grammatic.atf.types.unification.WarningType;
import org.abreslav.grammatic.atf.types.unification.impl.ConstraintSystem;
import org.abreslav.grammatic.atf.types.unification.impl.ConstraintSystemSolver;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystemClosedConstraintSolver;
import org.abreslav.grammatic.atf.util.AtfSwitch;
import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.common.util.EList;

public class ATFTypeChecker<T> implements ISyntacticFunctionHandler<Symbol> {

	public static final <T> void checkAndInferTypes(
			Collection<Grammar> grammars,
			IMetadataProvider metadataProvider,
			ITypeSystem<T> typeSystem,
			final IErrorHandler<? extends RuntimeException> errorHandler) {
		for (Grammar grammar : grammars) {
			for (Symbol symbol : grammar.getSymbols()) {
				SyntacticFunctionTraverser.processSyntacticFunctions(symbol, metadataProvider, 
						new ATFTypeChecker<T>(typeSystem, errorHandler), symbol);
			}
		}
	}
	
	private String myErrorPrefix;
	private IMetadataProvider myMetadataProvider;
	private final IErrorHandler<? extends RuntimeException> myErrorHandler;
	private final ConstraintSystem<T, RuntimeException> myConstraintSystem;
	private final Map<ATFAttribute, TypeVariable> myTypeVariables = new HashMap<ATFAttribute, TypeVariable>();
	private final Map<TypeVariable, ATFAttribute> myAttributeToVariable = new HashMap<TypeVariable, ATFAttribute>();
	private final ITypeSystem<T> myTypeSystem;
	private final ITypeErrorHandler<RuntimeException> myTypeErrorHandler = new ITypeErrorHandler<RuntimeException>() {

		@Override
		public void reportError(ErrorType errorType, Object... typeValues)
				throws RuntimeException {
			String message = errorType.getErrorMessage(typeValues);
			myErrorHandler.reportError("%s: " + message, myErrorPrefix);
		}

		@Override
		public void reportWarning(WarningType warningType, Object... typeValues) {
			String message = warningType.getErrorMessage(typeValues);
			myErrorHandler.reportError("%s [warning]:" + message, myErrorPrefix);
		}
		
	};
	
	private final AtfSwitch<INull> myStatementSwitch = new AtfSwitch<INull>() {

		@Override
		public INull caseATFAttributeAssignment(
				ATFAttributeAssignment object) {
			List<ATFAttributeReference> leftSide = object.getLeftSide();
			ATFExpression rightSide = object.getRightSide();
			processATFAttributeAssignment(leftSide, rightSide);
			return INull.NULL;
		}

		@Override
		public INull caseCollectionAppending(CollectionAppending object) {
			ATFAttributeReference leftSide = object.getLeftSide();
			ATFAttribute attribute = leftSide.getAttribute();
			@SuppressWarnings("unchecked")
			T type = (T) attribute.getType();
			if (type == null || !myTypeSystem.isCollection(type)) {
				myErrorHandler.reportError("%s: Attribute '%s' must be declared to be a collection", myErrorPrefix, 
						attribute.getName());
			}
			T elementType = myTypeSystem.getCollectionElementType(type);
			ATFExpression rightSide = object.getRightSide();
			processATFExpression(rightSide, elementType);
			return INull.NULL;
		}

		@Override
		public INull caseFunctionCall(FunctionCall object) {
			FunctionSignature function = object.getFunction();
			List<ATFExpression> arguments = object.getArguments();
			Iterator<ATFExpression> iterator = arguments.iterator();
			for (ATFAttribute attribute : function.getInputAttributes()) {
				processATFExpression(attribute, iterator.next());
			}
			return INull.NULL;
		}

		@Override
		public INull caseBlock(Block object) {
			for (Statement statement : object.getStatements()) {
				doSwitch(statement);
			}
			return INull.NULL;
		}
		
	};
	
	private final GrammarSwitch<INull> myExpressionSwitch = new ExpressionTraverser() {
		@Override
		public INull caseExpression(Expression object) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, myMetadataProvider);
			// expression.before
			readAndProcessStatement(metadata, ATFMetadata.BEFORE);
			// expression.after
			readAndProcessStatement(metadata, ATFMetadata.AFTER);
			return INull.NULL;
		}
		
		@Override
		public INull caseSymbolReference(SymbolReference object) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, myMetadataProvider);
			if (metadata.isPresent(ATFMetadata.ASSOCIATED_WITH_TOKEN)) {
				return null; // fall through
			}
			List<ATFAttributeReference> leftSide = metadata.readEObjects(ATFMetadata.ASSIGNED_TO_ATTRIBUTES);
			FunctionSignature function = (FunctionSignature) metadata.readEObject(ATFMetadata.ASSOCIATED_FUNCTION);
			List<ATFExpression> arguments = metadata.readEObjects(ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
			
			FunctionCall call = AtfFactory.eINSTANCE.createFunctionCall();
			if (arguments != null) {
				call.getArguments().addAll(arguments);
			}
			call.setFunction(function);
			
			if (leftSide != null) {
				processATFAttributeAssignment(leftSide, call);
			}
			return null; // fall through
		}

		@Override
		public INull caseCombination(Combination object) {
			super.caseCombination(object);
			return null; // fall through
		};
		
		@Override
		public INull caseIteration(Iteration object) {
			super.caseIteration(object);
			return null; // fall through
		};
	};

	public ATFTypeChecker(ITypeSystem<T> typeSystem, IErrorHandler<? extends RuntimeException> errorHandler) {
		myErrorHandler = errorHandler;
		myTypeSystem = typeSystem;
		myConstraintSystem = new ConstraintSystem<T, RuntimeException>(myTypeSystem.getSubtypingRelation(), myTypeErrorHandler);
	}

	public void handleSyntacticFunction(
			IMetadataProvider metadataProvider, Symbol symbol) {
		myMetadataProvider = metadataProvider;
		processFunction(metadataProvider, symbol);
		
		FiniteTypeSystemClosedConstraintSolver<T> closedConstraintSolver = new FiniteTypeSystemClosedConstraintSolver<T>(myTypeSystem);
		Map<TypeVariable, T> solution = ConstraintSystemSolver.solve(myConstraintSystem, closedConstraintSolver, myTypeErrorHandler);
		
		for (Map.Entry<TypeVariable, T> entry : solution.entrySet()) {
			TypeVariable typeVariable = entry.getKey();
			T type = entry.getValue();
			ATFAttribute attribute = myAttributeToVariable.get(typeVariable);
			attribute.setType(type);
		}
	}

	private void processFunction(IMetadataProvider metadataProvider, Symbol symbol) {
		IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, metadataProvider);
		FunctionSignature function = (FunctionSignature) symbolMetadata.readEObject(ATFMetadata.SYNTACTIC_FUNCTION);
		myErrorPrefix = symbol.getName() + "." + function.getName();
		readAndProcessStatement(symbolMetadata, ATFMetadata.BEFORE);
		readAndProcessStatement(symbolMetadata, ATFMetadata.AFTER);
		
		for (Production production : symbol.getProductions()) {
			IMetadataStorage productionMetadata = MetadataStorage.getMetadataStorage(production, metadataProvider);
			readAndProcessStatement(productionMetadata, ATFMetadata.BEFORE);
			// production.after
			readAndProcessStatement(productionMetadata, ATFMetadata.AFTER);
			
			Expression expression = production.getExpression();
			processExpressionMetadata(expression, metadataProvider);
		}
	}

	private void processExpressionMetadata(Expression expression, IMetadataProvider metadataProvider) {
		myExpressionSwitch.doSwitch(expression);
	}

	private void processATFExpression(ATFAttribute attribute,
			ATFExpression expression) {
		ATFAttributeReference reference = AtfFactory.eINSTANCE.createATFAttributeReference();
		reference.setAttribute(attribute);
		processATFAttributeAssignment(Collections.singletonList(reference), expression);
	}

	private void processATFExpression(ATFExpression expression,
			T expectedType) {
		if (expression instanceof ATFAttributeReference) {
			ATFAttributeReference ref = (ATFAttributeReference) expression;
			ATFAttribute attribute = ref.getAttribute();
			processAssignmentWithKnownTypeToTheLeft(expectedType, attribute);
		} else {
			FunctionCall call = (FunctionCall) expression;
			processATFStatement(call);
			FunctionSignature function = call.getFunction();
			List<ATFAttribute> inputAttributes = function.getInputAttributes();
			int outputSize = inputAttributes.size();
			if (outputSize != 1) {
				myErrorHandler.reportError("%s: function '%s' is expected to return one attribute, returned %d", myErrorPrefix, 
						function.getName(), outputSize);
			}
			processAssignmentWithKnownTypeToTheLeft(expectedType, inputAttributes.get(0));
		}
	}

	private void processATFAttributeAssignment(
			List<ATFAttributeReference> leftSide, ATFExpression rightSide) {
		int leftSize = leftSide.size();
		if (rightSide instanceof FunctionCall) {
			FunctionCall call = (FunctionCall) rightSide;
			processATFStatement(call);
			FunctionSignature function = call.getFunction();
			EList<ATFAttribute> outputAttributes = function.getOutputAttributes();
			int rightSize = outputAttributes.size();
			if (leftSize != rightSize) {
				myErrorHandler.reportError("%s: Tuple length on left side does not match signature of '%s': expected %d, found %d", myErrorPrefix, 
						function.getName(), rightSize, leftSize);
				throw new IllegalStateException();
			}
			Iterator<ATFAttribute> iterator = outputAttributes.iterator();
			for (ATFAttributeReference attributeReference : leftSide) {
				ATFAttribute outputAttribute = iterator.next();
				processAssignment(attributeReference.getAttribute(), outputAttribute);
			}
		} else {
			ATFAttributeReference ref = (ATFAttributeReference) rightSide;
			ATFAttribute attribute = ref.getAttribute();
			if (leftSize != 1) {
				myErrorHandler.reportError("%s: An attribute '%s' might be assigned only to a single attribute, not %d-tuple", myErrorPrefix, 
						attribute.getName(), leftSize);
			}
			processAssignment(leftSide.get(0).getAttribute(), attribute);
		}
	}

	private void processATFStatement(Statement statement) {
		if (statement == null) {
			return;
		}
		myStatementSwitch.doSwitch(statement);
	}

	private void processAssignment(ATFAttribute left, ATFAttribute right) {
		@SuppressWarnings("unchecked")
		T leftType = (T) left.getType();
		if (leftType == null) {
			@SuppressWarnings("unchecked")
			T rightType = (T) right.getType();
			if (rightType == null) {
				myConstraintSystem.addConstraint(getTypeVariable(right), getTypeVariable(left));
			} else {
				myConstraintSystem.addConstraint(rightType, getTypeVariable(left));
			}
		} else {
			processAssignmentWithKnownTypeToTheLeft(leftType, right);
		}
	}

	private void processAssignmentWithKnownTypeToTheLeft(T leftType,
			ATFAttribute right) {
		@SuppressWarnings("unchecked")
		T rightType = (T) right.getType();
		if (rightType == null) {
			myConstraintSystem.addConstraint(getTypeVariable(right), leftType);
		} else {
			myConstraintSystem.addConstraint(rightType, leftType);
		}
	}
	
	private TypeVariable getTypeVariable(ATFAttribute attribute) {
		TypeVariable typeVariable = myTypeVariables.get(attribute);
		if (typeVariable == null) {
			typeVariable = new TypeVariable(attribute.getName());
			myTypeVariables.put(attribute, typeVariable);
			myAttributeToVariable.put(typeVariable, attribute);
		}
		return typeVariable;
	}
	
	private static Statement readStatement(IMetadataStorage metadata, String name) {
		return (Statement) metadata.readEObject(name);
	}

	private void readAndProcessStatement(IMetadataStorage metadata, String name) {
		processATFStatement(readStatement(metadata, name));
	}
}
