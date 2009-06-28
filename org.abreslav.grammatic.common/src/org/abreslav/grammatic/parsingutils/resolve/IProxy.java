package org.abreslav.grammatic.parsingutils.resolve;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.utils.IHashingStrategy;

public interface IProxy<T> {

	IHashingStrategy HASHING_STRATEGY = new IHashingStrategy() {

		@Override
		public boolean equals(Object a, Object b) {
			return Utils.unwrap(a).equals(Utils.unwrap(b));
		}

		@Override
		public int hashCode(Object o) {
			return Utils.unwrap(o).hashCode();
		}
		
	};
	
	T pGetDelegate();
	void pSetDelegate(T delegate);
	
	public class Utils {
		public static <T> IProxy<T> universalProxyForClass(Class<T> mainIntf, Class<?> toReplace) {
			HashSet<Class<?>> superIntfs = new HashSet<Class<?>>();
			getAllSuperInterfaces(toReplace, superIntfs);
			superIntfs.remove(mainIntf);
			return universalProxy(mainIntf, superIntfs.toArray(new Class<?>[superIntfs.size()]));
		}
		
		private static void getAllSuperInterfaces(Class<?> clazz, Set<Class<?>> superIntfs) {
			while (clazz != null) {
				Class<?>[] interfaces = clazz.getInterfaces();
				for (Class<?> intf : interfaces) {
					superIntfs.add(intf);
				}
				clazz = clazz.getSuperclass();
			}
		}

		public static <T> T unwrap(T a) {
			while (a instanceof IProxy<?>) {
				@SuppressWarnings("unchecked")
				IProxy<T> proxy = (IProxy<T>) a;
				a = proxy.pGetDelegate();
			}
			return a;
		}
		
		@SuppressWarnings("unchecked")
		public static <T> IProxy<T> universalProxy(Class<T> mainIntf, Class<?>... otherIntfs) {
			Class<?>[] intfs = new Class<?>[otherIntfs.length + 2];
			intfs[0] = IProxy.class;
			intfs[1] = mainIntf;
			System.arraycopy(otherIntfs, 0, intfs, 2, otherIntfs.length);
			final Method pGetDelegate;
			final Method pSetDelegate;
			final Method equals;
			final Method hashCode;
			try {
				pGetDelegate = IProxy.class.getDeclaredMethod("pGetDelegate");
				pSetDelegate = IProxy.class.getDeclaredMethod("pSetDelegate", Object.class);
				equals = Object.class.getDeclaredMethod("equals", Object.class);
				hashCode = Object.class.getDeclaredMethod("hashCode");
			} catch (SecurityException e) {
				throw new IllegalStateException(e);
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
			return (IProxy<T>) Proxy.newProxyInstance(mainIntf.getClassLoader(), intfs, new InvocationHandler() {
				private T myDelegate;
				
				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					if (method.equals(pGetDelegate)) {
						return myDelegate;
					}
					if (method.equals(pSetDelegate)) {
						myDelegate = (T) args[0];
						return null;
					}
					if (method.equals(equals)) {
						throw new UnsupportedOperationException("Never use equals(Object) on proxies!");
					}
					if (method.equals(hashCode)) {
						throw new UnsupportedOperationException("Never use hashCode() on proxies!");
					}
					return method.invoke(myDelegate, args); 
				}
				
			});
		}
		
	}
}
