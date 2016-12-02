// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParameterList.java

package eis.iilang;

import java.util.*;

// Referenced classes of package eis.iilang:
//            Parameter, IILObjectVisitor, IILVisitor

public class ParameterList extends Parameter
    implements Iterable
{

    public ParameterList()
    {
        list = null;
        list = new LinkedList();
    }

    public transient ParameterList(Parameter parameters[])
    {
        this();
        Parameter aparameter[];
        int j = (aparameter = parameters).length;
        for(int i = 0; i < j; i++)
        {
            Parameter param = aparameter[i];
            list.addLast(param);
        }

    }

    public ParameterList(Collection parameters)
    {
        this();
        Parameter param;
        for(Iterator iterator1 = parameters.iterator(); iterator1.hasNext(); list.addLast(param))
            param = (Parameter)iterator1.next();

    }

    public Iterator iterator()
    {
        return list.iterator();
    }

    public int indexOf(Parameter p)
    {
        return list.indexOf(p);
    }

    public int size()
    {
        return list.size();
    }

    public Parameter get(int i)
    {
        return (Parameter)list.get(i);
    }

    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    protected String toXML(int depth)
    {
        String xml = "";
        xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth)).append("<parameterList>").append("\n").toString();
        for(Iterator iterator1 = list.iterator(); iterator1.hasNext();)
        {
            Parameter p = (Parameter)iterator1.next();
            xml = (new StringBuilder(String.valueOf(xml))).append(p.toXML(depth + 1)).toString();
        }

        xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth)).append("</parameterList>").append("\n").toString();
        return xml;
    }

    public void add(Parameter parameter)
    {
        list.add(parameter);
    }

    public String toProlog()
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append("[").toString();
        if(!list.isEmpty())
        {
            ret = (new StringBuilder(String.valueOf(ret))).append(((Parameter)list.getFirst()).toProlog()).toString();
            for(int a = 1; a < list.size(); a++)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").append(((Parameter)list.get(a)).toProlog()).toString();

        }
        ret = (new StringBuilder(String.valueOf(ret))).append("]").toString();
        return ret;
    }

    public Object clone()
    {
        ParameterList ret = new ParameterList();
        Parameter p;
        for(Iterator iterator1 = list.iterator(); iterator1.hasNext(); ret.add((Parameter)p.clone()))
            p = (Parameter)iterator1.next();

        return ret;
    }

    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + (list != null ? list.hashCode() : 0);
        return result;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof ParameterList))
            return false;
        ParameterList other = (ParameterList)obj;
        if(list == null)
        {
            if(other.list != null)
                return false;
        } else
        if(!list.equals(other.list))
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

    private static final long serialVersionUID = 0x49faa209b7e8807bL;
    private LinkedList list;
}
