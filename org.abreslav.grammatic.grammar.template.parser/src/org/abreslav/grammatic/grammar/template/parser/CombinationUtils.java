package org.abreslav.grammatic.grammar.template.parser;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.template.grammarTemplate.AlternativeTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplateFactory;
import org.abreslav.grammatic.grammar.template.grammarTemplate.SequenceTemplate;
import org.abreslav.grammatic.parser.util.ListBuilder;
import org.abreslav.grammatic.parser.util.ListBuilder.IListOperations;
import org.abreslav.grammatic.template.TemplateBody;

public class CombinationUtils {
	
	public static final ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> createAlternativeListBuilder() {
		return new ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate>(new IListOperations<TemplateBody<? extends Expression>, AlternativeTemplate>() {
		
			@Override
			public void addItemToList(AlternativeTemplate list,
					TemplateBody<? extends Expression> item) {
				list.getExpressions().add(item);
			}
			
			@Override
			public AlternativeTemplate createList() {
				return GrammarTemplateFactory.eINSTANCE.createAlternativeTemplate();
			}
		});
	}

	public static final ListBuilder<TemplateBody<? extends Expression>, SequenceTemplate> createSequenceListBuilder() {
		return new ListBuilder<TemplateBody<? extends Expression>, SequenceTemplate>(new IListOperations<TemplateBody<? extends Expression>, SequenceTemplate>() {

			@Override
			public void addItemToList(SequenceTemplate list,
					TemplateBody<? extends Expression> item) {
				list.getExpressions().add(item);
			}
	
			@Override
			public SequenceTemplate createList() {
				return GrammarTemplateFactory.eINSTANCE.createSequenceTemplate();
			}
		});
	}
	
}
