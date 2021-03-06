// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Action.java

package eis.iilang;

import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package eis.iilang:
//            DataContainer, Parameter, IILObjectVisitor, IILVisitor

public class Action extends DataContainer
{

    public Action(String name)
    {
        super(name, new Parameter[0]);
    }

    public transient Action(String name, Parameter parameters[])
    {
        super(name, parameters);
    }

    public Action(String name, LinkedList parameters)
    {
        super(name, parameters);
    }

    protected String toXML(int depth)
    {
        String xml = "";
        xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth)).append("<action name=\"").append(name).append("\">").append("\n").toString();
        for(Iterator iterator = params.iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth + 1)).append("<actionParameter>").append("\n").toString();
            xml = (new StringBuilder(String.valueOf(xml))).append(p.toXML(depth + 2)).toString();
            xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth + 1)).append("</actionParameter>").append("\n").toString();
        }

        xml = (new StringBuilder(String.valueOf(xml))).append(indent(depth)).append("</action>").append("\n").toString();
        return xml;
    }

    public String toProlog()
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(name).toString();
        if(!params.isEmpty())
        {
            ret = (new StringBuilder(String.valueOf(ret))).append("(").toString();
            ret = (new StringBuilder(String.valueOf(ret))).append(((Parameter)params.getFirst()).toProlog()).toString();
            for(int a = 1; a < params.size(); a++)
            {
                Parameter p = (Parameter)params.get(a);
                ret = (new StringBuilder(String.valueOf(ret))).append(",").append(p.toProlog()).toString();
            }

            ret = (new StringBuilder(String.valueOf(ret))).append(")").toString();
        }
        return ret;
    }

    public Object clone()
    {
        Action ret = new Action(name, getClonedParameters());
        ret.setSource(source);
        return ret;
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof Action))
            return false;
        else
            return super.equals(obj);
    }

    public Object accept(IILObjectVisitor visitor, Object object)
    {
        return visitor.visit(this, object);
    }

    public void accept(IILVisitor visitor)
    {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 0x22770f0299a338aeL;
}
