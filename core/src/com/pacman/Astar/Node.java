package com.pacman.Astar;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node {
	public int x;
	public int y;
	public boolean isWall;
	private int index;
	private final Array<Connection<Node>> connections;
	public Node(Graph g, int x, int y) {
		this.x = x;
		this.y = y;
		this.isWall = false;
		this.index = x * g.getHeight() + y;
		this.connections = new Array<Connection<Node>>();
	}
	
	public int getIndex() {
		return index;
	}
	
	
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return (x == node.x && y == node.y);
		}
		else return false;
	}

	public Array<Connection<Node>> getConnections() {
		// TODO Auto-generated method stub
		return connections;
	}
	@Override
	public String toString() {
		return "Node: ( " + x + " " + y + " )";
	}
}
