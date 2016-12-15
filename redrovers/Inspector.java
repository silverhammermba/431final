package redrovers;
import eis.iilang.Action;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import massim.javaagents.Agent;

public class Inspector extends RedAgent
{
	LinkedList<String> seekPath;

	public Inspector(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (wrongRole()) return skipAction();

		if (health == 0)
		{
			OtherAgent agent = graph.nearestAgent(this, (ag) -> getTeam().equals(ag.team) && "Repairer".equals(ag.role));

			if (agent == null)
				return rechargeAction();

			LinkedList<String> path = graph.shortestPath(position, agent.position);

			if (path != null && path.size() > 1)
				return gotoGreedy(path.pop());

			return rechargeAction();
		}

		// find enemy agents to inspect
		int known = 0; // keep track of total inspected
		Set<String> targets = new HashSet<String>();
		for (OtherAgent agent : agents.values())
		{
			// same team, don't care
			if (agent.team.equals(getTeam())) continue;
			// role known, don't care
			if (agent.role != null)
			{
				++known;
				continue;
			}
			// position unknown, don't care (how do we find them?)
			if (agent.position == null) continue;

			targets.add(agent.position);
			// if they are right here, it's simple
			if (position.equals(agent.position)) return inspectGreedy();
		}

		// if we've inspected every member of the enemy team
		if (known == teamSize)
		{
			OtherAgent agent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && a.healthAge > 10);

			if (agent != null)
			{
				LinkedList<String> path = graph.shortestPath(position, agent.position);
			}

			// TODO maybe keep inspecting to find out health? secure territory?
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

		LinkedList<String> path;

		if (targets.isEmpty())
		{
			// TODO really should be hunting the map for agents, but this is close
			path = graph.explore(position);

			if (path == null)
			{
				// TODO okay, here we really should be hunting
				List<String> nodes = graph.nodesAtRange(position, 1);
				return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
			}
			if (path.isEmpty()) return surveyGreedy();
			return gotoGreedy(path.pop());
		}

		path = graph.shortestPath(position, (id) -> targets.contains(id));

		// no path to any un-inspected agent
		if (path == null)
		{
			// TODO we really should be hunting
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
		if (path.isEmpty())
		{
			System.err.println("Somehow already at un-inspected agent!?");
			return inspectGreedy();
		}
		return gotoGreedy(path.pop());
	}
}
