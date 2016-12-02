// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BeliefUpdate.java

package apltk.interpreter.data;

import apltk.core.PointInTime;

public class BeliefUpdate
{

    public BeliefUpdate(String value)
    {
        relativeTime = 0L;
        creationTime = new PointInTime();
        this.value = value;
    }

    public String toString()
    {
        return value;
    }

    public int hashCode()
    {
        if(!$assertionsDisabled)
            throw new AssertionError("Implement!");
        else
            return 0;
    }

    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if(!(obj instanceof BeliefUpdate))
            return false;
        else
            return value.equals(((BeliefUpdate)obj).value);
    }

    public String value;
    public long relativeTime;
    public PointInTime creationTime;
    static final boolean $assertionsDisabled = !apltk/interpreter/data/BeliefUpdate.desiredAssertionStatus();

}
