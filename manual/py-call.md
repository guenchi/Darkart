# Py-call's Manual #



1. In and out of the Py-call's Scope shared a same Lexical Scope.

2. Some procedure is cross scope, that means it use in the same way in and out of the Py-call's Scope.

Such as: `define, display, write, newline`

3. Py-call's Procedure is only use in Py-call's Scope. Some Scheme's procedure is replace in the Py-call's Scope by Py-call's Procedure.

Such as: `+, -, *, /, //, mod, <<, >>, and(bit), or(bit), xor(bit)`

4. You may use the Scheme procedure in the Py-call's Scope, but write the name of procedure with string.
like: ("*" ("+" 5 5) 8)

5. Attention that the Py-call's Procedure and Native Procedure can't be cross nesting ... for now. If have to, use the symbol insteed.

6. All of the procedures in the Py-call's Scope can't accept lists as arguments, except list->py-list and list->py-tuple.