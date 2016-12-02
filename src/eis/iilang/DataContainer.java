// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataContainer.java

package eis.iilang;

import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package eis.iilang:
//            IILElement, Parameter, Percept

public abstract class DataContainer extends IILElement
{

    protected DataContainer()
    {
        name = null;
        params = new LinkedList();
        timeStamp = System.currentTimeMillis();
        source = null;
    }

    public transient DataContainer(String name, Parameter parameters[])
    {
        this.name = null;
        params = new LinkedList();
        timeStamp = System.currentTimeMillis();
        source = null;
        setName(name);
        Parameter aparameter[];
        int j = (aparameter = parameters).length;
        for(int i = 0; i < j; i++)
        {
            Parameter p = aparameter[i];
            params.add(p);
        }

    }

    public DataContainer(String name, LinkedList parameters)
    {
        this.name = null;
        params = new LinkedList();
        timeStamp = System.currentTimeMillis();
        source = null;
        setName(name);
        params = parameters;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if(!$assertionsDisabled && !Character.isLowerCase(name.charAt(0)))
        {
            throw new AssertionError((new StringBuilder(String.valueOf(name))).append(" should start with a lowercase letter").toString());
        } else
        {
            this.name = name;
            return;
        }
    }

    public LinkedList getParameters()
    {
        return params;
    }

    public LinkedList getClonedParameters()
    {
        LinkedList ret = new LinkedList();
        Parameter p;
        for(Iterator iterator = params.iterator(); iterator.hasNext(); ret.add((Parameter)p.clone()))
            p = (Parameter)iterator.next();

        return ret;
    }

    public void setParameters(LinkedList params)
    {
        this.params = params;
    }

    public void addParameter(Parameter p)
    {
        params.add(p);
    }

    public static Percept toPercept(DataContainer container)
    {
        Parameter parameters[] = new Parameter[container.params.size()];
        for(int a = 0; a < parameters.length; a++)
            parameters[a] = (Parameter)container.params.get(a);

        return new Percept(container.getName(), parameters);
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }

    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof DataContainer))
            return false;
        DataContainer dc = (DataContainer)obj;
        if(!dc.name.equals(name))
            return false;
        if(dc.params.size() != params.size())
            return false;
        for(int a = 0; a < params.size(); a++)
            if(!((Parameter)dc.params.get(a)).equals(params.get(a)))
                return false;

        return true;
    }

    private static final long serialVersionUID = 0xfd0c67bb46cf0fdL;
    protected String name;
    protected LinkedList params;
    protected long timeStamp;
    protected String source;
    static final boolean $assertionsDisabled = !eis/iilang/DataContainer.desiredAssertionStatus();

}
