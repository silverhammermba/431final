// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Message.java

package apltk.interpreter.data;


// Referenced classes of package apltk.interpreter.data:
//            Belief

public class Message
{

    public Message()
    {
        sender = null;
        receiver = null;
        timeSent = 0L;
        timeProcessed = 0L;
    }

    public Message(Belief value)
    {
        sender = null;
        receiver = null;
        timeSent = 0L;
        timeProcessed = 0L;
        this.value = value;
    }

    public String toString()
    {
        return value.toString();
    }

    public int hashCode()
    {
        if(!$assertionsDisabled)
            throw new AssertionError("Implement!");
        else
            return 0;
    }

    public String sender;
    public String receiver;
    public long timeSent;
    public long timeProcessed;
    public Belief value;
    static final boolean $assertionsDisabled = !apltk/interpreter/data/Message.desiredAssertionStatus();

}
