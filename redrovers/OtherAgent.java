package redrovers;
import eis.iilang.Action;

/**
 * This class is a simple public structure for storing information about other agents.
 *
 * The wonderful agent framework automatically manages all Agent instances,
 * which means we can't use that class to track other agents we observe.
 */
public class OtherAgent
{
	public String name;
	public String team;
	public String role;
	public String position;
	public Integer positionAge;
	public Integer energy;
	public Integer maxEnergy;
	public Integer health;
	public Integer healthAge;
	public Integer maxHealth;
	public Integer visRange;
	public Integer strength;
	public Action nextAction;
	public Integer actionStep;

	public OtherAgent(String name, String team)
	{
		this.name = name;
		this.team = team;
	}

	/**
	 * Check if we think this agent is damaged
	 *
	 * @return true if we think the agent is damaged (possibly disabled), false
	 *         if the agent is at full health or we just don't know
	 */
	public boolean knownDamaged()
	{
		return health != null && (health == 0 || (maxHealth != null && health < maxHealth));
	}

	/**
	 * This agent's next broadcasted action
	 *
	 * @param step The current step (to determine if the next action is out-of-date)
	 * @return the next action (if we know it) else null
	 */
	public Action next(int step)
	{
		if (this.actionStep == null || nextAction == null || !this.actionStep.equals(step)) return null;
		return nextAction;
	}

	@Override
	public String toString()
	{
		String str = "";
		str += name;
		if (team != null) str += " (" + team + ")";
		if (role != null) str += " - " + role;
		if (health != null && maxHealth != null) str += ", ❤ " + health + "/" + maxHealth + " (" + healthAge + ")";
		if (energy != null && maxEnergy != null) str += ", ⚡ " + energy + "/" + maxEnergy;
		if (visRange != null) str += ", → " + visRange;
		if (strength != null) str += ", † " + strength;
		if (position != null) str += ", " + position + " (" + healthAge + ")";
		if (nextAction != null) str += " " + nextAction + " (" + actionStep + ")";

		return str;
	}
}
