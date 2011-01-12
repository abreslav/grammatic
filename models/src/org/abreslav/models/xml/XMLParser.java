package org.abreslav.models.xml;

import org.abreslav.models.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author abreslav
 */
public class XMLParser {

    private int identityValue = 0;
    private String fileUID;

    public SetValue parse(File file) throws JDOMException, IOException {
        fileUID = file.getName();
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(file);

        Element model = document.getRootElement();
        if (!"Model".equals(model.getName())) {
            throw new IllegalArgumentException("Root must be <Model>");
        }

        Set<IValue> modelElements = new LinkedHashSet<IValue>();
        parseChildValues(model, modelElements);

        return new SetValue(modelElements);
    }

    private void parseChildValues(Element element, Collection<IValue> values) {
        @SuppressWarnings({"unchecked"})
        List<Element> children = element.getChildren();
        for (Element child : children) {
            values.add(parseValue(child));
        }
    }

    private IValue parseValue(Element element) {
        if ("Boolean".equals(element.getName())) {
            if (element.getChild("true") != null) {
                return BooleanValue.TRUE;
            } else if (element.getChild("false") != null) {
                return BooleanValue.FALSE;
            }
            throw new IllegalArgumentException("Expecting <true/> or <false/>");
        } else if ("Integer".equals(element.getName())) {
            String textTrim = element.getTextTrim();
            return new IntegerValue(Integer.parseInt(textTrim));
        } else if ("List".equals(element.getName())) {
            ArrayList<IValue> values = new ArrayList<IValue>();
            parseChildValues(element, values);
            return new ListValue(values);
        } else if ("Null".equals(element.getName())) {
            return NullValue.NULL;
        } else if ("Object".equals(element.getName())) {
            Element classIdElement = element.getChild("ClassReference");
            if (classIdElement == null) {
                throw new IllegalArgumentException("No class reference");
            }
            Element reference = classIdElement.getChild("Reference");
            if (reference == null) {
                throw new IllegalArgumentException("Reference expected");
            }
            ReferenceValue classReference = (ReferenceValue) parseValue(reference);

            Element objectIdElement = element.getChild("ObjectID");
            IValue objectId;
            if (objectIdElement == null) {
                objectId = new ListValue(new StringValue(fileUID), new IntegerValue(identityValue++));
            } else {
                List children = objectIdElement.getChildren();
                if (children.size() != 1) {
                    throw new IllegalArgumentException("Exactly one child expected inside ObjectID");
                }
                objectId = parseValue((Element) children.iterator().next());
            }

            ObjectValue object = new ObjectValue(
                    classReference,
                    objectId);

            @SuppressWarnings({"unchecked"})
            List<Element> features = element.getChildren("Feature");
            for (Element feature : features) {
                List children = feature.getChildren();
                if (children.size() != 2) {
                    throw new IllegalArgumentException("Exactly two children expected for a Feature");
                }
                Iterator iterator = children.iterator();
                IValue name = parseValue((Element) iterator.next());
                IValue value = parseValue((Element) iterator.next());
                object.setProperty(name, value);
            }

            return object;
        } else if ("Reference".equals(element.getName())) {
            List children = element.getChildren();
            if (children.size() != 1) {
                throw new IllegalArgumentException("Exactly one child expected for a Reference");
            }
            return new ReferenceValue(parseValue((Element) children.iterator().next()));
        } else if ("Set".equals(element.getName())) {
            Set<IValue> values = new LinkedHashSet<IValue>();
            parseChildValues(element, values);
            return new SetValue(values);
        } else if ("String".equals(element.getName())) {
            String textTrim = element.getTextTrim();
            return new StringValue(textTrim.substring(1, textTrim.length() - 1));
        } else if ("Id".equals(element.getName())) {
            return new StringValue(element.getTextTrim());
        } else {
            throw new IllegalArgumentException("Unknown tag: " + element.getName());
        }
    }
}
