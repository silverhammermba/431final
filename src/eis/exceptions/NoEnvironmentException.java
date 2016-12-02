// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NoEnvironmentException.java

package eis.exceptions;


public class NoEnvironmentException extends RuntimeException
{

    public NoEnvironmentException(String string)
    {
        super(string);
    }

    public NoEnvironmentException(String string, Exception cause)
    {
        super(string, cause);
    }

    private static final long serialVersionUID = 0x6465c1bbd865e92cL;
}
