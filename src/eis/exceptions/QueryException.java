// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryException.java

package eis.exceptions;


public class QueryException extends Exception
{

    public QueryException(String string)
    {
        super(string);
    }

    public QueryException(String string, Exception cause)
    {
        super(string, cause);
    }

    private static final long serialVersionUID = 0x38e670f4d58d980bL;
}
