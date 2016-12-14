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
		if (wrongRole()) return skipAction();

		if (health == 0)
		{
			for (OtherAgent agent : agents.values())
			{
				Set<String> reps = new HashSet<String>();
				if (getTeam().equals(agent.team) && agent.role != null && agent.position != null && agent.role.equals("Repairer"))
					reps.add(agent.position);

				if (!reps.isEmpty())
				{
					LinkedList<String> path = graph.shortestPath(position, (id) -> reps.contains(id));

					if (path == null || path.size() <= 1)
						return rechargeAction();
					else
						return gotoGreedy(path.pop());
				}

				// TODO can probably do something smarter, like hold territory?
				return rechargeAction();
			}
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
			// TODO maybe keep inspecting to find out health? secure territory?
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
				return MarsUtil.rechargeAction();
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
