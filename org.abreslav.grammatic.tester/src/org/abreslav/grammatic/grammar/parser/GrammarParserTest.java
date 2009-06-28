//package org.abreslav.grammatic.grammar.parser;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintStream;
//import java.util.Collection;
//import java.util.List;
//import java.util.Scanner;
//
//import org.abreslav.grammatic.grammar.Grammar;
//import org.abreslav.grammatic.grammar.template.parser.IGrammaticCharacterBuilders;
//import org.abreslav.grammatic.grammar.template.parser.IGrammaticLexicalGrammarBuilders;
//import org.abreslav.grammatic.grammar.template.parser.IGrammaticMetadataBuilders;
//import org.abreslav.grammatic.metadata.Namespace;
//import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
//import org.abreslav.grammatic.utils.printer.Printer;
//import org.antlr.runtime.ANTLRInputStream;
//import org.antlr.runtime.CommonTokenStream;
//import org.antlr.runtime.RecognitionException;
//import org.antlr.runtime.tree.CommonTree;
//import org.junit.Test;
//
//public class GrammarParserTest {
//	public static Grammar parseGrammar(InputStream input,
//			Collection<Namespace> namespaces, 
//			IWritableAspect writableAspect, Appendable log) throws IOException,
//			RecognitionException {
//		ANTLRInputStream in = new ANTLRInputStream(input);
//		GrammaticAnnotatedGrammarLexer lexer = new GrammaticAnnotatedGrammarLexer(
//				in);
//		GrammaticAnnotatedGrammarParser parser = new GrammaticAnnotatedGrammarParser(
//				new CommonTokenStream(lexer));
//
//		Printer printer = new Printer(log, "");
//		parser.setBuilders(
//				ProxyBuilders.getBuilders(IGrammaticMetadataBuilders.class, printer),
//				ProxyBuilders.getBuilders(IGrammaticCharacterBuilders.class, printer),
//				ProxyBuilders.getBuilders(IGrammaticAnnotatedGrammarBuilders.class, printer),
//				ProxyBuilders.getBuilders(IGrammaticLexicalGrammarBuilders.class, printer)
//		);
//
//		Grammar grammar = parser.annotatedGrammar();
////		grammarBuilders.resolveNames(namespaces);
////		namespaces.addAll(metadataBuilders.getNamespaces());
//		return grammar;
//	}
//
//	@SuppressWarnings("unused")
//	private static void printTree(CommonTree tree, Printer printer) {
//		if (tree == null) {
//			return;
//		}
//		
//		String text = tree.getText();
//		printer.endl().print("(").print(text + ":", tree.getLine() + "", ":", tree.getCharPositionInLine() + "").blockStart();
//		@SuppressWarnings("unchecked")
//		List children = tree.getChildren();
//		if (children != null) {
//			for (Object object : children) {
//				printTree((CommonTree) object, printer);
//			}
//		}
//		printer.blockEnd();
//	}
//
//	@Test
//	public void test() throws FileNotFoundException, IOException, RecognitionException {
//		String baseDir = "testData/grammarParser/structure/";
//		FileOutputStream fileOutputStream = new FileOutputStream(baseDir + "log");
//		parseGrammar(new FileInputStream(baseDir + "GrammaticAnnotatedGrammar.grammar"), null, null, new PrintStream(fileOutputStream));
//		fileOutputStream.close();
//		Scanner expectedLog = new Scanner(new FileInputStream(baseDir + "expected-log"));
//		Scanner log = new Scanner(new FileInputStream(baseDir + "log"));
//		int line = 1;
//		while (expectedLog.hasNextLine()) {
//			assertTrue("Log is shorter than expected", log.hasNextLine());
//			String expectedLine = expectedLog.nextLine();
//			String realLine = log.nextLine();
//			assertEquals(String.format("Line %d: expetcted '%s' but found '%s'", line, expectedLine, realLine), expectedLine, realLine);
//			line++;
//		}
//		assertFalse("Log is longer than expeced", log.hasNextLine());
//		log.close();
//		expectedLog.close();
//	}
//}
