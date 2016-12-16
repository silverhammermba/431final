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

		LinkedList<String> path;

		// if disabled, try to get to a repairer
		if (health == 0)
		{
			OtherAgent agent = graph.nearestAgent(this, (ag) -> getTeam().equals(ag.team) && "Repairer".equals(ag.role));

			if (agent == null)
				return rechargeAction();

			path = graph.shortestPath(position, agent.position);

			if (path != null && path.size() > 1)
				return gotoGreedy(path.pop());

			return rechargeAction();
		}

		// get the other Inspector's goal
		String tempGoal = null;
		for (OtherAgent agent : agents.values())
		{
			if (getTeam().equals(agent.team) && role.equals(agent.role))
			{
				tempGoal = agent.goal;
				break;
			}
		}
		final String otherGoal = tempGoal;

		// find an agent to inspect whose role we don't know
		OtherAgent agent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && a.role == null && !a.position.equals(otherGoal) && (a.positionAge == 0 || !position.equals(a.position)));

		if (agent != null)
		{
			path = graph.shortestPath(position, agent.position);

			if (path.isEmpty()) return inspectGreedy();

			setGoal(agent.position);
			return gotoGreedy(path.pop());
		}

		// try to inspect an agent with outdated health info
		agent = graph.nearestAgent(this, (a) -> !getTeam().equals(a.team) && (a.healthAge == null || a.healthAge > 10) && !a.position.equals(otherGoal) && (a.positionAge == 0 || !position.equals(a.position)));

		if (agent != null)
		{
			path = graph.shortestPath(position, agent.position);
			if (path.isEmpty()) return inspectGreedy();

			setGoal(agent.position);
			return gotoGreedy(path.pop());
		}

		// try to explore the graph
		Set<String> others = new HashSet<String>();
		for (OtherAgent a : agents.values())
		{
			if (a.goal != null) others.add(a.goal);
		}
		path = graph.shortestPath(position, (id) -> (graph.unknownEdges(id) || graph.unsurveyedEdges(id)) && !others.contains(id));

		if (path != null)
		{
			if (path.size() == 0)
			{
				setGoal(null);
				return surveyGreedy();
			}

			setGoal(path.get(path.size() - 1));
			return gotoGreedy(path.pop());
		}

		// try to hold territory
		path = graph.territory(this);

		if (path == null)
		{
			List<String> nodes = graph.nodesAtRange(position, 1);
			String n = nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size()));
			return gotoGreedy(n);
		}

		if (path.size() == 0) return rechargeAction();

		return gotoGreedy(path.pop());
	}
}
