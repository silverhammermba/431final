package redrovers;

import java.util.Random;
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
	
	private LinkedList<String> pathList; //store current path node list
	
	public Explorer(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		
		
		//probe	
		if (!role.equals("Explorer"))
		{
			System.err.println("wrong class for agent!");
			return MarsUtil.skipAction();
		}
		

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
						return MarsUtil.rechargeAction();

					return MarsUtil.probeAction(id);
				}
			}
		}
		
		
		System.err.println("\n\n now go somewhere else\n\n");
		*/
		
		
		
		/*
		//Node start = getNode(sid);
		LinkedList<String> pathList = graph.explore(position);
		for(String pos:pathList){
			System.err.println("\n\nwill go to " + pos + "\n\n");
			if(energy < maxEnergy/2){
				return MarsUtil.rechargeAction();
			}else{
				return MarsUtil.gotoAction(pos);
			}		
			
		}
		*/
		
		this.pathList = graph.explore(position);
		
		if(energy < 1){ 
			return MarsUtil.rechargeAction();
		}
		
		if(graph.nodeValue(position) == null){ // try to probe it 
			return MarsUtil.probeAction();
		
		}else{  //try to survey
			List<String> nodes = graph.nodesAtRange(position,1);
			if(graph.unknownNearby(position)){
				return MarsUtil.surveyAction();
			
			}else{ //try to goto somewhere according to pathList	
				String pos = pathList.get(0);
				if(pathList != null){ //if there is node to go 
					if(energy < graph.edgeWeight(position,pos)){ //energy too low to go
						return MarsUtil.rechargeAction();
					}else{
						pathList.remove(0);
						return MarsUtil.gotoAction(pos);
					}	
				}
			
			}
			
		}

		return MarsUtil.skipAction();
	}
}
