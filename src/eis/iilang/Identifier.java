// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Identifier.java

package eis.iilang;


// Referenced classes of package eis.iilang:
//            Parameter, IILObjectVisitor, IILVisitor

public class Identifier extends Parameter
{

    public Identifier(String value)
    {
        this.value = null;
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    protected String toXML(int depth)
    {
        return (new StringBuilder(String.valueOf(indent(depth)))).append("<identifier value=\"").append(value).append("\"/>").append("\n").toString();
    }

    public String toProlog()
    {
        String ret = value;
        return ret;
    }

    public Object clone()
    {
        return new Identifier(value);
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
        if(!(obj instanceof Identifier))
            return false;
        Identifier other = (Identifier)obj;
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

    private static final long serialVersionUID = 0xd74dcaa92adb50bbL;
    private String value;
}
