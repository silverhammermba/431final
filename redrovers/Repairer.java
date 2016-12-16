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
				if(l != null){
					String myGoal = l.peekLast();
					ArrayList<String> goals = new ArrayList<String>();
					for(OtherAgent agent: agents.values()){
						if(agent.team.equals(this.getTeam())){
							if(agent.goal != null){
								goals.add(agent.goal);
							}
						}
					}
					while(goals.contains(myGoal)){
						l = graph.shortestPath(position, (id) -> (graph.unknownEdges(id) || graph.unsurveyedEdges(id)) && !goals.contains(id));
						if(l != null){
							myGoal = path.peekLast();
						}
						else{
							myGoal = null;
						}
					}
					setGoal(myGoal);
				}
				
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
					this.setGoal(null);
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
							String node = "" + doing.getParameters().get(0);
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
			this.setGoal(null);
		}
		else{
			this.setGoal(goalAgent.position);
			path = graph.shortestPath(position, goalAgent.position);
		}
	}

	Action findGoal()
	{
		String tempGoal = null;
		for (OtherAgent agent: agents.values())
		{
			if(agent.team.equals(getTeam()) && "Repairer".equals(agent.role))
			{
				tempGoal = agent.goal;
				break;
			}
		}
		final String otherGoal = tempGoal;

		goalAgent = graph.nearestAgent(this, (agent) -> getTeam().equals(agent.team) && !agent.position.equals(otherGoal) && agent.health != null && agent.health == 0 && ("Explorer".equals(agent.role) || "Repairer".equals(agent.role) || "Saboteur".equals(agent.role)));
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (agent) -> getTeam().equals(agent.team) && !agent.position.equals(otherGoal) && agent.health != null && agent.health == 0);
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (agent) -> getTeam().equals(agent.team) && !agent.position.equals(otherGoal) && agent.knownDamaged());

		if (goalAgent != null){
			this.setGoal(goalAgent.position);
			path = graph.shortestPath(position, goalAgent.position);
		}
		else
		{
			path = graph.explore(position);
			if(path != null){
				String myGoal = path.peekLast();
				ArrayList<String> goals = new ArrayList<String>();
				for(OtherAgent agent: agents.values()){
					if(agent.team.equals(this.getTeam())){
						if(agent.goal != null){
							goals.add(agent.goal);
						}
					}
				}
				while(goals.contains(myGoal)){
					path = graph.shortestPath(position, (id) -> (graph.unknownEdges(id) || graph.unsurveyedEdges(id)) && !goals.contains(id));
					if(path != null){
						myGoal = path.peekLast();
					}
					else{
						myGoal = null;
					}
				}
				setGoal(myGoal);
			}
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
						String node = "" + doing.getParameters().get(0);
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
				this.setGoal(null);
				return repairAction(name);
			}
		}
		else if(path == null){
			goalAgent = null;
			LinkedList<String> n = graph.territory(this);
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
			setGoal(null);
			return surveyAction();
		}

		LinkedList<String> n = graph.territory(this);
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
