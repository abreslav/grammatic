(defglobal ?*g* = 123)

(defmodule X)

(defglobal ?*k* = 124)

(deftemplate multislots
	(multislot a)
	(multislot b
		(type ANY)
	)
	(multislot c
		(type STRING)
	)
	(multislot d
		(default a b c)
		(allowed-values a b c)
	)
	(multislot d
		(default-dynamic a b (+ 1 ?*f*))
	)
	(multislot e
		(default-dynamic 3)
		(allowed-values 1 2 3)
	)
	(multislot f
		(type INTEGER)
		(default x)
		(allowed-values a b c x)
	)
)

(deftemplate slots
	(slot a)
	(slot b
		(type ANY)
	)
	(slot c
		(type STRING)
	)
	(slot d
		(default a)
		(allowed-values a b c)
	)
	(slot e
		(default-dynamic 3)
		(allowed-values 1 2 3)
	)
	(slot e1
		(default-dynamic (+ 3 ?*x*))
	)
	(slot e2
		(default-dynamic (+ 3 ?*x*.x))
	)
	(slot f
		(type INTEGER)
		(allowed-values a b c)
	)
)

(deftemplate MAIN::slotsChild
	extends slots
)

(deftemplate templateDecls
	(declare
		(slot-specific TRUE)
		(backchain-reactive TRUE)
	)
)

(deftemplate fromClass
	(declare
		(slot-specific TRUE)
		(from-class java.lang.Object)
		(include-variables TRUE)
	)
)

(defmodule WOW (declare (auto-focus true)))