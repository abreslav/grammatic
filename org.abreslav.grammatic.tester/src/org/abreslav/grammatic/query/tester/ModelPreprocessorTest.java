package org.abreslav.grammatic.query.tester;

import static org.abreslav.grammatic.testing.Utils.characterRange;
import static org.abreslav.grammatic.testing.Utils.empty;
import static org.abreslav.grammatic.testing.Utils.grammar;
import static org.abreslav.grammatic.testing.Utils.symbol;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.metadata.system.ISystemMetadata;
import org.abreslav.grammatic.query.interpreter.IModelPreprocessor;
import org.abreslav.grammatic.query.interpreter.ModelPreprocessor;
import org.junit.Test;


public class ModelPreprocessorTest {

	@Test
	public void testAllAttributes() throws Exception {
		Symbol a = symbol("a", characterRange(0, 1));
		Symbol b = symbol("b", empty());
		Grammar grammar = grammar(a, b);
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		IModelPreprocessor modelPreprocessor = new ModelPreprocessor(writableAspect);
		modelPreprocessor.attachSystemMetadata(grammar);
		MetadataProvider metadataProvider = new MetadataProvider(aspect);
		checkSymbol(a, true, ISystemMetadata.SYMBOL_TYPE_TERMINAL, metadataProvider);
		checkSymbol(b, false, ISystemMetadata.SYMBOL_TYPE_NONTERMINAL, metadataProvider);
	}

	private void checkSymbol(Symbol a, boolean startingValue,
			String symbolTypeTerminal, IMetadataProvider metadataProvider) {
		Set<Attribute> attributes = metadataProvider.getAttributes(a);
		boolean name = false;
		boolean type = false;
		for (Attribute attribute : attributes) {
			assertSame(ISystemMetadata.SYSTEM_NAMESPACE, attribute.getNamespace());
			if (ISystemMetadata.SYMBOL_NAME.equals(attribute.getName())) {
				assertEquals(a.getName(), ((StringValue) attribute.getValue()).getValue());
				name = true;
			} else if (ISystemMetadata.SYMBOL_TYPE.equals(attribute.getName())) {
				assertEquals(symbolTypeTerminal, ((StringValue) attribute.getValue()).getValue());
				type = true;
			} else if (ISystemMetadata.SYMBOL_STARTING.equals(attribute.getName())) {
				assertTrue(startingValue);
			} else {
				fail("Unexpected attribute");
			}
		}
		assertTrue("No name attribute: " + attributes, name);
		assertTrue("No type attribute", type);
	}
}
