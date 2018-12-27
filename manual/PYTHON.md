# Enchantment Python Interface User's Guide

# Build

I haven't prepared the makefile yet, so it will need some manual settings.

First, compile Python.h file with gcc to make a shared object (.so) file.

Just like in https://github.com/guenchi/Enchantment/blob/master/c/py.c 

Compile with `cc -fPIC -shared -framework Python -o ../py.so py.c`

Note Depending on the system, you may need to manually specify the path to python.h.

For me, with my mac it just:

py.c: `<#include  <Python/Python.h>`

In other cases:

`#include "/Library/Frameworks/Python.framework/Versions/3.5/include/python3.5m/Python.h"`

or

`#include "/System/Library/Frameworks/Python.framework/Versions/2.7/include/python2.7/Python.h"`

etc.

Note that python belongs to a framework on mac, so with `-framework` when run cc command, in other cases, just: `cc -fPIC -shared Python -o ../py.so py.c`


# Py-FFI

Library `(enchantment py ffi)`

This file is the wrapper for CPython C-API. For details, please refer to: https://docs.python.org/2/c-api/index.html


# Py-call

Library `(enchantment py call)`

***definition：***

`*po`: Most of Py-call procedure accept or return a "*po", in scheme, it's a Memory Address which is pointer a specific Python Object.

In the programme Py-call, the Class is a *po, the Method is a *po, the Number is a *po, all the Python Side is *po. in other words, we just pass the Memory Address in Scheme level. So, it's efficient and cheap.

This is also why the python library we call via the chez scheme is faster than via the python：We just pass some pointer via binary interface, no extra overheadr.

When you write a code with Py-call, keep all the values in type of *po, don't convert it into scheme data until get the final result.

***Py-init and Py-fin***

`(py-init)` and `(py-fin)` is the alias of `(py-initialize)` and `(py-finalize)`.
 
The codes which manipulating the python library must between `(py-init)` and `(py-fin)`.

Note that you can use it only on time, or you risk to get a 

`Exception: invalid memory reference.  Some debugging context lost`

If you want to write a library which wrap some Python Library, you don't have to use `(py-init)` and `(py-fin)` in the library code. like: https://github.com/guenchi/numpy/blob/master/numpy.sc


***Point***




***Number and String***

procedure: `(int number)` 

procedure: `(float number)`

procedure: `(str number)`

return: `*po`

Covert a Scheme data to Python data.

procedure: `(py->int *po)`

return: `number(int)`

procedure: `(py->float *po)`

return: `number(float)`

procedure: `(py->str *po)` 

return: `string`

Covert a Python data to Scheme data.


***List and Tuple***

procedure: `(list->py-list listOf*Po)`

procedure: `(list->py-list type list)`

procedure: `(list->py-tuple listOf*Po)`

procedure: `(list->py-tuple type list)`

procedure: `(vector->py-list vectorOf*Po)`

procedure: `(vector->py-list type vector)`

procedure: `(vector->py-tuple vectorOf*Po)`

procedure: `(vector->py-tuple type vector)`

return: `*po`

Covert a Scheme's List and Vector to Python's List and Tuple.

The type will be: 'int 'float or 'str.

Exemple:

`(list->py-list 'int '(1 2 3 4 5 6 7 8))`

``(list->py-list `((int 1) (int 2) (int 3)))``

``(vector->py-list `#((int 1) (float 3.14159) (str "foo")))``
