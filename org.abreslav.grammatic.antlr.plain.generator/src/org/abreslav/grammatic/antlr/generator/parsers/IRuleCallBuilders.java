/**
 * 
 */
package org.abreslav.grammatic.antlr.generator.parsers;

import org.abreslav.grammatic.antlr.generator.antlr.Argument;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValueReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.ResultArgument;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;

public interface IRuleCallBuilders {
	public interface ICallBuilder {
		
		RuleCall createResult();
		
		void name(String id);
		
	}
	
	public interface IArgumentsBuilder {
		
		void argument(Argument argument);
		
		void firstArgument(Argument argument);
		
	}
	
	public interface IParameterReferenceOrBooleanArgumentBuilder {
		
		Argument _true();
		
		Argument _false();
		
		Argument parameterReference(String id);
		
	}
	
	public interface IVariableReferenceArgumentBuilder {
		
		AssignableValueReferenceArgument variableReference(String id);
		
	}
	
	public interface IResultArgumentBuilder {
		
		ResultArgument result();
		
	}
	
	ICallBuilder getCallBuilder();
	IArgumentsBuilder getArgumentsBuilder(RuleCall ruleCall);
	IParameterReferenceOrBooleanArgumentBuilder getParameterReferenceOrBooleanArgumentBuilder();
	IVariableReferenceArgumentBuilder getVariableReferenceArgumentBuilder();
	IResultArgumentBuilder getResultArgumentBuilder();
}