// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NoTranslatorException.java

package eis.eis2java.exception;


// Referenced classes of package eis.eis2java.exception:
//            TranslationException

public class NoTranslatorException extends TranslationException
{

    public NoTranslatorException()
    {
    }

    public NoTranslatorException(String message)
    {
        super(message);
    }

    public NoTranslatorException(Throwable cause)
    {
        super(cause);
    }

    public NoTranslatorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NoTranslatorException(Class parameterClass)
    {
        this((new StringBuilder("No translator found for class ")).append(parameterClass.getName()).toString());
    }

    public NoTranslatorException(Class parameterClass, Throwable cause)
    {
        this((new StringBuilder("No translator found for class ")).append(parameterClass.getName()).toString(), cause);
    }

    private static final long serialVersionUID = 1L;
}
