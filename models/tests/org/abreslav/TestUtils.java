package org.abreslav;

import junit.framework.Assert;
import org.abreslav.models.SetValue;
import org.abreslav.models.metamodels.ConformanceChecker;
import org.abreslav.models.metamodels.IDiagnostic;
import org.abreslav.models.wellformedness.CompositeContext;
import org.abreslav.models.wellformedness.Context;
import org.abreslav.models.wellformedness.IContext;
import org.abreslav.models.wellformedness.WellFormednessChecker;
import org.abreslav.models.xml.XMLParser;
import org.jdom.JDOMException;

import java.io.*;
import java.util.Collection;

import static junit.framework.Assert.fail;

/**
 * @author abreslav
 */
public class TestUtils {
    private static final String SDF_HOME = "/media/data/work/asfsdf";
    private static final String MMM_DIR = "metametamodel";

    private static SetValue MMM;

    public static SetValue loadMetaMetaModel() throws JDOMException, IOException {
        if (MMM == null) {
            MMM = new XMLParser().parse(new File(MMM_DIR + "/MMM.xml"));
            WellFormednessChecker.INSTANCE.checkWellFormedness(IContext.EMPTY, MMM.getValue());
            ConformanceChecker.check(MMM.getValue());
        }
        return MMM;
    }

    public static void gererateMetaMetaModelXml() {
        try {
            String mmmXml = MMM_DIR + "/MMM.xml";
            new File(mmmXml).delete();
            termToXml(MMM_DIR + "/MMM.trm", mmmXml);

        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    public static void generateParseTable() {
        String tableFile = "models.tbl";
        new File(tableFile).delete();
        try {
            execAndPrint(SDF_HOME + "/bin/sdf2table -v -c -m Models " +
                "-p .:" + SDF_HOME + "/share/sdf-library/library -t -o " + tableFile);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public static void termToXml(String termFileName, String xmlFileName) throws InterruptedException, IOException {
        execAndPrint(SDF_HOME + "/bin/sglr -i " + termFileName + " -p models.tbl -t -o tmp",
                "implodePT -t -i tmp -o tmp1",
                "aterm2xml --implicit -i tmp1 -o " + xmlFileName,
                "rm tmp tmp1");
    }

    public static void execAndPrint(String... commands) throws InterruptedException, IOException {
        for (String command : commands) {
            System.out.println("$ " + command);
            Process process = Runtime.getRuntime().exec(command);

            process.waitFor();
            InputStreamReader reader = new InputStreamReader(process.getErrorStream());
            int c;
            while ((c = reader.read()) != -1) {
                System.out.print((char) c);
            }
            reader.close();
        }
    }

    public static void assertStringEqualsToFile(String testDir, String result, String expectedFileName) throws IOException {
        File expectedFile = new File(testDir + "/" + expectedFileName);
        if (!expectedFile.exists()) {
            PrintStream printStream = new PrintStream(expectedFile);
            printStream.append(result);
            printStream.close();
            fail("No expectation file, created one");
        } else {
            FileReader fileReader = new FileReader(expectedFile);
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            while ((c = fileReader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            fileReader.close();

            Assert.assertEquals(stringBuilder.toString() + "\n", result + "\n");
        }
    }

    public static SetValue parseTerm(String termFileName) throws InterruptedException, IOException, JDOMException {
        String xmlFileName = termFileName + ".xml";
        File xmlFile = new File(xmlFileName);
        xmlFile.delete();
        termToXml(termFileName, xmlFileName);
        SetValue model = new XMLParser().parse(xmlFile);
        xmlFile.delete();
        return model;
    }

    public static void checkMetaModel(SetValue mm) throws JDOMException, IOException {
        checkModel(mm, loadMetaMetaModel());
    }

    public static void checkModel(SetValue m, SetValue mm) throws JDOMException, IOException {
        WellFormednessChecker.INSTANCE.checkWellFormedness(new CompositeContext(new Context(mm), new Context(loadMetaMetaModel())), m.getValue());
        Collection<IDiagnostic> result = ConformanceChecker.check(m.getValue());
        if (!result.isEmpty()) {
            fail(result.toString());
        }
    }

    public static SetValue parseMetaModel(String termFileName) throws InterruptedException, IOException, JDOMException {
        SetValue mm = parseTerm(termFileName);
        checkMetaModel(mm);
        return mm;
    }

    public static SetValue parseModel(String modelFileName, String metaModelFileName) throws InterruptedException, IOException, JDOMException {
        SetValue mm = parseMetaModel(metaModelFileName);

        SetValue m = parseTerm(modelFileName);
        checkModel(m, mm);
        return m;
    }
}
