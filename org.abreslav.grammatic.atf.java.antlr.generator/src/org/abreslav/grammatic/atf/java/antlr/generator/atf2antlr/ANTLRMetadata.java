package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class ANTLRMetadata {
	public static final String FRAGMENT_TOKEN_CLASS = "fragment";
	public static final String FORCE_RETAIN_ATTRIBUTE = "forceRetain";
	public static final String WHITESPACE_TOKEN_CLASS = "whitespace";
	public static final String GRAMMAR_NAME = "grammarName";
	public static final String GRAMMAR_PACKAGE = "grammarPackage";

	public static final ISymbolInclusionStrategy USED_FORCED_FRONT_WHITESPACE = new ISymbolInclusionStrategy() {
		
		@Override
		public boolean doInclude(Symbol symbol, boolean isFront,
				IMetadataStorage metadata) {
			if (isFront || metadata.isPresent(ANTLRMetadata.FORCE_RETAIN_ATTRIBUTE)) {
				return true;
			}
			List<String> classes = metadata.readObjects(ATFMetadata.TOKEN_CLASSES);
			return classes != null && classes.contains(ANTLRMetadata.WHITESPACE_TOKEN_CLASS);
		}
	};

	private static final Set<String> ANTLR_KEYWORDS = new HashSet<String>();
	static {
		ANTLR_KEYWORDS.add("fragment");
		ANTLR_KEYWORDS.add("grammar");
		ANTLR_KEYWORDS.add("lexer");
		ANTLR_KEYWORDS.add("options");
		ANTLR_KEYWORDS.add("parser");
		ANTLR_KEYWORDS.add("returns");
		ANTLR_KEYWORDS.add("scope");
		ANTLR_KEYWORDS.add("tokens");
		ANTLR_KEYWORDS.add("tree");
	}
	
	public static Set<String> getAntlrKeywords() {
		return ANTLR_KEYWORDS;
	}
}
