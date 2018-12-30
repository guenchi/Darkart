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






(library (darkart py call)
    (export
        py-init
        py-fin
        py-incref
        py-decref

        pint?
        pfloat?
        pstr?
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
        list->plist
        list->plist*
        list->ptuple
        list->ptuple*
        plist->list
        plist->list*
        ptuple->list
        ptuple->list*
        vector->plist
        vector->plist*
        vector->ptuple
        vector->ptuple*
        plist->vector
        plist->vector*
        ptuple->vector
        ptuple->vector*
        alist->pdict
        alist->pdict*

        plist?
        make-plist 
        plist-length 
        plist-ref 
        plist-set!
        plist-sref
        plist-sset!
        plist-insert! 
        plist-append! 
        plist-sort! 
        plist-reverse! 
    
        ptuple?
        make-ptuple 
        ptuple-length 
        ptuple-ref 
        ptuple-set!
        ptuple-sref 

        pset?
        make-pset
        pset-length
        pset-contains?
        pset-add!
        pset-del!
        pset-pop!
        pset-clear!
        
        psequ?
        psequ->plist
        psequ->ptuple
        psequ-length
        psequ-append
        psequ-repeat
        psequ-ref
        psequ-set!
        psequ-del!
        psequ-sref
        psequ-sset!
        psequ-sdel!
        psequ-count
        psequ-contains
        psequ-index
    
        pdict?
        make-pdict 
        pdict-length 
        pdict-ref 
        pdict-ref* 
        pdict-set! 
        pdict-set*! 
        pdict-del! 
        pdict-del*! 
        pdict-clear!
        pdict-copy 
        pdict-keys 
        pdict-values 
        pdict-items 

        pmap?
        pmap-size
        pmap-has?
        pmap-has*?
        pmap-ref
        pmap-set!

        py-display
    )
    (import
        (scheme)
        (darkart py ffi))



    (define py-init py-initialize)
    (define py-fin py-finalize)

    (define pint? py/long-check?)
    (define pfloat? py/float-check?)
    (define pstr? py/string-check?)
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

    (define plist? py/list-check?)
    (define make-plist py/list-new)
    (define plist-length py/list-size)
    (define plist-ref py/list-get-item)
    (define plist-set! py/list-set-item!)
    (define plist-sref py/list-get-slice)
    (define plist-sset! py/list-set-slice!)
    (define plist-insert! py/list-insert!)
    (define plist-append! py/list-append!)
    (define plist-sort! py/list-sort!)
    (define plist-reverse! py/list-reverse!)

    (define ptuple? py/tuple-check?)
    (define make-ptuple py/tuple-new)
    (define ptuple-length py/tuple-size)
    (define ptuple-ref py/tuple-get-item)
    (define ptuple-set! py/tuple-set-item!)
    (define ptuple-sref py/tuple-get-slice)

    (define pset? py/set-check?)
    (define make-pset py/set-new)
    (define pset-length py/set-size)
    (define pset-contains? py/set-contains?)
    (define pset-add! py/set-add!)
    (define pset-del! py/set-discard!)
    (define pset-pop! py/set-pop!)
    (define pset-clear! py/set-clear!)
    
    (define psequ? py/sequence-check?)
    (define psequ->plist py/sequence-list)
    (define psequ->ptuple py/sequence-tuple)
    (define psequ-length py/sequence-size)
    (define psequ-append py/sequence-concat)
    (define psequ-repeat py/sequence-repeat)
    (define psequ-ref py/sequence-get-item)
    (define psequ-set! py/sequence-set-item!)
    (define psequ-del! py/sequence-del-item!)
    (define psequ-sref py/sequence-get-slice)
    (define psequ-sset! py/sequence-set-slice!)
    (define psequ-sdel! py/sequence-del-slice!)
    (define psequ-count py/sequence-count)
    (define psequ-contains py/sequence-contains)
    (define psequ-index py/sequence-index)
    
    (define pdict? py/dict-check?)
    (define make-pdict py/dict-new)
    (define pdict-length py/dict-size)
    (define pdict-ref py/dict-get-item-string)
    (define pdict-ref* py/dict-get-item)
    (define pdict-set! py/dict-set-item-string!)
    (define pdict-set*! py/dict-set-item!)
    (define pdict-del! py/dict-del-item-string!)
    (define pdict-del*! py/dict-del-item!)
    (define pdict-clear! py/dict-clear!)
    (define pdict-copy py/dict-copy)
    (define pdict-keys py/dict-keys)
    (define pdict-values py/dict-values)
    (define pdict-items py/dict-items)

    (define pmap? py/mapping-check?)
    (define pmap-size py/mapping-size)
    (define pmap-has? py/mapping-has-key-string?)
    (define pmap-has*? py/mapping-has-key?)
    (define pmap-ref py/mapping-get-item-string)
    (define pmap-set! py/mapping-set-item-string!)

    (define py-import
        (lambda (x)
            (py/import-import-module (symbol->string x))))

    (define py-get
        (lambda (x y)
            (py/object-get-attr-string x (symbol->string y))))


    (define py-args
        (lambda args 
            (define len (length args))
            (define *p (make-ptuple len))
            (let loop ((n 0)(args args))
                (if (< n len)
                    (if (zero? (ptuple-set! *p n (car args)))
                        (loop (+ n 1) (cdr args))
                        (display "Procedure py-args: error when prepare arguments!\n"))
                    *p))))


    (define py-args*
        (lambda (args) 
            (define len (length args))
            (define *p (make-ptuple len))
            (let loop ((n 0)(args args))
                (if (< n len)
                    (if (zero? (ptuple-set! *p n (car args)))
                        (loop (+ n 1) (cdr args))
                        (display "Procedure py-args*: error when prepare arguments!\n"))
                    *p))))
            

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
                (define *d (alist->pdict* lst))
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
                    (define *d (alist->pdict* lst))
                    (define *r (py/object-call f *k *d))
                    (py-decref *k)
                    (py-decref *d)
                    *r))))


    (define list->plist
        (lambda (t lst)
            (define len (length lst))
            (define *p (make-plist len))
            (define f
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (list? x)
                        (list->plist t x)
                        (f x))))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (if (zero? (plist-set! *p n (g (car lst))))
                        (l (+ n 1) (cdr lst))
                        (display 
                            (string-append "Procedure list->plist*: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))


    (define list->plist*
        (lambda (lst)
            (define len (length lst))
            (define *p (make-plist len))
            (define f
                (lambda (x)
                    (if (list? x)
                        (list->plist* x)
                        x)))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (if (zero? (plist-set! *p n (f (car lst))))
                        (l (+ n 1) (cdr lst))
                        (display 
                            (string-append "Procedure list->plist*: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))


    (define list->ptuple
        (lambda (t lst)
            (define len (length lst))
            (define *p (make-ptuple len))
            (define f
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (list? x)
                        (list->ptuple t x)
                        (f x))))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (if (zero? (ptuple-set! *p n (g (car lst))))
                        (l (+ n 1) (cdr lst))
                        (display 
                            (string-append "Procedure list->ptuple: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))

                    
    (define list->ptuple*
        (lambda (lst)
            (define len (length lst))
            (define *p (make-ptuple len))
            (define f
                (lambda (x)
                    (if (list? x)
                        (list->ptuple* x)
                        x)))
            (let l ((n 0)(lst lst))
                (if (< n len)
                    (if (zero? (ptuple-set! *p n (f (car lst))))
                        (l (+ n 1) (cdr lst))
                        (display 
                            (string-append "Procedure list->ptuple*: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))
    
    
    (define plist->list
        (lambda (t *p)
            (define len (plist-length *p))
            (define f
                (case t
                    ('int py->int)
                    ('float py->float)
                    ('str py->str)))
            (define g
                (lambda (x)
                    (if (plist? x)
                        (plist->list t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (cons (g (plist-ref *p n)) (l (+ n 1)))
                    '()))))


    (define plist->list*
        (lambda (*p)
            (define len (plist-length *p))
            (define f
                (lambda (x)
                    (if (plist? x)
                        (plist->list* x)
                        x)))
            (let l ((n 0))
                (if (< n len)
                    (cons (f (plist-ref *p n)) (l (+ n 1)))
                    '()))))


    (define ptuple->list
        (lambda (t *p)
            (define len (py/tuple-size *p))
            (define f
                (case t
                    ('int py->int)
                    ('float py->float)
                    ('str py->str)))
            (define g
                (lambda (x)
                    (if (ptuple? x)
                        (ptuple->list t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (cons (g (ptuple-ref *p n)) (l (+ n 1)))
                    '()))))


    (define ptuple->list*
        (lambda (*p)
            (define len (py/tuple-size *p))
            (define f
                (lambda (x)
                    (if (ptuple? x)
                        (ptuple->list* x)
                        x)))
            (let l ((n 0))
                (if (< n len)
                    (cons (f (ptuple-ref *p n)) (l (+ n 1)))
                    '()))))


    (define vector->plist
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (make-plist len))
            (define f
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (vector? x)
                        (vector->plist t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (if (zero? (plist-set! *p n (g (vector-ref vct n))))
                        (l (+ n 1))
                        (display 
                            (string-append "Procedure vector->plist: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))


    (define vector->plist*
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (make-plist len))
            (define f
                (lambda (x)
                    (if (vector? x)
                        (vector->plist* t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (if (zero? (plist-set! *p n (f (vector-ref vct n))))
                        (l (+ n 1))
                        (display 
                            (string-append "Procedure vector->plist*: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))


    (define vector->ptuple
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (make-ptuple len))
            (define f
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (define g
                (lambda (x)
                    (if (vector? x)
                        (vector->ptuple t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (if (zero? (ptuple-set! *p n (g (vector-ref vct n))))
                        (l (+ n 1))
                        (display 
                            (string-append "Procedure vector->ptuple: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))


    (define vector->ptuple*
        (lambda (t vct)
            (define len (vector-length vct))
            (define *p (make-ptuple len))
            (define f
                (lambda (x)
                    (if (vector? x)
                        (vector->ptuple* t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (if (zero? (ptuple-set! *p n (f (vector-ref vct n))))
                        (l (+ n 1))
                        (display 
                            (string-append "Procedure vector->ptuple*: error when set index[" (number->string n) "]'s value!\n")))
                    *p))))


    (define plist->vector
        (lambda (t *p)
            (define len (plist-length *p))
            (define v (make-vector len))
            (define f
                (case t
                    ('int py->int)
                    ('float py->float)
                    ('str py->str)))
            (define g
                (lambda (x)
                    (if (plist? x)
                        (plist->vector t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (begin 
                        (vector-set! v n (g (plist-ref *p n)))
                        (l (+ n 1)))
                    v))))

                    
    (define plist->vector*
        (lambda (*p)
            (define len (plist-length *p))
            (define v (make-vector len))
            (define f
                (lambda (x)
                    (if (plist? x)
                        (plist->vector* x)
                        x)))
            (let l ((n 0))
                (if (< n len)
                    (begin 
                        (vector-set! v n (f (plist-ref *p n)))
                        (l (+ n 1)))
                    v))))
 


    (define ptuple->vector
        (lambda (t *p)
            (define len (py/tuple-size *p))
            (define v (make-vector len))
            (define f
                (case t
                    ('int py->int)
                    ('float py->float)
                    ('str py->str)))
            (define g
                (lambda (x)
                    (if (ptuple? x)
                        (ptuple->vector t x)
                        (f x))))
            (let l ((n 0))
                (if (< n len)
                    (begin 
                        (vector-set! v n (g (ptuple-ref *p n)))
                        (l (+ n 1)))
                    v))))

                    
    (define ptuple->vector*
        (lambda (*p)
            (define len (py/tuple-size *p))
            (define v (make-vector len))
            (define f
                (lambda (x)
                    (if (ptuple? x)
                        (ptuple->vector* x)
                        x)))
            (let l ((n 0))
                (if (< n len)
                    (begin 
                        (vector-set! v n (f (ptuple-ref *p n)))
                        (l (+ n 1)))
                    v))))


    (define alist->pdict
        (lambda (t lst)
            (define *p (py/dict-new))
            (define f 
                (case t
                    ('int int)
                    ('float float)
                    ('str str)))
            (let l ((i (car lst))(r (cdr lst)))
                (if (zero? (pdict-set! *p (symbol->string (car i)) (f (cdr i))))
                    (if (null? r)
                        *p
                        (l (car r)(cdr r)))
                    (display 
                        (string-append "Procedure alist->pdict: error when set " (symbol->string (car i)) "'s value!\n")))))) 

                    
    (define alist->pdict*
        (lambda (lst)
            (define *p (py/dict-new))
            (let l ((i (car lst))(r (cdr lst)))
                (if (zero? (pdict-set! *p (symbol->string (car i)) (cdr i)))
                    (if (null? r)
                        *p
                        (l (car r)(cdr r)))
                    (display 
                        (string-append "Procedure alist->pdict*: error when set " (symbol->string (car i)) "'s value!\n"))))))
 

   
    (define py-display
        (lambda (x)
            (display 
                (py->str
                    (py/object-str x)))))


)


