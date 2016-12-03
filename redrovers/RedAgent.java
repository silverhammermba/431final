package redrovers;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class RedAgent extends Agent
{
	// member variables for storing percept information
	String id; // agent name?
	String role; // agent role
	int energy;
	int maxEnergy;
	int maxEnergyDisabled; // max energy when disabled
	int health;
	int maxHealth;
	int visRange;
	int strength;
	String position; // vertex id
	List<Action> prevActions; // previous actions (results added as params)
	Graph graph;
	/* Graph tracks the following percepts:
	 *  surveyedEdge(id, id, num)
	 *  edges(num)
	 *  vertices(num)
	 *  visibleEdge(id, id)
	 *  visibleVertex(id)
	 *  probedVertex(id, num)
	 */
	int money;
	int score; // total score
	int lastStepScore; // score last step
	int zoneScore; // this vehicle's zone's score
	int zonesScore; // score from team's zones
	int step;
	int steps;

	Action lastAction; // temp variable for populating prevActions

	/* percepts not currently stored:
	 *  achievement(id)
	 *  bye
	 *  deadline(num)
	 *  ranking(num)
	 *  simEnd
	 *  simStart
	 *  requestAction
	 *  timestamp(num)
	 *  visibleEntity(id, id, id, id)
	 */

	public RedAgent(String name, String team)
	{
		super(name, team);
		prevActions = new ArrayList<Action>();
		graph = new Graph();
	}

	private void updateState()
	{
		Action[] last = new Action[1];
		for (Percept percept: getAllPercepts()) handlePercept(percept, last);
		if (last[0] != null)
			prevActions.add(last[0]);
	}

	private void handlePercept(Percept percept, Action[] last)
	{
		// convert percept parameters to strings
		List<String> params = new ArrayList<String>();
		for (Parameter p : percept.getParameters())
			params.add(p.toProlog());

		switch (percept.getName())
		{
			case "edges":
				graph.total_edges = Integer.parseInt(params.get(0));
				break;
			case "energy":
				energy = Integer.parseInt(params.get(0));
				break;
			case "health":
				health = Integer.parseInt(params.get(0));
				break;
			case "id":
				id = params.get(0);
				break;
			case "lastAction":
				if (last[0] == null)
					last[0] = new Action(params.get(0));
				else
					last[0] = new Action(params.get(0), last[0].getParameters());
				break;
			case "lastActionParam":
			case "lastActionResult":
				if (last[0] == null)
					last[0] = new Action("unknown", new Identifier(params.get(0)));
				else
				{
					LinkedList<Parameter> lp = last[0].getParameters();
					lp.add(new Identifier(params.get(0)));
					last[0] = new Action(last[0].getName(), lp);
				}
			case "position":
				position = params.get(0);
				graph.visit(position);
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
	public void handlePercept(Percept percept)
	{
	}

	@Override
	public Action step()
	{

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
