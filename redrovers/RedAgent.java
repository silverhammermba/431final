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

/**
 * This class defines a (slightly) higher-level API on top of the Agent class.
 *
 * <p>This class implements the abstract <code>step</code> method, processes
 * all percepts and stores them in appropriate fields for easier access
 * <i>and</i> broadcasts all (relevant) percepts to other agents on the team.
 * Role-specific logic is then handled in the <code>think</code> method, after
 * all percepts and simple messages have been dealt with.
 */
public abstract class RedAgent extends Agent
{
	// copied from team conf
	protected static final int teamSize = 10;

	// fields for storing percept information
	protected String role;
	protected int energy;
	protected int maxEnergy;
	protected int health;
	protected int maxHealth;
	protected int visRange;
	protected int strength;
	protected String position; // node id
	/**
	 * A list of previous actions with the action outcomes added as parameters.
	 *
	 * <p>This unifies the percepts lastAction, lastActionParam, and
	 * lastActionResult. Invalid parameters such as "" and "null" are not
	 * stored. unknownAction actions are not stored.
	 *
	 * <p>The ordering of the result parameter is arbitrary. For example
	 * goto(success,v22) and goto(v22,failed_random) are both possible Actions
	 * in this list.
	 */
	protected List<Action> prevActions;
	/**
	 * A map from agent IDs to {@link OtherAgent} objects for tracking other agents.
	 *
	 * <p>This unifies the percepts inspectedEntity and visibleEntity. To
	 * ensure a consistent state, the map should be accessed using {@link getAgent}.
	 */
	protected Map<String, OtherAgent> agents;
	/**
	 * The map of mars.
	 *
	 * <p>This unifies the percepts surveyedEdge, edges, vertices, visibleEdge,
	 * probedVertex. <b>TODO</b> it should also store the visibleVertex percept
	 * for tracking team zones.
	 */
	protected Graph graph;
	protected int money; // team money
	protected int score; // total score
	protected int lastStepScore; // score last step
	protected int zoneScore; // this vehicle's zone's score
	protected int zonesScore; // score from team's zones
	protected int step; // current step of simulation
	protected int steps; // total simulation steps

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
	 */

	// same as super, just inits some structures
	public RedAgent(String name, String team)
	{
		super(name, team);
		prevActions = new ArrayList<Action>();
		agents = new HashMap<String, OtherAgent>();
		graph = new Graph();
	}

	// convert percept to belief (for the handleBelief method)
	private LogicBelief perceptToBelief(Percept percept)
	{
		List<String> params = new ArrayList<String>();
		for (Parameter p : percept.getParameters())
			params.add("" + p);
		return new LogicBelief(percept.getName(), params);
	}

	/**
	 * Get the internal representation for an agent with the given id and team.
	 *
	 * <p>The agent is constructed first if the representation does not yet
	 * exist. In fact team parameter is ignored if agent is already known.
	 *
	 * @param id the id of the agent to get
	 * @param team the team of the agent (if it must be constructed)
	 * @return the internal representation of the agent
	 */
	protected OtherAgent getAgent(String id, String team)
	{
		if (!agents.containsKey(id)) agents.put(id, new OtherAgent(id, team));
		return agents.get(id);
	}

	// mandatory but unused
	@Override
	public void handlePercept(Percept percept)
	{
		System.err.println("Cannot handle percepts-as-notifications");
		System.exit(1);
	}

	/**
	 * Handle all percepts sent by other agents, then our own percepts, then
	 * calls <code>think</code> (if the simulation has started).
	 */
	@Override
	public Action step()
	{
		// reset (some) stale information about other agents
		resetAgents();
		// process messages first, because they might be outdated
		handleMessages();
		// then process percepts, possibly updating message info
		handlePercepts();
		// the wonderful agent framework asks for actions before the simulation has even started
		if (steps == 0) return new Action("unknownAction");

		return think();
	}

	/**
	 * Role-specific action selection.
	 */
	abstract Action think();

	private void resetAgents()
	{
		for (OtherAgent agent : agents.values())
			agent.position = null;
	}

	// store all of our own percepts in fields (using handleBelief)
	private void handlePercepts()
	{
		// hack to allow handleBelief to pass back an action
		Action[] last = new Action[1];
		// convert each percept to a belief and handle it
		for (Percept percept: getAllPercepts()) handleBelief(perceptToBelief(percept), last, null);
		// if we got a (valid) previous action
		if (last[0] != null && !last[0].getName().equals("unknownAction"))
			prevActions.add(last[0]);
	}

	// store all percepts from other agents in fields (using handleBelief)
	private void handleMessages()
	{
		for (Message message: getMessages())
		{
			if (!(message.value instanceof LogicBelief))
			{
				// XXX really shouldn't get here
				System.err.println("Unknown message belief: " + message.value);
				continue;
			}

			// handle each belief according to its sender (who must be on our team)
			handleBelief((LogicBelief)message.value, null, getAgent(message.sender, getTeam()));
		}
	}

