// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StepResultCapabilities.java

package apltk.interpreter;


// Referenced classes of package apltk.interpreter:
//            StepResultCapabilities

public static class 
{

    public  sentMessages()
    {
        sentMessages = true;
        return this;
    }

    public sentMessages processedMessages()
    {
        processedMessages = true;
        return this;
    }

    public processedMessages addedBeliefs()
    {
        addedBeliefs = true;
        return this;
    }

    public addedBeliefs removedBeliefs()
    {
        removedBeliefs = true;
        return this;
    }

    public removedBeliefs addedGoals()
    {
        addedGoals = true;
        return this;
    }

    public addedGoals removedGoals()
    {
        removedGoals = true;
        return this;
    }

    public removedGoals addedPlans()
    {
        addedBeliefs = true;
        return this;
    }

    public addedBeliefs removedPlans()
    {
        removedPlans = true;
        return this;
    }

    public removedPlans addedPercepts()
    {
        addedPercepts = true;
        return this;
    }

    public addedPercepts processedPercepts()
    {
        processedPercepts = true;
        return this;
    }

    public processedPercepts addedCoalitions()
    {
        addedCoalitions = true;
        return this;
    }

    public addedCoalitions removedCoalitions()
    {
        removedCoalitions = true;
        return this;
    }

    public StepResultCapabilities build()
    {
        return new StepResultCapabilities(this, null);
    }

    private boolean sentMessages;
    private boolean processedMessages;
    private boolean raisedEvents;
    private boolean processedEvents;
    private boolean addedBeliefs;
    private boolean removedBeliefs;
    private boolean addedGoals;
    private boolean removedGoals;
    private boolean addedPlans;
    private boolean removedPlans;
    private boolean addedPercepts;
    private boolean processedPercepts;
    private boolean addedCoalitions;
    private boolean removedCoalitions;















    public ()
    {
    }
}
