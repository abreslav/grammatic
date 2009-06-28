grammar GrammaticStringTemplate;

import GrammaticLexer;

@header {
	package org.abreslav.grammatic.transformation.stringtemplate.parser;
}

template returns [String s] 
	: st=STRING_TEMPLATE
		{
			s = $st.getText();
		}
	;		
		