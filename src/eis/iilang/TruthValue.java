// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TruthValue.java

package eis.iilang;


// Referenced classes of package eis.iilang:
//            Parameter, IILObjectVisitor, IILVisitor

public class TruthValue extends Parameter
{

    public TruthValue(String value)
    {
        this.value = value;
    }

    public TruthValue(boolean bool)
    {
        if(bool)
            value = "true";
        else
            value = "false";
    }

    protected String toXML(int depth)
    {
        return (new StringBuilder(String.valueOf(indent(depth)))).append("<truthvalue value=\"").append(value).append("\"/>").append("\n").toString();
    }

    public String toProlog()
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(value).toString();
        return ret;
    }

    public String getValue()
    {
        return value;
    }

    public boolean getBooleanValue()
    {
        if(value.equals("true"))
            return true;
        if(value.equals("false"))
            return false;
        else
            throw new AssertionError((new StringBuilder(String.valueOf(value))).append("cannot be converted to boolean").toString());
    }

    public Object clone()
    {
        return new TruthValue(value);
    }

    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof TruthValue))
            return false;
        TruthValue other = (TruthValue)obj;
        if(value == null)
        {
            if(other.value != null)
                return false;
        } else
        if(!value.equals(other.value))
            return false;
        return true;
    }

    public Object accept(IILObjectVisitor visitor, Object object)
    {
        return visitor.visit(this, object);
    }

    public void accept(IILVisitor visitor)
    {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 0x53db42d1cfd2f99L;
    private String value;
}
