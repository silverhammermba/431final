// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GoalUpdate.java

package apltk.interpreter.data;

import apltk.core.PointInTime;

public class GoalUpdate
{

    public GoalUpdate(String value)
    {
        creationTime = new PointInTime();
        relativeTime = 0L;
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
        if(!(obj instanceof GoalUpdate))
            return false;
        else
            return value.equals(((GoalUpdate)obj).value);
    }

    public String value;
    public PointInTime creationTime;
    public long relativeTime;
    static final boolean $assertionsDisabled = !apltk/interpreter/data/GoalUpdate.desiredAssertionStatus();

}
