// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Tool.java

package apltk.tool;

import apltk.core.Core;
import java.util.Collection;
import org.w3c.dom.Element;

// Referenced classes of package apltk.tool:
//            ToolException, ToolWindow

public interface Tool
{

    public abstract void init(Element element)
        throws ToolException;

    public abstract void addInterpreters(Collection collection);

    public abstract void addEnvironments(Collection collection);

    public abstract void processStepResults(Collection collection);

    public abstract void release();

    public abstract void setBasePath(String s);

    public abstract void setCore(Core core);

    public abstract void handleStarted();

    public abstract void handlePaused();

    public abstract void handleFinished();

    public abstract void handleSignal(String s, String s1);

    public abstract ToolWindow getWindow();

    public abstract String getName();
}
