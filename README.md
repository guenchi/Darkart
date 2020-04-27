
![image](img/darkart.jpg)

## What is the Darkart

Github: https://github.com/guenchi/Darkart/

Darkart implements a set of interfaces to other language libraries that can be used to easily call other languages' libraries from Scheme.

Thanks to Chez Scheme's efficient binary interface, Darkart access to libraries in other languages is very efficient and may even be faster than native languages. For example, [Darkart-based Scheme binding of NumPy](https://github.com/guenchi/NumPy) is faster than the Python version.

Darkart solves the  dilemmas of libraries of lisp community. I hope that with the help of this project, lisp can be used more widely.

## Exemple

```
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

(get-sin '(1 2 3 4 5 6 7 8))

=>
(0.01745240643728351 0.03489949670250097 0.05233595624294383 0.0697564737441253 
0.08715574274765817 0.10452846326765346 0.12186934340514748 0.13917310096006544)
```


## Darkart's ecosystem

- [NumPy](https://github.com/guenchi/NumPy)

- [SciPy](https://github.com/guenchi/SciPy)

- [SymPy](https://github.com/guenchi/SymPy)

- [Matplotlib](https://github.com/guenchi/Matplotlib)

- [Pandas](https://github.com/guenchi/Pandas)
