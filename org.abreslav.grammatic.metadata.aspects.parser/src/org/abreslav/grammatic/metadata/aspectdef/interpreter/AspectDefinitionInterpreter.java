package org.abreslav.grammatic.metadata.aspectdef.interpreter;

import java.util.List;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.query.variables.ItemValue;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

public class AspectDefinitionInterpreter {
	
	public static IErrorHandler<RuntimeException> SYSERR = new IErrorHandler<RuntimeException>() {

		@Override
		public void reportError(String string, Object... objects)
				throws RuntimeException {
			System.err.format(string + "\n", objects);
			
		}
		
	};
	
	public static <E extends Throwable> void runDefinition(
			AspectDefinition definition,
			Grammar grammar,
			IMetadataProvider metadataProvider,
			final IWritableAspect writableAspect,
			IErrorHandler<E> errorHandler
	) throws E {
		AbstractAssignmentInterpreter.runDefinition(
				definition, 
				grammar, 
				metadataProvider, 
				new AssignmentHandler() {
					@Override
					public void caseGrammar(Grammar grammar,
							List<Attribute> assignment) {
						writeAttributes(writableAspect, grammar, assignment);
					}
					
					@Override
					public <T> INull caseItemValue(ItemValue<T> object,
							List<Attribute> data) {
						writeAttributes(writableAspect, (EObject) object.getItem(), data);
						return INull.NULL;
					}
				}, 
				errorHandler);
	} 
	
	private static void writeAttributes(
			final IWritableAspect writableAspect, EObject object,
			List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			Value originalValue = attribute.getValue();
			Value valueCopy = originalValue == null ? null : EMFProxyUtil.copy(originalValue);
			writableAspect.setAttribute(object, attribute.getNamespace(), attribute.getName(), valueCopy);
		}
	}
	
}
