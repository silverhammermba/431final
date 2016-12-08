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
import massim.javaagents.agents.MarsUtil;

/**
 * An agent that probes nodes that are in range.
 *
 * Probes the closest nodes first, recharges when it doesn't have enough energy
 * to probe.
 */
public class Saboteur extends RedAgent
{
	private OtherAgent goalAgent;
	private LinkedList<String> path;
	public Saboteur(String name, String team)
	{
		super(name,team);
		goalAgent = null;
	}

	/* I think this will be helpful for refactoring this agent. for example:
	 *
	 * pathToDamagedAgent((agent) -> agent.role != null && agent.role.equals("Explorer"))
	 * returns a path to the nearest damaged explorer on our team (if any)
	 */
	LinkedList<String> pathToAgent(Function<OtherAgent, Boolean> test)
	{
		Set<String> pos = new HashSet<String>();
		for (OtherAgent agent : agents.values())
		{
			if (agent.team.equals(getTeam())) continue;
			if (agent.knownDamaged() && test.apply(agent))
				pos.add(agent.position);
		}

		if (pos.isEmpty()) return null;

		return graph.shortestPath(position, (id) -> pos.contains(id));
	}
	
	Action think()
	{
		if (wrongRole()) return MarsUtil.skipAction();

		if(energy == 0){
			return MarsUtil.rechargeAction();
		}

		if(goalAgent != null){
			System.out.println("my goal is this agent: " + goalAgent);
			checkGoal();
		}
		if(goalAgent != null){
			int health = goalAgent.health;
			String pos = goalAgent.position;
			//doesn't seem useful to try ranged repair, would only repair 1 hp
			if(path.size() == 0){
				if(energy < 2){
					return MarsUtil.rechargeAction();
				}
				else {
					path = null;
					String name = goalAgent.name;
					goalAgent = null;
					return MarsUtil.attackAction(name);
				}
			}
			else{
				path = graph.shortestPath(position, goalAgent.position);
				String next = path.removeFirst();
				int weight = graph.edgeWeight(position, next);
				if(energy < weight){
					path.addFirst(next);
					return MarsUtil.rechargeAction();
				}
				return MarsUtil.gotoAction(next);
			}
		}
		
		return findGoal();
	}
	
	void checkGoal(){
		int health = goalAgent.health;
		if(health == 0){
			goalAgent = null;
		}
		else{
			path = graph.shortestPath(position, goalAgent.position);
		}
	}
	Action findGoal(){
		List<OtherAgent> healthy = new ArrayList<OtherAgent>();
		List<OtherAgent> damaged = new ArrayList<OtherAgent>();
		List<OtherAgent> priority = new ArrayList<OtherAgent>();
		for(OtherAgent agent : agents.values()){
			if(agent.role != null && !agent.team.equals(this.getTeam())){
				if(agent.role.equals("Repairer") || agent.role.equals("Explorer")){
					priority.add(agent);
				}
				else if(agent.health == agent.maxHealth){
					healthy.add(agent);
				}
				else if(agent.health < agent.maxHealth){
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
		if(path == null && damaged.size() != 0){
			LinkedList<String> closest = new LinkedList<String>();
			OtherAgent goal = null;
			for(OtherAgent agent : damaged){
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
		if(path == null && healthy.size() != 0){
			LinkedList<String> closest = new LinkedList<String>();
			OtherAgent goal = null;
			for(OtherAgent agent : healthy){
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
			System.out.println("path size is: " + path.size());
		}
		if(path != null && path.size() != 0){
			String next = path.removeFirst();
			int weight = graph.edgeWeight(position, next);
			if(energy < weight){
				path.addFirst(next);
				return MarsUtil.rechargeAction();
			}
			if(path.size() == 0){
				path = null;
			}
			return MarsUtil.gotoAction(next);
		}
		else if(goalAgent != null && path.size() == 0){
			if(energy < 2){
				return MarsUtil.rechargeAction();
			}
			else {
				path = null;
				String name = goalAgent.name;
				goalAgent = null;
				return MarsUtil.attackAction(name);
			}
		}
		else if(goalAgent == null && path.size() == 0){
			path = null;
			return MarsUtil.surveyAction();
		}

		return MarsUtil.skipAction();
	}
}
