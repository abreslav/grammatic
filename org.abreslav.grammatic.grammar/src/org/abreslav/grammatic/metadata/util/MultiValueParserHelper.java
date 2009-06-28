/**
 * 
 */
package org.abreslav.grammatic.metadata.util;

import java.util.EnumSet;

import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.Value;

public class MultiValueParserHelper implements IMultiValueParserHelper {

	private final IValueStream myValueStream;
	
	public MultiValueParserHelper(IValueStream valueStream) {
		myValueStream = valueStream;
	}

	public final Value getCurrentToken() {
		return myValueStream.getCurrent();
	}	
	
	public void next() {
		myValueStream.next();
	}
	
	public void back() {
		myValueStream.back();
	}
	
	public final <V extends Value> V match(Class<V> clazz) {
		if (!matchCurrentTokenType(clazz)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		V currentToken = (V) getCurrentToken();
		next();
		return currentToken;
	}

	private boolean matchCurrentTokenType(Class<? extends Value> clazz) {
		if (!checkTokenType(clazz)) {
			String name = (getCurrentToken() == null) ? "null" : getCurrentToken().getClass().getName();
			reportError(String.format("%s expected but %s (%s) found", clazz.getName(), name, getCurrentToken()));
			return false;
		}
		return true;
	}

	public final boolean checkTokenType(Class<? extends Value> clazz) {
		return clazz.isInstance(getCurrentToken());
	}
	
	public final boolean checkPunctuationToken(PunctuationType type) {
		if (!checkTokenType(PunctuationValue.class)) {
			return false;
		}
		return ((PunctuationValue) getCurrentToken()).getType() == type;
	}
	
	public final boolean checkPunctuationTokens(PunctuationType first, PunctuationType... rest) {
		if (!checkTokenType(PunctuationValue.class)) {
			return false;
		}
		return EnumSet.of(first, rest).contains(((PunctuationValue) getCurrentToken()).getType());
	}
	
	public final void match(PunctuationType type) {
		if (!matchCurrentTokenType(PunctuationValue.class)) {
			return;
		}
		PunctuationValue punctuationValue = (PunctuationValue) getCurrentToken();
		if (punctuationValue.getType() != type) {
			reportError(String.format("%s expected but %s found", type, punctuationValue.getType()));
			return;
		}
		next();
	}
	
	public void reportError(String message) {
		throw new IllegalArgumentException(message);
	}

	@Override
	public void matchKeyword(String keyword) {
		IdValue idValue = match(IdValue.class);
		if (!keyword.equals(idValue.getId())) {
			reportError(String.format("\"%s\" expected but \"%s\" found", keyword, idValue.getId()));
		}
	}
	
	@Override
	public boolean checkKeyword(String keyword) {
		if (!checkTokenType(IdValue.class)) {
			return false;
		}
		if (!keyword.equals(((IdValue) getCurrentToken()).getId())) {
			return false;
		}
		return true;
	}
	
}