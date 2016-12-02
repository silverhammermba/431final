// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefaultPerceptHandler.java

package eis.eis2java.handlers;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter;
import eis.eis2java.util.EIS2JavaUtil;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package eis.eis2java.handlers:
//            AbstractPerceptHandler

public final class DefaultPerceptHandler extends AbstractPerceptHandler
{

    public DefaultPerceptHandler(Object entity)
        throws EntityException
    {
        super(entity);
        perceptMethods = EIS2JavaUtil.processPerceptAnnotations(entity.getClass());
    }

    public final LinkedList getAllPercepts()
        throws PerceiveException
    {
        LinkedList percepts = new LinkedList();
        Method method;
        for(Iterator iterator = perceptMethods.iterator(); iterator.hasNext(); percepts.addAll(getPercepts(method)))
            method = (Method)iterator.next();

        return percepts;
    }

    private List getPercepts(Method method)
        throws PerceiveException
    {
        List perceptObjects = new ArrayList();
        AsPercept annotation = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
        eis.eis2java.translation.Filter.Type filter = annotation.filter();
        if(filter != eis.eis2java.translation.Filter.Type.ONCE || previousPercepts.get(method) == null)
            perceptObjects = getPerceptObjects(method);
        List percepts = translatePercepts(method, perceptObjects);
        return percepts;
    }

    private List getPerceptObjects(Method method)
        throws PerceiveException
    {
        AsPercept annotation = (AsPercept)method.getAnnotation(eis/eis2java/annotation/AsPercept);
        String perceptName = annotation.name();
        Object returnValue;
        try
        {
            returnValue = method.invoke(entity, new Object[0]);
        }
        catch(IllegalArgumentException e)
        {
            throw new PerceiveException((new StringBuilder("Unable to perceive ")).append(perceptName).toString(), e);
        }
        catch(IllegalAccessException e)
        {
            throw new PerceiveException((new StringBuilder("Unable to perceive ")).append(perceptName).toString(), e);
        }
        catch(InvocationTargetException e)
        {
            throw new PerceiveException((new StringBuilder("Unable to perceive ")).append(perceptName).toString(), e);
        }
        return unpackPerceptObject(method, returnValue);
    }

    protected final Collection perceptMethods;
}
