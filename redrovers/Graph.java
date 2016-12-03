package redrovers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Graph
{
	private class Node
	{
		public final String id;
		public final Map<String, Edge> neighbors;
		public Integer value;
		public boolean visited;
		// for path finding
		public int distance;
		public Node pred;

		public Node(String id)
		{
			this.id = id;
			neighbors = new HashMap<String, Edge>();
			value = null;
			visited = false;
		}

		public void addEdge(Node end, Integer weight)
		{
			if (neighbors.containsKey(end.id))
			{
				if (weight != null) neighbors.get(end.id).weight = weight;
			}
			else
			{
				neighbors.put(end.id, new Edge(end, weight));
			}
		}

		public boolean hasEdge(String id)
		{
			return neighbors.containsKey(id);
		}

		@Override
		public int hashCode()
		{
			return id.hashCode();
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == this) return true;
			if (!(obj instanceof Node)) return false;
			return ((Node)obj).id == id;
		}
	}

	private class Edge
	{
		public final Node end;
		public Integer weight;

		public Edge(Node end, Integer weight)
		{
			this.end = end;
			this.weight = weight;
		}
	}

	// total size of graph
	public Integer total_verts;
	public Integer total_edges;
	// nodes (which contain the edges)
	private Map<String, Node> nodes;

	public Graph()
	{
		this.total_verts = null;
		this.total_edges = null;
		nodes = new HashMap<String, Node>();
	}

	// do we know of an edge between nodes named id1 and id2?
	public boolean hasEdge(String id1, String id2)
	{
		return nodes.containsKey(id1) && getNode(id1).hasEdge(id2);
	}

	// get edge weight between nodes named id1 and id2
	// XXX WE MUST KNOW OF AN EDGE BETWEEN THESE NODES
	public Integer edgeWeight(String id1, String id2)
	{
		return nodes.get(id1).neighbors.get(id2).weight;
	}

	// add an edge between nodes named id1 and id2 (with unknown weight)
	public boolean addEdge(String id1, String id2)
	{
		return addEdge(id1, id2, null);
	}
	// add an edge between nodes named id1 and id2 with given weight
	public boolean addEdge(String id1, String id2, Integer weight)
	{
		if (!hasEdge(id1, id2) || (weight != null && edgeWeight(id1, id2) == null))
		{
			Node n1 = getNode(id1);
			Node n2 = getNode(id2);
			n1.addEdge(n2, weight);
			n2.addEdge(n1, weight);
			return true;
		}
		return false;
	}

	public Integer numUnknownVerts()
	{
		if (total_verts == null) return null;
		return total_verts - nodes.size();
	}

	public Integer numUnknownEdges()
	{
		if (total_edges == null) return null;

		int edges = 0;
		for (Node n : nodes.values())
		{
			edges += n.neighbors.size();
		}
		return total_edges - edges / 2;
	}

	public void visit(String id)
	{
		getNode(id).visited = true;
	}

	public boolean unknownNearby(String id)
	{
		for (Edge edge : getNode(id).neighbors.values())
		{
			if (edge.weight == null) return true;
		}

		return false;
	}

	public String explore(String sid)
	{
		for (Node n : nodes.values())
			n.pred = null;

		Set<Node> frontier = new HashSet<Node>();

		Node start = getNode(sid);

		frontier.add(start);
		start.distance = 0;
		start.pred = null;

		while (!frontier.isEmpty())
		{
			// find closest node in frontier
			Node closest = null;
			int dist = 0;
			for (Node n : frontier)
			{
				if (closest == null || n.distance < dist)
				{
					closest = n;
					dist = n.distance;
				}
			}

			frontier.remove(closest);

			// if the closest node is unexplored, go there
			// TODO inefficient, should return the whole path
			if (!closest.visited || unknownNearby(closest.id))
			{
				while (closest.pred != start)
				{
					closest = closest.pred;
				}

				return closest.id;
			}

			// find new nodes for the frontier
			for (Edge e : closest.neighbors.values())
			{
				// XXX e.weight is not null because we would have returned in that case
				// check if we have a new shortest path to the node
				if (e.end.pred == null || closest.distance + e.weight < e.end.distance)
				{
					e.end.pred = closest;
					e.end.distance = closest.distance + e.weight;
					if (!frontier.contains(e.end)) frontier.add(e.end);
				}
			}
		}

		return null;
	}

	@Override
	public String toString()
	{
		String s = "";
		for (Node node : nodes.values())
		{
			for (Edge edge : node.neighbors.values())
			{
				if (edge.weight == null)
					s += node.id + " --> " + edge.end.id + "\n";
				else
					s += node.id + " -" + edge.weight + "-> " + edge.end.id + "\n";
			}
		}
		return s;
	}

	private Node getNode(String n1)
	{
		if (n1 == null)
		{
			System.err.println("Attempt to add null node");
			for (StackTraceElement tr : Thread.currentThread().getStackTrace())
				System.err.println(tr);
			System.exit(1);
		}
		if (nodes.containsKey(n1)) return nodes.get(n1);
		nodes.put(n1, new Node(n1));
		return nodes.get(n1);
	}
}
