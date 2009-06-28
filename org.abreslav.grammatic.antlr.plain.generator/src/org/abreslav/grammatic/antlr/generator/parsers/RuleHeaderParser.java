package org.abreslav.grammatic.antlr.generator.parsers;

import static org.abreslav.grammatic.metadata.PunctuationType.COMMA;
import static org.abreslav.grammatic.metadata.PunctuationType.GREATER;
import static org.abreslav.grammatic.metadata.PunctuationType.LEFT_BRACE;
import static org.abreslav.grammatic.metadata.PunctuationType.LESS;
import static org.abreslav.grammatic.metadata.PunctuationType.QUESTION;
import static org.abreslav.grammatic.metadata.PunctuationType.RIGHT_BRACE;
import static org.abreslav.grammatic.metadata.PunctuationType.SEMICOLON;

import java.util.Collection;

import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleHeaderBuilders.IGenericBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleHeaderBuilders.IGenericsBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleHeaderBuilders.IHeaderBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleHeaderBuilders.IHeadersBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleHeaderBuilders.IParameterBuilder;
import org.abreslav.grammatic.antlr.generator.parsers.IRuleHeaderBuilders.ITypeBuilder;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.util.IMultiValueParserHelper;
import org.abreslav.grammatic.metadata.util.IValueStream;
import org.abreslav.grammatic.metadata.util.MultiValueParserHelper;
/*
 * Collection<Rule> headers : header+;
 * Rule header : type ID '(' param[result]* ')' ';'
 * Param param : type name
 * String type : STRING || ID generics?
 * String generics : '<' generic (',' generic)* '>'
 * String generic : ID | '?' ('extends' type)?
 */
public class RuleHeaderParser {
	private final IMultiValueParserHelper myHelper;
	private final IRuleHeaderBuilders myBuilders = new RuleHeaderBuilders(); 
	
	public RuleHeaderParser(IValueStream stream) {
		myHelper = new MultiValueParserHelper(stream);
	}

	public Collection<SyntacticalRule> headers() {
		IHeadersBuilder builder = myBuilders.getHeadersBuilder();
		do {
			SyntacticalRule header = header();
			builder.header(header);
		} while (myHelper.checkTokenType(IdValue.class)
				|| myHelper.checkTokenType(StringValue.class));
		return builder.getResult();
	}
	
	private SyntacticalRule header() {
		IHeaderBuilder builder = myBuilders.getHeaderBuilder();
		String type = type();
		builder.type(type);
		IdValue idValue = myHelper.match(IdValue.class);
		builder.name(idValue.getId());
		if (myHelper.checkPunctuationToken(LEFT_BRACE)) {
			myHelper.next();
			if (myHelper.checkTokenType(IdValue.class)
					|| myHelper.checkTokenType(StringValue.class)) {
				Parameter param = param();
				builder.parameter(param);
			}
			while (myHelper.checkPunctuationToken(COMMA)) {
				myHelper.next();
				Parameter param = param();
				builder.parameter(param);
			}
			myHelper.match(RIGHT_BRACE);
		}
		myHelper.match(SEMICOLON);
		return builder.getResult();
	}
	
	private Parameter param() {
		IParameterBuilder builder = myBuilders.getParameterBuilder();
		String type = type();
		IdValue idValue = myHelper.match(IdValue.class);
		return builder.parameter(type, idValue.getId());
	}
	
	private String type() {
		ITypeBuilder builder = myBuilders.getTypeBuilder();
		if (myHelper.checkTokenType(StringValue.class)) {
			StringValue stringValue = myHelper.match(StringValue.class);
			return builder.string(stringValue.getValue());
		}
		IdValue idValue = myHelper.match(IdValue.class);
		String generics = myHelper.checkPunctuationToken(LESS) ? generics() : "";
		return builder.type(idValue.getId(), generics);
	}
	
	private String generics() {
		IGenericsBuilder builder = myBuilders.getGenericsBuilder();
		myHelper.match(LESS);
		builder.firstGeneric(generic());
		while (myHelper.checkPunctuationToken(COMMA)) {
			myHelper.next();
			builder.generic(generic());
		}
		myHelper.match(GREATER);
		return builder.getResult(); 
	}
	
	private String generic() {
		IGenericBuilder builder = myBuilders.getGenericBuilder();
		if (myHelper.checkPunctuationToken(QUESTION)) {
			myHelper.next();
			if (myHelper.checkKeyword("extends")) {
				myHelper.next();
				return builder.extends_(type());
			} else {
				return builder.question();
			}
		} else {
			IdValue idValue = myHelper.match(IdValue.class);
			return builder.id(idValue.getId());
		}
	}
}
