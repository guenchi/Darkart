# Py-call's Manual #

***Py-call's Scope:***

In and out of the Py-call's Scope shared a same Lexical Scope.

***Cross Scope Procedure:***

Some procedure is cross scope, that means it use in the same way in and out of the Py-call's Scope.

Such as: `define, display, newline`

***Py-call's Procedure***

Py-call's Procedure is only use in Py-call's Scope. Some Scheme's procedure is replace in the Py-call's Scope by Py-call's Procedure.

Such as: `+, -, *, /, //, mod, <<, >>, and(bit), or(bit), xor(bit)`


***Native Procedure***

You may use the Scheme procedure in the Py-call's Scope, but write the name of procedure with string.
Attention that the Py-call's Procedure and Native Procedure can't be cross nesting ... for now. If have to, use the symbol insteed.