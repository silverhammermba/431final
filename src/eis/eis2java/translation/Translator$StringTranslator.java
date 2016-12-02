// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translator.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

// Referenced classes of package eis.eis2java.translation:
//            Java2Parameter, Parameter2Java, Translator

private static class <init>
    implements Java2Parameter, Parameter2Java
{

    public String translate(Parameter parameter)
        throws TranslationException
    {
        if(!(parameter instanceof Identifier))
        {
            throw new TranslationException((new StringBuilder("Expected an Identifier parameter but got ")).append(parameter).toString());
        } else
        {
            Identifier id = (Identifier)parameter;
            return id.getValue();
        }
    }

    public Class translatesTo()
    {
        return java/lang/String;
    }

    public Parameter[] translate(String value)
        throws TranslationException
    {
        return (new Parameter[] {
            new Identifier(value)
        });
    }

    public Class translatesFrom()
    {
        return java/lang/String;
    }

    public volatile Parameter[] translate(Object obj)
        throws TranslationException
    {
        return translate((String)obj);
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
