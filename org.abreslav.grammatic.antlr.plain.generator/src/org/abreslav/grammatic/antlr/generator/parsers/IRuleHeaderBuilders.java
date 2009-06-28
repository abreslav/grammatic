/**
 * 
 */
package org.abreslav.grammatic.antlr.generator.parsers;

import java.util.Collection;

import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;

public interface IRuleHeaderBuilders {
	
	public interface IHeadersBuilder {
		
		void header(SyntacticalRule header);
		
		Collection<SyntacticalRule> getResult();
		
	}
	
	public interface IHeaderBuilder {
		
		void type(String type);
		
		void name(String id);
		
		void parameter(Parameter param);
		
		SyntacticalRule getResult();
		
	}
	
	public interface IParameterBuilder {
		
		Parameter parameter(String type, String id);
		
	}
	
	public interface ITypeBuilder {
		
		String string(String value);
		
		String type(String id, String generics);
		
	}
	
	public interface IGenericsBuilder {
		
		void generic(String id);
		
		String getResult();

		void firstGeneric(String id);
		
	}
	
	public interface IGenericBuilder {
		String question();
		String id(String id);
		String extends_(String type);
	}
	
	IHeaderBuilder getHeaderBuilder();
	IHeadersBuilder getHeadersBuilder();
	IParameterBuilder getParameterBuilder();
	ITypeBuilder getTypeBuilder();
	IGenericsBuilder getGenericsBuilder();
	IGenericBuilder getGenericBuilder();
}