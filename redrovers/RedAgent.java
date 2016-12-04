package redrovers;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import apltk.interpreter.data.Belief;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.Message;
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
	int money;
	int score; // total score
	int lastStepScore; // score last step
	int zoneScore; // this vehicle's zone's score
	int zonesScore; // score from team's zones
	int step;
	int steps;

	public RedAgent(String name, String team)
	{
		super(name, team);
		prevActions = new ArrayList<Action>();
		graph = new Graph(this);
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
				graph.addEdge(params.get(0), params.get(1), Integer.parseInt(params.get(2)), "");
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

		handleMessages();
		
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
	
	private void handleMessages(){
		Collection<Message> messages = getMessages();
		for(Message message: messages){
			Belief b = message.value;
			if(b instanceof LogicBelief){
				LogicBelief l = (LogicBelief)b;
				String pred = (l).getPredicate();
				switch(pred){
					case "newEdge":
						graph.addEdge(l.getParameters().get(0), l.getParameters().get(1), Integer.parseInt(l.getParameters().get(2)), message.sender);
					default:
						System.err.println(getName() + " can't handle message " + message);
				}
			}
		}
	}

	@Override
	public String toString()
	{
		String str = "";
		str += getName() + " - " + role + "\n";
		str += "hth " + health + "/" + maxHealth + "\n";
		str += "enr " + energy + "/" + (health == 0 ? maxEnergyDisabled : maxEnergy) + "\n";
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
