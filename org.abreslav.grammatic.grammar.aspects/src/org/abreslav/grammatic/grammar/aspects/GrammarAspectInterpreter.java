package org.abreslav.grammatic.grammar.aspects;

import java.util.List;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AbstractAssignmentInterpreter;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AssignmentHandler;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.query.variables.AlternativePartValue;
import org.abreslav.grammatic.query.variables.ExpressionValue;
import org.abreslav.grammatic.query.variables.ItemValue;
import org.abreslav.grammatic.query.variables.ListValue;
import org.abreslav.grammatic.query.variables.ProductionValue;
import org.abreslav.grammatic.query.variables.RulePartValue;
import org.abreslav.grammatic.query.variables.SequencePartValue;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.abreslav.grammatic.utils.INull;

public class GrammarAspectInterpreter {

	private interface IValueUnwrapper<T> {
		T unwrap(Value value);
	}
	
	private static final IValueUnwrapper<Expression> EXPRESSION_UNWRAPPER = new IValueUnwrapper<Expression>() {

		@Override
		public Expression unwrap(Value value) {
			return getExpression(value);
		}
		
	};
	
	private static final IValueUnwrapper<Production> PRODUCTION_UNWRAPPER = new IValueUnwrapper<Production>() {
		
		@Override
		public Production unwrap(Value value) {
			return getProduction(value);
		}
		
	};	
	
	private static final AssignmentHandler ASSIGNMENT_HANDLER = new AssignmentHandler() {
		
		@Override
		public INull caseExpressionValue(
				ExpressionValue object,
				List<Attribute> data) {
			processItemValue(object, data, 
					ExpressionAspectFunctions.INSTANCE, 
					EXPRESSION_UNWRAPPER);
			return INull.NULL;
		}

		@Override
		public INull caseProductionValue(ProductionValue object,
				List<Attribute> data) {
			processItemValue(object, data, 
					ProductionAspectFunctions.INSTANCE, 
					PRODUCTION_UNWRAPPER);
			return INull.NULL;
		}
		
		@Override
		public INull caseSequencePartValue(SequencePartValue object,
				List<Attribute> data) {
			processListValue(object, data, 
					SequencePartAspectFunctions.INSTANCE, 
					EXPRESSION_UNWRAPPER);
			return INull.NULL;
		}
		
		@Override
		public INull caseAlternativePartValue(AlternativePartValue object,
				List<Attribute> data) {
			processListValue(object, data, 
					AlternativePartAspectFunctions.INSTANCE, 
					EXPRESSION_UNWRAPPER);
			return INull.NULL;
		}

		@Override
		public INull caseRulePartValue(RulePartValue object,
				List<Attribute> data) {
			processListValue(object, data, 
					RulePartAspectFunctions.INSTANCE, 
					PRODUCTION_UNWRAPPER);
			return INull.NULL;
		}
		
	};
	
	public static void applyGrammarAspect(Grammar grammar, AspectDefinition aspectDefinition) {
		AbstractAssignmentInterpreter.runDefinition(
				aspectDefinition, grammar, 
				IMetadataProvider.EMPTY,
				ASSIGNMENT_HANDLER, IErrorHandler.EXCEPTION);
	}
	
	private static <T> void processItemValue(ItemValue<T> object,
			List<Attribute> data, IGrammarAspectFunctions<T, T> functions,
			IValueUnwrapper<T> unwrapper) {
		for (Attribute attribute : data) {
			apply(functions, 
					attribute.getName(), object.getItem(), 
					unwrapper, attribute.getValue());
		}
	}
	
	private static <T> void processListValue(ListValue<T> object,
			List<Attribute> data, 
			IGrammarAspectFunctions<List<T>, T> functions,
			IValueUnwrapper<T> unwrapper) {
		List<T> items = object.getItems();
		if (items.isEmpty()) {
			throw new IllegalArgumentException("Cannot process empty sequence part"); 
		}
		for (Attribute attribute : data) {
			apply(functions,
					attribute.getName(), items, 
					unwrapper, attribute.getValue());
		}
	}
	
	private static <S, R> void apply(
			IGrammarAspectFunctions<S, R> functions, 
			String name, S subject, 
			IValueUnwrapper<R> unwrapper, Value value) {
		R replacement = unwrapper.unwrap(value);
		if ("instead".equals(name)) {
			functions.instead(subject, replacement);
		} else if ("before".equals(name)) {
			functions.before(subject, replacement);
		} else if ("after".equals(name)) {
			functions.after(subject, replacement);
		} else if ("remove".equals(name)) {
			functions.remove(subject);
		} else {
			throw new IllegalArgumentException("Unsupported attribute: " + name);
		}
	}

	private static Expression getExpression(Value value) {
		if (value == null) {
			return null;
		}
		if (value instanceof org.abreslav.grammatic.grammar.ExpressionValue) {
			org.abreslav.grammatic.grammar.ExpressionValue expressionValue = (org.abreslav.grammatic.grammar.ExpressionValue) value;
			return EMFProxyUtil.copy(expressionValue.getExpression());
		}
		throw new IllegalArgumentException("Expression value expected, but found: " + value);
	}
	
	private static Production getProduction(Value value) {
		Expression expression = getExpression(value);
		if (expression == null) {
			return null;
		}
		Production production = GrammarFactory.eINSTANCE.createProduction();
		production.setExpression(expression);
		return production ;
	}

}