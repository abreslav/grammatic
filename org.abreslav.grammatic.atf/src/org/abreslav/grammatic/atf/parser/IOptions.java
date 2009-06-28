package org.abreslav.grammatic.atf.parser;

import java.util.Map;

public interface IOptions {
	void addOption(String name, String value);

	Map<String, String> getOptions();
}