	/* store a belief in whatever member variable makes sense
	 * last passes out a last action, sender is the sender of the belief (if any)
	 */
	private void handleBelief(LogicBelief belief, Action[] last, OtherAgent sender)
	{
		List<String> params = belief.getParameters();
		OtherAgent agent; // for inspectedEntity and visibleEntity

		switch (belief.getPredicate())
		{
			case "edges":
				graph.total_edges = Integer.parseInt(params.get(0));
				break;
			case "energy":
				if (sender == null)
				{
					energy = Integer.parseInt(params.get(0));
					broadcastBelief(belief);
				}
				else
					sender.energy = Integer.parseInt(params.get(0));
				break;
			case "health":
				if (sender == null)
				{
					health = Integer.parseInt(params.get(0));
					broadcastBelief(belief);
				}
				else
					sender.health = Integer.parseInt(params.get(0));
				break;
			case "inspectedEntity":
				if (params.get(0).equals(getName())) break;
				agent = getAgent(params.get(0), params.get(1));
				agent.team      = params.get(1);
				agent.role      = params.get(2);
				agent.position  = params.get(3);
				agent.energy    = Integer.parseInt(params.get(4));
				agent.maxEnergy = Integer.parseInt(params.get(5));
				agent.health    = Integer.parseInt(params.get(6));
				agent.maxHealth = Integer.parseInt(params.get(7));
				agent.strength  = Integer.parseInt(params.get(8));
				agent.visRange  = Integer.parseInt(params.get(9));

				if (sender == null)
					broadcastBelief(belief);
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
				if (sender == null)
				{
					maxEnergy = Integer.parseInt(params.get(0));
					broadcastBelief(belief);
				}
				else
					sender.maxEnergy = Integer.parseInt(params.get(0));
				break;
			case "maxHealth":
				if (sender == null)
				{
					maxHealth = Integer.parseInt(params.get(0));
					broadcastBelief(belief);
				}
				else
					sender.maxHealth = Integer.parseInt(params.get(0));
				break;
			case "money":
				money = Integer.parseInt(params.get(0));
				break;
			case "position":
				String pos = params.get(0);
				// we will at least get visibleEdge beliefs for this pos
				graph.noMoreEdges(pos);
				if (sender == null)
				{
					position = pos;
					broadcastBelief(belief);
				}
				else
					sender.position = pos;
				break;
			case "probedVertex":
				graph.nodeValue(params.get(0), Integer.parseInt(params.get(1)));
				if (sender == null)
					broadcastBelief(belief);
				break;
			case "role":
				if (sender == null)
				{
					role = params.get(0);
					broadcastBelief(belief);
				}
				else
					sender.role = params.get(0);
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
				if (sender == null)
				{
					strength = Integer.parseInt(params.get(0));
					broadcastBelief(belief);
				}
				else
					sender.strength = Integer.parseInt(params.get(0));
				break;
			case "surveyedEdge":
				/* TODO if we survey at range greater than 1, then we should be
				 * able to call graph.noMoreEdges on neighboring nodes. But we
				 * aren't directly told what the effective range of the survey
				 * was and it turns out to be pretty hard to calculate from the
				 * resulting surveyedEdge percepts (especially since agents in
				 * the same zone share percepts). So for now just ignore it,
				 * which does waste some time.
				 */
				graph.addEdge(params.get(0), params.get(1), Integer.parseInt(params.get(2)));
				if (sender == null)
					broadcastBelief(belief);
				break;
			case "vertices":
				graph.total_verts = Integer.parseInt(params.get(0));
				break;
			case "visibleEntity":
				if (params.get(0).equals(getName())) break;
				agent = getAgent(params.get(0), params.get(2));
				agent.position = params.get(1);
				agent.team = params.get(2);
				// TODO maybe we can do something smart in the "normal" case...
				if (params.get(3).equals("disabled"))
					agent.health = 0;
				if (sender == null)
					broadcastBelief(belief);
				break;
			case "visRange":
				if (sender == null)
				{
					visRange = Integer.parseInt(params.get(0));
					broadcastBelief(belief);
				}
				else
					sender.visRange = Integer.parseInt(params.get(0));
				break;
			case "visibleEdge":
				graph.addEdge(params.get(0), params.get(1));
				if (sender == null)
					broadcastBelief(belief);
				break;
			case "zoneScore":
				zoneScore = Integer.parseInt(params.get(0));
				break;
			case "zonesScore":
				zonesScore = Integer.parseInt(params.get(0));
				break;
			default:
				if (sender == null)
					System.err.println(getName() + " can't handle percept " + belief);
				else
					System.err.println(getName() + " can't handle message " + belief);
		}
	}

	@Override
	public String toString()
	{
		String str = "";
		str += getName() + " (" + getTeam() + ") - " + role + "\n";
		str += "❤ " + health + "/" + maxHealth + "\n";
		str += "⚡ " + energy + "/" + maxEnergy + "\n";
		str += "→ " + visRange + "\n";
		str += "† " + strength + "\n";
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

		str += "Graph\n" + graph;

		str += "TEAM STATUS\n";
		str += "score: " + score + "\n";
		str += "last step: " + lastStepScore + "\n";
		str += "zone: " + zoneScore + "\n";
		str += "all zones: " + zonesScore + "\n\n";

		str += "sim step " + step + "/" + steps;

		return str;
	}

