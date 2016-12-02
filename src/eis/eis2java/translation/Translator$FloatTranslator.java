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

    public Float translate(Parameter parameter)
        throws TranslationException
    {
        if(!(parameter instanceof Numeral))
            throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
        else
            return Float.valueOf(((Numeral)parameter).getValue().floatValue());
    }

    public Class translatesTo()
    {
        return java/lang/Float;
    }

    public volatile Object translate(Parameter parameter)
        throws TranslationException
    {
        return translate(parameter);
    }

    private _cls9()
    {
    }

    _cls9(_cls9 _pcls9)
    {
        this();
    }
}
