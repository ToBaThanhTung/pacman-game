package com.pacman.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PacmanComponent implements Component{
	public static final int STAY = 0;
	public static final int STAY_UP = 1;
	public static final int STAY_DOWN = 2;
	public static final int STAY_LEFT = 3;
	public static final int STAY_RIGHT= 4;
	
	public static final int MOVE_UP = 5;
	public static final int MOVE_DOWN = 6;
	public static final int MOVE_LEFT = 7;
	public static final int MOVE_RIGHT= 8;
	
	public Body body;
	
	public int curState;
	public int hp;
	
	public PacmanComponent(Body b) {
		body = b;
		curState = 0;
		
	}
	
	public Body getBody() {
		return body;
	}
}
