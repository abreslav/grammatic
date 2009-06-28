package org.abreslav.grammatic.grammar.parser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.utils.printer.Printer;

public class ProxyBuilders {
	
	private static Map<String, Object> ourPrimitiveNameToNull = new HashMap<String, Object>();
	static {
		ourPrimitiveNameToNull.put("byte", Byte.valueOf((byte) 0));
		ourPrimitiveNameToNull.put("char", Character.valueOf((char) 0));
		ourPrimitiveNameToNull.put("short", Short.valueOf((short) 0));
		ourPrimitiveNameToNull.put("int", Integer.valueOf(0));
		ourPrimitiveNameToNull.put("double", Double.valueOf(0));
		ourPrimitiveNameToNull.put("float", Float.valueOf(0));
		ourPrimitiveNameToNull.put("boolean", Boolean.FALSE);
		ourPrimitiveNameToNull.put("long", Long.valueOf(0));
	}

	public static <T> T getBuilders(
			Class<T> clazz, final Printer printer) {
		final ClassLoader classLoader = clazz.getClassLoader();
		@SuppressWarnings("unchecked")
		T result = (T) Proxy.newProxyInstance(classLoader, new Class<?>[] {clazz},
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						final Class<?> type = method.getReturnType();
						return Proxy.newProxyInstance(classLoader, new Class<?>[] {type},
								new InvocationHandler() {

									@Override
									public Object invoke(Object proxy,
											Method method, Object[] args)
											throws Throwable {
										if (method.equals(Object.class.getMethod("toString"))) {
											printer.print(type.getSimpleName());
										} else {
											printer.print(type.getSimpleName(), ".", method.getName()).endl();
										}
										return getNull(method.getReturnType());
									}

									private Object getNull(Class<?> returnType) throws InstantiationException, IllegalAccessException {
										if (returnType.isPrimitive()) {
											return ourPrimitiveNameToNull.get(returnType.getName());
										}
										return null;
									}
							
						});
					}
			
		});
		return result;
	}
}
