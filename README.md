# FLI: Chez Scheme's Forign Library Interface

A binary interface let Scheme use Python, Lua, Ruby etc's Library

This project is inspired by the Julia language. The FFI interface provided by Chez is used to embed the interpreter or JIT compiler of other languages into the Scheme program (CPython, Luajit etc) or to link the compiled object code with the C binary interface. (OCaml, Golang etc).

Implementation priority: Python ✅ > Julia > Javascript > OCaml

***Call foreign language libraries with a Easily and lispist syntax:***

```
(define x '(1 2 3 4 5 6 7 8))
    (py-call 
        '(
            (import numpy as np)
            (get np array)
            (get np ndarray)
            (get np cos as cosin)
            (get ndarray tolist)
            (define lst (list->py-list 'int x))
            (define cosin-lst (py-list->list 'float (tolist (cosin (array lst)))))))
    (display cosin-lst)

=> 

(0.5403023058681398 -0.4161468365471424 -0.9899924966004454 -0.6536436208636119 0.2836621854632263 0.9601702866503661 0.7539022543433046 -0.14550003380861354)    
```


![image](https://github.com/guenchi/FLI/blob/master/img/pycall.png)




Sources:

https://github.com/JuliaPy/PyCall.jl/blob/master/src/PyCall.jl

https://docs.python.org/2.5/ext/callingPython.html

http://www.linux-nantes.org/~fmonnier/OCaml/ocaml-wrapping-c.html

http://caml.inria.fr/pub/docs/manual-ocaml-4.00/manual033.html#htoc281
