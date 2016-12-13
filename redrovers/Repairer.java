package redrovers;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import apltk.interpreter.data.Belief;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.Message;
import massim.javaagents.Agent;

/**
 * An agent that probes nodes that are in range.
 *
 * Probes the closest nodes first, recharges when it doesn't have enough energy
 * to probe.
 */
public class Repairer extends RedAgent
{
	private OtherAgent goalAgent;	
	private LinkedList<String> path;
	public Repairer(String name, String team)
	{
		super(name,team);
		goalAgent = null;
	}

	/* I think this will be helpful for refactoring this agent. Returns a path
	 * to the nearest damaged agent on our team satisfying a test. for example:
	 *
	 * pathToDamagedAgent((agent) -> agent.role != null && agent.role.equals("Explorer"))
	 * returns a path to the nearest damaged explorer on our team (if any)
	 */
	LinkedList<String> pathToDamagedAgent(Function<OtherAgent, Boolean> test)
	{
		Set<String> pos = new HashSet<String>();
		for (OtherAgent agent : agents.values())
		{
			if (!agent.team.equals(getTeam())) continue;
			if (agent.knownDamaged() && test.apply(agent))
				pos.add(agent.position);
		}

		if (pos.isEmpty()) return null;

		return graph.shortestPath(position, (id) -> pos.contains(id));
	}

	Action think()
	{
		if (wrongRole()) return skipAction();

		if(energy == 0){
			return rechargeAction();
		}

		if(goalAgent != null){
			System.out.println("my goal is this agent: " + goalAgent);
			checkGoal();
			
		}
		if(goalAgent != null && goalAgent.role != null){
			System.out.println("in goal method");
			int health = goalAgent.health;
			String pos = goalAgent.position;
			//doesn't seem useful to try ranged repair, would only repair 1 hp
			if(path == null){
				LinkedList<String> l = graph.explore(position);
				String n = l.removeFirst();
				return gotoGreedy(n);
			}
			if(path.size() == 0){
				if(energy < 3){
					return rechargeAction();
				}
				else {
					path = null;
					String name = goalAgent.name;
					goalAgent = null;
					return repairAction(name);
				}
			}
			else{
				path = graph.shortestPath(position, goalAgent.position);
				String next = path.removeFirst();
				int weight = graph.edgeWeight(position, next);
				if(energy < weight){
					path.addFirst(next);
					return rechargeAction();
				}
				return gotoAction(next);
			}
		}
		
		return findGoal();
	}
	
	void checkGoal(){
		int health = goalAgent.health;
		if(health == goalAgent.maxHealth){
			goalAgent = null;
			path = null;
		}
		else{
			path = graph.shortestPath(position, goalAgent.position);
		}
	}
	Action findGoal(){
		List<OtherAgent> disabled = new ArrayList<OtherAgent>();
		List<OtherAgent> damaged = new ArrayList<OtherAgent>();
		List<OtherAgent> priority = new ArrayList<OtherAgent>();
		for(OtherAgent agent : agents.values()){
			if(agent.role == null){
				continue;
			}
			if(agent.team.equals(this.getTeam())){
				if((agent.role.equals("Explorer") || agent.role.equals("Repairer")) && agent.health < agent.maxHealth){
					System.out.println("This agent needs to be repaired " + agent.name);
					priority.add(agent);
				}
				else if(agent.health != null && agent.health == 0){
					System.out.println("This agent needs to be repaired " + agent.name);
					disabled.add(agent);
				}
				else if(agent.knownDamaged()){
					System.out.println("This agent needs to be repaired " + agent.name);
					damaged.add(agent);
				}
			}
		}
		if(priority.size() != 0){
			LinkedList<String> closest = new LinkedList<String>();
			OtherAgent goal = null;
			for(OtherAgent agent : priority){
				LinkedList<String> temp = graph.shortestPath(position, agent.position);
				if(temp != null && closest.size() == 0){
					closest = graph.shortestPath(position, agent.position);
					goal = agent;
				}
				if(temp != null && temp.size() < closest.size()){
					closest = temp;
					goal = agent;
				}
			}
			path = closest;
			goalAgent = goal;
			System.out.println("my goal is this agent: " + goalAgent);
		}
		if(path == null && disabled.size() != 0){
			LinkedList<String> closest = new LinkedList<String>();
			OtherAgent goal = null;
			for(OtherAgent agent : disabled){
				LinkedList<String> temp = graph.shortestPath(position, agent.position);
				if(temp != null && closest.size() == 0){
					closest = graph.shortestPath(position, agent.position);
					goal = agent;
				}
				else if(temp != null && temp.size() < closest.size()){
					closest = temp;
					goal = agent;
				}
			}
			path = closest;
			goalAgent = goal;
			System.out.println("my goal is this agent: " + goalAgent);
		}
		if(path == null && damaged.size() != 0){
			LinkedList<String> closest = new LinkedList<String>();
			OtherAgent goal = null;
			for(OtherAgent agent : damaged){
				LinkedList<String> temp = graph.shortestPath(position, agent.position);
				if(temp != null && closest.size() == 0){
					closest = graph.shortestPath(position, agent.position);
					goal = agent;
				}
				if(temp != null && temp.size() < closest.size()){
					closest = temp;
					goal = agent;
				}
			}
			path = closest;
			goalAgent = goal;
			System.out.println("my goal is this agent: " + goalAgent);
		}
		if(goalAgent == null){
			path = graph.explore(position);
			goalAgent = null;
			if (path != null) System.out.println("path size is: " + path.size());
		}
		if(path != null && path.size() != 0){
			String next = path.removeFirst();
			int weight = graph.edgeWeight(position, next);
			if(energy < weight){
				path.addFirst(next);
				return rechargeAction();
			}
			if(path.size() == 0){
				path = null;
			}
			return gotoAction(next);
		}
		else if(goalAgent != null && path.size() == 0){
			if(energy < 3){
				return rechargeAction();
			}
			else {
				path = null;
				String name = goalAgent.name;
				goalAgent = null;
				return repairAction(name);
			}
		}
		else if(goalAgent == null && (path == null || path.size() == 0)){
			path = null;
			return surveyAction();
		}

		return skipAction();
	}
}
