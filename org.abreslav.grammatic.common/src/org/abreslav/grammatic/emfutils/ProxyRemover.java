package org.abreslav.grammatic.emfutils;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.abreslav.grammatic.parsingutils.resolve.IProxy;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class ProxyRemover {

	private final Set<EObject> myProcessed = new HashSet<EObject>();
	
	public EObject removeProxies(EObject object) {
		EObject result = IProxy.Utils.unwrap(object);
		if (myProcessed.contains(result)) {
			return result;
		}
		myProcessed.add(result);

		EClass eClass = result.eClass();
		EList<EReference> allReferences = eClass.getEAllReferences();
		for (EReference reference : allReferences) {
			if (!result.eIsSet(reference) || !reference.isChangeable() || reference.isDerived()) {
				continue;
			}
			Object value = result.eGet(reference);
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				EList<EObject> values = (EList<EObject>) value;
				ListIterator<EObject> listIterator = values.listIterator();
				while (listIterator.hasNext()) {
					Object next = listIterator.next();
					EObject noProxies = removeProxies((EObject) next);
					if (noProxies != next) {
						listIterator.set(noProxies);
					}
				}
			} else {
				EObject noProxies = removeProxies((EObject) value);
				if (noProxies != value) {
					result.eSet(reference, noProxies);
				}
			}
		}
		
		return result;
	}


}
