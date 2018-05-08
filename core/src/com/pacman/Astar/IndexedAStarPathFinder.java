package com.pacman.Astar;



import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.PathFinderQueue;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.TimeUtils;

public class IndexedAStarPathFinder<N> implements PathFinder<N> {
	IndexedGraph<N> graph;
	NodeRecord<N>[] nodeRecords;
	BinaryHeap<NodeRecord<N>> openList;
	NodeRecord<N> current;
	public Metrics metrics;

	/** The unique ID for each search run. Used to mark nodes. */
	private int searchId;

	public IndexedAStarPathFinder (IndexedGraph<N> graph) {
		this(graph, false);
	}

	public IndexedAStarPathFinder (IndexedGraph<N> graph, boolean calculateMetrics) {
		this.graph = graph;
		this.nodeRecords = (NodeRecord<N>[])new NodeRecord[graph.getNodeCount()];
		this.openList = new BinaryHeap<NodeRecord<N>>();
		if (calculateMetrics) this.metrics = new Metrics();
	}

	@Override
	public boolean searchConnectionPath (N startNode, N endNode, Heuristic<N> heuristic, GraphPath<Connection<N>> outPath) {

		// Perform AStar
		search(startNode, endNode, heuristic);

		// We're here if we've either found the goal, or if we've no more nodes to search, find which
		if (current.node != endNode) {
			// We've run out of nodes without finding the goal, so there's no solution
			return false;
		}

		generateConnectionPath(startNode, outPath);

		return true;
	}

	@Override
	public boolean searchNodePath (N startNode, N endNode, Heuristic<N> heuristic, GraphPath<N> outPath) {

		// Perform AStar
		search(startNode, endNode, heuristic);

		// We're here if we've either found the goal, or if we've no more nodes to search, find which
		if (current.node != endNode) {
			// We've run out of nodes without finding the goal, so there's no solution
			return false;
		}

		generateNodePath(startNode, outPath);

		return true;
	}

	protected void search (N startNode, N endNode, Heuristic<N> heuristic) {

		initSearch(startNode, endNode, heuristic);

		// Iterate through processing each node
		do {
			// Retrieve the node with smallest estimated total cost from the open list
			current = openList.pop();
			current.category = CLOSED;

			// Terminate if we reached the goal node
			if (current.node == endNode) return;

			visitChildren(endNode, heuristic);

		} while (openList.size > 0);
	}

	@Override
	public boolean search (PathFinderRequest<N> request, long timeToRun) {

		long lastTime = TimeUtils.nanoTime();

		// We have to initialize the search if the status has just changed
		if (request.statusChanged) {
			initSearch(request.startNode, request.endNode, request.heuristic);
			request.statusChanged = false;
		}

		// Iterate through processing each node
		do {

			// Check the available time
			long currentTime = TimeUtils.nanoTime();
			timeToRun -= currentTime - lastTime;
			if (timeToRun <= PathFinderQueue.TIME_TOLERANCE) return false;

			// Retrieve the node with smallest estimated total cost from the open list
			current = openList.pop();
			current.category = CLOSED;

			// Terminate if we reached the goal node; we've found a path.
			if (current.node == request.endNode) {
				request.pathFound = true;

				generateNodePath(request.startNode, request.resultPath);

				return true;
			}

			// Visit current node's children
			visitChildren(request.endNode, request.heuristic);

			// Store the current time
			lastTime = currentTime;

		} while (openList.size > 0);

		// The open list is empty and we've not found a path.
		request.pathFound = false;
		return true;
	}

	protected void initSearch (N startNode, N endNode, Heuristic<N> heuristic) {
		if (metrics != null) metrics.reset();

		// Increment the search id
		if (++searchId < 0) searchId = 1;

		// Initialize the open list
		openList.clear();

		// Initialize the record for the start node and add it to the open list
		NodeRecord<N> startRecord = getNodeRecord(startNode);
		startRecord.node = startNode;
		startRecord.connection = null;
		startRecord.costSoFar = 0;
		addToOpenList(startRecord, heuristic.estimate(startNode, endNode));

		current = null;
	}

