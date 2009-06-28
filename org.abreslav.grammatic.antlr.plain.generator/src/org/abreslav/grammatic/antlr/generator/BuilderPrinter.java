package org.abreslav.grammatic.antlr.generator;

import java.util.List;

import org.abreslav.grammatic.antlr.generator.antlr.Import;
import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Builder;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderFactory;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderMethod;
import org.abreslav.grammatic.utils.printer.Printer;

public class BuilderPrinter {

	public void printBuilderFactoryInterface(BuilderFactory builderFactory, Printer printer) {
		List<Import> imports = builderFactory.getImports();
		JavaHeaderPrinter.printHeader(printer, builderFactory.getPackage(), imports);
		if (!imports.isEmpty()) {
			printer.endln();
		}
		
		printer.words("public", "interface", builderFactory.getFactoryInterfaceName()).blockStart("{").endl();
		for (Builder builder : builderFactory.getBuilders()) {
			printBuilderInterface(builder, printer);
		}
		
		printer.endl();
		for (BuilderMethod builderMethod : builderFactory.getMethods()) {
			printMethod(builderMethod, printer);
		}
		
		printer.blockEnd("}").endl();
	}

	public void printBuilderInterface(Builder builder, Printer printer) {
		printer.words("public", "interface", builder.getName()).blockStart("{").endl();
		for (BuilderMethod method : builder.getMethods()) {
			printMethod(method, printer);
		}
		printer.blockEnd("}").endl();
		printer.endln();
	}

	private void printMethod(BuilderMethod method, Printer printer) {
		printer.words(method.getType(), method.getName());
		printer.separator("(").list(", ");
		for (Parameter parameter : method.getParameters()) {
			printParameter(parameter, printer);
		}
		printer.endList().separator(");").endl();
	}

	private void printParameter(Parameter parameter, Printer printer) {
		printer.item().word(parameter.getType()).print(parameter.getName());
	}
}
