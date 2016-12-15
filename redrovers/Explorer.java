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
		
		
		//if disabled, move to a repairer
		if (health == 0)
		{
			OtherAgent agent = graph.nearestAgent(this, (ag) -> getTeam().equals(ag.team) && "Repairer".equals(ag.role));

			if (agent == null)
				return rechargeAction();

			LinkedList<String> path = graph.shortestPath(position, agent.position);

			if (path != null && path.size() > 1)
				return gotoGreedy(path.pop());

			return rechargeAction();
		}

		
		
	
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
		
		
		
		
	

		//go to other place to probe
		String pos;
		for (OtherAgent agent : agents.values()){
			if (getName().compareTo(agent.name) == 0){
				continue;
			}
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
		 
		if(this.pathList != null && this.pathList.size() != 0){
			return gotoGreedy(pathList.pop());
		}
		
		//try to survey	
		if(graph.unsurveyedEdges(position)){
			return surveyGreedy();
		}
		
		
		LinkedList<String> n = graph.territory(this.position, this);
		if(n == null){
			List<String> nodes = graph.nodesAtRange(position, 1);
			return gotoGreedy(nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size())));
		}
		else if(n.size() == 0){
			return parryGreedy();
		}

		return gotoGreedy(n.removeFirst());

	
	
			
	
	}
		
	
}
