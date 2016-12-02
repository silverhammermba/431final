// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLVisitor.java

package eis.iilang;

import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package eis.iilang:
//            IILObjectVisitor, Action, Parameter, Function, 
//            Identifier, Numeral, ParameterList, Percept, 
//            TruthValue, IILElement, DataContainer

public class XMLVisitor
    implements IILObjectVisitor
{

    public XMLVisitor()
    {
    }

    public Object visit(Action element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("<action name=\"").append(element.name).append("\">").append(newline).toString();
        for(Iterator iterator = element.getParameters().iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append(indent).append("<actionParameter>").append(newline).toString();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, (new StringBuilder(String.valueOf(object.toString()))).append(indent).append(indent).toString())).toString();
            ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append(indent).append("</actionParameter>").append(newline).toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("</action>").toString();
        return ret;
    }

    public Object visit(DataContainer element, Object object)
    {
        if(!$assertionsDisabled)
            throw new AssertionError("Not expected");
        else
            return "";
    }

    public Object visit(Function element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("<function name=\"").append(element.getName()).append("\">").append(newline).toString();
        for(Iterator iterator = element.getParameters().iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, (new StringBuilder(String.valueOf(object.toString()))).append(indent).toString())).toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("</function>").append(newline).toString();
        return ret;
    }

    public Object visit(Identifier element, Object object)
    {
        return (new StringBuilder(String.valueOf(object.toString()))).append("<identifier value=\"").append(element.getValue()).append("\"/>").append(newline).toString();
    }

    public Object visit(IILElement element, Object object)
    {
        if(!$assertionsDisabled)
            throw new AssertionError("Not expected");
        else
            return "";
    }

    public Object visit(Numeral element, Object object)
    {
        return (new StringBuilder(String.valueOf(object.toString()))).append("<number value=\"").append(element.getValue()).append("\"/>").append(newline).toString();
    }

    public Object visit(Parameter element, Object object)
    {
        if(!$assertionsDisabled)
            throw new AssertionError("Not expected");
        else
            return "";
    }

    public Object visit(ParameterList element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("<parameterList>").append(newline).toString();
        for(Iterator iterator = element.iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, (new StringBuilder(String.valueOf(object.toString()))).append(indent).toString())).toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("</parameterList>").append(newline).toString();
        return ret;
    }

    public Object visit(Percept element, Object object)
    {
        String ret = "";
        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("<percept name=\"").append(element.name).append("\">").append(newline).toString();
        for(Iterator iterator = element.getParameters().iterator(); iterator.hasNext();)
        {
            Parameter p = (Parameter)iterator.next();
            ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append(indent).append("<perceptParameter>").append(newline).toString();
            ret = (new StringBuilder(String.valueOf(ret))).append(p.accept(this, (new StringBuilder(String.valueOf(object.toString()))).append(indent).append(indent).toString())).toString();
            ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append(indent).append("</perceptParameter>").append(newline).toString();
        }

        ret = (new StringBuilder(String.valueOf(ret))).append(object.toString()).append("</percept>").append(newline).toString();
        return ret;
    }

    public Object visit(TruthValue element, Object object)
    {
        return (new StringBuilder(String.valueOf(object.toString()))).append("<truthvalue value=\"").append(element.getValue()).append("\"/>").append(newline).toString();
    }

    public static String staticVisit(IILElement element)
    {
        IILObjectVisitor visitor = new XMLVisitor();
        return (String)element.accept(visitor, "");
    }

    private static String indent = "  ";
    private static String newline = "\n";
    static final boolean $assertionsDisabled = !eis/iilang/XMLVisitor.desiredAssertionStatus();

}
