program
	: {this} statement{(this)}*
	;

statement{(this)}
	: expression{this.statements+=} // ??
	: defmodule{this.statements+=}
	: defglobal{(this)}
	: deftemplate{this.statements+=}
	: defrule{this.statements+=}
	: defquery{this.statements+=}
	: deffacts{this.statements+=}
	: deffunction{this.statements+=}
	;

expression
	: variable
	: nonVariableSymbol
	: number
	: STRING
	: dottedVariable
	: functionCall
	// NEGATIVE numbers are treated as numbers!
	// LISTS??? -- are handled specially by functions :( 
	;

defmodule
	: {this} '(' 'defmodule' nonVariableSymbol{this.name}
    	STRING{this.comment}?
    	moduleDeclarations{(this)}?
      ')'	
	;
	
moduleDeclarations{(this)}
	: '(' 'declare' autoFocus{(this)} ')'
	;
	
autoFocus{(this)}
// doubts here!
	: '(' 'auto-focus' (boolean{TRUE{this.autoFocus}}) ')'
	;


defglobal{(this:program)}
	: '(' 'defglobal' 
	     ({this} GLOBAL_VARIABLE{this.name} '=' expression{this.value}){this:program+=this}+ ')'
	;


templateDeclarations
	: {this} '(' 'declare' 
        (slotSpecific{(this)} |
        backchainReactive{(this)} |
        fromClass{(this)} | 
        includeVariables{(this)} | 
        ordered{(this)})*
      ')'
	;
	
ordered{(this)}
// If it works, it's the best thing ever!!!
	: '(' 'ordered' {assert this.ordered is not set}
	    boolean{TRUE{{this:slot} {this:slot.name="__data"}... {this.slots+=this:slot; this.ordered}}
	  ')'
	;
	
slotKind
	: 'slot' | 'multislot'
	;
	
slot
	: '(' slotKind nonVariableSymbol // check for dot absence in the name
         (slotType |
         default |
     	 defaultDynamic |
     	 allowedValues)*  
      ')'
    ;

deftemplate
	: '(' 
		'deftemplate' topLevelName
    	('extends' topLevelName)?
    	STRING?
    	templateDeclarations?
    	slot* // forbidden if declared ordered
      ')'
	;
	

ruleDeclarations
	: '(' 'declare' 
			(salience |
            nodeIndexHash |
            autoFocus |
            noLoop)*
      ')'
	;

defrule 
	: '(' 'defrule' topLevelName
    	STRING?
    	ruleDeclarations?
    	conditionalElement*
    	'=>'
    	functionCall* 
      ')'
	;


queryDeclarations
	: '(' 'declare' '(' 'variables' variable+ ')'
			nodeIndexHash?
        	maxBackgroundRules? 
      ')'
    ;

defquery
	: '(' 'defquery' topLevelName
    	STRING?
    	queryDeclarations?
    	conditionalElement* 
      ')'
	;

deffacts
	: '(' 'deffacts' topLevelName
		STRING?
    	fact* 
      ')'
	;

fact
	: '(' topLevelName ('(' nonVariableSymbol expression ')' )* ')'
	: '(' topLevelName expression* ')' // ordered facts
	;

deffunction
	: '(' 'deffunction' 
		nonVariableSymbol 
		STRING? 
		'(' variable* MULTIFIELD? ')' 
			// $? emits new on each occurrence
			// a multifield is not a special kind of variable. 
			// When a multifield $?list is matched, it's the variable ?list that receives the value.
		expression* 
	  ')'
	;