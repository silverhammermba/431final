// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TranslationException.java

package eis.eis2java.exception;


public class TranslationException extends Exception
{

    public TranslationException()
    {
    }

    public TranslationException(String message)
    {
        super(message);
    }

    public TranslationException(Throwable cause)
    {
        super(cause);
    }

    public TranslationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = 1L;
}
