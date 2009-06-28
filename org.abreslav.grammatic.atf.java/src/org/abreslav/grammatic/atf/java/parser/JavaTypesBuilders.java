package org.abreslav.grammatic.atf.java.parser;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;

public class JavaTypesBuilders implements IJavaTypesBuilders {
	
	private final JavaTypeSystemBuilder myTypeSystemBuilder;
	private final Map<String, ETypeParameter> myTypeParameters = new HashMap<String, ETypeParameter>();
	private final Map<String, ClassifierKey> myClassifierRenamings = new HashMap<String, ClassifierKey>();
	
	public JavaTypesBuilders(JavaTypeSystemBuilder typeSystemBuilder) {
		myTypeSystemBuilder = typeSystemBuilder;
	}

	private EGenericType lookupType(String pack, String name) {
		if (pack != null && pack.length() > 0) {
			EClassifier subjectStub = myTypeSystemBuilder.lookupClassifier(pack, name);
			return JavaTypeSystemBuilder.createGenericType(subjectStub);
		}
		
		ETypeParameter typeParameter = myTypeParameters.get(name);
		if (typeParameter != null) {
			EGenericType type = EcoreFactory.eINSTANCE.createEGenericType();
			type.setETypeParameter(typeParameter);
			return type;
		}
		
		EClassifier classifier = myTypeSystemBuilder.lookupClassifier(pack, name);
		return JavaTypeSystemBuilder.createGenericType(classifier);
	}
	
	@Override
	public IDeclarationsBuilder getDeclarationsBuilder() {
		return new IDeclarationsBuilder() {
			
			@Override
			public void init() {
				myClassifierRenamings.clear();
			}
			
			@Override
			public void release() {
			}

			@Override
			public void packageDeclaration(EPackage packageDeclaration) {
			}

			@Override
			public void classifierDeclaration(EClass classifierDeclaration) {
				myTypeSystemBuilder.registerClassifier(
						null, 
						classifierDeclaration);
			}

		};
	}
	
