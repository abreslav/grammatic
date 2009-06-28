package org.abreslav.grammatic.grammar.template.parser;

import java.util.List;

import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.template.grammarTemplate.AlternativeTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplateFactory;
import org.abreslav.grammatic.grammar.template.grammarTemplate.LexicalExpressionTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.StringExpressionTemplate;
import org.abreslav.grammatic.grammar.template.parser.SetComplementBuilder.Range;
import org.abreslav.grammatic.parser.util.ListBuilder;
import org.abreslav.grammatic.template.TemplateBody;

public class GrammaticLexicalGrammarBuilders implements IGrammaticLexicalGrammarBuilders {

	@Override
	public IBasicLexicalAtomBuilder getBasicLexicalAtomBuilder() {
		return new IBasicLexicalAtomBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				// Nothing;
			}
			
			@Override
			public TemplateBody<? extends Expression> character(char character) {
				return CharacterRangeUtils.createCharacter(character);
			}

			@Override
			public TemplateBody<? extends Expression> dot() {
				return CharacterRangeUtils.createCharacterRange(Character.MIN_VALUE, Character.MAX_VALUE);
			}

			@Override
			public TemplateBody<? extends Expression> range(TemplateBody<? extends Expression> range) {
				return range;
			}

			@Override
			public TemplateBody<? extends Expression> string(String string) {
				StringExpressionTemplate str = GrammarTemplateFactory.eINSTANCE.createStringExpressionTemplate();
				str.setValue(string.substring(1, string.length() - 1));
				return str;
			}			
		};
	}

	@Override
	public IRangeBuilder getRangeBuilder() {
		return new IRangeBuilder() {
			private final ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> myListBuilder = CombinationUtils.createAlternativeListBuilder();
			
			@Override
			public void init() {
				myListBuilder.init();
			}
			
			@Override
			public void release() {
				myListBuilder.init();
			}
			
			@Override
			public TemplateBody<? extends Expression> rangeComplement(
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
			public TemplateBody<? extends Expression> getResult() {
				TemplateBody<? extends Expression> result = myListBuilder.getResult();
				if (myListBuilder.isMany()) {
					return wrapIntoLexicalExpression(result);
				} else {
					return result;
				}
			}

			@Override
			public void rangeItem(TemplateBody<CharacterRange> rangeItem) {
				myListBuilder.item(rangeItem);
			}
			
		};
	}

	@Override
	public IRangeComplementBuilder getRangeComplementBuilder() {
		return new IRangeComplementBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public SetComplementBuilder createResult() {
				return new SetComplementBuilder();
			}
			
		};
	}

	@Override
	public IRangeComplementItemBuilder getRangeComplementItemBuilder() {
		return new IRangeComplementItemBuilder() {
			
			private SetComplementBuilder myBuilder;

			@Override
			public void init(SetComplementBuilder builder) {
				myBuilder = builder;
			}
			
			@Override
			public void release() {
				myBuilder = null;
			}

			@Override
			public void character(char character) {
				myBuilder.removeItem(character);
			}

			@Override
			public void rangeComplementItem(char character, char character1) {
				myBuilder.removeRange(character, character1);
			}
			
		};
	}

	@Override
	public IRangeItemBuilder getRangeItemBuilder() {
		return new IRangeItemBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public TemplateBody<CharacterRange> character(char character) {
				return CharacterRangeUtils.createCharacter(character);
			}

			@Override
			public TemplateBody<CharacterRange> rangeItem(char character, char character1) {
				return CharacterRangeUtils.createCharacterRange(character, character1);
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

