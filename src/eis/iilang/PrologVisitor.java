// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PrologVisitor.java

package eis.iilang;

import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package eis.iilang:
//            IILObjectVisitor, Action, Parameter, Function, 
//            Identifier, Numeral, ParameterList, Percept, 
//            TruthValue, IILElement, DataContainer

public class PrologVisitor
    implements IILObjectVisitor
{

    public PrologVisitor()
    {
    }

    public Object visit(Action element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(element.name).append("(").toString();
        for(Iterator iterator = element.getParameters().iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, null)).toString();
            if(element.getParameters().indexOf(p) != element.getParameters().size() - 1)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(")").toString();
        return ret;
    }

    public Object visit(DataContainer element, Object object)
    {
        return "UNKNOWN";
    }

    public Object visit(Function element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(element.getName()).append("(").toString();
        int count = 0;
        for(Iterator iterator = element.getParameters().iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, null)).toString();
            if(count < element.getParameters().size() - 1)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").toString();
            count++;
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(")").toString();
        return ret;
    }

    public Object visit(Identifier element, Object object)
    {
        return element.getValue();
    }

    public Object visit(IILElement element, Object object)
    {
        return "UNKNOWN";
    }

    public Object visit(Numeral element, Object object)
    {
        return element.getValue();
    }

    public Object visit(Parameter element, Object object)
    {
        return "UNKNOWN";
    }

    public Object visit(ParameterList element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append("[").toString();
        for(Iterator iterator = element.iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, null)).toString();
            if(element.indexOf(p) != element.size() - 1)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append("]").toString();
        return ret;
    }

    public Object visit(Percept element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(element.name).append("(").toString();
        for(Iterator iterator = element.getParameters().iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, null)).toString();
            if(element.getParameters().indexOf(p) != element.getParameters().size() - 1)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(")").toString();
        return ret;
    }

    public Object visit(TruthValue element, Object object)
    {
        return element.getValue();
    }

    public static String staticVisit(IILElement element)
    {
        PrologVisitor visitor = new PrologVisitor();
        return (String)element.accept(visitor, "");
    }
}
