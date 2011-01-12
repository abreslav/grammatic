package org.abreslav.templates;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.abreslav.TestUtils;
import org.abreslav.lambda.ITerm;
import org.abreslav.models.SetValue;
import org.abreslav.models.wellformedness.CompositeContext;
import org.abreslav.models.wellformedness.Context;
import org.abreslav.models.wellformedness.IContext;
import org.abreslav.models.wellformedness.WellFormednessChecker;
import org.abreslav.models.xml.XMLParser;
import org.abreslav.templates.lambda.TemplateTerm;

import java.io.File;
import java.io.IOException;

import static org.abreslav.TestUtils.termToXml;

/**
 * @author abreslav
 */
public class TemplateInstantiatorTest extends TestCase {

    private final String mmDir;
    private final String testDir;

    public TemplateInstantiatorTest(String name, String mmDir, String testDir) {
        super(name);
        this.mmDir = mmDir;
        this.testDir = testDir;
    }

    @Override
    protected void runTest() throws Throwable {
        String definitionsFileName = testDir + "/definitions.xml";
        new File(definitionsFileName).delete();
        termToXml(testDir + "/definitions.trm", definitionsFileName);

        String usageFileName = testDir + "/usage.xml";
        new File(usageFileName).delete();
        termToXml(testDir + "/usage.trm", usageFileName);

        SetValue mmm = TestUtils.loadMetaMetaModel();
        SetValue templateMM = new XMLParser().parse(new File(mmDir + "/templateMM.xml"));
        SetValue templateDefTest = new XMLParser().parse(new File(definitionsFileName));
        SetValue templateUsageTest = new XMLParser().parse(new File(usageFileName));

//        System.out.println(mmm);
//        System.out.println(templateMM);
//        System.out.println(templateDefTest);
//        System.out.println(templateUsageTest);

        Context mmmContext = new Context(mmm);
        WellFormednessChecker.INSTANCE.checkWellFormedness(mmmContext, templateMM.getValue());
        IContext mmContext = new CompositeContext(mmmContext, new Context(templateMM));
        WellFormednessChecker.INSTANCE.checkWellFormedness(mmContext, templateDefTest.getValue());
        IContext defContext = new CompositeContext(mmContext, new Context(templateDefTest));
        WellFormednessChecker.INSTANCE.checkWellFormedness(defContext, templateUsageTest.getValue());

        ITerm term = TemplateInstantiator.INSTANCE.instantiate(ITemplateContext.EMPTY, new TemplateTerm(templateUsageTest));
        String result = ((TemplateTerm) term).getValue().toString();

        TestUtils.assertStringEqualsToFile(testDir, result, "expected.trm");
        new File(usageFileName).delete();
        new File(definitionsFileName).delete();
    }

    public static Test suite() {
        String mmDir = "testData/templateInstantiation";
        generateTemplateMetaModelXml(mmDir);

        TestSuite suite = new TestSuite(TemplateInstantiatorTest.class.getSimpleName());

        for (File dir : new File(mmDir).listFiles()) {
            if (dir.isDirectory()) {
                suite.addTest(new TemplateInstantiatorTest(dir.getName(), mmDir, dir.getAbsolutePath()));
            }
        }

        return suite;
    }

    private static void generateTemplateMetaModelXml(String mmDir) {
        try {
            String tmmXml = mmDir + "/templateMM.xml";
            new File(tmmXml).delete();
            termToXml(mmDir + "/templateMM.trm", tmmXml);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
