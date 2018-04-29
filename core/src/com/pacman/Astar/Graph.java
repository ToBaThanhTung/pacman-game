package com.pacman.Astar;

public class Graph {
	
	private Node[][] map;
	
	private final int width;
	private final int height;
	
	public Graph(int height, int width) {
		this.width = width;
		this.height = height;
		
		map = new Node[height][width];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				map[y][x]= new Node(this, x,  y);
			}
		}
	}
	
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	
	
	public Node getNode(int x, int y) {
		return map[y][x];
	}
	
	  @Override
	    public String toString() {
	        StringBuilder stringBuilder = new StringBuilder();
	        //stringBuilder.append("map :" + "\n" + width + " " + height);
	        for (int y= height - 1; y >=0 ; y--) {
	            for (int x = 0; x < width; x++) {
	            	
	               stringBuilder.append(map[y][x].isWall ? "*" : " ");
	            }
	            stringBuilder.append("\n");
	        }
	        return stringBuilder.toString();
	    }
}
