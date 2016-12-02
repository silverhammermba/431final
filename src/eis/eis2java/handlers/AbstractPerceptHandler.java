// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractPerceptHandler.java

package eis.eis2java.handlers;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Filter;
import eis.eis2java.translation.Translator;
import eis.exceptions.PerceiveException;
import eis.iilang.*;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package eis.eis2java.handlers:
//            PerceptHandler

public abstract class AbstractPerceptHandler extends PerceptHandler
{

    public AbstractPerceptHandler(Object entity)
    {
        if(!$assertionsDisabled && entity == null)
        {
            throw new AssertionError();
        } else
        {
            this.entity = entity;
            return;
        }
    }

    protected final List translatePercepts(Method method, List perceptObjects)
        throws PerceiveException
    {
        List addList = new ArrayList();
        List delList = new ArrayList();
        AsPercept annotation = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
        eis.eis2java.translation.Filter.Type filter = annotation.filter();
        String perceptName = annotation.name();
        List previous = (List)previousPercepts.get(method);
        if(filter == eis.eis2java.translation.Filter.Type.ONCE && previous != null)
            return new ArrayList();
        if(previous == null)
        {
            previous = new LinkedList();
            previousPercepts.put(method, previous);
        }
        switch($SWITCH_TABLE$eis$eis2java$translation$Filter$Type()[filter.ordinal()])
        {
        default:
            break;

        case 1: // '\001'
        case 2: // '\002'
            addList = perceptObjects;
            break;

        case 3: // '\003'
            if(!perceptObjects.equals(previous))
                addList = perceptObjects;
            break;

        case 4: // '\004'
            addList.addAll(perceptObjects);
            if(previous != null)
            {
                addList.removeAll(previous);
                delList.addAll(previous);
                delList.removeAll(perceptObjects);
            }
            break;
        }
        List percepts = new ArrayList();
        Parameter parameters[];
        for(Iterator iterator = addList.iterator(); iterator.hasNext(); percepts.add(new Percept(perceptName, parameters)))
        {
            Object javaObject = iterator.next();
            try
            {
                parameters = Translator.getInstance().translate2Parameter(javaObject);
                if(annotation.multipleArguments())
                    parameters = extractMultipleParameters(parameters);
            }
            catch(TranslationException e)
            {
                throw new PerceiveException((new StringBuilder("Unable to translate percept ")).append(perceptName).toString(), e);
            }
        }

        Parameter parameters[];
        for(Iterator iterator1 = delList.iterator(); iterator1.hasNext(); percepts.add(new Percept("not", new Parameter[] {
    new Function(perceptName, parameters)
})))
        {
            Object javaObject = iterator1.next();
            try
            {
                parameters = Translator.getInstance().translate2Parameter(javaObject);
                if(annotation.multipleArguments())
                    parameters = extractMultipleParameters(parameters);
            }
            catch(TranslationException e)
            {
                throw new PerceiveException((new StringBuilder("Unable to translate percept ")).append(perceptName).toString(), e);
            }
        }

        previousPercepts.put(method, perceptObjects);
        return percepts;
    }

    private Parameter[] extractMultipleParameters(Parameter parameters[])
        throws PerceiveException
    {
        if(parameters.length == 1 && (parameters[0] instanceof ParameterList))
        {
            ParameterList params = (ParameterList)parameters[0];
            parameters = new Parameter[params.size()];
            for(int i = 0; i < params.size(); i++)
                parameters[i] = params.get(i);

        } else
        {
            throw new PerceiveException((new StringBuilder("multipleArguments parameter is set and therefore expecting a set but got ")).append(parameters).toString());
        }
        return parameters;
    }

    protected final List unpackPerceptObject(Method method, Object perceptObject)
        throws PerceiveException
    {
        AsPercept annotation = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
        String perceptName = annotation.name();
        if(!annotation.multiplePercepts())
        {
            List unpacked = new ArrayList(1);
            if(perceptObject != null)
                unpacked.add(perceptObject);
            return unpacked;
        }
        if(!(perceptObject instanceof Collection))
        {
            throw new PerceiveException((new StringBuilder("Unable to perceive ")).append(perceptName).append(" because a collection was expected but a ").append(perceptObject.getClass()).append(" was returned instead").toString());
        } else
        {
            Collection javaCollection = (Collection)perceptObject;
            ArrayList unpacked = new ArrayList(javaCollection);
            return unpacked;
        }
    }

    static int[] $SWITCH_TABLE$eis$eis2java$translation$Filter$Type()
    {
        $SWITCH_TABLE$eis$eis2java$translation$Filter$Type;
        if($SWITCH_TABLE$eis$eis2java$translation$Filter$Type == null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        JVM INSTR pop ;
        int ai[] = new int[eis.eis2java.translation.Filter.Type.values().length];
        try
        {
            ai[eis.eis2java.translation.Filter.Type.ALWAYS.ordinal()] = 1;
        }
        catch(NoSuchFieldError _ex) { }
        try
        {
            ai[eis.eis2java.translation.Filter.Type.ONCE.ordinal()] = 2;
        }
        catch(NoSuchFieldError _ex) { }
        try
        {
            ai[eis.eis2java.translation.Filter.Type.ON_CHANGE.ordinal()] = 3;
        }
        catch(NoSuchFieldError _ex) { }
        try
        {
            ai[eis.eis2java.translation.Filter.Type.ON_CHANGE_NEG.ordinal()] = 4;
        }
        catch(NoSuchFieldError _ex) { }
        return $SWITCH_TABLE$eis$eis2java$translation$Filter$Type = ai;
    }

    protected final Object entity;
    protected final Map previousPercepts = new HashMap();
    static final boolean $assertionsDisabled = !eis/eis2java/handlers/AbstractPerceptHandler.desiredAssertionStatus();
    private static int $SWITCH_TABLE$eis$eis2java$translation$Filter$Type[];

}
