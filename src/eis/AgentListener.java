// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AgentListener.java

package eis;

import eis.iilang.Percept;

public interface AgentListener
{

    public abstract void handlePercept(String s, Percept percept);
}
