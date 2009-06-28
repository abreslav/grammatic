package org.abreslav.grammatic.atf.types.unification.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.WarningType;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;
import org.abreslav.grammatic.atf.types.unification.graph.impl.DFSTools;
import org.abreslav.grammatic.atf.types.unification.graph.impl.Graph;
import org.abreslav.grammatic.atf.types.unification.graph.impl.ICycleHandler;
import org.abreslav.grammatic.atf.types.unification.graph.impl.IRepresentativeChooser;
import org.abreslav.grammatic.atf.types.unification.graph.impl.ISuccessorPolicy;
import org.abreslav.grammatic.atf.types.unification.graph.impl.StandardPolicies;
import org.abreslav.grammatic.utils.IHashingStrategy;

public class FiniteTypeSystem<T> implements ITypeSystem<T>, ISubtypingRelation<T> {

	private static final HashMap<?, ?> MAP_TO_ITSELF = new HashMap<Object, Object>() {
		@Override
		public Object get(Object key) {
			return key;
		}
	};
	
	private final IHashingStrategy myHashingStrategy; 
	private IGraph<T> myGraph;
	private Map<T, T> myTypeToRepresentative;
	private final IStringRepresentationProvider<? super T> myRepresentationProvider;
	private T myTopType;
	private List<T> myTopologicalOrder;
	private Map<T, Integer> myTopologicalIndices = new HashMap<T, Integer>();
	
	public <E extends Throwable> FiniteTypeSystem(
			Set<T> allTypes, 
			ISubtypingRelation<? super T> subtypingRelation, 
			IHashingStrategy hashingStrategy,
			IStringRepresentationProvider<? super T> stringRepresentationProvider, 
			ITypeErrorHandler<E> errorHandler) throws E {
		myRepresentationProvider = stringRepresentationProvider;
		myHashingStrategy = hashingStrategy;
		myGraph = new Graph<T>(myHashingStrategy);
		for (T a : allTypes) {
			myGraph.addVertex(a);
			for (T b : allTypes) {
				myGraph.addVertex(b);
				if (a != b) {
					if (subtypingRelation.isSubtypeOf(a, b)) {
						myGraph.addEdge(a, b);
					}
				}				
			}
		}
		prepareGraph(errorHandler, true);
	}

	public <E extends Throwable> FiniteTypeSystem(
			IGraph<T> graph, 
			IHashingStrategy hashingStrategy,
			IStringRepresentationProvider<? super T> stringRepresentationProvider, 
			ITypeErrorHandler<E> errorHandler) throws E {
		myHashingStrategy = hashingStrategy;
		myRepresentationProvider = stringRepresentationProvider;
		myGraph = graph;
		prepareGraph(errorHandler, false);
	}
	
	public T getTopType() {
		return myTopType;
	}
	
	public Set<T> getAllTypes() {
		return myGraph.getAllVertices();
	}
	
	// Called from constructors!
	private <E extends Throwable> void prepareGraph( 
			final ITypeErrorHandler<E> errorHandler, boolean alreadyTransitive) {
		
		// Called only once
		if (myTopologicalOrder != null) {
			return;
		}
		
		IGraph<T> condensation = new Graph<T>(myHashingStrategy);
		HashMap<T, T> typeToRepresentative = new HashMap<T, T>();
		
		List<T> topologicalOrder = DFSTools.topologicalSort(myGraph, getCycleHandler(errorHandler));
		List<Set<T>> components = new ArrayList<Set<T>>();
		boolean nontrivialCondensation = DFSTools.findConnectedComponents(myGraph, components, topologicalOrder);
		if (nontrivialCondensation) {
			topologicalOrder = DFSTools.buildCondensation(
					myGraph, 
					condensation, 
					components, 
					typeToRepresentative, 
					getRepresentativeChooser(myRepresentationProvider));
		}
		
		myTopologicalOrder = topologicalOrder;
		if (nontrivialCondensation) {
			myGraph = condensation;
			myTypeToRepresentative = typeToRepresentative;
		} else {
			@SuppressWarnings("unchecked")
			Map<T, T> mapToItself = (Map<T, T>) MAP_TO_ITSELF;
			myTypeToRepresentative = mapToItself;
		}

		if (!alreadyTransitive) {
			DFSTools.transitivelyClose(myGraph);
		} else {
			for (T type : myGraph.getAllVertices()) {
				myGraph.addEdge(type, type);
			}
		}

		T lastType = myTypeToRepresentative.get(topologicalOrder.get(topologicalOrder.size() - 1));
		processTopTypeCandidate(lastType);
	}

