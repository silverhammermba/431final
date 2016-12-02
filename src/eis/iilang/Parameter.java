// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Parameter.java

package eis.iilang;


// Referenced classes of package eis.iilang:
//            IILElement

public abstract class Parameter extends IILElement
{

    public Parameter()
    {
    }

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract Object clone();

    private static final long serialVersionUID = 0xb10a32017c191cd0L;
}
