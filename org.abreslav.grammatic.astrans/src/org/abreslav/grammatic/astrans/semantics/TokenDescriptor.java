package org.abreslav.grammatic.astrans.semantics;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class TokenDescriptor {

	private static final String TOKEN = "token";
	private static final String FRAGMENT = "fragment";
	private static final String WHITESPACE = "whitespace";

	public static void write(Symbol symbol, TokenDescriptor descriptor, IWritableAspect writableAspect) {
		writableAspect.setAttribute(symbol, SemanticalMetadata.SEMANTICAL_NAMESPACE, TOKEN, null);
		if (descriptor.isFragment()) {
			writableAspect.setAttribute(symbol, SemanticalMetadata.SEMANTICAL_NAMESPACE, FRAGMENT, null);
		}
		if (descriptor.isWhitespace()) {
			writableAspect.setAttribute(symbol, SemanticalMetadata.SEMANTICAL_NAMESPACE, WHITESPACE, null);
		}
	}
	
	public static TokenDescriptor read(IMetadataStorage metadata) {
		if (!metadata.isPresent(TOKEN)) {
			return null;
		}
		return new TokenDescriptor(metadata.isPresent(FRAGMENT), metadata.isPresent(WHITESPACE));
	}
	
	private final boolean myFragment;
	private final boolean myWhitespace;

	private TokenDescriptor(boolean fragment, boolean whitespace) {
		myFragment = fragment;
		myWhitespace = whitespace;
	}

	public boolean isFragment() {
		return myFragment;
	}

	public boolean isWhitespace() {
		return myWhitespace;
	}
	
	
}
