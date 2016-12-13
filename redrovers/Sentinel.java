package redrovers;
import eis.iilang.Action;
import massim.javaagents.Agent;

public class Sentinel extends RedAgent
{
	public Sentinel(String name, String team)
	{
		super(name,team);
	}

	Action think()
	{
		if (wrongRole()) return skipAction();

		// TODO logic

		return skipAction();
	}
}
