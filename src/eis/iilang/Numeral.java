// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Numeral.java

package eis.iilang;


// Referenced classes of package eis.iilang:
//            Parameter, IILObjectVisitor, IILVisitor

public class Numeral extends Parameter
{

    public Numeral(Number value)
    {
        this.value = value;
    }

    protected String toXML(int depth)
    {
        return (new StringBuilder(String.valueOf(indent(depth)))).append("<number value=\"").append(value).append("\"/>").append("\n").toString();
    }

    public String toProlog()
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(value).toString();
        return ret;
    }

    public Number getValue()
    {
        return value;
    }

    public Object clone()
    {
        return new Numeral(value);
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
        if(!(obj instanceof Numeral))
            return false;
        Numeral other = (Numeral)obj;
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

    private static final long serialVersionUID = 0x662999e531680309L;
    private Number value;
}
