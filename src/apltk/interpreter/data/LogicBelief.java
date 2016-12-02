// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogicBelief.java

package apltk.interpreter.data;

import java.util.*;

// Referenced classes of package apltk.interpreter.data:
//            Belief

public class LogicBelief extends Belief
{

    public LogicBelief(String predicate)
    {
        super("invalid");
        this.predicate = null;
        parameters = null;
        this.predicate = predicate;
        parameters = new Vector();
        value = toString();
    }

    public LogicBelief(String predicate, Collection parameters)
    {
        super("invalid");
        this.predicate = null;
        this.parameters = null;
        this.predicate = predicate;
        this.parameters = new Vector(parameters);
        value = toString();
    }

    public transient LogicBelief(String predicate, String parameters[])
    {
        super("invalid");
        this.predicate = null;
        this.parameters = null;
        this.predicate = predicate;
        this.parameters = new Vector();
        String arr$[] = parameters;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String p = arr$[i$];
            this.parameters.add(p);
        }

        value = toString();
    }

    public String toString()
    {
        String ret = "";
        ret = (new StringBuilder()).append(ret).append(predicate).toString();
        if(parameters != null)
        {
            ret = (new StringBuilder()).append(ret).append("(").toString();
            for(Iterator i$ = parameters.iterator(); i$.hasNext();)
            {
                String p = (String)i$.next();
                ret = (new StringBuilder()).append(ret).append(p).append(",").toString();
            }

            ret = ret.substring(0, ret.length() - 1);
            ret = (new StringBuilder()).append(ret).append(")").toString();
        }
        ret = (new StringBuilder()).append(ret).append(".").toString();
        return ret;
    }

    public String getPredicate()
    {
        return predicate;
    }

    public Vector getParameters()
    {
        return parameters;
    }

    private String predicate;
    private Vector parameters;
}
