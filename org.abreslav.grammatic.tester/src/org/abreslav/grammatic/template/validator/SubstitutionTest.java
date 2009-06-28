package org.abreslav.grammatic.template.validator;

import static org.junit.Assert.assertEquals;

import org.abreslav.grammatic.template.TemplatePackage;
import org.abreslav.grammatic.template.util.GenericTypeRenderer;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

public class SubstitutionTest {

	private static final EGenericType GT_ECLASS = gt(EcorePackage.eINSTANCE.getEClass());
	private static final EClass TEMPLATE = TemplatePackage.eINSTANCE.getTemplate();
	private static final ETypeParameter TEMPLATE_T = TEMPLATE.getETypeParameters().get(0);
	private static final ETypeParameter TEMPLATE_ARGUMENT_T = TemplatePackage.eINSTANCE.getTemplateArgument().getETypeParameters().get(0);
	private final ISubstitution mySubstitution = new Substitution();
	
	private static EGenericType gt(EClass class1) {
		EGenericType result = EcoreFactory.eINSTANCE.createEGenericType();
		result.setEClassifier(class1);
		return result;
	}

	@Test
	public void testGetGenericType_EmptySubst() {
		EGenericType template = mySubstitution.getGenericType(TEMPLATE);
		String toString = GenericTypeRenderer.render(template);
		assertEquals(toString, "Template<?>", toString);
	}
	
	@Test
	public void testGetGenericType_NonEmptySubst() {
		mySubstitution.addSubstitution(TEMPLATE_T, GT_ECLASS);
		EGenericType template = mySubstitution.getGenericType(TEMPLATE);
		String toString = GenericTypeRenderer.render(template);
		assertEquals(toString, "Template<EClass>", toString);
	}

	@Test
	public void testGetUnsubstitutedParameters_Empty() {
		assertEquals(0, mySubstitution.getUnsubstitutedParameters().size());
	}

	@Test
	public void testGetUnsubstitutedParameters_NonEmpty() {
		mySubstitution.addTypeParameter(TEMPLATE_T);
		assertEquals(1, mySubstitution.getUnsubstitutedParameters().size());

		mySubstitution.addSubstitution(TEMPLATE_T, GT_ECLASS);
		assertEquals(0, mySubstitution.getUnsubstitutedParameters().size());
	}
	
	@Test
	public void testApplyTo() {
		mySubstitution.addSubstitution(TEMPLATE_ARGUMENT_T, GT_ECLASS);
		EGenericType genericType = mySubstitution.applyTo(TemplatePackage.eINSTANCE.getTemplateArgument_Values().getEGenericType());
		String toString = GenericTypeRenderer.render(genericType);
		assertEquals(toString, "TemplateBody<? extends EClass>", toString);
	}

}

