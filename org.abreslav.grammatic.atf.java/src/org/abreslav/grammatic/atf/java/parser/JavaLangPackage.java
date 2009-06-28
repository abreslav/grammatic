package org.abreslav.grammatic.atf.java.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;


public class JavaLangPackage {
	public static final EDataType LONG = EcorePackage.eINSTANCE.getELong();
	public static final EDataType BOOLEAN = EcorePackage.eINSTANCE.getEBoolean();
	public static final EDataType FLOAT = EcorePackage.eINSTANCE.getEFloat();
	public static final EDataType DOUBLE = EcorePackage.eINSTANCE.getEDouble();
	public static final EDataType SHORT = EcorePackage.eINSTANCE.getEShort();
	public static final EDataType CHAR = EcorePackage.eINSTANCE.getEChar();
	public static final EDataType BYTE = EcorePackage.eINSTANCE.getEByte();
	public static final EDataType INT = EcorePackage.eINSTANCE.getEInt();
	
	private static final Map<String, EDataType> PREDEFINED_TYPE_NAMES = new HashMap<String, EDataType>();
	private static final Set<EDataType> PREDEFINED_TYPES = new HashSet<EDataType>();
	
	static {
		addPredefinedType("int", INT);
		addPredefinedType("byte", BYTE);
		addPredefinedType("char", CHAR);
		addPredefinedType("short", SHORT);
		addPredefinedType("double", DOUBLE);
		addPredefinedType("float", FLOAT);
		addPredefinedType("boolean", BOOLEAN);
		addPredefinedType("long", LONG);
	}

	private static void addPredefinedType(String name, EDataType type) {
		PREDEFINED_TYPE_NAMES.put(name, type);
		PREDEFINED_TYPES.add(type);
	}

	public static final EClass OBJECT_CLASS;
	public static final EClass STRING_CLASS;
	public static final EClass VOID_CLASS;
	public static final EClass INTEGER_CLASS;
	public static final EClass BOOLEAN_CLASS;
	public static final EClass SHORT_CLASS;
	public static final EClass BYTE_CLASS;
	public static final EClass LONG_CLASS;
	public static final EClass DOUBLE_CLASS;
	public static final EClass FLOAT_CLASS;
	public static final EClass CHARACTER_CLASS;
	
	private static final Map<ClassifierKey, EClass> CLASSES = new HashMap<ClassifierKey, EClass>();
	private static final Map<String, ClassifierKey> RENAMINGS = new HashMap<String, ClassifierKey>();
	static {
		try {
			// TODO : load the model by some reasonable URI
			EPackage ePackage = (EPackage) new ResourceLoader("../org.abreslav.grammatic.atf.java/model").loadStaticModel("java.lang.xmi", EcorePackage.eINSTANCE);
			
			String packName = ePackage.getName();
			for (EClassifier classifier : ePackage.getEClassifiers()) {
				String name = classifier.getName();
				ClassifierKey key = new ClassifierKey(packName, name);
				CLASSES.put(key, (EClass) classifier);
				RENAMINGS.put(name, key);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		OBJECT_CLASS = getClassByShortName("Object");
		STRING_CLASS = getClassByShortName("String");
		VOID_CLASS = getClassByShortName("Void");
		INTEGER_CLASS = getClassByShortName("Integer");
		BYTE_CLASS = getClassByShortName("Byte");
		BOOLEAN_CLASS = getClassByShortName("Boolean");
		SHORT_CLASS = getClassByShortName("Short");
		CHARACTER_CLASS = getClassByShortName("Character");
		LONG_CLASS = getClassByShortName("Long");
		DOUBLE_CLASS = getClassByShortName("Double");
		FLOAT_CLASS = getClassByShortName("Float");
	}
	
	public static EClass getClass(ClassifierKey classifierKey) {
		return CLASSES.get(classifierKey);
	}

	public static EClass getClassByShortName(String name) {
		return CLASSES.get(getRenaming(name));
	}
	
	public static ClassifierKey getRenaming(String name) {
		return RENAMINGS.get(name);
	}
	
	public static EDataType getPredefinedType(String name) {
		return PREDEFINED_TYPE_NAMES.get(name);
	}
	
	public static boolean isPredefinedType(EClassifier classifier) {
		return PREDEFINED_TYPES.contains(classifier);
	}
}
