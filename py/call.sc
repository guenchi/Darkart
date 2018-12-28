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
        py-incref
        py-decref

        int
        float
        str
        py->int
        py->float
        py->str
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
        py-args*
        py-call
        py-call*
        py-func
        py-func*
        list->py-list
        list->py-list*
        list->py-tuple
        list->py-tuple*
        py-list->list
        py-tuple->list
        vector->py-list
        vector->py-list*
        vector->py-tuple
        vector->py-tuple*
        py-list->vector
        py-tuple->vector
        alist->py-dict

        py-display
    )
    (import
        (scheme)
        (enchantment py ffi))



    (define py-init py-initialize)
    (define py-fin py-finalize)
    (define int py/long-from-long)
    (define float py/float-from-double)
    (define str py/string-from-string)
    (define py->int py/long-as-long)
    (define py->float py/float-as-double)
    (define py->str py/string-as-string)
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
            (let loop ((n 0)(args args))
                (if (< n len)
                    (begin 
                        (py/tuple-set-item! *p n (car args))
                        (loop (+ n 1) (cdr args))
                    *p)))))


    (define py-args*
        (lambda (args) 
            (define len (length args))
            (define *p (py/tuple-new len))
            (let loop ((n 0)(args args))
                (if (< n len)
                    (begin 
                        (py/tuple-set-item! *p n (car args))
                        (loop (+ n 1) (cdr args))
                    *p)))))
            

    (define py-call
        (lambda (f . args)
            (define *k (py-args* args))
            (define *r (py/object-call-object f *k))
            (py-decref *k)
            *r))


    (define py-call*
        (lambda (f . args)
            (lambda (lst)
                (define *k (py-args* args))
                (define *d (alist->py-dict lst))
                (define *r (py/object-call f *k *d))
                (py-decref *k)
                (py-decref *d)
                *r)))


    (define py-func
        (lambda (f)
            (lambda args
                (define *k (py-args* args))
                (define *r (py/object-call-object f *k))
                (py-decref *k)
                *r)))


    (define py-func*
        (lambda (f)
            (lambda args
                (lambda (lst)
                    (define *k (py-args* args))
                    (define *d (alist->py-dict lst))
                    (define *r (py/object-call f *k *d))
                    (py-decref *k)
                    (py-decref *d)
                    *r))))


    (define list->py-list
        (lambda (t lst)
            (define len (length lst))
            (define *p (py/list-new len))
            (define f
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (list? x)
                        (list->py-list t x)
                        (f x))))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (begin
                        (py/list-set-item! *p n (g (car lst)))
                        (l (+ n 1) (cdr lst)))
                    *p))))


    (define list->py-list*
        (lambda (lst)
            (define len (length lst))
            (define *p (py/list-new len))
            (define f
                (lambda (x)
                    (if (list? x)
                        (list->py-list* x)
                        x)))
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
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (list? x)
                        (list->py-tuple t x)
                        (f x))))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (begin
                        (py/tuple-set-item! *p n (g (car lst)))
                        (l (+ n 1) (cdr lst)))
                    *p))))

                    
    (define list->py-tuple*
        (lambda (lst)
            (define len (length lst))
            (define *p (py/tuple-new len))
            (define f
                (lambda (x)
                    (if (list? x)
                        (list->py-tuple* x)
                        x)))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (begin
                        (py/tuple-set-item! *p n (f (car lst)))
                        (l (+ n 1) (cdr lst)))
                    *p))))
    

    (define-syntax py-list->list
        (syntax-rules ()
            ((_ *p)
                (let 
                    ((len (py/list-size *p)))
                    (let loop ((n 0))
                        (if (< n len)
                            (cons (py/list-get-item *p n) (loop (+ n 1)))
                        '()))))
            ((_ t *p)
                (let 
                    ((len (py/list-size *p))
                    (f
                        (case t
                            ('int py->int)
                            ('float py->float)
                            ('str str))))
                    (let loop ((n 0))
                        (if (< n len)
                            (cons (f (py/list-get-item *p n)) (loop (+ n 1)))
                        '()))))))


    (define-syntax py-tuple->list
        (syntax-rules ()
            ((_ *p)
                (let 
                    ((len (py/tuple-size *p)))
                    (let loop ((n 0))
                        (if (< n len)
                            (cons (py/tuple-get-item *p n) (loop (+ n 1)))
                        '()))))
            ((_ t *p)
                (let 
                    ((len (py/tuple-size *p))
                    (f
                        (case t
                            ('int py->int)
                            ('float py->float)
                            ('str str))))
                    (let loop ((n 0))
                        (if (< n len)
                            (cons (f (py/tuple-get-item *p n)) (loop (+ n 1)))
                        '()))))))


    (define vector->py-list
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (py/list-new len))
            (define f
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (vector? x)
                        (vector->py-list t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (begin
                        (py/list-set-item! *p n (g (vector-ref vct n)))
                        (l (+ n 1)))
                    *p))))


    (define vector->py-list*
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (py/list-new len))
            (define f
                (lambda (x)
                    (if (vector? x)
                        (vector->py-list* t x)
                        (f x))))
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
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (vector? x)
                        (vector->py-tuple t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (begin
                        (py/tuple-set-item! *p n (g (vector-ref vct n)))
                        (l (+ n 1)))
                    *p))))

    (define vector->py-tuple*
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (py/tuple-new len))
            (define f
                (lambda (x)
                    (if (vector? x)
                        (vector->py-tuple* t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (begin
                        (py/tuple-set-item! *p n (f (vector-ref vct n)))
                        (l (+ n 1)))
                    *p))))


    (define-syntax py-list->vector
        (syntax-rules ()
            ((_ *p)
                (let 
                    ((len (py/list-size *p)))
                    (let loop ((v (make-vector len))(n 0))
                        (if (< n len)
                            (begin 
                                (vector-set! v n (py/list-get-item *p n))
                                (loop v (+ n 1)))
                        v))))
            ((_ t *p)
                (let 
                    ((len (py/list-size *p))
                    (f
                        (case t
                            ('int py->int)
                            ('float py->float)
                            ('str py->str))))
                    (let loop ((v (make-vector len))(n 0))
                        (if (< n len)
                            (begin 
                                (vector-set! v n (f (py/list-get-item *p n)))
                                (loop v (+ n 1)))
                        v))))))


    (define-syntax py-tuple->vector
        (syntax-rules ()
            ((_ *p)
                (let 
                    ((len (py/tuple-size *p)))
                    (let loop ((v (make-vector len))(n 0))
                        (if (< n len)
                            (begin 
                                (vector-set! v n (py/tuple-get-item *p n))
                                (loop v (+ n 1)))
                        v))))
            ((_ t *p)
                (let 
                    ((len (py/tuple-size *p))
                    (f
                        (case t
                            ('int py->int)
                            ('float py->float)
                            ('str py->str))))
                    (let loop ((v (make-vector len))(n 0))
                        (if (< n len)
                            (begin 
                                (vector-set! v n (f (py/tuple-get-item *p n)))
                                (loop v (+ n 1)))
                        v))))))
    

    (define-syntax alist->py-dict
        (syntax-rules ()
            ((_ lst)
                    (let loop 
                        ((*p (py/dict-new))
                        (i (car lst))
                        (r (cdr lst)))
                        (py/dict-set-item-string! *p (car i) (cdr i))
                        (if (null? r)
                            *p
                            (loop *p (car r)(cdr r)))))
            ((_ t lst)
                    (let loop 
                        ((*p (py/dict-new))
                        (f 
                            (case t
                                ('int int)
                                ('float float)
                                ('str str)))
                        (i (car lst))
                        (r (cdr lst)))
                        (py/dict-set-item-string! *p (car i) (f (cdr i)))
                        (if (null? r)
                            *p
                            (loop *p f (car r)(cdr r)))))))    

   
(define py-display
    (lambda (x)
        (display 
            (py->str
                (py/object-str x)))))


)


