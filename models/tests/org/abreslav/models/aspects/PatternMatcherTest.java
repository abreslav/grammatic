package org.abreslav.models.aspects;

import junit.framework.TestCase;
import org.abreslav.TestUtils;
import org.abreslav.aspects.patterns.IMultiEnvironment;
import org.abreslav.aspects.patterns.PatternMatcher;
import org.abreslav.models.SetValue;
import org.abreslav.templates.lambda.TemplateTerm;

/**
 * @author abreslav
 */
public class PatternMatcherTest extends TestCase {
    public void testCitation() throws Exception {

        String testRootDir = "testData/aspects";
        String testDir = testRootDir + "/citation";
        SetValue m = TestUtils.parseModel(testDir + "/m.trm", testDir + "/mm.trm");

        IMultiEnvironment environment = PatternMatcher.match(m, new TemplateTerm(m));
        System.out.println("environment = " + environment);
        assertNotNull(environment);
//        SetValue patternTerm = TestUtils.parseModel(testDir + "/pattern.trm", testRootDir + "/templateMM.trm");

//        System.out.println("m = " + m);
//        System.out.println("patternTerm = " + patternTerm);
    }

}
