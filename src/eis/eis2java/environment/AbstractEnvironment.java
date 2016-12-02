// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractEnvironment.java

package eis.eis2java.environment;

import eis.EIDefaultImpl;
import eis.eis2java.handlers.*;
import eis.exceptions.*;
import eis.iilang.Action;
import eis.iilang.Percept;
import java.util.*;

public abstract class AbstractEnvironment extends EIDefaultImpl
{

    public AbstractEnvironment()
    {
    }

    public final void registerEntity(String name, Object entity)
        throws EntityException
    {
        registerEntity(name, entity, ((ActionHandler) (new DefaultActionHandler(entity))), ((PerceptHandler) (new DefaultPerceptHandler(entity))));
    }

    public final void registerEntity(String name, Object entity, ActionHandler actionHandler, PerceptHandler perceptHandler)
        throws EntityException
    {
        actionHandlers.put(name, actionHandler);
        perceptHandlers.put(name, perceptHandler);
        entities.put(name, entity);
        addEntity(name);
    }

    public final void registerEntity(String name, String type, Object entity)
        throws EntityException
    {
        registerEntity(name, type, entity, ((ActionHandler) (new DefaultActionHandler(entity))), ((PerceptHandler) (new DefaultPerceptHandler(entity))));
    }

    public final void registerEntity(String name, String type, Object entity, ActionHandler actionHandler, PerceptHandler perceptHandler)
        throws EntityException
    {
        actionHandlers.put(name, actionHandler);
        perceptHandlers.put(name, perceptHandler);
        entities.put(name, entity);
        addEntity(name, type);
    }

    public final void deleteEntity(String name)
        throws EntityException, RelationException
    {
        super.deleteEntity(name);
        entities.remove(name);
        actionHandlers.remove(name);
        perceptHandlers.remove(name);
    }

    public final Object getEntity(String name)
    {
        return entities.get(name);
    }

    protected final LinkedList getAllPerceptsFromEntity(String name)
        throws PerceiveException, NoEnvironmentException
    {
        PerceptHandler handler = (PerceptHandler)perceptHandlers.get(name);
        if(handler == null)
            throw new PerceiveException((new StringBuilder("Entity with name ")).append(name).append(" has no handler").toString());
        else
            return handler.getAllPercepts();
    }

    protected final boolean isSupportedByEntity(Action action, String name)
    {
        Object entity = getEntity(name);
        ActionHandler handler = (ActionHandler)actionHandlers.get(entity);
        return handler.isSupportedByEntity(action);
    }

    protected final Percept performEntityAction(String name, Action action)
        throws ActException
    {
        ActionHandler handler = (ActionHandler)actionHandlers.get(name);
        if(handler == null)
            throw new ActException(7, (new StringBuilder("Entity with name ")).append(name).append(" has no handler").toString());
        else
            return handler.performAction(action);
    }

    public final String requiredVersion()
    {
        return "0.3";
    }

    private static final long serialVersionUID = 1L;
    private final Map entities = new HashMap();
    private final Map perceptHandlers = new HashMap();
    private final Map actionHandlers = new HashMap();
}
