MacOS

```
$ ln -s /Applications/Julia-1.2.app/Contents/Resources/julia/bin/julia /usr/local/bin/julia
$ ln -s /Applications/Julia-1.2.app/Contents/Resources/julia/include/julia /usr/local/include/julia
$ ln -s /Applications/Julia-1.2.app/Contents/Resources/julia/lib/julia /usr/local/lib/julia
$ ln -s /Applications/Julia-1.2.app/Contents/Resources/julia/lib/libjulia.dylib /usr/local/lib/libjulia.dylib
$ cc -fPIC -shared -I /usr/local/include/julia -L /usr/local/lib/julia  -ljulia -o ../jl.so jl.c 
```