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

public class Explorer extends RedAgent
{
	public Explorer(String name, String team){
		super(name,team);
	}
	Action think(){
		if(graph.nodeValue(position) == null){
			return MarsUtil.probeAction();
		}
		
		return MarsUtil.skipAction();
	}
}
