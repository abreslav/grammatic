package org.abreslav.grammatic.parser.util;


public class ListBuilder<I, L extends I> {
	
	public interface IListOperations<I, L> {
		void addItemToList(L list, I item);
		L createList();
	}
	
	private L myList;
	private I myItem;
	private final IListOperations<I, L> myListOperations;
	
	public ListBuilder(IListOperations<I, L> listOperations) {
		myListOperations = listOperations;
	}
	
	public void init() {
		myList = null;
		myItem = null;
	}
	
	public final I getResult() {
		return myList != null ? myList : myItem;
	}
	
	public final boolean isMany() {
		return myList != null;
	}

	public final L createList() {
		if (myList != null) {
			return myList;
		}
		myList = myListOperations.createList();
		if (myItem != null) {
			myListOperations.addItemToList(myList, myItem);
		}
		return myList;
	}
	
	public L getList() {
		return myList;
	}
	
	public final void item(I item) {
		if (myItem == null && myList == null) {
			myItem = item;
		} else {
			if (myList == null) {
				createList();
			}
			myListOperations.addItemToList(myList, item);
		}
	}


}
