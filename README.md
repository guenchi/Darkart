# FLI: Chez Scheme's Forign Library Interface

A binary interface let Scheme use Python, Lua, Ruby etc's Library

This project is inspired by the Julia language. The FFI interface provided by Chez is used to embed the interpreter or JIT compiler of other languages into the Scheme program (CPython, Luajit etc) or to link the compiled object code with the C binary interface. (OCaml, Golang etc).

For now, I have successfully call the Python's library Numpy in the ugly way. The next step is to do further packaging work, making cross-language library calls more convenient and simple.

Implementation priority: Python > Julia > Javascript > OCaml


Sources:

https://github.com/JuliaPy/PyCall.jl/blob/master/src/PyCall.jl

https://docs.python.org/2.5/ext/callingPython.html

http://www.linux-nantes.org/~fmonnier/OCaml/ocaml-wrapping-c.html

http://caml.inria.fr/pub/docs/manual-ocaml-4.00/manual033.html#htoc281
