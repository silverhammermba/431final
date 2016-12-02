// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Belief.java

package apltk.interpreter.data;

import apltk.core.PointInTime;

public abstract class Belief
{

    public Belief(String value)
    {
        creationTime = new PointInTime();
        this.value = value;
    }

    public String toString()
    {
        return value;
    }

    public int hashCode()
    {
        int hc = 0;
        hc = value != null ? value.hashCode() : 0;
        return hc;
    }

    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if(!(obj instanceof Belief))
            return false;
        else
            return value.equals(((Belief)obj).value);
    }

    public String value;
    public PointInTime creationTime;
}
