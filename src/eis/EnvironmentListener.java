// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnvironmentListener.java

package eis;

import eis.iilang.EnvironmentState;
import java.util.Collection;

public interface EnvironmentListener
{

    public abstract void handleStateChange(EnvironmentState environmentstate);

    public abstract void handleFreeEntity(String s, Collection collection);

    public abstract void handleDeletedEntity(String s, Collection collection);

    public abstract void handleNewEntity(String s);
}
