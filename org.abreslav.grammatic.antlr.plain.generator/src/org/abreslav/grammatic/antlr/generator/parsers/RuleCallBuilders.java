package org.abreslav.grammatic.antlr.generator.parsers;

import org.abreslav.grammatic.antlr.generator.antlr.AntlrFactory;
import org.abreslav.grammatic.antlr.generator.antlr.Argument;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValueReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.BooleanArgument;
import org.abreslav.grammatic.antlr.generator.antlr.ParameterReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.ResultArgument;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;

public class RuleCallBuilders implements IRuleCallBuilders {
	
	public interface IResolver {
		void assignRule(RuleCall call, String name);

		void assignParameterReference(ParameterReferenceArgument arg, String id);

		void assignVariableReference(AssignableValueReferenceArgument arg, String id);

		SyntacticalRule getContainingRule();
	}
	
	private final IResolver myResolver;
	
	public RuleCallBuilders(IResolver resolver) {
		myResolver = resolver;
	}

	@Override
	public IArgumentsBuilder getArgumentsBuilder(final RuleCall call) {
		return new IArgumentsBuilder() {

			@Override
			public void argument(Argument argument) {
				call.getArguments().add(argument);
				
			}

			@Override
			public void firstArgument(Argument argument) {
				call.getArguments().add(argument);
			}
			
		};
	}

	@Override
	public ICallBuilder getCallBuilder() {
		return new ICallBuilder() {
			private final RuleCall myRuleCall = AntlrFactory.eINSTANCE.createRuleCall();

			@Override
			public RuleCall createResult() {
				return myRuleCall;
			}

			@Override
			public void name(String id) {
				myResolver.assignRule(myRuleCall, id);
			}
			
		};
	}

	@Override
	public IParameterReferenceOrBooleanArgumentBuilder getParameterReferenceOrBooleanArgumentBuilder() {
		return new IParameterReferenceOrBooleanArgumentBuilder() {

			@Override
			public Argument _false() {
				return makeBoolean(false);
			}

			private Argument makeBoolean(boolean value) {
				BooleanArgument booleanArgument = AntlrFactory.eINSTANCE.createBooleanArgument();
				booleanArgument.setValue(value);
				return booleanArgument;
			}

			@Override
			public Argument _true() {
				return makeBoolean(true);
			}

			@Override
			public Argument parameterReference(String id) {
				ParameterReferenceArgument arg = AntlrFactory.eINSTANCE.createParameterReferenceArgument();
				myResolver.assignParameterReference(arg, id);
				return arg;
			}
			
		};
	}

	@Override
	public IResultArgumentBuilder getResultArgumentBuilder() {
		return new IResultArgumentBuilder() {

			@Override
			public ResultArgument result() {
				ResultArgument result = AntlrFactory.eINSTANCE.createResultArgument();
				result.setRule(myResolver.getContainingRule());
				return result;
			}
			
		};
	}

	@Override
	public IVariableReferenceArgumentBuilder getVariableReferenceArgumentBuilder() {
		return new IVariableReferenceArgumentBuilder() {

			@Override
			public AssignableValueReferenceArgument variableReference(String id) {
				AssignableValueReferenceArgument arg = AntlrFactory.eINSTANCE.createAssignableValueReferenceArgument();
				myResolver.assignVariableReference(arg, id);
				return arg;
			}
			
		};
	}

}
