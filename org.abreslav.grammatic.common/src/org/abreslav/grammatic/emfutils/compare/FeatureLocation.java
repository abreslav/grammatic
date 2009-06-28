package org.abreslav.grammatic.emfutils.compare;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

final class FeatureLocation implements IDifferenceLocation {
	private final EStructuralFeature feature;

	FeatureLocation(final EStructuralFeature feature) {
		super();
		this.feature = feature;
	}
	
	@Override
	public String toString() {
		return "Value of " + ((EClass) feature.eContainer()).getName() + "::" + feature.getName() + " differs";
	}
}