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
		if (wrongRole()) return skipAction();

		if(energy == 0){
			return rechargeAction();
		}
		if(health == 0){
			LinkedList<String> p = pathToAgent((agent) -> agent.team.equals(this.getTeam()) && agent.role.equals("Repairer"));
			
			if(path != null && path.size() == 0){
				return skipAction();
			}
			else if(path == null){
				System.out.println("Cant get to repairer");
				p = graph.explore(position);
			}
			if(p == null || p.size() == 0){
				return MarsUtil.skipAction();
			}
			System.out.println("going to repairer");
			return gotoGreedy(p.removeFirst());
		}
		if(goalAgent != null){
			checkGoal();
		}
		if(goalAgent != null){
			int health = goalAgent.health;
			String pos = goalAgent.position;
			//doesn't seem useful to try ranged repair, would only repair 1 hp
			if(path == null){
				goalAgent = null;
				LinkedList<String> l = graph.explore(position);
				if(l != null && l.size() != 0){
					String n = l.removeFirst();
					
					return gotoGreedy(n);
				}
				else{
					List<String> nodes = graph.nodesAtRange(position, 1);
					return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
				}
			}
			if(path.size() == 0){
				if(energy < 2){
					return rechargeAction();
				}
				else {
					path = null;
					String name = goalAgent.name;
					goalAgent = null;
					return attackAction(name);
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
		List<OtherAgent> here = new ArrayList<OtherAgent>();
		for(OtherAgent agent : agents.values()){
			if(!agent.team.equals(this.getTeam())){
				if(agent.position != null && agent.position.equals(position)){
					Action last = null;
					if(prevActions.size() != 0){
						last = prevActions.get(prevActions.size() - 1);
					}
					if(agent.health != null && agent.health != 0){
						here.add(agent);
					}
					else if(agent.health == null && last != null && last.getName() != "attack"){
						here.add(agent);
					}
				}
			}
			if(agent.role != null && !agent.team.equals(this.getTeam())){
				
				if(agent.role.equals("Repairer") || agent.role.equals("Explorer")){
					if(agent.health != null && agent.health != 0){
						priority.add(agent);
					}
				}
				else if(agent.health == agent.maxHealth){
					healthy.add(agent);
				}
				else if(agent.health < agent.maxHealth){
					damaged.add(agent);
				}
			}
		}
		if(here.size() != 0){
			return attackGreedy(here.get(0).name);
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
		}
		if(goalAgent == null){
			path = graph.explore(position);
			goalAgent = null;
			// TODO path might be null here
			if(path == null){
				return MarsUtil.skipAction();
			}
			System.out.println("path size is: " + path.size());
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
			for(OtherAgent agent: agents.values()){
				if(agent.team.equals(this.getTeam())){
					Action doing = agent.nextAction;
					if(doing.getName().equals("goto")){
						String node = doing.getParameters().get(0).toString();
						if(node.equals(next)&& agent.name.compareTo(this.getName()) < 0){
							List<String> nodes = graph.nodesAtRange(position, 1);
							return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
						}
					}
				}
			}
			return gotoAction(next);
		}
		else if(goalAgent != null && path.size() == 0 && goalAgent.health != 0){
			if(energy < 2){
				return rechargeAction();
			}
			else {
				path = null;
				System.out.println("Attacking " + goalAgent + " with health of " + goalAgent.health);
				String name = goalAgent.name;
				goalAgent = null;
				
				return attackAction(name);
			}
		}
		else if(goalAgent == null && path.size() == 0){
			path = null;
			return surveyAction();
		}

		LinkedList<String> n = graph.territory(this.position, this);
		if(n == null){
			List<String> nodes = graph.nodesAtRange(position, 1);
			return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
		}
		else if(n.size() == 0){
			return MarsUtil.rechargeAction();
		}
		return gotoGreedy(n.removeFirst());
	}
}
