package redrovers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import apltk.interpreter.data.LogicBelief;

/**
 * This class defines a collection of nodes and edges suitable for storing the
 * map of Mars.
 *
 * <p>The internal representations of nodes and edges are private since agents
 * should not rely on them. Instead define public methods for accessing
 * node/edge atrributes through the Graph object e.g. {@link nodesAtRange}.
 */
public class Graph
{
	/* A node has an id, value, and a map from IDs to edges. Also a visisted
	 * flag but that's a bit of hack that we need to think about.
	 */
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

		@Override
		public String toString()
		{
			if (value == null) return id;
			return id + "(" + value + ")";
		}
	}

	// an edge just a node and a weight (the other node on the edge is
	// determined by which node has this edge in its neighbors map).
	private class Edge
	{
		public final Node end;
		public Integer weight;

		public Edge(Node end, Integer weight)
		{
			this.end = end;
			this.weight = weight;
		}

		@Override
		public String toString()
		{
			if (weight == null) return "----" + end;
			return "--" + weight + "--" + end;
		}
	}

	// total size of graph
	public Integer total_verts;
	public Integer total_edges;
	// nodes (which contain the edges)
	private Map<String, Node> nodes;

	/**
	 * Create a graph of unknown size with no nodes or edges.
	 */
	public Graph()
	{
		this.total_verts = null;
		this.total_edges = null;
		nodes = new HashMap<String, Node>();
	}

	/** Indicate whether we know of edge between two nodes.
	 *
	 * @param id1 one node's ID
	 * @param id2 another node's ID
	 * @return true if both nodes are in the graph and have an edge between
	 *         them, otherwise false.
	 */
	public boolean hasEdge(String id1, String id2)
	{
		return nodes.containsKey(id1) && getNode(id1).hasEdge(id2);
	}

	/**
	 * Get the weight of an edge.
	 *
	 * @param id1 one node's ID
	 * @param id2 another node's ID
	 * @return the weight of the edge between the two nodes
	 * @throws NullPointerException if either node does not exist or there is
	 *         no edge between them. Use {@link hasEdge} first.
	 */
	public Integer edgeWeight(String id1, String id2)
	{
		return nodes.get(id1).neighbors.get(id2).weight;
	}

	/**
	 * Add an edge between two nodes with an unknown weight.
	 *
	 * @param id1 one node's ID
	 * @param id2 another node's ID
	 * @return true if this was a new edge (or new nodes)
	 */
	public boolean addEdge(String id1, String id2)
	{
		return addEdge(id1, id2, null);
	}

	/**
	 * Add a edge between two nodes.
	 *
	 * @param id1 one node's ID
	 * @param id2 another node's ID
	 * @param weight the weight of the edge (null if unknown)
	 * @return true if this was a new edge, new node, or new weight for an
	 *         existing edge
	 */
	public boolean addEdge(String id1, String id2, Integer weight)
	{
		// if its a new edge, or a new edge weight
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

	/**
	 * Determine how many vertices are left to discover.
	 *
	 * @return the number of undiscovered vertices or null if unknown.
	 */
	public Integer numUnknownVerts()
	{
		if (total_verts == null) return null;
		return total_verts - nodes.size();
	}

	/**
	 * Determine how many edges are left to discover
	 *
	 * @return the number of undiscovered edges or null if unknown.
	 */
	public Integer numUnknownEdges()
	{
		if (total_edges == null) return null;

		int edges = 0;
		for (Node n : nodes.values())
		{
			edges += n.neighbors.size();
		}
		// double-counts edges, so divide
		return total_edges - edges / 2;
	}

	/**
	 * Flag a node as visited.
	 *
	 * <b>TODO</b> this is hacky. We need a better way to figure which nodes
	 * might have undiscovered edges.
	 *
	 * @param id the node's ID
	 */
	public void visit(String id)
	{
		getNode(id).visited = true;
	}

	/**
	 * Check if a node is visited
	 *
	 * @param id the node's ID
	 * @return true if the node has the visited flag
	 */
	public boolean visited(String id)
	{
		return getNode(id).visited;
	}

	/**
	 * Check if a node has edges with unknown weights.
	 *
	 * @param id the node's ID
	 * @return true if the node has edges with null weight, else false
	 */
	public boolean unknownNearby(String id)
	{
		for (Edge edge : getNode(id).neighbors.values())
		{
			if (edge.weight == null) return true;
		}

		return false;
	}

	/**
	 * Get a path to follow in order to survey the graph
	 *
	 * @param sid the ID of the node the agent is at
	 * @return the path, see {@link #shortestPath(String,Function)}
	 */
	public LinkedList<String> explore(String sid)
	{
		return shortestPath(sid, (id) -> !visited(id) || unknownNearby(id));
	}

	/**
	 * Get the shortest path from node sid to node eid
	 *
	 * @param sid the ID of the node the agent is at
	 * @param eid the ID of the node to path to
	 * @return the path, see {@link #shortestPath(String,Function)}
	 */
	public LinkedList<String> shortestPath(String sid, String eid)
	{
		return shortestPath(sid, (id) -> id.equals(eid));
	}

	/**
	 * Get the shortest path to any node satisfying some condition.
	 *
	 * @param sid the ID of the node the agent is at
	 * @param destCheck a function taking a node ID and returning true if that
	 *                  node is an acceptable destination
	 * @return The path as a list of node IDs. goto each node in order. If
	 *         sid satisfies the condition, the list will be empty. If there
	 *         are no nodes satisfying the condition, returns null.
	 */
	public LinkedList<String> shortestPath(String sid, Function<String, Boolean> destCheck)
	{
		// reset all node predecessors (from previous path-finding attempts)
		for (Node n : nodes.values())
			n.pred = null;

		// init frontier for Dijkstra's algorithm
		Set<Node> frontier = new HashSet<Node>();

		// initial condition for path-finding
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
			// and remove it
			frontier.remove(closest);

			if (destCheck.apply(closest.id))
			{
				LinkedList<String> path = new LinkedList<String>();
				if (closest == start) return path;
				path.push(closest.id);

				// trace back to find just the first move to make
				while (closest.pred != start)
				{
					closest = closest.pred;
					path.push(closest.id);
				}

				return path;
			}

			// no path found, add new nodes to the frontier
			for (Edge e : closest.neighbors.values())
			{
				// check if we have a new shortest path to the node
				// TODO assume some maximum edge weight if unknown?
				if (e.weight != null && (e.end.pred == null || closest.distance + e.weight < e.end.distance))
				{
					e.end.pred = closest;
					e.end.distance = closest.distance + e.weight;
					if (!frontier.contains(e.end)) frontier.add(e.end);
				}
			}
		}

		// no nodes
		return null;
	}

	/**
	 * Get the value of a node.
	 *
	 * @param id the node's ID
	 * @return the value of the node or null if unknown
	 */
	public Integer nodeValue(String id)
	{
		return getNode(id).value;
	}

	/**
	 * Set the value of a node.
	 *
	 * @param id the node's ID
	 * @param value the new value
	 */
	public void nodeValue(String id, int value)
	{
		getNode(id).value = value;
	}

	@Override
	public String toString()
	{
		// TODO double-prints edges
		String s = "";
		for (Node node : nodes.values())
		{
			for (Edge edge : node.neighbors.values())
			{
				if (node.id.compareTo(edge.end.id) >= 0) continue;
				s += "" + node + edge + "\n";
			}
		}
		return s;
	}

	// get node with the given id (possibly creating it first)
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

	/**
	 * Get nodes that are a certain number of hops away from a node.
	 *
	 * @param id the starting node's ID
	 * @param range the number of hops away from that node (number of edges)
	 * @return a list of node IDs that are <i>exactly</i> that many hops from the starting node
	 */
	public List<String> nodesAtRange(String id, int range)
	{
		Set<Node> visited = new HashSet<Node>();
		Set<Node> next = new HashSet<Node>();
		next.add(getNode(id));

		int r = 0;

		while (r < range && !next.isEmpty())
		{
			for (Node n : next)
				visited.add(n);
			next.clear();

			for (Node n : visited)
			{
				for (Edge e : n.neighbors.values())
				{
					if (!visited.contains(e.end) && !next.contains(e.end))
						next.add(e.end);
				}
			}

			++r;
		}

		List<String> nodes = new ArrayList<String>();
		for (Node n : next)
			nodes.add(n.id);
		return nodes;
	}
}
