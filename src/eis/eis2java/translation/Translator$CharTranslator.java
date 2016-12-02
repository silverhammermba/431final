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

    public Character translate(Parameter parameter)
        throws TranslationException
    {
        if(!(parameter instanceof Identifier))
            throw new TranslationException((new StringBuilder("Expected an Identifier parameter but got ")).append(parameter).toString());
        Identifier id = (Identifier)parameter;
        String value = id.getValue();
        if(value.length() > 1)
            throw new TranslationException((new StringBuilder("A single character was expected instead a string of length ")).append(value.length()).append(" was given. Contents: ").append(value).toString());
        else
            return Character.valueOf(value.charAt(0));
    }

    public Class translatesTo()
    {
        return java/lang/Character;
    }

    public Parameter[] translate(Character value)
        throws TranslationException
    {
        return (new Parameter[] {
            new Identifier(String.valueOf(value))
        });
    }

    public Class translatesFrom()
    {
        return java/lang/Character;
    }

    public volatile Parameter[] translate(Object obj)
        throws TranslationException
    {
        return translate((Character)obj);
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
