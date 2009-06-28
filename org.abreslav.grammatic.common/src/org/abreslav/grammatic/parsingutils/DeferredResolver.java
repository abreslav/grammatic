package org.abreslav.grammatic.parsingutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author abreslav
 *
 * @param <K> - key (to resolve by)
 * @param <S> - subject (to be resolved)
 * @param <R> - referrer (to be assigned resolved values)
 */
public abstract class DeferredResolver<K, S, R> {

	public interface IErrorHandler<K, R> {
		void reportError(K referenceKey, R referrer);
	}
	
	public static final IErrorHandler<Object, Object> THROW_EXCEPTION = new IErrorHandler<Object, Object>() {

		@Override public void reportError(Object referenceKey, Object referrer){
			throw new IllegalStateException(String.format("Not found: %s for %s", referenceKey.toString(), referrer.toString()));		
		}
		
	};
	
	public static final IErrorHandler<Object, Object> PRINT_MESSAGE = new IErrorHandler<Object, Object>() {

		@Override public void reportError(Object referenceKey, Object referrer){
			System.err.println("Unresolved: " + referenceKey.toString());		
		}
		
	};
	
	public interface ISubjectFinder<K, S> {
		S findSubject(K referenceKey);
	}
	
	public static class NullSubjectFinder<K, S> implements ISubjectFinder<K, S> {

		@Override
		public S findSubject(K referenceKey) {
			return null;
		}
		
	}
	
	private final Map<K, S> myKeyToSubject = new HashMap<K, S>();
	private final Map<R, K> myRefererToKey = new HashMap<R, K>();
	private final IErrorHandler<? super K, ? super R> myErrorHandler;
	private final ISubjectFinder<? super K, ? extends S> mySubjectFinder;
	private final Map<R, Collection<R>> myClones = new HashMap<R, Collection<R>>();
	
	public DeferredResolver(IErrorHandler<? super K, ? super R> errorHandler,
			ISubjectFinder<? super K, ? extends S> subjectFinder) {
		myErrorHandler = errorHandler;
		mySubjectFinder = subjectFinder;
	}

	
	public DeferredResolver(IErrorHandler<? super K, ? super R> errorHandler) {
		super();
		myErrorHandler = errorHandler;
		mySubjectFinder = new NullSubjectFinder<K, S>();
	}

	public void addSubject(K key, S subject) {
		myKeyToSubject.put(key, subject);
	}

	public void addReferrer(R referrer, K referenceKey) {
		myRefererToKey.put(referrer, referenceKey);
	}
	
	public void resolveAll() {
		for (Entry<R, K> entry : myRefererToKey.entrySet()) {
			R referrer = entry.getKey();
			K referenceKey = entry.getValue();
			S subject;
			if (myKeyToSubject.containsKey(referenceKey)) {
				subject = myKeyToSubject.get(referenceKey);
			} else {
				subject = findSubject(referenceKey);
				if (subject == null) {
					reportError(referenceKey, referrer);
					return;
				}
				myKeyToSubject.put(referenceKey, subject);
			}
			assignSubject(referrer, subject);
			Collection<R> clones = myClones.get(referrer);
			if (clones != null) {
				for (R clone : clones) {
					assignSubject(clone, subject);
				}
			}
		}
	}
	
	private S findSubject(K referenceKey) {
		return mySubjectFinder.findSubject(referenceKey);
	}

	public void addClone(R original, R clone) {
		Collection<R> clones = myClones.get(original);
		if (clones == null) {
			clones = new ArrayList<R>();
			myClones.put(original, clones);
		}
		clones.add(clone);
	}
	
	private void reportError(K referenceKey, R referrer) {
		myErrorHandler.reportError(referenceKey, referrer);
	}

	protected abstract void assignSubject(R referrer, S subject);
}