	@Override
	public IJavaTypeBuilder getJavaTypeBuilder() {
		return new IJavaTypeBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public EGenericType classType(EGenericType classType) {
				return classType;
			}

			@Override
			public EGenericType primitiveType(EGenericType primitiveType) {
				return primitiveType;
			}
			
		};
	}
	
	@Override
	public IPrimitiveTypeBuilder getPrimitiveTypeBuilder() {
		return new IPrimitiveTypeBuilder() {

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public EGenericType primitiveType(String token) {
				EClassifier classifier = JavaLangPackage.getPredefinedType(token);
				return JavaTypeSystemBuilder.createGenericType(classifier);
			}
			
		};
	}
	
	@Override
	public IPackageDeclarationBuilder getPackageDeclarationBuilder() {
		return new IPackageDeclarationBuilder() {
			
			private EPackage myPackage;

			@Override
			public void init() {
				myPackage = EcoreFactory.eINSTANCE.createEPackage();
			}
			
			@Override
			public void release() {
				myPackage = null;
				myTypeSystemBuilder.endPackage();
			}
			
			@Override
			public void classifierDeclaration(EClass classifierDeclaration) {
				myPackage.getEClassifiers().add(classifierDeclaration);
				myTypeSystemBuilder.registerClassifier(
						myPackage.getName(), 
						classifierDeclaration);
			}

			@Override
			public void packageName(String packageName) {
				myPackage.setName(packageName);
				myTypeSystemBuilder.startPackage(myPackage);
			}

			@Override
			public EPackage getResult() {
				return myPackage;
			}
			
		};
	}
	
	@Override
	public IImport1Builder getImport1Builder() {
		return new IImport1Builder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public void import1(String packageName) {
				int lastDot = packageName.lastIndexOf('.');
				if (lastDot == -1) {
					reportError("Cannot import from default package: '%s'", packageName);
				}
				String pack = packageName.substring(0, lastDot);
				String classifier = packageName.substring(lastDot + 1);
				myTypeSystemBuilder.registerRenaming(pack, classifier);
			}
		};
	}
	
	@Override
	public IOptionBuilder getOptionBuilder() {
		return new IOptionBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}
			
			@Override
			public void option(String name, String string) {
				string = string.substring(1, string.length() - 1);
				myTypeSystemBuilder.addJavaOption(name, string);
			}
		};
	}
	
	@Override
	public IPackageNameBuilder getPackageNameBuilder() {
		return new IPackageNameBuilder() {

			private StringBuilder myBuilder;
			
			@Override
			public void init() {
				myBuilder = new StringBuilder();
			}
			
			@Override
			public void release() {
				myBuilder = null;
			}

			@Override
			public void name(String name) {
				appendPackageName(myBuilder, name);
			}

			@Override
			public void last(String last) {
				appendPackageName(myBuilder, last);
			}
			
			@Override
			public String getResult() {
				return myBuilder.toString();
			}
			
		};
	}
	
	@Override
	public IClassifierDeclarationBuilder getClassifierDeclarationBuilder() {
		return new IClassifierDeclarationBuilder() {
			
			private EClass myClass;

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				myClass = null;
				myTypeParameters.clear();
			}

			@Override
			public EClass createResult() {
				myClass = EcoreFactory.eINSTANCE.createEClass();
				return myClass;
			}

			@Override
			public void name(String name) {
				myClass.setName(name);
			}
			
		};
	}
	
	@Override
	public ISuperTypeListBuilder getSuperTypeListBuilder() {
		return new ISuperTypeListBuilder() {
			
			private EClass myClass;

			@Override
			public void init(EClass class1) {
				myClass = class1;
			}
			
			@Override
			public void release() {
				myClass = null;
			}

			@Override
			public void classType(EGenericType classType) {
				ETypeParameter typeParameter = classType.getETypeParameter();
				if (typeParameter != null) {
					reportError("Classifier '%s' cannot extend a type parameter '%s", myClass.getName(), typeParameter.getName());
				}
				myClass.getEGenericSuperTypes().add(classType);
			}
			
		};
	}
	
	@Override
	public IGenericParametersBuilder getGenericParametersBuilder() {
		return new IGenericParametersBuilder() {
			
			private EClass myClass;

			@Override
			public void init(EClass class1) {
				myClass = class1;
			}
			
			@Override
			public void release() {
				myClass = null;
			}

			@Override
			public void genericParameter(ETypeParameter parameter) {
				myClass.getETypeParameters().add(parameter);
			}
			
		};
	}
	
	@Override
	public IGenericParameterBuilder getGenericParameterBuilder() {
		return new IGenericParameterBuilder() {
			
			private ETypeParameter myParameter;

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				myParameter = null;
			}

			@Override
			public ETypeParameter createResult() {
				myParameter = EcoreFactory.eINSTANCE.createETypeParameter();
				return myParameter;
			}

			@Override
			public void name(String name) {
				if (myTypeParameters.containsKey(name)) {
					reportError("Parameter '%s' redeclared for", name);
				}
				myParameter.setName(name);
				myTypeParameters.put(name, myParameter);
			}
			
		};
	}
	
	@Override
	public IParameterBoundsBuilder getParameterBoundsBuilder() {
		return new IParameterBoundsBuilder() {
			
			private ETypeParameter myParameter;

			@Override
			public void init(ETypeParameter parameter) {
				myParameter = parameter;
			}
			
			@Override
			public void release() {
				myParameter = null;
			}

			@Override
			public void classType(EGenericType classType) {
				myParameter.getEBounds().add(classType);
			}
			
		};
	}
	
	@Override
	public IClassTypeBuilder getClassTypeBuilder() {
		return new IClassTypeBuilder() {

			@Override
			public void init() {
				
			}
			
			@Override
			public void release() {
			}

			@Override
			public EGenericType classType(EGenericType result) {
				myTypeSystemBuilder.addType(result);
				return result;
			}
			
		};
	}
	
	@Override
	public IRawClassifierReferenceBuilder getRawClassifierReferenceBuilder() {
		return new IRawClassifierReferenceBuilder() {
			
			private StringBuilder myBuilder;
			private String myName;

			@Override
			public void init() {
				myBuilder = new StringBuilder();
			}
			
			@Override
			public void release() {
				myBuilder = null;
			}

			@Override
			public void name(String name) {
				appendPackageName(myBuilder, name);
			}
			
			@Override
			public void last(String last) {
				myName = last;
			}
			
			@Override
			public EGenericType getResult() {
				return lookupType(myBuilder.toString(), myName);
			}
			
		};
	}
	
	@Override
	public IGenericArgumentsBuilder getGenericArgumentsBuilder() {
		return new IGenericArgumentsBuilder() {
			
			private EGenericType myType;

			@Override
			public void init(EGenericType type) {
				myType = type;
			}
			
			@Override
			public void release() {
				myType = null;
			}

			@Override
			public void genericArgument(EGenericType genericArgument) {
				myType.getETypeArguments().add(genericArgument);
			}
			
		};
	}
	
	@Override
	public IGenericArgumentBuilder getGenericArgumentBuilder() {
		return new IGenericArgumentBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}
			
			@Override
			public EGenericType classType(EGenericType classType) {
				return classType;
			}

			@Override
			public EGenericType wildcard(EGenericType wildcard) {
				return wildcard;
			}
			
		};
	}
	
	@Override
	public IWildcardBuilder getWildcardBuilder() {
		return new IWildcardBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public EGenericType wildcard(EGenericType upper, EGenericType lower) {
				EGenericType type = EcoreFactory.eINSTANCE.createEGenericType();
				type.setELowerBound(lower);
				type.setEUpperBound(upper);
				return type;
			}
			
		};
	}

	private void reportError(String string, Object... objects) {
		throw new IllegalArgumentException(String.format(string, objects));
	}

	private static void appendPackageName(StringBuilder builder, String name) {
		if (builder.length() != 0) {
			builder.append(".");
		}
		builder.append(name);
	}
	
}
