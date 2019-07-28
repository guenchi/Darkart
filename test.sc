(import (darkart py ffi)
        (py-call py-call))


(define x '(1 2 3 4 5 6 7 8))
(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np cos as cosin)
    (get ndarray tolist)
    (define lst 
        (list->py-list 'int x))
    (define cosin-lst 
        (py-list->list 'float 
            (tolist 
                (cosin 
                    (array lst)))))))
(display cosin-lst)
(newline)




(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np cos as cosin)
    (get ndarray tolist)
    (define cosin-lst 
        (py-list->list 'float 
            (tolist 
                (cosin 
                    (array 
                        (list->py-list 'int '(1 2 3 4 5 6 7 8)))))))
    (define x 
        (py->int 
            (* (+ (int 2) (int 3)) (int 7))))))
(display cosin-lst)
(newline)
(display x)
(newline)



(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get ndarray tolist)
    (define lst (list->py-list 'int '(1 2 3 4 5 6 7 8)))
    (define arr (array lst))
    (define x (py-list->list 'int (tolist (+ arr arr))))
    (define y (py-list->list 'int (tolist (* (int 7) arr))))))
(display x)
(newline)
(display y)
(newline)




(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np sin)
    (get np cos)
    (get np tan)
    (get np pi)
    (get ndarray tolist)
    (display (py->float pi))
    (newline)
    (define x (list->py-list 'int '(1 2 3 4 5 6 7 8)))
    (define y (/ (* pi (array x)) (int 180)))
    (define sin-x (py-list->list 'float (tolist (sin y))))
    (define cos-x (py-list->list 'float (tolist (cos y))))
    (define tan-x (py-list->list 'float (tolist (tan y))))
    ))
(display sin-x)
(newline)
(display cos-x)
(newline)
(display tan-x)
(newline)

