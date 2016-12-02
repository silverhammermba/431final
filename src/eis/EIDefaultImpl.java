// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EIDefaultImpl.java

package eis;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Percept;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Referenced classes of package eis:
//            EnvironmentInterfaceStandard, AgentListener, EnvironmentListener

public abstract class EIDefaultImpl
    implements EnvironmentInterfaceStandard, Serializable
{

    public EIDefaultImpl()
    {
        registeredAgents = null;
        entities = null;
        agentsToEntities = null;
        environmentListeners = null;
        agentsToAgentListeners = null;
        entitiesToTypes = null;
        state = null;
        environmentListeners = new Vector();
        agentsToAgentListeners = new ConcurrentHashMap();
        registeredAgents = new LinkedList();
        entities = new LinkedList();
        agentsToEntities = new ConcurrentHashMap();
        entitiesToTypes = new HashMap();
        state = EnvironmentState.INITIALIZING;
    }

    public void reset(Map parameters)
        throws ManagementException
    {
        state = EnvironmentState.PAUSED;
    }

    public void attachEnvironmentListener(EnvironmentListener listener)
    {
        if(!environmentListeners.contains(listener))
            environmentListeners.add(listener);
    }

    public void detachEnvironmentListener(EnvironmentListener listener)
    {
        if(environmentListeners.contains(listener))
            environmentListeners.remove(listener);
    }

    public void attachAgentListener(String agent, AgentListener listener)
    {
        if(!registeredAgents.contains(agent))
            return;
        HashSet listeners = (HashSet)agentsToAgentListeners.get(agent);
        if(listeners == null)
            listeners = new HashSet();
        listeners.add(listener);
        agentsToAgentListeners.put(agent, listeners);
    }

    public void detachAgentListener(String agent, AgentListener listener)
    {
        if(!registeredAgents.contains(agent))
            return;
        HashSet listeners = (HashSet)agentsToAgentListeners.get(agent);
        if(listeners == null || !listeners.contains(agent))
        {
            return;
        } else
        {
            listeners.remove(listener);
            return;
        }
    }

    protected transient void notifyAgents(Percept percept, String agents[])
        throws EnvironmentInterfaceException
    {
        if(agents == null)
        {
            for(Iterator iterator = registeredAgents.iterator(); iterator.hasNext();)
            {
                String agent = (String)iterator.next();
                HashSet agentListeners = (HashSet)agentsToAgentListeners.get(agent);
                if(agentListeners != null)
                {
                    AgentListener listener;
                    for(Iterator iterator1 = agentListeners.iterator(); iterator1.hasNext(); listener.handlePercept(agent, percept))
                        listener = (AgentListener)iterator1.next();

                }
            }

            return;
        }
        String as[];
        int j = (as = agents).length;
        for(int i = 0; i < j; i++)
        {
            String agent = as[i];
            if(!registeredAgents.contains(agent))
                throw new EnvironmentInterfaceException((new StringBuilder("Agent ")).append(agent).append(" has not registered to the environment.").toString());
            HashSet agentListeners = (HashSet)agentsToAgentListeners.get(agent);
            if(agentListeners != null)
            {
                AgentListener listener;
                for(Iterator iterator2 = agentListeners.iterator(); iterator2.hasNext(); listener.handlePercept(agent, percept))
                    listener = (AgentListener)iterator2.next();

            }
        }

    }

    protected transient void notifyAgentsViaEntity(Percept percept, String pEntities[])
        throws EnvironmentInterfaceException
    {
        String as[];
        int k = (as = pEntities).length;
        for(int i = 0; i < k; i++)
        {
            String entity = as[i];
            if(!entities.contains(entity))
                throw new EnvironmentInterfaceException((new StringBuilder("entity \"")).append(entity).append("\" does not exist.").toString());
        }

        if(pEntities.length == 0)
        {
            for(Iterator iterator = entities.iterator(); iterator.hasNext();)
            {
                String entity = (String)iterator.next();
                for(Iterator iterator1 = agentsToEntities.entrySet().iterator(); iterator1.hasNext();)
                {
                    java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
                    if(((HashSet)entry.getValue()).contains(entity))
                        notifyAgents(percept, new String[] {
                            (String)entry.getKey()
                        });
                }

            }

        } else
        {
            String as1[];
            int l = (as1 = pEntities).length;
            for(int j = 0; j < l; j++)
            {
                String entity = as1[j];
                for(Iterator iterator2 = agentsToEntities.entrySet().iterator(); iterator2.hasNext();)
                {
                    java.util.Map.Entry entry = (java.util.Map.Entry)iterator2.next();
                    if(((HashSet)entry.getValue()).contains(entity))
                        notifyAgents(percept, new String[] {
                            (String)entry.getKey()
                        });
                }

            }

        }
    }

    protected void notifyFreeEntity(String entity, Collection agents)
    {
        EnvironmentListener listener;
        for(Iterator iterator = environmentListeners.iterator(); iterator.hasNext(); listener.handleFreeEntity(entity, agents))
            listener = (EnvironmentListener)iterator.next();

    }

    private void notifyIfFree(Set entities, List agents)
    {
        List free = getFreeEntities();
        for(Iterator iterator = entities.iterator(); iterator.hasNext();)
        {
            String en = (String)iterator.next();
            if(free.contains(en))
                notifyFreeEntity(en, agents);
        }

    }

    protected void notifyNewEntity(String entity)
    {
        EnvironmentListener listener;
        for(Iterator iterator = environmentListeners.iterator(); iterator.hasNext(); listener.handleNewEntity(entity))
            listener = (EnvironmentListener)iterator.next();

    }

    protected void notifyDeletedEntity(String entity, Collection agents)
    {
        EnvironmentListener listener;
        for(Iterator iterator = environmentListeners.iterator(); iterator.hasNext(); listener.handleDeletedEntity(entity, agents))
            listener = (EnvironmentListener)iterator.next();

    }

    public void registerAgent(String agent)
        throws AgentException
    {
        if(registeredAgents.contains(agent))
        {
            throw new AgentException((new StringBuilder("Agent ")).append(agent).append(" has already registered to the environment.").toString());
        } else
        {
            registeredAgents.add(agent);
            return;
        }
    }

    public void unregisterAgent(String agent)
        throws AgentException
    {
        if(!registeredAgents.contains(agent))
        {
            throw new AgentException((new StringBuilder("Agent ")).append(agent).append(" has not registered to the environment.").toString());
        } else
        {
            agentsToEntities.remove(agent);
            agentsToAgentListeners.remove(agent);
            registeredAgents.remove(agent);
            return;
        }
    }

    public LinkedList getAgents()
    {
        return (LinkedList)registeredAgents.clone();
    }

    public LinkedList getEntities()
    {
        return (LinkedList)entities.clone();
    }

    public void associateEntity(String agent, String entity)
        throws RelationException
    {
        if(!entities.contains(entity))
            throw new RelationException((new StringBuilder("Entity \"")).append(entity).append("\" does not exist!").toString());
        if(!registeredAgents.contains(agent))
            throw new RelationException((new StringBuilder("Agent \"")).append(agent).append("\" has not been registered!").toString());
        HashSet ens = (HashSet)agentsToEntities.get(agent);
        if(ens == null)
            ens = new HashSet();
        ens.add(entity);
        agentsToEntities.put(agent, ens);
    }

    public void freeEntity(String entity)
        throws RelationException, EntityException
    {
        if(!entities.contains(entity))
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" does not exist!").toString());
        LinkedList agents = new LinkedList();
        boolean associated = false;
        for(Iterator iterator = agentsToEntities.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String agent = (String)entry.getKey();
            HashSet ens = (HashSet)entry.getValue();
            if(ens.contains(entity))
            {
                ens.remove(entity);
                agentsToEntities.put(agent, ens);
                associated = true;
                if(!agents.contains(agent))
                    agents.add(agent);
                break;
            }
        }

        if(!associated)
        {
            throw new RelationException((new StringBuilder("Entity \"")).append(entity).append("\" has not been associated!").toString());
        } else
        {
            notifyFreeEntity(entity, agents);
            return;
        }
    }

    public void freeAgent(String agent)
        throws RelationException
    {
        if(!registeredAgents.contains(agent))
        {
            throw new RelationException((new StringBuilder("Agent \"")).append(agent).append("\" does not exist!").toString());
        } else
        {
            HashSet ens = (HashSet)agentsToEntities.get(agent);
            LinkedList agents = new LinkedList();
            agents.add(agent);
            notifyIfFree(ens, agents);
            agentsToEntities.remove(agent);
            return;
        }
    }

    public void freePair(String agent, String entity)
        throws RelationException
    {
        if(!registeredAgents.contains(agent))
            throw new RelationException((new StringBuilder("Agent \"")).append(agent).append("\" does not exist!").toString());
        if(!entities.contains(entity))
            throw new RelationException((new StringBuilder("Entity \"")).append(entity).append("\" does not exist!").toString());
        HashSet ens = (HashSet)agentsToEntities.get(agent);
        if(ens == null || !ens.contains(entity))
        {
            throw new RelationException((new StringBuilder("Agent \"")).append(agent).append(" is not associated with entity \"").append(entity).append("\"!").toString());
        } else
        {
            ens.remove(entity);
            agentsToEntities.put(agent, ens);
            LinkedList agents = new LinkedList();
            agents.add(agent);
            notifyIfFree(ens, agents);
            return;
        }
    }

    public HashSet getAssociatedEntities(String agent)
        throws AgentException
    {
        if(!registeredAgents.contains(agent))
            throw new AgentException((new StringBuilder("Agent \"")).append(agent).append("\" has not been registered.").toString());
        HashSet ret = (HashSet)agentsToEntities.get(agent);
        if(ret == null)
            ret = new HashSet();
        return ret;
    }

    public HashSet getAssociatedAgents(String entity)
        throws EntityException
    {
        if(!entities.contains(entity))
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" has not been registered.").toString());
        HashSet ret = new HashSet();
        for(Iterator iterator = agentsToEntities.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            if(((HashSet)entry.getValue()).contains(entity))
                ret.add((String)entry.getKey());
        }

        return ret;
    }

    public LinkedList getFreeEntities()
    {
        LinkedList free = getEntities();
        String agent;
        for(Iterator iterator = agentsToEntities.keySet().iterator(); iterator.hasNext(); free.removeAll((Collection)agentsToEntities.get(agent)))
            agent = (String)iterator.next();

        return free;
    }

    public final transient Map performAction(String agent, Action action, String entities[])
        throws ActException
    {
        if(!registeredAgents.contains(agent))
            throw new ActException(1);
        HashSet associatedEntities = (HashSet)agentsToEntities.get(agent);
        if(associatedEntities == null || associatedEntities.size() == 0)
            throw new ActException(2);
        HashSet targetEntities = null;
        if(entities.length == 0)
        {
            targetEntities = associatedEntities;
        } else
        {
            targetEntities = new HashSet();
            String as[];
            int l = (as = entities).length;
            for(int i = 0; i < l; i++)
            {
                String entity = as[i];
                if(!associatedEntities.contains(entity))
                    throw new ActException(3);
                targetEntities.add(entity);
            }

        }
        if(!isSupportedByEnvironment(action))
            throw new ActException(4);
        String as1[];
        int i1 = (as1 = entities).length;
        for(int j = 0; j < i1; j++)
        {
            String entity = as1[j];
            String type;
            try
            {
                type = getType(entity);
            }
            catch(EntityException e)
            {
                e.printStackTrace();
                continue;
            }
            if(!isSupportedByType(action, type))
                throw new ActException(5);
        }

        i1 = (as1 = entities).length;
        for(int k = 0; k < i1; k++)
        {
            String entity = as1[k];
            String type;
            try
            {
                type = getType(entity);
            }
            catch(EntityException e)
            {
                e.printStackTrace();
                continue;
            }
            if(!isSupportedByEntity(action, entity))
                throw new ActException(6);
        }

        Map ret = new HashMap();
        for(Iterator iterator = targetEntities.iterator(); iterator.hasNext();)
        {
            String entity = (String)iterator.next();
            try
            {
                Percept p = performEntityAction(entity, action);
                if(p != null)
                    ret.put(entity, p);
            }
            catch(Exception e)
            {
                if(!(e instanceof ActException))
                    throw new ActException(7, "failure performing action", e);
                else
                    throw (ActException)e;
            }
        }

        return ret;
    }

    public transient Map getAllPercepts(String agent, String entities[])
        throws PerceiveException, NoEnvironmentException
    {
        if(state != EnvironmentState.RUNNING)
            throw new PerceiveException("Environment does not run");
        if(!registeredAgents.contains(agent))
            throw new PerceiveException((new StringBuilder("Agent \"")).append(agent).append("\" is not registered.").toString());
        HashSet associatedEntities = (HashSet)agentsToEntities.get(agent);
        if(associatedEntities == null || associatedEntities.size() == 0)
            throw new PerceiveException((new StringBuilder("Agent \"")).append(agent).append("\" has no associated entities.").toString());
        Map ret = new HashMap();
        if(entities.length == 0)
        {
            String entity;
            LinkedList all;
            for(Iterator iterator = associatedEntities.iterator(); iterator.hasNext(); ret.put(entity, all))
            {
                entity = (String)iterator.next();
                all = getAllPerceptsFromEntity(entity);
                Percept p;
                for(Iterator iterator1 = all.iterator(); iterator1.hasNext(); p.setSource(entity))
                    p = (Percept)iterator1.next();

            }

        } else
        {
            String as[];
            int j = (as = entities).length;
            for(int i = 0; i < j; i++)
            {
                String entity = as[i];
                if(!associatedEntities.contains(entity))
                    throw new PerceiveException((new StringBuilder("Entity \"")).append(entity).append("\" has not been associated with the agent \"").append(agent).append("\".").toString());
                LinkedList all = getAllPerceptsFromEntity(entity);
                Percept p;
                for(Iterator iterator2 = all.iterator(); iterator2.hasNext(); p.setSource(entity))
                    p = (Percept)iterator2.next();

                ret.put(entity, all);
            }

        }
        return ret;
    }

    protected abstract LinkedList getAllPerceptsFromEntity(String s)
        throws PerceiveException, NoEnvironmentException;

    protected abstract boolean isSupportedByEnvironment(Action action);

    protected abstract boolean isSupportedByType(Action action, String s);

    protected abstract boolean isSupportedByEntity(Action action, String s);

    protected abstract Percept performEntityAction(String s, Action action)
        throws ActException;

    public String getType(String entity)
        throws EntityException
    {
        if(!entities.contains(entity))
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" does not exist!").toString());
        String type = (String)entitiesToTypes.get(entity);
        if(type == null)
            type = "unknown";
        return type;
    }

    protected void addEntity(String entity)
        throws EntityException
    {
        if(entities.contains(entity))
        {
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" does already exist").toString());
        } else
        {
            entities.add(entity);
            notifyNewEntity(entity);
            return;
        }
    }

    protected void addEntity(String entity, String type)
        throws EntityException
    {
        if(entities.contains(entity))
        {
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" does already exist").toString());
        } else
        {
            entities.add(entity);
            setType(entity, type);
            notifyNewEntity(entity);
            return;
        }
    }

    protected void deleteEntity(String entity)
        throws EntityException, RelationException
    {
        if(!entities.contains(entity))
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" does not exist!").toString());
        LinkedList agents = new LinkedList();
        for(Iterator iterator = agentsToEntities.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String agent = (String)entry.getKey();
            HashSet ens = (HashSet)entry.getValue();
            if(ens.contains(entity))
            {
                ens.remove(entity);
                agentsToEntities.put(agent, ens);
                if(!agents.contains(agent))
                    agents.add(agent);
                break;
            }
        }

        entities.remove(entity);
        if(entitiesToTypes.containsKey(entity))
            entitiesToTypes.remove(entity);
        notifyDeletedEntity(entity, agents);
    }

    public void setType(String entity, String type)
        throws EntityException
    {
        if(!entities.contains(entity))
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" does not exist!").toString());
        if(entitiesToTypes.get(entity) != null)
        {
            throw new EntityException((new StringBuilder("Entity \"")).append(entity).append("\" already has a type!").toString());
        } else
        {
            entitiesToTypes.put(entity, type);
            return;
        }
    }

    protected void setState(EnvironmentState state)
        throws ManagementException
    {
        if(!isStateTransitionValid(this.state, state))
            throw new ManagementException((new StringBuilder("Invalid state transition from ")).append(this.state.toString()).append(" to  ").append(state.toString()).toString());
        this.state = state;
        EnvironmentListener listener;
        for(Iterator iterator = environmentListeners.iterator(); iterator.hasNext(); listener.handleStateChange(state))
            listener = (EnvironmentListener)iterator.next();

    }

    public boolean isStateTransitionValid(EnvironmentState oldState, EnvironmentState newState)
    {
        if(oldState == EnvironmentState.INITIALIZING && newState == EnvironmentState.INITIALIZING)
            return true;
        if(oldState == EnvironmentState.INITIALIZING && newState == EnvironmentState.PAUSED)
            return true;
        if(oldState == EnvironmentState.INITIALIZING && newState == EnvironmentState.KILLED)
            return true;
        if(oldState == EnvironmentState.PAUSED && newState == EnvironmentState.RUNNING)
            return true;
        if(oldState == EnvironmentState.RUNNING && newState == EnvironmentState.PAUSED)
            return true;
        if(oldState == EnvironmentState.PAUSED && newState == EnvironmentState.KILLED)
            return true;
        return oldState == EnvironmentState.RUNNING && newState == EnvironmentState.KILLED;
    }

    public EnvironmentState getState()
    {
        return state;
    }

    public boolean isInitSupported()
    {
        return true;
    }

    public boolean isKillSupported()
    {
        return true;
    }

    public boolean isPauseSupported()
    {
        return true;
    }

    public boolean isStartSupported()
    {
        return true;
    }

    public void init(Map parameters)
        throws ManagementException
    {
        if(!isInitSupported())
        {
            throw new ManagementException("init is not supported");
        } else
        {
            setState(EnvironmentState.INITIALIZING);
            return;
        }
    }

    public void kill()
        throws ManagementException
    {
        if(!isKillSupported())
        {
            throw new ManagementException("kill is not supported");
        } else
        {
            setState(EnvironmentState.KILLED);
            return;
        }
    }

    public void pause()
        throws ManagementException
    {
        if(!isPauseSupported())
        {
            throw new ManagementException("pause is not supported");
        } else
        {
            setState(EnvironmentState.PAUSED);
            return;
        }
    }

    public void start()
        throws ManagementException
    {
        if(!isStartSupported())
        {
            throw new ManagementException("start is not supported");
        } else
        {
            setState(EnvironmentState.RUNNING);
            return;
        }
    }

    public String queryProperty(String property)
    {
        return null;
    }

    public String queryEntityProperty(String entity, String property)
    {
        return null;
    }

    public volatile Collection getEntities()
    {
        return getEntities();
    }

    public volatile Collection getAgents()
    {
        return getAgents();
    }

    public volatile Collection getAssociatedEntities(String s)
        throws AgentException
    {
        return getAssociatedEntities(s);
    }

    public volatile Collection getAssociatedAgents(String s)
        throws EntityException
    {
        return getAssociatedAgents(s);
    }

    public volatile Collection getFreeEntities()
    {
        return getFreeEntities();
    }

    private static final long serialVersionUID = 0x33c01081fbd8528eL;
    private LinkedList registeredAgents;
    private LinkedList entities;
    private ConcurrentHashMap agentsToEntities;
    private Vector environmentListeners;
    private ConcurrentHashMap agentsToAgentListeners;
    private HashMap entitiesToTypes;
    private EnvironmentState state;
}
