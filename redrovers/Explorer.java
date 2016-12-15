package redrovers;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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





		//this.pathList = graph.explore(position);

		
		/*
		if(graph.nodeValue(position) == null){ 
			return probeGreedy();
		}
		*/
		
		
			
		
		
		//Map<String, OtherAgent> agentsCopy = agents;
		//probe the node 
		if(graph.nodeValue(position) == null){ 
			boolean flag = true; //whether this agent should probe
			
			for (OtherAgent agent : agents.values()){
				//if there is another explorer at same node, the agent with bigger name probe it
				if (getTeam().equals(agent.team) &&        
						position.equals(agent.position) &&
						role.equals(agent.role) &&
						agent.health <= 0 &&
						getName().compareTo(agent.name) < 0){
					flag = false; //another agent will probe 
					break;
					
				}
			}
			
			if(flag) return probeGreedy();
		}
		
		
		
		
	

		
		String pos;
		for (OtherAgent agent : agents.values()){
			//if there is another explorer at same node, random move
			if (getTeam().equals(agent.team) && position.equals(agent.position) && role.equals(agent.role)){
				//who has more energy go randomly 
				if((agent.energy == energy && getName().compareTo(agent.name) > 0)
						|| (agent.energy < energy)){
					
					//randomly moving
					Random random = new Random();
					List<String> neighborNodes = graph.nodesAtRange(position,1);
					pos = neighborNodes.get(random.nextInt(neighborNodes.size()));
					return gotoGreedy(pos);
			
				}
			
			}
		}
	
		
		
					

		this.pathList = graph.shortestPath(position, (id) -> (graph.nodeValue(id) == null));
		 
		if(this.pathList != null){
			// TODO pathList might be empty
			return gotoGreedy(pathList.pop());
		}
		
		//try to survey	
		if(graph.unsurveyedEdges(position)){
			return surveyGreedy();
		}
		
		LinkedList<String> n = graph.territory(this);
		if(n == null){
			List<String> nodes = graph.nodesAtRange(position, 1);
			return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
		}
		else if(n.size() == 0){
			return rechargeAction();
		}
		return gotoGreedy(n.removeFirst());

	

		
		
		
		

		
			/*
		
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
		*/
		
	
	}
		
	
}
