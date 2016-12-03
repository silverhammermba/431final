package redrovers;
import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import java.util.ArrayList;
import java.util.List;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class RedAgent extends Agent
{
	Graph graph;
	String position;
	int energy;
	boolean fuckit;

	public RedAgent(String name, String team)
	{
		super(name, team);
		graph = new Graph();
		fuckit = true;
	}

	@Override
	public void handlePercept(Percept percept)
	{
		// convert percept parameters to strings
		List<String> params = new ArrayList<String>();
		for (Parameter p : percept.getParameters())
			params.add(p.toProlog());

		switch (percept.getName())
		{
			case "position":
				position = params.get(0);
				graph.visit(position);
				break;
			case "energy":
				energy = Integer.parseInt(params.get(0));
				break;
			case "edges":
				graph.total_edges = Integer.parseInt(params.get(0));
				break;
			case "vertices":
				graph.total_verts = Integer.parseInt(params.get(0));
				break;
			case "visibleEdge":
				graph.addEdge(params.get(0), params.get(1));
				break;
			case "surveyedEdge":
				graph.addEdge(params.get(0), params.get(1), Integer.parseInt(params.get(2)));
				break;
			default:
				System.err.println(getName() + " can't handle percept " + percept);
		}
	}

	@Override
	public Action step()
	{
		for (Percept percept: getAllPercepts()) handlePercept(percept);

		if (position == null) return MarsUtil.skipAction();

		// if we have unsurveyed edges next to us, survey them
		if (graph.unknownNearby(position))
		{
			if (energy == 0) return MarsUtil.rechargeAction();
			return MarsUtil.surveyAction();
		}

		String dest = graph.explore(position);

		if (dest == null)
		{
			System.err.println(getName() + " is done exploring");
			return MarsUtil.skipAction();
		}
		else
		{
			if (graph.edgeWeight(position, dest) > energy)
				return MarsUtil.rechargeAction();
			return MarsUtil.gotoAction(dest);
		}
	}
}
