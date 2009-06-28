package org.abreslav.grammatic.grammar.template.parser;

import java.util.Set;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.template.Template;
import org.abreslav.grammatic.template.instantiator.TemplateInstantiatorInterpreter;

public interface IParsingContext extends IModuleLoader {

	public interface IUnresolvedKeysHandler<K> {
		void handleUnresolvedKeys(Set<K> unresolvedKeys, String additionalText);
	}
	
	IUnresolvedKeysHandler<IKey> THROW_EXCEPTION = new IUnresolvedKeysHandler<IKey>() {

		@Override
		public void handleUnresolvedKeys(Set<IKey> unresolvedKeys,
				String additionalText) {
			throw new IllegalStateException("Unresolved keys (" + additionalText + "): " + unresolvedKeys);
		}
		
	};
	
	Symbol getSymbol(IKey name);
	void registerSymbol(String moduleName, Symbol symbol);
	Template<?> getTemplate(IKey name);
	void registerTemplate(String moduleName, Template<?> template);
	IWritableAspect getWritableAspectForTemplates();
	TemplateInstantiatorInterpreter getInstantiator(String moduleName);
	void handleUnresolvedKeys();
}
