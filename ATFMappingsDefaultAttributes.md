# Bug description #

Consider the following example:
```
x
	: $a=atom
	$var=: varPref $b=atom
	;
[[
some () --> (String result1) {
	a:: result1 = #();
	var: result1 = b(atom#);
}
]]
```

As for [Revision f76c1b840d](http://code.google.com/p/grammatic/source/detail?r=f76c1b840d288c5fcd7a494d8c0b7a161e8292b4), this reports a problem:
result1 is read but might be never written before.
This problem is not really present in the code above: attribute result1 is properly initialized on every path leading to the end of the rule.

The problem is caused by a conflict of variables: $a and $atom clash on the first occurrence of atom. When we specify a::, we assign result1 attribute, but we also refer to atom# -- a default attribute. If $a and $atom were the same variable, it would be an error, but since they are different variables (bound to the same thing at application time), everything goes OK and at application time the annotation for $atom overwrites the one for $a, thus we have no assignment to result1, but an extra (unused) assignment to atom# instead.

# Solution #

We will consider variable chains like
a=b=c=#

and when a mapping is assigned to a variable X, we mark all the following variables in the chain as "forbidden", an X itself is marked as "mapped". If a mapping is assigned to a marked variable, it fails.
If a mapping is assigned to a non-marked variable but some of the following variables is mapped, it fails. If all the following are only forbidden or non-marked, it's OK.

# Test #

This case is covered by a test [ATFInterpreterTest.java](http://code.google.com/p/grammatic/source/browse/org.abreslav.grammatic.tester/src/org/abreslav/grammatic/atf/interpreter/ATFInterpreterTest.java).
Data is located in http://code.google.com/p/grammatic/source/browse/org.abreslav.grammatic.tester/testData/atf/interpreter/afterProduction.atf

NB: Test is not working actually because of no support for negative tests in the framework.