// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnvironmentInterfaceStandard.java

package eis;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.QueryException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import java.util.Collection;
import java.util.Map;

// Referenced classes of package eis:
//            EnvironmentListener, AgentListener

public interface EnvironmentInterfaceStandard
{

    public abstract void attachEnvironmentListener(EnvironmentListener environmentlistener);

    public abstract void detachEnvironmentListener(EnvironmentListener environmentlistener);

    public abstract void attachAgentListener(String s, AgentListener agentlistener);

    public abstract void detachAgentListener(String s, AgentListener agentlistener);

    public abstract void registerAgent(String s)
        throws AgentException;

    public abstract void unregisterAgent(String s)
        throws AgentException;

    public abstract Collection getAgents();

    public abstract Collection getEntities();

    public abstract void associateEntity(String s, String s1)
        throws RelationException;

    public abstract void freeEntity(String s)
        throws RelationException, EntityException;

    public abstract void freeAgent(String s)
        throws RelationException;

    public abstract void freePair(String s, String s1)
        throws RelationException;

    public abstract Collection getAssociatedEntities(String s)
        throws AgentException;

    public abstract Collection getAssociatedAgents(String s)
        throws EntityException;

    public abstract Collection getFreeEntities();

    public abstract String getType(String s)
        throws EntityException;

    public transient abstract Map performAction(String s, Action action, String as[])
        throws ActException;

    public transient abstract Map getAllPercepts(String s, String as[])
        throws PerceiveException, NoEnvironmentException;

    public abstract boolean isStateTransitionValid(EnvironmentState environmentstate, EnvironmentState environmentstate1);

    public abstract void init(Map map)
        throws ManagementException;

    public abstract void reset(Map map)
        throws ManagementException;

    public abstract void start()
        throws ManagementException;

    public abstract void pause()
        throws ManagementException;

    public abstract void kill()
        throws ManagementException;

    public abstract EnvironmentState getState();

    public abstract boolean isInitSupported();

    public abstract boolean isStartSupported();

    public abstract boolean isPauseSupported();

    public abstract boolean isKillSupported();

    public abstract String requiredVersion();

    public abstract String queryProperty(String s)
        throws QueryException;

    public abstract String queryEntityProperty(String s, String s1)
        throws QueryException;
}
