package org.abreslav.grammatic.metadata.util;

import java.util.List;
import java.util.ListIterator;

import org.abreslav.grammatic.metadata.Value;

public class ValueStream implements IValueStream {
	private final List<? extends Value> myTokens;
	private ListIterator<? extends Value> myIterator;
	private Value myCurrentToken;

	public ValueStream(List<? extends Value> tokens) {
		myTokens = tokens;
		myIterator = myTokens.listIterator();
		next();
	}

	@Override
	public void back() {
		if (!myIterator.hasPrevious()) {
			throw new IllegalStateException();
		}
		myIterator.previous();
		if (!myIterator.hasPrevious()) {
			throw new IllegalStateException();
		}
		myCurrentToken = myIterator.previous();
		myIterator.next();
	}

	@Override
	public Value getCurrent() {
		return myCurrentToken;
	}

	@Override
	public void next() {
		if (myIterator.hasNext()) {
			myCurrentToken = myIterator.next();
		} else {
			myCurrentToken = null;
		}
	}
	
}