// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActionHandler.java

package eis.eis2java.handlers;

import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Percept;

public abstract class ActionHandler
{

    public ActionHandler()
    {
    }

    public abstract boolean isSupportedByEntity(Action action);

    public abstract Percept performAction(Action action)
        throws ActException;
}
