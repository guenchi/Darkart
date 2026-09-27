#include <Python.h>
#include <dlfcn.h>


/* Chez loads this shim with RTLD_LOCAL, which keeps the libpython symbols
   out of the global namespace. C extension modules (numpy etc.) expect to
   resolve them from there, so re-open libpython with RTLD_GLOBAL. */
__attribute__((constructor))
static void _darkart_promote_libpython(void)
{
    Dl_info info;

    if (dladdr((void *)&Py_Initialize, &info) && info.dli_fname)
        dlopen(info.dli_fname, RTLD_NOW | RTLD_NOLOAD | RTLD_GLOBAL);
}


int _PyLong_Check(PyObject *p)
{
    return PyLong_Check(p);
}

int _PyFloat_Check(PyObject *p)
{
    return PyFloat_Check(p);
}

int _PyComplex_Check(PyObject *p)
{
    return PyComplex_Check(p);
}

int _PyBytes_Check(PyObject *p)
{
    return PyBytes_Check(p);
}

int _PyUnicode_Check(PyObject *p)
{
    return PyUnicode_Check(p);
}

int _PyBool_Check(PyObject *p)
{
    return PyBool_Check(p);
}

int _PyNone_Check(PyObject *p)
{
    return p == Py_None;
}

PyObject *_Py_GetNone(void)
{
    Py_INCREF(Py_None);
    return Py_None;
}

int _PyList_Check(PyObject *p)
{
    return PyList_Check(p);
}

int _PyTuple_Check(PyObject *p)
{
    return PyTuple_Check(p);
}

int _PySet_Check(PyObject *p)
{
    return PySet_Check(p);
}

int _PySequence_Check(PyObject *p)
{
    return PySequence_Check(p);
}

int _PyDict_Check(PyObject *p)
{
    return PyDict_Check(p);
}

int _PyMapping_Check(PyObject *p)
{
    return PyMapping_Check(p);
}
