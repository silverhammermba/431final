package redrovers;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import apltk.interpreter.data.Belief;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.Message;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class RedAgent extends Agent
{
	private static final int teamSize = 10;

	// member variables for storing percept information
	private String role;
	private int energy;
	private int maxEnergy;
	private int health;
	private int maxHealth;
	private int visRange;
	private int strength;
	private String position; // node id
	private List<Action> prevActions; // previous actions (results added as params)
	private Map<String, OtherAgent> agents; // info about other agents
	/* agents tracks the following percepts:
	 *  inspectedEntity(id, id, id, id, num, num, num, num, num, num)
	 *  visibleEntity(id, id, id, id)
	 */
	private Graph graph;
	/* graph tracks the following percepts:
	 *  surveyedEdge(id, id, num)
	 *  edges(num)
	 *  vertices(num)
	 *  visibleEdge(id, id)
	 *  probedVertex(id, num)
	 */
	private int money; // team money
	private int score; // total score
	private int lastStepScore; // score last step
	private int zoneScore; // this vehicle's zone's score
	private int zonesScore; // score from team's zones
	private int step; // current step of simulation
	private int steps; // total simulation steps

	/* percepts not currently stored:
	 *  id(id)
	 *  achievement(id)
	 *  bye
	 *  deadline(num)
	 *  maxEnergyDisabled(num) XXX always equal to maxEnergy in our sim config
	 *  ranking(num)
	 *  requestAction
	 *  simEnd
	 *  simStart
	 *  timestamp(num)
	 *  visibleVertex(id) TODO should be stored to track enemy zones
	 */

	public RedAgent(String name, String team)
	{
		super(name, team);
		prevActions = new ArrayList<Action>();
		agents = new HashMap<String, OtherAgent>();
		graph = new Graph(this);
	}

	private void updateState()
	{
		Action[] last = new Action[1];
		for (Percept percept: getAllPercepts()) handlePercept(percept, last);
		if (last[0] != null && !last[0].getName().equals("unknownAction"))
			prevActions.add(last[0]);
		
		broadcastBelief(new LogicBelief("done"));
	}

	private void handlePercept(Percept percept, Action[] last)
	{
		// convert percept parameters to strings
		List<String> params = new ArrayList<String>();
		for (Parameter p : percept.getParameters())
			params.add("" + p);
		OtherAgent agent; // for inspectedEntity and visibleEntity

		LogicBelief belief;
		switch (percept.getName())
		{
			case "edges":
				graph.total_edges = Integer.parseInt(params.get(0));
				break;
			case "energy":
				energy = Integer.parseInt(params.get(0));
				broadcastBelief(perceptToBelief(percept));
				break;
			case "health":
				health = Integer.parseInt(params.get(0));
				broadcastBelief(perceptToBelief(percept));
				break;
			case "inspectedEntity":
				agent = getAgent(params.get(0));
				agent.team      = params.get(1);
				agent.role      = params.get(2);
				agent.position  = params.get(3);
				agent.health    = Integer.parseInt(params.get(6));
				agent.maxHealth = Integer.parseInt(params.get(7));
				agent.strength  = Integer.parseInt(params.get(8));
				agent.visRange  = Integer.parseInt(params.get(9));
				agent.energy    = Integer.parseInt(params.get(4));
				agent.maxEnergy = Integer.parseInt(params.get(5));
				
				broadcastBelief(perceptToBelief(percept));
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
				if (params.get(0).isEmpty() || params.get(0).equals("null")) break;

				if (last[0] == null)
					// initialize last action
					last[0] = new Action("unknownAction", new Identifier(params.get(0)));
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
				broadcastBelief(perceptToBelief(percept));
				break;
			case "maxHealth":
				maxHealth = Integer.parseInt(params.get(0));
				broadcastBelief(perceptToBelief(percept));
				break;
			case "money":
				money = Integer.parseInt(params.get(0));
				break;
			case "position":
				position = params.get(0);
				broadcastBelief(perceptToBelief(percept));
				graph.visit(position);
				break;
			case "probedVertex":
				graph.nodeValue(params.get(0), Integer.parseInt(params.get(1)));
				broadcastBelief(perceptToBelief(percept));
				break;
			case "role":
				role = params.get(0);
				broadcastBelief(perceptToBelief(percept));
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
				broadcastBelief(perceptToBelief(percept));
				break;
			case "surveyedEdge":
				graph.addEdge(params.get(0), params.get(1), Integer.parseInt(params.get(2)));
				broadcastBelief(perceptToBelief(percept));
				break;
			case "vertices":
				graph.total_verts = Integer.parseInt(params.get(0));
				break;
			case "visibleEntity":
				agent = getAgent(params.get(0));
				agent.position = params.get(1);
				agent.team = params.get(2);
				if (params.get(3).equals("normal"))
				{
					// agent is not disabled
					if (agent.maxHealth == null)
						agent.health = 1; // don't know max health, choose some arbitrary value
					else // XXX assume the worst
					{
						if (agent.team.equals(getTeam()))
							agent.health = 1;
						else
							agent.health = agent.maxHealth;
					}
				}
				else
						agent.health = 0;
				broadcastBelief(perceptToBelief(percept));
				break;
			case "visRange":
				visRange = Integer.parseInt(params.get(0));
				broadcastBelief(perceptToBelief(percept));
				break;
			case "visibleEdge":
				graph.addEdge(params.get(0), params.get(1));
				broadcastBelief(perceptToBelief(percept));
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

	private LogicBelief perceptToBelief(Percept percept)
	{
		List<String> params = new ArrayList<String>();
		for (Parameter p : percept.getParameters())
			params.add("" + p);
		return new LogicBelief(percept.getName(), params);
	}

	private OtherAgent getAgent(String name)
	{
		if (!agents.containsKey(name)) agents.put(name, new OtherAgent(name));
		return agents.get(name);
	}

	@Override
	public void handlePercept(Percept percept)
	{
		System.err.println("Cannot handle percepts-as-notifications");
		System.exit(1);
	}

	@Override
	public Action step()
	{
		handleMessages();
		updateState();
		// the wonderful agent framework asks for actions before the simulation has even started
		if (steps == 0) return new Action("unknownAction");

		return MarsUtil.skipAction();
	}

	private void handleMessages()
	{
		

		Collection<Message> messages = getMessages();
		
		for(Message message: messages){
			Belief b = message.value;
			if(b instanceof LogicBelief){
				LogicBelief l = (LogicBelief)b;
				String pred = (l).getPredicate();
				
				switch(pred){
				    
					case "visited":
						graph.visit(l.getParameters().get(0));
						break;
					case "newEdge":
						graph.addEdge(l.getParameters().get(0), l.getParameters().get(1), Integer.parseInt(l.getParameters().get(2)));
						break;
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
		str += getName() + " (" + getTeam() + ") - " + role + "\n";
		str += "❤ " + health + "/" + maxHealth + "\n";
		str += "⚡ " + energy + "/" + maxEnergy + "\n";
		str += "vis " + visRange + "\n";
		str += "str " + strength + "\n";
		str += "pos " + position + "\n";
		if (!prevActions.isEmpty())
		{
			str += prevActions.get(prevActions.size() - 1) + "\n";
		}

		if (!agents.isEmpty())
		{
			str += "Known agents:\n";
			for (OtherAgent agent : agents.values())
				str += agent.toString() + "\n";
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
