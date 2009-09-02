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

defmodule
	: nonVariableSymbol STRING? autoFocus?
	;
	
@nonVariableSymbol.before = <<'(' 'defmodule'>>;
@#after = <<")">>;
@introduce(moduleDeclarations : '(' 'declare' <<autoFocus>> ')');
@autoFocus.instead = <<moduleDeclarations>>;
	
defglobal
	: GLOBAL_VARIABLE expression
	;
@before = <<'(' 'defglobal'>>;
@after = <<')'>>;
@enclose(defglobal, +) // <-- name clash
@GLOBAL_VARIABLE.after = <<'='>>;
 
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
@before = <<'('>>;
@after = <<')'>>;
// how will it prove that the abstract syntax is preserved?
// a* b* ~= (a | b)* -- is this a ground inference rule?
@declarations.instead = <<(slotType | defaultDynamic | default | allowedValues)*>>


deftemplate
	: $namesig=(
	    defmodule&
	    nonVariableSymbol
	  )
      ($super=deftemplate&)? // super
      STRING?
	  slotSpecific?
      backchainReactive?
      fromClass?
      includeVariables? 
      ordered?
      slot* // forbidden if declared ordered
	;
@before
@after
@super.instead = <<topLevelName>>;
// a ~= a? {default : a} 
@defmodule.representedBy = <<nonVariableSymbol?{default = "MAIN"}>>;
@namesig.instead = topLevelName:{nonVariableSymbol?{default = "MAIN"}};
//@ for declarations

defrule 
	: nonVariableSymbol // module
	  nonVariableSymbol // name
	  STRING?
	  salience?
	  nodeIndexHash?
	  autoFocus?
	  noLoop?
	  conditionalElement*
	  functionCall* 
	;


defquery
	: nonVariableSymbol // module
	  nonVariableSymbol // name
	  STRING?
	  variable*
	  nodeIndexHash?
	  maxBackgroundRules? 
	  conditionalElement* 
	;

deffacts
	: nonVariableSymbol // module
	  nonVariableSymbol // name
	  STRING?
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
	: nonVariableSymbol 
	  STRING? 
	  variable* 
	  MULTIFIELD? 
      expression* 
	;