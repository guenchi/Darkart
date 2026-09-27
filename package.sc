(("name" . "darkart")
 ("version" . "1.0.1")
 ("description" . "A binary interface let Chez Scheme use Python, Lua, Ruby etc's library")
 ("keywords"
   ("scheme" "library"))
 ("author" 
   ("guenchi"))
 ("private" . #f)
 ("scripts"
   ("build" . "cd ./lib/darkart/c && cc -fPIC -shared $(python3-config --includes) -o ../py.so py.c $(python3-config --ldflags --embed)"))
 ("dependencies")
 ("devDependencies"))
