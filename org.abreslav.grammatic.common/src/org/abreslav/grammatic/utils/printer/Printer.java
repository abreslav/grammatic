package org.abreslav.grammatic.utils.printer;

import java.io.IOException;
import java.util.ArrayList;

public class Printer {
	private final Appendable myOutput;
	private final StringBuilder myWhitespaceBuffer = new StringBuilder();
	private final StringBuilder myIndent = new StringBuilder();
	private String myIndentStep;
	private boolean myIsNewLine = false;
	private ArrayList<SeparatedList> myListStack = new ArrayList<SeparatedList>();

	public Printer(Appendable output, String indentStep) {
		myOutput = output;
		myIndentStep = indentStep;
	}
	
	public Printer print(CharSequence charSequence) {
		append(charSequence);
		return this;
	}
	
	public Printer print(CharSequence charSequence, CharSequence... others) {
		append(charSequence);
		for (CharSequence other : others) {
			append(other);
		}
		return this;
	}
	
	public Printer word(CharSequence charSequence) {
		append(charSequence);
		softSpace();
		return this;
	}

	public Printer softSpace() {
		myWhitespaceBuffer.append(" ");
		return this;
	}
	
	public Printer separator(CharSequence charSequence) {
		myWhitespaceBuffer.setLength(0);
		if (myIsNewLine) {
			setIndent();
		}
		append(charSequence);
		return this;
	}
	
	public Printer words(CharSequence... words) {
		for (CharSequence word : words) {
			word(word);
		}
		return this;
	}
	
	public Printer print(IPrintable p) {
		p.print(this);
		return this;
	}
	
	public Printer blockStart() {
		return blockStart("");
	}

	public Printer blockEnd() {
		return blockEnd("");
	}
	
	public Printer blockStart(CharSequence open) {
		print(open);
		pushIndent();
		return endl();
	}

	public Printer blockEnd(CharSequence close) {
		popIndent();
		return print(close).endl();
	}
	
	public Printer endl() {
		if (!myIsNewLine) {
			newline();
		}
		setIndent();
		return this;
	}

	public Printer endln() {
		newline();
		setIndent();
		return this;
	}
	
	public Printer list(CharSequence separator, Iterable<IPrintable> items, CharSequence closing) {
		boolean first = true;
		for (IPrintable printable : items) {
			if (!first) {
				print(separator);
			}
			first = false;
			print(printable);
		}
		print(closing);
		return this;
	}
	
	private void setIndent() {
		myWhitespaceBuffer.setLength(0);
		myWhitespaceBuffer.append(myIndent);
	}
	
	private void append(CharSequence charSequence) {
		if (charSequence.length() <= 0) {
			return;
		}
		myIsNewLine = false;
		outputAppend(myWhitespaceBuffer);
		myWhitespaceBuffer.setLength(0);
		outputAppend(charSequence);
	}
	
	private void popIndent() {
		myIndent.setLength(Math.max(0, myIndent.length() - myIndentStep.length()));
		setIndent();
	}

	private void newline() {
		myWhitespaceBuffer.setLength(0);
		outputAppend("\n");
		myIsNewLine = true;
	}
	
	private void pushIndent() {
		myIndent.append(myIndentStep);
		setIndent();
	};
	
	private void outputAppend(CharSequence charSequence) {
		try {
			myOutput.append(charSequence);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public Printer list(CharSequence separator) {
		myListStack.add(new SeparatedList(separator));
		return this;
	}
	
	public Printer item() {
		SeparatedList top = myListStack.get(myListStack.size() - 1);
		if (!top.isFirst()) {
			print(top.getSeparator());
		}
		top.setFirst(false);
		return this;
	}
	
	public Printer endList() {
		myListStack.remove(myListStack.size() - 1);
		return this;
	}
	
	private final static class SeparatedList {
		private final CharSequence mySeparator;
		private boolean myFirst = true;
		
		public SeparatedList(CharSequence separator) {
			mySeparator = separator;
		}
		
		public CharSequence getSeparator() {
			return mySeparator;
		}
		
		public boolean isFirst() {
			return myFirst;
		}
		
		public void setFirst(boolean first) {
			myFirst = first;
		}
	}
}