package org.abreslav.grammatic.antlr.generator.parsers;

import java.util.Collection;

import org.abreslav.grammatic.antlr.generator.antlr.AntlrFactory;
import org.abreslav.grammatic.antlr.generator.antlr.Import;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.util.IMultiValueParserHelper;
import org.abreslav.grammatic.metadata.util.IValueStream;
import org.abreslav.grammatic.metadata.util.MultiValueParserHelper;

/**
 * imports : importEntry+ ;
 * importEntry : 'import' (ID | '.' | '$' | '*')* ';' ; 
 *
 */
public class ImportParser {

	private final IMultiValueParserHelper myHelper;

	public ImportParser(IValueStream stream) {
		super();
		myHelper = new MultiValueParserHelper(stream);
	}
	
	public void imports(Collection<Import> imports) {
		while (myHelper.getCurrentToken() != null) {
			imports.add(importEntry());
		}
	}

	private Import importEntry() {
		myHelper.matchKeyword("import");
		StringBuilder stringBuilder = new StringBuilder();
		while (!myHelper.checkPunctuationToken(PunctuationType.SEMICOLON)) {
			if (myHelper.getCurrentToken() == null) {
				throw new IllegalArgumentException();
			}
			if (myHelper.checkPunctuationToken(
					PunctuationType.DOT)) {
				stringBuilder.append(".");
			} else if (myHelper.checkPunctuationToken(
					PunctuationType.DOLLAR)) {
				stringBuilder.append("$");
			} else if (myHelper.checkPunctuationToken(
					PunctuationType.ASTERISK)) {
				stringBuilder.append("*");
			} else {
				IdValue idValue = myHelper.match(IdValue.class);
				stringBuilder.append(idValue.getId());
				myHelper.back();
			}
			myHelper.next();
		}
		myHelper.match(PunctuationType.SEMICOLON);
		
		Import result = AntlrFactory.eINSTANCE.createImport();
		result.setImported(stringBuilder.toString());
		return result;
	}
	
}
