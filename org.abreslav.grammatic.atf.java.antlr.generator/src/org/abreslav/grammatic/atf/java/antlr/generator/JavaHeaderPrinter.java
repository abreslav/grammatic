package org.abreslav.grammatic.atf.java.antlr.generator;

import java.util.List;

import org.abreslav.grammatic.atf.java.antlr.semantics.Import;
import org.abreslav.grammatic.utils.printer.Printer;

public class JavaHeaderPrinter {
	public static void printHeader(Printer printer, String pack, List<Import> imports) {
		if (pack != null) {
			printer.words("package", pack).separator(";").endl();
			printer.endln();
		}
		for (Import imp : imports) {
			printer.word("import").print(imp.getImported(), ";").endl();
		}
	}
}
