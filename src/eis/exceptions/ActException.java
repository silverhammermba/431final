// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActException.java

package eis.exceptions;


// Referenced classes of package eis.exceptions:
//            EnvironmentInterfaceException

public class ActException extends EnvironmentInterfaceException
{

    public ActException(String string)
    {
        super(string);
        type = 0;
    }

    public ActException(String message, Exception cause)
    {
        super(message);
        type = 0;
        initCause(cause);
    }

    public ActException(String string, int type)
    {
        super(string);
        this.type = 0;
        setType(type);
    }

    public ActException(int type)
    {
        super("");
        this.type = 0;
        setType(type);
    }

    public ActException(int type, String message)
    {
        super(message);
        this.type = 0;
        setType(type);
    }

    public ActException(int type, String message, Exception cause)
    {
        super(message);
        this.type = 0;
        setType(type);
        initCause(cause);
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        if(type < 0 || type > 7)
        {
            if(!$assertionsDisabled)
                throw new AssertionError((new StringBuilder("Type \"")).append(type).append("\" not supported.").toString());
            type = 0;
        }
        this.type = type;
    }

    public String toString()
    {
        String ret = "";
        String strType = "";
        if(type == 0)
            strType = "not specific";
        else
        if(type == 1)
            strType = "not registered";
        else
        if(type == 2)
            strType = "no entities";
        else
        if(type == 3)
            strType = "wrong entity";
        else
        if(type == 4)
            strType = "not supported by environment";
        else
        if(type == 5)
            strType = "not supported by type";
        else
        if(type == 6)
            strType = "not supported by entity";
        else
        if(type == 7)
            strType = "failure";
        ret = (new StringBuilder(String.valueOf(ret))).append("ActException type=\"").append(strType).append("\"").toString();
        if(getMessage() != null)
            ret = (new StringBuilder(String.valueOf(ret))).append(" message=\"").append(getMessage()).append("\"").toString();
        if(getCause() != null)
            ret = (new StringBuilder(String.valueOf(ret))).append(" cause=\"").append(getCause()).append("\"").toString();
        return ret;
    }

    private static final long serialVersionUID = 0x7e3012d5b04b7e9eL;
    public static final int NOTSPECIFIC = 0;
    public static final int NOTREGISTERED = 1;
    public static final int NOENTITIES = 2;
    public static final int WRONGENTITY = 3;
    public static final int NOTSUPPORTEDBYENVIRONMENT = 4;
    public static final int NOTSUPPORTEDBYTYPE = 5;
    public static final int NOTSUPPORTEDBYENTITY = 6;
    public static final int FAILURE = 7;
    private int type;
    static final boolean $assertionsDisabled = !eis/exceptions/ActException.desiredAssertionStatus();

}
