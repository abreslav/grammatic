package org.abreslav.grammatic.antlr.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.abreslav.grammatic.antlr.generator.antlr.ANTLRAlternative;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRCharacterRange;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLREmpty;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRExpression;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRGrammar;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRIteration;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRProduction;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRSequence;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValue;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValueReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.BooleanArgument;
import org.abreslav.grammatic.antlr.generator.antlr.Combination;
import org.abreslav.grammatic.antlr.generator.antlr.Import;
import org.abreslav.grammatic.antlr.generator.antlr.LexicalLiteral;
import org.abreslav.grammatic.antlr.generator.antlr.LexicalRule;
import org.abreslav.grammatic.antlr.generator.antlr.Option;
import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.ParameterReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.ResultArgument;
import org.abreslav.grammatic.antlr.generator.antlr.Rule;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;
import org.abreslav.grammatic.antlr.generator.antlr.TokenSwitch;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Assignment;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderPoolVariable;
import org.abreslav.grammatic.antlr.generator.antlr.builders.CodeBlock;
import org.abreslav.grammatic.antlr.generator.antlr.builders.MethodCall;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Statement;
import org.abreslav.grammatic.antlr.generator.antlr.builders.VariableDefinition;
import org.abreslav.grammatic.antlr.generator.antlr.builders.util.BuildersSwitch;
import org.abreslav.grammatic.antlr.generator.antlr.util.AntlrSwitch;
import org.abreslav.grammatic.antlr.generator.utils.TemplateUtils;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.abreslav.grammatic.utils.CharacterUtils;
import org.abreslav.grammatic.utils.INull;
import org.abreslav.grammatic.utils.ListTransformer;
import org.abreslav.grammatic.utils.printer.IPrintable;
import org.abreslav.grammatic.utils.printer.Printer;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class ANTLRGrammarPrinter {

	private static final Comparator<BuilderPoolVariable> FACTORY_INTERFACE_NAME_ORDER = new Comparator<BuilderPoolVariable>() {

		@Override
		public int compare(BuilderPoolVariable o1,
				BuilderPoolVariable o2) {
			String interfaceName1 = o1.getBuilderFactory().getFactoryInterfaceName();
			String interfaceName2 = o2.getBuilderFactory().getFactoryInterfaceName();
			return interfaceName1.compareTo(interfaceName2);
		}
		
	};

	private final class ANTLRGrammarPrinterSwitch extends AntlrSwitch<INull, Boolean> {

		@Override
		public INull caseANTLRGrammar(ANTLRGrammar object, Boolean inMethod) {
			processGrammarMetadata(object);
			
			for (Rule rule : object.getRules()) {
				doSwitch(rule, inMethod);
			}
			return INull.NULL;
		}

		private void processGrammarMetadata(ANTLRGrammar object) {
			printFileHeader(object);
			printOptions(object);
			printJavaFileHeaders(object);
			printMembers(object);
		}

		private void printJavaFileHeaders(ANTLRGrammar object) {
			String pack = object.getPackage();
			if (pack != null) {
				myPrinter.blockStart("@header {");
				JavaHeaderPrinter.printHeader(myPrinter, pack, object.getImports());
				myPrinter.blockEnd("}").endln();
				myPrinter.blockStart("@lexer::header {");
				JavaHeaderPrinter.printHeader(myPrinter, pack, Collections.<Import>emptyList());
				myPrinter.blockEnd("}").endln();
			}
		}

		private void printMembers(ANTLRGrammar grammar) {
			myPrinter.word("@members").blockStart("{").endl();
			
			StringTemplateGroup group = TemplateUtils.loadTemplateGroup("ParserMembers");
			StringTemplate membersTemplate = group.getInstanceOf("main");
			List<BuilderPoolVariable> poolVariables = new ArrayList<BuilderPoolVariable>(grammar.getPoolVariables());
			Collections.sort(poolVariables, FACTORY_INTERFACE_NAME_ORDER);
			membersTemplate.setAttribute("poolVars", poolVariables);
			myPrinter.print(membersTemplate.toString());

			myPrinter.blockEnd("}").endl();
			myPrinter.endln();
		}

		private void printOptions(ANTLRGrammar grammar) {
			List<Option> options = grammar.getOptions();
			if (options.isEmpty()) {
				return;
			}
			
			myPrinter.blockStart("options {");
			for (Option option : options) {
				myPrinter
				.words(option.getName(), "=", option.getValue())
				.separator(";").endl();
			}
			myPrinter.blockEnd("}").endln();
		}

		private void printFileHeader(ANTLRGrammar grammar) {
			String name = grammar.getName();
			myPrinter.words("grammar", name + "").separator(";").endln();
			myPrinter.endln();
		}
		
		@Override
		public INull caseLexicalRule(LexicalRule object, Boolean inMethod) {
			if (object.isFragment()) {
				myPrinter.word("fragment");
			}
			myPrinter.word(object.getName()).blockStart()
				.word(":");
			if (object.isWhitespace()) {
				myPrinter.word("{$channel = HIDDEN;}");
			}
			myPrinter.list("| ", wrap(object.getProductions()), ";")
				.blockEnd().endln();
			return INull.NULL;
		}

		@Override
		public INull caseSyntacticalRule(SyntacticalRule rule, Boolean inMethod) {
			myPrinter
				.words(rule.getName());
			if (!rule.getParameters().isEmpty()) {
				printRuleParameters(rule);
			}
			String type = rule.getResultVariable().getType();
			if (!JavaUtils.isVoid(type)) {
				myPrinter.word("returns").print("[").words(type, "result").separator("]");
			}
			myPrinter.endl();
			printRuleLevelBlock("@init", rule.getBuilderCreationStatement());
			printRuleLevelBlock("@after", rule.getBuilderReleaseStatement());
			myPrinter.blockStart()
					.word(":").list("| ", wrap(rule.getProductions()), ";")
				.blockEnd().endln();
			return INull.NULL;
		}
		
		private void printRuleLevelBlock(String blockName, Statement statement) {
			if (statement == null) {
				return;
			}
			myPrinter.blockStart();
			myPrinter.word(blockName);
			printStatementBlock(statement);
			myPrinter.blockEnd();
		}

		private void printRuleParameters(SyntacticalRule rule) {
			myPrinter.print("[").list(", ");
			for (Parameter parameter : rule.getParameters()) {
				myPrinter.item().words(parameter.getType(), parameter.getName());
			}
			myPrinter.endList().separator("]").softSpace();
		}

		@Override
		public INull caseANTLRProduction(ANTLRProduction object, Boolean inMethod) {
			printStatementBlock(object.getBefore());
			
			myPrinter.list("", wrapOne(object.getExpression()), "").endl();

			printStatementBlock(object.getAfter());
			return INull.NULL;
		}

		@Override
		public INull caseANTLREmpty(ANTLREmpty object, Boolean inMethod) {
			myPrinter.word("/*empty*/");
			return INull.NULL;
		}

		private void printCharacter(int c) {
			myPrinter.print("'").print(CharacterUtils.getChararcterRepresentation(c)).word("'");
		}

		@Override
		public INull caseANTLRCharacterRange(final ANTLRCharacterRange object, Boolean inMethod) {
			preExpression(object);
			printVariablePreamble(object);
			printCharacter(object.getLowerBound());
			if (!ANTLRUtils.isSingleCharacter(object)) {
				myPrinter.separator("..");
				printCharacter(object.getUpperBound());
			}
			postExpression(object);
			return INull.NULL;
		}
		
		private void preExpression(ANTLRExpression expression) {
			if (expression.getAfter() != null) {
//				myPrinter.print("(");
			}
			List<Option> options = expression.getOptions();
			if (!options.isEmpty()) {
				myPrinter.print("options").print("{");
				for (Option option : options) {
					myPrinter.print(option.getName()).print("=").print(option.getValue()).word(";");
				}
				myPrinter.separator("}").word(":");
			}
		}

		private void postExpression(ANTLRExpression expression) {
			printStatementBlock(expression.getAfter());
//			myPrinter.separator(")");
		}

		@Override
		public INull caseLexicalLiteral(LexicalLiteral object, Boolean inMethod) {
			preExpression(object);
			printVariablePreamble(object);
			String value = object.getValue();
			myPrinter.print("'");
			for (int i = 0; i < value.length(); i++) {
				myPrinter.print(CharacterUtils.getChararcterRepresentation(value.charAt(i)));
			}
			myPrinter.print("'").softSpace();
			postExpression(object);
			return INull.NULL;
		}
		
		@Override
		public INull caseRuleCall(RuleCall object, Boolean inMethod) {
			preExpression(object);
			printVariablePreamble(object);
			Rule rule = object.getRule();
			myPrinter.word(rule.getName());
			if (!object.getArguments().isEmpty()) {
				myPrinter
					.separator("[")
						.list(", ", wrap(object.getArguments()), "")
					.separator("]").softSpace();
			}
			postExpression(object);
			return INull.NULL;
		}

		private void printVariablePreamble(AssignableValue object) {
			String varName = object.getVariableName();
			if (varName != null) {
				myPrinter.print(varName).print("=");
			}
		}
		
		@Override
		public INull caseANTLRIteration(final ANTLRIteration object, Boolean inMethod) {
			preExpression(object);
			myPrinter.print("(");
			doSwitch(object.getExpression(), inMethod);
			myPrinter.separator(")")
				.word(object.getType().getLiteral());
			postExpression(object);
			return INull.NULL;
		}

		@Override
		public INull caseANTLRSequence(final ANTLRSequence object, Boolean inMethod) {
			preExpression(object);
			printVariablePreamble(object);
			boolean hasVariable = object.getVariableName() != null;
			if (hasVariable) {
				myPrinter.print("(");
			}
			EList<ANTLRExpression> expressions = object.getExpressions();
			for (ANTLRExpression expression : expressions) {
				doSwitch(expression, inMethod);
			}
			if (hasVariable) {
				myPrinter.separator(")");
			}
			postExpression(object);
			return INull.NULL;
		}

		@Override
		public INull caseANTLRAlternative(final ANTLRAlternative object, Boolean inMethod) {
			preExpression(object);
			printAlternativeCombination(object, null);
			postExpression(object);
			return INull.NULL;
		}

		private void printAlternativeCombination(final Combination object, String commonVarName) {
			myPrinter.print("(").list(" | ");
			for (ANTLRExpression expression : object.getExpressions()) {
				myPrinter.item().print("(");
				if (commonVarName != null) {
					myPrinter.print(commonVarName, "=");
				}
				doSwitch(expression, false);
				myPrinter.separator(")");
			}
			myPrinter.endList().separator(")").softSpace();
		}
		
		@Override
		public INull caseTokenSwitch(TokenSwitch object, Boolean inMethod) {
			preExpression(object);
			myPrinter.print("/* tokenSwitch */");
			printAlternativeCombination(object, object.getVariableName());
			postExpression(object);
			return INull.NULL;
		}

		@Override
		public INull caseBooleanArgument(BooleanArgument object, Boolean inMethod) {
			myPrinter.print(object.isValue() ? "true" : "false");
			return INull.NULL;
		}
		
		@Override
		public INull caseParameterReferenceArgument(
				ParameterReferenceArgument object, Boolean inMethod) {
			myPrinter.print(object.getParameter().getName());
			return INull.NULL;
		}
		
		@Override
		public INull caseResultArgument(ResultArgument object, Boolean inMethod) {
			myPrinter.print(object.getRule().getResultVariable().getName());
			return INull.NULL;
		}
		
		@Override
		public INull caseAssignableValueReferenceArgument(
				AssignableValueReferenceArgument object, Boolean inMethod) {
			if (inMethod && isLexicalReference(object)) {
				String variableName = object.getAssignableValue().getVariableName();
				myPrinter.print("($", variableName, " == null ? null : $", variableName, ".getText())");
			} else {
				myPrinter.print(object.getAssignableValue().getVariableName());
			}
			return INull.NULL;
		}
		
		private boolean isLexicalReference(AssignableValueReferenceArgument object) {
			return ourLexicalReferenceExpert.doSwitch(object.getAssignableValue(), null);
		}

		@Override
		public INull defaultCase(final EObject object, Boolean inMethod) {
			myPrinter.print("<").print(object.getClass().getName()).word(">");
			return INull.NULL;
		}
	}
	
	private final BuildersSwitch<INull, Void> myStatementPrinter = new BuildersSwitch<INull, Void>() {
		@Override
		public INull caseAssignment(Assignment object, Void data) {
			myPrinter.words(object.getVariable().getName(), "=");
			caseMethodCall(object.getMethodCall(), data);
			return INull.NULL;
		}
		
		@Override
		public INull caseMethodCall(MethodCall methodCall, Void data) {
			myPrinter.print(methodCall.getBuilderVariableName(), ".");
			String name = methodCall.getMethod().getName();
			myPrinter.print(name, "(").list(", ", wrap(methodCall.getArguments(), true), "").separator(");").endl();
			return INull.NULL;
		}
		
		@Override
		public INull caseVariableDefinition(VariableDefinition object, Void data) {
			myPrinter.words(object.getType(), object.getName());
			if (object.getMethodCall() != null) {
				myPrinter.word("=");
				caseMethodCall(object.getMethodCall(), data);
			} else {
				myPrinter.separator(";").endl();
			}
			return INull.NULL;
		}
		
		@Override
		public INull caseCodeBlock(CodeBlock object, Void data) {
			printStatementBlock(object);
			return INull.NULL;
		}
	};

	private static final AntlrSwitch<Boolean, Void> ourLexicalReferenceExpert = new AntlrSwitch<Boolean, Void>() {
		@Override
		public Boolean caseRuleCall(RuleCall object, Void data) {
			return object.getRule() instanceof LexicalRule;
		}
		
		@Override
		public Boolean caseLexicalLiteral(LexicalLiteral object,
				Void data) {
			return true;
		}
		
		@Override
		public Boolean caseANTLRCharacterRange(
				ANTLRCharacterRange object, Void data) {
			return true;
		}
		
		@Override
		public Boolean caseTokenSwitch(TokenSwitch object, Void data) {
			return true;
		}
		
		@Override
		public Boolean caseANTLRSequence(ANTLRSequence object, Void data) {
			return true;
		}
		
		@Override
		public Boolean defaultCase(EObject object, Void data) {
			return false;
		}
	};
	private final Printer myPrinter;
	private final AntlrSwitch<INull, Boolean> myPrinterSwitch = new ANTLRGrammarPrinterSwitch();

	public ANTLRGrammarPrinter(Appendable out) {
		myPrinter = new Printer(out, "    ");;
	}

	private void printStatementBlock(Statement statement) {
		if (statement == null) {
			return;
		}
		myPrinter.blockStart("{").endl();
		if (statement instanceof CodeBlock) {
			CodeBlock block = (CodeBlock) statement;
			for (Statement st : block.getStatements()) {
				myStatementPrinter.doSwitch(st, null);
			}
		} else {
			myStatementPrinter.doSwitch(statement, null);
		}
		myPrinter.blockEnd("}").endl();
	}
	
	public void printGrammar(ANTLRGrammar ANTLRGrammar) throws IOException {
		try {
			myPrinterSwitch.doSwitch(ANTLRGrammar, false);
		} catch (IllegalStateException e) {
			throw (IOException) e.getCause();
		}
	}

	private <T extends EObject> Iterable<IPrintable> wrap(
			List<? extends T> objects) {
		return wrap(objects, false);
	}
	private <T extends EObject> Iterable<IPrintable> wrap(
			List<? extends T> objects, final Boolean inMethod) {
		return ListTransformer.transformList(objects, new ListTransformer.IElementTransformer<T, IPrintable>() {
			@Override
			public IPrintable transform(final T object) {
				return new IPrintable() {
					@Override
					public void print(Printer printer) {
						myPrinterSwitch.doSwitch(object, inMethod);
					}
				};
			}
			
		});
	}
	
	private <T extends EObject> Iterable<IPrintable> wrapOne(T object) {
		return wrap(Collections.singletonList(object));
	}
}
