package org.abreslav.models.metamodels;

import junit.framework.TestCase;
import org.abreslav.TestUtils;
import org.abreslav.models.SetValue;

import java.util.Collection;

/**
 * @author abreslav
 */
public class MMMConformanceCheckerTest extends TestCase {


    public void testMMM() throws Exception {
        SetValue mmm = TestUtils.loadMetaMetaModel();
        Collection<IDiagnostic> result = ConformanceChecker.check(mmm.getValue());
        if (!result.isEmpty()) {
            for (IDiagnostic iDiagnostic : result) {
                System.out.println(iDiagnostic);
            }
            fail();
        }
    }
}
