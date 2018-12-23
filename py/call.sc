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
        py-args
        list->py-list
        list->py-tuple
        py-list->list
        py-tuple->list
        vector->py-list
        vector->py-tuple
    )
    (import
        (scheme)
        (match match)
        (fli py ffi))

    (define py-call
        (lambda (lst)
            (py-initialize)
            (let l ((lst lst))
                (if (not (null? lst))  
                    (begin
                        (eval (parse (car lst)))
                        (l (cdr lst)))))
            (py-finalize)))

            
    (define parse
        (lambda (lst)
            (define Sym
                (lambda (x)
                    (match x
                        (,s (guard (symbol? s)) s))))
            (define Var
                (lambda (x)
                    (match x
                        (,s (guard (symbol? s)) s)
                        (,(Expr -> f) f))))
            (define Expr 
                (lambda (x)
                    (match x
                        ((int ,x) `(py/long-from-long ,x))
                        ((float ,x) `(py/float-from-double ,x))
                        ((list->py-list ,t ,x) `(list->py-list ,t ,x))
                        ((list->py-tuple ,t ,x) `(list->py-tuple ,t ,x))
                        ((vector->py-list ,t ,x) `(vector->py-list ,t ,x))
                        ((vector->py-tuple ,t ,x) `(vector->py-tuple ,t ,x))
                        ((+ ,(Var -> x) ,(Var -> y)) `(py/number-add ,x ,y))
                        ((- ,(Var -> x) ,(Var -> y)) `(py/number-subtract ,x ,y))
                        ((* ,(Var -> x) ,(Var -> y)) `(py/number-multiply ,x ,y))
                        ((/ ,(Var -> x) ,(Var -> y)) `(py/number-divide ,x ,y))
                        ((// ,(Var -> x) ,(Var -> y)) `(py/number-floor-divide ,x ,y))
                        ((mod ,(Var -> x) ,(Var -> y)) `(py/number-divmod ,x ,y))
                        ((<< ,(Var -> x) ,(Var -> y)) `(py/number-lshift ,x ,y))
                        ((>> ,(Var -> x) ,(Var -> y)) `(py/number-rshift ,x ,y))
                        ((and ,(Var -> x) ,(Var -> y)) `(py/number-and ,x ,y))
                        ((or ,(Var -> x) ,(Var -> y)) `(py/number-or ,x ,y))
                        ((xor ,(Var -> x) ,(Var -> y)) `(py/number-xor ,x ,y))
                        ((~ ,(Var -> x)) `(py/number-invert ,x))
                        ((abs ,(Var -> x)) `(py/number-absolute ,x))
                        ((- ,(Var -> x)) `(py/number-negative ,x))
                        ((,(Sym -> f) ,(Var -> x) ...) `(py/object-call-object ,f (py-args ,x ...))))))
            (match lst
                ((define ,(Sym -> x) ,y) `(define ,x ,(parse y)))
                ((int ,x) `(py/long-from-long ,x))
                ((py->int ,(Var -> x)) `(py/long-as-long ,x))
                ((float ,x) `(py/float-from-double ,x))
                ((py->float ,(Var -> x)) `(py/float-as-double ,x))
                ((list->py-list ,t ,x) `(list->py-list ,t ,x))
                ((list->py-tuple ,t ,x) `(list->py-tuple ,t ,x))
                ((py-list->list ,t ,(Var -> x)) `(py-list->list ,t ,x))
                ((py-tuple->list ,t ,(Var -> x)) `(py-tuple->list ,t ,x))
                ((vector->py-list ,t ,x) `(vector->py-list ,t ,x))
                ((vector->py-tuple ,t ,x) `(vector->py-tuple ,t ,x))
                ((import ,(Sym -> lib)) 
                    `(define ,lib (py/import-import-module ,(symbol->string lib))))
                ((import ,(Sym -> lib) as ,(Sym -> l)) 
                    `(define ,l (py/import-import-module ,(symbol->string lib))))
                ((get ,(Sym -> o) ,(Sym -> x))
                    `(define ,x (py/object-get-attr-string ,o ,(symbol->string x))))
                ((get ,(Sym -> o) ,(Sym -> x) as ,(Sym -> k))
                    `(define ,k (py/object-get-attr-string ,o ,(symbol->string x))))
                (,(Expr -> f) f))))


    (define py-args
        (lambda args 
            (define len (length args))
            (define *p (py/tuple-new len))
            (let l ((n 0)(args args))
                (if (< n len)
                    (begin 
                        (py/tuple-set-item! *p n (car args))
                        (l (+ n 1) (cdr args))
                    *p)))))

    
    (define list->py-list
        (lambda (t lst)
            (define len (length lst))
            (define *p (py/list-new len))
            (define f
                (case t
                    ('int py/long-from-long)
                    ('float py/float-from-double)))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (begin
                        (py/list-set-item! *p n (f (car lst)))
                        (l (+ n 1) (cdr lst)))
                    *p))))

    (define list->py-tuple
        (lambda (t lst)
            (define len (length lst))
            (define *p (py/tuple-new len))
            (define f
                (case t
                    ('int py/long-from-long)
                    ('float py/float-from-double)))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (begin
                        (py/tuple-set-item! *p n (f (car lst)))
                        (l (+ n 1) (cdr lst)))
                    *p))))
    
    (define py-list->list
        (lambda (t *p)
            (define len (py/list-size *p))
            (define f
                (case t
                    ('int py/long-as-long)
                    ('float py/float-as-double)))
            (let l ((n 0))
                (if (< n len)
                    (cons (f (py/list-get-item *p n)) (l (+ n 1)))
                    '()))))


    (define py-tuple->list
        (lambda (t *p)
            (define len (py/tuple-size *p))
            (define f
                (case t
                    ('int py/long-as-long)
                    ('float py/float-as-double)))
            (let l ((n 0))
                (if (< n len)
                    (cons (f (py/tuple-get-item *p n)) (l (+ n 1)))
                    '()))))



    (define vector->py-list
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (py/list-new len))
            (define f
                (case t
                    ('int py/long-from-long)
                    ('float py/float-from-double)))
            (let l ((n 0))
                (if (< n len)
                    (begin
                        (py/list-set-item! *p n (f (vector-ref vct n)))
                        (l (+ n 1)))
                    *p))))
    
    (define vector->py-tuple
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (py/tuple-new len))
            (define f
                (case t
                    ('int py/long-from-long)
                    ('float py/float-from-double)))
            (let l ((n 0))
                (if (< n len)
                    (begin
                        (py/tuple-set-item! *p n (f (vector-ref vct n)))
                        (l (+ n 1)))
                    *p))))
    
    (define alist->py-dict
        (lambda (lst)
            (define *p (py/dict-new))
            (let l ((i (car lst))(r (cdr lst)))
                (py/dict-set-item! *p (py/string-from-string (car i)) (py/long-from-long (cdr i)))
                (if (null? r)
                    *p
                    (l (car r)(cdr r))))))    



)
