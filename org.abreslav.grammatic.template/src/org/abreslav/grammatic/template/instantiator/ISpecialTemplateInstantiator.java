package org.abreslav.grammatic.template.instantiator;

import java.util.Collection;

import org.abreslav.grammatic.template.TemplateBody;

public interface ISpecialTemplateInstantiator {
	
	ISpecialTemplateInstantiator DEFAULT = new ISpecialTemplateInstantiator() {

		@Override
		public <T> boolean isSupported(TemplateBody<T> body) {
			return false;
		}

		@Override
		public <T> Collection<T> instantiate(TemplateBody<? extends T> body) {
			throw new UnsupportedOperationException();
		}
		
	};

	<T> boolean isSupported(TemplateBody<T> body);
	<T> Collection<T> instantiate(TemplateBody<? extends T> body);
}
