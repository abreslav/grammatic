program
	: statement*
	;

statement
	: expression
	: defmodule
	: defglobal
	: deftemplate
	: defrule
	: defquery
	: deffacts
	: deffunction
	;

expression // ???
	: variable
	: nonVariableSymbol
	: number
	: STRING
	: dottedVariable
	: functionCall
	;

namedAndCommented
	: nonVariableSymbol STRING? 
	;
	
defmodule
	: namedAndCommented autoFocus?
	;
	
defglobal
	: GLOBAL_VARIABLE expression
	;
 
slotKind
	: 'slot'
	: 'multislot'
	;
	
slot
	: slotKind nonVariableSymbol // check for dot absence in the name
	  $declarations=(slotType*
      default*
      defaultDynamic*
      allowedValues*)
    ;

moduleCitizen
	: namedAndCommented 
	  defmodule& 
	;

deftemplate
	: moduleCitizen
	  ($super=deftemplate)? // super
	  slotSpecific?
      backchainReactive?
      fromClass?
      includeVariables? 
      ordered?
      slot* // forbidden if declared ordered
	;

defrule 
	: moduleCitizen
	  salience?
	  nodeIndexHash?
	  autoFocus?
	  noLoop?
	  conditionalElement*
	  functionCall* 
	;


defquery
	: moduleCitizen
	  variable*
	  nodeIndexHash?
	  maxBackgroundRules? 
	  conditionalElement* 
	;

deffacts
	: moduleCitizen
	  fact* 
	;

fact
	: deftemplate&
	  slotValue*
	;
	
slotValue
	: slot& expression
	;

deffunction
	: namedAndCommented
	  variable* 
	  MULTIFIELD? 
      expression* 
	;