#include  <Python/Python.h>

int _PyInt_Check(PyObject *p)
{
    return PyInt_Check(p);
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

int _PyString_Check(PyObject *p)
{
    return PyString_Check(p);
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





