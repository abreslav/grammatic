package org.abreslav.grammatic.atf;

import java.util.Map;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;

public class SyntacticFunctionTraverser {

	public static <D> void processSyntacticFunctions(Symbol symbol, IMetadataProvider rootMetadataProvider, 
			ISyntacticFunctionHandler<D> handler, D data) {
		IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, rootMetadataProvider);
		if (symbolMetadata.isPresent(ATFMetadata.DEFAULT_NAMESPACE)) {
			Namespace namespace = (Namespace) symbolMetadata.readEObject(ATFMetadata.DEFAULT_NAMESPACE);
			handler.handleSyntacticFunction(rootMetadataProvider.getProjection(namespace), data);
		} else if (!symbolMetadata.isPresent(ATFMetadata.TOKEN)) {
			@SuppressWarnings("unchecked")
			Map<String, Namespace> functionNameToNamespace = (Map<String, Namespace>) symbolMetadata.readObject(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE);
			for (Namespace namespace : functionNameToNamespace.values()) {
				handler.handleSyntacticFunction(rootMetadataProvider.getProjection(namespace), data);
			}
		}
	}

}
