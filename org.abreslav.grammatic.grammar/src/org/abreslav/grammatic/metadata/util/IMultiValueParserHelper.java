package org.abreslav.grammatic.metadata.util;

import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.Value;

public interface IMultiValueParserHelper {

	Value getCurrentToken();

	void next();

	void back();

	<V extends Value> V match(Class<V> clazz);

	boolean checkTokenType(Class<? extends Value> clazz);

	boolean checkPunctuationToken(PunctuationType type);

	boolean checkPunctuationTokens(PunctuationType first,
			PunctuationType... rest);

	void match(PunctuationType type);
	void matchKeyword(String keyword);
	boolean checkKeyword(String keyword);

	void reportError(String message);

}