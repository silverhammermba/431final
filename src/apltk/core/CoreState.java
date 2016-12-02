// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Core.java

package apltk.core;


final class CoreState extends Enum
{

    public static CoreState[] values()
    {
        return (CoreState[])$VALUES.clone();
    }

    public static CoreState valueOf(String name)
    {
        return (CoreState)Enum.valueOf(apltk/core/CoreState, name);
    }

    private CoreState(String s, int i)
    {
        super(s, i);
    }

    public static final CoreState RUNNING;
    public static final CoreState PAUSED;
    public static final CoreState FINISHED;
    private static final CoreState $VALUES[];

    static 
    {
        RUNNING = new CoreState("RUNNING", 0);
        PAUSED = new CoreState("PAUSED", 1);
        FINISHED = new CoreState("FINISHED", 2);
        $VALUES = (new CoreState[] {
            RUNNING, PAUSED, FINISHED
        });
    }
}
