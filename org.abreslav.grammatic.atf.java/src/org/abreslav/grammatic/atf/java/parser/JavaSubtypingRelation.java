package org.abreslav.grammatic.atf.java.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.util.EcoreValidator;

public class JavaSubtypingRelation implements ISubtypingRelation<EGenericType> {
	
	private interface ISubtyping {
		boolean isMySupertype(EClassifier type);
		boolean isMySubtype(EClassifier type);
	}
	
	private final ISubtyping VOID_SUBTYPING = new ISubtyping() {

		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.VOID_CLASS;
		}

		@Override
		public boolean isMySupertype(EClassifier type) {
			return true;
		}
		
	};
	
	private final ISubtyping OBJECT_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return true;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.OBJECT_CLASS;
		}
		
	};

	private final static ISubtyping INT_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.INT
				|| type == JavaLangPackage.BYTE
				|| type == JavaLangPackage.SHORT
				|| type == JavaLangPackage.CHAR
				;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.INT
				|| type == JavaLangPackage.LONG
				|| type == JavaLangPackage.FLOAT
				|| type == JavaLangPackage.DOUBLE
				;
		}
		
	};
	
	private final static ISubtyping BYTE_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.BYTE
			;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.BYTE
				|| type == JavaLangPackage.INT
				|| type == JavaLangPackage.SHORT
				|| type == JavaLangPackage.LONG
				|| type == JavaLangPackage.FLOAT
				|| type == JavaLangPackage.DOUBLE
			;
		}
		
	};
	
	private final static ISubtyping SHORT_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.SHORT
				|| type == JavaLangPackage.BYTE
			;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.SHORT
			|| type == JavaLangPackage.INT
			|| type == JavaLangPackage.LONG
			|| type == JavaLangPackage.FLOAT
			|| type == JavaLangPackage.DOUBLE
			;
		}
		
	};
	
	private final static ISubtyping CHAR_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.CHAR
			;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.CHAR
			|| type == JavaLangPackage.INT
			|| type == JavaLangPackage.LONG
			|| type == JavaLangPackage.FLOAT
			|| type == JavaLangPackage.DOUBLE
			;
		}
		
	};
	
	private final static ISubtyping LONG_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.LONG
				|| type == JavaLangPackage.INT
				|| type == JavaLangPackage.BYTE
				|| type == JavaLangPackage.CHAR
				|| type == JavaLangPackage.SHORT
			;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.LONG
			|| type == JavaLangPackage.FLOAT
			|| type == JavaLangPackage.DOUBLE
			;
		}
		
	};
	
	private final static ISubtyping BOOLEAN_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.BOOLEAN;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.BOOLEAN;
		}
		
	};
	
	private final static ISubtyping FLOAT_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.FLOAT
				|| type == JavaLangPackage.INT
				|| type == JavaLangPackage.LONG
				|| type == JavaLangPackage.BYTE
				|| type == JavaLangPackage.CHAR
				|| type == JavaLangPackage.SHORT
			;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.FLOAT
			|| type == JavaLangPackage.DOUBLE
			;
		}
		
	};
	
	private final static ISubtyping DOUBLE_SUBTYPING = new ISubtyping() {
		
		@Override
		public boolean isMySubtype(EClassifier type) {
			return type == JavaLangPackage.DOUBLE
			|| type == JavaLangPackage.INT
			|| type == JavaLangPackage.BYTE
			|| type == JavaLangPackage.CHAR
			|| type == JavaLangPackage.SHORT
			|| type == JavaLangPackage.DOUBLE
			|| type == JavaLangPackage.LONG
			;
		}
		
		@Override
		public boolean isMySupertype(EClassifier type) {
			return type == JavaLangPackage.DOUBLE;
		}
		
	};

	public static final JavaSubtypingRelation INSTANCE = new JavaSubtypingRelation();
	
	private final Map<EClassifier, ISubtyping> mySubtypings = new HashMap<EClassifier, ISubtyping>();

	private JavaSubtypingRelation() {
		mySubtypings.put(JavaLangPackage.VOID_CLASS, VOID_SUBTYPING);
		mySubtypings.put(JavaLangPackage.OBJECT_CLASS, OBJECT_SUBTYPING);
		mySubtypings.put(JavaLangPackage.INT, INT_SUBTYPING);
		mySubtypings.put(JavaLangPackage.CHAR, CHAR_SUBTYPING);
		mySubtypings.put(JavaLangPackage.BYTE, BYTE_SUBTYPING);
		mySubtypings.put(JavaLangPackage.SHORT, SHORT_SUBTYPING);
		mySubtypings.put(JavaLangPackage.LONG, LONG_SUBTYPING);
		mySubtypings.put(JavaLangPackage.FLOAT, FLOAT_SUBTYPING);
		mySubtypings.put(JavaLangPackage.DOUBLE, DOUBLE_SUBTYPING);
		mySubtypings.put(JavaLangPackage.BOOLEAN, BOOLEAN_SUBTYPING);
	}

	@Override
	public boolean isSubtypeOf(EGenericType type, EGenericType supertype) {
		EClassifier theType = type.getEClassifier();
		EClassifier theSupertype = supertype.getEClassifier();
		ISubtyping subtyping = mySubtypings.get(theType);
		if (subtyping == null) {
			subtyping = mySubtypings.get(theSupertype);
			if (subtyping == null) {
				Map<? extends ETypeParameter, ? extends EGenericType> map = Collections.emptyMap();
				return EcoreValidator.isBounded(type, supertype, map);
			}
			return subtyping.isMySubtype(theType);
		}
		return subtyping.isMySupertype(theSupertype);
	}
	
}
