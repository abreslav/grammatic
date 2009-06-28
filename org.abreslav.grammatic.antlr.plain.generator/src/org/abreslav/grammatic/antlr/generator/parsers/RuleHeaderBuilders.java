package org.abreslav.grammatic.antlr.generator.parsers;

import java.util.ArrayList;
import java.util.Collection;

import org.abreslav.grammatic.antlr.generator.antlr.AntlrFactory;
import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuildersFactory;
import org.abreslav.grammatic.antlr.generator.antlr.builders.VariableDefinition;

public class RuleHeaderBuilders implements IRuleHeaderBuilders {

	@Override
	public IGenericsBuilder getGenericsBuilder() {
		return new IGenericsBuilder() {
			private final StringBuilder myString = new StringBuilder("<");

			@Override
			public String getResult() {
				myString.append(">");
				return myString.toString();
			}

			@Override
			public void generic(String id) {
				myString.append(",").append(id);
			}

			@Override
			public void firstGeneric(String id) {
				myString.append(id);
			}
			
		};
	}

	@Override
	public IHeaderBuilder getHeaderBuilder() {
		return new IHeaderBuilder() {

			private final SyntacticalRule myRule = AntlrFactory.eINSTANCE.createSyntacticalRule();
			
			@Override
			public SyntacticalRule getResult() {
				return myRule;
			}

			@Override
			public void name(String id) {
				myRule.setName(id);
			}

			@Override
			public void parameter(Parameter param) {
				myRule.getParameters().add(param);
			}

			@Override
			public void type(String type) {
				VariableDefinition resultVar = BuildersFactory.eINSTANCE.createVariableDefinition();
				resultVar.setType(type);
				myRule.setResultVariable(resultVar);
			}
			
		};
	}

	@Override
	public IHeadersBuilder getHeadersBuilder() {
		return new IHeadersBuilder() {
			
			private Collection<SyntacticalRule> myResult = new ArrayList<SyntacticalRule>();

			@Override
			public Collection<SyntacticalRule> getResult() {
				return myResult ;
			}

			@Override
			public void header(SyntacticalRule header) {
				myResult.add(header);
			}
			
		};
	}

	@Override
	public IParameterBuilder getParameterBuilder() {
		return new IParameterBuilder() {

			@Override
			public Parameter parameter(String type, String id) {
				Parameter param = AntlrFactory.eINSTANCE.createParameter();
				param.setName(id);
				param.setType(type);
				return param;
			}
			
		};
	}

	@Override
	public ITypeBuilder getTypeBuilder() {
		return new ITypeBuilder() {

			@Override
			public String string(String value) {
				return value;
			}

			@Override
			public String type(String id, String generics) {
				return id + generics;
			}
			
		};
	}

	@Override
	public IGenericBuilder getGenericBuilder() {
		return new IGenericBuilder() {

			@Override
			public String id(String id) {
				return id;
			}

			@Override
			public String question() {
				return "?";
			}

			@Override
			public String extends_(String type) {
				return "? extends " + type;
			}
			
		};
	}

}
