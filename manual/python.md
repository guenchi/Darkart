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

Note that python belongs to a framework on mac, so with `-framework` when run cc command, in other cases, just: `cc -fPIC -shared -o ../py.so py.c`


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


***Python Library***

The library has to install, for exemple via Pip, before enchantment call it.

You can go to python envirement do something like `import numpy` to test it.

```
procedure: (py-import libraryName)
return: *po
```

Don't forget store the memory addres that procedure return, like:
```
(define np (py-import numpy))
```

To repackage a Library to Scheme, there is a Exemple:
https://github.com/guenchi/numpy/blob/master/numpy.sc

***Point***

```
procedure: (py-get *po Name)
return: *po
```
This is the point syntax of Python.
Like:

```
(define array (py-get np array))       = numpy.array
(define pi (py-get np pi))             = numpy.pi
```

***Function***

```
procedure: (py-call *po args ...)
return: *po
```
Exemple:
```
(py-call array (list->py-list '(1 2 3 4 5)))
(define np-array
    (lambda (x)
        (py-call array x)))
```

There is three helpers to generate a procedure callable accept 1, 2 and 3 arguments:

```
procedure: (py-func1 *po)
procedure: (py-func2 *po)
procedure: (py-func3 *po)
return: *po (function)
```

Exemple:
```
(define np-array (py-func1 array))
```


Some python function need named arguments, use:

```
procedure: ((py-call* *po args ...) alistOfNamedArgs)
return: *po
```

Exemple:
```
((py-call* array (list->py-list '(1 2 3 4 5))) `("dtype" ,(str "float")))
(define-syntax np-array
    (syntax-rules ()
        ((_ e)(py-call *array e))
        ((_ e (k v) ...)((py-call* *array e) (list (cons k v) ...)))))
```

***Number and String***

```
procedure: (int number)
procedure: (float number)
procedure: (str number)
return: *po
```

Covert a Scheme data to Python data.

Exemple:
```
(int 8)
(float 3.1415926)
(str "foo")
```

```
procedure: (py->int *po)
return: number(int)
procedure: (py->float *po)
return: number(float)
procedure: (py->str *po)
return: string
```

Covert a Python data to Scheme data.

Exemple:
```
(py->int (int 8))                => 8
(py->float (float 3.1415926))    => 3.1415926
(py->str (str "foo"))            => "foo"
```

***List and Tuple***

```

procedure: (list->py-list listOf*Po)
procedure: (list->py-tuple listOf*Po)
procedure: (vector->py-list vectorOf*Po)
procedure: (vector->py-tuple vectorOf*Po)
procedure: (list->py-list type list)
procedure: (list->py-tuple type list)
procedure: (vector->py-list type vector)
procedure: (vector->py-tuple type vector)
return: *po
```

Covert a Scheme's List and Vector to Python's List and Tuple.

The type will be: 'int 'float or 'str.

Exemple:
```
(list->py-list 'int '(1 2 3 4 5 6 7 8))
(list->py-list `(,(int 1) ,(int 2) ,(int 3)))
(vector->py-list `#(,(int 1) ,(float 3.14159) ,(str "foo")))
```
Attention that if don't specific list / vector's type, you have to covert data to *po before make list / vector.

```
procedure: (py-list->list *po)
procedure: (py-tuple->list *po)
return: list of *po
procedure: (py-list->vector *po)
procedure: (py-tuple->vector *po)
return: vector of *po
procedure: (py-list->list type *po)
procedure: (py-tuple->list type *po)
return: list
procedure: (py-list->vector type *po)
procedure: (py-tuple->vector type *po)
return: vector
```

Exemple:
```
(py-list->list 'int (list->py-list 'int '(1 2 3 4 5 6 7 8)))
=> (1 2 3 4 5 6 7 8)
(py-list->list (list->py-list `(,(int 1) ,(float 3.14159) ,(str "foo"))))
=> (140581204937280 140581204937064 140581204936848)
```
In last case it return a list of Memory Adresse of *po. You can `(display)` it in Scheme or use (py->int), (py->float) or (py-str) to convert it to Scheme Data.

Like:
```
(py->int (car (py-list->list (list->py-list `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> 1
(py->float (cadr (py-list->list (list->py-list `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> 3.14159
(py->str (caddr (py-list->list (list->py-list `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> "foo"
```
