package com.pacman.Astar;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;

public class PathFinding {
	public Graph map;
	private PathFinder<Node> pathFinder;
	private Heuristic<Node> heuristic;
	private GraphPath<Connection<Node>> connectionPath;
	
	public PathFinding(Graph map) {
		// TODO Auto-generated constructor stub
		this.map = map;
		this.pathFinder = new IndexedAStarPathFinder<Node>(createGraph(map));
		this.heuristic = new Heuristic<Node>() {
            @Override
            public float estimate (Node node, Node endNode) {
                // Manhattan distance
                return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
            }
	     };
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
