package com.pacman.Astar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PathFinding {
	public Graph map;
	private final PathFinder<Node> pathFinder;
	private final Heuristic<Node> heuristic;
	private final GraphPath<Connection<Node>> connectionPath;
	
	public PathFinding(Graph map) {
		// TODO Auto-generated constructor stub
		this.map = map;
		this.pathFinder = new IndexedAStarPathFinder<Node>(createGraph(map));
		this.connectionPath = new DefaultGraphPath<Connection<Node>>();
		this.heuristic = new Heuristic<Node>() {
            @Override
            public float estimate (Node node, Node endNode) {
                // Manhattan distance
                return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
            }
	     };
	}
	
	public Node findNextNode(Vector2 source, Vector2 target) {
		
		// convert to integer
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        if (map == null
               || sourceX < 0 || sourceX >= map.getWidth()
               || sourceY < 0 || sourceY >= map.getHeight()
               || targetX < 0 || targetX >= map.getWidth()
               || targetY < 0 || targetY >= map.getHeight()) {
           return null;
        }
       
        Node sourceNode = map.getNode(sourceX, sourceY);
        Node targetNode = map.getNode(targetX, targetY);
        Gdx.app.log("cur Node", sourceNode.toString()+ "  " + targetNode.toString());
        connectionPath.clear();
        pathFinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);
        System.out.println(connectionPath.getCount());
        return connectionPath.getCount() == 0 ? null : connectionPath.get(0).getToNode();
    }
	
	 private static final int[][] NEIGHBORHOOD = new int[][] {
	        new int[] {-1,  0},
	        new int[] { 0, -1},
	        new int[] { 0,  1},
	        new int[] { 1,  0}
	    };
	public static MyGraph createGraph(Graph map) {
		final int height = map.getHeight();
		final int width = map.getWidth();
		MyGraph graph  = new MyGraph(map);
		 for (int y = 0; y < height; y++) {
	            for (int x = 0; x < width; x++) {
	                Node node = map.getNode(x, y);
	                if (node.isWall) {
	                    continue;
	                } 
	                for (int offset = 0; offset < NEIGHBORHOOD.length; offset++) {
	                    int neighborX = node.x + NEIGHBORHOOD[offset][0];
	                    int neighborY = node.y + NEIGHBORHOOD[offset][1];
	                    if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
	                        Node neighbor = map.getNode(neighborX, neighborY);
	                        if (!neighbor.isWall) {
	                            // Add connection to walkable neighbor
	                            node.getConnections().add(new DefaultConnection<Node>(node, neighbor));
	                        }
	                    }
	                }
	                node.getConnections().shuffle();
	            }
	        }
	        return graph;
		
	}
	
	
}
