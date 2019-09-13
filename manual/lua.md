

```
cc -fPIC -shared -I /usr/local/Cellar/lua/5.3.5_1/include/lua 
-L /usr/local/Cellar/lua/5.3.5_1/lib -llua   -o ../lu.so lu.c
```

You can replace lua with luajit, but in MacOS, there is a bug that I have not found a solution yet:

>If you're building a 64 bit application on OSX which links directly or indirectly against LuaJIT, you need to link your main executable with these flags:

>`-pagezero_size 10000 -image_base 100000000`

>LuaJIT requires a minimum of 2GB (2^31) of virtual address space for operation. That is, for a 64-bit pointer, its upper 33 bits need to be set to 0 (a method of 64-bit cpu compatible 32-bit instruction set, the upper 32 bits is the same as the normal 32-bit instruction set).

>The problem is that by default, the 4GB address space of the 64-bit drawin (with osx and ios) connector tags is reserved from `0x00000000` (ie, the upper 32 bits are reserved). Under normal circumstances, Apple considers this to be an ideal state, without using any RAM (it's just a virtual address space, not real memory). (There are many benefits to this limitation. For example, programmers don't convert 32-bit integers to pointers directly or indirectly because of misuse.)

>`-pagezero_size` flag allows the compiler to check that the range is smaller from 4gb (but still has the ability to catch pointer exceptions, such as null), making the address space available to the program larger.

>`-image_base flag` tells the linker program code not to use a memory address below `0x0000 0001 0000 0000`. (`0x1 0000 0000` = 2^33)
