// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AllPerceptsModule.java

package eis.eis2java.util;

import eis.eis2java.annotation.AsPercept;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package eis.eis2java.util:
//            EIS2JavaUtil

public class AllPerceptsModule
{

    public AllPerceptsModule(Object entity)
        throws EntityException
    {
        this.entity = entity;
        Class clazz = entity.getClass();
        perceptProviders = EIS2JavaUtil.processPerceptAnnotations(clazz);
    }

    public synchronized void updatePercepts()
        throws PerceiveException
    {
        perceptBatch.clear();
        for(Iterator iterator = perceptProviders.iterator(); iterator.hasNext();)
        {
            Method method = (Method)iterator.next();
            AsPercept annotation = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
            String perceptName = annotation.name();
            Object percept;
            try
            {
                percept = method.invoke(entity, new Object[0]);
            }
            catch(IllegalArgumentException e)
            {
                throw new PerceiveException((new StringBuilder("Unable to update ")).append(perceptName).toString(), e);
            }
            catch(IllegalAccessException e)
            {
                throw new PerceiveException((new StringBuilder("Unable to update ")).append(perceptName).toString(), e);
            }
            catch(InvocationTargetException e)
            {
                throw new PerceiveException((new StringBuilder("Unable to update ")).append(perceptName).toString(), e);
            }
            if(annotation.event())
            {
                if(!eventPerceptBatch.containsKey(method))
                    eventPerceptBatch.put(method, new ArrayList());
                List events = (List)eventPerceptBatch.get(method);
                if(!annotation.multiplePercepts())
                    throw new PerceiveException((new StringBuilder("Unable to update ")).append(perceptName).append(" event percept must have multiplePercepts.").toString());
                if(!(percept instanceof Collection))
                    throw new PerceiveException((new StringBuilder("Unable to update ")).append(perceptName).append(" return value must be a collection.").toString());
                events.addAll((Collection)percept);
            } else
            {
                perceptBatch.put(method, percept);
            }
        }

        perceptBatch.putAll(eventPerceptBatch);
    }

    public synchronized Map getAllPercepts()
    {
        eventPerceptBatch.clear();
        return new HashMap(perceptBatch);
    }

    private final Set perceptProviders;
    private final Map perceptBatch = new HashMap();
    private final Map eventPerceptBatch = new HashMap();
    private final Object entity;
}
