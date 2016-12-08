package redrovers;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import eis.iilang.Action;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class Sentinel extends RedAgent
{
	private LinkedList<String> pathList; //store current path node list
	
	public Sentinel(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (wrongRole()) return MarsUtil.skipAction();

		// TODO logic
		this.pathList = graph.explore(position);

		if(energy < 1){ 
			return MarsUtil.rechargeAction();
		}


		//try to survey
		List<String> nodes = graph.nodesAtRange(position,1);
		if(graph.unsurveyedEdges(position)){
			return MarsUtil.surveyAction();

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
							return MarsUtil.rechargeAction();
						}

						return MarsUtil.gotoAction(pos);
					}
				}
			}

			if(pathList != null){ //if there is node to go
				pos = pathList.get(0);
				if(energy < graph.edgeWeight(position,pos)){ //energy too low to go
					return MarsUtil.rechargeAction();
				}else{
					pathList.remove(0);
					return MarsUtil.gotoAction(pos);
				}
			}

			return MarsUtil.skipAction();
		}

	}
	
	
	

	
}
