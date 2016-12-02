// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnvironmentInterfaceException.java

package eis.exceptions;


public class EnvironmentInterfaceException extends Exception
{

    public EnvironmentInterfaceException(String string)
    {
        super(string);
    }

    public EnvironmentInterfaceException(String string, Exception cause)
    {
        super(string, cause);
    }

    private static final long serialVersionUID = 0xf42e8644a6c394f4L;
}
