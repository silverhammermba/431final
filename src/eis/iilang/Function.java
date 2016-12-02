// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Function.java

package eis.iilang;

import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package eis.iilang:
//            Parameter, IILObjectVisitor, IILVisitor

public class Function extends Parameter
{

    public transient Function(String name, Parameter parameters[])
    {
        this.name = null;
        params = new LinkedList();
        setName(name);
        Parameter aparameter[];
        int j = (aparameter = parameters).length;
        for(int i = 0; i < j; i++)
        {
            Parameter p = aparameter[i];
            params.add(p);
        }

    }

    public Function(String name, LinkedList parameters)
    {
        this.name = null;
        params = new LinkedList();
        setName(name);
        params = parameters;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if(!$assertionsDisabled && !Character.isLowerCase(name.charAt(0)))
        {
            throw new AssertionError((new StringBuilder(String.valueOf(name))).append(" should start with a lowercase letter").toString());
        } else
        {
            this.name = name;
            return;
        }
    }

    public LinkedList getParameters()
    {
        return params;
    }

    public void setParameters(LinkedList parameters)
    {
        params = parameters;
    }

    public LinkedList getClonedParameters()
    {
        LinkedList ret = new LinkedList();
        Parameter p;
        for(Iterator iterator = params.iterator(); iterator.hasNext(); ret.add((Parameter)p.clone()))
            p = (Parameter)iterator.next();

        return ret;
    }

    protected String toXML(int depth)
    {
        String xml = "";
        xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth)).append("<function name=\"").append(name).append("\">").append("\n").toString();
        for(Iterator iterator = params.iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            xml = (new StringBuilder(String.valueOf(xml))).append(p.toXML(depth + 1)).toString();
        }

        xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth)).append("</function>").append("\n").toString();
        return xml;
    }

    public String toProlog()
    {
        String ret = name;
        if(params.size() > 0)
        {
            ret = (new StringBuilder(String.valueOf(ret))).append("(").toString();
            ret = (new StringBuilder(String.valueOf(ret))).append(((Parameter)params.getFirst()).toProlog()).toString();
            for(int a = 1; a < params.size(); a++)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").append(((Parameter)params.get(a)).toProlog()).toString();

            ret = (new StringBuilder(String.valueOf(ret))).append(")").toString();
        }
        return ret;
    }

    public Object clone()
    {
        return new Function(name, getClonedParameters());
    }

    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof Function))
            return false;
        Function other = (Function)obj;
        if(name == null)
        {
            if(other.name != null)
                return false;
        } else
        if(!name.equals(other.name))
            return false;
        if(params == null)
        {
            if(other.params != null)
                return false;
        } else
        if(!params.equals(other.params))
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

    private static final long serialVersionUID = 0xc7d5d05e3bccd7aeL;
    private String name;
    private LinkedList params;
    static final boolean $assertionsDisabled = !eis/iilang/Function.desiredAssertionStatus();

}
