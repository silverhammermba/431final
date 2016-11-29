package redrovers;
import eis.iilang.Percept;
import eis.iilang.Action;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class RedAgent extends Agent
{
	public RedAgent(String name, String team)
	{
		super(name, team);
	}

	@Override
	public void handlePercept(Percept percept)
	{
	}

	@Override
	public Action step()
	{
		return MarsUtil.skipAction();
	}
}
