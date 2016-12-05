package redrovers;

/* the wonderful agent framework automatically manages all Agent instances,
 * which means we can't use that class to track other agents we observe.
 * Thus:
 */
public class OtherAgent
{
	public String name;
	public String team;
	public String role;
	public String position;
	public Integer energy;
	public Integer maxEnergy;
	public Integer health;
	public Integer maxHealth;
	public Integer visRange;
	public Integer strength;

	public OtherAgent(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		String str = "";
		str += name;
		if (team != null) str += " (" + team + ")";
		if (role != null) str += " - " + role;
		if (health != null && maxHealth != null) str += ", ❤ " + health + "/" + maxHealth;
		if (energy != null && maxEnergy != null) str += ", ⚡ " + energy + "/" + maxEnergy;
		if (visRange != null) str += ", → " + visRange;
		if (strength != null) str += ", † " + strength;
		if (position != null) str += ", " + position;

		return str;
	}
}