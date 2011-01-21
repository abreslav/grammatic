package org.abreslav.models.paths;

import junit.framework.TestCase;
import org.abreslav.TestUtils;
import org.abreslav.models.*;

import java.util.*;

/**
 * @author abreslav
 */
public class PathInterpreterTest extends TestCase {
    private final String testDir = "testData/pathInterpreter";
    private final String testFile = "data.trm";
    private SetValue model;

    @Override
    public void setUp() throws Exception {
        this.model = TestUtils.parseTerm(testDir + "/" + testFile);
    }

    public void testEmptyPath() throws Exception {
        IValue value = ModelPathInterpreter.INSTANCE.getValueByPath(model, Collections.<IModelPathEntry>emptyList());
        assertEquals(model, value);
    }

    public void testOneStepPathToItem() throws Exception {
        int i = 0;
        for (Iterator<IValue> iterator = model.getValue().iterator(); iterator.hasNext();) {
            IValue value = iterator.next();
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model, new ModelPath(new CollectionItemPathEntry(i)));
            assertEquals("Index: " + i, value, result);
            i++;
        }
    }

    public void testOneStepPathToRange() throws Exception {
        ArrayList<IValue> values = new ArrayList<IValue>(model.getValue());
        for (int width = 0; width < model.getValue().size(); width++) {
            for (int start = 0; start < model.getValue().size() - width; start++) {
                int end = start + width;
                IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model, new ModelPath(new CollectionRangePathEntry(start, end)));
                assertEquals("start: " + start + " width: " + width,
                        values.subList(start, end + 1), new ArrayList<IValue>(((ICollectionValue) result).getValue()));
            }
        }
    }

    public void testOneStepPathToProperty() throws Exception {
        ObjectValue value = (ObjectValue) ModelPathInterpreter.INSTANCE.getValueByPath(model,
                new ModelPath(new CollectionItemPathEntry(8)));
        for (Map.Entry<IValue, IValue> entry : value.getProperties()) {
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(value,
                    new ModelPath(new PropertyPathEntry(entry.getKey())));
            assertEquals("Property: " + entry.getKey(), entry.getValue(), result);
        }
    }

    public void testTwoStepPathToProperty() throws Exception {
        ObjectValue value = (ObjectValue) ModelPathInterpreter.INSTANCE.getValueByPath(model,
                new ModelPath(new CollectionItemPathEntry(8)));
        for (Map.Entry<IValue, IValue> entry : value.getProperties()) {
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                    new ModelPath(new CollectionItemPathEntry(8),
                            new PropertyPathEntry(entry.getKey())
                    ));
            assertEquals("Property: " + entry.getKey(), entry.getValue(), result);
        }
    }

    public void testMultiStepPathToProperty() throws Exception {
        IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                new ModelPath(new CollectionItemPathEntry(8),
                        new PropertyPathEntry(new StringValue("set")),
                        new CollectionItemPathEntry(0),
                        new PropertyPathEntry(new StringValue("a"))
                ));
        assertEquals(new IntegerValue(1), result);
        result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                new ModelPath(new CollectionItemPathEntry(8),
                        new PropertyPathEntry(new StringValue("set")),
                        new CollectionItemPathEntry(1)
                ));
        assertEquals(new IntegerValue(3), result);
    }

    public void testPathWithError() throws Exception {
        try {
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                    new ModelPath(new CollectionItemPathEntry(9)
                    ));
            fail();
        } catch (IllegalArgumentException e) {/* OK */}

        try {
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                    new ModelPath(new CollectionItemPathEntry(8),
                            new PropertyPathEntry(new StringValue("set1"))
                    ));
            fail();
        } catch (IllegalArgumentException e) {/* OK */}

        try {
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                    new ModelPath(new CollectionItemPathEntry(8),
                            new PropertyPathEntry(new StringValue("set")),
                            new CollectionItemPathEntry(-1),
                            new PropertyPathEntry(new StringValue("a"))
                    ));
            fail();
        } catch (IllegalArgumentException e) {/* OK */}

        try {
            IValue result = ModelPathInterpreter.INSTANCE.getValueByPath(model,
                    new ModelPath(new CollectionItemPathEntry(8),
                            new PropertyPathEntry(new StringValue("set")),
                            new CollectionRangePathEntry(0, 1),
                            new PropertyPathEntry(new StringValue("a"))
                    ));
            fail();
        } catch (IllegalArgumentException e) {/* OK */}
    }
}
