import junit.framework.Test;
import junit.framework.TestSuite;
import org.abreslav.models.metamodels.MetaMetamodelLoadingTest;
import org.abreslav.models.metamodels.MetamodelLoadingTest;
import org.abreslav.templates.TemplateInstantiatorTest;

import static org.abreslav.TestUtils.generateParseTable;
import static org.abreslav.TestUtils.gererateMetaMetaModelXml;

/**
 * @author abreslav
 */
public class AllTests {

    public static Test suite() {
        generateParseTable();
        gererateMetaMetaModelXml();
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(TemplateInstantiatorTest.suite());
        testSuite.addTest(MetamodelLoadingTest.suite());
        testSuite.addTestSuite(MetaMetamodelLoadingTest.class);
        return testSuite;
    }
}
