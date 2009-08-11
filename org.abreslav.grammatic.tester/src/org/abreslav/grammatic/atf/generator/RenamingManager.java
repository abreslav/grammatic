package org.abreslav.grammatic.atf.generator;

import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.grammar.template.parser.DoubleKey;
import org.abreslav.grammatic.grammar.template.parser.IKey;
import org.abreslav.grammatic.utils.IErrorHandler;

public class RenamingManager implements IRenamingManager {

	private final Map<String, IKey> myRenamings = new HashMap<String, IKey>();
	private final IErrorHandler<? extends RuntimeException> myErrorHandler;


	public RenamingManager(
			IErrorHandler<? extends RuntimeException> errorHandler) {
		super();
		myErrorHandler = errorHandler;
	}

	@Override
	public IKey resolveName(String name) {
		return myRenamings.get(name);
	}

	@Override
	public void addRenaming(String module, String newName, String oldName) {
		if (newName == null) {
			newName = oldName;
		}
		if (myRenamings.containsKey(newName)) {
			myErrorHandler.reportError("Name used twice: %s", newName);
		}
		IKey key = DoubleKey.createKey(module, oldName);
		myRenamings.put(newName, key);
	}

}
