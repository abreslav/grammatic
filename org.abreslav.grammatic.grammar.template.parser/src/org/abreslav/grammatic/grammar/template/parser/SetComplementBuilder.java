package org.abreslav.grammatic.grammar.template.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SetComplementBuilder {

	public static final class Range {
		private int myStart;
		private int myEnd;

		public Range(int start, int end) {
			super();
			myStart = start;
			myEnd = end;
		}

		public boolean contains(int item) {
			return (item >= myStart) && (item <= myEnd);
		}
		
		public boolean isEmpty() {
			return myStart > myEnd;
		}
		
		public int getStart() {
			return myStart;
		}
		
		public int getEnd() {
			return myEnd;
		}
		
		public void setStart(int start) {
			myStart = start;
		}
		
		public void setEnd(int end) {
			myEnd = end;
		}
		
		@Override
		public String toString() {
			return myStart + "-" + myEnd;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + myEnd;
			result = prime * result + myStart;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Range other = (Range) obj;
			if (myEnd != other.myEnd)
				return false;
			if (myStart != other.myStart)
				return false;
			return true;
		}
		
		
	}
	
	private final List<Range> myRanges = new LinkedList<Range>();
	
	public SetComplementBuilder() {
		myRanges.add(new Range(Character.MIN_VALUE, Character.MAX_VALUE));
	}
	
	private ListIterator<Range> containingRange(int item) {
		ListIterator<Range> i = myRanges.listIterator();
		while (i.hasNext()) {
			Range range = i.next();
			if (range.contains(item)) {
				break;
			}
		}
		return i;
	}
	
	private void subtract(ListIterator<Range> i, int start, int end) {
		if (start > end) {
			return;
		}
		
		Range range = i.next();
		
		if (end < range.getStart()) {
			return;
		}
		
		if (range.getEnd() < start) {
			return;
		}
		
		if (range.contains(start)) {
			if (range.contains(end)) {
				Range rightPart = new Range(end + 1, range.getEnd());
				range.setEnd(start - 1);
				if (!rightPart.isEmpty()) {
					i.add(rightPart);
				}
			} else {
				range.setEnd(start - 1);
			}
		} else {
			if (range.contains(end)) {
				range.setStart(end + 1);
			} else {
				i.remove();
			}
		}
	}
	
	public void removeItem(int item) {
		ListIterator<Range> rangeIndex = containingRange(item);
		if (!rangeIndex.hasPrevious()) {
			return;
		}
		
		rangeIndex.previous();

		subtract(rangeIndex, item, item);
	}
	
	public void removeRange(int start, int end) {
		if (start > end) {
			return;
		}
		if (start == end) {
			removeItem(start);
			return;
		}
		if (myRanges.isEmpty()) {
			return;
		}
		ListIterator<Range> i = myRanges.listIterator();
		while (i.hasNext()) {
			subtract(i, start, end);
		}
	}
	
	public void removeEmptyRanges() {
		for (Iterator<Range> i = myRanges.iterator(); i.hasNext(); ) {
			Range range = i.next();
			if (range.isEmpty()) {
				i.remove();
			}
		}
	}
	
	public List<Range> getRanges() {
		return myRanges;
	}
}
