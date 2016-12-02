// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EIS2JavaUtil.java

package eis.eis2java.util;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.exceptions.EntityException;
import eis.iilang.Action;
import java.lang.reflect.Method;
import java.util.*;

public class EIS2JavaUtil
{

    public EIS2JavaUtil()
    {
    }

    public static Set processPerceptAnnotations(Class clazz)
        throws EntityException
    {
        Set percepts = new HashSet();
        Method amethod[];
        int j = (amethod = clazz.getMethods()).length;
        for(int i = 0; i < j; i++)
        {
            Method method = amethod[i];
            AsPercept asPercept = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
            if(asPercept != null)
            {
                if(method.getParameterTypes().length != 0)
                    throw new EntityException("Percepts may not have any arguments");
                percepts.add(method);
            }
        }

        return percepts;
    }

    public static Map processActionAnnotations(Class clazz)
        throws EntityException
    {
        Map actions = new HashMap();
        Method amethod[];
        int j = (amethod = clazz.getMethods()).length;
        for(int i = 0; i < j; i++)
        {
            Method method = amethod[i];
            AsAction asAction = (AsAction)method.getAnnotation(eis/eis2java/annotation/AsAction);
            if(asAction != null)
            {
                String name = getNameOfAction(method);
                if(actions.containsKey(name))
                    throw new EntityException((new StringBuilder("Found two action definitions with the same name: ")).append(name).toString());
                actions.put(name, method);
            }
        }

        return actions;
    }

    public static String getNameOfAction(Action action)
    {
        return (new StringBuilder(String.valueOf(action.getName()))).append("/").append(action.getParameters().size()).toString();
    }

    public static String getNameOfAction(Method method)
    {
        AsAction annotation = (AsAction)method.getAnnotation(eis/eis2java/annotation/AsAction);
        if(annotation == null)
            return null;
        else
            return (new StringBuilder(String.valueOf(annotation.name()))).append("/").append(method.getParameterTypes().length).toString();
    }

    public static String getNameOfPercept(Method method)
    {
        AsPercept annotation = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
        if(annotation == null)
            return null;
        else
            return annotation.name();
    }
}
