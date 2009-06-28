package org.abreslav.grammatic.atf.java.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import org.abreslav.grammatic.atf.parser.IOptions;
import org.abreslav.grammatic.atf.parser.ITypeSystemBuilder;
import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystem;
import org.abreslav.grammatic.emfutils.ProxyRemover;
import org.abreslav.grammatic.parsingutils.resolve.IProxy;
import org.abreslav.grammatic.parsingutils.resolve.ResolvingDomain;
import org.abreslav.grammatic.parsingutils.resolve.ResolvingDomain.ISubjectStubFactory;
import org.abreslav.grammatic.template.util.GenericTypeRenderer;
import org.abreslav.grammatic.utils.CustomHashSet;
import org.abreslav.grammatic.utils.IHashingStrategy;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class JavaTypeSystemBuilder implements ITypeSystemBuilder<EGenericType> {
	
	private static final ISubjectStubFactory<ClassifierKey, EClass> STUB_FACTORY = new ISubjectStubFactory<ClassifierKey, EClass>() {
		@Override
		public IProxy<EClass> getSubjectStub(ClassifierKey key) {
			return new ClassifierProxy();
		}
	};

	private final IHashingStrategy myHashingStrategy = new IHashingStrategy() {

		@Override
		public boolean equals(Object a, Object b) {
			return EcoreUtil.equals((EObject) a, (EObject) b);
		}

		@Override
		public int hashCode(Object o) {
			return myGenericTypeRenderer.getStringRepresentation((EGenericType) o).hashCode();
		}
		
	};
	private final IStringRepresentationProvider<EGenericType> myGenericTypeRenderer = new IStringRepresentationProvider<EGenericType>() {

		private final Map<EGenericType, String> myCache = new WeakHashMap<EGenericType, String>();
		
		@Override
		public String getStringRepresentation(EGenericType type) {
			String result = myCache.get(type);
			if (result == null) {
				result = GenericTypeRenderer.render(type).intern();
				myCache.put(type, result);
			}
			return result;
		}
		
	};
	
	private final ISubtypingRelation<EGenericType> mySubtypingRelation = JavaSubtypingRelation.INSTANCE;
	
	private final ProxyRemover myProxyRemover = new ProxyRemover();
	private final Set<EGenericType> myTypes = new HashSet<EGenericType>();
	private final Set<EGenericType> myNewTypes = new HashSet<EGenericType>();
	private final Map<String, Map<ClassifierKey, EClass>> myModules = new HashMap<String, Map<ClassifierKey,EClass>>();
	
	private ResolvingDomain<ClassifierKey, EClass> myResolvingDomain;
	private IOptions myOptions;
	private final Map<String, ClassifierKey> myRenamings = new HashMap<String, ClassifierKey>();	
	private final Map<ClassifierKey, EClass> myLocalDeclarations = new LinkedHashMap<ClassifierKey, EClass>();
	private final List<EPackage> myEPackages = new ArrayList<EPackage>();

	private String myCurrentPackage;

	private final EGenericType myStringType;
	private final EGenericType myObjectType;
	private final EGenericType myVoidType;
	
	public JavaTypeSystemBuilder() {
		myStringType = createGenericType(JavaLangPackage.getClassByShortName("String"));
		myTypes.add(myStringType);
		myObjectType = createGenericType(JavaLangPackage.getClassByShortName("Object"));
		myTypes.add(myObjectType);
		myVoidType = createGenericType(JavaLangPackage.getClassByShortName("Void"));
		myTypes.add(myVoidType);
	}
	
	@Override
	public Object getStringType() {
		return myStringType;
	}
	
	@Override
	public void openModule(String moduleName, IOptions options) {
		myResolvingDomain = ResolvingDomain.create(STUB_FACTORY);
		myOptions = options;
	}
	
	@Override
	public void loadTypeSystemModule(String moduleName) {
		Map<ClassifierKey, EClass> module = myModules.get(moduleName);
		if (module == null) {
			throw new IllegalStateException("Module was not loaded: " + moduleName);
		}
		for (Entry<ClassifierKey, EClass> entry : module.entrySet()) {
			ClassifierKey key = entry.getKey();
			EClass classifier = entry.getValue();
			myResolvingDomain.markKeyResolved(key, classifier);
		}
	}

	@Override
	public void closeModule(String moduleName) {
		myRenamings.clear();
		myModules.put(moduleName, new HashMap<ClassifierKey, EClass>(myLocalDeclarations));
		processUresolvedKeys();

		for (EClass classifier : myLocalDeclarations.values()) {
			diagnoseClassifier(classifier);
		}
		
		for (EGenericType genericType : myNewTypes) {
			myProxyRemover.removeProxies(genericType);
			diagnoseEObject(genericType, myGenericTypeRenderer.getStringRepresentation(genericType));
		}
		myTypes.addAll(myNewTypes);
		myNewTypes.clear();
		
		myLocalDeclarations.clear();
		myResolvingDomain = null;
		myOptions = null;
	}
	
	@Override
	public void addType(Object type) {
		myNewTypes.add((EGenericType) type);
	}
	
	public ClassifierKey registerRenaming(String pack, String name) {
		ClassifierKey key = myRenamings.get(name);
		if (key == null) {
			key = new ClassifierKey(pack, name);
			myRenamings.put(name, key);
		} else if (!strEquals(pack, key.getPackage())) {
			reportError("Duplicate short name: %s", name);
		}
		return key;
	}

	public void startPackage(EPackage ePackage) {
		String name = ePackage.getName();
		myEPackages.add(ePackage);
		myCurrentPackage = name;
	}
	
	public void endPackage() {
		myCurrentPackage = null;
	}

	public void registerClassifier(String pack, EClass classifierDeclaration) {
		String name = classifierDeclaration.getName();
		ClassifierKey key = registerRenaming(pack, name);
		myResolvingDomain.markKeyResolved(key, classifierDeclaration);
		myLocalDeclarations.put(key, classifierDeclaration);
	}

	public EClass lookupClassifier(String pack, String name) {
		if (pack != null && pack.length() > 0) {
			ClassifierKey key = new ClassifierKey(pack, name);
			return myResolvingDomain.getSubjectStub(key);
		}
		
		ClassifierKey key = myRenamings.get(name);
		if (key == null) {
			key = JavaLangPackage.getRenaming(name);
			if (key == null) {
				key = new ClassifierKey(myCurrentPackage, name);
			}
		}
		
		return myResolvingDomain.getSubjectStub(key);
	}
	
	public void addJavaOption(String name, String value) {
		myOptions.addOption(name, value);
	}
	
	private void processUresolvedKeys() {
		Set<ClassifierKey> unresolvedKeys = myResolvingDomain.getUnresolvedKeys();
		for (ClassifierKey classifierKey : unresolvedKeys) {
			EClass javaLangClassifier = JavaLangPackage.getClass(classifierKey);
			if (javaLangClassifier != null) {
				myResolvingDomain.markKeyResolved(classifierKey, javaLangClassifier);
			} else {
				reportError("Classifier not found: %s.%s", classifierKey.getPackage(), classifierKey.getName());
			}
		}
	}

	private void reportError(String string, Object... objects) {
		throw new IllegalArgumentException(String.format(string, objects));
	}

	private void diagnoseClassifier(EClassifier object) {
		diagnoseEObject(object, "class " + object.getName());
	}
	
	private void diagnoseEObject(EObject object, String messagePart) {
		EObject noProxies = myProxyRemover.removeProxies(object);
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(noProxies);
		if (diagnostic.getSeverity() != Diagnostic.OK) {
			reportError("Error in '%s': %s", messagePart, composeMessage(diagnostic));
		}
	}

	private String composeMessage(Diagnostic diagnostic) {
		if (diagnostic.getSeverity() == Diagnostic.OK) {
			return "";
		}
		List<Diagnostic> children = diagnostic.getChildren();
		if (children.isEmpty()) {
			return diagnostic.getMessage();
		}
		StringBuilder builder = new StringBuilder();
		for (Diagnostic child : children) {
			String message = composeMessage(child);
			if (message.length() > 0) {
				if (builder.length() > 0) {
					builder.append('\n');
				}
				builder.append(message);
			}
		}
		return builder.toString();
	}

	private boolean strEquals(String a, String b) {
		return a == null ? b == null : a.equals(b);
	}
	
	public List<EPackage> getEPackages() {
		return myEPackages;
	}
	
	public <E extends Throwable> ITypeSystem<EGenericType> getTypeSystem(ITypeErrorHandler<E> errorHandler) throws E {
		Set<EGenericType> types = new CustomHashSet<EGenericType>(myHashingStrategy);
		types.add(myObjectType);
		types.add(myStringType);
		types.add(myVoidType);
		types.addAll(myTypes);
		return new FiniteTypeSystem<EGenericType>(
				types, 
				mySubtypingRelation,
				myHashingStrategy,
				myGenericTypeRenderer, 
				errorHandler);
	}

	public static EGenericType createGenericType(EClassifier classifier) {
		EGenericType type = EcoreFactory.eINSTANCE.createEGenericType();
		type.setEClassifier(classifier);
		return type;
	}
}