/**
 * 
 */
package org.abreslav.grammatic.astrans.semantics;

import org.eclipse.emf.ecore.EStructuralFeature;

public class SemanticalFeatureReference extends SemanticalReference {
	private final EStructuralFeature myFeature;
	private final boolean myOnlyIfUnset;
	private final boolean myAssertUnset;
	
	public SemanticalFeatureReference(SemanticalAttribute variable,
			EStructuralFeature feature, boolean onlyIfUnset, boolean assertUnset) {
		super(variable);
		myFeature = feature;
		myOnlyIfUnset = onlyIfUnset;
		myAssertUnset = assertUnset;
	}

	public final EStructuralFeature getFeature() {
		return myFeature;
	}

	public final boolean isOnlyIfUnset() {
		return myOnlyIfUnset;
	}

	public final boolean isAssertUnset() {
		return myAssertUnset;
	}
}