	private <E extends Throwable> ICycleHandler<T> getCycleHandler(
			final ITypeErrorHandler<E> errorHandler) {
		return new ICycleHandler<T>() {
			@Override
			public void cycleDetected(IGraph<T> graph, T a, T b, Set<T> started, Set<T> finished, ISuccessorPolicy successorPolicy) {
				errorHandler.reportWarning(WarningType.SUBTYPING_CYCLE_FOUND, myRepresentationProvider.getStringRepresentation(a), myRepresentationProvider.getStringRepresentation(b));
			}
		};
	}

	public static <T> IRepresentativeChooser<T> getRepresentativeChooser(
			final IStringRepresentationProvider<? super T> stringRepresentationProvider) {
		final Comparator<T> comparator = new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				String s1 = stringRepresentationProvider.getStringRepresentation(o1);
				String s2 = stringRepresentationProvider.getStringRepresentation(o2);
				return s1.compareTo(s2);
			}
			
		};
		return new IRepresentativeChooser<T>() {

			@Override
			public T chooseRepresentative(Set<T> component) {
				return Collections.min(component, comparator);
			}
			
		};
	}

	private void processTopTypeCandidate(T lastType) {
		Set<T> allVertices = myGraph.getAllVertices();
		for (T t : allVertices) {
			if (!myGraph.getSuccessors(t).contains(lastType)) {
				return;
			}
		}
		myTopType = lastType;
	}
	
	public int getTopologicalIndex(T type) {
		T typeRep = getRepresentative(type);
		Integer index = myTopologicalIndices.get(typeRep);
		if (index == null) {
			index = customSearch(myTopologicalOrder, type);
			if (index == null) {
				throw new IllegalArgumentException("Not my item: " + type);
			}
			myTopologicalIndices.put(typeRep, index);
		}
		return index;
	}
	
	public T getTypeByTopologicalIndex(int index) {
		return myTopologicalOrder.get(index);
	}
	
	public int size() {
		return myTopologicalOrder.size();
	}
	
	public Set<T> getSupertypes(T type) {
		return myGraph.getSuccessors(getRepresentative(type));
	}

	public Set<T> getSubtypes(T type) {
		return myGraph.getPredecessors(getRepresentative(type));
	}

	public Set<T> getNeighbours(T type, ISuccessorPolicy policy) {
		return policy.getSuccessors(myGraph, getRepresentative(type));
	}
	
	public Set<T> getCommonSubtypes(Set<T> superTypes) {
		return findCommonNeighbours(superTypes, StandardPolicies.PREDECESSORS);
	}

	public Set<T> getCommonSupertypes(Set<T> subTypes) {
		return findCommonNeighbours(subTypes, StandardPolicies.SUCCESSORS);
	}
	
	private Set<T> findCommonNeighbours(Set<T> bounds,
			ISuccessorPolicy successorPolicy) {
		Set<T> common = null;
		for (T bound : bounds) {
			Set<T> followers = successorPolicy.getSuccessors(myGraph, bound);
			if (common == null) {
				common = new HashSet<T>(followers);
			} else {
				common.retainAll(followers);
			}
		}
		return common;
	}
	
	private T getRepresentative(T type) {
		T representative = myTypeToRepresentative.get(type);
		if (representative == null) {
			throw new IllegalStateException("No representative for type " + myRepresentationProvider.getStringRepresentation(type));
		}
		return representative;
	}
	
	@Override
	public boolean isSubtypeOf(T type, T supertype) {
		return getSupertypes(type).contains(supertype);
	}

	@Override
	public T getCollectionElementType(T type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ISubtypingRelation<T> getSubtypingRelation() {
		return this;
	}

	@Override
	public boolean isCollection(T type) {
		return false;
	}

	@Override
	public String getStringRepresentation(T type) {
		return myRepresentationProvider.getStringRepresentation(type);
	}

	@Override
	public T getStringType() {
		throw new UnsupportedOperationException();
	}


	private Integer customSearch(List<T> list, T type) {
		int i = 0;
		for (T t : list) {
			if (t == type || myHashingStrategy.equals(type, t)) {
				return i;
			}
			i++;
		}
		return null;
	}
	
	@Deprecated
	public void __dump(String fileName) throws FileNotFoundException {
		List<T> arrayList = new ArrayList<T>(myGraph.getAllVertices());
		PrintStream out = new PrintStream(new FileOutputStream(fileName));
		out.println("digraph some {");
		
		int i = 0;
		for (T t : arrayList) {
			out.println("v" + i + " [label=\"" + myRepresentationProvider.getStringRepresentation(t) + "\"];");
			i++;
		}
		
		i = 0;
		for (T t : arrayList) {
			Set<T> supertypes = getSupertypes(t);
			if (supertypes.isEmpty()) {
				continue;
			}
			for (T t2 : supertypes) {
				out.println("v" + i + " -> v" + customSearch(arrayList, t2) + ";");
			}
			i++;
		}
		
		out.println("}");
		out.close();
	}
}
