package org.abreslav.grammatic.grammar.template.parser;

public interface IKeyConvertor {

	IKeyConvertor DEFAULT  = new IKeyConvertor() {

		@Override
		public IKey convert(IKey key) {
			return key;
		}
		
	};
	
	IKey convert(IKey key);
}
