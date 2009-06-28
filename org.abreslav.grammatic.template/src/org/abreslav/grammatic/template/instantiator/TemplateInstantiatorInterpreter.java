package org.abreslav.grammatic.template.instantiator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.emfutils.EMFCopier;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.emfutils.EMFCopier.ICopyHandler;
import org.abreslav.grammatic.template.ObjectContainer;
import org.abreslav.grammatic.template.ParameterReference;
import org.abreslav.grammatic.template.Template;
import org.abreslav.grammatic.template.TemplateApplication;
import org.abreslav.grammatic.template.TemplateArgument;
import org.abreslav.grammatic.template.TemplateBody;
import org.abreslav.grammatic.template.TemplateParameter;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

public class TemplateInstantiatorInterpreter {

	
	private final IMetadataResolver myMetadataResolver;
	private final IInstantiationHandler myInstantiationHandler;
	private final ISpecialTemplateInstantiator mySpecialTemplatesInstantiator;
	
	private final ICopyHandler myCopyHandler;

	public TemplateInstantiatorInterpreter() {
		this(IInstantiationHandler.DEFAULT, new DefaultMetadataResolver(), ISpecialTemplateInstantiator.DEFAULT, EMFCopier.DEFAULT_COPY_HANDLER);
	}
	
	public TemplateInstantiatorInterpreter(
			IInstantiationHandler instantiationHandler,
			IMetadataResolver metadataResolver,
			ISpecialTemplateInstantiator specialTemplatesInstantiator,
			ICopyHandler copyHandler) {
		myInstantiationHandler = instantiationHandler;
		myMetadataResolver = metadataResolver;
		mySpecialTemplatesInstantiator = specialTemplatesInstantiator;
		myCopyHandler = copyHandler;
	}

	public void addInstantiatorModel(InstantiatorModel model) {
		myMetadataResolver.addInstantiatorModel(model);
	}
	
