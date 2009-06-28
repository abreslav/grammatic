//package org.abreslav.grammatic.transformation.stringtemplate.tester;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//import org.abreslav.grammatic.grammar.Grammar;
//import org.abreslav.grammatic.grammar.antlr.parser.GrammarParser;
//import org.abreslav.grammatic.grammar.antlr.parser.ImportManager;
//import org.abreslav.grammatic.metadata.Namespace;
//import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
//import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
//import org.abreslav.grammatic.query.Query;
//import org.abreslav.grammatic.query.QueryContainer;
//import org.abreslav.grammatic.query.VariableDefinition;
//import org.abreslav.grammatic.query.interpreter.IMatchHandler;
//import org.abreslav.grammatic.query.interpreter.Matchers;
//import org.abreslav.grammatic.query.interpreter.VariableValues;
//import org.abreslav.grammatic.query.variables.ItemValue;
//import org.abreslav.grammatic.query.variables.ListValue;
//import org.abreslav.grammatic.query.variables.util.VariablesSwitch;
//import org.abreslav.grammatic.transformation.stringtemplate.parser.TransformationParser;
//import org.antlr.stringtemplate.StringTemplate;
//import org.eclipse.emf.common.util.EList;
//
//
//public class StringTemplateTransformationTest {
//
////	@Test
//	public void testTransformation() throws Exception {
//		
//		FileInputStream grammarInput = new FileInputStream("testData/stTransformation/test.grammar");
//		Collection<Namespace> namespaces = new ArrayList<Namespace>();
//		Grammar grammar = GrammarParser.parseGrammar(grammarInput, namespaces, new ImportManager(new File("testData/stTransformation"), IWritableAspect.NONE), IWritableAspect.NONE);
//		FileInputStream input = new FileInputStream("testData/stTransformation/test.trans");
//		final Map<QueryContainer<? extends Query>, StringTemplate> transformation = TransformationParser.parseTransformation(input);
//		
//		final ValueUnwrapper valueUnwrapper = new ValueUnwrapper();
//		new Matchers(IMetadataProvider.EMPTY).getGrammarTraverser().traverseGrammar(grammar, transformation.keySet(), new IMatchHandler() {
//
//			@Override
//			public boolean queryMatched(QueryContainer<? extends Query> query,
//					VariableValues variableValues) {
//				StringTemplate stringTemplate = transformation.get(query);
//				EList<VariableDefinition> variableDefinitions = query.getVariableDefinitions();
//
//				@SuppressWarnings("unchecked")
//				Map attributes = stringTemplate.getAttributes();
//				if (attributes != null) {
//					attributes.clear();
//				}
//				
//				for (VariableDefinition variableDefinition : variableDefinitions) {
//					stringTemplate.setAttribute(
//							variableDefinition.getName(),
//							valueUnwrapper.doSwitch(variableValues.getVariableValue(variableDefinition), null)
//					);
//				}
//	
//				stringTemplate.toString();
////				System.out.println(stringTemplate);
//				return true;
//			}
//			
//		});
//	}
//	
//	private static class ValueUnwrapper extends VariablesSwitch<Object, Void> {
//		@Override
//		public <T> Object caseItemValue(ItemValue<T> object, Void data) {
//			return object.getItem();
//		}
//
//		@Override
//		public <T> Object caseListValue(ListValue<T> object, Void data) {
//			return object.getItems();
//		}
//	}
//	
//}
