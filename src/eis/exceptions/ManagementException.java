// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ManagementException.java

package eis.exceptions;


// Referenced classes of package eis.exceptions:
//            EnvironmentInterfaceException

public class ManagementException extends EnvironmentInterfaceException
{

    public ManagementException(String string)
    {
        super(string);
    }

    public ManagementException(String string, Exception e)
    {
        super(string, e);
    }

    private static final long serialVersionUID = 0x74b32ad0ccbc52c1L;
}
