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

		Function<OtherAgent, Boolean> sabCheck = (ag) -> !getTeam().equals(ag.team) && (ag.health == null || ag.health > 0) && (ag.positionAge == 0 || !ag.position.equals(position));
		int numsab = 0;
		for (OtherAgent agent : agents.values())
		{
			if (!getTeam().equals(agent.team) && "Saboteur".equals(agent.role))
			{
				if (++numsab == 2)
				{
					sabCheck = (ag) -> !getTeam().equals(ag.team) && "Saboteur".equals(ag.role) && (ag.health == null || ag.health > 0) && (ag.positionAge == 0 || !ag.position.equals(position));
					break;
				}
			}
		}

		/*
		 * Agent should parry an attack whenever the enemy's Saboteur launches an attack
		 * Whenever the saboteur is in close proximity to the Sentinel
		 * Better approach is to move to a new location that does not contain any nearby enemy's saboteurs
		 */
		OtherAgent agent = graph.nearestAgent(this, sabCheck);
		if (agent!=null)
		{
			path = graph.shortestPath(position, agent.position);

			if (path.size() <= 1)
			{
				Set<String> saboteurPositions = new HashSet<String>();
				for(OtherAgent ag : agents.values())
				{
					if (sabCheck.apply(ag))
					{
						saboteurPositions.add(agent.position);
					}
				}
				LinkedList<String> p1 = graph.shortestPath(position, (id) -> !id.equals(position) && !saboteurPositions.contains(id));

				if (p1 == null)
				{
					List<String> nodes = graph.nodesAtRange(position, 1);
					String n = nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size()));
					return gotoGreedy(n);
				}

				return gotoGreedy(p1.pop());
			}
		}

		// Survey the adjacent edges
		if (graph.unsurveyedEdges(position))
		{
			return surveyGreedy();
		}

		// Explore the graph and goto the unsurveyed edges
		LinkedList<String> path = graph.explore(position);
		if (path != null)
		{
			return gotoGreedy(path.pop());
		}
		return skipAction();
	}
}
