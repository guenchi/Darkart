# Py-call
Abandoned.

Attempts in the [Darkart](https://github.com/guenchi/Darkart) project were abandoned because they were not conducive to code construction and packaging.

Py-call attempts to mix the Scheme procedures with the Python functions, which contains a parser for the Py-call source.

### Call foreign language libraries with a easily and lispist syntax: ###

![image](https://github.com/guenchi/Py-call/blob/master/py-call.png)

```
(define lst '(1 2 3 4 5 6 7 8))
(py-call 
  '((import numpy as np)
    (get np array)
    (get np ndarray)
    (get np pi)
    (get np sin)
    (get np cos)
    (get np tan)
    (get ndarray tolist)
    (define x (list->py-list 'int lst))
    (define y (/ (*  pi (array x)) (int 180)))
    (define sin-lst (py-list->list 'float (tolist (sin y))))
    (define cos-lst (py-list->list 'float (tolist (cos y))))
    (define tan-lst (py-list->list 'float (tolist (tan y))))))

=> 

(0.01745240643728351 0.03489949670250097 0.05233595624294383 0.0697564737441253 
0.08715574274765817 0.10452846326765346 0.12186934340514748 0.13917310096006544)
(0.9998476951563913 0.9993908270190958 0.9986295347545738 0.9975640502598242 
0.9961946980917455 0.9945218953682733 0.992546151641322 0.9902680687415704)
(0.017455064928217585 0.03492076949174773 0.0524077792830412 0.06992681194351041 
0.087488663525924 0.10510423526567646 0.1227845609029046 0.14054083470239143) 
```


Py-call is dependented https://github.com/guenchi/match, a pioneering work by Dan Friedman, Erik Hilsdale and Kent Dybvig.
