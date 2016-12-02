// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PerceptHandler.java

package eis.eis2java.handlers;

import eis.exceptions.PerceiveException;
import java.util.LinkedList;

public abstract class PerceptHandler
{

    public PerceptHandler()
    {
    }

    public abstract LinkedList getAllPercepts()
        throws PerceiveException;
}
