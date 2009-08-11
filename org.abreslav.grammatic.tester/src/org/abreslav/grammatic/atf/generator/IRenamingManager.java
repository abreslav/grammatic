package org.abreslav.grammatic.atf.generator;

import org.abreslav.grammatic.grammar.template.parser.INameResolver;

public interface IRenamingManager extends INameResolver {
	void addRenaming(String module, String newName, String oldName);
}
