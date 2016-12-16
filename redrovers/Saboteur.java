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

/**
 * An agent that attacks enemy vehicles.
 *
 * <p>Its subsumption rules are:
 * <ol>
 * <li>Get to a repairer if disabled</li>
 * <li>Probe current vertex if unprobed</li>
 * <li>Go to nearest unprobed vertex</li>
 * <li>Explore the graph</li>
 * <li>Help secure territory</li>
 * </ol>
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
			OtherAgent a = graph.nearestAgent(this, (agent) -> agent.team.equals(this.getTeam()) && agent.role.equals("Repairer"));
			LinkedList<String> p = graph.shortestPath(position,  a.position);
			if(p != null && (p.size() == 1 || p.size() == 0)){
				return skipAction();
			}
			else if(p == null){
				System.out.println("Cant get to repairer");
				p = graph.explore(position);
				if(p != null){
					String myGoal = p.peekLast();
					ArrayList<String> goals = new ArrayList<String>();
					for(OtherAgent agent: agents.values()){
						if(agent.team.equals(this.getTeam())){
							if(agent.goal != null){
								goals.add(agent.goal);
							}
						}
					}
					while(goals.contains(myGoal)){
						p = graph.shortestPath(position, (id) -> (graph.unknownEdges(id) || graph.unsurveyedEdges(id)) && !goals.contains(id));
						if(path != null){
							myGoal = p.peekLast();
						}
						else{
							myGoal = null;
						};
					}
					setGoal(myGoal);
				}
			}
			if(p == null || p.size() == 0){
				return skipAction();
			}
			System.out.println("going to repairer");
			return gotoGreedy(p.removeFirst());
		}
		if(goalAgent != null){
			checkGoal();
		}
		if(goalAgent != null){
			String pos = goalAgent.position;
			//doesn't seem useful to try ranged repair, would only repair 1 hp
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
						if(path != null){
							myGoal = l.peekLast();
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

	/**
	 * Update our current goal
	 */
	void checkGoal()
	{
		if (goalAgent.health != null && goalAgent.health == 0){
			goalAgent = null;
			setGoal(null);
		}
		else
		{
			path = graph.shortestPath(position, goalAgent.position);
			setGoal(goalAgent.position);
		}
	}

	/**
	 * Get a new agent to go to or path to follow
	 *
	 * @return The next action to perform toward the new goal
	 */
	Action findGoal(){
		goalAgent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && a.position.equals(position) && a.positionAge == 0 && (a.health == null || a.health != 0));
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && (a.positionAge == 0 || !a.position.equals(position)) && (a.health == null || a.health != 0) && ("Repairer".equals(a.role) || "Explorer".equals(a.role)));
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && (a.positionAge == 0 || !a.position.equals(position)) && a.health != null && a.health != 0 && a.healthAge != null && a.healthAge < 10);
		if (goalAgent == null) goalAgent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && (a.positionAge == 0 || !a.position.equals(position)) && (a.healthAge == null || a.healthAge >= 10));

		if (goalAgent != null)
		{
			path = graph.shortestPath(position, goalAgent.position);

			if (path.size() == 0)
			{
				String name = goalAgent.name;
				goalAgent = null;
				path = null;
				return attackGreedy(name);
			}
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
			if(path == null){

				LinkedList<String> n = graph.territory(this);
				if(n == null){
					List<String> nodes = graph.nodesAtRange(position, 1);
					return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
				}
				else if(n.size() == 0){
					return parryGreedy();
				}
				return gotoGreedy(n.removeFirst());

			}
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

		else if(goalAgent == null && path.size() == 0){
			path = null;
			setGoal(null);
			return surveyAction();
		}
		goalAgent = null;
		LinkedList<String> n = graph.territory(this);
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
