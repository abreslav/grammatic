package org.abreslav.grammatic.grammar.template.parser;

import org.abreslav.grammatic.emfutils.EObjectProxy;
import org.abreslav.grammatic.template.Template;
import org.abreslav.grammatic.template.TemplateBody;
import org.abreslav.grammatic.template.TemplateParameter;
import org.eclipse.emf.common.util.EList;

public class TemplateProxy<T> extends EObjectProxy<Template<T>> implements Template<T> {

	@Override
	public String getName() {
		return pGetDelegate().getName();
	}

	@Override
	public void setName(String value) {
		pGetDelegate().setName(value);
	}

	@Override
	public TemplateBody<T> getBody() {
		return pGetDelegate().getBody();
	}

	@Override
	public EList<TemplateParameter<?>> getParameters() {
		return pGetDelegate().getParameters();
	}

	@Override
	public void setBody(TemplateBody<T> value) {
		pGetDelegate().setBody(value);
	}
}
