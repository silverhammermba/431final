// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EntityException.java

package eis.exceptions;


// Referenced classes of package eis.exceptions:
//            EnvironmentInterfaceException

public class EntityException extends EnvironmentInterfaceException
{

    public EntityException(String string)
    {
        super(string);
    }

    public EntityException(String string, Exception cause)
    {
        super(string, cause);
    }

    private static final long serialVersionUID = 0x357a9902a31bd20aL;
}
