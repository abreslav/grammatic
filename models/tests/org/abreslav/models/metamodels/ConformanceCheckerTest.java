package org.abreslav.models.metamodels;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.abreslav.TestUtils;
import org.abreslav.models.SetValue;
import org.abreslav.models.wellformedness.CompositeContext;
import org.abreslav.models.wellformedness.Context;
import org.abreslav.models.wellformedness.IContext;
import org.abreslav.models.wellformedness.WellFormednessChecker;
import org.abreslav.models.xml.XMLParser;

import java.io.File;
import java.util.Collection;

/**
 * @author abreslav
 */
public class ConformanceCheckerTest extends TestCase {

    private final String testDir;

    public ConformanceCheckerTest(String name, String testDir) {
        super(name);
        this.testDir = testDir;
    }

    @Override
    protected void runTest() throws Throwable {
        SetValue mmm = TestUtils.loadMetaMetaModel();

        String mmXmlFileName = testDir + "/mm.xml";
        File mmFile = new File(mmXmlFileName);
        mmFile.delete();
        TestUtils.termToXml(testDir + "/mm.trm", mmXmlFileName);
        SetValue mm = new XMLParser().parse(mmFile);
        IContext mmmContext = new Context(mmm);
        WellFormednessChecker.INSTANCE.checkWellFormedness(mmmContext, mm.getValue());

        String mXmlFileName = testDir + "/m.xml";
        File mFile = new File(mXmlFileName);
        mFile.delete();
        TestUtils.termToXml(testDir + "/m.trm", mXmlFileName);
        SetValue m = new XMLParser().parse(mFile);
        WellFormednessChecker.INSTANCE.checkWellFormedness(new CompositeContext(mmmContext, new Context(mm)), m.getValue());

        String mmCheck = check(mm);
        TestUtils.assertStringEqualsToFile(testDir, mmCheck, "expected.mm");

        String mCheck = check(m);
        TestUtils.assertStringEqualsToFile(testDir, mCheck, "expected.m");

        mFile.delete();
        mmFile.delete();
    }

    private String check(SetValue mm) {
        Collection<IDiagnostic> result = ConformanceChecker.check(mm.getValue());
        StringBuilder mmCheck = new StringBuilder();
        for (IDiagnostic iDiagnostic : result) {
            mmCheck.append(iDiagnostic).append("\n");
        }
        return mmCheck.toString();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ConformanceCheckerTest.class.getSimpleName());

        for (File file : new File ("testData/conformance").listFiles()) {
            if (file.isDirectory()) {
                suite.addTest(new ConformanceCheckerTest(file.getName(), file.getAbsolutePath()));
            }
        }

        return suite;
    }
}
