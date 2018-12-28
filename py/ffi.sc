;  MIT License

;  Copyright guenchi (c) 2018 
         
;  Permission is hereby granted, free of charge, to any person obtaining a copy
;  of this software and associated documentation files (the "Software"), to deal
;  in the Software without restriction, including without limitation the rights
;  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
;  copies of the Software, and to permit persons to whom the Software is
;  furnished to do so, subject to the following conditions:
         
;  The above copyright notice and this permission notice shall be included in all
;  copies or substantial portions of the Software.
         
;  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
;  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
;  SOFTWARE.






(library (darkart py ffi)
    (export
        py-sigle-input
        py-file-input
        py-eval-input

        py-initialize
        py-finalize
        py-incref
        py-decref
        py-new-interpreter
        py-end-interpreter

        py/number-add
        py/number-subtract
        py/number-multiply
        py/number-divide
        py/number-floor-divide
        py/number-divmod
        py/number-lshift
        py/number-rshift
        py/number-and
        py/number-or
        py/number-xor
        py/number-invert
        py/number-absolute
        py/number-negative

        py/int-from-long
        py/int-from-size_t
        py/int-from-ssize_t
        py/int-as-long
        py/int-as-ssize_t
        
        py/long-from-long
        py/long-from-unsigned-long
        py/long-from-longlong
        py/long-from-unsigned-longlong
        py/long-from-double
        py/long-from-size_t
        py/long-from-ssize_t
        py/long-as-long
        py/long-as-unsigned-long
        py/long-as-longlong
        py/long-as-unsigned-longlong
        py/long-as-double
        py/long-as-ssize_t

        py/float-from-double
        py/float-as-double
        
        py/string-from-string
        py/string-as-string

        py/list-new
        py/list-size
        py/list-get-item
        py/list-set-item!
        py/list-insert!
        py/list-append!
        py/list-sort!
        py/list-reverse!

        py/tuple-new
        py/tuple-size
        py/tuple-set-item!
        py/tuple-get-item

        py/dict-new
        py/dict-size
        py/dict-get-item
        py/dict-get-item-string
        py/dict-set-item!
        py/dict-set-item-string!
        py/dict-del-item!
        py/dict-del-item-string!
        py/dict-clear
        py/dict-copy
        py/dict-keys
        py/dict-values
        py/dict-items

        py/run-simple-file
        py/run-file
        py/run-file-exflags
        py/run-simple-string
        py/run-string
        py/run-string-flags

        py/import-import
        py/import-import-module
        py/import-exec-code-module

        py/module-new
        py/module-get-dict
        py/module-get-name
        py/module-get-filename
        
        py/object-get-attr-string
        py/object-call
        py/object-call-object
        py/object-str
        py/callable-check
    
        py-compile-string
        )
    (import
        (scheme))

(define lib (load-shared-object "./lib/darkart/py.so"))

(define py-sigle-input 256)
(define py-file-input 257)
(define py-eval-input 258)


(define py-initialize
    (foreign-procedure "Py_Initialize" () void))

(define py-finalize
    (foreign-procedure "Py_Finalize" () void))

(define py-incref
    (foreign-procedure "Py_IncRef" (uptr) void))

(define py-decref
    (foreign-procedure "Py_DecRef" (uptr) void))

(define py-new-interpreter
    (foreign-procedure "Py_NewInterpreter" () uptr))

(define py-end-interpreter
    (foreign-procedure "Py_EndInterpreter" (uptr) void))

(define py/number-add
    (foreign-procedure "PyNumber_Add" (uptr uptr) uptr))

(define py/number-subtract
    (foreign-procedure "PyNumber_Subtract" (uptr uptr) uptr))

(define py/number-multiply
    (foreign-procedure "PyNumber_Multiply" (uptr uptr) uptr))

(define py/number-divide
    (foreign-procedure "PyNumber_Divide" (uptr uptr) uptr))

(define py/number-floor-divide
    (foreign-procedure "PyNumber_FloorDivide" (uptr uptr) uptr))

(define py/number-divmod
    (foreign-procedure "PyNumber_Divmod" (uptr uptr) uptr))

(define py/number-lshift
    (foreign-procedure "PyNumber_Lshift" (uptr uptr) uptr))

(define py/number-rshift
    (foreign-procedure "PyNumber_Rshift" (uptr uptr) uptr))

(define py/number-and
    (foreign-procedure "PyNumber_And" (uptr uptr) uptr))

(define py/number-or
    (foreign-procedure "PyNumber_Or" (uptr uptr) uptr))

(define py/number-xor
    (foreign-procedure "PyNumber_Xor" (uptr uptr) uptr))

(define py/number-invert
    (foreign-procedure "PyNumber_Invert" (uptr) uptr))

(define py/number-absolute
    (foreign-procedure "PyNumber_Absolute" (uptr) uptr))

(define py/number-negative
    (foreign-procedure "PyNumber_Negative" (uptr) uptr))

(define py/int-from-long
    (foreign-procedure "PyInt_FromLong" (long) uptr))

(define py/int-from-size_t
    (foreign-procedure "PyInt_FromSize_t" (size_t) uptr))

(define py/int-from-ssize_t
    (foreign-procedure "PyInt_FromSsize_t" (ssize_t) uptr))

(define py/int-as-long
    (foreign-procedure "PyInt_AsLong" (uptr) long))

(define py/int-as-ssize_t
    (foreign-procedure "PyInt_AsSsize_t" (uptr) ssize_t))

(define py/long-from-long
    (foreign-procedure "PyLong_FromLong" (long) uptr))

(define py/long-from-unsigned-long
    (foreign-procedure "PyLong_FromUnsignedLong" (unsigned-long) uptr))

(define py/long-from-longlong
    (foreign-procedure "PyLong_FromLongLong" (long-long) uptr))

(define py/long-from-unsigned-longlong
    (foreign-procedure "PyLong_FromUnsignedLongLong" (unsigned-long-long) uptr))

(define py/long-from-double
    (foreign-procedure "PyLong_FromDouble" (double) uptr))

(define py/long-from-size_t
    (foreign-procedure "PyLong_FromSize_t" (size_t) uptr))

(define py/long-from-ssize_t
    (foreign-procedure "PyLong_FromSsize_t" (ssize_t) uptr))

(define py/long-as-long
    (foreign-procedure "PyLong_AsLong" (uptr) long))

(define py/long-as-unsigned-long
    (foreign-procedure "PyLong_AsUnsignedLong" (uptr) unsigned-long))

(define py/long-as-longlong
    (foreign-procedure "PyLong_AsLongLong" (uptr) long-long))

(define py/long-as-unsigned-longlong
    (foreign-procedure "PyLong_AsUnsignedLongLong" (uptr) unsigned-long-long))

(define py/long-as-double
    (foreign-procedure "PyLong_AsDouble" (uptr) double))

(define py/long-as-ssize_t
    (foreign-procedure "PyLong_AsSsize_t" (uptr) ssize_t))

(define py/float-from-double
    (foreign-procedure "PyFloat_FromDouble" (double) uptr))
    
(define py/float-as-double
    (foreign-procedure "PyFloat_AsDouble" (uptr) double))

(define py/string-from-string
    (foreign-procedure "PyString_FromString" (string) uptr))

(define py/string-as-string
    (foreign-procedure "PyString_AsString" (uptr) string))

(define py/list-new
    (foreign-procedure "PyList_New" (int) uptr))

(define py/list-size
    (foreign-procedure "PyList_Size" (uptr) ssize_t))

(define py/list-get-item
    (foreign-procedure "PyList_GetItem" (uptr ssize_t) uptr))

(define py/list-set-item!
    (foreign-procedure "PyList_SetItem" (uptr ssize_t uptr) int))

(define py/list-insert!
    (foreign-procedure "PyList_Insert" (uptr ssize_t uptr) int))

(define py/list-append!
    (foreign-procedure "PyList_Append" (uptr uptr) int))

(define py/list-sort!
    (foreign-procedure "PyList_Sort" (uptr) int))

(define py/list-reverse!
    (foreign-procedure "PyList_Reverse" (uptr) int))

(define py/tuple-new
    (foreign-procedure "PyTuple_New" (int) uptr))

(define py/tuple-size
    (foreign-procedure "PyTuple_Size" (uptr) ssize_t))

(define py/tuple-get-item
    (foreign-procedure "PyTuple_GetItem" (uptr ssize_t) uptr))

(define py/tuple-set-item!
    (foreign-procedure "PyTuple_SetItem" (uptr ssize_t uptr) int))

(define py/dict-new
    (foreign-procedure "PyDict_New" () uptr))

(define py/dict-get-item
    (foreign-procedure "PyDict_GetItem" (uptr uptr) uptr))

(define py/dict-get-item-string
    (foreign-procedure "PyDict_GetItemString" (uptr string) uptr))

(define py/dict-set-item!
    (foreign-procedure "PyDict_SetItem" (uptr uptr uptr) int))

(define py/dict-set-item-string!
    (foreign-procedure "PyDict_SetItemString" (uptr string uptr) int))

(define py/dict-del-item!
    (foreign-procedure "PyDict_DelItem" (uptr uptr) int))

(define py/dict-del-item-string!
    (foreign-procedure "PyDict_DelItemString" (uptr string) uptr))

(define py/dict-clear
    (foreign-procedure "PyDict_Clear" (uptr) void))

(define py/dict-size
    (foreign-procedure "PyDict_Size" (uptr) ssize_t))

(define py/dict-keys
    (foreign-procedure "PyDict_Keys" (uptr) uptr))

(define py/dict-values
    (foreign-procedure "PyDict_Values" (uptr) uptr))

(define py/dict-items
    (foreign-procedure "PyDict_Items" (uptr) uptr))

(define py/dict-copy
    (foreign-procedure "PyDict_Copy" (uptr) uptr))

(define py/run-simple-file
    (foreign-procedure "PyRun_SimpleFile" (uptr string) int))

(define py/run-file
    (foreign-procedure "PyRun_File" (uptr string int uptr uptr) uptr))

(define py/run-file-exflags
    (foreign-procedure "PyRun_FileExFlags" (uptr string int uptr uptr int uptr) uptr))

(define py/run-simple-string
    (foreign-procedure "PyRun_SimpleString" (string) int))

(define py/run-string
    (foreign-procedure "PyRun_String" (string int uptr uptr) uptr))

(define py/run-string-flags
    (foreign-procedure "PyRun_StringFlags" (string int uptr uptr uptr) uptr))
    
(define py/import-import
    (foreign-procedure "PyImport_Import" (uptr) uptr))

(define py/import-import-module
    (foreign-procedure "PyImport_ImportModule" (string) uptr))

(define py/import-exec-code-module
    (foreign-procedure "PyImport_ExecCodeModule" (string uptr) uptr))

(define py/module-new
    (foreign-procedure "PyModule_New" (string) uptr))

(define py/module-get-dict
    (foreign-procedure "PyModule_GetDict" (uptr) uptr))

(define py/module-get-name
    (foreign-procedure "PyModule_GetName" (uptr) string))

(define py/module-get-filename
    (foreign-procedure "PyModule_GetFilename" (uptr) string))

(define py/object-get-attr
    (foreign-procedure "PyObject_GetAttr" (uptr uptr) uptr))

(define py/object-set-attr!
    (foreign-procedure "PyObject_SetAttr" (uptr uptr uptr) int))

(define py/object-has-attr
    (foreign-procedure "PyObject_HasAttr" (uptr uptr) int))

(define py/object-get-attr-string
    (foreign-procedure "PyObject_GetAttrString" (uptr string) uptr))

(define py/object-set-attr-string!
    (foreign-procedure "PyObject_SetAttrString" (uptr string uptr) int))

(define py/object-has-attr-string
    (foreign-procedure "PyObject_HasAttrString" (uptr string) int))

(define py/object-call
    (foreign-procedure "PyObject_Call" (uptr uptr uptr) uptr))

(define py/object-call-object
    (foreign-procedure "PyObject_CallObject" (uptr uptr) uptr))

(define py/object-str 
    (foreign-procedure "PyObject_Str" (uptr) uptr))

(define py/callable-check
    (foreign-procedure "PyCallable_Check" (uptr) int))

(define py-compile-string
    (foreign-procedure "Py_CompileString" (string string int) uptr))



)