// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Interpreter.java

package apltk.interpreter;

import apltk.core.StepResult;
import eis.EnvironmentInterfaceStandard;
import java.util.Collection;
import org.w3c.dom.Element;

// Referenced classes of package apltk.interpreter:
//            InterpreterException, QueryCapabilities

public interface Interpreter
{

    public abstract void init(Element element)
        throws InterpreterException;

    public abstract void addEnvironment(EnvironmentInterfaceStandard environmentinterfacestandard);

    public abstract StepResult step();

    public abstract void release();

    public abstract Collection getAgents();

    public abstract QueryCapabilities getQueryFlags();

    public abstract Collection getBeliefBase(String s);

    public abstract Collection getGoalBase(String s);

    public abstract Collection getPlanBase(String s);

    public abstract Collection getEventBase(String s);

    public abstract Collection getPerceptBase(String s);

    public abstract Collection getMessageBox(String s);

    public abstract Collection getCoalitions();

    public abstract String getName();

    public abstract void setBasePath(String s);
}
