package redrovers;
import eis.iilang.Action;
import massim.javaagents.Agent;
import massim.javaagents.agents.MarsUtil;

public class Explorer extends RedAgent
{
	public Explorer(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (wrongRole()) return MarsUtil.skipAction();

		// TODO logic

		return MarsUtil.skipAction();
	}
}
