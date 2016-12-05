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
 * An agent that probes nodes that are in range.
 *
 * Probes the closest nodes first, recharges when it doesn't have enough energy
 * to probe.
 */
public class Explorer extends RedAgent
{
	public Explorer(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (!role.equals("Explorer"))
		{
			System.err.println("wrong class for agent!");
			return MarsUtil.skipAction();
		}

		List<String> nodes;
		for (int i = 0; i < visRange; ++i)
		{
			nodes = graph.nodesAtRange(position,visRange);
			for (String id : nodes)
			{
				if (graph.nodeValue(id) == null)
				{
					if (energy < 1 + i)
						return MarsUtil.rechargeAction();
					return MarsUtil.probeAction(id);
				}
			}
		}
		return MarsUtil.skipAction();
	}
}
