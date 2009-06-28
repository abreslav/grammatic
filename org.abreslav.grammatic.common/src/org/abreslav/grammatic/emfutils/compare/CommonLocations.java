/**
 * 
 */
package org.abreslav.grammatic.emfutils.compare;

enum CommonLocations implements IDifferenceLocation {
	VALUE("Objects differ"), 
	VALUES("The same object is mapped to two or more different objects"), 
	CLASS("Classes differ"),
	LIST_LENGTH("List lengths differ"),
	DIFFERENT_FEATURES("Features in feature map differ");
	
	private final String comment;

	private CommonLocations(final String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return comment;
	}
}