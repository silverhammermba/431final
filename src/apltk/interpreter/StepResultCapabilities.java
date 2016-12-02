// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StepResultCapabilities.java

package apltk.interpreter;

import java.io.PrintStream;

public class StepResultCapabilities
{
    public static class Builder
    {

        public Builder sentMessages()
        {
            sentMessages = true;
            return this;
        }

        public Builder processedMessages()
        {
            processedMessages = true;
            return this;
        }

        public Builder addedBeliefs()
        {
            addedBeliefs = true;
            return this;
        }

        public Builder removedBeliefs()
        {
            removedBeliefs = true;
            return this;
        }

        public Builder addedGoals()
        {
            addedGoals = true;
            return this;
        }

        public Builder removedGoals()
        {
            removedGoals = true;
            return this;
        }

        public Builder addedPlans()
        {
            addedBeliefs = true;
            return this;
        }

        public Builder removedPlans()
        {
            removedPlans = true;
            return this;
        }

        public Builder addedPercepts()
        {
            addedPercepts = true;
            return this;
        }

        public Builder processedPercepts()
        {
            processedPercepts = true;
            return this;
        }

        public Builder addedCoalitions()
        {
            addedCoalitions = true;
            return this;
        }

        public Builder removedCoalitions()
        {
            removedCoalitions = true;
            return this;
        }

        public StepResultCapabilities build()
        {
            return new StepResultCapabilities(this);
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















        public Builder()
        {
        }
    }


    private StepResultCapabilities()
    {
        throw new AssertionError();
    }

    private StepResultCapabilities(Builder builder)
    {
        sentMessages = builder.sentMessages;
        processedMessages = builder.processedMessages;
        raisedEvents = builder.raisedEvents;
        processedEvents = builder.processedEvents;
        addedBeliefs = builder.addedBeliefs;
        removedBeliefs = builder.removedBeliefs;
        addedGoals = builder.addedGoals;
        removedGoals = builder.removedGoals;
        addedPlans = builder.addedPlans;
        removedPlans = builder.removedPlans;
        addedPercepts = builder.addedPercepts;
        processedPercepts = builder.processedPercepts;
        addedCoalitions = builder.addedCoalitions;
        removedCoalitions = builder.removedCoalitions;
    }

    public String toString()
    {
        String ret = "";
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(sentMessages).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(processedMessages).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(raisedEvents).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(processedEvents).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(addedBeliefs).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(removedBeliefs).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(addedGoals).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(removedGoals).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(addedPlans).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(removedPlans).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(addedPercepts).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(processedPercepts).append("\n").toString();
        return ret;
    }

    public static void main(String args[])
    {
        StepResultCapabilities flags = null;
        flags = (new Builder()).build();
        System.out.println(flags);
    }

    public boolean yieldsSentMessages()
    {
        return sentMessages;
    }

    public boolean yieldsProcessedMessages()
    {
        return processedMessages;
    }

    public boolean yieldsRaisedEvents()
    {
        return raisedEvents;
    }

    public boolean yieldsProcessedEvents()
    {
        return processedEvents;
    }

    public boolean yieldsAddedBeliefs()
    {
        return addedBeliefs;
    }

    public boolean yieldsRemovedBeliefs()
    {
        return removedBeliefs;
    }

    public boolean yieldsAddedGoals()
    {
        return addedGoals;
    }

    public boolean yieldsRemovedGoals()
    {
        return removedGoals;
    }

    public boolean yieldsAddedPlans()
    {
        return addedPlans;
    }

    public boolean yieldsRemovedPlans()
    {
        return removedPlans;
    }

    public boolean yieldsAddedPercepts()
    {
        return addedPercepts;
    }

    public boolean yieldsProcessedPercepts()
    {
        return processedPercepts;
    }

    public boolean yieldsAddedCoalitions()
    {
        return addedCoalitions;
    }

    public boolean yieldsRemovedCoalitions()
    {
        return removedCoalitions;
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
}
