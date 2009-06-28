//
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//import org.abreslav.grammatic.antlr.ANTLRGrammarGenerator;
//import org.antlr.runtime.RecognitionException;
//
//public class GenerateGrammaticGrammarParser {
//
//	public static void main(String[] args) throws IOException, RecognitionException {
//		String targetProject = "../org.abreslav.grammatic.grammar.antlr.parser/";
//		String grammarDir = targetProject + "grammar/";
//		String generatedGrammarDir = targetProject + "generated-grammar/";
//		String targetPackageDir = targetProject + "generated/org/abreslav/grammatic/grammar/antlr/parser/";
//		
//		String grammarName = "GrammaticAnnotatedGrammar";
//		List<File> grammarPath = Collections.singletonList(new File(grammarDir));
//
//		ANTLRGrammarGenerator.generate(grammarName, grammarDir, generatedGrammarDir,
//				targetPackageDir, grammarPath);
//		
//		System.out.println("OK");
//	}
//
//}