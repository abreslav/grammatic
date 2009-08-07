package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.Collections;
import java.util.List;

import org.abreslav.grammatic.atf.java.antlr.semantics.CodeBlock;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;

public class StructureUtils {
	
	public static Grammar getGrammar(Symbol symbol) {
		return (Grammar) symbol.eContainer();
	}
	
	public static JavaStatement joinStatements(JavaStatement first, JavaStatement second) {
		if (first == null) {
			return second;
		}
		if (second == null) {
			return first;
		}
		if (first instanceof CodeBlock) {
			CodeBlock block = (CodeBlock) first;
			block.getStatements().addAll(contents(second));
			return block;
		}
		if (second instanceof CodeBlock) {
			CodeBlock block = (CodeBlock) second;
			block.getStatements().addAll(0, contents(first));
			return block;
		}
		CodeBlock block = SemanticsFactory.eINSTANCE.createCodeBlock();
		block.getStatements().add(first);
		block.getStatements().add(second);
		return block;
	}
	
	public static List<JavaStatement> contents(JavaStatement statement) {
		if (statement instanceof CodeBlock) {
			CodeBlock block = (CodeBlock) statement;
			return block.getStatements();
		}
		return Collections.singletonList(statement);
	}

}
