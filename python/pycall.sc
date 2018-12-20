


(import fli pycall)
(import match match)

(define pycall
    (lambda (lst)
        (define Str
            (lambda (x)
                (match x
                    (,s (guard (string? s)) s))))
        (define Sym
            (lambda (x)
                (match x
                    (,s (guard (symbol? s)) s))))
        (match lst
            ((import ,(Str -> lib)) `(define (string->symbol ,lib) (py/import-import-module ,lib)))
            ((import ,(Str -> lib) as ,(Sym -> l)) `(define ,l (py/import-import-module ,lib))))))