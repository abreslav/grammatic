package org.abreslav.grammatic.grammar.template.parser;

import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.template.grammarTemplate.CharacterRangeTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplateFactory;
import org.abreslav.grammatic.template.TemplateBody;

public class CharacterRangeUtils {
	public static TemplateBody<CharacterRange> createCharacterRange(char from, char to) {
		CharacterRangeTemplate range = GrammarTemplateFactory.eINSTANCE.createCharacterRangeTemplate();
		range.setLowerBound(from);
		range.setUpperBound(to);
		return range;
	}

	public static TemplateBody<CharacterRange> createCharacter(char c) {
		return createCharacterRange(c, c);
	}

}
