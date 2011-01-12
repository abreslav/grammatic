package org.abreslav.models.metamodels;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.abreslav.TestUtils;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.SetValue;
import org.abreslav.models.wellformedness.Context;
import org.abreslav.models.wellformedness.WellFormednessChecker;
import org.abreslav.models.xml.XMLParser;

import java.io.File;

/**
 * @author abreslav
 */
public class MetamodelLoadingTest extends TestCase {

    private final String testDir;

    public MetamodelLoadingTest(String name, String testDir) {
        super(name);
        this.testDir = testDir;
    }

    @Override
    protected void runTest() throws Throwable {

        String xmlFileName = testDir + "/mm.xml";
        new File(xmlFileName).delete();
        TestUtils.termToXml(testDir + "/mm.trm", xmlFileName);
        SetValue mmm = TestUtils.loadMetaMetaModel();
        SetValue mm = new XMLParser().parse(new File(xmlFileName));
        WellFormednessChecker.INSTANCE.checkWellFormedness(new Context(mmm), mm.getValue());

        Appendable out = new StringBuilder();
        ClassPrinter classPrinter = new ClassPrinter(out);
        for (IValue value : mm.getValue()) {
            classPrinter.printClass(new ModelClass((ObjectValue) value));
        }
        TestUtils.assertStringEqualsToFile(testDir, out.toString(), "expected.txt");
        new File(xmlFileName).delete();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MetamodelLoadingTest.class.getSimpleName());
        for (File file : new File("testData/metamodelLoading").listFiles()) {
            if (file.isDirectory() && !"mmm".equals(file.getName())) {
                suite.addTest(new MetamodelLoadingTest(file.getName(), file.getAbsolutePath()));
            }
        }
        return suite;
    }

}
