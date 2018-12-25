# Enchantment: Chez Scheme's Forign Library Interface

A binary interface let Scheme use Python, Lua, Ruby etc's Library

This project is inspired by the Julia language. The FFI interface provided by Chez is used to embed the interpreter or JIT compiler of other languages into the Scheme program (CPython, Luajit etc) or to link the compiled object code with the C binary interface. (OCaml, Golang etc).

Implementation priority: Python ✅ > Julia > Javascript > OCaml

### Call foreign language libraries with a easily and lispist syntax: ###

```
(define lst '(1 2 3 4 5 6 7 8))
(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np pi)
    (get np sin)
    (get np cos)
    (get np tan)
    (get ndarray tolist)
    (define x (list->py-list 'int lst))
    (define y (/ (*  pi (array x)) (int 180)))
    (define sin-lst (py-list->list 'float (tolist (sin y))))
    (define cos-lst (py-list->list 'float (tolist (cos y))))
    (define tan-lst (py-list->list 'float (tolist (tan y))))))

=> 

(0.01745240643728351 0.03489949670250097 0.05233595624294383 0.0697564737441253 
0.08715574274765817 0.10452846326765346 0.12186934340514748 0.13917310096006544)
(0.9998476951563913 0.9993908270190958 0.9986295347545738 0.9975640502598242 
0.9961946980917455 0.9945218953682733 0.992546151641322 0.9902680687415704)
(0.017455064928217585 0.03492076949174773 0.0524077792830412 0.06992681194351041 
0.087488663525924 0.10510423526567646 0.1227845609029046 0.14054083470239143) 
```


Enchantment is dependented https://github.com/guenchi/match, a pioneering work by Dan Friedman, Erik Hilsdale and Kent Dybvig.


Sources:

https://github.com/JuliaPy/PyCall.jl/blob/master/src/PyCall.jl

https://docs.python.org/2.5/ext/callingPython.html

http://www.linux-nantes.org/~fmonnier/OCaml/ocaml-wrapping-c.html

http://caml.inria.fr/pub/docs/manual-ocaml-4.00/manual033.html#htoc281
