package org.abreslav.grammatic.atf;

import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;

public abstract class ATFMetadata {
	public static final String AFTER = "after";
	public static final String ASSIGN_TEXT_TO_ATTRIBUTE = "assignTextToAttribute";
	public static final String ASSIGNED_TO_ATTRIBUTES = "assignedToAttributes";
	public static final String ASSOCIATED_CALL_ARGUMENTS = "associatedCallArguments";
	public static final String ASSOCIATED_FUNCTION_NAME = "associatedFunctionName";
	public static final String ASSOCIATED_FUNCTION = "associatedFunction";
	public static final String ASSOCIATED_NAMESPACE = "associatedNamespace";
	public static final String ASSOCIATED_WITH_DEFAULT_FUNCTION = "associatedWithDefaultFunction";
	public static final String ASSOCIATED_WITH_TOKEN = "associatedWithAToken";
	public static final String BEFORE = "before";
	public static final String DEFAULT_NAMESPACE = "defaultNamespace";
	public static final String DEFAULT_SYNTACTIC_FUNCTION = "defaultSyntacticFunction";
	public static final String FUNCTION_NAME_TO_NAMESPACE = "functionNameToNamespace";
	public static final String FUNCTION_NAME_TO_FUNCTION = "functionNameToFunction";
	public static final String SEMANTIC_MODULE = "semanticModule";
	public static final String USED_SEMANTIC_MODULES = "usedSemanticModules";
	public static final String SYNTACTIC_FUNCTION = "syntacticFunction";
	public static final String TOKEN = "token";
	public static final String TOKEN_CLASSES = "tokenClasses";

	public static final Namespace ATF_NAMESPACE = MetadataFactory.eINSTANCE.createNamespace();
	static {
		ATF_NAMESPACE.setUri("http://www.abreslav.org/grammatic/namespaces/2009/ATF");		
	}

	public static final String TYPE_SYSTEM_OPTIONS = "typeSystemOptions";

	private ATFMetadata() {}
}
