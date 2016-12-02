// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StepResult.java

package apltk.core;

import apltk.interpreter.Interpreter;
import java.util.Collection;
import java.util.Map;

public class StepResult
{

    public StepResult()
    {
        elapsedTime = 0L;
        interpreter = null;
        step = 0L;
        agentResults = null;
        beliefUpdates = null;
        beliefQueries = null;
        goalUpdates = null;
        goalQueries = null;
    }

    public long elapsedTime;
    public Interpreter interpreter;
    public long step;
    public Collection agentResults;
    public Map beliefUpdates;
    public Map beliefQueries;
    public Map goalUpdates;
    public Map goalQueries;
}
