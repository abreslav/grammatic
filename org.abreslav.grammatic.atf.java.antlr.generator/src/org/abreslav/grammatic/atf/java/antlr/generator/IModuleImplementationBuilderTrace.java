package org.abreslav.grammatic.atf.java.antlr.generator;

import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;

public interface IModuleImplementationBuilderTrace {

	IModuleImplementationBuilderTrace NONE = new IModuleImplementationBuilderTrace() {

		@Override
		public void putFunctionToMethod(FunctionSignature function,
				Method method) {
		}
		
	};
	
	void putFunctionToMethod(FunctionSignature function, Method method);
}
