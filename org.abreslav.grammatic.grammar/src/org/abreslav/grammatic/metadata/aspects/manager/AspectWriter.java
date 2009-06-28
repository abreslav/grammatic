package org.abreslav.grammatic.metadata.aspects.manager;

import org.abreslav.grammatic.metadata.aspects.AttributeAssignment;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.WritableAspect.IAspectWritingStrategy;

public final class AspectWriter implements IAspectWritingStrategy {
	public static IWritableAspect createWritableAspect(MetadataAspect aspect) {
		return new WritableAspect(new AspectWriter(aspect));
	}
	
	private final MetadataAspect myAspect;

	public AspectWriter(MetadataAspect aspect) {
		myAspect = aspect;
	}

	@Override
	public void addAssignment(AttributeAssignment assignment) {
		myAspect.getAssignments().add(assignment);
	}

	@Override
	public Iterable<AttributeAssignment> getInitialAssignments() {
		return myAspect.getAssignments();
	}
	
}