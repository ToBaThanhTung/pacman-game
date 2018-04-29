package com.pacman.Astar;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class MyGraph implements IndexedGraph<Node>{
	Graph map;
	public MyGraph(Graph map) {
		this.map = map;
	}
	
	@Override
	public Array<Connection<Node>> getConnections(Node fromNode) {
		// TODO Auto-generated method stub
		return fromNode.getConnections();
	}

	@Override
	public int getIndex(Node node) {
		return node.getIndex();
	}

	@Override
	public int getNodeCount() {
		return map.getHeight() * map.getHeight();
	}
	
}
