// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AllPerceptPerceptHandler.java

package eis.eis2java.handlers;

import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package eis.eis2java.handlers:
//            AbstractPerceptHandler

public final class AllPerceptPerceptHandler extends AbstractPerceptHandler
{

    public AllPerceptPerceptHandler(AllPerceptsProvider entity)
        throws EntityException
    {
        super(entity);
        allPercepProvider = entity;
    }

    public final LinkedList getAllPercepts()
        throws PerceiveException
    {
        LinkedList percepts = new LinkedList();
        Map batchPerceptObjects = allPercepProvider.getAllPercepts();
        java.util.List translatedPercepts;
        for(Iterator iterator = batchPerceptObjects.entrySet().iterator(); iterator.hasNext(); percepts.addAll(translatedPercepts))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            Method method = (Method)entry.getKey();
            Object perceptObject = entry.getValue();
            java.util.List perceptObjects = unpackPerceptObject(method, perceptObject);
            translatedPercepts = translatePercepts(method, perceptObjects);
        }

        return percepts;
    }

    private final AllPerceptsProvider allPercepProvider;
}
