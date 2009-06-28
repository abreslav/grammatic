package org.abreslav.grammatic.atf.java.parser;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {
	private final List<T> myData = new ArrayList<T>();
	private T myTop;
	
	public T peek() {
		return myTop;
	}
	
	public T pop () {
		T remove = myData.remove(myData.size() - 1);
		if (myData.isEmpty()) {
			myTop = null;
		} else {
			myTop = myData.get(myData.size() - 1);
		}
		return remove; 
	}
	
	public void push(T item) {
		myData.add(item);
		myTop = item;
	}
	
}
