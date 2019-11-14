# Darkart: Chez Scheme's Forign Library Interface

# [Manual](https://guenchi.github.io/Darkart/)

A binary interface let Chez Scheme use Python, Lua, Ruby etc's library

This project is inspired by the Julia language. The FFI interface provided by Chez is used to embed the interpreter or JIT compiler of other languages into the Scheme program (CPython, Luajit etc) or to link the compiled object code with the C binary interface. (OCaml, Golang etc).

This library can be ported to other Scheme implementations, with [r6rs-pffi](https://github.com/ktakashi/r6rs-pffi).

***Priority***: Python ✅ > Julia ✅ > Javascript > OCaml > lua ✅ 

***Ecosystem***: [NumPy](https://github.com/guenchi/NumPy) ✅ [SciPy](https://github.com/guenchi/SciPy) :construction: [SymPy](https://github.com/guenchi/SymPy) :construction: [Matplotlib](https://github.com/guenchi/Matplotlib) ✅ [Pandas](https://github.com/guenchi/Pandas) :construction:


### Exemple

```
(define np (py-import 'numpy))
(define ndarray (py-get np 'ndarray))
(define pi (py-get np 'pi))
(define np-array (py-func np 'array))
(define np-sin (py-func np 'sin))
(define np-tolist (py-func ndarray 'tolist))

(define get-sin
  (lambda (lst)
    (plist->list
      (np-tolist
        (np-sin
          (py-div
            (py-mul pi 
              (np-array
                (list->plist lst)))
            (int 180)))))))

(get-sin '(1 2 3 4 5 6 7 8))

=>
(0.01745240643728351 0.03489949670250097 0.05233595624294383 0.0697564737441253 
0.08715574274765817 0.10452846326765346 0.12186934340514748 0.13917310096006544)
```


### The ecosystem which base on Darkart:

https://guenchi.github.io/NumPy

https://guenchi.github.io/SciPy

https://guenchi.github.io/SymPy

https://guenchi.github.io/Matplotlib

https://guenchi.github.io/Pandas

(which is more python library binding exemples)


### Sources:

https://github.com/JuliaPy/PyCall.jl/blob/master/src/PyCall.jl

https://docs.python.org/3.7/c-api/index.html

https://docs.python.org/2.5/ext/callingPython.html

http://www.linux-nantes.org/~fmonnier/OCaml/ocaml-wrapping-c.html

http://caml.inria.fr/pub/docs/manual-ocaml-4.00/manual033.html#htoc281


### Deprecated attempt:

† https://github.com/guenchi/Darkart/tree/deprecated
