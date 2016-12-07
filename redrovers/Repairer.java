package redrovers;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public class Repairer extends RedAgent
{
	private OtherAgent goalAgent;	
	private LinkedList<String> path;
	public Repairer(String name, String team)
	{
		super(name,team);
		goalAgent = null;
	}

	Action think()
	{
		if (!role.equals("Repairer"))
		{
			System.err.println("wrong class for agent!");
			return MarsUtil.skipAction();
		}
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
			if(pos == position){
				if(energy < 3){
					return MarsUtil.rechargeAction();
				}
				else {
					return MarsUtil.repairAction(goalAgent.name);
				}
			}
			else{
				//return graph.goto(goalAgent.position);
				return MarsUtil.skipAction();
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
				else if(agent.health == 0){
					System.out.println("This agent needs to be repaired " + agent.name);
					disabled.add(agent);
				}
				else if(agent.health < agent.maxHealth){
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
		else{
			path = graph.explore(position);
			goalAgent = null;
		}
		if(path != null && path.size() != 0){
			String next = path.removeFirst();
			int weight = graph.edgeWeight(position, next);
			if(energy < weight){
				path.addFirst(next);
				return MarsUtil.rechargeAction();
			}
			return MarsUtil.gotoAction(next);
		}
		else if(goalAgent != null && path.size() == 0){
			if(energy < 3){
				return MarsUtil.rechargeAction();
			}
			else {
				return MarsUtil.repairAction(goalAgent.name);
			}
		}
		
		return MarsUtil.surveyAction();
	}
}