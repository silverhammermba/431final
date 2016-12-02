// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translator.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

// Referenced classes of package eis.eis2java.translation:
//            Java2Parameter, Parameter2Java, Translator

private static class <init>
    implements Java2Parameter, Parameter2Java
{

    public Parameter[] translate(Number n)
        throws TranslationException
    {
        return (new Parameter[] {
            new Numeral(n)
        });
    }

    public Class translatesFrom()
    {
        return java/lang/Number;
    }

    public Number translate(Parameter parameter)
        throws TranslationException
    {
        if(!(parameter instanceof Numeral))
            throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
        else
            return ((Numeral)parameter).getValue();
    }

    public Class translatesTo()
    {
        return java/lang/Number;
    }

    public volatile Object translate(Parameter parameter)
        throws TranslationException
    {
        return translate(parameter);
    }

    public volatile Parameter[] translate(Object obj)
        throws TranslationException
    {
        return translate((Number)obj);
    }

    private ()
    {
    }

    ( )
    {
        this();
    }
}
