// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AgentException.java

package eis.exceptions;


// Referenced classes of package eis.exceptions:
//            EnvironmentInterfaceException

public class AgentException extends EnvironmentInterfaceException
{

    public AgentException(String string)
    {
        super(string);
    }

    public AgentException(String string, Exception cause)
    {
        super(string, cause);
    }

    private static final long serialVersionUID = 0xe5ffa90f5e266dccL;
}
