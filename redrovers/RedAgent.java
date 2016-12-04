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
	 *  probedVertex(id, num)
	 */
	int money;
	int score; // total score
	int lastStepScore; // score last step
	int zoneScore; // this vehicle's zone's score
	int zonesScore; // score from team's zones
	int step;
	int steps;

	/* percepts not currently stored:
	 *  id(id)
	 *  achievement(id)
	 *  bye
	 *  deadline(num)
	 *  ranking(num)
	 *  requestAction
	 *  simEnd
	 *  simStart
	 *  timestamp(num)
	 *  visibleEntity(id, id, id, id)
	 *  visibleVertex(id) TODO should be stored to track enemy zones
	 */

	public RedAgent(String name, String team)
	{
		super(name, team);
		prevActions = new ArrayList<Action>();
		graph = new Graph();
	}

	private void updateState()
	{
		System.err.println(getName() + "UPDATING");
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
			case "lastAction":
				if (last[0] == null)
					// initialize last action
					last[0] = new Action(params.get(0));
				else
					// update last action's name
					last[0] = new Action(params.get(0), last[0].getParameters());
				break;
			case "lastActionParam":
			case "lastActionResult": // treat result as just another param
				// these are never valid parameters
				if (params.get(0) == "" || params.get(0) == "null") break;

				if (last[0] == null)
					// initialize last action
					last[0] = new Action("unknown", new Identifier(params.get(0)));
				else
				{
					// update last action's parameters
					LinkedList<Parameter> lp = last[0].getParameters();
					lp.add(new Identifier(params.get(0)));
					last[0] = new Action(last[0].getName(), lp);
				}
				break;
			case "lastStepScore":
				lastStepScore = Integer.parseInt(params.get(0));
				break;
			case "maxEnergy":
				maxEnergy = Integer.parseInt(params.get(0));
				break;
			case "maxEnergyDisabled":
				maxEnergyDisabled = Integer.parseInt(params.get(0));
				break;
			case "maxHealth":
				maxHealth = Integer.parseInt(params.get(0));
				break;
			case "money":
				money = Integer.parseInt(params.get(0));
				break;
			case "position":
				position = params.get(0);
				graph.visit(position);
				break;
			case "probedVertex":
				graph.nodeValue(params.get(0), Integer.parseInt(params.get(1)));
				break;
			case "role":
				role = params.get(0);
				break;
			case "score":
				score = Integer.parseInt(params.get(0));
				break;
			case "step":
				step = Integer.parseInt(params.get(0));
				break;
			case "steps":
				steps = Integer.parseInt(params.get(0));
				break;
			case "strength":
				strength = Integer.parseInt(params.get(0));
				break;
			case "surveyedEdge":
				graph.addEdge(params.get(0), params.get(1), Integer.parseInt(params.get(2)));
				break;
			case "vertices":
				graph.total_verts = Integer.parseInt(params.get(0));
				break;
			case "visRange":
				visRange = Integer.parseInt(params.get(0));
				break;
			case "visibleEdge":
				graph.addEdge(params.get(0), params.get(1));
				break;
			case "zoneScore":
				zoneScore = Integer.parseInt(params.get(0));
				break;
			case "zonesScore":
				zonesScore = Integer.parseInt(params.get(0));
				break;
			default:
				System.err.println(getName() + " can't handle percept " + percept);
		}
	}

	@Override
	public void handlePercept(Percept percept)
	{
		System.err.println("Cannot hanlde percepts-as-notifications");
		System.exit(1);
	}

	@Override
	public Action step()
	{
		updateState();
		System.err.println(this);

		// TODO do something
		return MarsUtil.skipAction();
	}

	@Override
	public String toString()
	{
		String str = "";
		str += getName() + " - " + role + "\n";
		str += "❤ " + health + "/" + maxHealth + "\n";
		str += "⚡ " + energy + "/" + (health == 0 ? maxEnergyDisabled : maxEnergy) + "\n";
		str += "vis " + visRange + "\n";
		str += "str " + strength + "\n\n";
		str += "pos " + position + "\n";
		if (!prevActions.isEmpty())
		{
			str += prevActions.get(prevActions.size() - 1) + "\n";
		}

		str += "TEAM STATUS\n";
		str += "score: " + score + "\n";
		str += "last step: " + lastStepScore + "\n";
		str += "zone: " + zoneScore + "\n";
		str += "all zones: " + zonesScore + "\n\n";

		str += "sim step " + step + "/" + steps;

		return str;
	}
}
