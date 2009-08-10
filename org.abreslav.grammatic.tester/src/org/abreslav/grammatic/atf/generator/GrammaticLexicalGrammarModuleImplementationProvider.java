/**
 * 
 */
package org.abreslav.grammatic.atf.generator;

import java.util.List;

import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.template.grammarTemplate.AlternativeTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplateFactory;
import org.abreslav.grammatic.grammar.template.grammarTemplate.LexicalExpressionTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.StringExpressionTemplate;
import org.abreslav.grammatic.grammar.template.parser.CharacterRangeUtils;
import org.abreslav.grammatic.grammar.template.parser.CombinationUtils;
import org.abreslav.grammatic.grammar.template.parser.SetComplementBuilder;
import org.abreslav.grammatic.grammar.template.parser.SetComplementBuilder.Range;
import org.abreslav.grammatic.grammar1.IGrammaticLexicalGrammarModuleImplementationProvider;
import org.abreslav.grammatic.parser.util.ListBuilder;
import org.abreslav.grammatic.template.TemplateBody;

public class GrammaticLexicalGrammarModuleImplementationProvider
		implements IGrammaticLexicalGrammarModuleImplementationProvider {
	@Override
	public IBasicLexicalAtomFunctions getBasicLexicalAtomFunctions() {
		return new IBasicLexicalAtomFunctions() {
			
			@Override
			public TemplateBody<? extends Expression> createString(String string) {
				StringExpressionTemplate str = GrammarTemplateFactory.eINSTANCE.createStringExpressionTemplate();
				str.setValue(string);
				return str;
			}
			
			@Override
			public TemplateBody<? extends Expression> createFullRange() {
				return CharacterRangeUtils.createCharacterRange(Character.MIN_VALUE, Character.MAX_VALUE);
			}
			
			@Override
			public TemplateBody<? extends Expression> createCharacter(char character) {
				return CharacterRangeUtils.createCharacter(character);
			}
		};
	}

	@Override
	public IRangeComplementFunctions getRangeComplementFunctions() {
		return new IRangeComplementFunctions() {
			
			@Override
			public SetComplementBuilder createComplementBuilder() {
				return new SetComplementBuilder();
			}
		};
	}

	@Override
	public IRangeComplementItemFunctions getRangeComplementItemFunctions() {
		return new IRangeComplementItemFunctions() {
			
			@Override
			public void removeSubrange(SetComplementBuilder builder, char only,
					char only1) {
				builder.removeRange(only, only1);
			}
		};
	}

	@Override
	public IRangeFunctions getRangeFunctions() {
		return new IRangeFunctions() {

			@Override
			public TemplateBody<? extends Expression> createComplementTemplate(
					SetComplementBuilder builder) {
		        builder.removeEmptyRanges();            
	            List<Range> ranges = builder.getRanges();
		        if (ranges.size() == 1) {
		        	Range range = ranges.get(0);
		        	return CharacterRangeUtils.createCharacterRange((char) range.getStart(), (char) range.getEnd());
		        } else {
	                AlternativeTemplate alternative = GrammarTemplateFactory.eINSTANCE.createAlternativeTemplate();
					for (Range range : ranges) {
	                	char from = (char) range.getStart();
	                	char to = (char) range.getEnd();
						alternative.getExpressions().add(CharacterRangeUtils.createCharacterRange(from, to));
	                }
					return wrapIntoLexicalExpression(alternative);
		        }
			}

			@Override
			public void addRangeItem(
					ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> listBuilder,
					TemplateBody<CharacterRange> item) {
				listBuilder.item(item);
			}

			@Override
			public ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> createListBuilder() {
				return CombinationUtils.createAlternativeListBuilder();
			}

			@Override
			public TemplateBody<? extends Expression> getExpressionTemplate(
					ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> listBuilder) {
				TemplateBody<? extends Expression> result = listBuilder.getResult();
				if (listBuilder.isMany()) {
					return wrapIntoLexicalExpression(result);
				} else {
					return result;
				}
			}
			
		};
	}

	@Override
	public IRangeItemFunctions getRangeItemFunctions() {
		return new IRangeItemFunctions() {
			
			@Override
			public TemplateBody<CharacterRange> createCharacterRange(char only,
					char only1) {
				return CharacterRangeUtils.createCharacterRange(only, only1);
			}
		};
	}

	private TemplateBody<? extends Expression> wrapIntoLexicalExpression(
			TemplateBody<? extends Expression> alternative) {
		LexicalExpressionTemplate lexical = GrammarTemplateFactory.eINSTANCE.createLexicalExpressionTemplate();
		lexical.setExpression(alternative);
		return lexical;
	}

}