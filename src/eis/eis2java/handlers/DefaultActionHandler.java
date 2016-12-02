// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefaultActionHandler.java

package eis.eis2java.handlers;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.EIS2JavaUtil;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.iilang.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package eis.eis2java.handlers:
//            ActionHandler

public class DefaultActionHandler extends ActionHandler
{

    public DefaultActionHandler(Object entity)
        throws EntityException
    {
        this.entity = entity;
        actionMethods = EIS2JavaUtil.processActionAnnotations(entity.getClass());
    }

    public final boolean isSupportedByEntity(Action action)
    {
        String actionName = EIS2JavaUtil.getNameOfAction(action);
        return actionMethods.get(actionName) != null;
    }

    public final Percept performAction(Action action)
        throws ActException
    {
        Method actionMethod;
        String actionName = EIS2JavaUtil.getNameOfAction(action);
        actionMethod = (Method)actionMethods.get(actionName);
        if(actionMethod == null)
            throw new ActException(7, (new StringBuilder("Entity does not support action: ")).append(action).toString());
        return performAction(entity, actionMethod, action);
        Exception e;
        e;
        if(e instanceof ActException)
            throw (ActException)e;
        else
            throw new ActException(7, (new StringBuilder("execution of action ")).append(action).append("failed").toString(), e);
    }

    private Percept performAction(Object entity, Method method, Action action)
        throws ActException
    {
        Translator translator;
        LinkedList parameters;
        Object returnValue;
        IllegalAccessException e;
        translator = Translator.getInstance();
        Class parameterTypes[] = method.getParameterTypes();
        parameters = action.getParameters();
        Object arguments[] = new Object[parameters.size()];
        int i = 0;
        for(Iterator iterator = parameters.iterator(); iterator.hasNext();)
        {
            Parameter parameter = (Parameter)iterator.next();
            try
            {
                arguments[i] = translator.translate2Java(parameter, parameterTypes[i]);
            }
            catch(TranslationException e)
            {
                throw new ActException(7, (new StringBuilder("Action ")).append(action.getName()).append(" with parameters ").append(parameters).append(" failed to be translated").toString(), e);
            }
            i++;
        }

        try
        {
            returnValue = method.invoke(entity, arguments);
        }
        // Misplaced declaration of an exception variable
        catch(IllegalAccessException e)
        {
            throw new ActException(7, (new StringBuilder("Action ")).append(action.getName()).append(" with parameters ").append(parameters).append(" failed to execute").toString(), e);
        }
        // Misplaced declaration of an exception variable
        catch(IllegalAccessException e)
        {
            throw new ActException(7, (new StringBuilder("Action ")).append(action.getName()).append(" with parameters ").append(parameters).append(" failed to execute").toString(), e);
        }
        // Misplaced declaration of an exception variable
        catch(IllegalAccessException e)
        {
            throw new ActException(7, (new StringBuilder("Action ")).append(action.getName()).append(" with parameters ").append(parameters).append(" failed to execute").toString(), e);
        }
        if(returnValue == null)
            return null;
        return new Percept(action.getName(), translator.translate2Parameter(returnValue));
        e;
        throw new ActException(7, (new StringBuilder("Action ")).append(action.getName()).append(" with parameters ").append(parameters).append(" failed to return a proper failue").toString(), e);
    }

    protected final Map actionMethods;
    protected final Object entity;
}
