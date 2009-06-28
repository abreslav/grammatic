package org.abreslav.grammatic.grammar.template.parser;

import java.util.Collection;
import java.util.Collections;

import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.parsingutils.resolve.ResolvingDomain;
import org.abreslav.grammatic.template.TemplateBody;
import org.abreslav.grammatic.template.instantiator.ISpecialTemplateInstantiator;
import org.eclipse.emf.ecore.impl.EObjectImpl;

public class LazySymbolReference extends EObjectImpl implements TemplateBody<SymbolReference> {

	public static ISpecialTemplateInstantiator createInstantiator(final IKeyConvertor keyConvertor, final ResolvingDomain<IKey, Symbol> symbolDomain) {
		return new ISpecialTemplateInstantiator() {

			@Override
			public <T> Collection<T> instantiate(TemplateBody<? extends T> body) {
				IKey templateKey = ((LazySymbolReference) body).getKey();
				IKey realKey = keyConvertor.convert(templateKey);
				Symbol symbolStub = symbolDomain.getSubjectStub(realKey);
				SymbolReference ref = GrammarFactory.eINSTANCE.createSymbolReference();
				ref.setSymbol(symbolStub);
				@SuppressWarnings("unchecked")
				T result = (T) ref;
				return Collections.singleton(result);
			}

			@Override
			public <T> boolean isSupported(TemplateBody<T> body) {
				return body instanceof LazySymbolReference;
			}
		};	
	}
	
	private final IKey myKey;

	public LazySymbolReference(IKey key) {
		super();
		myKey = key;
	}

	public IKey getKey() {
		return myKey;
	}
}
