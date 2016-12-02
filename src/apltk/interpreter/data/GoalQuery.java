// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GoalQuery.java

package apltk.interpreter.data;

import apltk.core.PointInTime;

public class GoalQuery
{

    public GoalQuery(String value)
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
        if(!(obj instanceof GoalQuery))
            return false;
        else
            return value.equals(((GoalQuery)obj).value);
    }

    public String value;
    public PointInTime creationTime;
    public long relativeTime;
    static final boolean $assertionsDisabled = !apltk/interpreter/data/GoalQuery.desiredAssertionStatus();

}
