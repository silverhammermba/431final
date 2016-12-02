// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RelationException.java

package eis.exceptions;


// Referenced classes of package eis.exceptions:
//            EnvironmentInterfaceException

public class RelationException extends EnvironmentInterfaceException
{

    public RelationException(String string)
    {
        super(string);
    }

    public RelationException(String string, Exception cause)
    {
        super(string, cause);
    }

    private static final long serialVersionUID = 0x273005cd0166fbaL;
}