	public void addTargetPackage(EPackage ePackage) {
		myMetadataResolver.addTargetPackage(ePackage);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EObject> Collection<T> instantiate(TemplateApplication<? extends T> templateApplication) {
		Template<? extends T> template = templateApplication.getTemplate();
		int paramCount = template.getParameters().size();
		int argCount = templateApplication.getArguments().size();
		if (paramCount != argCount) {
			throw new IllegalArgumentException(String.format(
					"Application of template '%s' has wrong number of arguments: expected %d but was %d", 
					template.getName(), paramCount, argCount));
		}
		TemplateBody<? extends T> body = template.getBody();
		TemplateInstantiatorContext context = new TemplateInstantiatorContext();
		for (TemplateArgument<?> templateArgument : templateApplication.getArguments()) {
			Collection<Object> valueIntances = new ArrayList<Object>();
			Collection values = templateArgument.getValues();
			for (Object object : values) {
				TemplateBody<EObject> tbody = (TemplateBody<EObject>) object;
				valueIntances.addAll(instantiateTemplateBody(tbody, context));
			}
			context.setValue(templateArgument.getParameter(), valueIntances);
		}
		return instantiateTemplateBody(body, context);
	}
	
	public <T extends EObject> Collection<T> instantiateTemplateBody(TemplateBody<? extends T> body) {
		return instantiateTemplateBody(body, new TemplateInstantiatorContext());
	}
	
	private <T extends EObject> Collection<T> instantiateTemplateBody(TemplateBody<? extends T> body, TemplateInstantiatorContext context) {
		if (body == null) {
			return null;
		}
		// Template parameter references
		if (body instanceof ParameterReference) {
			ParameterReference<? extends T> parameterReference = (ParameterReference<? extends T>) body;
			TemplateParameter<? extends T> parameter = parameterReference.getParameter();
			@SuppressWarnings("unchecked")
			Collection<T> value = (Collection<T>) context.getValue(parameter);
			switch (parameter.getUsagePolicy()) {
			// TODO: Check against these limitations
			case COPY:
				value = EMFProxyUtil.copyAll(value, myCopyHandler);
				break;
			case CONTAIN_ONCE:
				break;
			case REFER_ONLY:
				break;
			}
			for (T t : value) {
				myInstantiationHandler.handleCreatedObject(parameterReference, t);
			}
			return value;
		}
		// Template applications
		if (body instanceof TemplateApplication) {
			return instantiate((TemplateApplication<? extends T>) body);
		}
		// ObjectContainer
		if (body instanceof ObjectContainer) {
			ObjectContainer<? extends T> container = (ObjectContainer<? extends T>) body;
			@SuppressWarnings("unchecked")
			T copy = (T) EMFProxyUtil.copy(container.getObject(), myCopyHandler);
			myInstantiationHandler.handleCreatedObject(container, copy);
			return Collections.singletonList(copy);
			
		}
		// Ordinary TemplateBody instances
		ObjectInstantiator instantiator = myMetadataResolver.getObjectInstantiator(body);
		if (instantiator == null) {
			if (mySpecialTemplatesInstantiator.isSupported(body)) {
				Collection<T> result = mySpecialTemplatesInstantiator.instantiate(body);
				for (T t : result) {
					myInstantiationHandler.handleCreatedObject(body, t);
				}
				return result;
			}
			EClass eClass = body.eClass();
			throw new IllegalArgumentException("No instantiator for class " + eClass.getName() + " " + eClass.eResource().getURI() + " " + eClass);
		}
		
		EClass target = myMetadataResolver.resolveClass(instantiator.getTarget());
		@SuppressWarnings("unchecked")
		T result = (T) target.getEPackage().getEFactoryInstance().create(instantiator.getTarget());
		for (ReferenceInstantiator referenceInstantiator : instantiator.getReferenceInstantiators()) {
			instantiateReference(body, result, referenceInstantiator, context);
		}
		
		for (AttributeInstantiator attributeInstantiator : instantiator.getAttributeInstantiators()) {
			instantiateAttribute(body, result, attributeInstantiator);
		}
		myInstantiationHandler.handleCreatedObject(body, result);
		return Collections.singletonList(result);
	}

	private <T extends EObject> void instantiateAttribute(TemplateBody<? extends T> body, T result,
			AttributeInstantiator attributeInstantiator) {
		EAttribute source = myMetadataResolver.resolveAttribute(body.eClass(), attributeInstantiator.getSource());
		EAttribute target = myMetadataResolver.resolveAttribute(result.eClass(), attributeInstantiator.getTarget());
		Object value = body.eGet(source);
		result.eSet(target, value);
	}

	@SuppressWarnings("unchecked")
	private <T extends EObject> void instantiateReference(TemplateBody<? extends T> body, T result,
			ReferenceInstantiator referenceInstantiator, TemplateInstantiatorContext context) {
		EReference source = myMetadataResolver.resolveReference(body.eClass(), referenceInstantiator.getSource());
		EReference target = myMetadataResolver.resolveReference(result.eClass(), referenceInstantiator.getTarget());
		if (source.isMany()) {
			Collection values = (Collection) body.eGet(source);
			Collection targetCollection = (Collection) result.eGet(target);
			for (Object object : values) {
				Collection objects = instantiateTemplateBody((TemplateBody) object, context);
				targetCollection.addAll(objects);
			}
		} else {
			Object value = body.eGet(source);
			Collection object = instantiateTemplateBody((TemplateBody) value, context);
			result.eSet(target, getFirst(object));
		}
	}

	private static Object getFirst(Collection<Object> collection) {
		return collection == null 
			? null 
			: (collection.isEmpty() 
					? null 
					: collection.iterator().next());
	}
	
	private static class TemplateInstantiatorContext {
		private final Map<TemplateParameter<?>, Collection<?>> myValues = new HashMap<TemplateParameter<?>, Collection<?>>();
		
		public void setValue(TemplateParameter<?> parameter, Collection<Object> collection) {
			myValues.put(parameter, collection);
		}

		public Object getValue(TemplateParameter<?> parameter) {
			return myValues.get(parameter);
		}
	}

}


