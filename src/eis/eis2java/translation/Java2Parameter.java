// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Java2Parameter.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Parameter;

public interface Java2Parameter
{

    public abstract Parameter[] translate(Object obj)
        throws TranslationException;

    public abstract Class translatesFrom();
}
