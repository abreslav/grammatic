package org.abreslav.grammatic.query.tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.abreslav.grammatic.emfutils.PackageLocationUtils;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.GrammarPackage;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;

import test.AbstractTest;
import test.Tests;

public class GenericModelBasedTest<T extends AbstractTest, TS extends Tests<T, TS>> {

	protected static class TestUtil<T extends AbstractTest, TS extends Tests<T, TS>> {
		private final ResourceLoader myLoader;
		private TS myResults;
		private final EPackage myPackage;
		private final String myPackageLocation;
		
		public TestUtil(String baseDir, EPackage pack, String packLocation) {
			myPackage = pack;
			myPackageLocation = packLocation;
			myLoader = new ResourceLoader(baseDir);
		}

		public void addSchemaLocation(String from, String to) {
			myLoader.putSchemaLocation(from, URI.createURI(to));
		}
		
		public void saveResults(String resultsFileName) throws IOException {
			URI oldUri = PackageLocationUtils.temporarilyChangeLocation(myPackageLocation, myPackage);
			myLoader.save(resultsFileName, myResults);
			PackageLocationUtils.restoreLocation(myPackage, oldUri);
		}
		
		@SuppressWarnings("unchecked")
		public Collection<Object[]> loadTests(String testFileName, boolean printDiagnostics) throws IOException {
			TS tests = (TS) myLoader.loadStaticModel(testFileName, myPackage, GrammarPackage.eINSTANCE);
			if (printDiagnostics) {
				print(Diagnostician.INSTANCE.validate(tests));
			}
			myResults = (TS) EcoreUtil.copy(tests);
			Collection<Object[]> result = new ArrayList<Object[]>();
			traverseTestTree(tests, myResults, result);
			return result;
		}

		private void print(Diagnostic diagnostics) {
			System.out.println(diagnostics.getMessage());
			List<Diagnostic> children = diagnostics.getChildren();
			for (Diagnostic diagnostic : children) {
				print(diagnostic);
			}
		}

		private void traverseTestTree(TS tests, TS results, Collection<Object[]> result) {
			EList<T> list = tests.getTests();
			Iterator<T> i = results.getTests().iterator();
			for (T test : list) {
				result.add(new Object[] {("" + test.getText()).replace("$", "[S]"), tests.getName(), test, i.next()});
			}
			EList<TS> subTests = tests.getSubTests();
			Iterator<TS> it = results.getSubTests().iterator();
			for (TS tests2 : subTests) {
				traverseTestTree(tests2, it.next(), result);
			}
		}
		
		public ResourceSet getResourceSet() {
			return myLoader.getResourceSet();
		}
		
		public ResourceLoader getLoader() {
			return myLoader;
		}
	}
	
	protected final T myTest;
	protected final T myResult;
	protected final String myTestsName;
	
	public GenericModelBasedTest(String testsName, T test, T result) {
		super();
		myTest = test;
		myResult = result;
		myTestsName = testsName;
	}

}
