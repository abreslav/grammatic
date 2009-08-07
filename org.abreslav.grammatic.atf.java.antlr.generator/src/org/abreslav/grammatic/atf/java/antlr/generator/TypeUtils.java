package org.abreslav.grammatic.atf.java.antlr.generator;

import org.abreslav.grammatic.atf.java.antlr.semantics.JavaType;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;

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
		EPackage ePackage = type.getEClassifier().getEPackage();
		String pack = ePackage != null ? ePackage.getName() : null;
		return createJavaType(pack, name);
	}

	public static Type createJavaType(String pack, String name) {
		JavaType javaType = SemanticsFactory.eINSTANCE.createJavaType();
		javaType.setName(name);
		javaType.setPackage(pack);
		return javaType;
	}

}
