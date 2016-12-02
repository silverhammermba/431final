// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryCapabilities.java

package apltk.interpreter;


// Referenced classes of package apltk.interpreter:
//            QueryCapabilities

public static class queryMessages
{

    public queryMessages beliefs()
    {
        queryBeliefs = true;
        return this;
    }

    public queryBeliefs goals()
    {
        queryGoals = true;
        return this;
    }

    public queryGoals plans()
    {
        queryPlans = true;
        return this;
    }

    public queryPlans percepts()
    {
        queryPercepts = true;
        return this;
    }

    public queryPercepts events()
    {
        queryEvents = true;
        return this;
    }

    public queryEvents messages()
    {
        queryMessages = true;
        return this;
    }

    public QueryCapabilities build()
    {
        return new QueryCapabilities(this, null);
    }

    private boolean queryBeliefs;
    private boolean queryGoals;
    private boolean queryPlans;
    private boolean queryPercepts;
    private boolean queryEvents;
    private boolean queryMessages;







    public ()
    {
        queryBeliefs = false;
        queryGoals = false;
        queryPlans = false;
        queryPercepts = false;
        queryEvents = false;
        queryMessages = false;
    }
}
