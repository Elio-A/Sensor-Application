using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Promise<T> 
{
    public bool done;
    public T value;

    public Promise()
    {

    }

    public Promise(T val)
    {
        value = val;
    }
}
