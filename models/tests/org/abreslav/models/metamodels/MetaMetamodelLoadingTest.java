package org.abreslav.models.metamodels;

import junit.framework.TestCase;
import org.abreslav.TestUtils;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.SetValue;

/**
 * @author abreslav
 */
public class MetaMetamodelLoadingTest extends TestCase {

    public void test() throws Throwable {
        SetValue mmm = TestUtils.loadMetaMetaModel();
        Appendable out = new StringBuilder();
        ClassPrinter classPrinter = new ClassPrinter(out);
        for (IValue value : mmm.getValue()) {
            classPrinter.printClass(new ModelClass((ObjectValue) value));
        }
        TestUtils.assertStringEqualsToFile("testData/metamodelLoading/mmm", out.toString(), "expected.txt");
    }
}
