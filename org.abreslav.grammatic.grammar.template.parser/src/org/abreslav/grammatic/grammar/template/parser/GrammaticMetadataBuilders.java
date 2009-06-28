package org.abreslav.grammatic.grammar.template.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.ExpressionValue;
import org.abreslav.grammatic.grammar.GrammarFactory;
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

public class GrammaticMetadataBuilders implements
		IGrammaticMetadataBuilders {

	private final IWritableAspect myWritableAspect;
	private final Map<String, Namespace> myNamespaces = new HashMap<String, Namespace>();
	private final TemplateInstantiatorInterpreter myInstantiator;

	public GrammaticMetadataBuilders(
			IWritableAspect writableAspect,
			TemplateInstantiatorInterpreter instantiator) {
		myInstantiator = instantiator;
		myWritableAspect = writableAspect;
	}

	public GrammaticMetadataBuilders(IParsingContext parsingContext) {
		this(
			parsingContext.getWritableAspectForTemplates(), 
			parsingContext.getInstantiator(null)
		);
	}

	public Collection<Namespace> getNamespaces() {
		return myNamespaces.values();
	}

	@Override
	public IAttributeListBuilder getAttributeListBuilder() {
		return new IAttributeListBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public TupleValue createResult() {
				return MetadataFactory.eINSTANCE.createTupleValue();
			}
			
		};
	}

	@Override
	public IAttributeBuilder getAttributeBuilder() {
		return new IAttributeBuilder() {
			
			private EObject mySubject;

			@Override
			public void init(EObject subject) {
				mySubject = subject;
			}
			
			@Override
			public void release() {
				mySubject = null;
			}

			@Override
			public void attribute(Namespace namespace, String name,
					Value attributeValue) {
				myWritableAspect.setAttribute(mySubject, namespace, name, attributeValue);
			}
			
		};
	}

	@Override
	public IAttributeInListBuilder getAttributeInListBuilder() {
		
		return new IAttributeInListBuilder() {

			private Annotated mySubject;

			@Override
			public void init(Annotated subject) {
				mySubject = subject;
			}
			
			@Override
			public void release() {
				mySubject = null;
			}
			
			@Override
			public void attributeInList(Namespace namespace,
					String name, Value attributeValue) {
				Attribute attr = MetadataFactory.eINSTANCE.createAttribute();
				attr.setName(name);
				attr.setNamespace(namespace);
				attr.setValue(attributeValue);
				mySubject.getAttributes().add(attr);
			}
			
		};
	}

	@Override
	public IAttributeValueBuilder getAttributeValueBuilder() {
		return new IAttributeValueBuilder() {

			private MultiValue myMultiValue;
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				myMultiValue = null;
			}
			
			@Override
			public Value attributeList(TupleValue attributeList) {
				return attributeList;
			}

			@Override
			public Value character(char character) {
				StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
				sv.setValue(character + "");
				return sv;
			}

			@Override
			public Value int_(String int_) {
				IntegerValue intv = MetadataFactory.eINSTANCE.createIntegerValue();
				intv.setValue(Integer.valueOf(int_));
				return intv;
			}

			@Override
			public Value name(String name) {
				IdValue idv = MetadataFactory.eINSTANCE.createIdValue();
				idv.setId(name);
				return idv;
			}

			@Override
			public Value string(String string) {
				StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
				sv.setValue(string.substring(1, string.length() - 1));
				return sv;
			}

			private MultiValue getMultiValue() {
				if (myMultiValue == null) {
					myMultiValue = MetadataFactory.eINSTANCE.createMultiValue();
				}
				return myMultiValue;
			}
			
			@Override
			public void term(Value term) {
				getMultiValue().getValues().add(term);
			}

			@Override
			public Value getResult() {
				return getMultiValue();
			}

			@Override
			public Value alternative(
					TemplateBody<? extends Expression> alternative) {
				ExpressionValue value = GrammarFactory.eINSTANCE.createExpressionValue();
				Collection<Expression> expression = myInstantiator.instantiateTemplateBody(alternative);
				value.setExpression(GrammaticGrammarTemplateBuilders.unwrap(expression));
				return value;
			}
			
		};
	}

	@Override
	public INamespaceBuilder getNamespaceBuilder() {
		return new INamespaceBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public Namespace namespace(String uri) {
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
	public IPunctuationBuilder getPunctuationBuilder() {
		return new IPunctuationBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public PunctuationType punctuation(String token) {
				PunctuationType v;
				switch (token.charAt(0)) {
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
	public ITermBuilder getTermBuilder() {
		return new ITermBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public Value attributeValue(Value attributeValue) {
				return attributeValue;
			}

			@Override
			public Value punctuation(PunctuationType punctuation) {
				PunctuationValue pv = MetadataFactory.eINSTANCE.createPunctuationValue();
				pv.setType(punctuation);
				return pv;
			}
			
		};
	}
}
