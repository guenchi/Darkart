
(import (chezscheme)
        (darkart py ffi)
        (darkart py call))


(define failures 0)

(define-syntax check
  (syntax-rules ()
    ((_ name expected expr)
      (let ((v expr))
        (if (equal? v expected)
          (printf "ok   ~a~%" name)
          (begin
            (set! failures (+ failures 1))
            (printf "FAIL ~a: expected ~s, got ~s~%" name expected v)))))))

(define-syntax check-approx
  (syntax-rules ()
    ((_ name expected expr)
      (let ((v expr))
        (if (and (= (length v) (length expected))
                 (andmap (lambda (a b) (< (abs (- a b)) 1e-12)) v expected))
          (printf "ok   ~a~%" name)
          (begin
            (set! failures (+ failures 1))
            (printf "FAIL ~a: expected ~s, got ~s~%" name expected v)))))))


(py-init)


;; numbers

(check "int round trip" 42 (*int (int 42)))
(check "negative int round trip" -7 (*int (int -7)))
(check "bignum round trip" (expt 2 100) (*int (int (expt 2 100))))
(check "negative bignum round trip" (- (expt 3 70)) (*int (int (- (expt 3 70)))))
(check "float round trip" 2.5 (*flt (flt 2.5)))
(check "rational becomes float" 0.5 (*auto (auto 1/2)))
(check "inexact complex round trip" 1.0-2.0i (*cplx (cplx 1.0-2.0i)))
(check "exact complex auto" 1.0+2.0i (*auto (auto 1+2i)))
(check "complex add" 7.0+2.0i (*cplx (py-add (cplx 4.0-3.0i) (cplx 3.0+5.0i))))
(check "py-mod is remainder" 1 (*int (py-mod (int 7) (int 3))))
(check "py-divmod" '(2 1) (ptuple->list (py-divmod (int 7) (int 3))))


;; booleans and None

(check "true round trip" #t (*auto (auto #t)))
(check "false round trip" #f (*auto (auto #f)))
(check "bool is not int" #t (py/bool-check? (auto #t)))
(check "None round trip" (void) (*auto (auto (void))))


;; strings and bytes

(check "str is unicode" #t (pstr? (str "abc")))
(check "str round trip" "abc" (*str (str "abc")))
(check "non-ascii str round trip" "h\xe9;llo \x4e2d;\x6587;" (*str (str "h\xe9;llo \x4e2d;\x6587;")))
(check "auto str" "xyz" (*auto (auto "xyz")))
(check "bytes are not str" #f (pstr? (s->pbytes "abc")))
(check "bytes round trip" "abc" (p->sbytes (s->pbytes "abc")))
(check "py-display str"
  "hello"
  (with-output-to-string (lambda () (py-display (str "hello")))))
(check "py-display list"
  "[1, 'a']"
  (with-output-to-string (lambda () (py-display (list->plist '(1 "a"))))))


;; lists

(define nested '((((1 2 3 4) (1 2 3 4) (1 2 3 4)) ((1 2 3 4) (1 2 3 4) (1 2 3 4)))
                 (((1 2 3 4) (1 2 3 4) (1 2 3 4)) ((1 2 3 4) (1 2 3 4) (1 2 3 4)))))

(check "plist length" 5 (plist-length (list->plist '(1 2 3 4 5))))
(check "nested plist round trip" nested (plist->list (list->plist nested)))
(check "nested plist to vector" 4 (vector-length (vector-ref (plist->vector (list->plist '((1 2 3 4)))) 0)))

(define x (list->plist nested))
(check "plist-set! nested" 0 (plist-set! x 0 1 2 3 (int 100)))
(check "plist-ref nested" 100 (*int (plist-ref x 0 1 2 3)))
(check "plist-sref nested" '(1 2 3 100) (plist->list (plist-sref x 0 1 2 (0 4))))
(plist-sset! x 0 1 2 (0 4) (list->plist '(90 91 92 93)))
(check "plist-sset! nested" '(90 91 92 93) (plist->list (plist-ref x 0 1 2)))


;; tuples

(define t (list->ptuple '((1 2) (3 4))))
(check "nested ptuple round trip" '((1 2) (3 4)) (ptuple->list t))
(check "ptuple-ref nested" 3 (*int (ptuple-ref t 1 0)))
(check "ptuple-set! nested" 0 (ptuple-set! t 1 0 (int 30)))
(check "ptuple-ref after set" 30 (*int (ptuple-ref t 1 0)))
(check "ptuple-sref" '((3 4)) (ptuple->list (ptuple-sref (list->ptuple '((1 2) (3 4))) 1 2)))


;; sets

(define s (make-pset (list->plist '(5))))
(check "pset length" 1 (pset-length s))
(check "pset-pop! returns the element" 5 (*int (pset-pop! s)))
(check "pset empty after pop" 0 (pset-length s))


;; dicts

(check "empty alist to pdict" 0 (pdict-length (alist->pdict '())))
(check "empty pdict to alist" '() (pdict->alist (make-pdict)))
(check "alist pdict round trip"
  '((a . 8) (b . 9.5) (c . "c"))
  (pdict->alist (alist->pdict '((a . 8) (b . 9.5) (c . "c")))))


;; calls

(define builtins (py-import 'builtins))
(check "py-call" 3 (*int (py-call (py-get builtins 'len) (list->plist '(1 2 3)))))
(check "py-call* with no kwargs" 3 (*int ((py-call* (py-get builtins 'len) (list->plist '(1 2 3))) '())))
(check "py-call* with kwargs"
  '(3 2 1)
  (plist->list
    ((py-call* (py-get builtins 'sorted) (list->plist '(1 3 2)))
      `((reverse . ,(auto #t))))))


;; numpy

(define np (py-import 'numpy))
(define ndarray (py-get np 'ndarray))
(define pi (py-get np 'pi))
(define np-array (py-func np 'array))
(define np-sin (py-func np 'sin))
(define np-tolist (py-func ndarray 'tolist))

(define get-sin
  (lambda (lst)
    (plist->list
      (np-tolist
        (np-sin
          (py-div
            (py-mul pi
              (np-array
                (list->plist lst)))
            (int 180)))))))

(check-approx "numpy sin"
  (map (lambda (d) (sin (/ (* 3.141592653589793 d) 180.0))) '(1 2 3 4 5 6 7 8))
  (get-sin '(1 2 3 4 5 6 7 8)))


(py-fin)

(if (zero? failures)
  (printf "~%all tests passed~%")
  (begin
    (printf "~%~a test(s) failed~%" failures)
    (exit 1)))
