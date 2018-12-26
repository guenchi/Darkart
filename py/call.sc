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






(library (enchantment py call)
    (export
        py-init
        py-fin

        int
        float
        py->int
        py->float
        py-add
        py-sub
        py-mul
        py-div
        py-fdiv
        py-mod
        py-lsh
        py-rsh
        py-and
        py-or
        py-xor
        py-inv
        py-abs
        py-neg

        py-import
        py-get
        py-args
        py-call
        py-func1
        py-func2
        py-func3
        list->py-list
        list->py-tuple
        py-list->list
        py-tuple->list
        vector->py-list
        vector->py-tuple
    )
    (import
        (scheme)
        (enchantment py ffi))



    (define py-init py-initialize)
    (define py-fin py-finalize)
    (define int py/long-from-long)
    (define float py/float-from-double)
    (define py->int py/long-as-long)
    (define py->float py/float-as-double)
    (define py-add py/number-add)
    (define py-sub py/number-subtract)
    (define py-mul py/number-multiply)
    (define py-div py/number-divide)
    (define py-fdiv py/number-floor-divide)
    (define py-mod py/number-divmod)
    (define py-lsh py/number-lshift)
    (define py-rsh py/number-rshift)
    (define py-and py/number-and)
    (define py-or py/number-or)
    (define py-xor py/number-xor)
    (define py-inv py/number-invert)
    (define py-abs py/number-absolute)
    (define py-neg py/number-negative)

    (define py-import
        (lambda (x)
            (py/import-import-module (symbol->string x))))

    (define py-get
        (lambda (x y)
            (py/object-get-attr-string x (symbol->string y))))

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
            
    (define-syntax py-call
        (syntax-rules ()
            ((_ f a ...)(py/object-call-object f (py-args a ...)))))

    (define py-func1
        (lambda (f)
            (lambda (x)
                (py-call f x))))

    (define py-func2
        (lambda (f)
            (lambda (x y)
                (py-call f x y))))

    (define py-func3
        (lambda (f)
            (lambda (x y z)
                (py-call f x y z))))

    
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


