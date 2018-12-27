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

(enchantment py ffi)

This file is the wrapper for CPython C-API. For details, please refer to: https://docs.python.org/2/c-api/index.html


# Py-call

(enchantment py call)

***definition：***

`*po`: Most of Py-call procedure accept or return a "*po", in scheme, it's a Memory Address which is pointer a specific Python Object.