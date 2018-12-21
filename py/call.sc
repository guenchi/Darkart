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






(library (fli py call)
    (export
        py-call
    )
    (import
        (scheme)
        (fli py ffi))

    (define py-call
        (lambda (lst)
            (py-initialize)
            (let l ((x (compiler lst)))
                (if (not (null? x))  
                    (begin
                        (eval (car x))
                        (l (cdr x)))))
            (Py_Finalize)))

    (define compiler
        (lambda (lst)
            (define Num
                (lambda (x)
                    (match x
                        (,n (guard (number? n)) n))))
            (define Str
                (lambda (x)
                    (match x
                        (,s (guard (string? s)) s))))
            (define Sym
                (lambda (x)
                    (match x
                        (,s (guard (symbol? s)) s))))
            (match lst
                ((import ,(Sym -> lib)) `(define ,lib (py/import-import-module (symbol->string ,lib))))
                ((import ,(Sym -> lib) as ,(Sym -> l)) `(define ,l (py/import-import-module (symbol->string ,lib)))))))

)