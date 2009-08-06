package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.List;

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
	
}