	/* the following are wrappers for the various MarsUtil action methods. they
	 * perform basic checks (and print informative error messages) and
	 * automatically recharge if insufficient energy
	 */

	protected Action gotoGreedy(String node_id)
	{
		if (!graph.hasEdge(position, node_id))
		{
			System.err.println(getName() + " attempted an invalid goto: " + position + " -> " + node_id);
			return MarsUtil.gotoAction(node_id);
		}

		Integer weight = graph.edgeWeight(position, node_id);
		if (weight == null) return surveyGreedy();

		if (energy < weight) return MarsUtil.rechargeAction();

		return MarsUtil.gotoAction(node_id);
	}

	protected Action probeGreedy()
	{
		if (energy < 1) return MarsUtil.rechargeAction();
		return MarsUtil.probeAction();
	}

	protected Action probeGreedy(String node_id)
	{
		Integer range = graph.range(position, node_id);
		if (range == null || range > visRange)
		{
			System.err.println(getName() + " attempted an invalid probe: " + node_id + " (from " + position + ")");
			return MarsUtil.probeAction(node_id);
		}

		if (energy < range + 1) return MarsUtil.rechargeAction();
		return MarsUtil.probeAction(node_id);
	}

	protected Action surveyGreedy()
	{
		if (energy < 1) return MarsUtil.rechargeAction();
		return MarsUtil.surveyAction();
	}

	protected Action inspectGreedy()
	{
		if (energy < 2) return MarsUtil.rechargeAction();
		return MarsUtil.inspectAction();
	}

	protected Action inspectGreedy(String id)
	{
		// if we don't know the agent or its position, that's bad
		if (!agents.containsKey(id) || agents.get(id).position == null)
		{
			System.err.println(getName() + " attempted an invalid inspect (unknown agent/range): " + id);
			return MarsUtil.inspectAction(id);
		}

		Integer range = graph.range(position, agents.get(id).position);
		if (range == null || range > visRange)
		{
			System.err.println(getName() + " attempted an invalid inspect (out of range): " + id + " (from " + position + ")");
			return MarsUtil.inspectAction(id);
		}

		if (energy < range + 2) return MarsUtil.rechargeAction();
		return MarsUtil.inspectAction(id);
	}

	protected Action parryGreedy()
	{
		if (energy < 2) return MarsUtil.rechargeAction();
		return MarsUtil.parryAction();
	}

	protected Action attackGreedy(String id)
	{
		// if we don't know the agent or its position, that's bad
		if (!agents.containsKey(id) || agents.get(id).position == null)
		{
			System.err.println(getName() + " attempted an invalid attack (unknown agent/range): " + id);
			return MarsUtil.attackAction(id);
		}
		// same team is bad
		if (agents.get(id).team.equals(getTeam()))
		{
			System.err.println(getName() + " attempted an invalid attack (same team): " + id);
			return MarsUtil.attackAction(id);
		}

		Integer range = graph.range(position, agents.get(id).position);
		if (range == null || range > visRange)
		{
			System.err.println(getName() + " attempted an invalid attack (out of range): " + id + " (from " + position + ")");
			return MarsUtil.attackAction(id);
		}

		if (energy < range + 2) return MarsUtil.rechargeAction();
		return MarsUtil.attackAction(id);
	}

	protected Action buyGreedy(String item)
	{
		if (money < 2)
		{
			System.err.println(getName() + " attempted an invalid buy (not enough money): " + item);
			return MarsUtil.buyAction(item);
		}

		switch (item)
		{
			case "battery":
			case "sensor":
			case "shield":
			case "sabotageDevice":
				break;
			default:
				System.err.println(getName() + " attempted an invalid buy: " + item);
				return MarsUtil.buyAction(item);
		}

		if (energy < 2) return MarsUtil.rechargeAction();
		return MarsUtil.buyAction(item);
	}

	protected Action repairGreedy(String id)
	{
		// if we don't know the agent or its position, that's bad
		if (!agents.containsKey(id) || agents.get(id).position == null)
		{
			System.err.println(getName() + " attempted an invalid repair (unknown agent/range): " + id);
			return MarsUtil.repairAction(id);
		}
		// other team is bad
		if (!agents.get(id).team.equals(getTeam()))
		{
			System.err.println(getName() + " attempted an invalid repair (wrong team): " + id);
			return MarsUtil.repairAction(id);
		}

		Integer range = graph.range(position, agents.get(id).position);
		if (range == null || range > visRange)
		{
			System.err.println(getName() + " attempted an invalid repair (out of range): " + id + " (from " + position + ")");
			return MarsUtil.repairAction(id);
		}

		if (energy < range + 2) return MarsUtil.rechargeAction();
		return MarsUtil.repairAction(id);
	}

	protected boolean wrongRole()
	{
		if (role.equals(getClass().getSimpleName())) return false;
		System.err.println(role + " agent is running class " + getClass().getSimpleName());
		return true;
	}
}
