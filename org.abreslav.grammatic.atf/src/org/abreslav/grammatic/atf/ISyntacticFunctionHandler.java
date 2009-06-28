package org.abreslav.grammatic.atf;

import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;

public interface ISyntacticFunctionHandler<D> {

	void handleSyntacticFunction(IMetadataProvider projection, D data);

}
