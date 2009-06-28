/**
 * 
 */
package org.abreslav.grammatic.emfutils.compare;


public class Difference {
	
	public static final Difference NO_DIFFERENCE = new Difference(true, null, null, null) {
		@Override
		public String toString() {
			return "No DIFFERENCE";				
		}
	};
	
	public static Difference getInstance(final boolean equals, final IDifferenceLocation location, final Object object1, final Object object2) {
		if (equals) {
			return NO_DIFFERENCE;
		}
		return new Difference(equals, location, object1, object2);
	}
	
	private final boolean equal;
	private final IDifferenceLocation location;
	private final Object object1;
	private final Object object2;
	
	private Difference(final boolean equals, final IDifferenceLocation location, final Object object1, final Object object2) {
		this.equal = equals;
		this.location = location;
		this.object1 = object1;
		this.object2 = object2;
	}
	
	public Object getObject1() {
		return object1;
	}

	public Object getObject2() {
		return object2;
	}

	public boolean areEqual() {
		return equal;
	}

	public IDifferenceLocation getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		return "Objects " + object1 + " and " + object2 + " are not same as: " + location;
	}
}