package org.abreslav.grammatic.atf.java.antlr.generator;

import org.abreslav.grammatic.atf.java.antlr.semantics.GenericJavaType;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaType;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.parsingutils.ImportManager;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;

public class TypeUtils {

	private static Type ourVoidType;
	
	public static Type getVoidType() {
		if (ourVoidType == null) {
			ourVoidType = createJavaType(null, "void");
		}
		return ourVoidType;
	}
	
	public static Type getType(EGenericType type, IStringRepresentationProvider<EGenericType> myStringRepresentationProvider) {
		if (type == null) {
			return null;
		}
		String name = myStringRepresentationProvider.getStringRepresentation(type);
		if (JavaUtils.getPrimitiveTypeNames().contains(name)) {
			return createJavaType(null, name);
		}
		EClassifier eClassifier = type.getEClassifier();
		EPackage ePackage = eClassifier.getEPackage();
		String pack = ePackage != null ? ePackage.getName() : null;
		if (type.getETypeArguments().isEmpty()) {
			return createJavaType(pack, name);
		}
		GenericJavaType genericJavaType = SemanticsFactory.eINSTANCE.createGenericJavaType();
		genericJavaType.setName(eClassifier.getName());
		genericJavaType.setPackage(pack);
		genericJavaType.setGenericType(EMFProxyUtil.copy(type));
		return genericJavaType;
	}

	public static Type createJavaType(String pack, String name) {
		JavaType javaType = SemanticsFactory.eINSTANCE.createJavaType();
		javaType.setName(name);
		javaType.setPackage(pack);
		return javaType;
	}

	public static String getQualifiedName(Type type) {
		String pack = type.getPackage();
		String name = type.getName();
		return qualify(pack, name);
	}

	private static String getQualifiedName(EClassifier type) {
		String pack = type.getEPackage().getName();
		String name = type.getName();
		return qualify(pack, name);
	}
	
	private static String qualify(String pack, String name) {
		String fqn = pack != null ? pack + "." + name : name;
		return fqn;
	}

	public static String getTypeName(Type type, ImportManager importManager) {
		if (type instanceof GenericJavaType) {
			GenericJavaType genericJavaType = (GenericJavaType) type;
			EGenericType genericType = genericJavaType.getGenericType();
			StringBuilder result = new StringBuilder();
			stringRepresentation(genericType, result, importManager);
			return result.toString();
		}
		String fqn = getQualifiedName(type);
		return importManager.getTypeName(fqn);
	}

	// TODO !!! Stolen from EMF implementation !!! 
	private static void stringRepresentation(EGenericType genericType,
			StringBuilder result, ImportManager importManager) {
		EClassifier eClassifier = genericType.getEClassifier();
		EList<EGenericType> eTypeArguments = genericType.getETypeArguments();
		ETypeParameter eTypeParameter = genericType.getETypeParameter();
		EGenericType eLowerBound = genericType.getELowerBound();
		EGenericType eUpperBound = genericType.getEUpperBound();		
		if (eClassifier != null) {
			String tail = null;
			if (eClassifier.getName() != null) {
				result.append(importManager.getTypeName(getQualifiedName(eClassifier)));
			} else {
				String instanceTypeName = eClassifier.getInstanceTypeName();
				if (instanceTypeName != null) {
					int index = instanceTypeName.indexOf('[');
					if (index != -1) {
						tail = instanceTypeName.substring(index);
						result.append(instanceTypeName, 0, index);
					} else {
						result.append(instanceTypeName);
					}
				}
			}

			if (eTypeArguments != null && !eTypeArguments.isEmpty()) {
				boolean first = true;
				result.append('<');
				for (EGenericType eTypeArgument : eTypeArguments) {
					if (first) {
						first = false;
					} else {
						result.append(", ");
					}
					stringRepresentation(eTypeArgument, result, importManager);
				}
				result.append('>');
			}

			if (tail != null) {
				result.append(tail);
			}
		} else if (eTypeParameter != null) {
			String label = eTypeParameter.getName();
			if (label != null) {
				result.append(label);
			}
		} else {
			result.append('?');
			if (eLowerBound != null) {
				result.append(" super ");
				stringRepresentation(eLowerBound, result, importManager);
			} else {
				if (eUpperBound != null) {
					result.append(" extends ");
					stringRepresentation(eUpperBound, result, importManager);
				}
			}
		}
	}

}
