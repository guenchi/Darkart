# Darkart: Chez Scheme's Forign Library Interface

A binary interface let Chez Scheme use Python, Lua, Ruby etc's library

This project is inspired by the Julia language. The FFI interface provided by Chez is used to embed the interpreter or JIT compiler of other languages into the Scheme program (CPython, Luajit etc) or to link the compiled object code with the C binary interface. (OCaml, Golang etc).

This library can be ported to other Scheme implementations, with [r6rs-pffi](https://github.com/ktakashi/r6rs-pffi).

***Priority***: Python ✅ > Julia ✅ > Javascript > OCaml > lua ✅ 

***Ecosystem***: [NumPy](https://github.com/guenchi/NumPy) ✅ [SciPy](https://github.com/guenchi/SciPy) :construction: [SymPy](https://github.com/guenchi/SymPy) :construction: [Matplotlib](https://github.com/guenchi/Matplotlib) ✅ [Pandas](https://github.com/guenchi/Pandas) :construction:


### Manual

[Darkart Python Interface User’s Guide](https://guenchi.github.io/Darkart/manual/python.html)


### The ecosystem which base on Darkart:

https://github.com/guenchi/NumPy

https://github.com/guenchi/SciPy

https://github.com/guenchi/SymPy

https://github.com/guenchi/Matplotlib

https://github.com/guenchi/Pandas

(which is more python library binding exemples)


### Sources:

https://github.com/JuliaPy/PyCall.jl/blob/master/src/PyCall.jl

https://docs.python.org/3.7/c-api/index.html

https://docs.python.org/2.5/ext/callingPython.html

http://www.linux-nantes.org/~fmonnier/OCaml/ocaml-wrapping-c.html

http://caml.inria.fr/pub/docs/manual-ocaml-4.00/manual033.html#htoc281


### Deprecated attempt:

† https://github.com/guenchi/Darkart/tree/deprecated
