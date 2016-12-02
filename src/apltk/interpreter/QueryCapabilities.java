// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryCapabilities.java

package apltk.interpreter;

import java.io.PrintStream;

public class QueryCapabilities
{
    public static class Builder
    {

        public Builder beliefs()
        {
            queryBeliefs = true;
            return this;
        }

        public Builder goals()
        {
            queryGoals = true;
            return this;
        }

        public Builder plans()
        {
            queryPlans = true;
            return this;
        }

        public Builder percepts()
        {
            queryPercepts = true;
            return this;
        }

        public Builder events()
        {
            queryEvents = true;
            return this;
        }

        public Builder messages()
        {
            queryMessages = true;
            return this;
        }

        public QueryCapabilities build()
        {
            return new QueryCapabilities(this);
        }

        private boolean queryBeliefs;
        private boolean queryGoals;
        private boolean queryPlans;
        private boolean queryPercepts;
        private boolean queryEvents;
        private boolean queryMessages;







        public Builder()
        {
            queryBeliefs = false;
            queryGoals = false;
            queryPlans = false;
            queryPercepts = false;
            queryEvents = false;
            queryMessages = false;
        }
    }


    private QueryCapabilities()
    {
        throw new AssertionError();
    }

    private QueryCapabilities(Builder builder)
    {
        queryBeliefs = builder.queryBeliefs;
        queryGoals = builder.queryGoals;
        queryPlans = builder.queryPlans;
        queryPercepts = builder.queryPercepts;
        queryEvents = builder.queryEvents;
        queryMessages = builder.queryMessages;
    }

    public String toString()
    {
        String ret = "";
        ret = (new StringBuilder()).append(ret).append("Beliefs ").append(queryBeliefs).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Goals ").append(queryGoals).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Plans ").append(queryPlans).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Percepts ").append(queryPercepts).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Events ").append(queryEvents).append("\n").toString();
        ret = (new StringBuilder()).append(ret).append("Messages ").append(queryMessages).append("\n").toString();
        return ret;
    }

    public static void main(String args[])
    {
        QueryCapabilities flags = null;
        flags = (new Builder()).build();
        System.out.println(flags);
        flags = (new Builder()).beliefs().build();
        System.out.println(flags);
        flags = (new Builder()).beliefs().events().build();
        System.out.println(flags);
        flags = (new Builder()).beliefs().events().goals().build();
        System.out.println(flags);
        flags = (new Builder()).beliefs().events().goals().percepts().build();
        System.out.println(flags);
        flags = (new Builder()).beliefs().events().goals().percepts().plans().build();
        System.out.println(flags);
        flags = (new Builder()).beliefs().events().goals().percepts().plans().messages().build();
        System.out.println(flags);
    }

    public boolean canQueryBeliefs()
    {
        return queryBeliefs;
    }

    public boolean canQueryGoals()
    {
        return queryGoals;
    }

    public boolean canQueryPlans()
    {
        return queryPlans;
    }

    public boolean canQueryPercepts()
    {
        return queryPercepts;
    }

    public boolean canQueryEvents()
    {
        return queryEvents;
    }

    public boolean canQueryMessages()
    {
        return queryMessages;
    }


    private boolean queryBeliefs;
    private boolean queryGoals;
    private boolean queryPlans;
    private boolean queryPercepts;
    private boolean queryEvents;
    private boolean queryMessages;
}
