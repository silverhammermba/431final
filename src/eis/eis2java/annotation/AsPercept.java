// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AsPercept.java

package eis.eis2java.annotation;

import eis.eis2java.translation.Filter;
import java.lang.annotation.Annotation;

public interface AsPercept
    extends Annotation
{

    public abstract String name();

    public abstract boolean multiplePercepts();

    public abstract boolean multipleArguments();

    public abstract eis.eis2java.translation.Filter.Type filter();

    public abstract boolean event();
}
