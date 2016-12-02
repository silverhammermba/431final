// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translator.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

// Referenced classes of package eis.eis2java.translation:
//            Parameter2Java, Translator

private static class <init>
    implements Parameter2Java
{

    public Integer translate(Parameter parameter)
        throws TranslationException
    {
        if(!(parameter instanceof Numeral))
            throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
        else
            return Integer.valueOf(((Numeral)parameter).getValue().intValue());
    }

    public Class translatesTo()
    {
        return java/lang/Integer;
    }

    public volatile Object translate(Parameter parameter)
        throws TranslationException
    {
        return translate(parameter);
    }

    private ()
    {
    }

    ( )
    {
        this();
    }
}
