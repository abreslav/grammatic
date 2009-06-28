package org.abreslav.grammatic.antlr.generator.parsers;

import static org.abreslav.grammatic.metadata.PunctuationType.COMMA;
import static org.abreslav.grammatic.metadata.PunctuationType.DOLLAR;
import static org.abreslav.grammatic.metadata.PunctuationType.HASH;
import static org.abreslav.grammatic.metadata.PunctuationType.LEFT_BRACE;
import static org.abreslav.grammatic.metadata.PunctuationType.RIGHT_BRACE;

import org.abreslav.grammatic.antlr.generator.antlr.Argument;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValueReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.ResultArgument;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleCallBuilders.IArgumentsBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleCallBuilders.ICallBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleCallBuilders.IParameterReferenceOrBooleanArgumentBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleCallBuilders.IResultArgumentBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleCallBuilders.IVariableReferenceArgumentBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.RuleCallBuilders.IResolver;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.util.IMultiValueParserHelper;
import org.abreslav.grammatic.metadata.util.IValueStream;
import org.abreslav.grammatic.metadata.util.MultiValueParserHelper;

/*
 * RuleCall call : ruleName '(' arguments? ')' ;
 * void arguments[RuleCall] : argument (',' argument)* ;
 * Argument argument 
 *         : parameterReferenceArg
 *         || symbolReferenceArg
 *         || booleanArg
 *         || resultArg
 *         ;
 * ParameterReferenceArgument parameterReferenceArg : ID ;
 * SymbolReferenceArgument symbolReferenceArg : '$' ID ;
 * BooleanArgument booleanArg : 'true' | 'false' ;
 * ResultArgument resultArg : '#' 'result' ; 
 */
public class RuleCallParser {
	
	private final IMultiValueParserHelper myHepler;
	private final IRuleCallBuilders myBuilders;

	public RuleCallParser(IValueStream stream, IResolver resolver) {
		myHepler = new MultiValueParserHelper(stream);
		myBuilders = new RuleCallBuilders(resolver);
	}

	public RuleCall call() {
		ICallBuilder builder = myBuilders.getCallBuilder();
		RuleCall result = builder.createResult(); 
		IdValue idValue = myHepler.match(IdValue.class);
		builder.name(idValue.getId());
		myHepler.match(LEFT_BRACE);
		if (!myHepler.checkPunctuationToken(RIGHT_BRACE)) {
			arguments(result);
		}
		myHepler.match(RIGHT_BRACE);
		return result;
	}
	
	private void arguments(RuleCall ruleCall) {
		IArgumentsBuilder builder = myBuilders.getArgumentsBuilder(ruleCall);
		Argument argument = argument();
		builder.firstArgument(argument);
		while (myHepler.checkPunctuationToken(COMMA)) {
			myHepler.next();
			argument = argument();
			builder.argument(argument);
		}
	}
	
	private Argument argument() {
		if (myHepler.checkPunctuationToken(DOLLAR)) {
			return symbolReferenceArgument();
		}
		if (myHepler.checkPunctuationToken(HASH)) {
			return resultArgument();
		}
		return parameterReferenceOrBooleanArgument();
	}
	
	private Argument parameterReferenceOrBooleanArgument() {
		IParameterReferenceOrBooleanArgumentBuilder builder = myBuilders.getParameterReferenceOrBooleanArgumentBuilder();
		IdValue idValue = myHepler.match(IdValue.class);
		if ("true".equals(idValue.getId())) {
			return builder._true();
		} else if ("false".equals(idValue.getId())) {
			return builder._false();
		}
		return builder.parameterReference(idValue.getId());
	}
	
	private AssignableValueReferenceArgument symbolReferenceArgument() {
		IVariableReferenceArgumentBuilder builder = myBuilders.getVariableReferenceArgumentBuilder();
		myHepler.match(DOLLAR);
		IdValue idValue = myHepler.match(IdValue.class);
		return builder.variableReference(idValue.getId());
	}
	
	private ResultArgument resultArgument() {
		IResultArgumentBuilder builder = myBuilders.getResultArgumentBuilder();
		myHepler.match(HASH);
		IdValue idValue = myHepler.match(IdValue.class);
		if (!"result".equals(idValue.getId())) {
			throw new IllegalArgumentException();
		}
		return builder.result();
	}
}
