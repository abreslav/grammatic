grammar Pascal;

fragment ALPHA : '_' | 'a'..'z' | 'A'..'Z';
fragment DIGIT : '0'..'9';
fragment ALPHANUM : ALPHA | DIGIT;
ID : ALPHA ALPHANUM*;
INT : DIGIT+;

program 
	: programHeader? uses? (var | typeSection | const | routine)* block '.';

name
	: ID | 'program' | 'var' | 'type' | 'const' | 'uses' | 'array' | 'of' 
	| 'packed' | 'record' | 'end' | 'begin' | 'function' | 'string' | 'while'
	| 'do' | 'if' | 'then' | 'else' ;
	
programHeader
	: 'program' name ';'
	;

uses
	: 'uses' nameList ';'
	;
	
nameList
	: name (',' name)*
	;

var
	: 'var' (varDef ';')+
	;
	
varDef
	: nameList ':' type
	;
	
typeSection
	: 'type' typeDef+
	;

typeDef
	: name '=' type ';'
	;
	
type
	: name
	| arrayType
	| recordType
	| 'string' ('[' INT ']')?
	;
	
arrayType
	: 'array' '[' type ']' 'of' type
	;
	
recordType
	: 'packed'? 'record' varDef* 'end'
	;
	
const
	: 'const' constDef+
	;
	
constDef
	: name '=' expression ';'
	
routine
	: (procedure | function) (var | const)* block ';' 
	;
	
procedure
	: 'procedure' name parameterList? ';' 
	;
	
function
	: 'function' name parameterList? ':' type ';'
	;
	
parameterList
	: '(' varDef (';' varDef)* ')'
	;
	
block
	: 'begin' statement (';' statement)* 'end'
	;
	
statement
	: block
	| name ':=' expression
	| 'while' expression 'do' statement
	| 'if' expression 'then' statement ('else' statement)?
	| name argList?
	;
	
argList
	: '(' expression (',' expression)* ')'
	;
	
expression
	: rel ('or' rel)*
	;

rel
	: sum '<' sum
	;
	
sum
	: mult ('+' mult)*
	;

mult
	: factor ('*' factor)*
	;
	
factor
	: name
	| INT
	| '(' sum ')'
	;