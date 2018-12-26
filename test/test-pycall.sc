
(import (enchantment py ffi)
        (enchantment py call)
        (enchantment py eval))


(py-init)

;; test pass values

(define x 9)
(py-define 'x x)
(define k (py/import-import-module "__main__"))
(define dic (py/module-get-dict k))
(define y (py/long-as-long (py/run-string "x * x" py-eval-input dic dic)))
(display (+ y 8))
(newline)

;; test list pass and operate

(define x '(1 2 3 4 5))
(define t (py/list->list x))
(define a (py/list-get-item t 0))
(define b (py/list-get-item t 1))
(define c (py/list-get-item t 2))
(define d (py/list-get-item t 3))
(define e (py/list-get-item t 4))
(display (py/long-as-long a))
(newline)
(display (py/long-as-long b))
(newline)
(display (py/long-as-long c))
(newline)
(display (py/long-as-long d))
(newline)
(display (py/long-as-long e))
(newline)


(define x `#(1 2 3 4 5))
(define t (py/vector->list x))
(define a (py/list-get-item t 0))
(define b (py/list-get-item t 1))
(define c (py/list-get-item t 2))
(define d (py/list-get-item t 3))
(define e (py/list-get-item t 4))
(display (py/long-as-long a))
(newline)
(display (py/long-as-long b))
(newline)
(display (py/long-as-long c))
(newline)
(display (py/long-as-long d))
(newline)
(display (py/long-as-long e))
(newline)

;; test tuple pass and operate

(define x '(1 2 3 4 5))
(define t (list->*tuple x))
(define a (py/tuple-get-item t 0))
(define b (py/tuple-get-item t 1))
(define c (py/tuple-get-item t 2))
(define d (py/tuple-get-item t 3))
(define e (py/tuple-get-item t 4))
(display (py/long-as-long a))
(newline)
(display (py/long-as-long b))
(newline)
(display (py/long-as-long c))
(newline)
(display (py/long-as-long d))
(newline)
(display (py/long-as-long e))
(newline)


(define x `#(1 2 3 4 5))
(define t (py/vector->tuple x))
(define a (py/tuple-get-item t 0))
(define b (py/tuple-get-item t 1))
(define c (py/tuple-get-item t 2))
(define d (py/tuple-get-item t 3))
(define e (py/tuple-get-item t 4))
(display (py/long-as-long a))
(newline)
(display (py/long-as-long b))
(newline)
(display (py/long-as-long c))
(newline)
(display (py/long-as-long d))
(newline)
(display (py/long-as-long e))
(newline)


;test call numpy 


(define x '(1 2 3 4 5 6 7 8))
(define t (list->py-list 'int x))
(define np (py/import-import-module "numpy"))
(define array (py/object-get-attr-string np "array"))
(define cosin (py/object-get-attr-string np "cos"))
(define ndarray (py/object-get-attr-string np "ndarray"))
(define tolist (py/object-get-attr-string ndarray "tolist"))
(define arr (py/object-call-object array (py-args t)))
(define lst (py/object-call-object cosin (py-args arr)))
(define pylst (py/object-call-object tolist (py-args lst)))
(display (py-list->list 'float pylst))
(newline)

(py-fin)

