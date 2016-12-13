package redrovers;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import eis.iilang.Action;
import massim.javaagents.Agent;

/**
 * An agent that probes nodes that are in range.
 *
 * Probes the closest nodes first, recharges when it doesn't have enough energy
 * to probe.
 */
public class Explorer extends RedAgent
{

	private LinkedList<String> pathList; //store current path node list



	public Explorer(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (wrongRole()) return skipAction();

		/*
		List<String> nodes;
		for (int i = 0; i <= visRange; ++i)
		{
			nodes = graph.nodesAtRange(position,i);


			for (String id : nodes)
			{
				if (graph.nodeValue(id) == null)
				{
					if (energy < 1 + i)
						return rechargeAction();

					return probeAction(id);
				}
			}
		}


		System.err.println("\n\n now go somewhere else\n\n");
		*/





		this.pathList = graph.explore(position);

		if(energy < 1){ 
			return rechargeAction();
		}

		if(graph.nodeValue(position) == null){ // try to probe it
			return probeAction();

		}else{  //try to survey
			List<String> nodes = graph.nodesAtRange(position,1);
			if(graph.unsurveyedEdges(position)){
				return surveyAction();

			}else{ //try to goto somewhere according to pathList
				String pos;
				for (Map.Entry<String, OtherAgent> agentEntry : agents.entrySet()){
					//if there is another explorer at same node, random move
					OtherAgent agent = agentEntry.getValue();
					if (getTeam().equals(agent.team) && position.equals(agent.position) && role.equals(agent.role)){
						//who has more energy go randomly 
						if(agent.energy <= energy){
							Random random = new Random();
							List<String> neighborNodes = graph.nodesAtRange(position,1);
							pos = neighborNodes.get(random.nextInt(neighborNodes.size()));

							if(energy < graph.edgeWeight(position,pos)){ //energy too low to go
								return rechargeAction();
							}

							return gotoAction(pos);
						}
					}
				}

				if(pathList != null){ //if there is node to go
					pos = pathList.get(0);
					if(energy < graph.edgeWeight(position,pos)){ //energy too low to go
						return rechargeAction();
					}else{
						pathList.remove(0);
						return gotoAction(pos);
					}
				}

			}

		}
		return skipAction();
	}
}
