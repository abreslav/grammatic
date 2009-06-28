package org.abreslav.grammatic.emfutils;

import java.util.Collection;

import org.abreslav.grammatic.emfutils.EMFCopier.ICopyHandler;
import org.abreslav.grammatic.emfutils.compare.EMFComparator;
import org.abreslav.grammatic.parsingutils.resolve.IProxy;
import org.abreslav.grammatic.utils.CustomHashMap;
import org.abreslav.grammatic.utils.CustomHashSet;
import org.abreslav.grammatic.utils.CustomLinkedHashMap;
import org.abreslav.grammatic.utils.IHashingStrategy;
import org.eclipse.emf.ecore.EObject;

public class EMFProxyUtil {

	// Note! This hashing is dangerous for long-time storages 
	public static final IHashingStrategy UNRESOLVED_PROXY_TOLERANT_HASHING = new IHashingStrategy() {
		
		@Override
		public boolean equals(Object a, Object b) {
			Object inA = IProxy.Utils.unwrap(a);
			if (inA == null) {
				return a == b; 
			}
			Object inB = IProxy.Utils.unwrap(b);
			return inA.equals(inB);
		}
		
		@Override
		public int hashCode(Object o) {
			Object subject = IProxy.Utils.unwrap(o);
			if (subject == null) {
				return System.identityHashCode(o); 
			}
			return subject.hashCode();
		}
		
	};
	
	public static EObject removeProxies(EObject object) {
		return new ProxyRemover().removeProxies(object);
	}

	public static EObject copy(EObject eObject, ICopyHandler copyHandler) {
		if (eObject == null) {
			return null;
		}
		EMFCopier copier = new EMFCopier(UNRESOLVED_PROXY_TOLERANT_HASHING, copyHandler);
		EObject result = copier.copy(eObject);
		copier.copyReferences();
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T copy(T eObject) {
		return (T) copy(eObject, EMFCopier.DEFAULT_COPY_HANDLER);
	}
		
	public static <T extends EObject> Collection<T> copyAll(Collection<? extends T> eObjects, ICopyHandler copyHandler) {
		EMFCopier copier = new EMFCopier(UNRESOLVED_PROXY_TOLERANT_HASHING, copyHandler);
		return copier.copyAll(eObjects);
	}

	public static <T extends EObject> Collection<T> copyAll(Collection<? extends T> eObjects) {
		return copyAll(eObjects, EMFCopier.DEFAULT_COPY_HANDLER);
	}
	
	public static boolean equals(EObject a, EObject b) {
		return EMFComparator.compare(a, b, IProxy.HASHING_STRATEGY).areEqual();
	}
	
	public static <K, V> CustomHashMap<K, V> customHashMap() {
		return new CustomHashMap<K, V>(IProxy.HASHING_STRATEGY);
	}
	
	public static <K> CustomHashSet<K> customHashSet() {
		return new CustomHashSet<K>(IProxy.HASHING_STRATEGY);
	}
	
	public static <K, V> CustomLinkedHashMap<K, V> customLinkedHashMap() {
		return new CustomLinkedHashMap<K, V>(IProxy.HASHING_STRATEGY);
	}
}
