# Enchantment: Chez Scheme's Forign Library Interface

A binary interface let Scheme use Python, Lua, Ruby etc's Library

This project is inspired by the Julia language. The FFI interface provided by Chez is used to embed the interpreter or JIT compiler of other languages into the Scheme program (CPython, Luajit etc) or to link the compiled object code with the C binary interface. (OCaml, Golang etc).

Implementation priority: Python ✅ > Julia > Javascript > OCaml

![image](https://github.com/guenchi/Enchantment/blob/master/img/py-call.png)

***Call foreign language libraries with a easily and lispist syntax:***

```
(define lst '(1 2 3 4 5 6 7 8))
(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np sin)
    (get np cos)
    (get np tan)
    (get ndarray tolist)
    (define x (list->py-list 'int lst))
    (define y (/ (*  (float 3.1415926) (array x)) (int 180)))
    (define sin-lst 
        (py-list->list 'float 
            (tolist 
                (sin y))))
    (define cos-lst 
        (py-list->list 'float 
            (tolist 
                (cos y))))
    (define tan-lst 
        (py-list->list 'float 
            (tolist 
                (tan y))))))

=> 

(0.017452406139607784 0.03489949610742155 0.052335955351004666 0.06975647255614194
0.0871557412647174 0.10452846149111272 0.12186934133663416 0.13917309860147606)
(0.9998476951615872 0.9993908270398764 0.9986295348013184 0.9975640503428962
0.996194698221486 0.9945218955549953 0.9925461518953035 0.9902680690730484)
(0.017455064630405803 0.03492076889557947 0.052407778387424844 0.06992681074680297
0.08748866202592445 0.10510423345961667 0.1227845587874379 0.1405408322735788) 
```

***User's Guide***

Py-call's Scope:

In and out of the Py-call's Scope shared a same Lexical Scope.

Cross Scope Procedure:

Some procedure is cross scope, that means it use in the same way in and out of the Py-call's Scope.


Enchantment is dependented https://github.com/guenchi/match, a pioneering work by Dan Friedman, Erik Hilsdale and Kent Dybvig.


Sources:

https://github.com/JuliaPy/PyCall.jl/blob/master/src/PyCall.jl

https://docs.python.org/2.5/ext/callingPython.html

http://www.linux-nantes.org/~fmonnier/OCaml/ocaml-wrapping-c.html

http://caml.inria.fr/pub/docs/manual-ocaml-4.00/manual033.html#htoc281