	protected void visitChildren (N endNode, Heuristic<N> heuristic) {
		// Get current node's outgoing connections
		Array<Connection<N>> connections = graph.getConnections(current.node);

		// Loop through each connection in turn
		for (int i = 0; i < connections.size; i++) {
			if (metrics != null) metrics.visitedNodes++;

			Connection<N> connection = connections.get(i);

			// Get the cost estimate for the node
			N node = connection.getToNode();
			float nodeCost = current.costSoFar + connection.getCost();

			float nodeHeuristic;
			NodeRecord<N> nodeRecord = getNodeRecord(node);
			if (nodeRecord.category == CLOSED) { // The node is closed

				// If we didn't find a shorter route, skip
				if (nodeRecord.costSoFar <= nodeCost) continue;

				// We can use the node's old cost values to calculate its heuristic
				// without calling the possibly expensive heuristic function
				nodeHeuristic = nodeRecord.getEstimatedTotalCost() - nodeRecord.costSoFar;
			} else if (nodeRecord.category == OPEN) { // The node is open

				// If our route is no better, then skip
				if (nodeRecord.costSoFar <= nodeCost) continue;

				// Remove it from the open list (it will be re-added with the new cost)
				openList.remove(nodeRecord);

				// We can use the node's old cost values to calculate its heuristic
				// without calling the possibly expensive heuristic function
				nodeHeuristic = nodeRecord.getEstimatedTotalCost() - nodeRecord.costSoFar;
			} else { // the node is unvisited

				// We'll need to calculate the heuristic value using the function,
				// since we don't have a node record with a previously calculated value
				nodeHeuristic = heuristic.estimate(node, endNode);
			}

			// Update node record's cost and connection
			nodeRecord.costSoFar = nodeCost;
			nodeRecord.connection = connection;

			// Add it to the open list with the estimated total cost
			addToOpenList(nodeRecord, nodeCost + nodeHeuristic);
		}

	}

	protected void generateConnectionPath (N startNode, GraphPath<Connection<N>> outPath) {

		// Work back along the path, accumulating connections
		// outPath.clear();
		while (current.node != startNode) {
			outPath.add(current.connection);
			current = nodeRecords[graph.getIndex(current.connection.getFromNode())];
		}

		// Reverse the path
		outPath.reverse();
	}

	protected void generateNodePath (N startNode, GraphPath<N> outPath) {

		// Work back along the path, accumulating nodes
		// outPath.clear();
		while (current.connection != null) {
			outPath.add(current.node);
			current = nodeRecords[graph.getIndex(current.connection.getFromNode())];
		}
		outPath.add(startNode);

		// Reverse the path
		outPath.reverse();
	}

	protected void addToOpenList (NodeRecord<N> nodeRecord, float estimatedTotalCost) {
		openList.add(nodeRecord, estimatedTotalCost);
		nodeRecord.category = OPEN;
		if (metrics != null) {
			metrics.openListAdditions++;
			metrics.openListPeak = Math.max(metrics.openListPeak, openList.size);
		}
	}

	protected NodeRecord<N> getNodeRecord (N node) {
		int index = graph.getIndex(node);
		NodeRecord<N> nr = nodeRecords[index];
		if (nr != null) {
			if (nr.searchId != searchId) {
				nr.category = UNVISITED;
				nr.searchId = searchId;
			}
			return nr;
		}
		nr = nodeRecords[index] = new NodeRecord<N>();
		nr.node = node;
		nr.searchId = searchId;
		return nr;
	}

	private static final int UNVISITED = 0;
	private static final int OPEN = 1;
	private static final int CLOSED = 2;

	static class NodeRecord<N> extends BinaryHeap.Node {
		N node;

		Connection<N> connection;

		
		float costSoFar;

		int category;

		int searchId;

		public NodeRecord () {
			super(0);
		}

		public float getEstimatedTotalCost () {
			return getValue();
		}
	}


	public static class Metrics {
		public int visitedNodes;
		public int openListAdditions;
		public int openListPeak;

		public Metrics () {
		}

		public void reset () {
			visitedNodes = 0;
			openListAdditions = 0;
			openListPeak = 0;
		}
	}
}
