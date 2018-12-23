
(import (enchantment py ffi)
        (enchantment py call))

(py-initialize)



(define x '(1 2 3 4 5 6 7 8))

(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np sin)
    (get np cos)
    (get np tan)
    (get ndarray tolist)
    ("display" ("/" ("*" 60 60) 20))
    (newline)
    (define y (list->py-list 'int x))
    (define z (/ (* (float 3.1415926) (array y)) (int 180)))
    (display (py-list->list 'float (tolist (sin z))))
    (newline)
    (define cos-x (py-list->list 'float (tolist (cos z))))
    (define tan-x (py-list->list 'float (tolist (tan z))))
    ("display" cos-x)))
(newline)
(display tan-x)
(newline)
