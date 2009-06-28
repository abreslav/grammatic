package org.abreslav.grammatic.metadata.util;

import org.abreslav.grammatic.metadata.Value;

public interface IValueStream {
	void next();
	void back();
	Value getCurrent();
}
