;  MIT License

;  Copyright guenchi (c) 2018 - 2019

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
    py-inc
    py-dec


    pint?
    pflt?
    pcplx?
    pstr?
    pbytes?
    s->pint
    s->pflt
    s->pcplx
    s->pstr
    s->pbytes
    s->ptype
    p->sint
    p->sflt
    p->scplx
    p->sstr
    p->sbytes
    p->stype


    *int?
    *flt?
    *cplx?
    *str?
    int
    flt
    cplx
    str
    auto
    *int
    *flt
    *cplx
    *str
    *auto


    obj->bytes

    py-add
    py-sub
    py-mul
    py-div
    py-fdiv
    py-mod
    py-divmod
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
    pdict->alist
    pdict->alist*

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
    (chezscheme)
    (darkart py ffi))



  (alias py-init py-initialize)
  (alias py-fin py-finalize)
  (alias py-inc py-incref)
  (alias py-dec py-decref)

  (alias pint? py/long-check?)
  (alias pflt? py/float-check?)
  (alias pcplx? py/complex-check?)
  (alias pstr? py/unicode-check?)
  (alias pbytes? py/bytes-check?)

  (alias *int? pint?)
  (alias *flt? pflt?)
  (alias *cplx? pcplx?)
  (alias *str? pstr?)

  (alias s->pflt py/float-from-double)
  (alias s->pstr py/unicode-from-string)
  (alias s->pbytes py/bytes-from-string)

  (alias flt s->pflt)
  (alias str s->pstr)

  (alias p->sflt py/float-as-double)
  (alias p->sstr py/unicode-as-utf8)
  (alias p->sbytes py/bytes-as-string)

  (alias *flt p->sflt)
  (alias *str p->sstr)

  (alias py-add py/number-add)
  (alias py-sub py/number-subtract)
  (alias py-mul py/number-multiply)
  (alias py-div py/number-divide)
  (alias py-fdiv py/number-floor-divide)
  (alias py-mod py/number-remainder)
  (alias py-divmod py/number-divmod)
  (alias py-lsh py/number-lshift)
  (alias py-rsh py/number-rshift)
  (alias py-and py/number-and)
  (alias py-or py/number-or)
  (alias py-xor py/number-xor)
  (alias py-inv py/number-invert)
  (alias py-abs py/number-absolute)
  (alias py-neg py/number-negative)

  (alias plist? py/list-check?)
  (alias make-plist py/list-new)
  (alias plist-length py/list-size)
  (alias plist-insert! py/list-insert!)
  (alias plist-append! py/list-append!)
  (alias plist-sort! py/list-sort!)
  (alias plist-reverse! py/list-reverse!)

  (alias ptuple? py/tuple-check?)
  (alias make-ptuple py/tuple-new)
  (alias ptuple-length py/tuple-size)

  (alias pset? py/set-check?)
  (alias make-pset py/set-new)
  (alias pset-length py/set-size)
  (alias pset-contains? py/set-contains?)
  (alias pset-add! py/set-add!)
  (alias pset-del! py/set-discard!)
  (alias pset-pop! py/set-pop!)
  (alias pset-clear! py/set-clear!)

  (alias psequ? py/sequence-check?)
  (alias psequ->plist py/sequence-list)
  (alias psequ->ptuple py/sequence-tuple)
  (alias psequ-length py/sequence-size)
  (alias psequ-append py/sequence-concat)
  (alias psequ-repeat py/sequence-repeat)
  (alias psequ-ref py/sequence-get-item)
  (alias psequ-set! py/sequence-set-item!)
  (alias psequ-del! py/sequence-del-item!)
  (alias psequ-sref py/sequence-get-slice)
  (alias psequ-sset! py/sequence-set-slice!)
  (alias psequ-sdel! py/sequence-del-slice!)
  (alias psequ-count py/sequence-count)
  (alias psequ-contains py/sequence-contains)
  (alias psequ-index py/sequence-index)

  (alias pdict? py/dict-check?)
  (alias make-pdict py/dict-new)
  (alias pdict-length py/dict-size)
  (alias pdict-ref py/dict-get-item-string)
  (alias pdict-ref* py/dict-get-item)
  (alias pdict-set! py/dict-set-item-string!)
  (alias pdict-set*! py/dict-set-item!)
  (alias pdict-del! py/dict-del-item-string!)
  (alias pdict-del*! py/dict-del-item!)
  (alias pdict-clear! py/dict-clear!)
  (alias pdict-copy py/dict-copy)
  (alias pdict-keys py/dict-keys)
  (alias pdict-values py/dict-values)
  (alias pdict-items py/dict-items)

  (alias pmap? py/mapping-check?)
  (alias pmap-size py/mapping-size)
  (alias pmap-has? py/mapping-has-key-string?)
  (alias pmap-has*? py/mapping-has-key?)
  (alias pmap-ref py/mapping-get-item-string)
  (alias pmap-set! py/mapping-set-item-string!)


  (define self
    (lambda (x) x))

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
            (error 'py-args "error when set args" (car args)))
          *p))))


  (define py-args*
    (lambda (args)
      (define len (length args))
      (define *p (make-ptuple len))
      (let loop ((n 0)(args args))
        (if (< n len)
          (if (zero? (ptuple-set! *p n (car args)))
            (loop (+ n 1) (cdr args))
            (error 'py-args* "error when set args" (car args)))
          *p))))


  (define py-call
    (lambda (*f . args)
      (define *k (py-args* args))
      (define *r (py/object-call-object *f *k))
      (py-dec *k)
      *r))


  (define py-call*
    (lambda (*f . args)
      (lambda (lst)
        (define *k (py-args* args))
        (define *d (alist->pdict* lst))
        (define *r (py/object-call *f *k *d))
        (py-dec *k)
        (py-dec *d)
        *r)))


  (define py-func
    (lambda (*p s)
      (define *f (py-get *p s))
      (lambda args
        (define *k (py-args* args))
        (define *r (py/object-call-object *f *k))
        (py-dec *k)
        *r)))


  (define py-func*
    (lambda (*p s)
      (define *f (py-get *p s))
      (lambda args
        (lambda (lst)
          (define *k (py-args* args))
          (define *d (alist->pdict* lst))
          (define *r (py/object-call *f *k *d))
          (py-dec *k)
          (py-dec *d)
          *r))))


  ;; Fixnums take the fast path; bignums go through their decimal form.
  (define s->pint
    (lambda (x)
      (if (fixnum? x)
        (py/long-from-longlong x)
        (py/long-from-string (number->string x) 0 10))))

  (alias int s->pint)


  ;; Values that do not fit in a long long are read back from their decimal form.
  (define p->sint
    (lambda (*p)
      (define n (py/long-as-longlong *p))
      (if (and (= n -1) (not (zero? (py/err-occurred))))
        (begin
          (py/err-clear)
          (let* ((*s (py/object-str *p))
                 (s (py/unicode-as-utf8 *s)))
            (py-dec *s)
            (string->number s)))
        n)))

  (alias *int p->sint)


  (define s->ptype
    (lambda (x)
      (cond 
        ((boolean? x) (py/bool-from-long (if x 1 0)))
        ((flonum? x) (flt x))
        ((and (integer? x) (exact? x)) (int x))
        ((real? x) (flt (inexact x)))
        ((number? x) (cplx x))
        ((string? x) (str x))
        ((eq? x (void)) (py/none))
        (else (error 's->ptype "illegal input" x)))))

  (alias auto s->ptype)


  (define p->stype
    (lambda (x)
      (cond 
        ((py/none-check? x) (void))
        ((py/bool-check? x) (not (zero? (py/long-as-long x))))
        ((*int? x) (*int x))
        ((*flt? x) (*flt x))
        ((*cplx? x) (*cplx x))
        ((*str? x) (*str x))
        ((pbytes? x) (p->sbytes x))
        (else (error 'p->stype "illegal input" x)))))

  (alias *auto p->stype)


  (define s->pcplx
    (lambda (c)
      (py/complex-from-doubles
        (inexact (real-part c))
        (inexact (imag-part c)))))

  (alias cplx s->pcplx)      


  (define p->scplx
    (lambda (*c)
      (fl-make-rectangular
        (py/complex-real-as-double *c)
        (py/complex-imag-as-double *c))))

  (alias *cplx p->scplx)


  (define-syntax plist-ref
    (syntax-rules ()
      ((_ *p k)(py/list-get-item *p k))
      ((_ *p k* ... k)(plist-ref (plist-ref *p k* ...) k))))


  (define-syntax plist-set!
    (syntax-rules ()
      ((_ *p k v)(py/list-set-item! *p k v))
      ((_ *p k* ... k v)(plist-set! (plist-ref *p k* ...) k v))))


  (define-syntax plist-sref
    (syntax-rules ()
      ((_ *p b e)(py/list-get-slice *p b e))
      ((_ *p k* ... (b e))(plist-sref (plist-ref *p k* ...) b e))))


  (define-syntax plist-sset!
    (syntax-rules ()
      ((_ *p b e l)(py/list-set-slice! *p b e l))
      ((_ *p k* ... (b e) l)(plist-sset! (plist-ref *p k* ...) b e l))))


  (define-syntax ptuple-ref
    (syntax-rules ()
      ((_ *p k)(py/tuple-get-item *p k))
      ((_ *p k* ... k)(ptuple-ref (ptuple-ref *p k* ...) k))))


  (define-syntax ptuple-set!
    (syntax-rules ()
      ((_ *p k v)(py/tuple-set-item! *p k v))
      ((_ *p k* ... k v)(ptuple-set! (ptuple-ref *p k* ...) k v))))


  (define-syntax ptuple-sref
    (syntax-rules ()
      ((_ *p b e)(py/tuple-get-slice *p b e))
      ((_ *p k* ... (b e))(ptuple-sref (ptuple-ref *p k* ...) b e))))


  (define *list->plist
    (lambda (f lst)
      (define len (length lst))
      (define *p (make-plist len))
      (define i
        (lambda (x)
          (if (list? x)
            (*list->plist f x)
            (f x))))
      (let l ((n 0)(lst lst))
        (if (< n len)
          (if (zero? (plist-set! *p n (i (car lst))))
            (l (+ n 1) (cdr lst))
            (error 'list->plist "error when set value" n (i (car lst))))
          *p))))


  (define-syntax list->plist
    (syntax-rules ()
      ((_ x)(*list->plist auto x))
      ((_ f x)(*list->plist f x))))


  (define list->plist*
    (lambda (x)
      (*list->plist self x)))


  (define *list->ptuple
    (lambda (f lst)
      (define len (length lst))
      (define *p (make-ptuple len))
      (define i
        (lambda (x)
          (if (list? x)
            (*list->ptuple f x)
            (f x))))
      (let l ((n 0)(lst lst))
        (if (< n len)
          (if (zero? (ptuple-set! *p n (i (car lst))))
            (l (+ n 1) (cdr lst))
            (error 'list->ptuple "error when set value" n (i (car lst))))
          *p))))


  (define-syntax list->ptuple
    (syntax-rules ()
      ((_ x)(*list->ptuple auto x))
      ((_ f x)(*list->ptuple f x))))


  (define list->ptuple*
    (lambda (x)
      (*list->ptuple self x)))


  (define *plist->list
    (lambda (f *p)
      (define len (plist-length *p))
      (define i
        (lambda (x)
          (if (plist? x)
            (*plist->list f x)
            (f x))))
      (let l ((n 0))
        (if (< n len)
          (cons (i (plist-ref *p n)) (l (+ n 1)))
          '()))))


  (define-syntax plist->list
    (syntax-rules ()
      ((_ x)(*plist->list *auto x))
      ((_ f x)(*plist->list f x))))


  (define plist->list*
    (lambda (x)
      (*plist->list self x)))


  (define *ptuple->list
    (lambda (f *p)
      (define len (py/tuple-size *p))
      (define i
        (lambda (x)
          (if (ptuple? x)
            (*ptuple->list f x)
            (f x))))
      (let l ((n 0))
        (if (< n len)
          (cons (i (ptuple-ref *p n)) (l (+ n 1)))
          '()))))


  (define-syntax ptuple->list
    (syntax-rules ()
      ((_ x)(*ptuple->list *auto x))
      ((_ f x)(*ptuple->list f x))))


  (define ptuple->list*
    (lambda (x)
      (*ptuple->list self x)))


  (define *vector->plist
    (lambda (f vct)
      (define len (vector-length vct))
      (define *p (make-plist len))
      (define i
        (lambda (x)
          (if (vector? x)
            (*vector->plist f x)
            (f x))))
      (let l ((n 0))
        (if (< n len)
          (if (zero? (plist-set! *p n (i (vector-ref vct n))))
            (l (+ n 1))
            (error 'vector->plist "error when set value" n (i (vector-ref vct n))))
          *p))))


  (define-syntax vector->plist
    (syntax-rules ()
      ((_ x)(*vector->plist auto x))
      ((_ f x)(*vector->plist f x))))


  (define vector->plist*
    (lambda (x)
      (*vector->plist self x)))


  (define *vector->ptuple
    (lambda (f vct)
      (define len (vector-length vct))
      (define *p (make-ptuple len))
      (define i
        (lambda (x)
          (if (vector? x)
            (*vector->ptuple f x)
            (f x))))
      (let l ((n 0))
        (if (< n len)
          (if (zero? (ptuple-set! *p n (i (vector-ref vct n))))
            (l (+ n 1))
            (error 'vector->ptuple "error when set value" n (i (vector-ref vct n))))
          *p))))


  (define-syntax vector->ptuple
    (syntax-rules ()
      ((_ x)(*vector->ptuple auto x))
      ((_ f x)(*vector->ptuple f x))))


  (define vector->ptuple*
    (lambda (x)
      (*vector->ptuple self x)))


  (define *plist->vector
    (lambda (f *p)
      (define len (plist-length *p))
      (define v (make-vector len))
      (define i
        (lambda (x)
          (if (plist? x)
            (*plist->vector f x)
            (f x))))
      (let l ((n 0))
        (if (< n len)
          (begin 
            (vector-set! v n (i (plist-ref *p n)))
            (l (+ n 1)))
          v))))


  (define-syntax plist->vector
    (syntax-rules ()
      ((_ x)(*plist->vector *auto x))
      ((_ f x)(*plist->vector f x))))


  (define plist->vector*
    (lambda (x)
      (*plist->vector self x)))


  (define *ptuple->vector
    (lambda (f *p)
      (define len (py/tuple-size *p))
      (define v (make-vector len))
      (define i
        (lambda (x)
          (if (ptuple? x)
            (*ptuple->vector f x)
            (f x))))
      (let l ((n 0))
        (if (< n len)
          (begin 
            (vector-set! v n (i (ptuple-ref *p n)))
            (l (+ n 1)))
          v))))


  (define-syntax ptuple->vector
    (syntax-rules ()
      ((_ x)(*ptuple->vector *auto x))
      ((_ f x)(*ptuple->vector f x))))


  (define ptuple->vector*
    (lambda (x)
      (*ptuple->vector self x)))


  (define *alist->pdict
    (lambda (f lst)
      (define *p (py/dict-new))
      (let l ((lst lst))
        (if (null? lst)
          *p
          (let ((k (symbol->string (caar lst)))
                (v (f (cdar lst))))
            (if (zero? (pdict-set! *p k v))
              (l (cdr lst))
              (error 'alist->pdict "error when set value" k v)))))))


  (define-syntax alist->pdict
    (syntax-rules ()
      ((_ x)(*alist->pdict auto x))
      ((_ f x)(*alist->pdict f x))))


  (define alist->pdict*
    (lambda (x)
      (*alist->pdict self x)))


  (define *pdict->alist
    (lambda (f *p)
      (define *k (pdict-keys *p))
      (define k (plist->list *str *k))
      (py-dec *k)
      (map
        (lambda (x)
          (cons (string->symbol x) (f (pdict-ref *p x))))
        k)))


  (define-syntax pdict->alist
    (syntax-rules ()
      ((_ x)(*pdict->alist *auto x))
      ((_ f x)(*pdict->alist f x))))


  (define pdict->alist*
    (lambda (x)
      (*pdict->alist self x)))


  (define obj->bytes
    (lambda (obj)
      (let ((string (py/object-str obj)))
        (define *bytes (py/unicode-as-encoded-string string "utf-8" "strict"))
        (py-dec string)
        *bytes)))


  (define py-display
    (lambda (obj)
      (let ((obj->str (lambda (obj)
            (define *s (py/object-str obj))
            (define string (*str *s))
            (py-dec *s)
            string)))
        (display (if (*str? obj)
               (*str obj)
               (obj->str obj))))))

)
