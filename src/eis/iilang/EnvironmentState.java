// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnvironmentState.java

package eis.iilang;


public final class EnvironmentState extends Enum
{

    private EnvironmentState(String s, int i)
    {
        super(s, i);
    }

    public static EnvironmentState[] values()
    {
        EnvironmentState aenvironmentstate[];
        int i;
        EnvironmentState aenvironmentstate1[];
        System.arraycopy(aenvironmentstate = ENUM$VALUES, 0, aenvironmentstate1 = new EnvironmentState[i = aenvironmentstate.length], 0, i);
        return aenvironmentstate1;
    }

    public static EnvironmentState valueOf(String s)
    {
        return (EnvironmentState)Enum.valueOf(eis/iilang/EnvironmentState, s);
    }

    public static final EnvironmentState INITIALIZING;
    public static final EnvironmentState RUNNING;
    public static final EnvironmentState PAUSED;
    public static final EnvironmentState KILLED;
    private static final EnvironmentState ENUM$VALUES[];

    static 
    {
        INITIALIZING = new EnvironmentState("INITIALIZING", 0);
        RUNNING = new EnvironmentState("RUNNING", 1);
        PAUSED = new EnvironmentState("PAUSED", 2);
        KILLED = new EnvironmentState("KILLED", 3);
        ENUM$VALUES = (new EnvironmentState[] {
            INITIALIZING, RUNNING, PAUSED, KILLED
        });
    }
}
