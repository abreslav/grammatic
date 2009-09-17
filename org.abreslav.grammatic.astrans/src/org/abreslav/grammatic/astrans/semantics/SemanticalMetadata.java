package org.abreslav.grammatic.astrans.semantics;

import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;

public abstract class SemanticalMetadata {

	public static Namespace SEMANTICAL_NAMESPACE = MetadataFactory.eINSTANCE.createNamespace();
	static {
		SEMANTICAL_NAMESPACE.setUri("http://grammatic.googlecode.com/grammatic/2009/ASTrans/Semantics");
	}
	
	private SemanticalMetadata() {}
}
