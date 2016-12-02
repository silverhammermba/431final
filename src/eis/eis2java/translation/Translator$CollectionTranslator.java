// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translator.java

package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import java.util.AbstractCollection;
import java.util.Iterator;

// Referenced classes of package eis.eis2java.translation:
//            Java2Parameter, Translator

private static class <init>
    implements Java2Parameter
{

    public Parameter[] translate(AbstractCollection value)
        throws TranslationException
    {
        Parameter parameters[] = new Parameter[value.size()];
        int i = 0;
        Iterator it = value.iterator();
        Translator translator = Translator.getInstance();
        while(it.hasNext()) 
        {
            Parameter translation[] = translator.translate2Parameter(it.next());
            if(translation.length == 1)
                parameters[i] = translation[0];
            else
                parameters[i] = new ParameterList(translation);
            i++;
        }
        return (new Parameter[] {
            new ParameterList(parameters)
        });
    }

    public Class translatesFrom()
    {
        Class cls = java/util/AbstractCollection;
        return cls;
    }

    public volatile Parameter[] translate(Object obj)
        throws TranslationException
    {
        return translate((AbstractCollection)obj);
    }

    private ()
    {
    }

    ( )
    {
        this();
    }
}
