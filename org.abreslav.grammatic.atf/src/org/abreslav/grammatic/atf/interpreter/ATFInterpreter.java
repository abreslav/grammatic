package org.abreslav.grammatic.atf.interpreter;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.IErrorHandler;

public class ATFInterpreter {

	public static <E extends RuntimeException> void runATFDefinition(
			AspectDefinition aspectDefinition,
			Grammar grammar,
			MetadataAspect aspect,
			IErrorHandler<E> errorHandler) throws E {
		System.err.println("This does not work");
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		AspectDefinitionInterpreter.runDefinition(
				aspectDefinition, 
				grammar, 
				IMetadataProvider.EMPTY, 
				writableAspect, 
				errorHandler);
		new ATFPostProcessor<E>().process(grammar, 
				"String",
				new MetadataProvider(aspect), 
				writableAspect, 
				errorHandler);
	}
}
