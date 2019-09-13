(("name" . "darkart")
 ("version" . "1.0.1")
 ("description" . "A binary interface let Chez Scheme use Python, Lua, Ruby etc's library")
 ("keywords"
   ("scheme" "library"))
 ("author" 
   ("guenchi"))
 ("private" . #f)
 ("scripts"
   ("build" . "cd ./lib/darkart/c && cc -fPIC -shared  -L/Library/Frameworks/Python.framework/Versions/3.7/lib/ -lpython3.7 -o ../py.so py.c"))
 ("dependencies")
 ("devDependencies"))
