package com.pacman.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class GhostComponent implements Component{
	public static final int STAY = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int MOVE_LEFT = 3;
	public static final int MOVE_RIGHT= 4;
	
	public Body body;
	
	public int curState;
	public int hp;
	
	public GhostComponent(Body b) {
		body = b;
		curState = 0;
		
	}
	
	public Body getBody() {
		return body;
	}
}
