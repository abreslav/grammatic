package org.abreslav.grammatic.template.generator;

import org.abreslav.grammatic.template.TemplatePackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class EcoreToTemplateModels {

	public interface ITraceBuilder {
		void afterClassTemplate(EClass self, EClass template);
		void onReferenceTemplate(EClass selfClass, EClass resultClass, EReference self, EReference template);
		void onAttributeTemplate(EClass selfClass, EClass resultClass, EAttribute self, EAttribute template);
		void beforeClassTemplate(EClass self, EClass result);
	}
	private final ITraceBuilder myTraceBuilder;
	
	public EcoreToTemplateModels(ITraceBuilder traceBuilder) {
		myTraceBuilder = traceBuilder;
	}

	public EPackage generateTemplateEcore(
			EPackage source
		) {
		return toTemplatePackage(source);
	}
	
	/*
	 * mapping ecore::EPackage::toTemplatePackage() : ecore::EPackage {
	 * 	name := self.name + 'Template';
	 * 	nsURI := self.nsURI + 'Template';
	 * 	nsPrefix := self.nsPrefix + '-template';
	 * 	eSubpackages += self.eSubpackages.map toTemplatePackage();
	 * 	eClassifiers += self.eClassifiers[EClass].map toTemplateClass();	
	 * }	
	 */
	private EPackage toTemplatePackage(EPackage self) {
		EPackage result = EcoreFactory.eINSTANCE.createEPackage();
		result.setName(self.getName() + "Template");
		result.setNsURI(self.getNsURI() + "Template");
		result.setNsPrefix(self.getNsPrefix() + "-template");
		for (EPackage subpackage : self.getESubpackages()) {
			result.getESubpackages().add(toTemplatePackage(subpackage));
		}
		for (EClassifier classifier : self.getEClassifiers()) {
			if (classifier instanceof EClass) {
				result.getEClassifiers().add(toTemplateClass((EClass) classifier));
			}
		}
		return result;
	}

	/*
	 * mapping EClass::toTemplateClass() : EClass {
	 * 	name := self.name + 'Template'; 
	 * 	_abstract := self._abstract;
	 * 	interface := self.interface;
	 * 	eGenericSuperTypes += TemplateBody(self);
	 * 	eStructuralFeatures += self.eAllAttributes.clone().oclAsType(EStructuralFeature);
	 * 	eStructuralFeatures += self.eAllReferences.toTemplateReference();
	 * 	eAnnotations += object EAnnotation {
	 * 		source := 'http://www.eclipse.org/emf/2002/Ecore';
	 * 		details += object EStringToStringMapEntry {
	 * 			key := 'constraints';
	 * 			value := 'subtemplatesConformToSpec';
	 * 		};
	 * 	};
	 * }
	 */
	private EClassifier toTemplateClass(EClass self) {
		EClass result = EcoreFactory.eINSTANCE.createEClass();
		myTraceBuilder.beforeClassTemplate(self, result);
		result.setName(self.getName() + "Template");
		result.setAbstract(self.isAbstract());
		result.setInterface(self.isInterface());
		result.getEGenericSuperTypes().add(TemplateBody(self));
		for (EAttribute attribute : self.getEAllAttributes()) {
			EAttribute copy = (EAttribute) EcoreUtil.copy(attribute);
			result.getEStructuralFeatures().add(copy);
			myTraceBuilder.onAttributeTemplate(self, result, attribute, copy);
		}
		EList<EReference> allReferences = self.getEAllReferences();
		for (EReference reference : allReferences) {
			EReference copy = toTemplateReference(reference);
			result.getEStructuralFeatures().add(copy);
			myTraceBuilder.onReferenceTemplate(self, result, reference, copy);
		}
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(EcorePackage.eNS_URI);
		annotation.getDetails().put("constraints", "subtemplatesConformToSpec");
		result.getEAnnotations().add(annotation);
		myTraceBuilder.afterClassTemplate(self, result);
		return result;
	}
	
	/*
	 * query EReference::toTemplateReference() : EReference {
	 * 	var r := self.clone().oclAsType(EReference);
	 * 	r.eGenericType := TemplateBodyBounded(self.eReferenceType);
	 * 	return r;
	 * }
	 */
	private EReference toTemplateReference(EReference self) {
		EReference copy = (EReference) EcoreUtil.copy(self);
		copy.setEGenericType(TemplateBodyBounded(self.getEReferenceType()));
		return copy;
	}

	/*
	 * query TemplateBodyBounded(in class : EClass) : EGenericType {
	 * 	return TemplateBody.oclAsType(EClass).toBoundedGenericType(class);
	 * }
	 */
	private EGenericType TemplateBodyBounded(EClass clazz) {
		return toBoundedGenericType(TemplatePackage.eINSTANCE.getTemplateBody(), clazz);
	}

	/*
	 * query EClass::toBoundedGenericType(in argument : EClass) : EGenericType {
	 * 	return object EGenericType {
	 * 		eClassifier := self;
	 * 		eTypeArguments += object EGenericType {
	 * 			eUpperBound := object EGenericType {
	 * 				eClassifier := argument;
	 * 			};
	 * 		};
	 * 	}
	 * }
	 */
	private EGenericType toBoundedGenericType(EClass self, EClass argument) {
		EGenericType result = wrapClassifier(self);
		EGenericType arg = EcoreFactory.eINSTANCE.createEGenericType();
		arg.setEUpperBound(wrapClassifier(argument));
		result.getETypeArguments().add(arg);
		return result;
	}

	/*
	 * query TemplateBody(in class : EClass) : EGenericType {
	 * 	return TemplateBody.oclAsType(EClass).toGenericType(class);
	 * }
	 */
	private EGenericType TemplateBody(EClass self) {
		return toGenericType(TemplatePackage.eINSTANCE.getTemplateBody(), self);
	}

	/*
	 * query EClass::toGenericType(in argument : EClass) : EGenericType {
	 * 	return object EGenericType {
	 * 		eClassifier := self;
	 * 		eTypeArguments += object EGenericType {
	 * 			eClassifier := argument;
	 * 		};
	 * 	}
	 * }
	 */
	private EGenericType toGenericType(EClass self, EClass argument) {
		EGenericType result = wrapClassifier(self);
		result.getETypeArguments().add(wrapClassifier(argument));
		return result;
	}

	private EGenericType wrapClassifier(EClass argument) {
		EGenericType arg = EcoreFactory.eINSTANCE.createEGenericType();
		arg.setEClassifier(argument);
		return arg;
	}

}
