# Darkart: Python Interface User's Guide

# Build

I haven't prepared the makefile yet, so it will need some manual settings.

First, compile Python.h file with gcc to make a shared object (.so) file.

Just like in https://github.com/guenchi/Darkart/blob/master/c/py.c 

Compile with `cc -fPIC -shared -framework Python -o ../py.so py.c`

Note that depending on the system, you may need to manually specify the path to `python.h`.

For me, with my mac it just:

py.c: `<#include  <Python/Python.h>`

In other cases:

py.c: `#include "/Library/Frameworks/Python.framework/Versions/3.5/include/python3.5m/Python.h"`

or

py.c: `#include "/System/Library/Frameworks/Python.framework/Versions/2.7/include/python2.7/Python.h"`

etc.

Note that python belongs to a framework on mac, so with `-framework` when run cc command, in other cases, just: `cc -fPIC -shared -o ../py.so py.c`


# Definition:

`*po` representing a pointer to a Python Object reference.

Most of Py-call procedure accept or return a `*po`, in Scheme level, it's a memory address which is pointer a specific Python Object.

In the programme Py-call, the Class, the Method, the Number, all of it is `*po`. In other words, we just pass the memory address in Scheme level. 

This fits the philosophy of Scheme: "Parameter passing is by value, but the values are references."

This is also why the python library we call via the chez scheme is efficient than via the python：We just pass some pointer via binary interface, no extra overheadr.

When you write a code with Py-call, keep all the values in type of *po, don't convert it into scheme data until get the final result.

In this document, *po may be followed by the type of Python object it points to, like: *po<number>, *po<list,tuple> etc.

And *po{value} means a `*po` point to this value.

# Py-call

Library `(darkart py call)`

### Py-init and Py-fin

`(py-init)` and `(py-fin)` is the alias of `(py-initialize)` and `(py-finalize)`.
 
The codes which manipulating the python library must between `(py-init)` and `(py-fin)`.

Note that you can use it only on time, or you risk to get a 

`Exception: invalid memory reference.  Some debugging context lost`

If you want to write a library which wrap some Python Library, you don't have to use `(py-init)` and `(py-fin)` in the library code. like: https://github.com/guenchi/numpy/blob/master/numpy.sc


### Python Library

The library has to install, for exemple via Pip, before darkart call it.

You can go to python envirement do something like `import numpy` to test it.

```scheme
procedure: (py-import libraryName)

return: *po
```

Don't forget store the memory addres that procedure return, like:
```scheme
(define np (py-import numpy))
```

To repackage a Library to Scheme, there is a Exemple:
https://github.com/guenchi/numpy/blob/master/numpy.sc

### Point

```scheme
procedure: (py-get *po Name)

return: *po
```
This is the point syntax of Python.
Like:

```scheme
(define array (py-get np array))      = numpy.array

(define pi (py-get np pi))            = numpy.pi
```

### Function

```scheme
procedure: (py-call *po<callable> args ...)

return: *po
```

Exemple:
```scheme
(py-call array (list->py-list 'int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}

(define np-array
    (lambda (x)
        (py-call array x)))

(np-array (list->py-list 'int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}
```

There is a helper to generate a procedure with *po<callable>:

```scheme
procedure: (py-func *po<callable>)

return: *po<function>
```

Exemple:
```scheme
(define np-array (py-func array))

(np-array (list->py-list 'int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}
```

Some python function need named arguments, use:

```scheme
procedure: ((py-call* *po<callable> args ...) alistOfNamedArgs)

return: *po
```
The alist is like: `'(("Name" . *po) ...)`

