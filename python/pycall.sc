


(import fli pycall)
(import match match)

(define pycall
    (lambda (lst)
        (define Num
            (lambda (x)
                (match x
                    (,n (guard (number? n)) n))))
        (define Str
            (lambda (x)
                (match x
                    (,s (guard (string? s)) s))))
        (define Sym
            (lambda (x)
                (match x
                    (,s (guard (symbol? s)) s))))
        (match lst
            ((import ,(Sym -> lib)) `(define ,lib (py/import-import-module (symbol->string ,lib))))
            ((import ,(Sym -> lib) as ,(Sym -> l)) `(define ,l (py/import-import-module (symbol->string ,lib)))))))