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
import java.util.concurrent.ThreadLocalRandom;
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

	Action think()
	{
		if (wrongRole()) return skipAction();

		if(energy == 0){
			return rechargeAction();
		}

		if(goalAgent != null){
			checkGoal();
		}
		if(goalAgent != null && goalAgent.role != null){
			int health = goalAgent.health;
			String pos = goalAgent.position;
			//if the goal is not yet reachable, explore
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
			//if already on the node with the damaged agent
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
			else{ //if have a goal agent but not reached yet
				path = graph.shortestPath(position, goalAgent.position);
				String next = path.removeFirst();
				int weight = graph.edgeWeight(position, next);
				for(OtherAgent agent: agents.values()){
					if(agent.team.equals(this.getTeam()) && agent.role.equals("Repairer")){
						Action doing = agent.next(step);
						if(doing != null && doing.getName().equals("goto")){
							String node = doing.getParameters().get(0).toString();
							if(node.equals(next) && agent.name.compareTo(this.getName()) < 0){
								List<String> nodes = graph.nodesAtRange(position, 1);
								return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
							}
						}
					}
				}
				return gotoGreedy(next);
			}
		}

		return findGoal();
	}

	void checkGoal(){
		int health = goalAgent.health;
		//check if already repaired
		if(health == goalAgent.maxHealth){
			goalAgent = null;
			path = null;
		}
		else{
			path = graph.shortestPath(position, goalAgent.position);
		}
	}
	
	
	OtherAgent getRepairer(){
		for(OtherAgent agent: agents.values()){
			if(agent.team.equals(this.getTeam()) && agent.role != null && agent.role.equals("Repairer")){
				return agent;
			}
		}
		return null;
	}
	Action findGoal(){
		OtherAgent repairer = getRepairer();
		goalAgent = graph.nearestAgent(this, (agent) -> getTeam().equals(agent.team) && repairer != null && ((repairer.goal != null && repairer.goal!= agent.position ) || repairer.goal == null) && agent.health != null && agent.health == 0 && ("Explorer".equals(agent.role) || "Repairer".equals(agent.role) || "Saboteur".equals(agent.role)));
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (agent) -> getTeam().equals(agent.team) && repairer != null && ((repairer.goal != null && repairer.goal!= agent.position ) || repairer.goal == null) && agent.health != null && agent.health == 0);
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (agent) -> getTeam().equals(agent.team) && repairer != null && ((repairer.goal != null && repairer.goal!= agent.position ) || repairer.goal == null) && agent.knownDamaged());

		if (goalAgent != null){
			setGoal(goalAgent.position);
			path = graph.shortestPath(position, goalAgent.position);
		}
		else
		{
			path = graph.explore(position);
			goalAgent = null;
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
					Action doing = agent.next(step);
					if(doing != null && doing.getName().equals("goto")){
						String node = doing.getParameters().get(0).toString();
						if(node.equals(next) && agent.name.compareTo(this.getName()) < 0){
							List<String> nodes = graph.nodesAtRange(position, 1);
							return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
						}
					}
				}
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
		else if(path == null){
			goalAgent = null;
			LinkedList<String> n = graph.territory(this.position, this);
			if(n == null){
				List<String> nodes = graph.nodesAtRange(position, 1);
				return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
			}
			else if(n.size() == 0){
				return parryAction();
			}
			return gotoGreedy(n.removeFirst());
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
			return parryAction();
		}
		return gotoGreedy(n.removeFirst());
	}
}