Exemple:
```scheme
((py-call* array (list->py-list 'int '(1 2 3 4 5)))
    `(('dtype . ,(str "float"))))
=> *op{[1.0, 2.0, 3.0, 4.0, 5.0]}

(define-syntax np-array
    (syntax-rules ()
        ((_ e)(py-call *array e))
        ((_ e (k v) ...)
            ((py-call* *array e) 
                (list (cons k v) ...)))))

(np-array (list->py-list 'int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}

(np-array (list->py-list 'int '(1 2 3 4 5)) ('dtype (str "float")))
=> *op{[1.0, 2.0, 3.0, 4.0, 5.0]}
```

There is also a helper to generate a procedure which need named argument with *po<callable>:

```scheme
procedure: (py-func* *po<callable>)

return: *po<function>
```

Exemple:
```scheme
(define np-array (py-func* array))

((np-array 
    (list->py-list 'int '(1 2 3 4 5)))
    `(('dtype . ,(str "float"))))
=> *op{[1.0, 2.0, 3.0, 4.0, 5.0]}
```

Args helpers:
```scheme
procedure: (py-args *po ...)

procedure: (py-args* '(*po ...))

return: *po<tuple>
```
This procedure is use to prepare a tuple of arguments for Python's function. 
It usually won't be used directly because it's included in `(py-call)`,`(py-call*)`,`(py-func)`and`(py-func*)`.

### Number and String

```scheme
procedure: (int number)

procedure: (float number)

procedure: (str number)

return: *po<number,string>
```

Covert a Scheme data to Python data.

Exemple:
```scheme
(int 8)                       => *po{8}

(float 3.1415926)             => *po{3.1415926}

(str "foo")                   => *po{"foo"}
```

```scheme
procedure: (py->int *po<number>)

return: number<int>

procedure: (py->float *po<number>)

return: number<float>

procedure: (py->str *po<string>)

return: string
```

Covert a Python data to Scheme data.

Exemple:
```scheme
(py->int (int 8))                => 8

(py->float (float 3.1415926))    => 3.1415926

(py->str (str "foo"))            => "foo"
```

### List and Tuple

```scheme
procedure: (list->py-list* listOf*Po)

procedure: (list->py-tuple* listOf*Po)

procedure: (vector->py-list* vectorOf*Po)

procedure: (vector->py-tuple* vectorOf*Po)

procedure: (list->py-list type list)

procedure: (list->py-tuple type list)

procedure: (vector->py-list type vector)

procedure: (vector->py-tuple type vector)

return: *po<list,tuple>
```

Covert a Scheme's List and Vector to Python's List and Tuple.

The type will be: 'int 'float or 'str.

Exemple:
```scheme
(list->py-list 'int '(1 2 3 4 5 6 7 8))

(list->py-list* `(,(int 1) ,(int 2) ,(int 3)))

(vector->py-list* `#(,(int 1) ,(float 3.14159) ,(str "foo")))
```
Attention that if don't specific list / vector's type, you have to covert data to *po before make list / vector.

```scheme
procedure: (py-list->list type *po<list>)

procedure: (py-tuple->list type *po<tuple>)

return: list

procedure: (py-list->vector type *po<list>)

procedure: (py-tuple->vector type *po<tuple>)

return: vector

procedure: (py-list->list* *po<tuple>)

procedure: (py-tuple->list* *po<tuple>)

return: list of *po

procedure: (py-list->vector* *po<list>)

procedure: (py-tuple->vector* *po<tuple>)

return: vector of *po
```

Exemple:
```scheme
(py-list->list 'int (list->py-list 'int '(1 2 3 4 5 6 7 8)))
=> (1 2 3 4 5 6 7 8)

(py-list->list* (list->py-list* `(,(int 1) ,(float 3.14159) ,(str "foo"))))
=> (*po{1} *po{3.14159} *po{"foo"})
```
In last case it return a list of Memory Adresse of *po. You can use (py->int), (py->float) or (py-str) to convert it to Scheme Data.

Like:
```scheme
(py->int (car (py-list->list* (list->py-list* `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> 1

(py->float (cadr (py-list->list* (list->py-list* `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> 3.14159

(py->str (caddr (py-list->list* (list->py-list* `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> "foo"
```

### Numeric Operations

```scheme
procedure: (py-add *po *po)

procedure: (py-sub *po *po)

procedure: (py-mul *po *po)

procedure: (py-div *po *po)

procedure: (py-fdiv *po *po)

procedure: (py-mod *po *po)

procedure: (py-lsh *po *po)

procedure: (py-rsh *po *po)

procedure: (py-and *po *po)

procedure: (py-or *po *po)

procedure: (py-xor *po *po)

procedure: (py-inv *po)

procedure: (py-abs *po)

procedure: (py-neg *po)

return: *po
```

*po in here may be a number or a list, tuple, array(numpy) of number.

Exemple:
```scheme
(py-add (int 3) (int 5))                            => *po{8}

(py-mul (int 8) (list->py-list int' '(1 2 3 4 5)))  => *po{[8, 16, 24, 32, 40]}

(py-abs (int -5))                                   => *po{5} 
```


# Py-FFI

Library `(darkart py ffi)`

This file contain the base interface to Python.

### Interpreter

```scheme
procedure: (py-initialize)

procedure: (py-finalize)
```

See (py-init) and (py-fin) in `(darkart py call)`

```scheme
procedure: (py-incref)

procedure: (py-decref)

procedure: (py-new-interpreter)

procedure: (py-end-interpreter)
```

### Math

```scheme
procedure: (py/number-add *po *po)

procedure: (py/number-subtract *po *po)

procedure: (py/number-multiply *po *po)

procedure: (py/number-divide *po *po)

procedure: (py/number-floor-divide *po *po)

procedure: (py/number-divmod *po *po)

procedure: (py/number-lshift *po *po)

procedure: (py/number-rshift *po *po)

procedure: (py/number-and *po *po)

procedure: (py/number-or *po *po)

procedure: (py/number-xor *po *po)

procedure: (py/number-invert *po)

procedure: (py/number-absolute *po)

procedure: (py/number-negative *po)

return: *po
```

*po in here may be a number or a list, tuple, array(numpy) of number.


### Number

```scheme
procedure: (py/int-from-long number)

procedure: (py/int-from-size_t number)

procedure: (py/int-from-ssize_t number)

return: *po<int>
```

```scheme
procedure: (py/long-from-long number)

procedure: (py/long-from-unsigned-long number)

procedure: (py/long-from-longlong number)

procedure: (py/long-from-unsigned-longlong number)

procedure: (py/long-from-double number)

procedure: (py/long-from-size_t number)

procedure: (py/long-from-ssize_t number)

return: *po<long>
```

```scheme
procedure: (py/float-from-double number)

return: *po<float>
```

Pass Scheme number to Python.

```scheme
procedure: (py/int-as-long *po<int>)

procedure: (py/int-as-ssize_t *po<int>)

procedure: (py/long-as-long *po<long>)

procedure: (py/long-as-unsigned-long *po<long>)

procedure: (py/long-as-longlong *po<long>)

procedure: (py/long-as-unsigned-longlong *po<long>)

procedure: (py/long-as-double *po<float>)

procedure: (py/long-as-ssize_t *po<float>)

procedure: (py/float-as-double *po<float>)

return: number
```

Pass Python number to Scheme.

### String

```scheme
procedure: (py/string-from-string string)

procedure: (py/string-as-string *po<string>)
```

### List

```scheme
procedure: (py/list-new)

procedure: (py/list-size)

procedure: (py/list-get-item)

procedure: (py/list-set-item!)

procedure: (py/list-insert!)

procedure: (py/list-append!)

procedure: (py/list-sort!)

procedure: (py/list-reverse!)
```

### Tuple

```scheme
procedure: (py/tuple-new)

procedure: (py/tuple-size)

procedure: (py/tuple-set-item!)

procedure: (py/tuple-get-item)
```

### Dict

```scheme
procedure: (py/dict-new)

procedure: (py/dict-size)

procedure: (py/dict-get-item)

procedure: (py/dict-get-item-string)

procedure: (py/dict-set-item!)

procedure: (py/dict-set-item-string!)

procedure: (py/dict-del-item!)

procedure: (py/dict-del-item-string!)

procedure: (py/dict-clear)

procedure: (py/dict-copy)

procedure: (py/dict-keys)

procedure: (py/dict-values)

procedure: (py/dict-items)
```

### Run

```scheme
procedure: (py/run-simple-file)

procedure: (py/run-file)

procedure: (py/run-file-exflags)

procedure: (py/run-simple-string)

procedure: (py/run-string)

procedure: (py/run-string-flags)
```

### Library

```scheme
procedure: (py/import-import)

procedure: (py/import-import-module)

procedure: (py/import-exec-code-module)
```

### Module

```scheme
procedure: (py/module-new)

procedure: (py/module-get-dict)

procedure: (py/module-get-name)

procedure: (py/module-get-filename)
```

### Object

```scheme
procedure: (py/object-get-attr-string)

procedure: (py/object-call)

procedure: (py/object-call-object)

procedure: (py/object-str)

procedure: (py/callable-check)
```

###Compile

```scheme
procedure: (py-compile-string)
```