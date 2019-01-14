# Darkart: Python Interface User's Guide

# Build

I haven't prepared the makefile yet, so it will need some manual settings.

Compile `./c/py.c` file with `cc -fPIC -shared  -L/Library/Frameworks/Python.framework/Versions/3.7/lib/ -lpython3.7 -o ../py.so py.c` to make a shared object (.so) file.

Note that depending on the system, you may need to manually specify the path to `python.h`.


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
(py-call array (list->plist int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}

(define np-array
    (lambda (x)
        (py-call array x)))

(np-array (list->plist int '(1 2 3 4 5)))
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

(np-array (list->plist int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}
```

Some python function need named arguments, use:

```scheme
procedure: ((py-call* *po<callable> args ...) alistOfNamedArgs)

return: *po
```
The alist is like: `'((symbol<Name> . *po) ...)`

Exemple:
```scheme
((py-call* array (list->plist int '(1 2 3 4 5)))
    `(('dtype . ,(str "float"))))
=> *op{[1.0, 2.0, 3.0, 4.0, 5.0]}

(define-syntax np-array
    (syntax-rules ()
        ((_ e)(py-call *array e))
        ((_ e (k v) ...)
            ((py-call* *array e) 
                (list (cons k v) ...)))))

(np-array (list->plist int '(1 2 3 4 5)))
=> *op{[1, 2, 3, 4, 5]}

(np-array (list->plist int '(1 2 3 4 5)) ('dtype (str "float")))
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
    (list->plist int '(1 2 3 4 5)))
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
procedure: (*int? *po)

procedure: (*float? *po)

procedure: (*complex? *po)

procedure: (*str? *po*)

return: boolean
```
Type Check for Python's number and string.


```scheme
procedure: (int int)

procedure: (float float)

procedure: (complex cflonum)

procedure: (str string)

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
procedure: (*int *po<number>)

return: number<int>

procedure: (*float *po<number>)

return: number<float>

procedure: (*complex? *po)

return: number<cflonum>

procedure: (*str *po<string>)

return: string
```

Covert a Python data to Scheme data.

Exemple:
```scheme
(*int (int 8))                => 8

(*float (float 3.1415926))    => 3.1415926

(*str (str "foo"))            => "foo"
```

```scheme
procedure: (s->ptype int/float/cflonum/string>)

return: *po<int/float/complex/string>

procedure: (p->stype *po<int/float/complex/string>)

return: int/float/cflonum/string
```

Automatic simple type detection and conversion.

Exemple:
```scheme
(p->stype (int 8))                => 8

(p->stype (float 3.1415926))    => 3.1415926

(p->stype (str "foo"))            => "foo"

```


### List and Tuple

```scheme
procedure: (list->plist* listOf*Po)

procedure: (list->ptuple* listOf*Po)

procedure: (vector->plist* vectorOf*Po)

procedure: (vector->ptuple* vectorOf*Po)

procedure: (list->plist list)

procedure: (list->ptuple list)

procedure: (vector->plist vector)

procedure: (vector->ptuple vector)

procedure: (list->plist type list)

procedure: (list->ptuple type list)

procedure: (vector->plist type vector)

procedure: (vector->ptuple type vector)

return: *po<list,tuple>
```

Covert a Scheme's List and Vector to Python's List and Tuple.

The type procedure will be: `int` `float` `complex` or `str`.

If there is no specific type transfer function, the program will automatically check the type, but it is more efficient when specifying the type.

Exemple:
```scheme
(list->plist int '(1 2 3 4 5 6 7 8))

(vector->plist* `#(,(int 1) ,(float 3.14159) ,(str "foo")))
```
Attention that if don't specific list / vector's type, you have to covert data to *po before make list / vector.

```scheme
procedure: (plist->list *po<list>)

procedure: (ptuple->list *po<tuple>)

procedure: (plist->list *type *po<list>)

procedure: (ptuple->list *type *po<tuple>)

return: list

procedure: (plist->vector *po<list>)

procedure: (ptuple->vector *po<tuple>)

procedure: (plist->vector *type *po<list>)

procedure: (ptuple->vector *type *po<tuple>)

return: vector

procedure: (plist->list* *po<tuple>)

procedure: (ptuple->list* *po<tuple>)

procedure: (plist->list* *type *po<tuple>)

procedure: (ptuple->list* *type *po<tuple>)

return: list of *po

procedure: (plist->vector* *po<list>)

procedure: (ptuple->vector* *po<tuple>)

procedure: (plist->vector* *type *po<list>)

procedure: (ptuple->vector* *type *po<tuple>)

return: vector of *po
```

The *type procedure will be: `*int` `*float` `*complex` or `*str`.

If there is no specific type transfer function, the program will automatically check the type, but it is more efficient when specifying the type.


Exemple:
```scheme
(plist->list *int (list->plist int '(1 2 3 4 5 6 7 8)))
=> (1 2 3 4 5 6 7 8)

(plist->list (list->plist* `(,(int 1) ,(float 3.14159) ,(str "foo"))))
=> (1 3.14159 "foo")

(plist->list* (list->plist* `(,(int 1) ,(float 3.14159) ,(str "foo"))))
=> (*po{1} *po{3.14159} *po{"foo"})
```
In last case it return a list of Memory Adresse of *po. You can use (*int), (*float) or (*str) to convert it to Scheme Data.

Like:
```scheme
(*int (car (plist->list* (list->plist* `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> 1

(*float (cadr (plist->list* (list->plist* `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
=> 3.14159

(*str (caddr (plist->list* (list->plist* `(,(int 1) ,(float 3.14159) ,(str "foo"))))))
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

(py-mul (int 8) (list->plist int' '(1 2 3 4 5)))  => *po{[8, 16, 24, 32, 40]}

(py-abs (int -5))                                   => *po{5} 
```


### Python's list

```scheme
procedure: (plist? *po)

return: boolean

procedure: (make-plist int<size>)

return: *po<list>

procedure: (plist-length *po<list>)

return: int<size>

procedure: (plist-ref *po<list> int<index> ...)

return: *po

procedure: (plist-set! *po<list> int<index> ... *po)

return: 0 on success or -1 on failure.

procedure: (plist-sref *po<list> int<begin> int<end>)

procedure: (plist-sref *po<list> int<index> ... (int<begin> int<end>))

return: *po<list>{slice}

procedure: (plist-sset! *po<list> int<begin> int<end> *po<list>)

procedure: (plist-sset! *po<list> int<index> ... (int<begin> int<end>) *po<list>)

return: 0 on success or -1 on failure.

procedure: (plist-insert! *po<list> int<index> *po)

return: 0 on success or -1 on failure.

procedure: (plist-append! *po<list-target> *po<list>)

return: 0 on success or -1 on failure.

procedure: (plist-sort!)

return: 0 on success or -1 on failure.

procedure: (plist-reverse!)

return: 0 on success or -1 on failure.
```

### Python's tuple

```scheme
procedure: (ptuple? *po)

return: boolean

procedure: (make-ptuple int<index>)

return: *po<tuple>

procedure: (ptuple-length *po<tuple>)

return: int<index>

procedure: (ptuple-ref *po<tuple> int<index> ...)

return: *po

procedure: (ptuple-set! *po<tuple> int<index> ... *po)

return: 0 on success or -1 on failure.

procedure: (ptuple-sref *po<tuple> int<begin> int<end>)

procedure: (ptuple-sref *po<tuple> int<index> ... (int<begin> int<end>))

return: *po<tuple>{slice}
```

### Python's set

```scheme
procedure: (pset? *po)

return: boolean

procedure: (make-pset *po<iterable>)

return: *po<set>

procedure: (pset-length *po<set>)

return: int<size>

procedure: (pset-contains *po<set>)

return: 1 if found, 0 if not found, and -1 if an error is encountered.

procedure: (pset-add! *po<set> *po{key})

return: 0 on success or -1 on failure.

procedure: (pset-del! *po<set> *po{key})

return: 1 if found and removed, 0 if not found and no action taken, and -1 if an error is encountered.

procedure: (pset-pop! *po<set>)

return: *po{arbitrary_key}

procedure: (pset-clear! *po<set>)
```

### Python's sequence

```scheme
procedure: (psequ? *po)

return: boolean

procedure: (psequ->plist *po<sequence>)

return: *po<list>

procedure: (psequ->ptuple *po<sequence>)

return: *po<tuple>

procedure: (psequ-length *po<sequence>)

return: int<size>

procedure: (psequ-append *po<sequence> *po<sequence>)

return: *po<sequence>

procedure: (psequ-repeat *po<sequence> int)

return: *po<sequence>

procedure: (psequ-ref *po<sequence> int)

return: *po

procedure: (psequ-set! *po<sequence> int *po)

return: 0 on success or -1 on failure.

procedure: (psequ-del! *po<sequence> int)

return: 0 on success or -1 on failure.

procedure: (psequ-sref *po<sequence> int int)

return: *po<sequence>{slice}

procedure: (psequ-sset! *po<sequence> int int *po<sequence>)

return: 0 on success or -1 on failure.

procedure: (psequ-sdel! *po<sequence> int int)

return: 0 on success or -1 on failure.

procedure: (psequ-count *po<sequence> *po<value>)

return: int

procedure: (psequ-contains *po<sequence> *po<value>)

return: 1 for true 0 for false -1 for fail

procedure: (psequ-index *po<sequence> *po<value>)

return: int
```

### Python's dict

```scheme
procedure: (pdict? *po)

return: boolean

procedure: (make-pdict)

return: *po<dict>

procedure: (pdict-length *po<dict>)

return: int<size>

procedure: (pdict-ref *po<dict> string<key)

return: *po

procedure: (pdict-ref* *po<dict> *po{key})

return: *po

procedure: (pdict-set! *po<dict> string<key> *po<value>)

return: 0 on success or -1 on failure.

procedure: (pdict-set*! *po<dict> *po{key} *po<value>)

return: 0 on success or -1 on failure.

procedure: (pdict-del! *po<dict> string<key>)

return: 0 on success or -1 on failure.

procedure: (pdict-del*! *po<dict> *po{key})

return: 0 on success or -1 on failure.

procedure: (pdict-clear! *po<dict>)

return: <void>

procedure: (pdict-copy *po<dict>)

return: new *po<dict>

procedure: (pdict-keys *po<dict>)

return: *po<list>{key...}

procedure: (pdict-values)

return: *po<list>{value...}

procedure: (pdict-items)

return: *po<list>{key...values...}
```

### Python's mapping

```scheme
procedure: (pmap? *po)

return: boolean

procedure: (pmap-size *po<mapping>)

return: int

procedure: (pmap-has? *po<mapping> string<key>)

return: boolean

procedure: (pmap-has*? *po<mapping> *po{key})

return: boolean

procedure: (pmap-ref *po<mapping> string<key>)

return: *po

procedure: (pmap-set! *po<mapping> string<key> *po{value}) 

return: 0 on success or -1 on failure.
```

### Display

```scheme
procedure: (py-display *po)
```

Display anything of Python Object.

# Py-FFI

Library `(darkart py ffi)`

This file contains the underlying interface to Python. Usually you won't use it.

### Interpreter

```scheme
procedure: (py-initialize)

procedure: (py-finalize)
```

See `(py-init)` and `(py-fin)` in `(darkart py call)`

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

procedure: (py/int-from-size_t size_t)

procedure: (py/int-from-ssize_t ssize_t)

return: *po<int>
```

```scheme
procedure: (py/long-from-long number)

procedure: (py/long-from-unsigned-long number)

procedure: (py/long-from-longlong number)

procedure: (py/long-from-unsigned-longlong number)

procedure: (py/long-from-double number)

procedure: (py/long-from-size_t size_t)

procedure: (py/long-from-ssize_t ssize_t)

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
procedure: (py/string-check? *po)

return: boolean
```

```scheme
procedure: (py/string-from-string string)

return: *po<string>
```

Pass Scheme string to Python.

```scheme
procedure: (py/string-as-string *po<string>)

return: string
```

Pass Python string to Scheme.

### List

```scheme
procedure: (py/list-check? *po)

return: boolean

procedure: (py/list-new int<size>)

return: *po<list>

procedure: (py/list-size *po<list>)

return: int<size>

procedure: (py/list-get-item *po<list> int<index>)

return: *po

procedure: (py/list-set-item! *po<list> int<index> *po)

return: 0 on success or -1 on failure.

procedure: (py/list-get-slice *po<list> int int)

return: *po<list>{slice}

procedure: (py/list-set-slice! *po<list> int int *po<list>)

return: 0 on success or -1 on failure.

procedure: (py/list-insert! *po<list> int<index> *po)

return: 0 on success or -1 on failure.

procedure: (py/list-append! *po<list-target> *po<list>)

return: 0 on success or -1 on failure.

procedure: (py/list-sort!)

return: 0 on success or -1 on failure.

procedure: (py/list-reverse!)

return: 0 on success or -1 on failure.
```

### Tuple

```scheme
procedure: (py/tuple-check? *po)

return: boolean

procedure: (py/tuple-new int<index>)

return: *po<tuple>

procedure: (py/tuple-size *po<tuple>)

return: int<index>

procedure: (py/tuple-get-item *po<tuple> int<index>)

return: *po

procedure: (py/tuple-set-item! *po<tuple> int<index> *po)

return: 0 on success or -1 on failure.

procedure: (py/tuple-get-slice *po<tuple> int int)

return: *po<tuple>{slice}
```

### Set

```scheme
procedure: (py/set-check? *po)

return: boolean

procedure: (py/set-new *po<iterable>)

return: *po<set>

procedure: (py/set-size *po<set>)

return: int<size>

procedure: (py/set-contains *po<set>)

return: 1 if found, 0 if not found, and -1 if an error is encountered.

procedure: (py/set-add! *po<set> *po{key})

return: 0 on success or -1 on failure.

procedure: (py/set-discard! *po<set> *po{key})

return: 1 if found and removed, 0 if not found and no action taken, and -1 if an error is encountered.

procedure: (py/set-pop! *po<set>)

return: *po{arbitrary_key}

procedure: (py/set-clear! *po<set>)
```

### Sequence  
        
```scheme
procedure: (py/sequence-check? *po)

return: boolean

procedure: (py/sequence-list *po<sequence>)

return: *po<list>

procedure: (py/sequence-tuple *po<sequence>)

return: *po<tuple>

procedure: (py/sequence-size *po<sequence>)

return: int<size>

procedure: (py/sequence-concat *po<sequence> *po<sequence>)

return: *po<sequence>

procedure: (py/sequence-repeat *po<sequence> int)

return: *po<sequence>

procedure: (py/sequence-get-item *po<sequence> int)

return: *po

procedure: (py/sequence-set-item! *po<sequence> int *po)

return: 0 on success or -1 on failure.

procedure: (py/sequence-del-item! *po<sequence> int)

return: 0 on success or -1 on failure.

procedure: (py/sequence-get-slice *po<sequence> int int)

return: *po<sequence>{slice}

procedure: (py/sequence-set-slice! *po<sequence> int int *po<sequence>)

return: 0 on success or -1 on failure.

procedure: (py/sequence-del-slice! *po<sequence> int int)

return: 0 on success or -1 on failure.

procedure: (py/sequence-count *po<sequence> *po<value>)

return: int

procedure: (py/sequence-contains *po<sequence> *po<value>)

return: 1 for true 0 for false -1 for fail

procedure: (py/sequence-index *po<sequence> *po<value>)

return: int
```


### Dictionary

```scheme
procedure: (py/dict-check? *po)

return: boolean

procedure: (py/dict-new)

return: *po<dict>

procedure: (py/dict-size *po<dict>)

return: int<size>

procedure: (py/dict-get-item *po<dict> *po{key})

return: *po

procedure: (py/dict-get-item-string *po<dict> string<key>)

return: *po

procedure: (py/dict-set-item! *po<dict> *po{key} *po<value>)

return: 0 on success or -1 on failure.

procedure: (py/dict-set-item-string! *po<dict> string<key> *po<value>)

return: 0 on success or -1 on failure.

procedure: (py/dict-del-item! *po<dict> *po{key})

return: 0 on success or -1 on failure.

procedure: (py/dict-del-item-string! *po<dict> string<key>)

return: 0 on success or -1 on failure.

procedure: (py/dict-clear! *po<dict>)

return: <void>

procedure: (py/dict-copy *po<dict>)

return: new *po<dict>

procedure: (py/dict-keys *po<dict>)

return: *po<list>{key...}

procedure: (py/dict-values)

return: *po<list>{value...}

procedure: (py/dict-items)

return: *po<list>{key...values...}
```

### Mapping

```scheme
procedure: (py/mapping-check? *po)

return: boolean

procedure: (py/mapping-size *po<mapping>)

return: int

procedure: (py/mapping-has-key-string? *po<mapping> string<key>)

return: boolean

procedure: (py/mapping-has-key? *po<mapping> *po{key})

return: boolean

procedure: (py/mapping-get-item-string *po<mapping> string<key>)

return: *po

procedure: (py/mapping-set-item-string! *po<mapping> string<key> *po{value}) 

return: 0 on success or -1 on failure.
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

procedure: (py/import-import-module string<library>)

return: *po<library>

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
procedure: (py/object-get-attr-string *po string<keyword>)

return: *po<object,method>

procedure: (py/object-call *po<callable> *po<tulpe>{argument...} *po<dict>{name:argument...})

return: *po

procedure: (py/object-call-object *po<callable> *po<tuple>{argument...)

return: *po

procedure: (py/object-str *po)

return: *po<string>

procedure: (py/callable-check? *po)

return: boolean
```

### Compile

```scheme
procedure: (py-compile-string)
```