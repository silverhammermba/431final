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
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import apltk.interpreter.data.Belief;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.Message;
import massim.javaagents.Agent;

public class Sentinel extends RedAgent
{
	private OtherAgent goalAgent;	
	private LinkedList<String> path;
	
	public Sentinel(String name, String team)
	{
		super(name,team);
		goalAgent = null;
	}

	Action think()
	{
		if (wrongRole()) return skipAction();
		
		// Agent recharges if its energy drops to zero
		if(energy == 0)
		{
			return rechargeAction();
		}
		
		/** 
		  * If agent's health drops to zero
		  * Look for the nearest repairer	
		  * Return the shortest path to the Repairer
		  * Get Repaired
		  */
		if(health == 0)
		{
			OtherAgent agent = graph.nearestAgent(this, (ag) -> getTeam().equals(ag.team) && "Repairer".equals(ag.role));

			if (agent == null)
				return rechargeAction();

			LinkedList<String> path = graph.shortestPath(position, agent.position);

			if (path != null && path.size() > 1)
				return gotoGreedy(path.pop());

			return rechargeAction();
		}
					
		// Survey the adjacent edges
		if (graph.unsurveyedEdges(position))
		{
			return surveyGreedy();
		}
		
		// Agent paries an attack whenever the enemy's Saboteur launches an attack
		OtherAgent agent = graph.nearestAgent(this, (ag) -> !getTeam().equals(ag.team) && "Saboteur".equals(ag.role));
		if (agent!=null)
			path = graph.shortestPath(position, agent.position);

		if (path.size() <= 1)
		{
			// Explore the graph and goto the unsurveyed edges 
			LinkedList<String> path = graph.explore(position);
			if (path != null)
			{
			return gotoGreedy(path.pop());
			}
		}
		
		if(goalAgent != null){
			System.out.println("my goal is this agent: " + goalAgent);
			checkGoal();
			
		}
		
		if(goalAgent != null && goalAgent.role != null){
			System.out.println("In Goal Method");
			int health = goalAgent.health;
			String pos = goalAgent.position;
			
			if (path == null)
			{
				goalAgent = null;
				/*LinkedList<String> l = graph.explore(position);
				String n = l.removeFirst();*/
				
				return rechargeAction();
			}
			
			// TODO path might be null here
			if(path.size() == 0){
				if(energy < 1){
					return rechargeAction();
				}
				
			}
			/*else{
				path = graph.shortestPath(position, goalAgent.position);
				String next = path.removeFirst();
				int weight = graph.edgeWeight(position, next);
				return gotoGreedy(next);
				if(energy < weight){
					path.addFirst(next);
					return rechargeAction();
				}
				return gotoAction(next);
			}*/
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
			if(energy < 1){
				return rechargeAction();
			}
			/*else if(path == null){
			goalAgent = null;
			List<String> nodes = graph.nodesAtRange(position, 1);
			return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
			
		}*/
		else if(goalAgent == null && (path == null || path.size() == 0)){
			path = null;
			return surveyAction();
		}

			LinkedList<String> n = graph.territory(this.position, this);
			if(n == null){
				List<String> nodes = graph.nodesAtRange(position, 1);
				return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
			}
			else if(n.size() == 0){
				return rechargeAction();
			}
			return gotoGreedy(n.removeFirst());
	}
		LinkedList<String> n = graph.territory(this.position, this);
		if(n == null){
			List<String> nodes = graph.nodesAtRange(position, 1);
			return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
		}
		else if(n.size() == 0){
			return rechargeAction();
		}
		return gotoGreedy(n.removeFirst());
}
}
