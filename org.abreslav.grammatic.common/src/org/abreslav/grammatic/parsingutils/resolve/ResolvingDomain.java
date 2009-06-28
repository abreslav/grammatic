package org.abreslav.grammatic.parsingutils.resolve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResolvingDomain<K, S> {

	public interface ISubjectStubFactory<K, S> {
		IProxy<S> getSubjectStub(K key); 
	}
	
	private final ISubjectStubFactory<K, S> myStubFactory;
	private final Map<K, IProxy<S>> myKeyToSubjectStub = new HashMap<K, IProxy<S>>();
	private final Map<K, S> myResolvedKeys = new HashMap<K, S>();

	public static <K, S> ResolvingDomain<K, S> create(ISubjectStubFactory<K, S> stubFactory) {
		return new ResolvingDomain<K, S>(stubFactory);
	}
	
	public static <K, S> ResolvingDomain<K, S> copy(ResolvingDomain<K, S> original) {
		return new ResolvingDomain<K, S>(original);
	}
	
	private ResolvingDomain(ISubjectStubFactory<K, S> stubFactory) {
		myStubFactory = stubFactory;
	}
	
	private ResolvingDomain(ResolvingDomain<K, S> original) {
		myStubFactory = original.myStubFactory;
		myKeyToSubjectStub.putAll(original.myKeyToSubjectStub);
		myResolvedKeys.putAll(original.myResolvedKeys);
	}

	@SuppressWarnings("unchecked")
	public S getSubjectStub(K key) {
		S subject = myResolvedKeys.get(key);
		if (subject != null) {
			return subject;
		}
		IProxy<S> stub = getStub(key);
		return (S) stub;
	}

	private IProxy<S> getStub(K key) {
		IProxy<S> stub = myKeyToSubjectStub.get(key);
		if (stub == null) {
			stub = myStubFactory.getSubjectStub(key); 
			myKeyToSubjectStub.put(key, stub);
		}
		return stub;
	}
	
	public void markKeyResolved(K key, S subject) {
		IProxy<S> stub = getStub(key);
		if (stub.pGetDelegate() != null && stub.pGetDelegate() != subject) {
			throw new IllegalArgumentException("Duplicate marking for key \"" + key + "\"");
		}
		stub.pSetDelegate(subject);
		myResolvedKeys.put(key, subject);
	}
	
	public Set<K> getUnresolvedKeys() {
		HashSet<K> result = new HashSet<K>(myKeyToSubjectStub.keySet());
		result.removeAll(myResolvedKeys.keySet());
		return result;
	}
	
}
