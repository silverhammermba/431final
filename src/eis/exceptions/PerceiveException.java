// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PerceiveException.java

package eis.exceptions;


// Referenced classes of package eis.exceptions:
//            EnvironmentInterfaceException

public class PerceiveException extends EnvironmentInterfaceException
{

    public PerceiveException(String string)
    {
        super(string);
    }

    public PerceiveException(String message, Exception cause)
    {
        super(message);
        initCause(cause);
    }

    private static final long serialVersionUID = 0xd142007738872e3cL;
}
