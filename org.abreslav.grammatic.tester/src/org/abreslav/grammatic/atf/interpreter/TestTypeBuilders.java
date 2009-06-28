package org.abreslav.grammatic.atf.interpreter;


import org.abreslav.grammatic.atf.parser.ITestTypesBuilders;

public class TestTypeBuilders implements ITestTypesBuilders {

	@Override
	public ITestTypeBuilder getTestTypeBuilder() {
		return new ITestTypeBuilder() {

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public String testType(String token) {
				return token;
			}
		};
	}

}
