/**
 * 
 */
package org.abreslav.grammatic.atf.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.ExpressionValue;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.template.parser.GrammaticGrammarTemplateBuilders;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar1.IGrammaticMetadataModuleImplementationProvider;
import org.abreslav.grammatic.metadata.Annotated;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.IntegerValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.template.TemplateBody;
import org.abreslav.grammatic.template.instantiator.TemplateInstantiatorInterpreter;
import org.eclipse.emf.ecore.EObject;

public class GrammaticMetadataModuleImplementationProvider implements
		IGrammaticMetadataModuleImplementationProvider {
	private final Map<String, Namespace> myNamespaces = new HashMap<String, Namespace>();
	private final TemplateInstantiatorInterpreter myInstantiator;
	private final IWritableAspect myWritableAspect;

	public GrammaticMetadataModuleImplementationProvider(
			IWritableAspect writableAspect,
			TemplateInstantiatorInterpreter instantiator) {
		myInstantiator = instantiator;
		myWritableAspect = writableAspect;
	}

	public GrammaticMetadataModuleImplementationProvider(IParsingContext parsingContext) {
		this(
			parsingContext.getWritableAspectForTemplates(), 
			parsingContext.getInstantiator(null)
		);
	}
	
	@Override
	public ITermFunctions getTermFunctions() {
		return new ITermFunctions() {
			
			@Override
			public Value createPunctuationValue(PunctuationType punctuation) {
				PunctuationValue value = MetadataFactory.eINSTANCE.createPunctuationValue();
				value.setType(punctuation);
				return value;
			}
		};
	}

	@Override
	public IPunctuationFunctions getPunctuationFunctions() {
		return new IPunctuationFunctions() {
			
			@Override
			public PunctuationType punctuationType(String textTokens) {
				PunctuationType v;
				switch (textTokens.charAt(0)) {
				case '`':
					v = PunctuationType.GRAVE_ACCENT;
					break;
				case '~':
					v = PunctuationType.TILDE;
					break;
				case '!':
					v = PunctuationType.EXCLAMATION;
					break;
				case '@':
					v = PunctuationType.AT;
					break;
				case '#':
					v = PunctuationType.HASH;
					break;
				case '$':
					v = PunctuationType.DOLLAR;
					break;
				case '%':
					v = PunctuationType.PERCENT;
					break;
				case '^':
					v = PunctuationType.CIRCUMFLEX;
					break;
				case '&':
					v = PunctuationType.AMPERSANT;
					break;
				case '*':
					v = PunctuationType.ASTERISK;
					break;
				case '(':
					v = PunctuationType.LEFT_BRACE;
					break;
				case ')':
					v = PunctuationType.RIGHT_BRACE;
					break;
				case '-':
					v = PunctuationType.MINUS;
					break;
				case '+':
					v = PunctuationType.PLUS;
					break;
				case '=':
					v = PunctuationType.EQUALS;
					break;
				case '|':
					v = PunctuationType.BAR;
					break;
				case '\\':
					v = PunctuationType.BACKSLASH;
					break;
				case '[':
					v = PunctuationType.LEFT_BRACKET;
					break;
				case ']':
					v = PunctuationType.RIGHT_BRACKET;
					break;
				case ';':
					v = PunctuationType.SEMICOLON;
					break;
				case ':':
					v = PunctuationType.COLON;
					break;
				case ',':
					v = PunctuationType.COMMA;
					break;
				case '.':
					v = PunctuationType.DOT;
					break;
				case '/':
					v = PunctuationType.SLASH;
					break;
				case '?':
					v = PunctuationType.QUESTION;
					break;
				case '<':
					v = PunctuationType.LESS;
					break;
				case '>':
					v = PunctuationType.GREATER;
					break;
				case '{':
					v = PunctuationType.LEFT_CURLY;
					break;
				case '}':
					v = PunctuationType.RIGHT_CURLY;
					break;
				default:
					throw new IllegalArgumentException();
				}
				return v;
			}
		};
	}

	@Override
	public INamespaceFunctions getNamespaceFunctions() {
		return new INamespaceFunctions() {
			
			@Override
			public Namespace createNamespace(String uri) {
				Namespace ns = myNamespaces.get(uri);
				if (ns == null) {
					ns = MetadataFactory.eINSTANCE.createNamespace();
					ns.setUri(uri);
					myNamespaces.put(uri, ns);
				}
				return ns;
			}
		};
	}

	@Override
	public ILocal getLocal() {
		return new ILocal() {
			
			@Override
			public Value valueNull() {
				return null;
			}
		};
	}

	@Override
	public IAttributeValueFunctions getAttributeValueFunctions() {
		return new IAttributeValueFunctions() {
			
			@Override
			public Value createStringValue(String string) {
				StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
				sv.setValue(string);
				return sv;
			}
			
			@Override
			public MultiValue createMultiValue() {
				return MetadataFactory.eINSTANCE.createMultiValue();
			}
			
			@Override
			public Value createIntValue(String int1) {
				IntegerValue intv = MetadataFactory.eINSTANCE.createIntegerValue();
				intv.setValue(Integer.valueOf(int1));
				return intv;
			}
			
			@Override
			public Value createIdValue(String name) {
				IdValue idv = MetadataFactory.eINSTANCE.createIdValue();
				idv.setId(name);
				return idv;
			}
			
			@Override
			public Value createCharacterValue(char character) {
				StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
				sv.setValue(character + "");
				return sv;
			}
			
			@Override
			public void addItem(MultiValue multi, Value term) {
				multi.getValues().add(term);
			}

			@Override
			public Value createExpressionValue(
					TemplateBody<? extends Expression> alternative) {
				ExpressionValue value = GrammarFactory.eINSTANCE.createExpressionValue();
				Collection<Expression> expression = myInstantiator.instantiateTemplateBody(alternative);
				value.setExpression(GrammaticGrammarTemplateBuilders.unwrap(expression));
				return value;
			}
		};
	}

	@Override
	public IAttributeListFunctions getAttributeListFunctions() {
		return new IAttributeListFunctions() {
			
			@Override
			public TupleValue createTupleValue() {
				return MetadataFactory.eINSTANCE.createTupleValue();
			}
		};
	}

	@Override
	public IAttributeInListFunctions getAttributeInListFunctions() {
		return new IAttributeInListFunctions() {
			
			@Override
			public void addAttribute(Annotated subject, Namespace namespace,
					String name, Value attributeValue) {
				Attribute attr = MetadataFactory.eINSTANCE.createAttribute();
				attr.setName(name);
				attr.setNamespace(namespace);
				attr.setValue(attributeValue);
				subject.getAttributes().add(attr);
			}
		};
	}

	@Override
	public IAttributeFunctions1 getAttributeFunctions1() {
		return new IAttributeFunctions1() {
			
			@Override
			public void writeAttribute(EObject subject, Namespace namespace,
					String name, Value attributeValue) {
				myWritableAspect.setAttribute(subject, namespace, name, attributeValue);
			}
		};
	}

	@Override
	public IAttributeFunctions getAttributeFunctions() {
		return new IAttributeFunctions() {
			
			@Override
			public Namespace nsNull() {
				return null;
			}
		};
	}
}