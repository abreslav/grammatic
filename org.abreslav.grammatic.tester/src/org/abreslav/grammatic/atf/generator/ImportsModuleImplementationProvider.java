package org.abreslav.grammatic.atf.generator;

import org.abreslav.grammatic.grammar.template.parser.IKey;
import org.abreslav.grammatic.grammar1.IImportsModuleImplementationProvider;

public class ImportsModuleImplementationProvider implements
		IImportsModuleImplementationProvider {
	
	@Override
	public IRenamingFunctions getRenamingFunctions() {
		return new IRenamingFunctions() {
			
			@Override
			public String null1() {
				return null;
			}
			
			@Override
			public void addRenaming(IRenamingManager manager, String moduleName,
					String old, String new1) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	public IKey resolveName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}