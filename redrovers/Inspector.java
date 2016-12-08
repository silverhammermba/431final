package redrovers;
import eis.iilang.Action;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class Inspector extends RedAgent
{
	public Inspector(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (wrongRole()) return MarsUtil.skipAction();

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
			// TODO maybe keep inspecting to find out health? secure territory?
			return MarsUtil.skipAction();
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
			List<String> nodes = graph.nodesAtRange(position, 1);
			return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
		}
		if (path.isEmpty())
		{
			System.err.println("Somehow already at un-inspected agent!?");
			return inspectGreedy();
		}
		return gotoGreedy(path.pop());
	}
}
