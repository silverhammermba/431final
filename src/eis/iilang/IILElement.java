// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IILElement.java

package eis.iilang;

import java.io.Serializable;

// Referenced classes of package eis.iilang:
//            IILVisitor, IILObjectVisitor

public abstract class IILElement
    implements Serializable, Cloneable
{

    public IILElement()
    {
    }

    /**
     * @deprecated Method toString is deprecated
     */

    public final String toString()
    {
        if(toProlog)
            return toProlog();
        else
            return toXML();
    }

    /**
     * @deprecated Method toXML is deprecated
     */

    protected abstract String toXML(int i);

    /**
     * @deprecated Method toXMLWithHeader is deprecated
     */

    public final String toXMLWithHeader()
    {
        String xml = "";
        xml = (new StringBuilder(String.valueOf(xml))).append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").toString();
        xml = (new StringBuilder(String.valueOf(xml))).append(toXML(0)).toString();
        return xml;
    }

    /**
     * @deprecated Method toXML is deprecated
     */

    public final String toXML()
    {
        return toXML(0);
    }

    /**
     * @deprecated Method toProlog is deprecated
     */

    public abstract String toProlog();

    protected String indent(int depth)
    {
        String ret = "";
        for(int a = 0; a < depth; a++)
            ret = (new StringBuilder(String.valueOf(ret))).append("  ").toString();

        return ret;
    }

    public abstract Object clone();

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract void accept(IILVisitor iilvisitor);

    public abstract Object accept(IILObjectVisitor iilobjectvisitor, Object obj);

    private static final long serialVersionUID = 0x30275c75299c7ff9L;
    public static boolean toProlog = false;

}
