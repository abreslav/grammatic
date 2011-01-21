package org.abreslav.models.aspects;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.abreslav.TestUtils;
import org.abreslav.aspects.patterns.PatternMatcher;
import org.abreslav.models.SetValue;
import org.abreslav.templates.lambda.TemplateTerm;

import java.io.File;
import java.util.Arrays;

/**
 * @author abreslav
 */
public class PatternMatcherTest extends TestCase {

    private static final String testRootDir = "testData/aspects";
    private final String testDir;// = testRootDir + "/citation";

    public PatternMatcherTest(String name, String testDir) {
        super(name);
        this.testDir = testDir;
    }

    @Override
    protected void runTest() throws Throwable {
        SetValue m = TestUtils.parseModel(testDir + "/mm.trm", testDir + "/m.trm");

        String additional = testDir + "/additionalData.trm";

        SetValue pattern;
        if (!new File(additional).exists()) {
            pattern = TestUtils.parseModel(
                    "metamodels/templateMM.trm",
                    testDir + "/mm.trm",
                    testDir + "/pattern.trm"
            );
        } else {
            pattern = TestUtils.parseModel(
                    "metamodels/templateMM.trm",
                    testDir + "/mm.trm",
                    additional,
                    testDir + "/pattern.trm"
            );
        }
        PatternMatcher.Result result = PatternMatcher.match(m, new TemplateTerm(pattern));
        TestUtils.assertStringEqualsToFile(testDir, result + "", "expected.txt");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PatternMatcherTest.class.getSimpleName());

        File[] files = new File(testRootDir).listFiles();
        Arrays.sort(files);
        for (File file : files) {
            if (file.isDirectory()) {
                suite.addTest(new PatternMatcherTest(file.getName(), file.getAbsolutePath()));
            }
        }

        return suite;
    }

}
