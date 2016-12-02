// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translator.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Parameter;
import eis.iilang.TruthValue;

// Referenced classes of package eis.eis2java.translation:
//            Java2Parameter, Parameter2Java, Translator

private static class <init>
    implements Java2Parameter, Parameter2Java
{

    public Parameter[] translate(Boolean b)
        throws TranslationException
    {
        return (new Parameter[] {
            new TruthValue(b.booleanValue())
        });
    }

    public Class translatesFrom()
    {
        return java/lang/Boolean;
    }

    public Boolean translate(Parameter parameter)
        throws TranslationException
    {
        if(!(parameter instanceof TruthValue))
            throw new TranslationException((new StringBuilder("Expected a Truthvalue parameter but got ")).append(parameter).toString());
        else
            return Boolean.valueOf(((TruthValue)parameter).getBooleanValue());
    }

    public Class translatesTo()
    {
        return java/lang/Boolean;
    }

    public volatile Object translate(Parameter parameter)
        throws TranslationException
    {
        return translate(parameter);
    }

    public volatile Parameter[] translate(Object obj)
        throws TranslationException
    {
        return translate((Boolean)obj);
    }

    private ()
    {
    }

    ( )
    {
        this();
    }
}
