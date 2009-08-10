package org.abreslav.grammatic.atf.java.antlr.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.abreslav.grammatic.atf.java.antlr.ANTLRAlternative;
import org.abreslav.grammatic.atf.java.antlr.ANTLRCharacterRange;
import org.abreslav.grammatic.atf.java.antlr.ANTLRCombination;
import org.abreslav.grammatic.atf.java.antlr.ANTLREmpty;
import org.abreslav.grammatic.atf.java.antlr.ANTLRExpression;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.ANTLRIteration;
import org.abreslav.grammatic.atf.java.antlr.ANTLRProduction;
import org.abreslav.grammatic.atf.java.antlr.ANTLRSequence;
import org.abreslav.grammatic.atf.java.antlr.LexicalLiteral;
import org.abreslav.grammatic.atf.java.antlr.LexicalRule;
import org.abreslav.grammatic.atf.java.antlr.Option;
import org.abreslav.grammatic.atf.java.antlr.Rule;
import org.abreslav.grammatic.atf.java.antlr.RuleCall;
import org.abreslav.grammatic.atf.java.antlr.SyntacticalRule;
import org.abreslav.grammatic.atf.java.antlr.semantics.CodeBlock;
import org.abreslav.grammatic.atf.java.antlr.semantics.GrammarExpressionReference;
import org.abreslav.grammatic.atf.java.antlr.semantics.ImplementationPoolField;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaAssignment;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaExpression;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.MethodCall;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ParserField;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableDefinition;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableReference;
import org.abreslav.grammatic.atf.java.antlr.semantics.util.SemanticsSwitch;
import org.abreslav.grammatic.atf.java.antlr.util.AntlrSwitch;
import org.abreslav.grammatic.parsingutils.ImportManager;
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

	public static void printGrammar(ANTLRGrammar grammar, Appendable out) throws IOException {
		StringBuilder buffer = new StringBuilder();
		ANTLRGrammarPrinter antlrGrammarPrinter = new ANTLRGrammarPrinter(buffer);
		antlrGrammarPrinter.printGrammar(grammar);
		printFileHeader(new Printer(out, "    "), grammar, antlrGrammarPrinter.myImportManager.getImports());
		out.append(buffer);
	}
	
	private static void printFileHeader(Printer printer, ANTLRGrammar grammar, List<String> imports) {
		String name = grammar.getName();
		printer.words("grammar", name + "").separator(";").endln();
		printer.endln();

		printer.blockStart("@header {");
		JavaHeaderPrinter.printHeader(printer, grammar.getPackage(), imports);
		printer.blockEnd("}").endln();
	}

	private final Comparator<ParserField> myInterfaceNameOrder = new Comparator<ParserField>() {

		@Override
		public int compare(ParserField o1, ParserField o2) {
			String type1 = getTypeName(o1.getField().getType());
			String type2 = getTypeName(o2.getField().getType());
			return type1.compareTo(type2);
		}
		
	};

	private final class ANTLRGrammarPrinterSwitch extends AntlrSwitch<INull> {

		@Override
		public INull caseANTLRGrammar(ANTLRGrammar object) {
			processGrammarMetadata(object);
			
			for (Rule rule : object.getRules()) {
				doSwitch(rule);
			}
			return INull.NULL;
		}

		private void processGrammarMetadata(ANTLRGrammar object) {
			printOptions(object);
			printJavaFileHeaders(object);
			printMembers(object);
		}

		private void printJavaFileHeaders(ANTLRGrammar object) {
			String pack = object.getPackage();
			if (pack != null) {
				myPrinter.blockStart("@lexer::header {");
				JavaHeaderPrinter.printHeader(myPrinter, pack, Collections.<String>emptyList());
				myPrinter.blockEnd("}").endln();
			}
		}

		private void printMembers(ANTLRGrammar grammar) {
			myPrinter.word("@members").blockStart("{").endl();
			
			List<ParserField> parserFields = new ArrayList<ParserField>(grammar.getPoolFields());
			parserFields.addAll(grammar.getModuleFields());
			Collections.sort(parserFields, myInterfaceNameOrder);
			
			for (ParserField parserField : parserFields) {
				Variable field = parserField.getField();
				String typeName = getTypeName(field.getType());
				myPrinter.words("private", typeName, field.getName()).separator(";").endl();
			}
			myPrinter.endln();
			
			myPrinter.words("public void setModuleImplementations").blockStart("(").endl().list(", ");
			for (ParserField parserField : parserFields) {
				Variable parameter = parserField.getConstructorParameter();
				if (parameter != null) {
					myPrinter.item().endl().word(getTypeName(parameter.getType())).print(parameter.getName());
				}
			}
			myPrinter.endList().endl().blockEnd(")").blockStart("{").endl();
			for (ImplementationPoolField poolField : grammar.getPoolFields()) {
				String fieldName = poolField.getField().getName();
				String poolsClassName = getTypeName(poolField.getProvider().getPoolsClass());
				String parameterName = poolField.getConstructorParameter().getName();
				myPrinter
					.print("this", ".").words(fieldName, "=", "new", poolsClassName).separator("(")
						.print(parameterName).separator(");").endl();
			}
			for (ModuleImplementationField moduleField : grammar.getModuleFields()) {
				Variable field = moduleField.getField();
				myPrinter.print("this", ".").words(field.getName(), "=");
				myStatementPrinter.doSwitch(moduleField.getInitExpression());
				myPrinter.separator(";").endl();
			}
			myPrinter.blockEnd("}").endl();
			
			StringTemplateGroup group = TemplateUtils.loadTemplateGroup("ParserMembers");
			StringTemplate membersTemplate = group.getInstanceOf("main");
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
		
		@Override
		public INull caseLexicalRule(LexicalRule object) {
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
		public INull caseSyntacticalRule(SyntacticalRule rule) {
			myPrinter
				.words(rule.getName());
			if (!rule.getParameters().isEmpty()) {
				printRuleParameters(rule);
			}
			Variable resultVariable = rule.getResultVariable();
			String type = getTypeName(resultVariable.getType());
			if (!JavaUtils.isVoid(type)) {
				myPrinter.word("returns").print("[").words(type, resultVariable.getName()).separator("]");
			}
			myPrinter.endl();
			printRuleLevelBlock("@init", rule.getBefore());
			printRuleLevelBlock("@after", rule.getAfter());
			myPrinter.blockStart()
					.word(":").list("| ", wrap(rule.getProductions()), ";")
				.blockEnd().endln();
			return INull.NULL;
		}
		
		private void printRuleLevelBlock(String blockName, JavaStatement statement) {
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
			for (Variable parameter : rule.getParameters()) {
				myPrinter.item().words(getTypeName(parameter.getType()), parameter.getName());
			}
			myPrinter.endList().separator("]").softSpace();
		}

		@Override
		public INull caseANTLRProduction(ANTLRProduction object) {
			printStatementBlock(object.getBefore());
			
			myPrinter.list("", wrapOne(object.getExpression()), "").endl();

			printStatementBlock(object.getAfter());
			return INull.NULL;
		}

		@Override
		public INull caseANTLREmpty(ANTLREmpty object) {
			myPrinter.word("/*empty*/");
			return INull.NULL;
		}

		private void printCharacter(int c) {
			myPrinter.print("'").print(CharacterUtils.getChararcterRepresentation(c)).word("'");
		}

		@Override
		public INull caseANTLRCharacterRange(final ANTLRCharacterRange object) {
			preExpression(object);
			printCharacter(object.getLowerBound());
			if (!ANTLRUtils.isSingleCharacter(object)) {
				myPrinter.separator("..");
				printCharacter(object.getUpperBound());
			}
			postExpression(object);
			return INull.NULL;
		}
		
		private void preExpression(ANTLRExpression expression) {
			printVariablePreamble(expression);
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
		public INull caseLexicalLiteral(LexicalLiteral object) {
			preExpression(object);
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
		public INull caseRuleCall(RuleCall object) {
			preExpression(object);
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

		private void printVariablePreamble(ANTLRExpression object) {
			Variable assignToVariable = object.getAssignToVariable();
			if (assignToVariable == null) {
				return;
			}
			String varName = assignToVariable.getName();
			if (varName != null) {
				myPrinter.print(varName).print("=");
			}
		}
		
		@Override
		public INull caseANTLRIteration(final ANTLRIteration object) {
			preExpression(object);
			myPrinter.print("(");
			doSwitch(object.getExpression());
			myPrinter.separator(")")
				.word(object.getType().getLiteral());
			postExpression(object);
			return INull.NULL;
		}

		@Override
		public INull caseANTLRSequence(final ANTLRSequence object) {
			preExpression(object);
			boolean hasVariable = object.getAssignToVariable() != null;
			if (hasVariable) {
				myPrinter.print("(");
			}
			EList<ANTLRExpression> expressions = object.getExpressions();
			for (ANTLRExpression expression : expressions) {
				doSwitch(expression);
			}
			if (hasVariable) {
				myPrinter.separator(")");
			}
			postExpression(object);
			return INull.NULL;
		}

		@Override
		public INull caseANTLRAlternative(final ANTLRAlternative object) {
			preExpression(object);
			printAlternativeCombination(object);
			postExpression(object);
			return INull.NULL;
		}

		private void printAlternativeCombination(final ANTLRCombination object) {
			myPrinter.print("(").list("| ");
			for (ANTLRExpression expression : object.getExpressions()) {
				myPrinter.item();
				doSwitch(expression);
			}
			myPrinter.endList().separator(")").softSpace();
		}

		@Override
		public INull defaultCase(final EObject object) {
			return myStatementPrinter.doSwitch(object);
		}
	}
	
	private final SemanticsSwitch<INull> myStatementPrinter = new SemanticsSwitch<INull>() {
		
		@Override
		public INull caseVariableReference(VariableReference object) {
			myPrinter.print(object.getVariable().getName());
			return INull.NULL;
		}
		
		@Override
		public INull caseGrammarExpressionReference(
				GrammarExpressionReference object) {
			String variableName = object.getExpression().getAssignToVariable().getName();
			if (isNotSyntacticalReference(object)) {
//				myPrinter.print("($", variableName, " == null ? null : $", variableName, ".getText())");
				myPrinter.print("$", variableName, ".getText()");
			} else {
				myPrinter.print(variableName);
			}
			return INull.NULL;
		}
		
		private boolean isNotSyntacticalReference(GrammarExpressionReference object) {
			return ourLexicalReferenceExpert.doSwitch(object.getExpression());
		}

		@Override
		public INull caseVariableDefinition(VariableDefinition object) {
			Variable variable = object.getVariable();
			myPrinter.words(getTypeName(variable.getType()), variable.getName());
			JavaExpression value = object.getValue();
			if (value != null) {
				myPrinter.word("=");
				doSwitch(value);
			}
			return INull.NULL;
		}
		
		@Override
		public INull caseJavaAssignment(JavaAssignment object) {
			Variable variable = object.getVariable();
			myPrinter.words(variable.getName(), "=");
			doSwitch(object.getValue());
			return INull.NULL;
		}
		
		@Override
		public INull caseMethodCall(MethodCall methodCall) {
			Variable variable = methodCall.getVariable();
			if (variable != null) {
				myPrinter.print(variable.getName(), ".");
			}
			String name = methodCall.getMethod().getName();
			myPrinter.print(name, "(").list(", ", wrap(methodCall.getArguments()), "").separator(")");
			return INull.NULL;
		}
		
		@Override
		public INull caseCodeBlock(CodeBlock object) {
			printStatementBlock(object);
			return INull.NULL;
		}
		
		@Override
		public INull defaultCase(EObject object) {
			throw new IllegalArgumentException(object.toString());
		}
	};

	private static final AntlrSwitch<Boolean> ourLexicalReferenceExpert = new AntlrSwitch<Boolean>() {
		@Override
		public Boolean caseRuleCall(RuleCall object) {
			return object.getRule() instanceof LexicalRule;
		}
		
//		@Override
//		public Boolean caseLexicalLiteral(LexicalLiteral object) {
//			return true;
//		}
//		
//		@Override
//		public Boolean caseANTLRCharacterRange(
//				ANTLRCharacterRange object) {
//			return true;
//		}
//		
//		@Override
//		public Boolean caseANTLRSequence(ANTLRSequence object) {
//			return true;
//		}
//		
		@Override
		public Boolean defaultCase(EObject object) {
			return true;
		}
	};
	private final Printer myPrinter;
	private final AntlrSwitch<INull> myPrinterSwitch = new ANTLRGrammarPrinterSwitch();
	private ImportManager myImportManager;

	private ANTLRGrammarPrinter(Appendable out) {
		myPrinter = new Printer(out, "    ");
	}

	private void printStatementBlock(JavaStatement statement) {
		if (statement == null) {
			return;
		}
		myPrinter.blockStart("{").endl();
		if (statement instanceof CodeBlock) {
			CodeBlock block = (CodeBlock) statement;
			for (JavaStatement st : block.getStatements()) {
				myStatementPrinter.doSwitch(st);
				if (false == st instanceof CodeBlock) {
					myPrinter.separator(";").endl();
				}
			}
		} else {
			myStatementPrinter.doSwitch(statement);
			myPrinter.separator(";").endl();
		}
		myPrinter.blockEnd("}").endl();
	}
	
	public void printGrammar(ANTLRGrammar grammar) throws IOException {
		try {
			myImportManager = new ImportManager(grammar.getPackage());
			myPrinterSwitch.doSwitch(grammar);
		} catch (IllegalStateException e) {
			throw (IOException) e.getCause();
		}
	}

	private <T extends EObject> Iterable<IPrintable> wrap(
			List<? extends T> objects) {
		return ListTransformer.transformList(objects, new ListTransformer.IElementTransformer<T, IPrintable>() {
			@Override
			public IPrintable transform(final T object) {
				return new IPrintable() {
					@Override
					public void print(Printer printer) {
						myPrinterSwitch.doSwitch(object);
					}
				};
			}
			
		});
	}
	
	private <T extends EObject> Iterable<IPrintable> wrapOne(T object) {
		return wrap(Collections.singletonList(object));
	}
	
	private String getTypeName(Type type) {
		ImportManager importManager = myImportManager;
		return TypeUtils.getTypeName(type, importManager);
	}
}
