(defmodule BIG)

(defmodule autofocus 
	(declare (auto-focus TRUE))
)

(defglobal ?*x* = 5)

?*x*

xyz

123

"sadsadf"

(bind ?x (new java.lang.Object))

?x.class

(+ 1 2)

(deftemplate BIG::simple (slot x))
(deftemplate BIG::pair (slot x) (slot y))

(deftemplate BIG::complex
  extends BIG::simple
  (declare
  	(ordered FALSE)
  	(slot-specific TRUE)
  	(slot-specific FALSE)
  )
  (slot x$?123)  
  (slot y
  		(type INTEGER)
  		(default 1)
  		(default-dynamic (a))
  		(type SYMBOL)
  )
  (slot y ;<-----------
  		(type SYMBOL)
  		(default a)
  		(type INTEGER)
  		(allowed-values a b c)
  )
  (multislot x
  		(type ANY)
		(default g)
  		(default-dynamic a.b.c ^ g !)
  )
  (multislot x ;<--- additive
  		(type SYMBOL)
  )
  (multislot xy
  		(type ANY)
  		(default 1 2 3)
  		(allowed-values 1 2 3)
  )
)

(deftemplate ord 
	"How to restrict an ordered template?"
	(declare 
		(ordered TRUE)
	)
) 

(defrule rule
	(declare
		(salience (+ 1 1))
		(node-index-hash 123)
	)
	(ord $?list)
	=>
	(printout t ?list crlf)
)

(defrule some
	(ord ?a&:(> ?a 0) $?)
	=>
	(printout t ?a crlf)
)

(defquery q
	(declare (variables ?v ?w))
	(ord ?v ?w ?r)
)

(deffacts some "asda"
	(ord -3 d r)
	(ord 5 d r)
	(BIG::simple (x 5))
)

(reset)
(assert (ord 1 2 3))
(assert (ord 1 2 5))

(defglobal ?*y* = (run-query* q 1 2))

(defrule r
	(ord $?)
	=>
	(while (?*y* next)
	    (printout t "note " (?*y* getString r) crlf)
	)
	(printout t ?*y* crlf)
)

(defrule simplePatterns
	(BIG::simple {x > 2})
	(BIG::simple {x > 2 && (x < 3 || x > 2)})
	(BIG::simple {x > 2} (x 5) (x ?x))
	(BIG::simple {(x < (+ 1 ?x1))} (x 5) (x ?x1))
	(BIG::pair (x ?x) (y ?x))
	(BIG::pair (x ?x & ?x1 & = (+ 1 1) &~:(< ?x 1)) (y ?x))
	=>
)

(defrule bindings
	?fact <- (BIG::pair (x ?x & 2 | 1))
	(and 
		?fact1 <- (BIG::pair (x ?x & 2 | 1))
		?fact2 <- (BIG::pair (x ?x & 2 | 1))
	)
	(forall
		?a <- (BIG::pair (x ?x & 2 | 1))
		?b <- (BIG::pair (x ?x & 2 | 1))
	)
	(not
		?xy <- (BIG::pair (x ?x & 223 | 111))
	)
	=>
	(printout t ?xy)
)

(defrule notbindings
	(not
		?xy <- (BIG::pair (x ?x & 223 | 111))
	)
	(test (if (< 1 2) then (bind ?c 5) (return (< 1 2)) (printout t "ha" ?c crlf) else TRUE))
	=>
	(printout t x crlf)
)

(run)