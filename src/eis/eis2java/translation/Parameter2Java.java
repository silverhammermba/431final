// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Parameter2Java.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Parameter;

public interface Parameter2Java
{

    public abstract Object translate(Parameter parameter)
        throws TranslationException;

    public abstract Class translatesTo();
}
