package org.abreslav.grammatic.query.interpreter.graphmatching;

public class Edge implements IEdge {

	private final int myLeft;
	private final int myRight;
	
	public Edge(int left, int right) {
		myLeft = left;
		myRight = right;
	}

	@Override
	public int getLeft() {
		return myLeft;
	}

	@Override
	public int getRight() {
		return myRight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + myLeft;
		result = prime * result + myRight;
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
		final Edge other = (Edge) obj;
		if (myLeft != other.myLeft)
			return false;
		if (myRight != other.myRight)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + myLeft + ", " + myRight + ")";
	}
}
