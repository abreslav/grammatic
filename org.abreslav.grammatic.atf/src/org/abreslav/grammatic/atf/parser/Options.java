package org.abreslav.grammatic.atf.parser;

import java.util.HashMap;
import java.util.Map;

public class Options implements IOptions {

	private final Map<String, String> myOptions = new HashMap<String, String>();
	
	@Override
	public void addOption(String name, String value) {
		myOptions.put(name, value);
	}

	
	@Override
	public Map<String, String> getOptions() {
		return myOptions;
	}

}
