package org.abreslav.grammatic.query.tester;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class MyParameterized extends Suite {

	private class TestClassRunnerForParameters extends
			BlockJUnit4ClassRunner {
		private final int fParameterSetNumber;

		private final List<Object[]> fParameterList;

		TestClassRunnerForParameters(Class<?> type,
				List<Object[]> parameterList, int i) throws InitializationError {
			super(type);
			fParameterList= parameterList;
			fParameterSetNumber= i;
		}

		@Override
		public Object createTest() throws Exception {
			return getTestClass().getOnlyConstructor().newInstance(
					computeParams());
		}

		private Object[] computeParams() throws Exception {
			try {
				Object[] objects = fParameterList.get(fParameterSetNumber);
				Object[] params = new Object[objects.length - 1];
				System.arraycopy(objects, 1, params, 0, params.length);
				return params;
			} catch (ClassCastException e) {
				throw new Exception(String.format(
						"%s.%s() must return a Collection of arrays.",
						getTestClass().getName(), getParametersMethod(
								getTestClass()).getName()));
			}
		}

		@Override
		protected String getName() {
			Object[] objects = fParameterList.get(fParameterSetNumber);
			Object name = objects[0];
			return String.format("[%s] %s", fParameterSetNumber, name);
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return String.format("%s[%s]", method.getName(),
					fParameterSetNumber);
		}

		@Override
		protected void validateZeroArgConstructor(List<Throwable> errors) {
			// constructor can, nay, should have args.
		}

		@Override
		protected Statement classBlock(RunNotifier notifier) {
			return childrenInvoker(notifier);
		}
	}

	private final ArrayList<Runner> runners= new ArrayList<Runner>();

	/**
	 * Only called reflectively. Do not use programmatically.
	 */
	public MyParameterized(Class<?> klass) throws Throwable {
		super(klass, Collections.<Runner>emptyList());
		List<Object[]> parametersList= getParametersList(getTestClass());
		for (int i= 0; i < parametersList.size(); i++)
			runners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(),
					parametersList, i));
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getParametersList(TestClass klass)
			throws Throwable {
		return (List<Object[]>) getParametersMethod(klass).invokeExplosively(
				null);
	}

	private FrameworkMethod getParametersMethod(TestClass testClass)
			throws Exception {
		List<FrameworkMethod> methods= testClass
				.getAnnotatedMethods(Parameters.class);
		for (FrameworkMethod each : methods) {
			int modifiers= each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				return each;
		}

		throw new Exception("No public static parameters method on class "
				+ testClass.getName());
	}

}
//package org.abreslav.grammatic.query.tester;
//
//import static org.junit.Assert.assertEquals;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//import org.junit.internal.runners.CompositeRunner;
//import org.junit.internal.runners.MethodValidator;
//import org.junit.internal.runners.TestClassMethodsRunner;
//import org.junit.internal.runners.TestClassRunner;
//import org.junit.runners.Parameterized.Parameters;
//
//public class MyParameterized extends TestClassRunner {
//
//	public static Collection<Object[]> eachOne(Object... params) {
//		List<Object[]> results= new ArrayList<Object[]>();
//		for (Object param : params)
//			results.add(new Object[] { param });
//		return results;
//	}
//
//	// TODO: single-class this extension
//	
//	private static class TestClassRunnerForParameters extends TestClassMethodsRunner {
//		private final Object[] fParameters;
//
//		private final int fParameterSetNumber;
//
//		private final Constructor<?> fConstructor;
//		
//		private final String fName;
//
//		private TestClassRunnerForParameters(String name, Class<?> klass, Object[] parameters, int i) {
//			super(klass);
//			fName = name;
//			fParameters= parameters;
//			fParameterSetNumber= i;
//			fConstructor= getOnlyConstructor();
//		}
//
//		@Override
//		protected Object createTest() throws Exception {
//			return fConstructor.newInstance(fParameters);
//		}
//		
//		@Override
//		protected String getName() {
//			return String.format("[%s] %s", fParameterSetNumber, fName);
//		}
//		
//		@Override
//		protected String testName(final Method method) {
//			return String.format("%s[%s]", method.getName(), fParameterSetNumber);
//		}
//
//		private Constructor<?> getOnlyConstructor() {
//			Constructor<?>[] constructors= getTestClass().getConstructors();
//			assertEquals(1, constructors.length);
//			return constructors[0];
//		}
//	}
//	
//	// TODO: I think this now eagerly reads parameters, which was never the point.
//	
//	public static class RunAllParameterMethods extends CompositeRunner {
//		private final Class<?> fKlass;
//
//		public RunAllParameterMethods(Class<?> klass) throws Exception {
//			super(klass.getName());
//			fKlass= klass;
//			int i= 0;
//			for (final Object each : getParametersList()) {
//				if (each instanceof Object[]) {
//					Object[] objects = (Object[])each;
//					Object[] params = Arrays.copyOfRange(objects, 1, objects.length);
//					super.add(new TestClassRunnerForParameters((String) objects[0], klass, params, i++));
//				} else
//					throw new Exception(String.format("%s.%s() must return a Collection of arrays.", fKlass.getName(), getParametersMethod().getName()));
//			}
//		}
//
//		private Collection<?> getParametersList() throws IllegalAccessException, InvocationTargetException, Exception {
//			return (Collection<?>) getParametersMethod().invoke(null);
//		}
//		
//		private Method getParametersMethod() throws Exception {
//			for (Method each : fKlass.getMethods()) {
//				if (Modifier.isStatic(each.getModifiers())) {
//					Annotation[] annotations= each.getAnnotations();
//					for (Annotation annotation : annotations) {
//						if (annotation.annotationType() == Parameters.class)
//							return each;
//					}
//				}
//			}
//			throw new Exception("No public static parameters method on class "
//					+ getName());
//		}
//	}
//	
//	public MyParameterized(final Class<?> klass) throws Exception {
//		super(klass, new RunAllParameterMethods(klass));
//	}
//	
//	@Override
//	protected void validate(MethodValidator methodValidator) {
//		methodValidator.validateStaticMethods();
//		methodValidator.validateInstanceMethods();
//	}
//}
